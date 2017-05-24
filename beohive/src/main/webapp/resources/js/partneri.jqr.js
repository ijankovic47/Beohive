$(function() {
    console.log( "ready!" );
    var ctxPath = $('#ctx-path').val();
       
    $('#par-add-btn').on('click', function(){
    	$('#par-add-tr').toggle();
    });
    
    $('table').on('click', '.par-edit-btn', function(evt){		// vezuje event 'onClick' za edit button sa klasom '.opr-edit-btn'
    	evt.preventDefault();
    	var $er = $('#par-edit-tr').clone();			// u objekat $er smešta TR (red sa formom) kog ćemo koristiti i umetnuti kao upd formu.    	
    	var csrf = getCsrfParams();
    	var id = $(this).attr('title');					// uzima ID operatera na kom je izvršen klik na edit button    	
    	var $or = $(this).parent().parent();			//  u objekat $or smešta originalni TR operatera koji se edituje
    	
    	toggleDisableButton($('.par-edit-btn'), true);
    	toggleDisableButton($('#edit-update-btn'),true);                                                     // disabluj sve ostale edit buttone
    	$or.replaceWith($er.show(300));
    	
    	$('#edit-update-btn').on('click', function(){
            
    		var $ur = $(this).parent().parent();
    		updateOpr(id, $ur);
    		
    	});
    	
    	$('.edit-cancel-btn').on('click',function(evt){
    		evt.preventDefault();
    		$(this).parent().parent().replaceWith($or);			// na click Cancel buttona umesto update forme stavlja originalni(pređašnji) red gore zapamćen
    		toggleDisableButton($('.par-edit-btn'), false);		// edeble sve ostale edit buttone
        }); 
    	  	    	
    	$.ajax({
    		url : ctxPath+"/partner/getPartner?id="+id,
    		type : 'GET',
    		dataType: 'json',
    		contentType : 'application/json; charset=utf-8',
    		success: function(data){    
    			console.log('Edit button pozvan za operatera: '+data.name);
    			$('#edit-id').html(data.id);
    			$('#edit-name').val(data.name);
    			$("#edit-prefix").html(data.prefix);
    			$("#edit-maxNoOp").val(data.maxNoOp);
    			$("#edit-status").val(data.status);
    			toggleDisableButton($('#edit-update-btn'),false); 
    		},
    		error:function(er, st, msg) { 
    			console.log("ERROR: ", msg);
    			console.log("ER: ", er);
    			
    	    }
    	});
    });   
  
}); // KRAJ .READY() METODE
/*
 * Update operatera preko ajax requesta, uzima vrednosti iz inputa i pravi json objekat
 * */
function updateOpr(id, $ur){
	var ctxPath = $('#ctx-path').val();
	var dataForUpd = {};
	var csrf = getCsrfParams();
		
	dataForUpd['id'] = id; 
	dataForUpd['prefix']=$('#edit-prefix').html();
	dataForUpd['name'] = $('#edit-name').val();
	dataForUpd['maxNoOp'] = $('#edit-maxNoOp').val();
	dataForUpd['status'] = $('#edit-status').val();
	console.log(dataForUpd['prefix']);
	
	$.ajax({
		url : ctxPath+'/partner/updatePartner',
		type : 'POST',
		dataType: 'text',//ocekuje u response json, response body konvertuje u json
		contentType : 'application/json; charset=utf-8',  //ovo je potrebno kada saljemo objekat post metodom
		data : JSON.stringify(dataForUpd),
		beforeSend: function(xhr) {
            xhr.setRequestHeader(csrf[0], csrf[1]);
        },
		success : function(data) {

			var id = dataForUpd['id'];
			var status = (dataForUpd['status']=='1') ? 'Aktivan' : 'Neaktivan';
			var nr = '<tr id="row-'+id+'"><td>'+id+'</td><td>'+dataForUpd['prefix']+'</td><td>'+dataForUpd['name']+'</td><td>'+dataForUpd['maxNoOp']+'</td>'
					 +'<td>'+status+'</td><td><button type="button" id="edit-'+id+'" class="par-edit-btn" title="'+id+'">Izmeni</button></td>'
					 +'<td><button type="button" id="edit-'+id+'" class="par-showOp-btn" title="'+id+'">Prikazi operatere</button></td>'
					 +'<td><a href="'+ctxPath+'/cenovnik/paketiPrikaz?id='+id+'"><button>Cenovnik</button></a></td>';
			        
			$ur.replaceWith(nr);
			$('#row-'+id+'').css('border-color','#BAD696');
			alert("Partner id:"+id+" uspesno izmenjen!");
			toggleDisableButton($('.par-edit-btn'), false);
		},	
		error:function(er, st, msg) { 
			toggleDisableButton($('.par-edit-btn'), true);
			alert(er.responseText);
			console.log("ERROR: "+ msg);
			
	    }
	})
}
/*
 *  Uzima vrednosti iz meta tagova na stranici i vraća parametre za CSFR request, neophodan za Spring Security.
 * */
function getCsrfParams() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	return [ header, token ];
}
/*
 * funkcija disable / enable button koji je prosleđen kao prvi argument, drugi argument je tipa boolean (true = enable, false = disable button)
 * */
function toggleDisableButton($btn, disabled){
	$($btn).attr('disabled', disabled);
}
function validateLength(inputElement, min, max) {

    var inputText = inputElement.value;
    console.log(inputText);
    if (inputText.length < min || inputText.length > max) {
        inputElement.style = "border-color: red;";
        return false;
    }
    else {
        inputElement.style = "border-color: none;";
        return true;
    }
}
function validateForm(){
	
	var name = document.forms["registracija"]["name"];
	if(!validateLength(name,5,30)){
		return false;
	}
	var rpefix = document.forms["registracija"]["prefix"];
	if(!validateLength(prefix,5,10)){
		return false;
	}
}
function validateLength2(inputElement, min, max) {

    var inputText = inputElement.val();
    console.log(inputText);
    if (inputText.length < min || inputText.length > max) {
        inputElement.style = "border-color: red;";
        return false;
    }
    else {
        inputElement.style = "border-color: none;";
        return true;
    }
}
function btnClick($btn){
	$($btn).trigger('click');
}

$('table').on('click', '.par-showOp-btn', function(evt){	
	var ctxPath = $('#ctx-path').val();
	evt.preventDefault();			 	
	var csrf = getCsrfParams();
	var id = $(this).attr('title');		
	var div=$('#list')
	
	toggleDisableButton($('.par-showOp-btn'), true);
 	
	$.ajax({
		url : ctxPath+"/partner/getPartner?id="+id,
		type : 'GET',
		dataType: 'json',
		//contentType : 'application/json; charset=utf-8',
		
		success: function(data){    
			var list='<h1>Operateri</h1><ul>';
			for(i=0;i<data.operators.length;i++){
				var status;
				if(data.operators[i].active==0){
					status='Neaktivan';
				}
				else{
					status='Aktivan';
				}
			
				list+='<li>'+data.operators[i].ime+' status: '+status+' </li>'
			}
			list+='</ul>';
			
			div.html(list);
					
			toggleDisableButton($('.par-showOp-btn'),false); 
		},
		error:function(er, st, msg) { 
			console.log("ERROR: ", msg);
			console.log("ER: ", er);
	    }
	});
});   





