<div class="form-field">
   <#if form.mode == "view">
      <div class="viewmode-field">
         <span class="viewmode-label">${field.label?html}:</span>
         <span class="viewmode-value">${field.value?html}</span>
      </div>
   <#else>
      <label for="${fieldHtmlId}">${field.label?html}:<#if field.mandatory><span class="mandatory-indicator">*</span></#if></label>
      <input id="${fieldHtmlId}" type="text" name="${field.name}" value="${field.value}" readonly
                   style="width: 700px;" <#if field.disabled>disabled="true"</#if> />
   </#if>
   <script>
      var intentos = "0";
      function cambiarDireccion(){
         if(intentos=="0"){
            intentos = "1";
            var code = document.getElementById('iframe').src;
            code = code.split("/")[code.split("/").length-1];
            var str = document.URL.substring(0,document.URL.indexOf("/share"))+"/conte/evaluacion?solicitud="+code;
            document.getElementById('iframe').src=str;
         }
      }
   </script>
   <#escape x as x?html>
   <iframe src=<#noescape>${field.value}</#noescape> name="SubHtml"
      width="600" height="500" scrolling="auto" frameborder="1" id="iframe" onload="cambiarDireccion()">
      <p>Por favor ingrese con un navegador moderno, como Mozilla Firefox<</p>
    </iframe>
	</#escape>
</div>