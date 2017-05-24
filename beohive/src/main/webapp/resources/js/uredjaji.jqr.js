$(function() {
    console.log( "ready!" );
    setCtxPath()
       
   $('#ur-add-btn').on('click', function(){
	$('#forma').toggle();
});
    
    $('table').on('click', '.ur-edit-btn', function(evt){		// vezuje event 'onClick' za edit button sa klasom '.opr-edit-btn'
    	
    	toggleDisableButton($('.ur-edit-btn'), true);
    	toggleDisableButton($('#edit-update-btn'), true);
    	evt.preventDefault();
    	var $er = $('#ur-edit-tr').clone();			// u objekat $er smešta TR (red sa formom) kog ćemo koristiti i umetnuti kao upd formu.    	
    	var id = $(this).attr('title');					// uzima ID operatera na kom je izvršen klik na edit button    	
    	var $or = $(this).parent().parent();			//  objekat $or smešta originalni TR operatera koji se edituje
  
    	
    			// disabluj sve ostale edit buttone
    	
    	$or.replaceWith($er.show(300));	
    	
    	//getDataForUpd(id, $frm);						// poziva f-ju getDataForUpd() prosleđuje ID operateri čije podatke trebamo i formu
    	console.log(ctxPath);
    	$.ajax({   	
    		url : ctxPath+"/uredjaj/getUredjaj?id="+id,
    		type : 'GET',
    		dataType: 'json',
    		success: function(data){    
    			console.log(data);
    			console.log(data.partner);
    			console.log(data.partner.id);
    			$('#edit-id').html(data.id);
    			$('#edit-sn').val(data.sn);
    			$("#edit-mac").val(data.macAdresa);
    			$("#edit-model").val(data.model.id);
    			$("#edit-partner").val(data.partner.id);
    			$("#edit-status").val(data.status);
    			toggleDisableButton($('#edit-update-btn'),false); 
    		
    		},
    		error:function(er, st, msg) { 
    			console.log("ERROR: ", msg);
    			console.log("ER: ", er);
    			
    	    }
    	});
    	
    	$('#edit-update-btn').on('click', function(){
    		var $ur = $(this).parent().parent();    		
    		updateOpr(id, $ur);
    	});
    	
    	$('.edit-cancel-btn').on('click',function(evt){
    		evt.preventDefault();
    		$(this).parent().parent().replaceWith($or);			// na click Cancel buttona umesto update forme stavlja originalni(pređašnji) red gore zapamćen
    		toggleDisableButton($('.ur-edit-btn'), false);		// edeble sve ostale edit buttone
        }); 
    	
    	    	
    	 
    });  
    $('table').on('click','.ur-del-btn',function(evt){
    	var $dlr = $(this).parent().parent();
    	var id = $(this).attr('title');
    	deleteOpr($dlr, id);
    
    });	 
    /*	ON DELETE BUTTON
     * */
   
    
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
/**function getDataForUpd(id, $frm){
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
 * Update operatera preko ajax requesta, uzima vrednosti iz inputa i pravi json objekat
 * */
function updateOpr(id, $ur){
	
	toggleDisableButton($('#edit-update-btn'), true);

	var dataForUpd = {};
	var csrf = getCsrfParams();
		
	dataForUpd['id'] = id; 
	dataForUpd['sn'] = $('#edit-sn').val();
	dataForUpd['macAdresa'] = $('#edit-mac').val();
	var partId = $('#edit-partner').val();
	var modelId = $('#edit-model').val();
	var partnerLabel=$('#edit-partner option:selected').text();
	var modelLabel=$('#edit-model option:selected').text();
	dataForUpd['model'] = {'id':modelId};
	dataForUpd['partner'] = {'id':partId};
	dataForUpd['status'] = $('#edit-status').val();

	$.ajax({
		url : getCtxPath()+'/uredjaj/updateUredjaj',
		type : 'POST',
		dataType: 'text',
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(dataForUpd),
		beforeSend: function(xhr) {
            xhr.setRequestHeader(csrf[0], csrf[1]);
        },
		success : function(data) {
		
			var id = dataForUpd['id'];
			var status = (dataForUpd['status']=='1') ? 'Aktivan' : 'Neaktivan';
			var nr = '<tr id="row-'+id+'"><td>'+id+'</td><td>'+dataForUpd['sn']+'</td><td>'+dataForUpd['macAdresa']+'</td><td>'+modelLabel+
			'</td><td>'+partnerLabel+'</td><td>'+status+'</td><td><button type="button" id="edit-'+id+'" class="ur-edit-btn" title="'+id+'">Izmeni</button></td>'
					 +'<td><button type="button" id="del-'+id+'" class="ur-del-btn" title="'+id+'">Obrisi</button></td></tr>';
	
			$ur.replaceWith(nr);
			toggleDisableButton($('#edit-update-btn'), false);
			toggleDisableButton($('.ur-edit-btn'), false);
			alert("Uredjaj uspesno izmenjen !");
		},	
		error:function(er, st, msg) { 
			toggleDisableButton($('#edit-update-btn'), false);
			alert(er.responseText);
			console.log(er.responseText)
			
	    }
	})
}
/*
 * 	ADD NEW DEVICE AND FIELDS VALIDATION
 * *//**
function dodajNoviUredjaj($rfrm){
	var csrf = getCsrfParams();
	
	var newDev = {};
	
	newDev['sn'] = $('#dev-sn-inp').val();
	newDev['macAdresa'] = $('#dev-mac-inp').val();
	newDev['model'] = $('#dev-model-inp').val();
	var partId = $('#opr-add-select').val();	
	newDev['partner'] = {'id':partId};
	
	//alert(JSON.stringify(newDev));
	
	$.ajax({
		url: getCtxPath()+'/uredjaj/register',
		type: 'POST',
		dataType: 'json',
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(newDev),
		beforeSend: function(xhr) {
            xhr.setRequestHeader(csrf[0], csrf[1]);
        },
        success : function(data) {
        	console.log(data);
//        	var id = data[0].id;
//        	var status = (data[0].active=='1') ? 'aktivan' : 'neaktivan';
//        	var nr = '<tr><td>'+id+'</td><td>'+data[0].ime+'</td><td>'+data[0].prezime+'</td><td>'+data[0].username+'</td><td>********</td>'
//			 +'<td>'+data[0].email+'</td><td>'+data[0].partner.name+'</td><td>'+status+'</td><td><button type="button" id="edit-'+id+'" class="opr-edit-btn" title="'+id+'">Edit</button></td>'
//			 +'<td><button type="button" id="opr-del-btn" >Del</button></td></tr>';
//        	
//        	$('table > tbody > #opr-add-tr').after(nr);			// dodaj red sa novim operaterom na početak tabele
//        	$('#opr-add-tr').hide();
//        	$rfrm.trigger('reset');
        },
        error:function(jqXhr, st, msg) { 
			console.log("ERROR: ", msg);
			var errs = jqXhr.responseJSON;
			
			console.log(errs);
//			var errms = new Array('','','','','','','');				// niz sa 7 praznih karaktera
//			
//			$.each(errs, function(i){
//				  switch (errs[i].field) {
//					case 'ime':
//						errms[0] = errs[i].defaultMessage;
//						break;
//					case 'prezime':
//						errms[1] = errs[i].defaultMessage;
//						break;
//					case 'username':
//						errms[2] = errs[i].defaultMessage;
//						break;
//					case 'password':
//						errms[3] = errs[i].defaultMessage;
//						break;
//					case 'email':
//						errms[4] = errs[i].defaultMessage;
//						break;
//					}
//			});	  
//			
//			var errw = '<tr class="opr-err-row"><td></td><td>'+errms[0]+'</td><td>'+errms[1]+'</td>'
//						+'<td>'+errms[2]+'</td><td>'+errms[3]+'</td>'
//						+'<td>'+errms[4]+'</td><td></td><td></td><td colspan="2"></td></tr>';
//			
//			$('#opr-add-tr').after(errw);				// postavlja red tabele sa ispisanim greškama ispod reda za unos novog itema					
	    }
	});
}
/*
 * 	DELETE operater by id
 * */
function deleteOpr($dlr, id){
	
//	alert(getCtxPath());
	toggleDisableButton($('.ur-del-btn'), true);
	var con=confirm("Da li ste sigurni da zelite da ukolonite uredjaj id:"+id+"?");
	if(con==true){
	$.ajax({
		url : getCtxPath()+'/uredjaj/delete?id='+id,
		type : 'GET',
		dataType: 'text',
		success: function(data){
			console.log('Resp; '+data);
			$dlr.remove();
			alert("Uredjaj id:"+id+" uspesno uklonjen");
			toggleDisableButton($('.ur-del-btn'), false);
		},
		error:function(er, st, msg) { 
			alert("Ne mozete ukoniti uredjaj iz sistema jer je zauzet !");
			toggleDisableButton($('.ur-del-btn'), false);
			console.log("ERROR: ", msg);
			console.log("ER: ", er);
	    }
	});
	}
	else{
		toggleDisableButton($('.ur-del-btn'), false);
	}
	
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
function btnClick($btn){
	$($btn).trigger('click');
}

function populateUpdform(data, $frm){
	$.each(data, function(key, value){
		  console.log("K: "+key+" / V: "+value);
		  $('[name='+key+']', $frm).val(value);				  
	  });
}
