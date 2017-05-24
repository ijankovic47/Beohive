$(function() {

	var ctxPath = $('#ctx-path').val();
	$('#mod-add-btn').on('click', function() {
		$('#mod-add-tr').toggle();
	});

	$('table').on('click', '.mod-edit-btn', function(evt) {

		toggleDisableButton($('.mod-edit-btn'), true);
		toggleDisableButton($('#edit-save-btn'), true);
		var id = $(this).attr('title');
		var $er = $('#mod-edit-tr').clone();
		var $or = $(this).parent().parent();
		$(this).parent().parent().replaceWith($er.show(300));

		$.ajax({
			url : ctxPath+"/modeli/getModel?id=" + id,
			type : 'GET',
			dataType : 'json',
			contentType : 'application/json; charset=utf-8',
			success : function(data) {
				console.log('Edit button pozvan za model: ' + data.ime);
				$('#edit-id').html(data.id);
				$('#edit-ime').val(data.ime);
				$('#edit-imeNaPlatformi').val(data.imeNaPlatformi);

				toggleDisableButton($('#edit-save-btn'), false);
			},
			error : function(er, st, msg) {
				console.log("ERROR: ", msg);
				console.log("ER: ", er);
			}
		});

		$('#edit-save-btn').on('click', function() {

			var $ur = $(this).parent().parent();
			updateOpr(id, $ur);
			
		});

		$('.edit-cancel-btn').on('click', function(evt) {
			evt.preventDefault();
			
			$(this).parent().parent().replaceWith($or); 
    										
			toggleDisableButton($('.mod-edit-btn'), false); 
															
		});
	});
	
	$('table').on('click', '.mod-del-btn', function(evt) {
		
		var id = $(this).attr('title');
		var $dlr = $(this).parent().parent();
		toggleDisableButton($('.mod-del-btn'), true); 
		deleteMod($dlr, id);
		toggleDisableButton($('.mod-del-btn'), false); 
	
	});
});

function updateOpr(id, $ur){
	var ctxPath = $('#ctx-path').val();
	var dataForUpd = {};
	var csrf = getCsrfParams();
	
	dataForUpd['id'] = id; 
	dataForUpd['ime'] = $('#edit-ime').val();
	dataForUpd['imeNaPlatformi'] = $('#edit-imeNaPlatformi').val();
	
	$.ajax({
		url : ctxPath+'/modeli/updateModel',
		type : 'POST',
		dataType: 'text',										//ocekuje u response json, response body konvertuje u json
		contentType : 'application/json;charset=utf-8',  		//ovo je potrebno kada saljemo objekat post metodom
		data : JSON.stringify(dataForUpd),
		beforeSend: function(xhr) {
           xhr.setRequestHeader(csrf[0], csrf[1]);
        },
		success : function(data) {

			var id = dataForUpd['id'];
			var nr = '<tr id="row-'+id+'"><td>'+id+'</td><td>'+dataForUpd['ime']+'</td><td>'+dataForUpd['imeNaPlatformi']+'</td><td><button type="button" id="edit-'+id+'" class="mod-edit-btn" title="'+id+'">Izmeni</button></td>'
					 +'<td><button type="button" id="edit-'+id+'" class="mod-del-btn" title="'+id+'">Obrisi</button></td></tr>';

			$ur.replaceWith(nr);
			$('#row-'+id+'').css('border-color','#BAD696');
			alert("Model id:"+id+" uspesno izmenjen");
			toggleDisableButton($('.mod-edit-btn'), false);
		},	
		error:function(er, st, msg) { 
			toggleDisableButton($('.mod-edit-btn'), true);
			alert(er.responseText)
			console.log("ERROR: ", msg);
			console.log("ERROR: ", er)
	    }
	});
}

function toggleDisableButton($btn, disabled) {
	$($btn).attr('disabled', disabled);
}

function deleteMod($dlr, id){
//	alert(getCtxPath());
	var txt;
	var r = confirm("Da li ste sigurni da zelite da obrisete model id:"+id+"?");
	if (r == true) {
		var ctxPath = $('#ctx-path').val();
		$.ajax({
			url : ctxPath+'/modeli/deleteModel?id='+id,
			type : 'GET',
			dataType: 'text',
			success: function(data){
			
				console.log('Resp; '+data);
				
				$dlr.remove();
				alert("Model id:"+id+" uspesno uklonjen");
			},
			error:function(er, st, msg) { 
				console.log("ERROR: ", msg);
				console.log("ER: ", er);
				alert("Model id:"+id+" ne moze biti uklonjen jer ima uredjaja tog modela");
		    }
		});
	} else {
		
	}
	toggleDisableButton($('.mod-del-btn'), false);
}
function btnClick($btn){
	$($btn).trigger('click');
}
function getCsrfParams() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	return [ header, token ];
}