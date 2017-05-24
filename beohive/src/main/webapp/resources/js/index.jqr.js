$(function() {
	
	var ctxPath = $('#ctx-path').val();
	
	$('#ur-sn-btn').on('click', function(){
        
		var sn=$('#ur-sn').val();
		var id = $(this).attr('title');	
		
		$.ajax({
    		url : ctxPath+"/getUredjajBySn?sn="+sn+"&partnerId="+id,
    		type : 'GET',
    		dataType: 'json',
    		contentType : 'application/json; charset=utf-8',
    		success: function(data){    
    			console.log(data);
    			
    		},
    		error:function(er, st, msg) { 
    			console.log("ERROR: ", msg);
    			console.log("ER: ", er);
    			
    	    }
    	});
		
	});
	
    $('#ur-mac-btn').on('click', function(){
        
    	var mac=$('#ur-mac').val();
    	
		var id = $(this).attr('title');	
		
		$.ajax({
    		url : ctxPath+"/getUredjajByMac?mac="+mac+"&partnerId="+id,
    		type : 'GET',
    		dataType: 'json',
    		contentType : 'application/json; charset=utf-8',
    		success: function(data){    
    			console.log(data);
    			
    		},
    		error:function(er, st, msg) { 
    			console.log("ERROR: ", msg);
    			console.log("ER: ", er);
    			
    	    }
    	});
	});
});