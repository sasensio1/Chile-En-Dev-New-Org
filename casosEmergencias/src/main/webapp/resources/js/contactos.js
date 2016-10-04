var table;
var urlTable = createUrl();

function funcionOnload(){
	initHeader(); 
	showNotifications(); 
	cargarAsociarSuministro();
}

//Crear caso por contacto
function goCrearCasoByContacto(){
	verCaragando();
	window.location="../private/goCrearCasoByContacto";
}

function ocultarNotificacion(){
	$('#divInsertError').hide();
}

//Cargamos el dialog para asociar suministro a un contacto
function cargarAsociarSuministro() {
	$("#dialogAsociarSuministroContacto").dialog({
		resizable: false,
		autoOpen: false, 
		modal: true, 
		show: "blind", 
		hide: "blind", 
		height: "auto",
		width: "40%",
		position:{
			my: "center center", 
			at: "center center",
			of: $('#divEntidadContactoSuministros') 
		},
		create: function (event) {}
	});
}

function abrirAsociarSuministro(){
	ocultarNotificacion();
	$("#dialogAsociarSuministroContacto").dialog('open');
	cargarTablaSuministros();
}

function cargarTablaSuministros(){
	if(table == null){
		var formulario = $('#asociarSuministroId');
		
		table = $('#tablaSuministros').DataTable({
	       	"scrollY": "250px",
			"scrollX": false,
			"scrollCollapse": true,
			"paging": false,
			"serverSide": true,
		    "ordering": true,	    
			"processing": true, 
	        "language": {
	        	processing:  "<img src='../resources/images/loading.gif' width='25' > Cargando...",
	            lengthMenu: "",
	            info: "",
	            infoEmpty: "",
	            infoFiltered: "",
	            zeroRecords:"",
	            emptyTable:"No se han encontrado registros"
	        },
	        "lengthChange": false,
	        "deferRender": true,
	        "deferLoading": -1,
			"ajax": { 
	        	"type": formulario.attr('method'),
	        	 "url": formulario.attr('action'), 	               	
	        	 "contentType": 'application/json; charset=utf-8' ,
	        	 "error": function(data) {
	        		 alert('Se ha producido un error obteniendo la lista de suministros. Repita la operación y, si el error persiste, contacte con el administrador de la plataforma.');
	        	 }
	      	},
	       	"columns": [
	       	            {data: "name", width: "18%", defaultContent: "", orderable: false}, 
	       	            {data: "direccionConcatenada", width: "31%", defaultContent: "", orderable: false},
	       	            {data: "comuna", width: "15%", defaultContent: "", orderable: false},
	       	            {data: "estadoSuministroPickList", width: "18%", defaultContent: "", orderable: false},
	       	            {data: "empresaPickList", width: "15%", defaultContent: "", orderable: false},
	       	            {data: "sfid", width: "1%", defaultContent: "", visible: false, orderable: false},
	       	            {data: "calle", width: "1%", defaultContent: "", visible: false, orderable: false},
	       	            {data: "numero", width: "1%", defaultContent: "", visible: false, orderable: false}
			],
			"columnDefs": [
	                    {"targets": 0,
	                     "render": function (data, type, full, meta) {
	                    	 var sfid = "";
	                    	 var txtColumn = "";
	                    	 if (full.sfid != null) {
	                    		 sfid = full.sfid;
	                    	 }
	                    	 if (data != null) {
	                    		 txtColumn = data;
	                    	 }
	                    	 return '<a href="javaScript:{cargandoGif('+"'" +sfid + "', '"+"entidadSuministro"+"'"+')}">'+ txtColumn + '</a>';
	                    }
	        }],
	        "order":[[0, "desc"]]
		});
		
		$('#buscar').on('click', function() {
			table
				.columns(0).search($('#idNumero').val())
				.columns(2).search($('#idComuna').val())
				.columns(5).search($('#idCalle').val())
				.columns(6).search($('#idNumero').val())
				.draw();			
		});
		
		//Añadir opcion de buscar pulsando enter
		$("#idNumero").on("keyup", function (event) {
		    if (event.keyCode==13) {
		        $("#buscar").get(0).click();
		    }
		});
		$("#idComuna").on("keyup", function (event) {
		    if (event.keyCode==13) {
		        $("#buscar").get(0).click();
		    }
		});
		$("#idCalle").on("keyup", function (event) {
		    if (event.keyCode==13) {
		        $("#buscar").get(0).click();
		    }
		});
		$("#idNumero").on("keyup", function (event) {
		    if (event.keyCode==13) {
		        $("#buscar").get(0).click();
		    }
		});
	}
}



