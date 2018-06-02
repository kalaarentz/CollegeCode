var state = {
   user : null,
   tasks : null
}

function filter( ) {
   var searchText = document.getElementById('searchField').value.trim();
   var incomplete = document.getElementById('incompleteBox').checked;
   var overdue = document.getElementById('overdueBox').checked;

   var overDue = overdue ? function(task) { return (new Date().getTime() > task.due.getTime()) && !task.complete; } : t => true;
   var incomplete = incomplete ? t => !t.complete : t => true;
   var search = searchText ? t => t.desc.indexOf( searchText ) >= 0 : t => true;
   return state.tasks.filter( t => overDue(t) && incomplete(t) && search(t) );
}

function searchTextChange() {
   createTable( filter() );
}

function overdueChange() {
   createTable( filter() );   
}

function incompleteChange() {
   createTable( filter() );   
}

function deleteAllChildren(n) {
   var child = n.firstChild;
   while (child) {
      n.removeChild(child);
      child = n.firstChild;
   }
}

function createHeader() {
   var row = document.createElement('tr');
   row.innerHTML = "<th>Description</th><th>Color</th><th>Due</th><th>Completed</th><th></th>";
   return row;
};

function updateTableCell( id ) {
   var task = tasks.filter( function( t ) { return t.id === id; } )[0];
   var td = document.getElementById( id );
   task.desc = td.firstChild.value;
   deleteAllChildren( td ) ;
   td.innerHTML = task.desc;
}

var deleteTask = function( taskId ) {
   tasker.delete( state.user.id, taskId, function( result, err ) {
      if( err ) {
         alert( err );
      } else {
         tasker.tasks( state.user.id, function( tasklist, err ) {
            if( err ) {
               alert( err );
            } else {
               state.tasks = tasklist;
               createTable( state.tasks );
            }
         } );
      }
   } );
}

var addTask = function( ) {
   var desc = document.getElementById('descField').value;
   var color = document.getElementById('cField').value;
   var due = document.getElementById('dField').value;
   var complete = false;

   var components = due.split(/\D/);
   due = new Date( components[0], --components[1], components[2] );

   tasker.add( state.user.id, { desc : desc, due : due, color : color, complete : complete, ownerId : state.user.id }, function( task, err ) {
         tasker.tasks( state.user.id, function( tasklist, err ) {
            if( err ) {
               alert( err );
            } else {
               state.tasks = tasklist;
               createTable( state.tasks );
            }
         } );
   } );
};

var toggleOn = function( node ) {
   var status = node.getAttribute('class');
   status = status === 'on' ? 'off' : 'on';
   node.setAttribute( 'class', status );
};

var logout = function( ) {
   tasker.logout();
   var node = document.getElementById('login');
   toggleOn( node );
   node = document.getElementById('home');
   toggleOn( node );
   state.user = null;
}

var login = function(evt) {
   evt.preventDefault(); // THIS IS IMPORTANT
   var input1 = document.getElementById('usernameInput');
   var input2 = document.getElementById('passwordInput');
   var name = input1.value;
   var password = input2.value;
   input1.value = '';
   input2.value = '';

   tasker.login( name, password, function( usr, err ) {
      if( err ) {
         alert( err );
      } else {
         window.tasker.state = { user : usr };
         state.user = usr;
         document.getElementById('logout').innerHTML = 'Welcome ' + state.user.name;
         toggleOn( document.getElementById('login') ); // HIDE LOGIN
         toggleOn( document.getElementById('home') );  // SHOW TASK LIST       
         tasker.tasks( state.user.id, function( tasklist, err ) {
            if( err ) {
               alert( err );
            } else {
               state.tasks = tasklist;
               createTable( state.tasks );
            }
         } );
      }
   } );
}

function updateTask( task, changes, property ) {


   tasker.edit( state.user.id, task.id, changes, function( updatedTask, error ) {
      if( error) {
         alert(error);
      } else {
         // if( property === 'due') {
         //    task[property] = updatedTask[property]
         // }
         task[ property ] = updatedTask[ property ];
         createTable( filter( ) );
      }
   } );
}

function createTable(tasks) {
   var table = document.getElementById('tasks');
   deleteAllChildren(table);
   table.appendChild(createHeader());

   tasks.forEach( function(task) { 
      var row = document.createElement('tr');
      var descCell = document.createElement('td');
      descCell.appendChild( document.createTextNode( task.desc ) );

      var colorCell = document.createElement('td');
      var colorInput = document.createElement('input');
      colorInput.setAttribute('type', 'color' );
      colorInput.setAttribute('class', 'form-control');
      colorInput.value = task.color;
      colorInput.addEventListener('change', function() {
          updateTask( task, { color : colorInput.value }, 'color' );
      } );
      colorCell.appendChild( colorInput );

      var dueCell = document.createElement('td');
      var dueInput = document.createElement('input');
      dueInput.setAttribute('type', 'date');
      dueInput.value = task.due.substring(0, 10);

      dueInput.addEventListener( 'change', function() {
          //console.log("Value of the date: " + dueInput.value);
          updateTask( task, { due : dueInput.value }, 'due' );
      } );
      dueCell.appendChild( dueInput );
      
      var completedCell = document.createElement('td');
      var completedInput = document.createElement('input');
      var overdueSpan = document.createElement('span');
      completedInput.setAttribute('type', 'checkbox' );
      completedInput.checked = task.complete;
      completedInput.addEventListener('change', function() {
          updateTask( task, { 'complete' : Boolean(completedInput.checked) }, 'complete' );
      } );
      completedCell.appendChild( completedInput );

      var overDue = ((new Date() - task.due ) > 0) && !task.complete;
      overdueSpan.setAttribute('class', overDue ? 'od' : '' );
      completedCell.appendChild( overdueSpan );

      var killCell = document.createElement('td');
      var killButton = document.createElement('span');
      killButton.setAttribute('class', 'btn btn-sm btn-danger' );
      killButton.innerHTML = '<span class="glyphicon glyphicon-trash"></span>';
      killButton.addEventListener( 'click', function() {
          deleteTask( task.id );
      });
      killCell.appendChild( killButton );

      row.appendChild(descCell);
      row.appendChild(colorCell);
      row.appendChild(dueCell);
      row.appendChild(completedCell);
      row.appendChild(killCell);
      
      table.appendChild( row );
   } );
}