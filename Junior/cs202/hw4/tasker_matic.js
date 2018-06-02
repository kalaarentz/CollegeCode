// state.user will hold the user object
// state.tasks will hold the tasks list;
var state = {
    user: null,
    tasks: []
};

/*
 MAIN METHODS THAT ARE SPECIFIED IN THE HOMEWORK!
 */
//login function that is in the tasker.html to login in the page
var login = function (event) {
    event.preventDefault();
    var name = document.getElementById("usernameInput").value;
    var password = document.getElementById("passwordInput").value;

    tasker.login(name, password, function (user, error) {
        if (user) {
            // put the username into the logout span
            var logout = document.getElementById("logout");
            var txtLogout = document.createTextNode("Welcome " + user.name);
            logout.appendChild(txtLogout);

            // switch the login to off and home to on
            var logIn = document.getElementById("login");
            logIn.setAttribute("class", "off");
            var home = document.getElementById("home");
            home.setAttribute("class", "on");

            state.user = user;
            getTasks(user);

        } else {
            alert(error);
        }
    });
};

// logout of the window
var logout = function () {


    var logIn = document.getElementById("login");
    logIn.setAttribute("class", "on");
    var home = document.getElementById("home");
    home.setAttribute("class", "off");

    // delete table
    var table = document.getElementById("tasks");
    deleteTableRows(table);

    // reset the welcome to empty string
    var logout = document.getElementById("logout").innerHTML = "";

    document.getElementById("usernameInput").value = "";
    document.getElementById("passwordInput").value = "";

    // reset user to be nothing
    state.user = null;
    state.tasks = [];
};

var addTask = function () {
    var ownerId = state.user.id;
    var description = document.getElementById("descField").value;
    var colorOfTask = document.getElementById("cField").value;
    var dueDate = document.getElementById("dField").value;

    // id for the task is the ownerId of the user
    var task = {desc: description, due: dueDate, color: colorOfTask, complete: false, id: state.user.id};
    //console.log("task object: " + task.desc +', ' + task.id +', ' + task.color + ', ' + task.due + ", " + task.complete);
    tasker.add(ownerId, task, function (task, error) {

        //add task to list in the state.task list
        // remove the table rows
        // reload the task
        if (error) {
            alert(error);
        } else {
            state.tasks.unshift(task);
            rePrintTable(state.tasks);
        }

    });
};

var getTasks = function (user) {
    var ownerId = user.id;

    tasker.tasks(ownerId, function (list, error) {
        //console.log("hello!");
        if (error) {
            alert(error);
        } else {
            state.tasks = list;
            // make a table of all the original tasks
            makeTableHeader(["Description", "Color", "Due", "Completed"]);
            makeTableRows(list);
        }
    });

};

var editTasks = function ( task, changedProperty, changedValue) {

    console.log(changedValue);
    console.log(task);

    state.tasks.forEach( function( t ) {
       if( t.id === task.id) {
           if( changedProperty === "color") {
               t.color = changedValue;
               console.log(t);
           } else if (changedProperty === "due" ) {
               dateList = changedProperty.split("/");
               newDate = new Date();

               newDate.setDate(Number(dateList[0]));
               newDate.setMonth((Number(dateList[1])-1) );
               newDate.setYear(Number(dateList[2]));

               t.due = newDate;
               console.log(t);
           } else if ( changedProperty === "complete" ) {

               if( changedValue ) {
                   t.complete = true;
               } else {
                   t.complete = false;
               }
               console.log(t);
           }
       }

    });

};

/*
 METHODS THAT CHANGE WHAT THE TASK TABLE LOOKS LIKE AND LOGOUT BUTTON

 EVENT LISTENER TYPE METHODS
 */
// inCompleteChange method, only have a list of all the tasks that are incomplete
var incompleteChange = function () {

    var checked = document.getElementById("incompleteBox").checked;

    if (checked) {
        var removedTasks = state.tasks.filter(function (task) {
            return task.complete === false;
        });

        rePrintTable(removedTasks);
    } else {
        rePrintTable(state.tasks);
    }

};

// over due method, have a list of all tasks that are not completed and past the due date
var overdueChange = function () {

    var checked = document.getElementById("overdueBox").checked;

    if (checked) {
        var removedTasks = [];
        var date = new Date();

        state.tasks.forEach(function (t) {

            if (t.due < date) {
                //console.log("due date is less then the date");
                if (t.complete === false) {
                    //console.log( " complete is false! not checked not completed");
                    removedTasks.push(t);
                }
            }
        });

        rePrintTable(removedTasks);

    } else {
        rePrintTable(state.tasks);
    }

};

