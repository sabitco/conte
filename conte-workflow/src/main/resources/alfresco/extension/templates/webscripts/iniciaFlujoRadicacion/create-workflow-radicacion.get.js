//Arguments values
var definition = args.definition;
var description = args.description;
var assignee = args.assign;
var FileNodeId = args.fileNodeId

var registrationWorkflowDefinition = workflow.getDefinitionByName(definition);

//Parameters
var parameters = new Object();
parameters["bpm:workflowDescription"] = description;
//parameters["bpm:assignee"] = "workspace://SpacesStore/33e69c23-04e6-4438-80f8-c7d1fa9894b2";
//parameters["bpm:assignee"] =[people.getPerson(assignee)];
//for each (arg in args.fields){
	//alert(arg.name);
	//console.log(arg.name);
	//parameters["qswf:" + arg.name] = arg.value;
//}

parameters["qswfr:tiposolicitud"] = args.tiposolicitud;
parameters["qswfr:tipodocumento"] = args.tipodocumento;
parameters["qswfr:numerodocumento"] = args.numerodocumento;
parameters["qswfr:nombres"] = args.nombres;
parameters["qswfr:apellidos"] = args.apellidos;
parameters["qswfr:fechaNacimiento"] = args.fechaNacimiento;				
parameters["qswfr:direccionResidencia"] = args.direccionResidencia;	
parameters["qswfr:dependiente"] = args.dependiente;
parameters["qswfr:notificacion"] = args.notificacion;
parameters["qswfr:email"] = args.email;			
parameters["qswfr:departamento"] = args.departamento;
parameters["qswfr:ciudad"] = args.ciudad;	
parameters["qswfr:celular"] = args.celular;				
parameters["qswfr:telefono"] = args.telefono;		
parameters["qswfr:empresa"] = args.empresa;
parameters["qswfr:direccionEmpresa"] = args.direccionEmpresa; 				
parameters["qswfr:departamentoEmpresa"] = args.departamentoEmpresa;
parameters["qswfr:ciudadEmpresa"] = args.ciudadEmpresa;
parameters["qswfr:telefonoEmpresa"] = args.telefonoEmpresa;	
parameters["qswfr:nombresReferencia1"] = args.nombresReferencia1;
parameters["qswfr:direccionReferencia1"] = args.direccionReferencia1;
parameters["qswfr:telefonoReferencia1"] = args.telefonoReferencia1;			
parameters["qswfr:nombresReferencia2"] = args.nombresReferencia2;
parameters["qswfr:direccionReferencia2"] = args.direccionReferencia2; 				
parameters["qswfr:telefonoReferencia2"] = args.telefonoReferencia2; 				
parameters["qswfr:tramitePor"] = args.tramitePor;		
parameters["qswfr:realizadoEn"] = args.realizadoEn;
parameters["qswfr:esEmpresa"] = args.esEmpresa;
parameters["qswfr:nit"] = args.nit;
parameters["qswfr:numeroformulario"]=args.numeroformulario;
parameters["qswfr:asociacion"]=args.asociacion;
parameters["qswfr:identificacionInspector"] = args.identificacionInspector;

//Attached File
if (FileNodeId!= null)
{
  var nodeId = FileNodeId;
  var theDocument = search.findNode("workspace://SpacesStore/" + nodeId);
  var workflowPackage = workflow.createPackage();
  workflowPackage.addNode(theDocument);

}
else
{
  var workflowPackage = workflow.createPackage();
}

//Creating the workflow
//Note the order of the arguments in startWorkflow method are in inverse order than documentation
var registrationWorkflowStart = registrationWorkflowDefinition.startWorkflow(workflowPackage,parameters);

//Values to return
model.workflowInstance = registrationWorkflowStart.instance;
model.workflowInstanceID = registrationWorkflowStart.id;
