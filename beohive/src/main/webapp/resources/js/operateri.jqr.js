$(function() {
    console.log( "ready!" );
    setCtxPath();
    
    $('#opr-partn-select').on('change', function(e){
    	e.preventDefault();
    	var prefix = $(this).val();    	
    	var uri = getCtxPath()+'/operater/'+prefix+'/operateri';
    	
    	window.location.href = uri;
    });
    
    
    $('#opr-add-btn').on('click', function(){
    	$('#opr-add-tr').toggle();
    });
    
    $('#opr-reg-btn').on('click', function(){
    	var $rfrm = $('#opr-reg-form');    	
    	addNewOperator($rfrm);
    	$('.opr-err-row').remove();
    });
    
    $('table').on('click', '.opr-edit-btn', function(evt){		// vezuje event 'onClick' za edit button sa klasom '.opr-edit-btn'
    	evt.preventDefault();
    	var $er = $('#opr-edit-tr').clone();			// u objekat $er smešta TR (red sa formom) kog ćemo koristiti i umetnuti kao upd formu.    	
    	var id = $(this).attr('title');					// uzima ID operatera na kom je izvršen klik na edit button    	
    	var $or = $(this).parent().parent();			//  objekat $or smešta originalni TR operatera koji se edituje
    	var $frm = $('#upd-form-row');
    	
    	toggleDisableButton($('.opr-edit-btn'), true);		// disabluj sve ostale edit buttone
    	
    	$(this).parent().parent().replaceWith($er.show(300));	
    	
    	getDataForUpd(id, $frm);						// poziva f-ju getDataForUpd() prosleđuje ID operateri čije podatke trebamo i formu
    	
    	$('#edit-update-btn').on('click', function(){
    		var $ur = $(this).parent().parent();    		
    		$('.opr-err-row').remove();
    		updateOpr(id, $ur);
    		toggleDisableButton($('.opr-edit-btn'), false);
    	});
    	
    	$('.edit-cancel-btn').on('click',function(evt){
    		evt.preventDefault();
    		$(this).parent().parent().replaceWith($or);			// na click Cancel buttona umesto update forme stavlja originalni(pređašnji) red gore zapamćen
    		toggleDisableButton($('.opr-edit-btn'), false);		// edeble sve ostale edit buttone
    		$('.opr-err-row').remove();
        }); 
    	  	    	
    	
    });    
    /*	ON DELETE BUTTON
     * */
    $('table').on('click','.opr-del-btn', function(){
    	var $dlr = $(this).parent().parent();
    	var id = $(this).attr('title');
    	toggleDisableButton($('.opr-del-btn'), true);
    	$('.del-dialog').show(function(){
    		$('#del-btn-yes').on('click', function(evt){
    			evt.preventDefault();
    			deleteOpr($dlr, id);
    		});
    		$('#del-btn-no').on('click', function(){
    			$('.del-dialog').hide();
    			toggleDisableButton($('.opr-del-btn'), false);
    		});   		
    	});    	
    });
    /*
     * 	CHANGE PASSWORD
     * */
    $('.password-edit').on('click', function(){
    	$chpasswd_box =	$('.opr-chpasswd-box');
    	$chpasswd_box.toggle();
    	var id = $(this).prop('title');
    	var uri = getCtxPath()+'/operater/chpasswd/'+id;
 //   	window.open(uri, "", "width=350,height=300, top=300,left=500,location=no,resizable=no,menubar=no");    
    	
    	$('#passwd-save-btn').on('click', function(){
        	changePasswordForOpr(id);
    	});
    	
    	$('.close-icon-box').on('click', function(){
    		$chpasswd_box.hide();
    		$('.opr-chpasswd-msg').hide();
    	});
    });
    
    
    
}); // KRAJ .READY() METODE
var ctxPath;

function setCtxPath(){
	ctxPath = $('#ctx-path').val();
}
function getCtxPath(){
	return ctxPath;
}
/*
 * 	Uzima objekate Operater iz baze i popunjava upd formu 
 * */
