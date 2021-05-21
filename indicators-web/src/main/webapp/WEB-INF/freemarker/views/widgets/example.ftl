[#ftl]
[#include "/includes.ftl"]

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${organisation} | [@apph.messageEscape 'page.widgets.example.title' /]</title>
</head>

<body>
    <script src="${serverURL}/theme/js/widgets/widget.min.all.js"></script>
    <div id="istac-widget" class="edatos-indicators"></div>
    <script>
        var options =  JSON.parse('${options}');
        var istacWidget = new IstacWidget(options);
    </script>
</body>
</html>