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
    <script>
        var options =  JSON.parse('${options}');

        var body = document.getElementsByTagName('body')[0];
        var indicatorsWidgetDiv = document.createElement('div');
        indicatorsWidgetDiv.id = options.el.substring(1);
        indicatorsWidgetDiv.class = 'edatos-indicators';
        body.appendChild(indicatorsWidgetDiv);

        var istacWidget = new IstacWidget(options);
    </script>
</body>
</html>