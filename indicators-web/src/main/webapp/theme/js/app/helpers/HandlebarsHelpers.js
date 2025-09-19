(function () {

    Handlebars.registerHelper('getLabel', function (key) {
        return App.helpers.I18n.getLabel(key);
    });

    var toOptions = function (context) {
        var ret = '';
        for (var i = 0; i < context.length; i++) {
            var option = '<option value="' + context[i].value + '"';
            option += '>' + context[i].text + '</option>';
            ret += option;
        }
        return ret;
    };

    Handlebars.registerHelper('options', function (context) {
        return new Handlebars.SafeString(toOptions(context));
    });

    Handlebars.registerHelper('groupedOptions', function (context) {
        var ret = '';
        for (var i = 0; i < context.length; i++) {
            var item = context[i];
            ret += "<optgroup label='" + item.title + "'>";
            ret += toOptions(item.values);
            ret += "</optgroup>";
        }
        return new Handlebars.SafeString(ret);
    });

    Handlebars.registerHelper('serverURL', function (context) {
        return serverURL;
    });

    Handlebars.registerPartial('subjectsAndIndicatorsTreeNode', `
        {{#if isSubject}}
            <li class="indicator-picklist-subject-header collapsed">
                <span>{{subjectTitle}}</span>
                <ul id="{{subjectCode}}">
                    {{#each indicators}}
                        {{> subjectsAndIndicatorsTreeNode }}
                    {{/each}}
                    {{#each children}}
                        {{> subjectsAndIndicatorsTreeNode }}
                    {{/each}}
                </ul>
            </li>
        {{else}}
            <li indicatorId="{{id}}" subjectId="{{subjectCode}}" class="selectable-picklist-item"><span>{{title}}</span></li>
        {{/if}}
    `);

}());