var searchTextChange = function () {

    var inputString = document.getElementById("searchField").value;
    var tasksIncludeSearch = [];

    state.tasks.forEach(function (t) {

        var index = t.desc.search(inputString);
        if (index !== -1) {
            tasksIncludeSearch.push(t);
        }
    });

    rePrintTable(tasksIncludeSearch);
};

var deleteTask = function (task) {
    //console.log("delete button works!");
    //console.log(task);

    var id = state.user.id;
    var ownerId = task.id;
    console.log("owner id : " + ownerId);
    console.log("task id " + id);

    tasker.delete(ownerId, id, function (error) {
        //console.log("enter the tasker.delete function");

        if (error) {
            //console.log("cant delete something!");
            alert(error);
        } else {
            // remove the task from the state.tasks
            state.tasks = state.tasks.filter(function (t) {
                return t.id !== ownerId;
            });

            rePrintTable(state.tasks);
            //console.log(removedTask);
            //console.log("have deleted the website!");
        }

    });
};

/*
 PRINTING TABLE OF TASKS METHODS
 */

// reprints the table if you add a task or if you delete a task
var rePrintTable = function (list) {
    var table = document.getElementById("tasks");
    deleteTableRows(table);
    makeTableHeader(["Description", "Color", "Due", "Completed"]);
    makeTableRows(list);
};

// delete table rows--> will be used for when you add a new task and need to reload the tasks
var deleteTableRows = function (table) {
    while (table.firstChild.nextSibling) {
        table.removeChild(table.firstChild.nextSibling);
    }
};

// make the header for the table
var makeTableHeader = function (headers) {

    // make the header that holds the props of the task object
    var table = document.getElementById("tasks");
    var tr = document.createElement('tr');

    headers.forEach(function (h) {
        var th = document.createElement('th');
        var txt = document.createTextNode(h);

        th.appendChild(txt);
        tr.appendChild(th);
    });
    table.appendChild(tr);
};

// makes the individual rows of the task table
var makeTableRows = function (list) {
    // properties of task objects
    var prop = ["desc", "color", "due", "complete", "delete"];
    //table node
    var table = document.getElementById("tasks");

    list.forEach(function (task) {
        var tr = document.createElement('tr');
        table.appendChild(tr);

        prop.forEach(function (p) {
            var td = document.createElement('td');
            // make sure you have the correct type of input or text node
            // for the td elements of the rows!
            if (p === "color") {
                //console.log("hello" + p);
                var inputColor = document.createElement('input');
                inputColor.setAttribute('type', 'color');
                inputColor.value = task[p];

                inputColor.addEventListener("change", function() {
                   editTasks(task, "color", inputColor.value);
                });

                td.appendChild(inputColor);
            } else if (p === "due") {
                var date = task[p];
                // months are 0 -11 so add one to keep it on gregorian calender
                var month = String(date.getMonth() + 1);
                var day = String(date.getDate());
                var year = String(date.getFullYear());

                var inputDate = document.createElement('input');
                inputDate.setAttribute('type', 'text');
                inputDate.setAttribute('class', "due-Left-Align");
                inputDate.value = month + "/" + day + "/" + year;

                // edit tasks
                inputDate.addEventListener("change", function() {

                    editTasks(task, "due", inputDate.value);
                });

                td.appendChild(inputDate);
            } else if (p === "complete") {
                var inputCheck = document.createElement('input');
                // will only use span if the document is past the complete date
                var span = document.createElement('span');
                inputCheck.setAttribute('type', 'checkbox');
                inputCheck.setAttribute('class', 'complete-Left-Aligned');
                // if true then you put something in checked,
                // if not then put empty string!
                if (task[p] === true) {
                    inputCheck.setAttribute('checked', task[p]);
                }

                // if the date is less then todays date and the task is not completed, then
                // it needs to have a class added to it's complete element
                //noinspection JSDuplicatedDeclaration
                var date = new Date();
                if (task.due < date) {
                    //console.log("due date is less then the date");
                    if (task.complete === false) {
                        //console.log( " complete is false! not checked not completed");

                        span.setAttribute("class", "notCompletedInTime");

                    }
                }

                // edit tasks
                inputCheck.addEventListener("change", function() {

                    editTasks(task, "complete", inputCheck.checked);
                });

                td.appendChild(inputCheck);
                td.appendChild(span);
            } else if (p === "delete") {

                var deleteButton = document.createElement('img');
                deleteButton.setAttribute('src', 'Remove.png');
                deleteButton.setAttribute('alt', 'remove image, a large silve X in a red circle');
                deleteButton.setAttribute('class', 'deleteButtonImage');

                deleteButton.addEventListener("click", function () {
                    deleteTask(task);
                });

                td.appendChild(deleteButton);
            } else {
                // description prop
                var txt = document.createTextNode(task[p]);
                td.appendChild(txt);
            }

            tr.appendChild(td);
        });

    });

};