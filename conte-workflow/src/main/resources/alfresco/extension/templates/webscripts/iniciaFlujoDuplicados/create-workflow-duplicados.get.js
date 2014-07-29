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

parameters["qswfgd:tiposolicitud"] = args.tiposolicitud;
parameters["qswfgd:tipodocumento"] = args.tipodocumento;
parameters["qswfgd:numerodocumento"] = args.numerodocumento;
parameters["qswfgd:nombres"] = args.nombres;
parameters["qswfgd:apellidos"] = args.apellidos;
parameters["qswfgd:fechaNacimiento"] = args.fechaNacimiento;				
parameters["qswfgd:direccionResidencia"] = args.direccionResidencia;	
parameters["qswfgd:dependiente"] = args.dependiente;
parameters["qswfgd:notificacion"] = args.notificacion;
parameters["qswfgd:email"] = args.email;			
parameters["qswfgd:departamento"] = args.departamento;
parameters["qswfgd:ciudad"] = args.ciudad;	
parameters["qswfgd:celular"] = args.celular;				
parameters["qswfgd:telefono"] = args.telefono;		
parameters["qswfgd:empresa"] = args.empresa;
parameters["qswfgd:direccionEmpresa"] = args.direccionEmpresa; 				
parameters["qswfgd:departamentoEmpresa"] = args.departamentoEmpresa;
parameters["qswfgd:ciudadEmpresa"] = args.ciudadEmpresa;
parameters["qswfgd:telefonoEmpresa"] = args.telefonoEmpresa;	
parameters["qswfgd:nombresReferencia1"] = args.nombresReferencia1;
parameters["qswfgd:direccionReferencia1"] = args.direccionReferencia1;
parameters["qswfgd:telefonoReferencia1"] = args.telefonoReferencia1;			
parameters["qswfgd:nombresReferencia2"] = args.nombresReferencia2;
parameters["qswfgd:direccionReferencia2"] = args.direccionReferencia2; 				
parameters["qswfgd:telefonoReferencia2"] = args.telefonoReferencia2; 				
parameters["qswfgd:tramitePor"] = args.tramitePor;		
parameters["qswfgd:realizadoEn"] = args.realizadoEn;
parameters["qswfgd:esEmpresa"] = args.esEmpresa;
parameters["qswfgd:nit"] = args.nit;
parameters["qswfgd:numeroformulario"]=args.numeroformulario;
parameters["qswfgd:asociacion"]=args.asociacion;
parameters["qswfgd:identificacionInspector"] = args.identificacionInspector;

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