function getDataForUpd(id, $frm){
	toggleDisableButton($('.upd-btn'), true);	// disabluj UPDATE button kad se pojavi
	$.ajax({
		url : getCtxPath()+'/operater/edit/'+id,
		type : 'GET',
		dataType: 'json',
		success: function(data){
			console.log('Edit button pozvan za operatera: '+data.username);
			console.log(data.partner);
			
			$('#edit-id').val(data.id);
			$('#edit-ime').val(data.ime);
			$("#edit-prezime").val(data.prezime);
			$("#edit-username").val(data.username);
			$("#edit-password").val(data.password);
			$("#edit-email").val(data.email);
			$("#edit-active").val(data.active);
			$("#edit-partner").val(data.partner.id);			
		//	populateUpdform(data, $frm);
			
			toggleDisableButton($('.upd-btn'), false);
		},
		error:function(er, st, msg) { 
			console.log("ERROR: ", msg);
			console.log("ER: ", er);
	    }
	});
}
/*
 * UPDATE operatera preko ajax requesta, uzima vrednosti iz inputa i pravi json objekat
 * */
function updateOpr(id, $ur){
	var dataForUpd = {};
	var csrf = getCsrfParams();
		
	dataForUpd['id'] = id; 
	dataForUpd['ime'] = $('#edit-ime').val();
	dataForUpd['prezime'] = $('#edit-prezime').val();
	dataForUpd['username'] = $('#edit-username').val();
	dataForUpd["password"] = $('#edit-password').val();
	dataForUpd['email'] = $('#edit-email').val();
	dataForUpd['active'] = $('#edit-active').val();
	var partId = $('#edit-partner').val(); 
	dataForUpd['partner'] = {'id':partId};
	
	$.ajax({
		url : getCtxPath()+'/operater/update/'+id,
		type : 'POST',
		dataType: 'json',
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(dataForUpd),
		beforeSend: function(xhr) {
            xhr.setRequestHeader(csrf[0], csrf[1]);
        },
		success : function(data) {
			console.log('Operater: '+data[0].username+' uspešno UPDATE-ovan');
			var id = data[0].id;
			var status = (data[0].active=='1') ? 'aktivan' : 'neaktivan';
			var $nr = '<tr id="row-'+id+'"><td>'+id+'</td><td>'+data[0].ime+'</td><td>'+data[0].prezime+'</td><td>'+data[0].username+'</td><td>********</td>'
					 +'<td>'+data[0].email+'</td><td>'+data[0].partner.name+'</td><td>'+status+'</td><td>'+data[0].role+'</td>'
					 +'<td><button type="button" id="edit-'+id+'" class="opr-edit-btn" title="'+id+'">Edit</button></td>'
					 +'<td><button type="button" class="opr-del-btn" title="'+id+'">Del</button></td></tr>';
			console.log($nr);
			$ur.replaceWith($nr);
			
			$('#row-'+id+'').addClass('highlight').delay(2000).queue(function() {
                	$(this).removeClass('highlight');
                	$(this).dequeue();
            });
			
		},	
		error:function(jqXhr, st, msg) { 
			console.log("ERROR: ", msg);
			displayErrorRow(jqXhr, $('#opr-edit-tr'));
	    }
	})
}
/*
 * 	ADD NEW OPERATOR AND FIELDS VALIDATION
 * */
function addNewOperator($rfrm){
	var csrf = getCsrfParams();	
	var newOpr = {};
	
	newOpr['ime'] = $('#ime').val();
	newOpr['prezime'] = $('#prezime').val();
	newOpr['username'] = $('#username').val();
	newOpr["password"] = $('#password').val();
	newOpr['email'] = $('#email').val();
	newOpr['active'] = $('#active').val();
	var partId = $('#opr-add-select').val();	
	newOpr['partner'] = {'id':partId};
	newOpr['role']= $('#opr-role-select').val();
	
//	console.log(JSON.stringify(newOpr));
	
	$.ajax({
		url: getCtxPath()+'/operater/register',
		type: 'POST',
		dataType: 'json',
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(newOpr),
		beforeSend: function(xhr) {
            xhr.setRequestHeader(csrf[0], csrf[1]);
        },
        success : function(data) {
        	console.log('Partner: '+data[0].partner);
        	var id = data[0].id;
        	var status = (data[0].active=='1') ? 'aktivan' : 'neaktivan';
        	var nr = '<tr id="opr-'+id+'"><td>'+id+'</td><td>'+data[0].ime+'</td><td>'+data[0].prezime+'</td><td>'+data[0].username+'</td><td>********</td>'
			 +'<td>'+data[0].email+'</td><td>'+data[0].partner.name+'</td><td>'+status+'</td><td>'+data[0].role+'</td>'
			 +'<td><button type="button" id="edit-'+id+'" class="opr-edit-btn" title="'+id+'">Edit</button></td>'
			 +'<td><button type="button" class="opr-del-btn" title="'+id+'">Del</button></td></tr>';
        	
        	$('table > tbody > #opr-add-tr').after(nr);			// dodaj red sa novim operaterom na početak tabele
        	
        	$('#opr-'+id+'').addClass('highlight').delay(2000).queue(function() {
            	$(this).removeClass('highlight');
            	$(this).dequeue();
        });
        	$('#opr-add-tr').hide();
        	$rfrm.trigger('reset');
        },
        error:function(jqXhr, st, msg) { 
			console.log("ERROR: ", msg);
			displayErrorRow(jqXhr, $('#opr-add-tr'));				
	    }
	});
}
/*
 * 	DELETE operater by id
 * */
