[#ftl]
[#macro base]
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8" />		
		<meta http-equiv="Content-language" content="es" />	

		<title>[@spring.message "app.title" /]</title>
		
		<link rel="stylesheet" href="[@spring.url "/theme/css/reset.css"                   /]" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="[@spring.url "/theme/css/clearfix.css"                /]" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="[@spring.url "/theme/css/screen.css"                  /]" type="text/css" media="screen, projection" />		
		<link rel="stylesheet" href="[@spring.url "/theme/css/jquery-ui-dataset-1.0.0.css" /]" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="[@spring.url "/theme/css/jquery.jscrollpane.css"      /]" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="[@spring.url "/theme/css/tables.css"                  /]" type="text/css" media="screen, projection" />
		
		<script type="text/javascript">
			var context = "[@spring.url '' /]";
		</script>
		
		<script type="text/javascript" src="[@spring.url "/theme/js/libs/jquery-1.7.1.js"                         /]" ></script>
        <script type="text/javascript" src="[@spring.url "/theme/js/libs/jquery-ui-1.8.17.custom.js"              /]" ></script>
        <script type="text/javascript" src="[@spring.url "/theme/js/libs/jquery.json-2.3.min.js"                  /]" ></script>    
        
        <script type="text/javascript" src="[@spring.url "/theme/js/libs/jquery-disable-text-selection-1.0.0.js"  /]" ></script>
        
        <script type="text/javascript" src="[@spring.url "/theme/js/libs/highcharts-2.2.0.js"                     /]" ></script>
		
		<script type="text/javascript" src="[@spring.url "/theme/js/libs/css_browser_selector.js"                 /]" ></script>
		
		<script type="text/javascript">
		  var _gaq = _gaq || [];
		  _gaq.push(['_setAccount', 'UA-30024362-1']);
		  _gaq.push(['_trackPageview']);
		
		  (function() {
		    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
		  })();
		</script>
	</head>
	<body>
		<div class="container">
			<div id="header">
				<div id="top"></div> 
				<h1><a href="[@spring.url "/index.html" /]">Indicators</a></h1>
				<div id="menu">
					<ul class="menu">
						<li>
							<a href="[@spring.url "/indicators-systems" /]">[@spring.message 'menu.indicators-systems' /]</a>
						</li>
					</ul>
				</div>		
			</div>
			<div id="column-body">
				[#nested]
			</div>
	
		  	<div id="footer">
		  		<p class="left">[@apph.messageEscape 'app.footer.gobcan' /]</p>
		  		<div class="right">
		  			<ul>
		  				<li>[@apph.messageEscape 'app.footer.legal-advice' /]</li>
		  				<li>[@apph.messageEscape 'app.footer.suggests' /]</li>
		  			</ul>
		  		</div>
			</div>
		</div>
	</body>
</html>
[/#macro]