$(function() {
	
	var ctxPath = $('#ctx-path').val();
	
	$('.doplatniPaketi').on('click', function(){
		$('.doplatniPaketi').attr('disabled',true);
	});
	
	$('.kor-info-btn').on('click', function(){
		
		$('.kor-info-btn').attr('disabled',true);
		var id = $(this).attr('title');	
		
		console.log(id);
		 $.ajax({
	   		url : ctxPath+"/administracija/korInfo?id="+id,
	   		type : 'GET',
	   		dataType: 'json',
	   		contentType : 'application/json; charset=utf-8',
	   		success: function(data){  
	   			console.log('succes');
	   			
	   			if(data.paketi!=null){
	   				
	   		    var out="<h1>Paketi</h1><ul>";
	   			var list=data.paketi.split("#");
	   			
	   			for(i=0;i<list.length;i++){
	   				out+="<li>"+list[i]+"</li>";
	   			}
	   			
	   			}
	   
	   			var status;
	   			
	   			if(data.uredjaj.status==0){
	   				status="Neaktivan";
	   			}
	   			else{
	   				status="Aktivan";
	   			}
	   			out+="</ul><h1>Uredjaj</h1>";
	   			
	   			
	   			out+="<table><tr><th>Serijski broj</th><th>MAC adresa</th><th>Model</th><th>Status</th></tr>" +
	   					"<tr><td>"+data.uredjaj.sn+"</td><td>"+data.uredjaj.macAdresa+"</td><td>"+data.uredjaj.model.ime+"</td>" +
	   							"<td>"+status+"</td></tr></table>";
	   			
	   			out+="<a href='"+ctxPath+"/administracija/korEdit?id="+id+"'><button>Izmeni</button></a>";
	   			
	   			$('#korInfo').html(out);
	   			
	   			$('.kor-info-btn').attr('disabled',false);
	   		},
	   		error:function(er, st, msg) { 
	   			console.log("ERROR: ", msg);
	   			console.log("ER: ", er);
	   			
	   	    }
	   	});
});

	$('#zamenaUr').on('click', function(){
		
		$('#zamenaUredjaja').toggle();
	});
	
	

   });
function btnClick($btn){
	$($btn).trigger('click');
}