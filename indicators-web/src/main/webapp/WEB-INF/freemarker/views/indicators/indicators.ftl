[#ftl]
[#include "/includes.ftl"]
[@template.base]

    <div id="page-loader">[@apph.messageEscape 'app.loading'/]</div>

    <div id="categories"></div>

    <div id="indicators"></div>


    <script type="text/html" id="indicatorTmpl">
        <a href="<%= getVisualizerUrlForIndicator(id) %>" title="<%= getLabel(title) %>"><%= getLabel(title) %></a>
    </script>

    <script type="text/html" id="noResultsTmpl">
        <div>[@apph.messageEscape 'page.error.no-results'/] "<strong><%= query %></strong>"</div>
    </script>

    <script>
        const indicatorsApiUrl = apiUrl + '/indicators/?limit=1000';
        const srmCategoriesApiUrl = srmRestUrl + '/categoryschemes/' + srmAgency + '/' + srmResource + '/' + srmVersion + '/categories.json';

        var IndicatorsCollection = Backbone.Collection.extend({

            url: indicatorsApiUrl,

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

        var CategoriesCollection = Backbone.Collection.extend({
            url: srmCategoriesApiUrl,

            initialize: function () {
                this.fetch({
                    success: function () {
                        console.log('CategoriesCollection cargada con éxito');
                    },
                    error: function () {
                        console.error('Error al cargar CategoriesCollection');
                    }
                });
                _.bindAll(this);
            },

            parse: function (response) {
                const allCategories = response.category || [];
                const parentCategories = allCategories.filter(function (category) {
                    return !category.nestedId.includes('.');
                });
                return parentCategories;
            },
        });

        var IndicatorView = Backbone.View.extend({
            template: _.template($('#indicatorTmpl').html()),

            render: function () {
                return this.template(this.model.toJSON());
            }
        });

        var IndicatorsView = Backbone.View.extend({

            noResultsHtml: _.template($('#noResultsTmpl').html()),

            initialize: function (options) {
                this.categories = options.categories;
                this.collection.bind("filterChange", this.render, this);
                this.collection.bind("reset", this.render, this);
            },

            getCategoryNameById: function (id) {
                const category = this.categories.find(function (cat) {
                    return cat.get('id') === id;
                });

                if (!category) return '[@apph.messageEscape 'page.indicators-list.categories.noName'/]';

                const name = category.get('name');
                const nameTexts = (name && name.text) ? name.text : [];

                const currentMatch = nameTexts.find(txt => txt.lang === currentLocale);
                const fallbackMatch = nameTexts.find(txt => txt.lang === defaultLocale);

                return currentMatch?.value || fallbackMatch?.value || '[@apph.messageEscape 'page.indicators-list.categories.noCategory'/]';
            },

            render: function () {
                let self = this;
                const filtered = this.collection.filtered();

                if (filtered.length === 0 && this.collection.query != null) {
                    $(this.el).html(this.noResultsHtml({query: this.collection.query}));
                    return;
                }

                const groupByCategory = {};

                filtered.forEach(function (model) {
                    const subjectCode = model.get("subjectCode") || "";
                    const categoryKey = subjectCode.split('.')[0];

                    if (!groupByCategory[categoryKey]) {
                        groupByCategory[categoryKey] = [];
                    }

                    groupByCategory[categoryKey].push(model);
                });

                let viewHtml = '<ul>';

                Object.keys(groupByCategory).forEach(function (categoryKey) {
                    const indicators = groupByCategory[categoryKey];
                    const categoryName = self.getCategoryNameById(categoryKey);

                    viewHtml += '<li>';
                    viewHtml += '<h2 class="collapsible-header">' + categoryName + '</h2>';
                    viewHtml += '<ul class="collapsible-group-content">';

                    const groupedBySubjectCode = {};

                    indicators.forEach(function (model) {
                        const subjectCode = model.get("subjectCode");
                        if (!groupedBySubjectCode[subjectCode]) {
                            groupedBySubjectCode[subjectCode] = [];
                        }
                        groupedBySubjectCode[subjectCode].push(model);
                    });

                    Object.keys(groupedBySubjectCode).forEach(function (subjectCode) {
                        const models = groupedBySubjectCode[subjectCode];

                        if (subjectCode === categoryKey) {
                            models.forEach(function (model) {
                                const view = new IndicatorView({model});
                                viewHtml += '<li>' + view.render() + '</li>';
                            });
                        } else {
                            viewHtml += '<li>';
                            viewHtml += '<h3 class="collapsible-header">' + getLabel(models[0].get("subjectTitle")) + '</h3>';
                            viewHtml += '<ul class="collapsible-content">';

                            models.forEach(function (model) {
                                const view = new IndicatorView({model});
                                viewHtml += '<li>' + view.render() + '</li>';
                            });

                            viewHtml += '</ul></li>';
                        }
                    });

                    viewHtml += '</ul></li>';
                });

                viewHtml += '</ul>';
                this.$el.html(viewHtml);
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
            let categoriesCollection = new CategoriesCollection();
            let indicatorsCollection = new IndicatorsCollection();

            categoriesCollection.fetch({
                success: function () {
                    var indicatorsView = new IndicatorsView({
                        el: $("#indicators"),
                        collection: indicatorsCollection,
                        categories: categoriesCollection
                    });
                    indicatorsView.render();
                }
            });

            new SearchView({
                el: $("#indicators-search"),
                collection: indicatorsCollection
            });
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
