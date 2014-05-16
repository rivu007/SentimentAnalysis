// JavaScript Document

$(document).ready(function() {	

	//select all the a tag with name equal to modal
	$('a[name=modal]').live('click',function(e) {
		//Cancel the link behavior
		e.preventDefault();
		
		//Get the A tag
		var id = $(this).attr('href');
	
		//Get the screen height and width
		var maskHeight = $(document).height();
		var maskWidth = $(window).width();
	
		//Set heigth and width to mask to fill up the whole screen
		$('#mask').css({'width':maskWidth,'height':maskHeight});
		
		//transition effect		
		$('#mask').fadeIn(800);
		$('#mask').fadeTo("slow",0.7);	
	
		//Get the window height and width
		var winH = $(window).height();
		var winW = $(window).width();
              
		//Set the popup window to center
		$(id).css('top',  winH/2-$(id).height()/2);
		$(id).css('left', winW/2-$(id).width()/2);
	
		//transition effect
		$(id).fadeIn(1500); 
	
	});
	
	//if close button is clicked
	/*$('.window .close').click(function (e) {
		//Cancel the link behavior
		e.preventDefault();
		
		$('#mask').hide();
		$('.window').hide();
	});	*/	
	
	//if mask is clicked
	
	$('#mask').hide(function () {
		$('.chkBox').find('input').attr('checked',false);	
	});
	

	$('#mask').click(function () {
		$(this).fadeOut();
		$('.window').fadeOut();
		$('.chkBox').find('input').attr('checked',false);	
		$("#GoToDemo a").attr('name',"");
	    $("#GoToDemo a img").attr('src','image/goToDemoDisable.png');
	});
	
	

	$(window).resize(function () {
	 
 		var box = $('#boxes .window');
 
        //Get the screen height and width
        var maskHeight = $(document).height();
        var maskWidth = $(window).width();
      
        //Set height and width to mask to fill up the whole screen
        $('#mask').css({'width':maskWidth,'height':maskHeight});
               
        //Get the window height and width
        var winH = $(window).height();
        var winW = $(window).width();

        //Set the popup window to center
        box.css('top',  winH/2 - box.height()/2);
        box.css('left', winW/2 - box.width()/2);
	 
	});
	
});