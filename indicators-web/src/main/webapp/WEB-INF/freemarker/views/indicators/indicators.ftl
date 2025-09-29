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
        const indicatorsApiUrl = apiUrl + '/indicators/?limit=1000';
        const srmCategoriesApiUrl = srmRestUrl + '/categoryschemes/' + srmAgency + '/' + srmResource + '/' + srmVersion + '/categories.json';

        var IndicatorsCollection = Backbone.Collection.extend({
            url: indicatorsApiUrl,

            initialize: function () {
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

            initialize: function (models, options) {
                this.options = options || {};
            },

            sync: function(method, model, options) {
                options = options || {};
                options.beforeSend = function(xhr) {
                    xhr.setRequestHeader("api-key", apiKey);
                };
                return Backbone.sync(method, model, options);
            },

            parse: function (response) {
                var indicatorsCollection = this.options.indicators;
                var categories = response.category || [];
                var categoryMap = {};

                for (var i = 0; i < categories.length; i++) {
                    var category = categories[i];
                    category.children = [];
                    category.indicators = [];
                    categoryMap [category.urn] = category;
                }

                for (var i = 0; i < categories.length; i++) {
                    var category = categories[i];
                    if (category.parent && categoryMap [category.parent]) {
                        categoryMap [category.parent].children.push(category);
                    }
                }

                if (indicatorsCollection) {
                    indicatorsCollection.each(function (indicator) {
                        var subjectCode = indicator.get('subjectCode');
                        var matchingUrn = null;
                        for (var urn in categoryMap) {
                            if (categoryMap.hasOwnProperty(urn)) {
                                // See code at SrmRestInternalServiceImpl.splitUrnWithoutPrefixItem
                                var urnCode = urn.substring(urn.indexOf(").") + 2, urn.length);
                                if (urnCode === subjectCode) {
                                    matchingUrn = urn;
                                    break;
                                }
                            }
                        }
                        if (subjectCode && matchingUrn) {
                            categoryMap [matchingUrn].indicators.push(indicator.toJSON());
                        }
                    });
                }


                function hasIndicators(category) {
                    var filteredChildren = [];
                    for (var j = 0; j < category.children.length; j++) {
                        if (hasIndicators(category.children[j])) {
                            filteredChildren.push(category.children[j]);
                        }
                    }
                    category.children = filteredChildren;
                    return category.indicators.length > 0 || category.children.length > 0;
                }

                var rootCategory = [];
                for (var i = 0; i < categories.length; i++) {
                    var category = categories[i];
                    if (!category.parent || !categoryMap[category.parent]) {
                        rootCategory.push(category);
                    }
                }

                var tree = [];
                for (var i = 0; i < rootCategory.length; i++) {
                    if (hasIndicators(rootCategory[i])) {
                        tree.push(rootCategory[i]);
                    }
                }

                this.tree = tree;
                return categories;
            },

            toTree: function () {
                return this.tree || [];
            }
        });

        var IndicatorView = Backbone.View.extend({
            template: _.template($('#indicatorTmpl').html()),

            render: function () {
                return this.template(this.model.toJSON());
            }
        });

        var CategoryView = Backbone.View.extend({

            tagName: 'li',

            render: function () {
                var nameArray = this.model.name ? this.model.name.text : [];
                var displayName = this.getLocalizedName(nameArray);

                this.$el.html('<div class="collapsible-header">' + displayName + '</div>');

                var hasContent = false;
                var $content = $('<div class="collapsible-content"></div>');

                if (this.model.indicators && this.model.indicators.length) {
                    hasContent = true;
                    var ul = $('<ul class="indicators">');
                    for (var i = 0; i < this.model.indicators.length; i++) {
                        var model = new Backbone.Model(this.model.indicators[i]);
                        var indicatorView = new IndicatorView({model: model});
                        ul.append('<li>' + indicatorView.render() + '</li>');
                    }
                    $content.append(ul);
                }

                if (this.model.children && this.model.children.length) {
                    hasContent = true;
                    var childrenUl = $('<ul class="children">');
                    for (var j = 0; j < this.model.children.length; j++) {
                        var childView = new CategoryView({
                            model: this.model.children[j]
                        });
                        childrenUl.append(childView.render().el);
                    }
                    $content.append(childrenUl);
                }

                if (hasContent) {
                    this.$el.append($content);
                }

                return this;
            },

            getLocalizedName: function (text) {
                var nameInCurrentLocale = text.find(function (text) {
                    return text.lang === currentLocale;
                });
                if (nameInCurrentLocale) {
                    return nameInCurrentLocale.value;
                }
                var nameInDefaultLocale = text.find(function (text) {
                    return text.lang === defaultLocale;
                });
                if (nameInDefaultLocale) {
                    return nameInDefaultLocale.value;
                }
                return '[@apph.messageEscape 'page.indicators-list.categories.noName'/]';
            }

        });

        var CategoriesView = Backbone.View.extend({
            el: '#indicators',

            render: function () {
                var tree = this.collection.toTree();
                var $ul = $('<ul></ul>');

                for (var i = 0; i < tree.length; i++) {
                    var view = new CategoryView({model: tree[i]});
                    $ul.append(view.render().el);
                }

                this.$el.empty().append($ul);
                return this;
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
            const indicatorsCollection = new IndicatorsCollection();

            indicatorsCollection.fetch({
                success: function () {
                    const categoriesCollection = new CategoriesCollection([], {
                        indicators: indicatorsCollection
                    });

                    categoriesCollection.fetch({
                        success: function () {
                            $('#page-loader').hide();
                            var categoriesView = new CategoriesView({
                                el: $("#indicators"),
                                collection: categoriesCollection
                            });
                            categoriesView.render();
                        },
                        error: function () {
                            $('#page-loader').hide();
                        }
                    });
                },
                error: function () {
                    $('#page-loader').hide();
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
            var nextContent = $(this).next('.collapsible-content, .collapsible-group-content');
            if (nextContent.length) {
                nextContent.toggleClass('show');
            }
        });
    </script>

[/@template.base]