// WRITE THE FUNCTIONS BELOW


var tasker = (function() {
   return {
      login : function( name, passwd, cb ) {

          $.ajax({
                  url: 'http://138.49.184.143:3000/tasker/api/login?key=8f3ee7b687',
                  method: "POST",
                  //Content-Type : application/x-www-form-urlencoded,
                  data: {username: name, password: passwd},
                  success: function (result, status, xhr) {
                      cb(result, null);
                  },
                  error: function ( xhr, status, error) {
                      cb(null, error);
                  }
              }
           );
      },

      tasks : function( ownerId, cb ) {
          $.ajax( {
              url : "http://138.49.184.143:3000/tasker/api/" + ownerId + "?key=8f3ee7b687",
              method : "GET",
              success : function( result, status, xhr ) {
                  cb( result, null );
              },
              error : function( xhr, status, error ) {
                 cb( null,error.msg );
              }

          } );
      },

      add : function( ownerId, task, cb ) {
          $.ajax( {
              url : "http://138.49.184.143:3000/tasker/api/" + ownerId + "?key=8f3ee7b687",
              method : "POST",
              data : {desc : task.desc, due : task.due.toISOString(), color : task.color, complete : task.complete, id : task.id},
              success : function( result, status, xhr ) {
                  result.due = task.due;
                  cb( result, null );
              },
              error : function( xhr, status, error ) {
                  cb( null, error.msg)
              }

          } );
      },

      delete : function( ownerId, taskId, cb ) {
          $.ajax( {
              url : "http://138.49.184.143:3000/tasker/api/" + ownerId + "/" + taskId + "?key=8f3ee7b687",
              method : "DELETE",
              success : function( result, status, xhr ) {
                  cb( null );
              },
              error : function( xhr, status, error ) {
                  cb( error.msg );
              }

          } );
      },

      edit : function( ownerId, taskId, task, cb ) {
          $.ajax( {
              url : "http://138.49.184.143:3000/tasker/api/" + ownerId + "/" + taskId + "?key=8f3ee7b687",
              method : "PUT",
              data : task,
              success : function( result, status, xhr ) {
                  cb( result, null );
              },
              error : function( xhr, status, error ) {
                  cb( null, error.msg );
              }


          } );
      }
   }
})();
