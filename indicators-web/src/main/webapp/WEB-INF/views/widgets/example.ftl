[#ftl]
[#include "/inc/includes.ftl"]

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Istac widget example</title>
</head>

<body>
    <script src="[@spring.url "/theme/js/widgets/widget.min.all.js"/]"></script>
    <div id="istac-widget"></div>
    <script>
        var options =  ${options};
        var istacWidget = new IstacWidget(options);
    </script>
</body>
</html>