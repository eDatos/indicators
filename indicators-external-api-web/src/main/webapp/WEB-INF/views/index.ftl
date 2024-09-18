[#ftl]
[#--
 * messageEscape indicating escape text  (default = false)
 *
 * Macro to translate a message code with arguments into a message.
 --]
[#macro messageEscape code, escape=false]
	[#assign args = [] /]
	${springMacroRequestContext.getMessage(code, args, '', escape)}[/#macro]
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="icon" href="${faviconUrl}"/>
	</head>
	<body>
	
	   ${apiStyleHeader!}
	   
	   <div class="version-list">
    	   <h1>[@messageEscape 'api.doc.title'/]</h1>
    	   <h2>[@messageEscape 'api.doc.versions'/]</h2>
    	   <ul>
    	       <li>
    	           <h3 class="version-title"><a href="${indicatorsExternalApiUrlBase}/latest">/latest</a></h3>
    	           <div class="version-description">
    	               <p><strong>latest</strong> [@messageEscape 'api.doc.latest'/]</p>
    	           </div>
    	       </li>
    	       
    	       <li>
                   <h3 class="version-title"><a href="${indicatorsExternalApiUrlBase}/v1.0">/v1.0</a></h3>
                   <div class="version-description">
                        <p>[@messageEscape 'api.doc.version.1_0'/]</p>
                   </div>
               </li>
    	   </ul>
	   </div>
	   
        ${apiStyleFooter!}	   	           
	</body>
</html>