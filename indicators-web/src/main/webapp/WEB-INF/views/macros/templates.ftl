[#ftl]
[#macro base migas='' page_title='' extra_keywords='' page_description='']
<!DOCTYPE html>
<html>
	<head>
        [#include "/layout/html-header.ftl"]
	</head>
	<body>
	    
	    <!-- begin: #cabecera -->
        ${portalDefaultStyleHeader!} 
        <!-- end: #cabecera -->
        
        <!-- FIXME Migas -->
        [#if migas?has_content]
            ${migas}
        [/#if]
        
        <!-- begin: #bloq_interior -->  
        <div id="bloq_interior">    
            <div class="contenido">
                [#nested]
            </div>
        </div>
        <!-- end: #bloq_interior -->
        
        <!-- begin: pie -->
        ${portalDefaultStyleFooter!}
        <!-- end: pie -->

        <script type="text/javascript">               
            (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
            })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
            
            ga('create', '${analyticsGoogleTrackingId}', 'auto');
            ga('send', 'pageview');
        </script>        
	</body>
</html>
[/#macro]