[#ftl]
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="shortcut icon" href="//www.gobiernodecanarias.org/gc/img/favicon.ico"/>
		
		[#if apiStyleCssUrl?has_content]
            <link href="${apiStyleCssUrl}" media='screen' rel='stylesheet' type='text/css' />
        [/#if]
	</head>
	<body>
	
	   ${apiStyleHeader!}
	   
	   <div class="version-list">
    	   <h1>Indicators API</h1>
    	   <h2>Versiones</h2>
    	   <ul>
    	       <li>
    	           <h3 class="version-title"><a href="${indicatorsExternalApiUrlBase}/latest" alt="Última versión de la API">/latest</a></h3>
    	           <div class="version-description">
    	               <p><strong>latest</strong> es la palabra clave reservada con la que se puede acceder a la última versión de la API</p>    	               
    	           </div>
    	       </li>
    	       
    	       <li>
                   <h3 class="version-title"><a href="${indicatorsExternalApiUrlBase}/v1.0" alt="Versión 1.0">/v1.0</a></h3>
                   <div class="version-description">
                        <p>Versión 1.0 de la API</p>    
                   </div>
               </li>
    	   </ul>
	   </div>
	   
        ${apiStyleFooter!}	   	           
	</body>
</html>