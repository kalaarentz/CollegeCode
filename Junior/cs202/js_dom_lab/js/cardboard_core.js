var cb = (function()  {

   var Pictures = (function() {
      const KEY = "pictures";
      
      var Picture = function(url, desc) {
         this.url = url;
         this.desc = desc;
         this.id = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
         } );
      };

      var getById = function( id ) {
         var pics = getPictures();
         for( var i=0; i<pics.length; i++) {
            if( pics[i].id === id ) {
               return i;
            }
         }
         return -1;
      }
      
      var pictures = [
         new Picture('http://www.freedigitalphotos.net/images/img/homepage/87357.jpg', 'Ducks' ),
         new Picture('http://all4desktop.com/data_images/original/4237679-images.jpg', 'Dog with bubbles'),
         new Picture('http://www.irishtimes.com/polopoly_fs/1.2678698.1465493495!/image/image.jpg_gen/derivatives/landscape_685/image.jpg', 'Jelly fish fish' ),
         new Picture('http://ak-hdl.buzzfed.com/static/enhanced/webdr05/2013/9/17/5/enhanced-buzz-1492-1379411828-15.jpg', 'Hedghog' ),
         new Picture('http://www.hindustantimes.com/Images/popup/2015/6/kungfu2.jpg', 'Panda' ),
         new Picture('http://www.menucool.com/slider/jsImgSlider/images/image-slider-1.jpg', 'Up')
      ];

      var getPictures = function() {
         var result = JSON.parse( localStorage.getItem(KEY) );
         if( !result ) {
            savePictures( pictures );
            result = pictures;
         }
         return result;
      }

      var savePictures = function( pics ) {
         localStorage.setItem( KEY, JSON.stringify( pics ) );
      }
       
 
      var add = function( url, desc ) {
         var pics = getPictures( );
         var result = new Picture( url, desc );
         pics.push( result );
         savePictures( pics );
         return result;
      }

      var deleteCard = function( id ) {
         var index = getById( id );
         if( index >= 0 ) {
            var pics = getPictures();
            pics.splice( index, 1 );
            savePictures( pics );
            return true;
         } else {
            return false;
         }
      }

      return {
         add : add,
         deleteCard : deleteCard,
         getPictures : getPictures
      }
   })();

   var urlExists = function( url ) {
      var exists = Pictures.getPictures().filter( p => p.url === url );
      return exists.length > 0;
   };

   return {
      addCard : function( url, desc, cb ) {
         if( url && desc ) {
            if( urlExists(url) ) {
               cb( null, 'this picture already exists' );
            } else {
               var newPicture = Pictures.add( url, desc );
               cb( newPicture, null );
            }
         } else {
            cb( null, 'missing either a url or a desc' );
         }
      },

      removeCard : function( id, cb ) {
         var result = Pictures.deleteCard( id );
         if( result ) {
            cb( result, null );
         } else {
            cb( null, 'invalid id' );
         }
      },

      getCards : function( cb ) {
         cb( Pictures.getPictures(), null );
      }
   }
} )();
