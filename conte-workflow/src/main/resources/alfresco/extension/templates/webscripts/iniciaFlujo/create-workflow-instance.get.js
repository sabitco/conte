//Arguments values
var definition = "activiti$pdpqrs";
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

parameters["qswfa:tipodocumento"] = args.tipodocumento;
parameters["qswfa:numerodocumento"] = args.numerodocumento;
parameters["qswfa:nombres"] = args.nombres;
parameters["qswfa:apellidos"] = args.apellidos;
parameters["qswfa:telefono"] = args.telefono;
parameters["qswfa:celular"] = args.celular;
parameters["qswfa:direccion"] = args.direccion;
parameters["qswfa:departamento"] = args.departamento;
parameters["qswfa:ciudad"] = args.ciudad;
parameters["qswfa:direccion"] = args.direccion;
parameters["qswfa:email"] = args.email;
parameters["qswfa:tiposolicitud"] = args.tiposolicitud;
parameters["qswfa:redaccionsolicitud"] = args.redaccionsolicitud;
parameters["qswfa:razonSocial"] = args.razonSocial;
parameters["qswfa:cargo"] = args.cargo;
parameters["qswfa:rutaarchivo"] = args.rutaarchivo;
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
