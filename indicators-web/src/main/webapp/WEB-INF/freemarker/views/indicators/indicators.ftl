[#ftl]
[#include "/includes.ftl"]
[@template.base]

    <div id="page-loader">[@apph.messageEscape 'app.loading'/]</div>

    <div id="indicators"></div>

    <script type="text/html" id="indicatorTmpl">
        <a href="<%= getVisualizerUrlForIndicator(id) %>" title="<%= getLabel(title) %>"><%= getLabel(title) %></a>
    </script>

    <script type="text/html" id="noResultsTmpl">
        <div>[@apph.messageEscape 'page.error.no-results'/] "<strong><%= query %></strong>"</div>
    </script>

    <script>
        var IndicatorsCollection = Backbone.Collection.extend({

            url: apiUrl + '/indicators/?limit=1000',

            initialize: function () {
                this.fetch({
                    success: function () {
                        $('#page-loader').hide();
                    },
                    error: function () {
                        $('#page-loader').hide();
                    }
                });
                _.bindAll(this);
            },

            parse: function (response) {
                return response.items;
            },

            search: function (query) {
                this.query = query.trim();
                this.trigger("filterChange", "");
            },

            filtered: function () {
                if (this.query && this.query.length > 0) {
                    var query = this.query;
                    return this.filter(function (indicatorBySubject) {
                        var code = indicatorBySubject.get('subject').get('code');
                        return containsLowerCase(code, query);
                    });
                }
                return this;
            },

            comparator: function (item) {
                return item.get("subjectCode");
            }
        });

        var SRMCollection = Backbone.Collection.extend({
            url: srmRestUrl + '/categoryschemes/' + srmAgency + '/' + srmResource + '/' + srmVersion + '/categories?limit=1000',

            initialize: function () {
                this.fetch({
                    success: function () {
                        console.log('SRMCollection cargada con éxito');
                    },
                    error: function () {
                        console.error('Error al cargar SRMCollection');
                    }
                });
                _.bindAll(this);
            },

            parse: function (response) {
                return response.items || response; // ajusta según estructura del JSON
            }
        });

        var IndicatorView = Backbone.View.extend({
            template: _.template($('#indicatorTmpl').html()),

            render: function () {
                return this.template(this.model.toJSON());
            }
        });

        var IndicatorsView = Backbone.View.extend({

            noResultsHtml: _.template($('#noResultsTmpl').html()),

            initialize: function () {
                this.collection.bind("filterChange", this.render, this);
                this.collection.bind("reset", this.render, this);
            },

            render: function () {
                var filtered = this.collection.filtered();
                if (filtered.length > 0) {
                    var self = this;
                    var viewHtml = '';
                    var groupLast = '';
                    var subjectCodeLastIndicator = '';

                    filtered.forEach(function (model) {
                        var indicatorsView = new IndicatorView({model: model});
                        var subViewHtml = indicatorsView.render();

                        var groupCurrent = model.get("subjectCode");
                        var subjectCodeIndicator = model.get("subjectCode");

                        if (groupLast != groupCurrent) {
                            viewHtml += viewHtml != '' ? '</ul></li></ul></li>' : '';
                            viewHtml += '<li>';
                            viewHtml += '<h2 class="collapsible-header">' + getLabel(model.get("subjectTitle")) + '</h2>';
                            viewHtml += '<ul class="collapsible-group-content">';
                            groupLast = groupCurrent;
                            subjectCodeLastIndicator = '';
                        }

                        if (subjectCodeLastIndicator != subjectCodeIndicator) {
                            viewHtml += subjectCodeLastIndicator != '' ? '</ul></li>' : '';
                            viewHtml += '<li>';
                            viewHtml += '<h3 class="collapsible-header">' + getLabel(model.get("subjectTitle")) + '</h3>';
                            viewHtml += '<ul class="collapsible-content">';
                            subjectCodeLastIndicator = subjectCodeIndicator;
                        }

                        viewHtml += '<li>' + subViewHtml + '</li>';
                    });

                    viewHtml += viewHtml != '' ? '</ul></li></ul></li>' : '';
                    $(self.el).html('<ul>' + viewHtml + '</ul>');
                } else if (this.collection.query != null) {
                    $(this.el).html(this.noResultsHtml({query: this.collection.query}));
                }
            }
        });

        var SearchView = Backbone.View.extend({
            events: {
                'keyup .search': 'searchBoxKeydown'
            },

            searchBoxKeydown: function (e) {
                var text = $(".search", this.el).val();
                this.collection.search(text);
            }
        });

        $(function () {
            var indicatorsCollection = new IndicatorsCollection();
            var indicatorsView = new IndicatorsView({el: $("#indicators"), collection: indicatorsCollection});
            indicatorsView.render();
            new SearchView({el: $("#indicators-search"), collection: indicatorsCollection});
        });

    </script>

    <script>
        $(document).on('click', '.collapsible-header', function () {
            $(this).toggleClass('open');
            const nextContent = $(this).next('.collapsible-content, .collapsible-group-content');
            if (nextContent.length) {
                nextContent.toggleClass('show');
            }
        });
    </script>

[/@template.base]
