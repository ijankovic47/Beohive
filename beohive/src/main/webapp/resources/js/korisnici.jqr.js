$(function() {
//	$('input[@type="text"]')[0].focus();
	$("input:text:visible:first").focus();
	
	$('#sbc-paketi-slbtn').on('click', function(){
		$box = $('.sbc-paketi-box');
		$box.toggle();
			
		$('#sbc-confrpkg-btn').on('click', function() {
			$box.hide();
		});
		
	});
	
	/*
	 * 	Sakriva odgovor o uspesnosti/neuspesnosti registracije korisnika, nakon 2000 ms
	 * */
	setTimeout(function(){
		$('.sbc-action-response').hide(300);		    	 
    }, 2000);	
	/*
	 * 	Označava odabrane pakete, provereva da li kliknuti bttn ima classu 
	 * */
	$('.sbc-paket-choose-btn').on('click', function() {
		$bttn = $(this);
		$hiddInp = $(this).next('.sbc-paket-choose-inp');
		
		if($bttn.hasClass('sbc-paket-chosen')){
			$hiddInp.prop('disabled', true);
			$bttn.removeClass('sbc-paket-chosen');
		}else{
			$bttn.addClass('sbc-paket-chosen');
			$hiddInp.prop('disabled', false);
		}
	});
});