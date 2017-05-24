$(function() {
	
	
	$('input:radio').click(function(){
	
		
		if(this.value=='O' || this.value=='I'){
			
			var name=$(this).prop('name');
			var index=name.charAt(7);
			var doubleDigit=name.charAt(8);
			var int=parseInt(doubleDigit);
			if(int>=0){
				index=index+int.toString();    //ako je broj dvocifren
			}
			
			$('#cena'+index).prop('disabled', true);
			
			if($('input[name=tip3]:checked').val()!='D'||$('input[name=tip4]:checked').val()!='D'){
				$('#dpc1').prop('disabled', true);
			}
			if($('input[name=tip3]:checked').val()!=='D'||$('input[name=tip6]:checked').val()!='D'){
				$('#dpc2').prop('disabled', true);
			}
			if($('input[name=tip4]:checked').val()!='D'||$('input[name=tip6]:checked').val()!='D'){
				$('#dpc3').prop('disabled', true);
			}
			if($('input[name=tip3]:checked').val()!='D'||$('input[name=tip4]:checked').val()!='D'||$('input[name=tip6]:checked').val()!='D'){
				$('#dpc4').prop('disabled', true);
			}
		}
         if(this.value=='D' || this.value=='P'){
			
			var name=$(this).prop('name');
			var index=name.charAt(7);
			var doubleDigit=name.charAt(8);
			var int=parseInt(doubleDigit);
			if(int>=0){
				index=index+int.toString();
			}
			$('#cena'+index).prop('disabled', false);
			
			if($('input[name=tip3]:checked').val()=='D'&&$('input[name=tip4]:checked').val()=='D'){
				$('#dpc1').prop('disabled', false);
			}
			if($('input[name=tip3]:checked').val()=='D'&&$('input[name=tip6]:checked').val()=='D'){
				$('#dpc2').prop('disabled', false);
			}
			if($('input[name=tip4]:checked').val()=='D'&&$('input[name=tip6]:checked').val()=='D'){
				$('#dpc3').prop('disabled', false);
			}
			if($('input[name=tip3]:checked').val()=='D'&&$('input[name=tip4]:checked').val()=='D'&&$('input[name=tip6]:checked').val()=='D'){
				$('#dpc4').prop('disabled', false);
			}
			
		}
         
         
	});
	
});

$('#showIstorija').on('click', function(){
	$('#istorija').toggle();
});
function btnClick($btn){
	$($btn).trigger('click');
}
function f(){
	$('#form').css("display", 'none');
}