function deleteOpr($dlr, id){
	
	$.ajax({
		url : getCtxPath()+'/operater/delete/'+id,
		type : 'GET',
		dataType: 'text',
		success: function(data){
			console.log('Resp; '+data);
			toggleDisableButton($('.opr-del-btn'), false);
			$('.del-dialog').hide();
			$dlr.remove();
		},
		error:function(er, st, msg) { 
			console.log("ERROR: ", msg);
			console.log("ER: ", er);
	    }
	});
}
/*	Prikazuje porkuke o neispravno unetim podacima na poljima, za svaki neispravni input prikazuje se odg poruka
 * i prikazuje se u novom kreiranom redu ispod.
 * */
function displayErrorRow(jqXhr, $after){
	var errs = jqXhr.responseJSON;	
	console.log(errs);
	var errms = new Array('','','','','','','');				// niz sa 7 praznih karaktera
	
	$.each(errs, function(i){
		  switch (errs[i].field) {
			case 'ime':
				errms[0] = errs[i].defaultMessage;
				break;
			case 'prezime':
				errms[1] = errs[i].defaultMessage;
				break;
			case 'username':
				errms[2] = errs[i].defaultMessage;
				break;
			case 'password':
				errms[3] = errs[i].defaultMessage;
				break;
			case 'email':
				errms[4] = errs[i].defaultMessage;
				break;
			case 'partner.id':
				errms[5] = errs[i].defaultMessage;
				break;
			}
	});	
	var errw = '<tr class="opr-err-row"><td></td><td>'+errms[0]+'</td><td>'+errms[1]+'</td>'
				+'<td>'+errms[2]+'</td><td>'+errms[3]+'</td>'
				+'<td>'+errms[4]+'</td><td>'+errms[5]+'</td><td></td><td colspan="2"></td></tr>';
	
	$after.after(errw);				// postavlja red tabele sa ispisanim greškama ispod reda za unos novog itema	
}
/*
 * 	CHANGE PASWORD FOR OPERATER
 * */
function changePasswordForOpr(id){
	var csrf = getCsrfParams();	
	var $msgBox = $('.opr-chpasswd-msg');
	var uri = '/operater/chpasswd/'+id;
	var data = $('#passwd-ch-frm').serialize();
	
	 $.ajax({
		 url: getCtxPath()+uri,
		 type: 'POST',
		 data: data,
		 beforeSend: function(xhr) {
	            xhr.setRequestHeader(csrf[0], csrf[1]);
		 },
		 success: function(msg){			 	
			 $msgBox.html('Lozinka uspešno promenjena.').removeClass('error-msg').addClass('success-msg').show();			 
		     setTimeout(function(){
		    	 $('.opr-chpasswd-box').removeClass('success-msg').hide();		    	 
		     }, 2000);		        
		 },
		 error:function(jqXhr, st, msg) {			
			$msgBox.html(jqXhr.responseText).removeClass('success-msg').addClass('error-msg').show();			
			console.log("ERROR: ", msg);    
			console.log("ERROR: ", jqXhr);    
		 }
	 });
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

function populateUpdform(data, $frm){
	$.each(data, function(key, value){
		  console.log("K: "+key+" / V: "+value);
		  $('[name='+key+']', $frm).val(value);				  
	  });
}