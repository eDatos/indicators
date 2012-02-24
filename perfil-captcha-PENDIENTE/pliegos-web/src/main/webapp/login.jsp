
<%@page import="org.apache.commons.lang.StringUtils"%><html>
	<head>
		<title>Perfil del Contratante</title>
		
		<!-- Character enconding por problemas con Acegi despues de la autenticacion -->
		<meta name="tipo_contenido"  content="text/html;" http-equiv="content-type" charset="utf-8"/>

		<link rel="shortcut icon" href="/theme/images/favicon.ico"/>

		<!-- Hojas de estilo -->
		<link rel="stylesheet" href="theme/css/theme.css" type="text/css">
		<link rel="stylesheet" href="theme/css/theme-icefaces.css" type="text/css">
		<link rel="stylesheet" href="theme/css/estilos-https.css" type="text/css">
	</head>
	<body>
		<div id="principal_interior" class="pliegos">
			<div id="principal_interior">
				<div id="bloq_menu">
					<h1>
						<a href="http://www.gobiernodecanarias.org/">© Gobierno de Canarias</a>
					</h1>
					<div id="listado_superior">
					</div>
					<div class="menu">
					</div>
				</div>
			</div>
			<div id="migas">
				<form id="migasForm">
					<p class="txt">Estás en: </p>
					<ul>
						<strong>Login</strong>
					</ul>
				</form>
			</div>
			<div id="bloq_interior">
				<div class="bloq_der_grande" style="margin-left: 10% !important;">
					<div class="tam_body">
						<form id="login" action="j_acegi_security_check" method="post">				
							<div style="margin-left: 5%;">

							<%
								if (!StringUtils.isBlank(request.getParameter("loginsFallidos"))) {
							%>
								<div class="row" style="padding-left: auto; padding-bottom:15px;">								
									<span class="errorMessage"> 
										Ha de introducir los caracteres de la imagen
									</span>
									
								</div>
								<div class="row" style="padding-left: auto; padding-bottom:15px;">
									<span class="warningMessage"> 
										Si se producen sucesivos intentos fallidos de login, el usuario será bloqueado
									</span>  
								</div>
							<%
								} else if (!StringUtils.isBlank(request.getParameter("error"))) {
							%>
								<div class="row" style="padding-left: auto; padding-bottom:15px;">						
									<span class="formw" style="padding-bottom:3px;">
										<span class="errorMessage"> 
											Usuario y/o password incorrectos
										</span> 
									</span>
								</div>
							<%
								}
							%>								
								<!-- Nombre de usuario y password -->
								<div class="row" style="padding-left: auto">
									<span class="label" style="width: 150px"> 
										<strong>Usuario:</strong>
									</span>
									<span class="formw"> 
										<input type="text" name="j_username" tabindex="1"/>
									</span>
								</div>
								<div class="row" style="padding-left: auto">
									<span class="label" style="width: 150px"> 
										<strong>Password:</strong>
									</span>
									<span class="formw"> 
										<input type="password" name="j_password" tabindex="2"/>
									</span>
								</div>
								
							<%
							if (!StringUtils.isBlank(request.getParameter("loginsFallidos"))) {
							%>								
								<div class="row" style="padding-left: auto">
									<span class="formw" style="margin-left: 155px"> 
										<img src="captcha.jpg"/>
									</span>
								</div>
								<div class="row" style="padding-left: auto">
									<span class="label" style="width: 150px">
									   <strong>Introduzca los caracteres de la imagen:</strong>
									</span>
									<span class="formw"> 
		    							<input type="text" name="j_captcha" id="captcha" autocomplete="off" />
									</span>
								</div>
							<%
								} 
							%>									
								
								<div class="finForm" />
							</div>
							<!-- Botón acceder -->
							<div style="margin-left: 30%; margin-top: 15px;">
								<input type="submit" name="submit" value="Acceder" title="Acceder" />				
							</div>
						</form>
					</div>
				</div>
			</div>
			<div id="pie">
				<p class="izda">© Gobierno de Canarias</p>
				<div class="dcha">
					<ul>
						<li class="nobarra3">
							<a href="http://www.gobiernodecanarias.org/avisolegal.html">Aviso Legal</a>
						</li>
						<li>
							<a href="http://www.gobiernodecanarias.org/sugrec/">Sugerencias y Reclamaciones</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</body>
</html>