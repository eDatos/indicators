//     Underscore.js 1.4.2
//     http://underscorejs.org
//     (c) 2009-2012 Jeremy Ashkenas, DocumentCloud Inc.
//     Underscore may be freely distributed under the MIT license.

(function() {

    // Baseline setup
    // --------------

    // Establish the root object, `window` in the browser, or `global` on the server.
    var root = this;

    // Save the previous value of the `_` variable.
    var previousUnderscore = root._;

    // Establish the object that gets returned to break out of a loop iteration.
    var breaker = {};

    // Save bytes in the minified (but not gzipped) version:
    var ArrayProto = Array.prototype, ObjProto = Object.prototype, FuncProto = Function.prototype;

    // Create quick reference variables for speed access to core prototypes.
    var push             = ArrayProto.push,
        slice            = ArrayProto.slice,
        concat           = ArrayProto.concat,
        unshift          = ArrayProto.unshift,
        toString         = ObjProto.toString,
        hasOwnProperty   = ObjProto.hasOwnProperty;

    // All **ECMAScript 5** native function implementations that we hope to use
    // are declared here.
    var
        nativeForEach      = ArrayProto.forEach,
        nativeMap          = ArrayProto.map,
        nativeReduce       = ArrayProto.reduce,
        nativeReduceRight  = ArrayProto.reduceRight,
        nativeFilter       = ArrayProto.filter,
        nativeEvery        = ArrayProto.every,
        nativeSome         = ArrayProto.some,
        nativeIndexOf      = ArrayProto.indexOf,
        nativeLastIndexOf  = ArrayProto.lastIndexOf,
        nativeIsArray      = Array.isArray,
        nativeKeys         = Object.keys,
        nativeBind         = FuncProto.bind;

    // Create a safe reference to the Underscore object for use below.
    var _ = function(obj) {
        if (obj instanceof _) return obj;
        if (!(this instanceof _)) return new _(obj);
        this._wrapped = obj;
    };

    // Export the Underscore object for **Node.js**, with
    // backwards-compatibility for the old `require()` API. If we're in
    // the browser, add `_` as a global object via a string identifier,
    // for Closure Compiler "advanced" mode.
    if (typeof exports !== 'undefined') {
        if (typeof module !== 'undefined' && module.exports) {
            exports = module.exports = _;
        }
        exports._ = _;
    } else {
        root['_'] = _;
    }

    // Current version.
    _.VERSION = '1.4.2';

    // Collection Functions
    // --------------------

    // The cornerstone, an `each` implementation, aka `forEach`.
    // Handles objects with the built-in `forEach`, arrays, and raw objects.
    // Delegates to **ECMAScript 5**'s native `forEach` if available.
    var each = _.each = _.forEach = function(obj, iterator, context) {
        if (obj == null) return;
        if (nativeForEach && obj.forEach === nativeForEach) {
            obj.forEach(iterator, context);
        } else if (obj.length === +obj.length) {
            for (var i = 0, l = obj.length; i < l; i++) {
                if (iterator.call(context, obj[i], i, obj) === breaker) return;
            }
        } else {
            for (var key in obj) {
                if (_.has(obj, key)) {
                    if (iterator.call(context, obj[key], key, obj) === breaker) return;
                }
            }
        }
    };

    // Return the results of applying the iterator to each element.
    // Delegates to **ECMAScript 5**'s native `map` if available.
    _.map = _.collect = function(obj, iterator, context) {
        var results = [];
        if (obj == null) return results;
        if (nativeMap && obj.map === nativeMap) return obj.map(iterator, context);
        each(obj, function(value, index, list) {
            results[results.length] = iterator.call(context, value, index, list);
        });
        return results;
    };

    // **Reduce** builds up a single result from a list of values, aka `inject`,
    // or `foldl`. Delegates to **ECMAScript 5**'s native `reduce` if available.
    _.reduce = _.foldl = _.inject = function(obj, iterator, memo, context) {
        var initial = arguments.length > 2;
        if (obj == null) obj = [];
        if (nativeReduce && obj.reduce === nativeReduce) {
            if (context) iterator = _.bind(iterator, context);
            return initial ? obj.reduce(iterator, memo) : obj.reduce(iterator);
        }
        each(obj, function(value, index, list) {
            if (!initial) {
                memo = value;
                initial = true;
            } else {
                memo = iterator.call(context, memo, value, index, list);
            }
        });
        if (!initial) throw new TypeError('Reduce of empty array with no initial value');
        return memo;
    };

    // The right-associative version of reduce, also known as `foldr`.
    // Delegates to **ECMAScript 5**'s native `reduceRight` if available.
    _.reduceRight = _.foldr = function(obj, iterator, memo, context) {
        var initial = arguments.length > 2;
        if (obj == null) obj = [];
        if (nativeReduceRight && obj.reduceRight === nativeReduceRight) {
            if (context) iterator = _.bind(iterator, context);
            return arguments.length > 2 ? obj.reduceRight(iterator, memo) : obj.reduceRight(iterator);
        }
        var length = obj.length;
        if (length !== +length) {
            var keys = _.keys(obj);
            length = keys.length;
        }
        each(obj, function(value, index, list) {
            index = keys ? keys[--length] : --length;
            if (!initial) {
                memo = obj[index];
                initial = true;
            } else {
                memo = iterator.call(context, memo, obj[index], index, list);
            }
        });
        if (!initial) throw new TypeError('Reduce of empty array with no initial value');
        return memo;
    };

    // Return the first value which passes a truth test. Aliased as `detect`.
    _.find = _.detect = function(obj, iterator, context) {
        var result;
        any(obj, function(value, index, list) {
            if (iterator.call(context, value, index, list)) {
                result = value;
                return true;
            }
        });
        return result;
    };

    // Return all the elements that pass a truth test.
    // Delegates to **ECMAScript 5**'s native `filter` if available.
    // Aliased as `select`.
    _.filter = _.select = function(obj, iterator, context) {
        var results = [];
        if (obj == null) return results;
        if (nativeFilter && obj.filter === nativeFilter) return obj.filter(iterator, context);
        each(obj, function(value, index, list) {
            if (iterator.call(context, value, index, list)) results[results.length] = value;
        });
        return results;
    };

    // Return all the elements for which a truth test fails.
    _.reject = function(obj, iterator, context) {
        var results = [];
        if (obj == null) return results;
        each(obj, function(value, index, list) {
            if (!iterator.call(context, value, index, list)) results[results.length] = value;
        });
        return results;
    };

    // Determine whether all of the elements match a truth test.
    // Delegates to **ECMAScript 5**'s native `every` if available.
    // Aliased as `all`.
    _.every = _.all = function(obj, iterator, context) {
        iterator || (iterator = _.identity);
        var result = true;
        if (obj == null) return result;
        if (nativeEvery && obj.every === nativeEvery) return obj.every(iterator, context);
        each(obj, function(value, index, list) {
            if (!(result = result && iterator.call(context, value, index, list))) return breaker;
        });
        return !!result;
    };

    // Determine if at least one element in the object matches a truth test.
    // Delegates to **ECMAScript 5**'s native `some` if available.
    // Aliased as `any`.
    var any = _.some = _.any = function(obj, iterator, context) {
        iterator || (iterator = _.identity);
        var result = false;
        if (obj == null) return result;
        if (nativeSome && obj.some === nativeSome) return obj.some(iterator, context);
        each(obj, function(value, index, list) {
            if (result || (result = iterator.call(context, value, index, list))) return breaker;
        });
        return !!result;
    };

    // Determine if the array or object contains a given value (using `===`).
    // Aliased as `include`.
    _.contains = _.include = function(obj, target) {
        var found = false;
        if (obj == null) return found;
        if (nativeIndexOf && obj.indexOf === nativeIndexOf) return obj.indexOf(target) != -1;
        found = any(obj, function(value) {
            return value === target;
        });
        return found;
    };

    // Invoke a method (with arguments) on every item in a collection.
    _.invoke = function(obj, method) {
        var args = slice.call(arguments, 2);
        return _.map(obj, function(value) {
            return (_.isFunction(method) ? method : value[method]).apply(value, args);
        });
    };

    // Convenience version of a common use case of `map`: fetching a property.
    _.pluck = function(obj, key) {
        return _.map(obj, function(value){ return value[key]; });
    };

    // Convenience version of a common use case of `filter`: selecting only objects
    // with specific `key:value` pairs.
    _.where = function(obj, attrs) {
        if (_.isEmpty(attrs)) return [];
        return _.filter(obj, function(value) {
            for (var key in attrs) {
                if (attrs[key] !== value[key]) return false;
            }
            return true;
        });
    };

    // Return the maximum element or (element-based computation).
    // Can't optimize arrays of integers longer than 65,535 elements.
    // See: https://bugs.webkit.org/show_bug.cgi?id=80797
    _.max = function(obj, iterator, context) {
        if (!iterator && _.isArray(obj) && obj[0] === +obj[0] && obj.length < 65535) {
            return Math.max.apply(Math, obj);
        }
        if (!iterator && _.isEmpty(obj)) return -Infinity;
        var result = {computed : -Infinity};
        each(obj, function(value, index, list) {
            var computed = iterator ? iterator.call(context, value, index, list) : value;
            computed >= result.computed && (result = {value : value, computed : computed});
        });
        return result.value;
    };

    // Return the minimum element (or element-based computation).
    _.min = function(obj, iterator, context) {
        if (!iterator && _.isArray(obj) && obj[0] === +obj[0] && obj.length < 65535) {
            return Math.min.apply(Math, obj);
        }
        if (!iterator && _.isEmpty(obj)) return Infinity;
        var result = {computed : Infinity};
        each(obj, function(value, index, list) {
            var computed = iterator ? iterator.call(context, value, index, list) : value;
            computed < result.computed && (result = {value : value, computed : computed});
        });
        return result.value;
    };

    // Shuffle an array.
    _.shuffle = function(obj) {
        var rand;
        var index = 0;
        var shuffled = [];
        each(obj, function(value) {
            rand = _.random(index++);
            shuffled[index - 1] = shuffled[rand];
            shuffled[rand] = value;
        });
        return shuffled;
    };

    // An internal function to generate lookup iterators.
    var lookupIterator = function(value) {
        return _.isFunction(value) ? value : function(obj){ return obj[value]; };
    };

    // Sort the object's values by a criterion produced by an iterator.
    _.sortBy = function(obj, value, context) {
        var iterator = lookupIterator(value);
        return _.pluck(_.map(obj, function(value, index, list) {
            return {
                value : value,
                index : index,
                criteria : iterator.call(context, value, index, list)
            };
        }).sort(function(left, right) {
                var a = left.criteria;
                var b = right.criteria;
                if (a !== b) {
                    if (a > b || a === void 0) return 1;
                    if (a < b || b === void 0) return -1;
                }
                return left.index < right.index ? -1 : 1;
            }), 'value');
    };

    // An internal function used for aggregate "group by" operations.
    var group = function(obj, value, context, behavior) {
        var result = {};
        var iterator = lookupIterator(value);
        each(obj, function(value, index) {
            var key = iterator.call(context, value, index, obj);
            behavior(result, key, value);
        });
        return result;
    };

    // Groups the object's values by a criterion. Pass either a string attribute
    // to group by, or a function that returns the criterion.
    _.groupBy = function(obj, value, context) {
        return group(obj, value, context, function(result, key, value) {
            (_.has(result, key) ? result[key] : (result[key] = [])).push(value);
        });
    };

    // Counts instances of an object that group by a certain criterion. Pass
    // either a string attribute to count by, or a function that returns the
    // criterion.
    _.countBy = function(obj, value, context) {
        return group(obj, value, context, function(result, key, value) {
            if (!_.has(result, key)) result[key] = 0;
            result[key]++;
        });
    };

    // Use a comparator function to figure out the smallest index at which
    // an object should be inserted so as to maintain order. Uses binary search.
    _.sortedIndex = function(array, obj, iterator, context) {
        iterator = iterator == null ? _.identity : lookupIterator(iterator);
        var value = iterator.call(context, obj);
        var low = 0, high = array.length;
        while (low < high) {
            var mid = (low + high) >>> 1;
            iterator.call(context, array[mid]) < value ? low = mid + 1 : high = mid;
        }
        return low;
    };

    // Safely convert anything iterable into a real, live array.
    _.toArray = function(obj) {
        if (!obj) return [];
        if (obj.length === +obj.length) return slice.call(obj);
        return _.values(obj);
    };

    // Return the number of elements in an object.
    _.size = function(obj) {
        return (obj.length === +obj.length) ? obj.length : _.keys(obj).length;
    };

    // Array Functions
    // ---------------

    // Get the first element of an array. Passing **n** will return the first N
    // values in the array. Aliased as `head` and `take`. The **guard** check
    // allows it to work with `_.map`.
    _.first = _.head = _.take = function(array, n, guard) {
        return (n != null) && !guard ? slice.call(array, 0, n) : array[0];
    };

    // Returns everything but the last entry of the array. Especially useful on
    // the arguments object. Passing **n** will return all the values in
    // the array, excluding the last N. The **guard** check allows it to work with
    // `_.map`.
    _.initial = function(array, n, guard) {
        return slice.call(array, 0, array.length - ((n == null) || guard ? 1 : n));
    };

    // Get the last element of an array. Passing **n** will return the last N
    // values in the array. The **guard** check allows it to work with `_.map`.
    _.last = function(array, n, guard) {
        if ((n != null) && !guard) {
            return slice.call(array, Math.max(array.length - n, 0));
        } else {
            return array[array.length - 1];
        }
    };

    // Returns everything but the first entry of the array. Aliased as `tail` and `drop`.
    // Especially useful on the arguments object. Passing an **n** will return
    // the rest N values in the array. The **guard**
    // check allows it to work with `_.map`.
    _.rest = _.tail = _.drop = function(array, n, guard) {
        return slice.call(array, (n == null) || guard ? 1 : n);
    };

    // Trim out all falsy values from an array.
    _.compact = function(array) {
        return _.filter(array, function(value){ return !!value; });
    };

    // Internal implementation of a recursive `flatten` function.
    var flatten = function(input, shallow, output) {
        each(input, function(value) {
            if (_.isArray(value)) {
                shallow ? push.apply(output, value) : flatten(value, shallow, output);
            } else {
                output.push(value);
            }
        });
        return output;
    };

    // Return a completely flattened version of an array.
    _.flatten = function(array, shallow) {
        return flatten(array, shallow, []);
    };

    // Return a version of the array that does not contain the specified value(s).
    _.without = function(array) {
        return _.difference(array, slice.call(arguments, 1));
    };

    // Produce a duplicate-free version of the array. If the array has already
    // been sorted, you have the option of using a faster algorithm.
    // Aliased as `unique`.
    _.uniq = _.unique = function(array, isSorted, iterator, context) {
        var initial = iterator ? _.map(array, iterator, context) : array;
        var results = [];
        var seen = [];
        each(initial, function(value, index) {
            if (isSorted ? (!index || seen[seen.length - 1] !== value) : !_.contains(seen, value)) {
                seen.push(value);
                results.push(array[index]);
            }
        });
        return results;
    };

    // Produce an array that contains the union: each distinct element from all of
    // the passed-in arrays.
    _.union = function() {
        return _.uniq(concat.apply(ArrayProto, arguments));
    };

    // Produce an array that contains every item shared between all the
    // passed-in arrays.
    _.intersection = function(array) {
        var rest = slice.call(arguments, 1);
        return _.filter(_.uniq(array), function(item) {
            return _.every(rest, function(other) {
                return _.indexOf(other, item) >= 0;
            });
        });
    };

    // Take the difference between one array and a number of other arrays.
    // Only the elements present in just the first array will remain.
    _.difference = function(array) {
        var rest = concat.apply(ArrayProto, slice.call(arguments, 1));
        return _.filter(array, function(value){ return !_.contains(rest, value); });
    };

    // Zip together multiple lists into a single array -- elements that share
    // an index go together.
    _.zip = function() {
        var args = slice.call(arguments);
        var length = _.max(_.pluck(args, 'length'));
        var results = new Array(length);
        for (var i = 0; i < length; i++) {
            results[i] = _.pluck(args, "" + i);
        }
        return results;
    };

    // Converts lists into objects. Pass either a single array of `[key, value]`
    // pairs, or two parallel arrays of the same length -- one of keys, and one of
    // the corresponding values.
    _.object = function(list, values) {
        var result = {};
        for (var i = 0, l = list.length; i < l; i++) {
            if (values) {
                result[list[i]] = values[i];
            } else {
                result[list[i][0]] = list[i][1];
            }
        }
        return result;
    };

    // If the browser doesn't supply us with indexOf (I'm looking at you, **MSIE**),
    // we need this function. Return the position of the first occurrence of an
    // item in an array, or -1 if the item is not included in the array.
    // Delegates to **ECMAScript 5**'s native `indexOf` if available.
    // If the array is large and already in sort order, pass `true`
    // for **isSorted** to use binary search.
    _.indexOf = function(array, item, isSorted) {
        if (array == null) return -1;
        var i = 0, l = array.length;
        if (isSorted) {
            if (typeof isSorted == 'number') {
                i = (isSorted < 0 ? Math.max(0, l + isSorted) : isSorted);
            } else {
                i = _.sortedIndex(array, item);
                return array[i] === item ? i : -1;
            }
        }
        if (nativeIndexOf && array.indexOf === nativeIndexOf) return array.indexOf(item, isSorted);
        for (; i < l; i++) if (array[i] === item) return i;
        return -1;
    };

    // Delegates to **ECMAScript 5**'s native `lastIndexOf` if available.
    _.lastIndexOf = function(array, item, from) {
        if (array == null) return -1;
        var hasIndex = from != null;
        if (nativeLastIndexOf && array.lastIndexOf === nativeLastIndexOf) {
            return hasIndex ? array.lastIndexOf(item, from) : array.lastIndexOf(item);
        }
        var i = (hasIndex ? from : array.length);
        while (i--) if (array[i] === item) return i;
        return -1;
    };

    // Generate an integer Array containing an arithmetic progression. A port of
    // the native Python `range()` function. See
    // [the Python documentation](http://docs.python.org/library/functions.html#range).
    _.range = function(start, stop, step) {
        if (arguments.length <= 1) {
            stop = start || 0;
            start = 0;
        }
        step = arguments[2] || 1;

        var len = Math.max(Math.ceil((stop - start) / step), 0);
        var idx = 0;
        var range = new Array(len);

        while(idx < len) {
            range[idx++] = start;
            start += step;
        }

        return range;
    };

    // Function (ahem) Functions
    // ------------------

    // Reusable constructor function for prototype setting.
    var ctor = function(){};

    // Create a function bound to a given object (assigning `this`, and arguments,
    // optionally). Binding with arguments is also known as `curry`.
    // Delegates to **ECMAScript 5**'s native `Function.bind` if available.
    // We check for `func.bind` first, to fail fast when `func` is undefined.
    _.bind = function bind(func, context) {
        var bound, args;
        if (func.bind === nativeBind && nativeBind) return nativeBind.apply(func, slice.call(arguments, 1));
        if (!_.isFunction(func)) throw new TypeError;
        args = slice.call(arguments, 2);
        return bound = function() {
            if (!(this instanceof bound)) return func.apply(context, args.concat(slice.call(arguments)));
            ctor.prototype = func.prototype;
            var self = new ctor;
            var result = func.apply(self, args.concat(slice.call(arguments)));
            if (Object(result) === result) return result;
            return self;
        };
    };

    // Bind all of an object's methods to that object. Useful for ensuring that
    // all callbacks defined on an object belong to it.
    _.bindAll = function(obj) {
        var funcs = slice.call(arguments, 1);
        if (funcs.length == 0) funcs = _.functions(obj);
        each(funcs, function(f) { obj[f] = _.bind(obj[f], obj); });
        return obj;
    };

    // Memoize an expensive function by storing its results.
    _.memoize = function(func, hasher) {
        var memo = {};
        hasher || (hasher = _.identity);
        return function() {
            var key = hasher.apply(this, arguments);
            return _.has(memo, key) ? memo[key] : (memo[key] = func.apply(this, arguments));
        };
    };

    // Delays a function for the given number of milliseconds, and then calls
    // it with the arguments supplied.
    _.delay = function(func, wait) {
        var args = slice.call(arguments, 2);
        return setTimeout(function(){ return func.apply(null, args); }, wait);
    };

    // Defers a function, scheduling it to run after the current call stack has
    // cleared.
    _.defer = function(func) {
        return _.delay.apply(_, [func, 1].concat(slice.call(arguments, 1)));
    };

    // Returns a function, that, when invoked, will only be triggered at most once
    // during a given window of time.
    _.throttle = function(func, wait) {
        var context, args, timeout, throttling, more, result;
        var whenDone = _.debounce(function(){ more = throttling = false; }, wait);
        return function() {
            context = this; args = arguments;
            var later = function() {
                timeout = null;
                if (more) {
                    result = func.apply(context, args);
                }
                whenDone();
            };
            if (!timeout) timeout = setTimeout(later, wait);
            if (throttling) {
                more = true;
            } else {
                throttling = true;
                result = func.apply(context, args);
            }
            whenDone();
            return result;
        };
    };

    // Returns a function, that, as long as it continues to be invoked, will not
    // be triggered. The function will be called after it stops being called for
    // N milliseconds. If `immediate` is passed, trigger the function on the
    // leading edge, instead of the trailing.
    _.debounce = function(func, wait, immediate) {
        var timeout, result;
        return function() {
            var context = this, args = arguments;
            var later = function() {
                timeout = null;
                if (!immediate) result = func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) result = func.apply(context, args);
            return result;
        };
    };

    // Returns a function that will be executed at most one time, no matter how
    // often you call it. Useful for lazy initialization.
    _.once = function(func) {
        var ran = false, memo;
        return function() {
            if (ran) return memo;
            ran = true;
            memo = func.apply(this, arguments);
            func = null;
            return memo;
        };
    };

    // Returns the first function passed as an argument to the second,
    // allowing you to adjust arguments, run code before and after, and
    // conditionally execute the original function.
    _.wrap = function(func, wrapper) {
        return function() {
            var args = [func];
            push.apply(args, arguments);
            return wrapper.apply(this, args);
        };
    };

    // Returns a function that is the composition of a list of functions, each
    // consuming the return value of the function that follows.
    _.compose = function() {
        var funcs = arguments;
        return function() {
            var args = arguments;
            for (var i = funcs.length - 1; i >= 0; i--) {
                args = [funcs[i].apply(this, args)];
            }
            return args[0];
        };
    };

    // Returns a function that will only be executed after being called N times.
    _.after = function(times, func) {
        if (times <= 0) return func();
        return function() {
            if (--times < 1) {
                return func.apply(this, arguments);
            }
        };
    };

    // Object Functions
    // ----------------

    // Retrieve the names of an object's properties.
    // Delegates to **ECMAScript 5**'s native `Object.keys`
    _.keys = nativeKeys || function(obj) {
        if (obj !== Object(obj)) throw new TypeError('Invalid object');
        var keys = [];
        for (var key in obj) if (_.has(obj, key)) keys[keys.length] = key;
        return keys;
    };

    // Retrieve the values of an object's properties.
    _.values = function(obj) {
        var values = [];
        for (var key in obj) if (_.has(obj, key)) values.push(obj[key]);
        return values;
    };

    // Convert an object into a list of `[key, value]` pairs.
    _.pairs = function(obj) {
        var pairs = [];
        for (var key in obj) if (_.has(obj, key)) pairs.push([key, obj[key]]);
        return pairs;
    };

    // Invert the keys and values of an object. The values must be serializable.
    _.invert = function(obj) {
        var result = {};
        for (var key in obj) if (_.has(obj, key)) result[obj[key]] = key;
        return result;
    };

    // Return a sorted list of the function names available on the object.
    // Aliased as `methods`
    _.functions = _.methods = function(obj) {
        var names = [];
        for (var key in obj) {
            if (_.isFunction(obj[key])) names.push(key);
        }
        return names.sort();
    };

    // Extend a given object with all the properties in passed-in object(s).
    _.extend = function(obj) {
        each(slice.call(arguments, 1), function(source) {
            for (var prop in source) {
                obj[prop] = source[prop];
            }
        });
        return obj;
    };

    // Return a copy of the object only containing the whitelisted properties.
    _.pick = function(obj) {
        var copy = {};
        var keys = concat.apply(ArrayProto, slice.call(arguments, 1));
        each(keys, function(key) {
            if (key in obj) copy[key] = obj[key];
        });
        return copy;
    };

    // Return a copy of the object without the blacklisted properties.
    _.omit = function(obj) {
        var copy = {};
        var keys = concat.apply(ArrayProto, slice.call(arguments, 1));
        for (var key in obj) {
            if (!_.contains(keys, key)) copy[key] = obj[key];
        }
        return copy;
    };

    // Fill in a given object with default properties.
    _.defaults = function(obj) {
        each(slice.call(arguments, 1), function(source) {
            for (var prop in source) {
                if (obj[prop] == null) obj[prop] = source[prop];
            }
        });
        return obj;
    };

    // Create a (shallow-cloned) duplicate of an object.
    _.clone = function(obj) {
        if (!_.isObject(obj)) return obj;
        return _.isArray(obj) ? obj.slice() : _.extend({}, obj);
    };

    // Invokes interceptor with the obj, and then returns obj.
    // The primary purpose of this method is to "tap into" a method chain, in
    // order to perform operations on intermediate results within the chain.
    _.tap = function(obj, interceptor) {
        interceptor(obj);
        return obj;
    };

    // Internal recursive comparison function for `isEqual`.
    var eq = function(a, b, aStack, bStack) {
        // Identical objects are equal. `0 === -0`, but they aren't identical.
        // See the Harmony `egal` proposal: http://wiki.ecmascript.org/doku.php?id=harmony:egal.
        if (a === b) return a !== 0 || 1 / a == 1 / b;
        // A strict comparison is necessary because `null == undefined`.
        if (a == null || b == null) return a === b;
        // Unwrap any wrapped objects.
        if (a instanceof _) a = a._wrapped;
        if (b instanceof _) b = b._wrapped;
        // Compare `[[Class]]` names.
        var className = toString.call(a);
        if (className != toString.call(b)) return false;
        switch (className) {
            // Strings, numbers, dates, and booleans are compared by value.
            case '[object String]':
                // Primitives and their corresponding object wrappers are equivalent; thus, `"5"` is
                // equivalent to `new String("5")`.
                return a == String(b);
            case '[object Number]':
                // `NaN`s are equivalent, but non-reflexive. An `egal` comparison is performed for
                // other numeric values.
                return a != +a ? b != +b : (a == 0 ? 1 / a == 1 / b : a == +b);
            case '[object Date]':
            case '[object Boolean]':
                // Coerce dates and booleans to numeric primitive values. Dates are compared by their
                // millisecond representations. Note that invalid dates with millisecond representations
                // of `NaN` are not equivalent.
                return +a == +b;
            // RegExps are compared by their source patterns and flags.
            case '[object RegExp]':
                return a.source == b.source &&
                    a.global == b.global &&
                    a.multiline == b.multiline &&
                    a.ignoreCase == b.ignoreCase;
        }
        if (typeof a != 'object' || typeof b != 'object') return false;
        // Assume equality for cyclic structures. The algorithm for detecting cyclic
        // structures is adapted from ES 5.1 section 15.12.3, abstract operation `JO`.
        var length = aStack.length;
        while (length--) {
            // Linear search. Performance is inversely proportional to the number of
            // unique nested structures.
            if (aStack[length] == a) return bStack[length] == b;
        }
        // Add the first object to the stack of traversed objects.
        aStack.push(a);
        bStack.push(b);
        var size = 0, result = true;
        // Recursively compare objects and arrays.
        if (className == '[object Array]') {
            // Compare array lengths to determine if a deep comparison is necessary.
            size = a.length;
            result = size == b.length;
            if (result) {
                // Deep compare the contents, ignoring non-numeric properties.
                while (size--) {
                    if (!(result = eq(a[size], b[size], aStack, bStack))) break;
                }
            }
        } else {
            // Objects with different constructors are not equivalent, but `Object`s
            // from different frames are.
            var aCtor = a.constructor, bCtor = b.constructor;
            if (aCtor !== bCtor && !(_.isFunction(aCtor) && (aCtor instanceof aCtor) &&
                _.isFunction(bCtor) && (bCtor instanceof bCtor))) {
                return false;
            }
            // Deep compare objects.
            for (var key in a) {
                if (_.has(a, key)) {
                    // Count the expected number of properties.
                    size++;
                    // Deep compare each member.
                    if (!(result = _.has(b, key) && eq(a[key], b[key], aStack, bStack))) break;
                }
            }
            // Ensure that both objects contain the same number of properties.
            if (result) {
                for (key in b) {
                    if (_.has(b, key) && !(size--)) break;
                }
                result = !size;
            }
        }
        // Remove the first object from the stack of traversed objects.
        aStack.pop();
        bStack.pop();
        return result;
    };

    // Perform a deep comparison to check if two objects are equal.
    _.isEqual = function(a, b) {
        return eq(a, b, [], []);
    };

    // Is a given array, string, or object empty?
    // An "empty" object has no enumerable own-properties.
    _.isEmpty = function(obj) {
        if (obj == null) return true;
        if (_.isArray(obj) || _.isString(obj)) return obj.length === 0;
        for (var key in obj) if (_.has(obj, key)) return false;
        return true;
    };

    // Is a given value a DOM element?
    _.isElement = function(obj) {
        return !!(obj && obj.nodeType === 1);
    };

    // Is a given value an array?
    // Delegates to ECMA5's native Array.isArray
    _.isArray = nativeIsArray || function(obj) {
        return toString.call(obj) == '[object Array]';
    };

    // Is a given variable an object?
    _.isObject = function(obj) {
        return obj === Object(obj);
    };

    // Add some isType methods: isArguments, isFunction, isString, isNumber, isDate, isRegExp.
    each(['Arguments', 'Function', 'String', 'Number', 'Date', 'RegExp'], function(name) {
        _['is' + name] = function(obj) {
            return toString.call(obj) == '[object ' + name + ']';
        };
    });

    // Define a fallback version of the method in browsers (ahem, IE), where
    // there isn't any inspectable "Arguments" type.
    if (!_.isArguments(arguments)) {
        _.isArguments = function(obj) {
            return !!(obj && _.has(obj, 'callee'));
        };
    }

    // Optimize `isFunction` if appropriate.
    if (typeof (/./) !== 'function') {
        _.isFunction = function(obj) {
            return typeof obj === 'function';
        };
    }

    // Is a given object a finite number?
    _.isFinite = function(obj) {
        return _.isNumber(obj) && isFinite(obj);
    };

    // Is the given value `NaN`? (NaN is the only number which does not equal itself).
    _.isNaN = function(obj) {
        return _.isNumber(obj) && obj != +obj;
    };

    // Is a given value a boolean?
    _.isBoolean = function(obj) {
        return obj === true || obj === false || toString.call(obj) == '[object Boolean]';
    };

    // Is a given value equal to null?
    _.isNull = function(obj) {
        return obj === null;
    };

    // Is a given variable undefined?
    _.isUndefined = function(obj) {
        return obj === void 0;
    };

    // Shortcut function for checking if an object has a given property directly
    // on itself (in other words, not on a prototype).
    _.has = function(obj, key) {
        return hasOwnProperty.call(obj, key);
    };

    // Utility Functions
    // -----------------

    // Run Underscore.js in *noConflict* mode, returning the `_` variable to its
    // previous owner. Returns a reference to the Underscore object.
    _.noConflict = function() {
        root._ = previousUnderscore;
        return this;
    };

    // Keep the identity function around for default iterators.
    _.identity = function(value) {
        return value;
    };

    // Run a function **n** times.
    _.times = function(n, iterator, context) {
        for (var i = 0; i < n; i++) iterator.call(context, i);
    };

    // Return a random integer between min and max (inclusive).
    _.random = function(min, max) {
        if (max == null) {
            max = min;
            min = 0;
        }
        return min + (0 | Math.random() * (max - min + 1));
    };

    // List of HTML entities for escaping.
    var entityMap = {
        escape: {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#x27;',
            '/': '&#x2F;'
        }
    };
    entityMap.unescape = _.invert(entityMap.escape);

    // Regexes containing the keys and values listed immediately above.
    var entityRegexes = {
        escape:   new RegExp('[' + _.keys(entityMap.escape).join('') + ']', 'g'),
        unescape: new RegExp('(' + _.keys(entityMap.unescape).join('|') + ')', 'g')
    };

    // Functions for escaping and unescaping strings to/from HTML interpolation.
    _.each(['escape', 'unescape'], function(method) {
        _[method] = function(string) {
            if (string == null) return '';
            return ('' + string).replace(entityRegexes[method], function(match) {
                return entityMap[method][match];
            });
        };
    });

    // If the value of the named property is a function then invoke it;
    // otherwise, return it.
    _.result = function(object, property) {
        if (object == null) return null;
        var value = object[property];
        return _.isFunction(value) ? value.call(object) : value;
    };

    // Add your own custom functions to the Underscore object.
    _.mixin = function(obj) {
        each(_.functions(obj), function(name){
            var func = _[name] = obj[name];
            _.prototype[name] = function() {
                var args = [this._wrapped];
                push.apply(args, arguments);
                return result.call(this, func.apply(_, args));
            };
        });
    };

    // Generate a unique integer id (unique within the entire client session).
    // Useful for temporary DOM ids.
    var idCounter = 0;
    _.uniqueId = function(prefix) {
        var id = idCounter++;
        return prefix ? prefix + id : id;
    };

    // By default, Underscore uses ERB-style template delimiters, change the
    // following template settings to use alternative delimiters.
    _.templateSettings = {
        evaluate    : /<%([\s\S]+?)%>/g,
        interpolate : /<%=([\s\S]+?)%>/g,
        escape      : /<%-([\s\S]+?)%>/g
    };

    // When customizing `templateSettings`, if you don't want to define an
    // interpolation, evaluation or escaping regex, we need one that is
    // guaranteed not to match.
    var noMatch = /(.)^/;

    // Certain characters need to be escaped so that they can be put into a
    // string literal.
    var escapes = {
        "'":      "'",
        '\\':     '\\',
        '\r':     'r',
        '\n':     'n',
        '\t':     't',
        '\u2028': 'u2028',
        '\u2029': 'u2029'
    };

    var escaper = /\\|'|\r|\n|\t|\u2028|\u2029/g;

    // JavaScript micro-templating, similar to John Resig's implementation.
    // Underscore templating handles arbitrary delimiters, preserves whitespace,
    // and correctly escapes quotes within interpolated code.
    _.template = function(text, data, settings) {
        settings = _.defaults({}, settings, _.templateSettings);

        // Combine delimiters into one regular expression via alternation.
        var matcher = new RegExp([
            (settings.escape || noMatch).source,
            (settings.interpolate || noMatch).source,
            (settings.evaluate || noMatch).source
        ].join('|') + '|$', 'g');

        // Compile the template source, escaping string literals appropriately.
        var index = 0;
        var source = "__p+='";
        text.replace(matcher, function(match, escape, interpolate, evaluate, offset) {
            source += text.slice(index, offset)
                .replace(escaper, function(match) { return '\\' + escapes[match]; });
            source +=
                escape ? "'+\n((__t=(" + escape + "))==null?'':_.escape(__t))+\n'" :
                    interpolate ? "'+\n((__t=(" + interpolate + "))==null?'':__t)+\n'" :
                        evaluate ? "';\n" + evaluate + "\n__p+='" : '';
            index = offset + match.length;
        });
        source += "';\n";

        // If a variable is not specified, place data values in local scope.
        if (!settings.variable) source = 'with(obj||{}){\n' + source + '}\n';

        source = "var __t,__p='',__j=Array.prototype.join," +
            "print=function(){__p+=__j.call(arguments,'');};\n" +
            source + "return __p;\n";

        try {
            var render = new Function(settings.variable || 'obj', '_', source);
        } catch (e) {
            e.source = source;
            throw e;
        }

        if (data) return render(data, _);
        var template = function(data) {
            return render.call(this, data, _);
        };

        // Provide the compiled function source as a convenience for precompilation.
        template.source = 'function(' + (settings.variable || 'obj') + '){\n' + source + '}';

        return template;
    };

    // Add a "chain" function, which will delegate to the wrapper.
    _.chain = function(obj) {
        return _(obj).chain();
    };

    // OOP
    // ---------------
    // If Underscore is called as a function, it returns a wrapped object that
    // can be used OO-style. This wrapper holds altered versions of all the
    // underscore functions. Wrapped objects may be chained.

    // Helper function to continue chaining intermediate results.
    var result = function(obj) {
        return this._chain ? _(obj).chain() : obj;
    };

    // Add all of the Underscore functions to the wrapper object.
    _.mixin(_);

    // Add all mutator Array functions to the wrapper.
    each(['pop', 'push', 'reverse', 'shift', 'sort', 'splice', 'unshift'], function(name) {
        var method = ArrayProto[name];
        _.prototype[name] = function() {
            var obj = this._wrapped;
            method.apply(obj, arguments);
            if ((name == 'shift' || name == 'splice') && obj.length === 0) delete obj[0];
            return result.call(this, obj);
        };
    });

    // Add all accessor Array functions to the wrapper.
    each(['concat', 'join', 'slice'], function(name) {
        var method = ArrayProto[name];
        _.prototype[name] = function() {
            return result.call(this, method.apply(this._wrapped, arguments));
        };
    });

    _.extend(_.prototype, {

        // Start chaining a wrapped Underscore object.
        chain: function() {
            this._chain = true;
            return this;
        },

        // Extracts the result from a wrapped and chained object.
        value: function() {
            return this._wrapped;
        }

    });

}).call(this);
/*!
 * jQuery JavaScript Library v1.8.1
 * http://jquery.com/
 *
 * Includes Sizzle.js
 * http://sizzlejs.com/
 *
 * Copyright 2012 jQuery Foundation and other contributors
 * Released under the MIT license
 * http://jquery.org/license
 *
 * Date: Thu Aug 30 2012 17:17:22 GMT-0400 (Eastern Daylight Time)
 */
(function( window, undefined ) {
    var
    // A central reference to the root jQuery(document)
        rootjQuery,

    // The deferred used on DOM ready
        readyList,

    // Use the correct document accordingly with window argument (sandbox)
        document = window.document,
        location = window.location,
        navigator = window.navigator,

    // Map over jQuery in case of overwrite
        _jQuery = window.jQuery,

    // Map over the $ in case of overwrite
        _$ = window.$,

    // Save a reference to some core methods
        core_push = Array.prototype.push,
        core_slice = Array.prototype.slice,
        core_indexOf = Array.prototype.indexOf,
        core_toString = Object.prototype.toString,
        core_hasOwn = Object.prototype.hasOwnProperty,
        core_trim = String.prototype.trim,

    // Define a local copy of jQuery
        jQuery = function( selector, context ) {
            // The jQuery object is actually just the init constructor 'enhanced'
            return new jQuery.fn.init( selector, context, rootjQuery );
        },

    // Used for matching numbers
        core_pnum = /[\-+]?(?:\d*\.|)\d+(?:[eE][\-+]?\d+|)/.source,

    // Used for detecting and trimming whitespace
        core_rnotwhite = /\S/,
        core_rspace = /\s+/,

    // Make sure we trim BOM and NBSP (here's looking at you, Safari 5.0 and IE)
        rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,

    // A simple way to check for HTML strings
    // Prioritize #id over <tag> to avoid XSS via location.hash (#9521)
        rquickExpr = /^(?:[^#<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/,

    // Match a standalone tag
        rsingleTag = /^<(\w+)\s*\/?>(?:<\/\1>|)$/,

    // JSON RegExp
        rvalidchars = /^[\],:{}\s]*$/,
        rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g,
        rvalidescape = /\\(?:["\\\/bfnrt]|u[\da-fA-F]{4})/g,
        rvalidtokens = /"[^"\\\r\n]*"|true|false|null|-?(?:\d\d*\.|)\d+(?:[eE][\-+]?\d+|)/g,

    // Matches dashed string for camelizing
        rmsPrefix = /^-ms-/,
        rdashAlpha = /-([\da-z])/gi,

    // Used by jQuery.camelCase as callback to replace()
        fcamelCase = function( all, letter ) {
            return ( letter + "" ).toUpperCase();
        },

    // The ready event handler and self cleanup method
        DOMContentLoaded = function() {
            if ( document.addEventListener ) {
                document.removeEventListener( "DOMContentLoaded", DOMContentLoaded, false );
                jQuery.ready();
            } else if ( document.readyState === "complete" ) {
                // we're here because readyState === "complete" in oldIE
                // which is good enough for us to call the dom ready!
                document.detachEvent( "onreadystatechange", DOMContentLoaded );
                jQuery.ready();
            }
        },

    // [[Class]] -> type pairs
        class2type = {};

    jQuery.fn = jQuery.prototype = {
        constructor: jQuery,
        init: function( selector, context, rootjQuery ) {
            var match, elem, ret, doc;

            // Handle $(""), $(null), $(undefined), $(false)
            if ( !selector ) {
                return this;
            }

            // Handle $(DOMElement)
            if ( selector.nodeType ) {
                this.context = this[0] = selector;
                this.length = 1;
                return this;
            }

            // Handle HTML strings
            if ( typeof selector === "string" ) {
                if ( selector.charAt(0) === "<" && selector.charAt( selector.length - 1 ) === ">" && selector.length >= 3 ) {
                    // Assume that strings that start and end with <> are HTML and skip the regex check
                    match = [ null, selector, null ];

                } else {
                    match = rquickExpr.exec( selector );
                }

                // Match html or make sure no context is specified for #id
                if ( match && (match[1] || !context) ) {

                    // HANDLE: $(html) -> $(array)
                    if ( match[1] ) {
                        context = context instanceof jQuery ? context[0] : context;
                        doc = ( context && context.nodeType ? context.ownerDocument || context : document );

                        // scripts is true for back-compat
                        selector = jQuery.parseHTML( match[1], doc, true );
                        if ( rsingleTag.test( match[1] ) && jQuery.isPlainObject( context ) ) {
                            this.attr.call( selector, context, true );
                        }

                        return jQuery.merge( this, selector );

                        // HANDLE: $(#id)
                    } else {
                        elem = document.getElementById( match[2] );

                        // Check parentNode to catch when Blackberry 4.6 returns
                        // nodes that are no longer in the document #6963
                        if ( elem && elem.parentNode ) {
                            // Handle the case where IE and Opera return items
                            // by name instead of ID
                            if ( elem.id !== match[2] ) {
                                return rootjQuery.find( selector );
                            }

                            // Otherwise, we inject the element directly into the jQuery object
                            this.length = 1;
                            this[0] = elem;
                        }

                        this.context = document;
                        this.selector = selector;
                        return this;
                    }

                    // HANDLE: $(expr, $(...))
                } else if ( !context || context.jquery ) {
                    return ( context || rootjQuery ).find( selector );

                    // HANDLE: $(expr, context)
                    // (which is just equivalent to: $(context).find(expr)
                } else {
                    return this.constructor( context ).find( selector );
                }

                // HANDLE: $(function)
                // Shortcut for document ready
            } else if ( jQuery.isFunction( selector ) ) {
                return rootjQuery.ready( selector );
            }

            if ( selector.selector !== undefined ) {
                this.selector = selector.selector;
                this.context = selector.context;
            }

            return jQuery.makeArray( selector, this );
        },

        // Start with an empty selector
        selector: "",

        // The current version of jQuery being used
        jquery: "1.8.1",

        // The default length of a jQuery object is 0
        length: 0,

        // The number of elements contained in the matched element set
        size: function() {
            return this.length;
        },

        toArray: function() {
            return core_slice.call( this );
        },

        // Get the Nth element in the matched element set OR
        // Get the whole matched element set as a clean array
        get: function( num ) {
            return num == null ?

                // Return a 'clean' array
                this.toArray() :

                // Return just the object
                ( num < 0 ? this[ this.length + num ] : this[ num ] );
        },

        // Take an array of elements and push it onto the stack
        // (returning the new matched element set)
        pushStack: function( elems, name, selector ) {

            // Build a new jQuery matched element set
            var ret = jQuery.merge( this.constructor(), elems );

            // Add the old object onto the stack (as a reference)
            ret.prevObject = this;

            ret.context = this.context;

            if ( name === "find" ) {
                ret.selector = this.selector + ( this.selector ? " " : "" ) + selector;
            } else if ( name ) {
                ret.selector = this.selector + "." + name + "(" + selector + ")";
            }

            // Return the newly-formed element set
            return ret;
        },

        // Execute a callback for every element in the matched set.
        // (You can seed the arguments with an array of args, but this is
        // only used internally.)
        each: function( callback, args ) {
            return jQuery.each( this, callback, args );
        },

        ready: function( fn ) {
            // Add the callback
            jQuery.ready.promise().done( fn );

            return this;
        },

        eq: function( i ) {
            i = +i;
            return i === -1 ?
                this.slice( i ) :
                this.slice( i, i + 1 );
        },

        first: function() {
            return this.eq( 0 );
        },

        last: function() {
            return this.eq( -1 );
        },

        slice: function() {
            return this.pushStack( core_slice.apply( this, arguments ),
                "slice", core_slice.call(arguments).join(",") );
        },

        map: function( callback ) {
            return this.pushStack( jQuery.map(this, function( elem, i ) {
                return callback.call( elem, i, elem );
            }));
        },

        end: function() {
            return this.prevObject || this.constructor(null);
        },

        // For internal use only.
        // Behaves like an Array's method, not like a jQuery method.
        push: core_push,
        sort: [].sort,
        splice: [].splice
    };

// Give the init function the jQuery prototype for later instantiation
    jQuery.fn.init.prototype = jQuery.fn;

    jQuery.extend = jQuery.fn.extend = function() {
        var options, name, src, copy, copyIsArray, clone,
            target = arguments[0] || {},
            i = 1,
            length = arguments.length,
            deep = false;

        // Handle a deep copy situation
        if ( typeof target === "boolean" ) {
            deep = target;
            target = arguments[1] || {};
            // skip the boolean and the target
            i = 2;
        }

        // Handle case when target is a string or something (possible in deep copy)
        if ( typeof target !== "object" && !jQuery.isFunction(target) ) {
            target = {};
        }

        // extend jQuery itself if only one argument is passed
        if ( length === i ) {
            target = this;
            --i;
        }

        for ( ; i < length; i++ ) {
            // Only deal with non-null/undefined values
            if ( (options = arguments[ i ]) != null ) {
                // Extend the base object
                for ( name in options ) {
                    src = target[ name ];
                    copy = options[ name ];

                    // Prevent never-ending loop
                    if ( target === copy ) {
                        continue;
                    }

                    // Recurse if we're merging plain objects or arrays
                    if ( deep && copy && ( jQuery.isPlainObject(copy) || (copyIsArray = jQuery.isArray(copy)) ) ) {
                        if ( copyIsArray ) {
                            copyIsArray = false;
                            clone = src && jQuery.isArray(src) ? src : [];

                        } else {
                            clone = src && jQuery.isPlainObject(src) ? src : {};
                        }

                        // Never move original objects, clone them
                        target[ name ] = jQuery.extend( deep, clone, copy );

                        // Don't bring in undefined values
                    } else if ( copy !== undefined ) {
                        target[ name ] = copy;
                    }
                }
            }
        }

        // Return the modified object
        return target;
    };

    jQuery.extend({
        noConflict: function( deep ) {
            if ( window.$ === jQuery ) {
                window.$ = _$;
            }

            if ( deep && window.jQuery === jQuery ) {
                window.jQuery = _jQuery;
            }

            return jQuery;
        },

        // Is the DOM ready to be used? Set to true once it occurs.
        isReady: false,

        // A counter to track how many items to wait for before
        // the ready event fires. See #6781
        readyWait: 1,

        // Hold (or release) the ready event
        holdReady: function( hold ) {
            if ( hold ) {
                jQuery.readyWait++;
            } else {
                jQuery.ready( true );
            }
        },

        // Handle when the DOM is ready
        ready: function( wait ) {

            // Abort if there are pending holds or we're already ready
            if ( wait === true ? --jQuery.readyWait : jQuery.isReady ) {
                return;
            }

            // Make sure body exists, at least, in case IE gets a little overzealous (ticket #5443).
            if ( !document.body ) {
                return setTimeout( jQuery.ready, 1 );
            }

            // Remember that the DOM is ready
            jQuery.isReady = true;

            // If a normal DOM Ready event fired, decrement, and wait if need be
            if ( wait !== true && --jQuery.readyWait > 0 ) {
                return;
            }

            // If there are functions bound, to execute
            readyList.resolveWith( document, [ jQuery ] );

            // Trigger any bound ready events
            if ( jQuery.fn.trigger ) {
                jQuery( document ).trigger("ready").off("ready");
            }
        },

        // See test/unit/core.js for details concerning isFunction.
        // Since version 1.3, DOM methods and functions like alert
        // aren't supported. They return false on IE (#2968).
        isFunction: function( obj ) {
            return jQuery.type(obj) === "function";
        },

        isArray: Array.isArray || function( obj ) {
            return jQuery.type(obj) === "array";
        },

        isWindow: function( obj ) {
            return obj != null && obj == obj.window;
        },

        isNumeric: function( obj ) {
            return !isNaN( parseFloat(obj) ) && isFinite( obj );
        },

        type: function( obj ) {
            return obj == null ?
                String( obj ) :
                class2type[ core_toString.call(obj) ] || "object";
        },

        isPlainObject: function( obj ) {
            // Must be an Object.
            // Because of IE, we also have to check the presence of the constructor property.
            // Make sure that DOM nodes and window objects don't pass through, as well
            if ( !obj || jQuery.type(obj) !== "object" || obj.nodeType || jQuery.isWindow( obj ) ) {
                return false;
            }

            try {
                // Not own constructor property must be Object
                if ( obj.constructor &&
                    !core_hasOwn.call(obj, "constructor") &&
                    !core_hasOwn.call(obj.constructor.prototype, "isPrototypeOf") ) {
                    return false;
                }
            } catch ( e ) {
                // IE8,9 Will throw exceptions on certain host objects #9897
                return false;
            }

            // Own properties are enumerated firstly, so to speed up,
            // if last one is own, then all properties are own.

            var key;
            for ( key in obj ) {}

            return key === undefined || core_hasOwn.call( obj, key );
        },

        isEmptyObject: function( obj ) {
            var name;
            for ( name in obj ) {
                return false;
            }
            return true;
        },

        error: function( msg ) {
            throw new Error( msg );
        },

        // data: string of html
        // context (optional): If specified, the fragment will be created in this context, defaults to document
        // scripts (optional): If true, will include scripts passed in the html string
        parseHTML: function( data, context, scripts ) {
            var parsed;
            if ( !data || typeof data !== "string" ) {
                return null;
            }
            if ( typeof context === "boolean" ) {
                scripts = context;
                context = 0;
            }
            context = context || document;

            // Single tag
            if ( (parsed = rsingleTag.exec( data )) ) {
                return [ context.createElement( parsed[1] ) ];
            }

            parsed = jQuery.buildFragment( [ data ], context, scripts ? null : [] );
            return jQuery.merge( [],
                (parsed.cacheable ? jQuery.clone( parsed.fragment ) : parsed.fragment).childNodes );
        },

        parseJSON: function( data ) {
            if ( !data || typeof data !== "string") {
                return null;
            }

            // Make sure leading/trailing whitespace is removed (IE can't handle it)
            data = jQuery.trim( data );

            // Attempt to parse using the native JSON parser first
            if ( window.JSON && window.JSON.parse ) {
                return window.JSON.parse( data );
            }

            // Make sure the incoming data is actual JSON
            // Logic borrowed from http://json.org/json2.js
            if ( rvalidchars.test( data.replace( rvalidescape, "@" )
                .replace( rvalidtokens, "]" )
                .replace( rvalidbraces, "")) ) {

                return ( new Function( "return " + data ) )();

            }
            jQuery.error( "Invalid JSON: " + data );
        },

        // Cross-browser xml parsing
        parseXML: function( data ) {
            var xml, tmp;
            if ( !data || typeof data !== "string" ) {
                return null;
            }
            try {
                if ( window.DOMParser ) { // Standard
                    tmp = new DOMParser();
                    xml = tmp.parseFromString( data , "text/xml" );
                } else { // IE
                    xml = new ActiveXObject( "Microsoft.XMLDOM" );
                    xml.async = "false";
                    xml.loadXML( data );
                }
            } catch( e ) {
                xml = undefined;
            }
            if ( !xml || !xml.documentElement || xml.getElementsByTagName( "parsererror" ).length ) {
                jQuery.error( "Invalid XML: " + data );
            }
            return xml;
        },

        noop: function() {},

        // Evaluates a script in a global context
        // Workarounds based on findings by Jim Driscoll
        // http://weblogs.java.net/blog/driscoll/archive/2009/09/08/eval-javascript-global-context
        globalEval: function( data ) {
            if ( data && core_rnotwhite.test( data ) ) {
                // We use execScript on Internet Explorer
                // We use an anonymous function so that context is window
                // rather than jQuery in Firefox
                ( window.execScript || function( data ) {
                    window[ "eval" ].call( window, data );
                } )( data );
            }
        },

        // Convert dashed to camelCase; used by the css and data modules
        // Microsoft forgot to hump their vendor prefix (#9572)
        camelCase: function( string ) {
            return string.replace( rmsPrefix, "ms-" ).replace( rdashAlpha, fcamelCase );
        },

        nodeName: function( elem, name ) {
            return elem.nodeName && elem.nodeName.toUpperCase() === name.toUpperCase();
        },

        // args is for internal usage only
        each: function( obj, callback, args ) {
            var name,
                i = 0,
                length = obj.length,
                isObj = length === undefined || jQuery.isFunction( obj );

            if ( args ) {
                if ( isObj ) {
                    for ( name in obj ) {
                        if ( callback.apply( obj[ name ], args ) === false ) {
                            break;
                        }
                    }
                } else {
                    for ( ; i < length; ) {
                        if ( callback.apply( obj[ i++ ], args ) === false ) {
                            break;
                        }
                    }
                }

                // A special, fast, case for the most common use of each
            } else {
                if ( isObj ) {
                    for ( name in obj ) {
                        if ( callback.call( obj[ name ], name, obj[ name ] ) === false ) {
                            break;
                        }
                    }
                } else {
                    for ( ; i < length; ) {
                        if ( callback.call( obj[ i ], i, obj[ i++ ] ) === false ) {
                            break;
                        }
                    }
                }
            }

            return obj;
        },

        // Use native String.trim function wherever possible
        trim: core_trim && !core_trim.call("\uFEFF\xA0") ?
            function( text ) {
                return text == null ?
                    "" :
                    core_trim.call( text );
            } :

            // Otherwise use our own trimming functionality
            function( text ) {
                return text == null ?
                    "" :
                    text.toString().replace( rtrim, "" );
            },

        // results is for internal usage only
        makeArray: function( arr, results ) {
            var type,
                ret = results || [];

            if ( arr != null ) {
                // The window, strings (and functions) also have 'length'
                // Tweaked logic slightly to handle Blackberry 4.7 RegExp issues #6930
                type = jQuery.type( arr );

                if ( arr.length == null || type === "string" || type === "function" || type === "regexp" || jQuery.isWindow( arr ) ) {
                    core_push.call( ret, arr );
                } else {
                    jQuery.merge( ret, arr );
                }
            }

            return ret;
        },

        inArray: function( elem, arr, i ) {
            var len;

            if ( arr ) {
                if ( core_indexOf ) {
                    return core_indexOf.call( arr, elem, i );
                }

                len = arr.length;
                i = i ? i < 0 ? Math.max( 0, len + i ) : i : 0;

                for ( ; i < len; i++ ) {
                    // Skip accessing in sparse arrays
                    if ( i in arr && arr[ i ] === elem ) {
                        return i;
                    }
                }
            }

            return -1;
        },

        merge: function( first, second ) {
            var l = second.length,
                i = first.length,
                j = 0;

            if ( typeof l === "number" ) {
                for ( ; j < l; j++ ) {
                    first[ i++ ] = second[ j ];
                }

            } else {
                while ( second[j] !== undefined ) {
                    first[ i++ ] = second[ j++ ];
                }
            }

            first.length = i;

            return first;
        },

        grep: function( elems, callback, inv ) {
            var retVal,
                ret = [],
                i = 0,
                length = elems.length;
            inv = !!inv;

            // Go through the array, only saving the items
            // that pass the validator function
            for ( ; i < length; i++ ) {
                retVal = !!callback( elems[ i ], i );
                if ( inv !== retVal ) {
                    ret.push( elems[ i ] );
                }
            }

            return ret;
        },

        // arg is for internal usage only
        map: function( elems, callback, arg ) {
            var value, key,
                ret = [],
                i = 0,
                length = elems.length,
            // jquery objects are treated as arrays
                isArray = elems instanceof jQuery || length !== undefined && typeof length === "number" && ( ( length > 0 && elems[ 0 ] && elems[ length -1 ] ) || length === 0 || jQuery.isArray( elems ) ) ;

            // Go through the array, translating each of the items to their
            if ( isArray ) {
                for ( ; i < length; i++ ) {
                    value = callback( elems[ i ], i, arg );

                    if ( value != null ) {
                        ret[ ret.length ] = value;
                    }
                }

                // Go through every key on the object,
            } else {
                for ( key in elems ) {
                    value = callback( elems[ key ], key, arg );

                    if ( value != null ) {
                        ret[ ret.length ] = value;
                    }
                }
            }

            // Flatten any nested arrays
            return ret.concat.apply( [], ret );
        },

        // A global GUID counter for objects
        guid: 1,

        // Bind a function to a context, optionally partially applying any
        // arguments.
        proxy: function( fn, context ) {
            var tmp, args, proxy;

            if ( typeof context === "string" ) {
                tmp = fn[ context ];
                context = fn;
                fn = tmp;
            }

            // Quick check to determine if target is callable, in the spec
            // this throws a TypeError, but we will just return undefined.
            if ( !jQuery.isFunction( fn ) ) {
                return undefined;
            }

            // Simulated bind
            args = core_slice.call( arguments, 2 );
            proxy = function() {
                return fn.apply( context, args.concat( core_slice.call( arguments ) ) );
            };

            // Set the guid of unique handler to the same of original handler, so it can be removed
            proxy.guid = fn.guid = fn.guid || proxy.guid || jQuery.guid++;

            return proxy;
        },

        // Multifunctional method to get and set values of a collection
        // The value/s can optionally be executed if it's a function
        access: function( elems, fn, key, value, chainable, emptyGet, pass ) {
            var exec,
                bulk = key == null,
                i = 0,
                length = elems.length;

            // Sets many values
            if ( key && typeof key === "object" ) {
                for ( i in key ) {
                    jQuery.access( elems, fn, i, key[i], 1, emptyGet, value );
                }
                chainable = 1;

                // Sets one value
            } else if ( value !== undefined ) {
                // Optionally, function values get executed if exec is true
                exec = pass === undefined && jQuery.isFunction( value );

                if ( bulk ) {
                    // Bulk operations only iterate when executing function values
                    if ( exec ) {
                        exec = fn;
                        fn = function( elem, key, value ) {
                            return exec.call( jQuery( elem ), value );
                        };

                        // Otherwise they run against the entire set
                    } else {
                        fn.call( elems, value );
                        fn = null;
                    }
                }

                if ( fn ) {
                    for (; i < length; i++ ) {
                        fn( elems[i], key, exec ? value.call( elems[i], i, fn( elems[i], key ) ) : value, pass );
                    }
                }

                chainable = 1;
            }

            return chainable ?
                elems :

                // Gets
                bulk ?
                    fn.call( elems ) :
                    length ? fn( elems[0], key ) : emptyGet;
        },

        now: function() {
            return ( new Date() ).getTime();
        }
    });

    jQuery.ready.promise = function( obj ) {
        if ( !readyList ) {

            readyList = jQuery.Deferred();

            // Catch cases where $(document).ready() is called after the browser event has already occurred.
            // we once tried to use readyState "interactive" here, but it caused issues like the one
            // discovered by ChrisS here: http://bugs.jquery.com/ticket/12282#comment:15
            if ( document.readyState === "complete" ) {
                // Handle it asynchronously to allow scripts the opportunity to delay ready
                setTimeout( jQuery.ready, 1 );

                // Standards-based browsers support DOMContentLoaded
            } else if ( document.addEventListener ) {
                // Use the handy event callback
                document.addEventListener( "DOMContentLoaded", DOMContentLoaded, false );

                // A fallback to window.onload, that will always work
                window.addEventListener( "load", jQuery.ready, false );

                // If IE event model is used
            } else {
                // Ensure firing before onload, maybe late but safe also for iframes
                document.attachEvent( "onreadystatechange", DOMContentLoaded );

                // A fallback to window.onload, that will always work
                window.attachEvent( "onload", jQuery.ready );

                // If IE and not a frame
                // continually check to see if the document is ready
                var top = false;

                try {
                    top = window.frameElement == null && document.documentElement;
                } catch(e) {}

                if ( top && top.doScroll ) {
                    (function doScrollCheck() {
                        if ( !jQuery.isReady ) {

                            try {
                                // Use the trick by Diego Perini
                                // http://javascript.nwbox.com/IEContentLoaded/
                                top.doScroll("left");
                            } catch(e) {
                                return setTimeout( doScrollCheck, 50 );
                            }

                            // and execute any waiting functions
                            jQuery.ready();
                        }
                    })();
                }
            }
        }
        return readyList.promise( obj );
    };

// Populate the class2type map
    jQuery.each("Boolean Number String Function Array Date RegExp Object".split(" "), function(i, name) {
        class2type[ "[object " + name + "]" ] = name.toLowerCase();
    });

// All jQuery objects should point back to these
    rootjQuery = jQuery(document);
// String to Object options format cache
    var optionsCache = {};

// Convert String-formatted options into Object-formatted ones and store in cache
    function createOptions( options ) {
        var object = optionsCache[ options ] = {};
        jQuery.each( options.split( core_rspace ), function( _, flag ) {
            object[ flag ] = true;
        });
        return object;
    }

    /*
     * Create a callback list using the following parameters:
     *
     *	options: an optional list of space-separated options that will change how
     *			the callback list behaves or a more traditional option object
     *
     * By default a callback list will act like an event callback list and can be
     * "fired" multiple times.
     *
     * Possible options:
     *
     *	once:			will ensure the callback list can only be fired once (like a Deferred)
     *
     *	memory:			will keep track of previous values and will call any callback added
     *					after the list has been fired right away with the latest "memorized"
     *					values (like a Deferred)
     *
     *	unique:			will ensure a callback can only be added once (no duplicate in the list)
     *
     *	stopOnFalse:	interrupt callings when a callback returns false
     *
     */
    jQuery.Callbacks = function( options ) {

        // Convert options from String-formatted to Object-formatted if needed
        // (we check in cache first)
        options = typeof options === "string" ?
            ( optionsCache[ options ] || createOptions( options ) ) :
            jQuery.extend( {}, options );

        var // Last fire value (for non-forgettable lists)
            memory,
        // Flag to know if list was already fired
            fired,
        // Flag to know if list is currently firing
            firing,
        // First callback to fire (used internally by add and fireWith)
            firingStart,
        // End of the loop when firing
            firingLength,
        // Index of currently firing callback (modified by remove if needed)
            firingIndex,
        // Actual callback list
            list = [],
        // Stack of fire calls for repeatable lists
            stack = !options.once && [],
        // Fire callbacks
            fire = function( data ) {
                memory = options.memory && data;
                fired = true;
                firingIndex = firingStart || 0;
                firingStart = 0;
                firingLength = list.length;
                firing = true;
                for ( ; list && firingIndex < firingLength; firingIndex++ ) {
                    if ( list[ firingIndex ].apply( data[ 0 ], data[ 1 ] ) === false && options.stopOnFalse ) {
                        memory = false; // To prevent further calls using add
                        break;
                    }
                }
                firing = false;
                if ( list ) {
                    if ( stack ) {
                        if ( stack.length ) {
                            fire( stack.shift() );
                        }
                    } else if ( memory ) {
                        list = [];
                    } else {
                        self.disable();
                    }
                }
            },
        // Actual Callbacks object
            self = {
                // Add a callback or a collection of callbacks to the list
                add: function() {
                    if ( list ) {
                        // First, we save the current length
                        var start = list.length;
                        (function add( args ) {
                            jQuery.each( args, function( _, arg ) {
                                var type = jQuery.type( arg );
                                if ( type === "function" && ( !options.unique || !self.has( arg ) ) ) {
                                    list.push( arg );
                                } else if ( arg && arg.length && type !== "string" ) {
                                    // Inspect recursively
                                    add( arg );
                                }
                            });
                        })( arguments );
                        // Do we need to add the callbacks to the
                        // current firing batch?
                        if ( firing ) {
                            firingLength = list.length;
                            // With memory, if we're not firing then
                            // we should call right away
                        } else if ( memory ) {
                            firingStart = start;
                            fire( memory );
                        }
                    }
                    return this;
                },
                // Remove a callback from the list
                remove: function() {
                    if ( list ) {
                        jQuery.each( arguments, function( _, arg ) {
                            var index;
                            while( ( index = jQuery.inArray( arg, list, index ) ) > -1 ) {
                                list.splice( index, 1 );
                                // Handle firing indexes
                                if ( firing ) {
                                    if ( index <= firingLength ) {
                                        firingLength--;
                                    }
                                    if ( index <= firingIndex ) {
                                        firingIndex--;
                                    }
                                }
                            }
                        });
                    }
                    return this;
                },
                // Control if a given callback is in the list
                has: function( fn ) {
                    return jQuery.inArray( fn, list ) > -1;
                },
                // Remove all callbacks from the list
                empty: function() {
                    list = [];
                    return this;
                },
                // Have the list do nothing anymore
                disable: function() {
                    list = stack = memory = undefined;
                    return this;
                },
                // Is it disabled?
                disabled: function() {
                    return !list;
                },
                // Lock the list in its current state
                lock: function() {
                    stack = undefined;
                    if ( !memory ) {
                        self.disable();
                    }
                    return this;
                },
                // Is it locked?
                locked: function() {
                    return !stack;
                },
                // Call all callbacks with the given context and arguments
                fireWith: function( context, args ) {
                    args = args || [];
                    args = [ context, args.slice ? args.slice() : args ];
                    if ( list && ( !fired || stack ) ) {
                        if ( firing ) {
                            stack.push( args );
                        } else {
                            fire( args );
                        }
                    }
                    return this;
                },
                // Call all the callbacks with the given arguments
                fire: function() {
                    self.fireWith( this, arguments );
                    return this;
                },
                // To know if the callbacks have already been called at least once
                fired: function() {
                    return !!fired;
                }
            };

        return self;
    };
    jQuery.extend({

        Deferred: function( func ) {
            var tuples = [
                    // action, add listener, listener list, final state
                    [ "resolve", "done", jQuery.Callbacks("once memory"), "resolved" ],
                    [ "reject", "fail", jQuery.Callbacks("once memory"), "rejected" ],
                    [ "notify", "progress", jQuery.Callbacks("memory") ]
                ],
                state = "pending",
                promise = {
                    state: function() {
                        return state;
                    },
                    always: function() {
                        deferred.done( arguments ).fail( arguments );
                        return this;
                    },
                    then: function( /* fnDone, fnFail, fnProgress */ ) {
                        var fns = arguments;
                        return jQuery.Deferred(function( newDefer ) {
                            jQuery.each( tuples, function( i, tuple ) {
                                var action = tuple[ 0 ],
                                    fn = fns[ i ];
                                // deferred[ done | fail | progress ] for forwarding actions to newDefer
                                deferred[ tuple[1] ]( jQuery.isFunction( fn ) ?
                                    function() {
                                        var returned = fn.apply( this, arguments );
                                        if ( returned && jQuery.isFunction( returned.promise ) ) {
                                            returned.promise()
                                                .done( newDefer.resolve )
                                                .fail( newDefer.reject )
                                                .progress( newDefer.notify );
                                        } else {
                                            newDefer[ action + "With" ]( this === deferred ? newDefer : this, [ returned ] );
                                        }
                                    } :
                                    newDefer[ action ]
                                );
                            });
                            fns = null;
                        }).promise();
                    },
                    // Get a promise for this deferred
                    // If obj is provided, the promise aspect is added to the object
                    promise: function( obj ) {
                        return typeof obj === "object" ? jQuery.extend( obj, promise ) : promise;
                    }
                },
                deferred = {};

            // Keep pipe for back-compat
            promise.pipe = promise.then;

            // Add list-specific methods
            jQuery.each( tuples, function( i, tuple ) {
                var list = tuple[ 2 ],
                    stateString = tuple[ 3 ];

                // promise[ done | fail | progress ] = list.add
                promise[ tuple[1] ] = list.add;

                // Handle state
                if ( stateString ) {
                    list.add(function() {
                        // state = [ resolved | rejected ]
                        state = stateString;

                        // [ reject_list | resolve_list ].disable; progress_list.lock
                    }, tuples[ i ^ 1 ][ 2 ].disable, tuples[ 2 ][ 2 ].lock );
                }

                // deferred[ resolve | reject | notify ] = list.fire
                deferred[ tuple[0] ] = list.fire;
                deferred[ tuple[0] + "With" ] = list.fireWith;
            });

            // Make the deferred a promise
            promise.promise( deferred );

            // Call given func if any
            if ( func ) {
                func.call( deferred, deferred );
            }

            // All done!
            return deferred;
        },

        // Deferred helper
        when: function( subordinate /* , ..., subordinateN */ ) {
            var i = 0,
                resolveValues = core_slice.call( arguments ),
                length = resolveValues.length,

            // the count of uncompleted subordinates
                remaining = length !== 1 || ( subordinate && jQuery.isFunction( subordinate.promise ) ) ? length : 0,

            // the master Deferred. If resolveValues consist of only a single Deferred, just use that.
                deferred = remaining === 1 ? subordinate : jQuery.Deferred(),

            // Update function for both resolve and progress values
                updateFunc = function( i, contexts, values ) {
                    return function( value ) {
                        contexts[ i ] = this;
                        values[ i ] = arguments.length > 1 ? core_slice.call( arguments ) : value;
                        if( values === progressValues ) {
                            deferred.notifyWith( contexts, values );
                        } else if ( !( --remaining ) ) {
                            deferred.resolveWith( contexts, values );
                        }
                    };
                },

                progressValues, progressContexts, resolveContexts;

            // add listeners to Deferred subordinates; treat others as resolved
            if ( length > 1 ) {
                progressValues = new Array( length );
                progressContexts = new Array( length );
                resolveContexts = new Array( length );
                for ( ; i < length; i++ ) {
                    if ( resolveValues[ i ] && jQuery.isFunction( resolveValues[ i ].promise ) ) {
                        resolveValues[ i ].promise()
                            .done( updateFunc( i, resolveContexts, resolveValues ) )
                            .fail( deferred.reject )
                            .progress( updateFunc( i, progressContexts, progressValues ) );
                    } else {
                        --remaining;
                    }
                }
            }

            // if we're not waiting on anything, resolve the master
            if ( !remaining ) {
                deferred.resolveWith( resolveContexts, resolveValues );
            }

            return deferred.promise();
        }
    });
    jQuery.support = (function() {

        var support,
            all,
            a,
            select,
            opt,
            input,
            fragment,
            eventName,
            i,
            isSupported,
            clickFn,
            div = document.createElement("div");

        // Preliminary tests
        div.setAttribute( "className", "t" );
        div.innerHTML = "  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>";

        all = div.getElementsByTagName("*");
        a = div.getElementsByTagName("a")[ 0 ];
        a.style.cssText = "top:1px;float:left;opacity:.5";

        // Can't get basic test support
        if ( !all || !all.length || !a ) {
            return {};
        }

        // First batch of supports tests
        select = document.createElement("select");
        opt = select.appendChild( document.createElement("option") );
        input = div.getElementsByTagName("input")[ 0 ];

        support = {
            // IE strips leading whitespace when .innerHTML is used
            leadingWhitespace: ( div.firstChild.nodeType === 3 ),

            // Make sure that tbody elements aren't automatically inserted
            // IE will insert them into empty tables
            tbody: !div.getElementsByTagName("tbody").length,

            // Make sure that link elements get serialized correctly by innerHTML
            // This requires a wrapper element in IE
            htmlSerialize: !!div.getElementsByTagName("link").length,

            // Get the style information from getAttribute
            // (IE uses .cssText instead)
            style: /top/.test( a.getAttribute("style") ),

            // Make sure that URLs aren't manipulated
            // (IE normalizes it by default)
            hrefNormalized: ( a.getAttribute("href") === "/a" ),

            // Make sure that element opacity exists
            // (IE uses filter instead)
            // Use a regex to work around a WebKit issue. See #5145
            opacity: /^0.5/.test( a.style.opacity ),

            // Verify style float existence
            // (IE uses styleFloat instead of cssFloat)
            cssFloat: !!a.style.cssFloat,

            // Make sure that if no value is specified for a checkbox
            // that it defaults to "on".
            // (WebKit defaults to "" instead)
            checkOn: ( input.value === "on" ),

            // Make sure that a selected-by-default option has a working selected property.
            // (WebKit defaults to false instead of true, IE too, if it's in an optgroup)
            optSelected: opt.selected,

            // Test setAttribute on camelCase class. If it works, we need attrFixes when doing get/setAttribute (ie6/7)
            getSetAttribute: div.className !== "t",

            // Tests for enctype support on a form(#6743)
            enctype: !!document.createElement("form").enctype,

            // Makes sure cloning an html5 element does not cause problems
            // Where outerHTML is undefined, this still works
            html5Clone: document.createElement("nav").cloneNode( true ).outerHTML !== "<:nav></:nav>",

            // jQuery.support.boxModel DEPRECATED in 1.8 since we don't support Quirks Mode
            boxModel: ( document.compatMode === "CSS1Compat" ),

            // Will be defined later
            submitBubbles: true,
            changeBubbles: true,
            focusinBubbles: false,
            deleteExpando: true,
            noCloneEvent: true,
            inlineBlockNeedsLayout: false,
            shrinkWrapBlocks: false,
            reliableMarginRight: true,
            boxSizingReliable: true,
            pixelPosition: false
        };

        // Make sure checked status is properly cloned
        input.checked = true;
        support.noCloneChecked = input.cloneNode( true ).checked;

        // Make sure that the options inside disabled selects aren't marked as disabled
        // (WebKit marks them as disabled)
        select.disabled = true;
        support.optDisabled = !opt.disabled;

        // Test to see if it's possible to delete an expando from an element
        // Fails in Internet Explorer
        try {
            delete div.test;
        } catch( e ) {
            support.deleteExpando = false;
        }

        if ( !div.addEventListener && div.attachEvent && div.fireEvent ) {
            div.attachEvent( "onclick", clickFn = function() {
                // Cloning a node shouldn't copy over any
                // bound event handlers (IE does this)
                support.noCloneEvent = false;
            });
            div.cloneNode( true ).fireEvent("onclick");
            div.detachEvent( "onclick", clickFn );
        }

        // Check if a radio maintains its value
        // after being appended to the DOM
        input = document.createElement("input");
        input.value = "t";
        input.setAttribute( "type", "radio" );
        support.radioValue = input.value === "t";

        input.setAttribute( "checked", "checked" );

        // #11217 - WebKit loses check when the name is after the checked attribute
        input.setAttribute( "name", "t" );

        div.appendChild( input );
        fragment = document.createDocumentFragment();
        fragment.appendChild( div.lastChild );

        // WebKit doesn't clone checked state correctly in fragments
        support.checkClone = fragment.cloneNode( true ).cloneNode( true ).lastChild.checked;

        // Check if a disconnected checkbox will retain its checked
        // value of true after appended to the DOM (IE6/7)
        support.appendChecked = input.checked;

        fragment.removeChild( input );
        fragment.appendChild( div );

        // Technique from Juriy Zaytsev
        // http://perfectionkills.com/detecting-event-support-without-browser-sniffing/
        // We only care about the case where non-standard event systems
        // are used, namely in IE. Short-circuiting here helps us to
        // avoid an eval call (in setAttribute) which can cause CSP
        // to go haywire. See: https://developer.mozilla.org/en/Security/CSP
        if ( div.attachEvent ) {
            for ( i in {
                submit: true,
                change: true,
                focusin: true
            }) {
                eventName = "on" + i;
                isSupported = ( eventName in div );
                if ( !isSupported ) {
                    div.setAttribute( eventName, "return;" );
                    isSupported = ( typeof div[ eventName ] === "function" );
                }
                support[ i + "Bubbles" ] = isSupported;
            }
        }

        // Run tests that need a body at doc ready
        jQuery(function() {
            var container, div, tds, marginDiv,
                divReset = "padding:0;margin:0;border:0;display:block;overflow:hidden;",
                body = document.getElementsByTagName("body")[0];

            if ( !body ) {
                // Return for frameset docs that don't have a body
                return;
            }

            container = document.createElement("div");
            container.style.cssText = "visibility:hidden;border:0;width:0;height:0;position:static;top:0;margin-top:1px";
            body.insertBefore( container, body.firstChild );

            // Construct the test element
            div = document.createElement("div");
            container.appendChild( div );

            // Check if table cells still have offsetWidth/Height when they are set
            // to display:none and there are still other visible table cells in a
            // table row; if so, offsetWidth/Height are not reliable for use when
            // determining if an element has been hidden directly using
            // display:none (it is still safe to use offsets if a parent element is
            // hidden; don safety goggles and see bug #4512 for more information).
            // (only IE 8 fails this test)
            div.innerHTML = "<table><tr><td></td><td>t</td></tr></table>";
            tds = div.getElementsByTagName("td");
            tds[ 0 ].style.cssText = "padding:0;margin:0;border:0;display:none";
            isSupported = ( tds[ 0 ].offsetHeight === 0 );

            tds[ 0 ].style.display = "";
            tds[ 1 ].style.display = "none";

            // Check if empty table cells still have offsetWidth/Height
            // (IE <= 8 fail this test)
            support.reliableHiddenOffsets = isSupported && ( tds[ 0 ].offsetHeight === 0 );

            // Check box-sizing and margin behavior
            div.innerHTML = "";
            div.style.cssText = "box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;padding:1px;border:1px;display:block;width:4px;margin-top:1%;position:absolute;top:1%;";
            support.boxSizing = ( div.offsetWidth === 4 );
            support.doesNotIncludeMarginInBodyOffset = ( body.offsetTop !== 1 );

            // NOTE: To any future maintainer, we've window.getComputedStyle
            // because jsdom on node.js will break without it.
            if ( window.getComputedStyle ) {
                support.pixelPosition = ( window.getComputedStyle( div, null ) || {} ).top !== "1%";
                support.boxSizingReliable = ( window.getComputedStyle( div, null ) || { width: "4px" } ).width === "4px";

                // Check if div with explicit width and no margin-right incorrectly
                // gets computed margin-right based on width of container. For more
                // info see bug #3333
                // Fails in WebKit before Feb 2011 nightlies
                // WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
                marginDiv = document.createElement("div");
                marginDiv.style.cssText = div.style.cssText = divReset;
                marginDiv.style.marginRight = marginDiv.style.width = "0";
                div.style.width = "1px";
                div.appendChild( marginDiv );
                support.reliableMarginRight =
                    !parseFloat( ( window.getComputedStyle( marginDiv, null ) || {} ).marginRight );
            }

            if ( typeof div.style.zoom !== "undefined" ) {
                // Check if natively block-level elements act like inline-block
                // elements when setting their display to 'inline' and giving
                // them layout
                // (IE < 8 does this)
                div.innerHTML = "";
                div.style.cssText = divReset + "width:1px;padding:1px;display:inline;zoom:1";
                support.inlineBlockNeedsLayout = ( div.offsetWidth === 3 );

                // Check if elements with layout shrink-wrap their children
                // (IE 6 does this)
                div.style.display = "block";
                div.style.overflow = "visible";
                div.innerHTML = "<div></div>";
                div.firstChild.style.width = "5px";
                support.shrinkWrapBlocks = ( div.offsetWidth !== 3 );

                container.style.zoom = 1;
            }

            // Null elements to avoid leaks in IE
            body.removeChild( container );
            container = div = tds = marginDiv = null;
        });

        // Null elements to avoid leaks in IE
        fragment.removeChild( div );
        all = a = select = opt = input = fragment = div = null;

        return support;
    })();
    var rbrace = /(?:\{[\s\S]*\}|\[[\s\S]*\])$/,
        rmultiDash = /([A-Z])/g;

    jQuery.extend({
        cache: {},

        deletedIds: [],

        // Please use with caution
        uuid: 0,

        // Unique for each copy of jQuery on the page
        // Non-digits removed to match rinlinejQuery
        expando: "jQuery" + ( jQuery.fn.jquery + Math.random() ).replace( /\D/g, "" ),

        // The following elements throw uncatchable exceptions if you
        // attempt to add expando properties to them.
        noData: {
            "embed": true,
            // Ban all objects except for Flash (which handle expandos)
            "object": "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",
            "applet": true
        },

        hasData: function( elem ) {
            elem = elem.nodeType ? jQuery.cache[ elem[jQuery.expando] ] : elem[ jQuery.expando ];
            return !!elem && !isEmptyDataObject( elem );
        },

        data: function( elem, name, data, pvt /* Internal Use Only */ ) {
            if ( !jQuery.acceptData( elem ) ) {
                return;
            }

            var thisCache, ret,
                internalKey = jQuery.expando,
                getByName = typeof name === "string",

            // We have to handle DOM nodes and JS objects differently because IE6-7
            // can't GC object references properly across the DOM-JS boundary
                isNode = elem.nodeType,

            // Only DOM nodes need the global jQuery cache; JS object data is
            // attached directly to the object so GC can occur automatically
                cache = isNode ? jQuery.cache : elem,

            // Only defining an ID for JS objects if its cache already exists allows
            // the code to shortcut on the same path as a DOM node with no cache
                id = isNode ? elem[ internalKey ] : elem[ internalKey ] && internalKey;

            // Avoid doing any more work than we need to when trying to get data on an
            // object that has no data at all
            if ( (!id || !cache[id] || (!pvt && !cache[id].data)) && getByName && data === undefined ) {
                return;
            }

            if ( !id ) {
                // Only DOM nodes need a new unique ID for each element since their data
                // ends up in the global cache
                if ( isNode ) {
                    elem[ internalKey ] = id = jQuery.deletedIds.pop() || ++jQuery.uuid;
                } else {
                    id = internalKey;
                }
            }

            if ( !cache[ id ] ) {
                cache[ id ] = {};

                // Avoids exposing jQuery metadata on plain JS objects when the object
                // is serialized using JSON.stringify
                if ( !isNode ) {
                    cache[ id ].toJSON = jQuery.noop;
                }
            }

            // An object can be passed to jQuery.data instead of a key/value pair; this gets
            // shallow copied over onto the existing cache
            if ( typeof name === "object" || typeof name === "function" ) {
                if ( pvt ) {
                    cache[ id ] = jQuery.extend( cache[ id ], name );
                } else {
                    cache[ id ].data = jQuery.extend( cache[ id ].data, name );
                }
            }

            thisCache = cache[ id ];

            // jQuery data() is stored in a separate object inside the object's internal data
            // cache in order to avoid key collisions between internal data and user-defined
            // data.
            if ( !pvt ) {
                if ( !thisCache.data ) {
                    thisCache.data = {};
                }

                thisCache = thisCache.data;
            }

            if ( data !== undefined ) {
                thisCache[ jQuery.camelCase( name ) ] = data;
            }

            // Check for both converted-to-camel and non-converted data property names
            // If a data property was specified
            if ( getByName ) {

                // First Try to find as-is property data
                ret = thisCache[ name ];

                // Test for null|undefined property data
                if ( ret == null ) {

                    // Try to find the camelCased property
                    ret = thisCache[ jQuery.camelCase( name ) ];
                }
            } else {
                ret = thisCache;
            }

            return ret;
        },

        removeData: function( elem, name, pvt /* Internal Use Only */ ) {
            if ( !jQuery.acceptData( elem ) ) {
                return;
            }

            var thisCache, i, l,

                isNode = elem.nodeType,

            // See jQuery.data for more information
                cache = isNode ? jQuery.cache : elem,
                id = isNode ? elem[ jQuery.expando ] : jQuery.expando;

            // If there is already no cache entry for this object, there is no
            // purpose in continuing
            if ( !cache[ id ] ) {
                return;
            }

            if ( name ) {

                thisCache = pvt ? cache[ id ] : cache[ id ].data;

                if ( thisCache ) {

                    // Support array or space separated string names for data keys
                    if ( !jQuery.isArray( name ) ) {

                        // try the string as a key before any manipulation
                        if ( name in thisCache ) {
                            name = [ name ];
                        } else {

                            // split the camel cased version by spaces unless a key with the spaces exists
                            name = jQuery.camelCase( name );
                            if ( name in thisCache ) {
                                name = [ name ];
                            } else {
                                name = name.split(" ");
                            }
                        }
                    }

                    for ( i = 0, l = name.length; i < l; i++ ) {
                        delete thisCache[ name[i] ];
                    }

                    // If there is no data left in the cache, we want to continue
                    // and let the cache object itself get destroyed
                    if ( !( pvt ? isEmptyDataObject : jQuery.isEmptyObject )( thisCache ) ) {
                        return;
                    }
                }
            }

            // See jQuery.data for more information
            if ( !pvt ) {
                delete cache[ id ].data;

                // Don't destroy the parent cache unless the internal data object
                // had been the only thing left in it
                if ( !isEmptyDataObject( cache[ id ] ) ) {
                    return;
                }
            }

            // Destroy the cache
            if ( isNode ) {
                jQuery.cleanData( [ elem ], true );

                // Use delete when supported for expandos or `cache` is not a window per isWindow (#10080)
            } else if ( jQuery.support.deleteExpando || cache != cache.window ) {
                delete cache[ id ];

                // When all else fails, null
            } else {
                cache[ id ] = null;
            }
        },

        // For internal use only.
        _data: function( elem, name, data ) {
            return jQuery.data( elem, name, data, true );
        },

        // A method for determining if a DOM node can handle the data expando
        acceptData: function( elem ) {
            var noData = elem.nodeName && jQuery.noData[ elem.nodeName.toLowerCase() ];

            // nodes accept data unless otherwise specified; rejection can be conditional
            return !noData || noData !== true && elem.getAttribute("classid") === noData;
        }
    });

    jQuery.fn.extend({
        data: function( key, value ) {
            var parts, part, attr, name, l,
                elem = this[0],
                i = 0,
                data = null;

            // Gets all values
            if ( key === undefined ) {
                if ( this.length ) {
                    data = jQuery.data( elem );

                    if ( elem.nodeType === 1 && !jQuery._data( elem, "parsedAttrs" ) ) {
                        attr = elem.attributes;
                        for ( l = attr.length; i < l; i++ ) {
                            name = attr[i].name;

                            if ( name.indexOf( "data-" ) === 0 ) {
                                name = jQuery.camelCase( name.substring(5) );

                                dataAttr( elem, name, data[ name ] );
                            }
                        }
                        jQuery._data( elem, "parsedAttrs", true );
                    }
                }

                return data;
            }

            // Sets multiple values
            if ( typeof key === "object" ) {
                return this.each(function() {
                    jQuery.data( this, key );
                });
            }

            parts = key.split( ".", 2 );
            parts[1] = parts[1] ? "." + parts[1] : "";
            part = parts[1] + "!";

            return jQuery.access( this, function( value ) {

                if ( value === undefined ) {
                    data = this.triggerHandler( "getData" + part, [ parts[0] ] );

                    // Try to fetch any internally stored data first
                    if ( data === undefined && elem ) {
                        data = jQuery.data( elem, key );
                        data = dataAttr( elem, key, data );
                    }

                    return data === undefined && parts[1] ?
                        this.data( parts[0] ) :
                        data;
                }

                parts[1] = value;
                this.each(function() {
                    var self = jQuery( this );

                    self.triggerHandler( "setData" + part, parts );
                    jQuery.data( this, key, value );
                    self.triggerHandler( "changeData" + part, parts );
                });
            }, null, value, arguments.length > 1, null, false );
        },

        removeData: function( key ) {
            return this.each(function() {
                jQuery.removeData( this, key );
            });
        }
    });

    function dataAttr( elem, key, data ) {
        // If nothing was found internally, try to fetch any
        // data from the HTML5 data-* attribute
        if ( data === undefined && elem.nodeType === 1 ) {

            var name = "data-" + key.replace( rmultiDash, "-$1" ).toLowerCase();

            data = elem.getAttribute( name );

            if ( typeof data === "string" ) {
                try {
                    data = data === "true" ? true :
                        data === "false" ? false :
                            data === "null" ? null :
                                // Only convert to a number if it doesn't change the string
                                +data + "" === data ? +data :
                                    rbrace.test( data ) ? jQuery.parseJSON( data ) :
                                        data;
                } catch( e ) {}

                // Make sure we set the data so it isn't changed later
                jQuery.data( elem, key, data );

            } else {
                data = undefined;
            }
        }

        return data;
    }

// checks a cache object for emptiness
    function isEmptyDataObject( obj ) {
        var name;
        for ( name in obj ) {

            // if the public data object is empty, the private is still empty
            if ( name === "data" && jQuery.isEmptyObject( obj[name] ) ) {
                continue;
            }
            if ( name !== "toJSON" ) {
                return false;
            }
        }

        return true;
    }
    jQuery.extend({
        queue: function( elem, type, data ) {
            var queue;

            if ( elem ) {
                type = ( type || "fx" ) + "queue";
                queue = jQuery._data( elem, type );

                // Speed up dequeue by getting out quickly if this is just a lookup
                if ( data ) {
                    if ( !queue || jQuery.isArray(data) ) {
                        queue = jQuery._data( elem, type, jQuery.makeArray(data) );
                    } else {
                        queue.push( data );
                    }
                }
                return queue || [];
            }
        },

        dequeue: function( elem, type ) {
            type = type || "fx";

            var queue = jQuery.queue( elem, type ),
                startLength = queue.length,
                fn = queue.shift(),
                hooks = jQuery._queueHooks( elem, type ),
                next = function() {
                    jQuery.dequeue( elem, type );
                };

            // If the fx queue is dequeued, always remove the progress sentinel
            if ( fn === "inprogress" ) {
                fn = queue.shift();
                startLength--;
            }

            if ( fn ) {

                // Add a progress sentinel to prevent the fx queue from being
                // automatically dequeued
                if ( type === "fx" ) {
                    queue.unshift( "inprogress" );
                }

                // clear up the last queue stop function
                delete hooks.stop;
                fn.call( elem, next, hooks );
            }

            if ( !startLength && hooks ) {
                hooks.empty.fire();
            }
        },

        // not intended for public consumption - generates a queueHooks object, or returns the current one
        _queueHooks: function( elem, type ) {
            var key = type + "queueHooks";
            return jQuery._data( elem, key ) || jQuery._data( elem, key, {
                empty: jQuery.Callbacks("once memory").add(function() {
                    jQuery.removeData( elem, type + "queue", true );
                    jQuery.removeData( elem, key, true );
                })
            });
        }
    });

    jQuery.fn.extend({
        queue: function( type, data ) {
            var setter = 2;

            if ( typeof type !== "string" ) {
                data = type;
                type = "fx";
                setter--;
            }

            if ( arguments.length < setter ) {
                return jQuery.queue( this[0], type );
            }

            return data === undefined ?
                this :
                this.each(function() {
                    var queue = jQuery.queue( this, type, data );

                    // ensure a hooks for this queue
                    jQuery._queueHooks( this, type );

                    if ( type === "fx" && queue[0] !== "inprogress" ) {
                        jQuery.dequeue( this, type );
                    }
                });
        },
        dequeue: function( type ) {
            return this.each(function() {
                jQuery.dequeue( this, type );
            });
        },
        // Based off of the plugin by Clint Helfers, with permission.
        // http://blindsignals.com/index.php/2009/07/jquery-delay/
        delay: function( time, type ) {
            time = jQuery.fx ? jQuery.fx.speeds[ time ] || time : time;
            type = type || "fx";

            return this.queue( type, function( next, hooks ) {
                var timeout = setTimeout( next, time );
                hooks.stop = function() {
                    clearTimeout( timeout );
                };
            });
        },
        clearQueue: function( type ) {
            return this.queue( type || "fx", [] );
        },
        // Get a promise resolved when queues of a certain type
        // are emptied (fx is the type by default)
        promise: function( type, obj ) {
            var tmp,
                count = 1,
                defer = jQuery.Deferred(),
                elements = this,
                i = this.length,
                resolve = function() {
                    if ( !( --count ) ) {
                        defer.resolveWith( elements, [ elements ] );
                    }
                };

            if ( typeof type !== "string" ) {
                obj = type;
                type = undefined;
            }
            type = type || "fx";

            while( i-- ) {
                tmp = jQuery._data( elements[ i ], type + "queueHooks" );
                if ( tmp && tmp.empty ) {
                    count++;
                    tmp.empty.add( resolve );
                }
            }
            resolve();
            return defer.promise( obj );
        }
    });
    var nodeHook, boolHook, fixSpecified,
        rclass = /[\t\r\n]/g,
        rreturn = /\r/g,
        rtype = /^(?:button|input)$/i,
        rfocusable = /^(?:button|input|object|select|textarea)$/i,
        rclickable = /^a(?:rea|)$/i,
        rboolean = /^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i,
        getSetAttribute = jQuery.support.getSetAttribute;

    jQuery.fn.extend({
        attr: function( name, value ) {
            return jQuery.access( this, jQuery.attr, name, value, arguments.length > 1 );
        },

        removeAttr: function( name ) {
            return this.each(function() {
                jQuery.removeAttr( this, name );
            });
        },

        prop: function( name, value ) {
            return jQuery.access( this, jQuery.prop, name, value, arguments.length > 1 );
        },

        removeProp: function( name ) {
            name = jQuery.propFix[ name ] || name;
            return this.each(function() {
                // try/catch handles cases where IE balks (such as removing a property on window)
                try {
                    this[ name ] = undefined;
                    delete this[ name ];
                } catch( e ) {}
            });
        },

        addClass: function( value ) {
            var classNames, i, l, elem,
                setClass, c, cl;

            if ( jQuery.isFunction( value ) ) {
                return this.each(function( j ) {
                    jQuery( this ).addClass( value.call(this, j, this.className) );
                });
            }

            if ( value && typeof value === "string" ) {
                classNames = value.split( core_rspace );

                for ( i = 0, l = this.length; i < l; i++ ) {
                    elem = this[ i ];

                    if ( elem.nodeType === 1 ) {
                        if ( !elem.className && classNames.length === 1 ) {
                            elem.className = value;

                        } else {
                            setClass = " " + elem.className + " ";

                            for ( c = 0, cl = classNames.length; c < cl; c++ ) {
                                if ( !~setClass.indexOf( " " + classNames[ c ] + " " ) ) {
                                    setClass += classNames[ c ] + " ";
                                }
                            }
                            elem.className = jQuery.trim( setClass );
                        }
                    }
                }
            }

            return this;
        },

        removeClass: function( value ) {
            var removes, className, elem, c, cl, i, l;

            if ( jQuery.isFunction( value ) ) {
                return this.each(function( j ) {
                    jQuery( this ).removeClass( value.call(this, j, this.className) );
                });
            }
            if ( (value && typeof value === "string") || value === undefined ) {
                removes = ( value || "" ).split( core_rspace );

                for ( i = 0, l = this.length; i < l; i++ ) {
                    elem = this[ i ];
                    if ( elem.nodeType === 1 && elem.className ) {

                        className = (" " + elem.className + " ").replace( rclass, " " );

                        // loop over each item in the removal list
                        for ( c = 0, cl = removes.length; c < cl; c++ ) {
                            // Remove until there is nothing to remove,
                            while ( className.indexOf(" " + removes[ c ] + " ") > -1 ) {
                                className = className.replace( " " + removes[ c ] + " " , " " );
                            }
                        }
                        elem.className = value ? jQuery.trim( className ) : "";
                    }
                }
            }

            return this;
        },

        toggleClass: function( value, stateVal ) {
            var type = typeof value,
                isBool = typeof stateVal === "boolean";

            if ( jQuery.isFunction( value ) ) {
                return this.each(function( i ) {
                    jQuery( this ).toggleClass( value.call(this, i, this.className, stateVal), stateVal );
                });
            }

            return this.each(function() {
                if ( type === "string" ) {
                    // toggle individual class names
                    var className,
                        i = 0,
                        self = jQuery( this ),
                        state = stateVal,
                        classNames = value.split( core_rspace );

                    while ( (className = classNames[ i++ ]) ) {
                        // check each className given, space separated list
                        state = isBool ? state : !self.hasClass( className );
                        self[ state ? "addClass" : "removeClass" ]( className );
                    }

                } else if ( type === "undefined" || type === "boolean" ) {
                    if ( this.className ) {
                        // store className if set
                        jQuery._data( this, "__className__", this.className );
                    }

                    // toggle whole className
                    this.className = this.className || value === false ? "" : jQuery._data( this, "__className__" ) || "";
                }
            });
        },

        hasClass: function( selector ) {
            var className = " " + selector + " ",
                i = 0,
                l = this.length;
            for ( ; i < l; i++ ) {
                if ( this[i].nodeType === 1 && (" " + this[i].className + " ").replace(rclass, " ").indexOf( className ) > -1 ) {
                    return true;
                }
            }

            return false;
        },

        val: function( value ) {
            var hooks, ret, isFunction,
                elem = this[0];

            if ( !arguments.length ) {
                if ( elem ) {
                    hooks = jQuery.valHooks[ elem.type ] || jQuery.valHooks[ elem.nodeName.toLowerCase() ];

                    if ( hooks && "get" in hooks && (ret = hooks.get( elem, "value" )) !== undefined ) {
                        return ret;
                    }

                    ret = elem.value;

                    return typeof ret === "string" ?
                        // handle most common string cases
                        ret.replace(rreturn, "") :
                        // handle cases where value is null/undef or number
                        ret == null ? "" : ret;
                }

                return;
            }

            isFunction = jQuery.isFunction( value );

            return this.each(function( i ) {
                var val,
                    self = jQuery(this);

                if ( this.nodeType !== 1 ) {
                    return;
                }

                if ( isFunction ) {
                    val = value.call( this, i, self.val() );
                } else {
                    val = value;
                }

                // Treat null/undefined as ""; convert numbers to string
                if ( val == null ) {
                    val = "";
                } else if ( typeof val === "number" ) {
                    val += "";
                } else if ( jQuery.isArray( val ) ) {
                    val = jQuery.map(val, function ( value ) {
                        return value == null ? "" : value + "";
                    });
                }

                hooks = jQuery.valHooks[ this.type ] || jQuery.valHooks[ this.nodeName.toLowerCase() ];

                // If set returns undefined, fall back to normal setting
                if ( !hooks || !("set" in hooks) || hooks.set( this, val, "value" ) === undefined ) {
                    this.value = val;
                }
            });
        }
    });

    jQuery.extend({
        valHooks: {
            option: {
                get: function( elem ) {
                    // attributes.value is undefined in Blackberry 4.7 but
                    // uses .value. See #6932
                    var val = elem.attributes.value;
                    return !val || val.specified ? elem.value : elem.text;
                }
            },
            select: {
                get: function( elem ) {
                    var value, i, max, option,
                        index = elem.selectedIndex,
                        values = [],
                        options = elem.options,
                        one = elem.type === "select-one";

                    // Nothing was selected
                    if ( index < 0 ) {
                        return null;
                    }

                    // Loop through all the selected options
                    i = one ? index : 0;
                    max = one ? index + 1 : options.length;
                    for ( ; i < max; i++ ) {
                        option = options[ i ];

                        // Don't return options that are disabled or in a disabled optgroup
                        if ( option.selected && (jQuery.support.optDisabled ? !option.disabled : option.getAttribute("disabled") === null) &&
                            (!option.parentNode.disabled || !jQuery.nodeName( option.parentNode, "optgroup" )) ) {

                            // Get the specific value for the option
                            value = jQuery( option ).val();

                            // We don't need an array for one selects
                            if ( one ) {
                                return value;
                            }

                            // Multi-Selects return an array
                            values.push( value );
                        }
                    }

                    // Fixes Bug #2551 -- select.val() broken in IE after form.reset()
                    if ( one && !values.length && options.length ) {
                        return jQuery( options[ index ] ).val();
                    }

                    return values;
                },

                set: function( elem, value ) {
                    var values = jQuery.makeArray( value );

                    jQuery(elem).find("option").each(function() {
                        this.selected = jQuery.inArray( jQuery(this).val(), values ) >= 0;
                    });

                    if ( !values.length ) {
                        elem.selectedIndex = -1;
                    }
                    return values;
                }
            }
        },

        // Unused in 1.8, left in so attrFn-stabbers won't die; remove in 1.9
        attrFn: {},

        attr: function( elem, name, value, pass ) {
            var ret, hooks, notxml,
                nType = elem.nodeType;

            // don't get/set attributes on text, comment and attribute nodes
            if ( !elem || nType === 3 || nType === 8 || nType === 2 ) {
                return;
            }

            if ( pass && jQuery.isFunction( jQuery.fn[ name ] ) ) {
                return jQuery( elem )[ name ]( value );
            }

            // Fallback to prop when attributes are not supported
            if ( typeof elem.getAttribute === "undefined" ) {
                return jQuery.prop( elem, name, value );
            }

            notxml = nType !== 1 || !jQuery.isXMLDoc( elem );

            // All attributes are lowercase
            // Grab necessary hook if one is defined
            if ( notxml ) {
                name = name.toLowerCase();
                hooks = jQuery.attrHooks[ name ] || ( rboolean.test( name ) ? boolHook : nodeHook );
            }

            if ( value !== undefined ) {

                if ( value === null ) {
                    jQuery.removeAttr( elem, name );
                    return;

                } else if ( hooks && "set" in hooks && notxml && (ret = hooks.set( elem, value, name )) !== undefined ) {
                    return ret;

                } else {
                    elem.setAttribute( name, "" + value );
                    return value;
                }

            } else if ( hooks && "get" in hooks && notxml && (ret = hooks.get( elem, name )) !== null ) {
                return ret;

            } else {

                ret = elem.getAttribute( name );

                // Non-existent attributes return null, we normalize to undefined
                return ret === null ?
                    undefined :
                    ret;
            }
        },

        removeAttr: function( elem, value ) {
            var propName, attrNames, name, isBool,
                i = 0;

            if ( value && elem.nodeType === 1 ) {

                attrNames = value.split( core_rspace );

                for ( ; i < attrNames.length; i++ ) {
                    name = attrNames[ i ];

                    if ( name ) {
                        propName = jQuery.propFix[ name ] || name;
                        isBool = rboolean.test( name );

                        // See #9699 for explanation of this approach (setting first, then removal)
                        // Do not do this for boolean attributes (see #10870)
                        if ( !isBool ) {
                            jQuery.attr( elem, name, "" );
                        }
                        elem.removeAttribute( getSetAttribute ? name : propName );

                        // Set corresponding property to false for boolean attributes
                        if ( isBool && propName in elem ) {
                            elem[ propName ] = false;
                        }
                    }
                }
            }
        },

        attrHooks: {
            type: {
                set: function( elem, value ) {
                    // We can't allow the type property to be changed (since it causes problems in IE)
                    if ( rtype.test( elem.nodeName ) && elem.parentNode ) {
                        jQuery.error( "type property can't be changed" );
                    } else if ( !jQuery.support.radioValue && value === "radio" && jQuery.nodeName(elem, "input") ) {
                        // Setting the type on a radio button after the value resets the value in IE6-9
                        // Reset value to it's default in case type is set after value
                        // This is for element creation
                        var val = elem.value;
                        elem.setAttribute( "type", value );
                        if ( val ) {
                            elem.value = val;
                        }
                        return value;
                    }
                }
            },
            // Use the value property for back compat
            // Use the nodeHook for button elements in IE6/7 (#1954)
            value: {
                get: function( elem, name ) {
                    if ( nodeHook && jQuery.nodeName( elem, "button" ) ) {
                        return nodeHook.get( elem, name );
                    }
                    return name in elem ?
                        elem.value :
                        null;
                },
                set: function( elem, value, name ) {
                    if ( nodeHook && jQuery.nodeName( elem, "button" ) ) {
                        return nodeHook.set( elem, value, name );
                    }
                    // Does not return so that setAttribute is also used
                    elem.value = value;
                }
            }
        },

        propFix: {
            tabindex: "tabIndex",
            readonly: "readOnly",
            "for": "htmlFor",
            "class": "className",
            maxlength: "maxLength",
            cellspacing: "cellSpacing",
            cellpadding: "cellPadding",
            rowspan: "rowSpan",
            colspan: "colSpan",
            usemap: "useMap",
            frameborder: "frameBorder",
            contenteditable: "contentEditable"
        },

        prop: function( elem, name, value ) {
            var ret, hooks, notxml,
                nType = elem.nodeType;

            // don't get/set properties on text, comment and attribute nodes
            if ( !elem || nType === 3 || nType === 8 || nType === 2 ) {
                return;
            }

            notxml = nType !== 1 || !jQuery.isXMLDoc( elem );

            if ( notxml ) {
                // Fix name and attach hooks
                name = jQuery.propFix[ name ] || name;
                hooks = jQuery.propHooks[ name ];
            }

            if ( value !== undefined ) {
                if ( hooks && "set" in hooks && (ret = hooks.set( elem, value, name )) !== undefined ) {
                    return ret;

                } else {
                    return ( elem[ name ] = value );
                }

            } else {
                if ( hooks && "get" in hooks && (ret = hooks.get( elem, name )) !== null ) {
                    return ret;

                } else {
                    return elem[ name ];
                }
            }
        },

        propHooks: {
            tabIndex: {
                get: function( elem ) {
                    // elem.tabIndex doesn't always return the correct value when it hasn't been explicitly set
                    // http://fluidproject.org/blog/2008/01/09/getting-setting-and-removing-tabindex-values-with-javascript/
                    var attributeNode = elem.getAttributeNode("tabindex");

                    return attributeNode && attributeNode.specified ?
                        parseInt( attributeNode.value, 10 ) :
                        rfocusable.test( elem.nodeName ) || rclickable.test( elem.nodeName ) && elem.href ?
                            0 :
                            undefined;
                }
            }
        }
    });

// Hook for boolean attributes
    boolHook = {
        get: function( elem, name ) {
            // Align boolean attributes with corresponding properties
            // Fall back to attribute presence where some booleans are not supported
            var attrNode,
                property = jQuery.prop( elem, name );
            return property === true || typeof property !== "boolean" && ( attrNode = elem.getAttributeNode(name) ) && attrNode.nodeValue !== false ?
                name.toLowerCase() :
                undefined;
        },
        set: function( elem, value, name ) {
            var propName;
            if ( value === false ) {
                // Remove boolean attributes when set to false
                jQuery.removeAttr( elem, name );
            } else {
                // value is true since we know at this point it's type boolean and not false
                // Set boolean attributes to the same name and set the DOM property
                propName = jQuery.propFix[ name ] || name;
                if ( propName in elem ) {
                    // Only set the IDL specifically if it already exists on the element
                    elem[ propName ] = true;
                }

                elem.setAttribute( name, name.toLowerCase() );
            }
            return name;
        }
    };

// IE6/7 do not support getting/setting some attributes with get/setAttribute
    if ( !getSetAttribute ) {

        fixSpecified = {
            name: true,
            id: true,
            coords: true
        };

        // Use this for any attribute in IE6/7
        // This fixes almost every IE6/7 issue
        nodeHook = jQuery.valHooks.button = {
            get: function( elem, name ) {
                var ret;
                ret = elem.getAttributeNode( name );
                return ret && ( fixSpecified[ name ] ? ret.value !== "" : ret.specified ) ?
                    ret.value :
                    undefined;
            },
            set: function( elem, value, name ) {
                // Set the existing or create a new attribute node
                var ret = elem.getAttributeNode( name );
                if ( !ret ) {
                    ret = document.createAttribute( name );
                    elem.setAttributeNode( ret );
                }
                return ( ret.value = value + "" );
            }
        };

        // Set width and height to auto instead of 0 on empty string( Bug #8150 )
        // This is for removals
        jQuery.each([ "width", "height" ], function( i, name ) {
            jQuery.attrHooks[ name ] = jQuery.extend( jQuery.attrHooks[ name ], {
                set: function( elem, value ) {
                    if ( value === "" ) {
                        elem.setAttribute( name, "auto" );
                        return value;
                    }
                }
            });
        });

        // Set contenteditable to false on removals(#10429)
        // Setting to empty string throws an error as an invalid value
        jQuery.attrHooks.contenteditable = {
            get: nodeHook.get,
            set: function( elem, value, name ) {
                if ( value === "" ) {
                    value = "false";
                }
                nodeHook.set( elem, value, name );
            }
        };
    }


// Some attributes require a special call on IE
    if ( !jQuery.support.hrefNormalized ) {
        jQuery.each([ "href", "src", "width", "height" ], function( i, name ) {
            jQuery.attrHooks[ name ] = jQuery.extend( jQuery.attrHooks[ name ], {
                get: function( elem ) {
                    var ret = elem.getAttribute( name, 2 );
                    return ret === null ? undefined : ret;
                }
            });
        });
    }

    if ( !jQuery.support.style ) {
        jQuery.attrHooks.style = {
            get: function( elem ) {
                // Return undefined in the case of empty string
                // Normalize to lowercase since IE uppercases css property names
                return elem.style.cssText.toLowerCase() || undefined;
            },
            set: function( elem, value ) {
                return ( elem.style.cssText = "" + value );
            }
        };
    }

// Safari mis-reports the default selected property of an option
// Accessing the parent's selectedIndex property fixes it
    if ( !jQuery.support.optSelected ) {
        jQuery.propHooks.selected = jQuery.extend( jQuery.propHooks.selected, {
            get: function( elem ) {
                var parent = elem.parentNode;

                if ( parent ) {
                    parent.selectedIndex;

                    // Make sure that it also works with optgroups, see #5701
                    if ( parent.parentNode ) {
                        parent.parentNode.selectedIndex;
                    }
                }
                return null;
            }
        });
    }

// IE6/7 call enctype encoding
    if ( !jQuery.support.enctype ) {
        jQuery.propFix.enctype = "encoding";
    }

// Radios and checkboxes getter/setter
    if ( !jQuery.support.checkOn ) {
        jQuery.each([ "radio", "checkbox" ], function() {
            jQuery.valHooks[ this ] = {
                get: function( elem ) {
                    // Handle the case where in Webkit "" is returned instead of "on" if a value isn't specified
                    return elem.getAttribute("value") === null ? "on" : elem.value;
                }
            };
        });
    }
    jQuery.each([ "radio", "checkbox" ], function() {
        jQuery.valHooks[ this ] = jQuery.extend( jQuery.valHooks[ this ], {
            set: function( elem, value ) {
                if ( jQuery.isArray( value ) ) {
                    return ( elem.checked = jQuery.inArray( jQuery(elem).val(), value ) >= 0 );
                }
            }
        });
    });
    var rformElems = /^(?:textarea|input|select)$/i,
        rtypenamespace = /^([^\.]*|)(?:\.(.+)|)$/,
        rhoverHack = /(?:^|\s)hover(\.\S+|)\b/,
        rkeyEvent = /^key/,
        rmouseEvent = /^(?:mouse|contextmenu)|click/,
        rfocusMorph = /^(?:focusinfocus|focusoutblur)$/,
        hoverHack = function( events ) {
            return jQuery.event.special.hover ? events : events.replace( rhoverHack, "mouseenter$1 mouseleave$1" );
        };

    /*
     * Helper functions for managing events -- not part of the public interface.
     * Props to Dean Edwards' addEvent library for many of the ideas.
     */
    jQuery.event = {

        add: function( elem, types, handler, data, selector ) {

            var elemData, eventHandle, events,
                t, tns, type, namespaces, handleObj,
                handleObjIn, handlers, special;

            // Don't attach events to noData or text/comment nodes (allow plain objects tho)
            if ( elem.nodeType === 3 || elem.nodeType === 8 || !types || !handler || !(elemData = jQuery._data( elem )) ) {
                return;
            }

            // Caller can pass in an object of custom data in lieu of the handler
            if ( handler.handler ) {
                handleObjIn = handler;
                handler = handleObjIn.handler;
                selector = handleObjIn.selector;
            }

            // Make sure that the handler has a unique ID, used to find/remove it later
            if ( !handler.guid ) {
                handler.guid = jQuery.guid++;
            }

            // Init the element's event structure and main handler, if this is the first
            events = elemData.events;
            if ( !events ) {
                elemData.events = events = {};
            }
            eventHandle = elemData.handle;
            if ( !eventHandle ) {
                elemData.handle = eventHandle = function( e ) {
                    // Discard the second event of a jQuery.event.trigger() and
                    // when an event is called after a page has unloaded
                    return typeof jQuery !== "undefined" && (!e || jQuery.event.triggered !== e.type) ?
                        jQuery.event.dispatch.apply( eventHandle.elem, arguments ) :
                        undefined;
                };
                // Add elem as a property of the handle fn to prevent a memory leak with IE non-native events
                eventHandle.elem = elem;
            }

            // Handle multiple events separated by a space
            // jQuery(...).bind("mouseover mouseout", fn);
            types = jQuery.trim( hoverHack(types) ).split( " " );
            for ( t = 0; t < types.length; t++ ) {

                tns = rtypenamespace.exec( types[t] ) || [];
                type = tns[1];
                namespaces = ( tns[2] || "" ).split( "." ).sort();

                // If event changes its type, use the special event handlers for the changed type
                special = jQuery.event.special[ type ] || {};

                // If selector defined, determine special event api type, otherwise given type
                type = ( selector ? special.delegateType : special.bindType ) || type;

                // Update special based on newly reset type
                special = jQuery.event.special[ type ] || {};

                // handleObj is passed to all event handlers
                handleObj = jQuery.extend({
                    type: type,
                    origType: tns[1],
                    data: data,
                    handler: handler,
                    guid: handler.guid,
                    selector: selector,
                    namespace: namespaces.join(".")
                }, handleObjIn );

                // Init the event handler queue if we're the first
                handlers = events[ type ];
                if ( !handlers ) {
                    handlers = events[ type ] = [];
                    handlers.delegateCount = 0;

                    // Only use addEventListener/attachEvent if the special events handler returns false
                    if ( !special.setup || special.setup.call( elem, data, namespaces, eventHandle ) === false ) {
                        // Bind the global event handler to the element
                        if ( elem.addEventListener ) {
                            elem.addEventListener( type, eventHandle, false );

                        } else if ( elem.attachEvent ) {
                            elem.attachEvent( "on" + type, eventHandle );
                        }
                    }
                }

                if ( special.add ) {
                    special.add.call( elem, handleObj );

                    if ( !handleObj.handler.guid ) {
                        handleObj.handler.guid = handler.guid;
                    }
                }

                // Add to the element's handler list, delegates in front
                if ( selector ) {
                    handlers.splice( handlers.delegateCount++, 0, handleObj );
                } else {
                    handlers.push( handleObj );
                }

                // Keep track of which events have ever been used, for event optimization
                jQuery.event.global[ type ] = true;
            }

            // Nullify elem to prevent memory leaks in IE
            elem = null;
        },

        global: {},

        // Detach an event or set of events from an element
        remove: function( elem, types, handler, selector, mappedTypes ) {

            var t, tns, type, origType, namespaces, origCount,
                j, events, special, eventType, handleObj,
                elemData = jQuery.hasData( elem ) && jQuery._data( elem );

            if ( !elemData || !(events = elemData.events) ) {
                return;
            }

            // Once for each type.namespace in types; type may be omitted
            types = jQuery.trim( hoverHack( types || "" ) ).split(" ");
            for ( t = 0; t < types.length; t++ ) {
                tns = rtypenamespace.exec( types[t] ) || [];
                type = origType = tns[1];
                namespaces = tns[2];

                // Unbind all events (on this namespace, if provided) for the element
                if ( !type ) {
                    for ( type in events ) {
                        jQuery.event.remove( elem, type + types[ t ], handler, selector, true );
                    }
                    continue;
                }

                special = jQuery.event.special[ type ] || {};
                type = ( selector? special.delegateType : special.bindType ) || type;
                eventType = events[ type ] || [];
                origCount = eventType.length;
                namespaces = namespaces ? new RegExp("(^|\\.)" + namespaces.split(".").sort().join("\\.(?:.*\\.|)") + "(\\.|$)") : null;

                // Remove matching events
                for ( j = 0; j < eventType.length; j++ ) {
                    handleObj = eventType[ j ];

                    if ( ( mappedTypes || origType === handleObj.origType ) &&
                        ( !handler || handler.guid === handleObj.guid ) &&
                        ( !namespaces || namespaces.test( handleObj.namespace ) ) &&
                        ( !selector || selector === handleObj.selector || selector === "**" && handleObj.selector ) ) {
                        eventType.splice( j--, 1 );

                        if ( handleObj.selector ) {
                            eventType.delegateCount--;
                        }
                        if ( special.remove ) {
                            special.remove.call( elem, handleObj );
                        }
                    }
                }

                // Remove generic event handler if we removed something and no more handlers exist
                // (avoids potential for endless recursion during removal of special event handlers)
                if ( eventType.length === 0 && origCount !== eventType.length ) {
                    if ( !special.teardown || special.teardown.call( elem, namespaces, elemData.handle ) === false ) {
                        jQuery.removeEvent( elem, type, elemData.handle );
                    }

                    delete events[ type ];
                }
            }

            // Remove the expando if it's no longer used
            if ( jQuery.isEmptyObject( events ) ) {
                delete elemData.handle;

                // removeData also checks for emptiness and clears the expando if empty
                // so use it instead of delete
                jQuery.removeData( elem, "events", true );
            }
        },

        // Events that are safe to short-circuit if no handlers are attached.
        // Native DOM events should not be added, they may have inline handlers.
        customEvent: {
            "getData": true,
            "setData": true,
            "changeData": true
        },

        trigger: function( event, data, elem, onlyHandlers ) {
            // Don't do events on text and comment nodes
            if ( elem && (elem.nodeType === 3 || elem.nodeType === 8) ) {
                return;
            }

            // Event object or event type
            var cache, exclusive, i, cur, old, ontype, special, handle, eventPath, bubbleType,
                type = event.type || event,
                namespaces = [];

            // focus/blur morphs to focusin/out; ensure we're not firing them right now
            if ( rfocusMorph.test( type + jQuery.event.triggered ) ) {
                return;
            }

            if ( type.indexOf( "!" ) >= 0 ) {
                // Exclusive events trigger only for the exact event (no namespaces)
                type = type.slice(0, -1);
                exclusive = true;
            }

            if ( type.indexOf( "." ) >= 0 ) {
                // Namespaced trigger; create a regexp to match event type in handle()
                namespaces = type.split(".");
                type = namespaces.shift();
                namespaces.sort();
            }

            if ( (!elem || jQuery.event.customEvent[ type ]) && !jQuery.event.global[ type ] ) {
                // No jQuery handlers for this event type, and it can't have inline handlers
                return;
            }

            // Caller can pass in an Event, Object, or just an event type string
            event = typeof event === "object" ?
                // jQuery.Event object
                event[ jQuery.expando ] ? event :
                    // Object literal
                    new jQuery.Event( type, event ) :
                // Just the event type (string)
                new jQuery.Event( type );

            event.type = type;
            event.isTrigger = true;
            event.exclusive = exclusive;
            event.namespace = namespaces.join( "." );
            event.namespace_re = event.namespace? new RegExp("(^|\\.)" + namespaces.join("\\.(?:.*\\.|)") + "(\\.|$)") : null;
            ontype = type.indexOf( ":" ) < 0 ? "on" + type : "";

            // Handle a global trigger
            if ( !elem ) {

                // TODO: Stop taunting the data cache; remove global events and always attach to document
                cache = jQuery.cache;
                for ( i in cache ) {
                    if ( cache[ i ].events && cache[ i ].events[ type ] ) {
                        jQuery.event.trigger( event, data, cache[ i ].handle.elem, true );
                    }
                }
                return;
            }

            // Clean up the event in case it is being reused
            event.result = undefined;
            if ( !event.target ) {
                event.target = elem;
            }

            // Clone any incoming data and prepend the event, creating the handler arg list
            data = data != null ? jQuery.makeArray( data ) : [];
            data.unshift( event );

            // Allow special events to draw outside the lines
            special = jQuery.event.special[ type ] || {};
            if ( special.trigger && special.trigger.apply( elem, data ) === false ) {
                return;
            }

            // Determine event propagation path in advance, per W3C events spec (#9951)
            // Bubble up to document, then to window; watch for a global ownerDocument var (#9724)
            eventPath = [[ elem, special.bindType || type ]];
            if ( !onlyHandlers && !special.noBubble && !jQuery.isWindow( elem ) ) {

                bubbleType = special.delegateType || type;
                cur = rfocusMorph.test( bubbleType + type ) ? elem : elem.parentNode;
                for ( old = elem; cur; cur = cur.parentNode ) {
                    eventPath.push([ cur, bubbleType ]);
                    old = cur;
                }

                // Only add window if we got to document (e.g., not plain obj or detached DOM)
                if ( old === (elem.ownerDocument || document) ) {
                    eventPath.push([ old.defaultView || old.parentWindow || window, bubbleType ]);
                }
            }

            // Fire handlers on the event path
            for ( i = 0; i < eventPath.length && !event.isPropagationStopped(); i++ ) {

                cur = eventPath[i][0];
                event.type = eventPath[i][1];

                handle = ( jQuery._data( cur, "events" ) || {} )[ event.type ] && jQuery._data( cur, "handle" );
                if ( handle ) {
                    handle.apply( cur, data );
                }
                // Note that this is a bare JS function and not a jQuery handler
                handle = ontype && cur[ ontype ];
                if ( handle && jQuery.acceptData( cur ) && handle.apply( cur, data ) === false ) {
                    event.preventDefault();
                }
            }
            event.type = type;

            // If nobody prevented the default action, do it now
            if ( !onlyHandlers && !event.isDefaultPrevented() ) {

                if ( (!special._default || special._default.apply( elem.ownerDocument, data ) === false) &&
                    !(type === "click" && jQuery.nodeName( elem, "a" )) && jQuery.acceptData( elem ) ) {

                    // Call a native DOM method on the target with the same name name as the event.
                    // Can't use an .isFunction() check here because IE6/7 fails that test.
                    // Don't do default actions on window, that's where global variables be (#6170)
                    // IE<9 dies on focus/blur to hidden element (#1486)
                    if ( ontype && elem[ type ] && ((type !== "focus" && type !== "blur") || event.target.offsetWidth !== 0) && !jQuery.isWindow( elem ) ) {

                        // Don't re-trigger an onFOO event when we call its FOO() method
                        old = elem[ ontype ];

                        if ( old ) {
                            elem[ ontype ] = null;
                        }

                        // Prevent re-triggering of the same event, since we already bubbled it above
                        jQuery.event.triggered = type;
                        elem[ type ]();
                        jQuery.event.triggered = undefined;

                        if ( old ) {
                            elem[ ontype ] = old;
                        }
                    }
                }
            }

            return event.result;
        },

        dispatch: function( event ) {

            // Make a writable jQuery.Event from the native event object
            event = jQuery.event.fix( event || window.event );

            var i, j, cur, ret, selMatch, matched, matches, handleObj, sel, related,
                handlers = ( (jQuery._data( this, "events" ) || {} )[ event.type ] || []),
                delegateCount = handlers.delegateCount,
                args = [].slice.call( arguments ),
                run_all = !event.exclusive && !event.namespace,
                special = jQuery.event.special[ event.type ] || {},
                handlerQueue = [];

            // Use the fix-ed jQuery.Event rather than the (read-only) native event
            args[0] = event;
            event.delegateTarget = this;

            // Call the preDispatch hook for the mapped type, and let it bail if desired
            if ( special.preDispatch && special.preDispatch.call( this, event ) === false ) {
                return;
            }

            // Determine handlers that should run if there are delegated events
            // Avoid non-left-click bubbling in Firefox (#3861)
            if ( delegateCount && !(event.button && event.type === "click") ) {

                for ( cur = event.target; cur != this; cur = cur.parentNode || this ) {

                    // Don't process clicks (ONLY) on disabled elements (#6911, #8165, #11382, #11764)
                    if ( cur.disabled !== true || event.type !== "click" ) {
                        selMatch = {};
                        matches = [];
                        for ( i = 0; i < delegateCount; i++ ) {
                            handleObj = handlers[ i ];
                            sel = handleObj.selector;

                            if ( selMatch[ sel ] === undefined ) {
                                selMatch[ sel ] = jQuery( sel, this ).index( cur ) >= 0;
                            }
                            if ( selMatch[ sel ] ) {
                                matches.push( handleObj );
                            }
                        }
                        if ( matches.length ) {
                            handlerQueue.push({ elem: cur, matches: matches });
                        }
                    }
                }
            }

            // Add the remaining (directly-bound) handlers
            if ( handlers.length > delegateCount ) {
                handlerQueue.push({ elem: this, matches: handlers.slice( delegateCount ) });
            }

            // Run delegates first; they may want to stop propagation beneath us
            for ( i = 0; i < handlerQueue.length && !event.isPropagationStopped(); i++ ) {
                matched = handlerQueue[ i ];
                event.currentTarget = matched.elem;

                for ( j = 0; j < matched.matches.length && !event.isImmediatePropagationStopped(); j++ ) {
                    handleObj = matched.matches[ j ];

                    // Triggered event must either 1) be non-exclusive and have no namespace, or
                    // 2) have namespace(s) a subset or equal to those in the bound event (both can have no namespace).
                    if ( run_all || (!event.namespace && !handleObj.namespace) || event.namespace_re && event.namespace_re.test( handleObj.namespace ) ) {

                        event.data = handleObj.data;
                        event.handleObj = handleObj;

                        ret = ( (jQuery.event.special[ handleObj.origType ] || {}).handle || handleObj.handler )
                            .apply( matched.elem, args );

                        if ( ret !== undefined ) {
                            event.result = ret;
                            if ( ret === false ) {
                                event.preventDefault();
                                event.stopPropagation();
                            }
                        }
                    }
                }
            }

            // Call the postDispatch hook for the mapped type
            if ( special.postDispatch ) {
                special.postDispatch.call( this, event );
            }

            return event.result;
        },

        // Includes some event props shared by KeyEvent and MouseEvent
        // *** attrChange attrName relatedNode srcElement  are not normalized, non-W3C, deprecated, will be removed in 1.8 ***
        props: "attrChange attrName relatedNode srcElement altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),

        fixHooks: {},

        keyHooks: {
            props: "char charCode key keyCode".split(" "),
            filter: function( event, original ) {

                // Add which for key events
                if ( event.which == null ) {
                    event.which = original.charCode != null ? original.charCode : original.keyCode;
                }

                return event;
            }
        },

        mouseHooks: {
            props: "button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
            filter: function( event, original ) {
                var eventDoc, doc, body,
                    button = original.button,
                    fromElement = original.fromElement;

                // Calculate pageX/Y if missing and clientX/Y available
                if ( event.pageX == null && original.clientX != null ) {
                    eventDoc = event.target.ownerDocument || document;
                    doc = eventDoc.documentElement;
                    body = eventDoc.body;

                    event.pageX = original.clientX + ( doc && doc.scrollLeft || body && body.scrollLeft || 0 ) - ( doc && doc.clientLeft || body && body.clientLeft || 0 );
                    event.pageY = original.clientY + ( doc && doc.scrollTop  || body && body.scrollTop  || 0 ) - ( doc && doc.clientTop  || body && body.clientTop  || 0 );
                }

                // Add relatedTarget, if necessary
                if ( !event.relatedTarget && fromElement ) {
                    event.relatedTarget = fromElement === event.target ? original.toElement : fromElement;
                }

                // Add which for click: 1 === left; 2 === middle; 3 === right
                // Note: button is not normalized, so don't use it
                if ( !event.which && button !== undefined ) {
                    event.which = ( button & 1 ? 1 : ( button & 2 ? 3 : ( button & 4 ? 2 : 0 ) ) );
                }

                return event;
            }
        },

        fix: function( event ) {
            if ( event[ jQuery.expando ] ) {
                return event;
            }

            // Create a writable copy of the event object and normalize some properties
            var i, prop,
                originalEvent = event,
                fixHook = jQuery.event.fixHooks[ event.type ] || {},
                copy = fixHook.props ? this.props.concat( fixHook.props ) : this.props;

            event = jQuery.Event( originalEvent );

            for ( i = copy.length; i; ) {
                prop = copy[ --i ];
                event[ prop ] = originalEvent[ prop ];
            }

            // Fix target property, if necessary (#1925, IE 6/7/8 & Safari2)
            if ( !event.target ) {
                event.target = originalEvent.srcElement || document;
            }

            // Target should not be a text node (#504, Safari)
            if ( event.target.nodeType === 3 ) {
                event.target = event.target.parentNode;
            }

            // For mouse/key events, metaKey==false if it's undefined (#3368, #11328; IE6/7/8)
            event.metaKey = !!event.metaKey;

            return fixHook.filter? fixHook.filter( event, originalEvent ) : event;
        },

        special: {
            load: {
                // Prevent triggered image.load events from bubbling to window.load
                noBubble: true
            },

            focus: {
                delegateType: "focusin"
            },
            blur: {
                delegateType: "focusout"
            },

            beforeunload: {
                setup: function( data, namespaces, eventHandle ) {
                    // We only want to do this special case on windows
                    if ( jQuery.isWindow( this ) ) {
                        this.onbeforeunload = eventHandle;
                    }
                },

                teardown: function( namespaces, eventHandle ) {
                    if ( this.onbeforeunload === eventHandle ) {
                        this.onbeforeunload = null;
                    }
                }
            }
        },

        simulate: function( type, elem, event, bubble ) {
            // Piggyback on a donor event to simulate a different one.
            // Fake originalEvent to avoid donor's stopPropagation, but if the
            // simulated event prevents default then we do the same on the donor.
            var e = jQuery.extend(
                new jQuery.Event(),
                event,
                { type: type,
                    isSimulated: true,
                    originalEvent: {}
                }
            );
            if ( bubble ) {
                jQuery.event.trigger( e, null, elem );
            } else {
                jQuery.event.dispatch.call( elem, e );
            }
            if ( e.isDefaultPrevented() ) {
                event.preventDefault();
            }
        }
    };

// Some plugins are using, but it's undocumented/deprecated and will be removed.
// The 1.7 special event interface should provide all the hooks needed now.
    jQuery.event.handle = jQuery.event.dispatch;

    jQuery.removeEvent = document.removeEventListener ?
        function( elem, type, handle ) {
            if ( elem.removeEventListener ) {
                elem.removeEventListener( type, handle, false );
            }
        } :
        function( elem, type, handle ) {
            var name = "on" + type;

            if ( elem.detachEvent ) {

                // #8545, #7054, preventing memory leaks for custom events in IE6-8 –
                // detachEvent needed property on element, by name of that event, to properly expose it to GC
                if ( typeof elem[ name ] === "undefined" ) {
                    elem[ name ] = null;
                }

                elem.detachEvent( name, handle );
            }
        };

    jQuery.Event = function( src, props ) {
        // Allow instantiation without the 'new' keyword
        if ( !(this instanceof jQuery.Event) ) {
            return new jQuery.Event( src, props );
        }

        // Event object
        if ( src && src.type ) {
            this.originalEvent = src;
            this.type = src.type;

            // Events bubbling up the document may have been marked as prevented
            // by a handler lower down the tree; reflect the correct value.
            this.isDefaultPrevented = ( src.defaultPrevented || src.returnValue === false ||
                src.getPreventDefault && src.getPreventDefault() ) ? returnTrue : returnFalse;

            // Event type
        } else {
            this.type = src;
        }

        // Put explicitly provided properties onto the event object
        if ( props ) {
            jQuery.extend( this, props );
        }

        // Create a timestamp if incoming event doesn't have one
        this.timeStamp = src && src.timeStamp || jQuery.now();

        // Mark it as fixed
        this[ jQuery.expando ] = true;
    };

    function returnFalse() {
        return false;
    }
    function returnTrue() {
        return true;
    }

// jQuery.Event is based on DOM3 Events as specified by the ECMAScript Language Binding
// http://www.w3.org/TR/2003/WD-DOM-Level-3-Events-20030331/ecma-script-binding.html
    jQuery.Event.prototype = {
        preventDefault: function() {
            this.isDefaultPrevented = returnTrue;

            var e = this.originalEvent;
            if ( !e ) {
                return;
            }

            // if preventDefault exists run it on the original event
            if ( e.preventDefault ) {
                e.preventDefault();

                // otherwise set the returnValue property of the original event to false (IE)
            } else {
                e.returnValue = false;
            }
        },
        stopPropagation: function() {
            this.isPropagationStopped = returnTrue;

            var e = this.originalEvent;
            if ( !e ) {
                return;
            }
            // if stopPropagation exists run it on the original event
            if ( e.stopPropagation ) {
                e.stopPropagation();
            }
            // otherwise set the cancelBubble property of the original event to true (IE)
            e.cancelBubble = true;
        },
        stopImmediatePropagation: function() {
            this.isImmediatePropagationStopped = returnTrue;
            this.stopPropagation();
        },
        isDefaultPrevented: returnFalse,
        isPropagationStopped: returnFalse,
        isImmediatePropagationStopped: returnFalse
    };

// Create mouseenter/leave events using mouseover/out and event-time checks
    jQuery.each({
        mouseenter: "mouseover",
        mouseleave: "mouseout"
    }, function( orig, fix ) {
        jQuery.event.special[ orig ] = {
            delegateType: fix,
            bindType: fix,

            handle: function( event ) {
                var ret,
                    target = this,
                    related = event.relatedTarget,
                    handleObj = event.handleObj,
                    selector = handleObj.selector;

                // For mousenter/leave call the handler if related is outside the target.
                // NB: No relatedTarget if the mouse left/entered the browser window
                if ( !related || (related !== target && !jQuery.contains( target, related )) ) {
                    event.type = handleObj.origType;
                    ret = handleObj.handler.apply( this, arguments );
                    event.type = fix;
                }
                return ret;
            }
        };
    });

// IE submit delegation
    if ( !jQuery.support.submitBubbles ) {

        jQuery.event.special.submit = {
            setup: function() {
                // Only need this for delegated form submit events
                if ( jQuery.nodeName( this, "form" ) ) {
                    return false;
                }

                // Lazy-add a submit handler when a descendant form may potentially be submitted
                jQuery.event.add( this, "click._submit keypress._submit", function( e ) {
                    // Node name check avoids a VML-related crash in IE (#9807)
                    var elem = e.target,
                        form = jQuery.nodeName( elem, "input" ) || jQuery.nodeName( elem, "button" ) ? elem.form : undefined;
                    if ( form && !jQuery._data( form, "_submit_attached" ) ) {
                        jQuery.event.add( form, "submit._submit", function( event ) {
                            event._submit_bubble = true;
                        });
                        jQuery._data( form, "_submit_attached", true );
                    }
                });
                // return undefined since we don't need an event listener
            },

            postDispatch: function( event ) {
                // If form was submitted by the user, bubble the event up the tree
                if ( event._submit_bubble ) {
                    delete event._submit_bubble;
                    if ( this.parentNode && !event.isTrigger ) {
                        jQuery.event.simulate( "submit", this.parentNode, event, true );
                    }
                }
            },

            teardown: function() {
                // Only need this for delegated form submit events
                if ( jQuery.nodeName( this, "form" ) ) {
                    return false;
                }

                // Remove delegated handlers; cleanData eventually reaps submit handlers attached above
                jQuery.event.remove( this, "._submit" );
            }
        };
    }

// IE change delegation and checkbox/radio fix
    if ( !jQuery.support.changeBubbles ) {

        jQuery.event.special.change = {

            setup: function() {

                if ( rformElems.test( this.nodeName ) ) {
                    // IE doesn't fire change on a check/radio until blur; trigger it on click
                    // after a propertychange. Eat the blur-change in special.change.handle.
                    // This still fires onchange a second time for check/radio after blur.
                    if ( this.type === "checkbox" || this.type === "radio" ) {
                        jQuery.event.add( this, "propertychange._change", function( event ) {
                            if ( event.originalEvent.propertyName === "checked" ) {
                                this._just_changed = true;
                            }
                        });
                        jQuery.event.add( this, "click._change", function( event ) {
                            if ( this._just_changed && !event.isTrigger ) {
                                this._just_changed = false;
                            }
                            // Allow triggered, simulated change events (#11500)
                            jQuery.event.simulate( "change", this, event, true );
                        });
                    }
                    return false;
                }
                // Delegated event; lazy-add a change handler on descendant inputs
                jQuery.event.add( this, "beforeactivate._change", function( e ) {
                    var elem = e.target;

                    if ( rformElems.test( elem.nodeName ) && !jQuery._data( elem, "_change_attached" ) ) {
                        jQuery.event.add( elem, "change._change", function( event ) {
                            if ( this.parentNode && !event.isSimulated && !event.isTrigger ) {
                                jQuery.event.simulate( "change", this.parentNode, event, true );
                            }
                        });
                        jQuery._data( elem, "_change_attached", true );
                    }
                });
            },

            handle: function( event ) {
                var elem = event.target;

                // Swallow native change events from checkbox/radio, we already triggered them above
                if ( this !== elem || event.isSimulated || event.isTrigger || (elem.type !== "radio" && elem.type !== "checkbox") ) {
                    return event.handleObj.handler.apply( this, arguments );
                }
            },

            teardown: function() {
                jQuery.event.remove( this, "._change" );

                return !rformElems.test( this.nodeName );
            }
        };
    }

// Create "bubbling" focus and blur events
    if ( !jQuery.support.focusinBubbles ) {
        jQuery.each({ focus: "focusin", blur: "focusout" }, function( orig, fix ) {

            // Attach a single capturing handler while someone wants focusin/focusout
            var attaches = 0,
                handler = function( event ) {
                    jQuery.event.simulate( fix, event.target, jQuery.event.fix( event ), true );
                };

            jQuery.event.special[ fix ] = {
                setup: function() {
                    if ( attaches++ === 0 ) {
                        document.addEventListener( orig, handler, true );
                    }
                },
                teardown: function() {
                    if ( --attaches === 0 ) {
                        document.removeEventListener( orig, handler, true );
                    }
                }
            };
        });
    }

    jQuery.fn.extend({

        on: function( types, selector, data, fn, /*INTERNAL*/ one ) {
            var origFn, type;

            // Types can be a map of types/handlers
            if ( typeof types === "object" ) {
                // ( types-Object, selector, data )
                if ( typeof selector !== "string" ) { // && selector != null
                    // ( types-Object, data )
                    data = data || selector;
                    selector = undefined;
                }
                for ( type in types ) {
                    this.on( type, selector, data, types[ type ], one );
                }
                return this;
            }

            if ( data == null && fn == null ) {
                // ( types, fn )
                fn = selector;
                data = selector = undefined;
            } else if ( fn == null ) {
                if ( typeof selector === "string" ) {
                    // ( types, selector, fn )
                    fn = data;
                    data = undefined;
                } else {
                    // ( types, data, fn )
                    fn = data;
                    data = selector;
                    selector = undefined;
                }
            }
            if ( fn === false ) {
                fn = returnFalse;
            } else if ( !fn ) {
                return this;
            }

            if ( one === 1 ) {
                origFn = fn;
                fn = function( event ) {
                    // Can use an empty set, since event contains the info
                    jQuery().off( event );
                    return origFn.apply( this, arguments );
                };
                // Use same guid so caller can remove using origFn
                fn.guid = origFn.guid || ( origFn.guid = jQuery.guid++ );
            }
            return this.each( function() {
                jQuery.event.add( this, types, fn, data, selector );
            });
        },
        one: function( types, selector, data, fn ) {
            return this.on( types, selector, data, fn, 1 );
        },
        off: function( types, selector, fn ) {
            var handleObj, type;
            if ( types && types.preventDefault && types.handleObj ) {
                // ( event )  dispatched jQuery.Event
                handleObj = types.handleObj;
                jQuery( types.delegateTarget ).off(
                    handleObj.namespace ? handleObj.origType + "." + handleObj.namespace : handleObj.origType,
                    handleObj.selector,
                    handleObj.handler
                );
                return this;
            }
            if ( typeof types === "object" ) {
                // ( types-object [, selector] )
                for ( type in types ) {
                    this.off( type, selector, types[ type ] );
                }
                return this;
            }
            if ( selector === false || typeof selector === "function" ) {
                // ( types [, fn] )
                fn = selector;
                selector = undefined;
            }
            if ( fn === false ) {
                fn = returnFalse;
            }
            return this.each(function() {
                jQuery.event.remove( this, types, fn, selector );
            });
        },

        bind: function( types, data, fn ) {
            return this.on( types, null, data, fn );
        },
        unbind: function( types, fn ) {
            return this.off( types, null, fn );
        },

        live: function( types, data, fn ) {
            jQuery( this.context ).on( types, this.selector, data, fn );
            return this;
        },
        die: function( types, fn ) {
            jQuery( this.context ).off( types, this.selector || "**", fn );
            return this;
        },

        delegate: function( selector, types, data, fn ) {
            return this.on( types, selector, data, fn );
        },
        undelegate: function( selector, types, fn ) {
            // ( namespace ) or ( selector, types [, fn] )
            return arguments.length == 1? this.off( selector, "**" ) : this.off( types, selector || "**", fn );
        },

        trigger: function( type, data ) {
            return this.each(function() {
                jQuery.event.trigger( type, data, this );
            });
        },
        triggerHandler: function( type, data ) {
            if ( this[0] ) {
                return jQuery.event.trigger( type, data, this[0], true );
            }
        },

        toggle: function( fn ) {
            // Save reference to arguments for access in closure
            var args = arguments,
                guid = fn.guid || jQuery.guid++,
                i = 0,
                toggler = function( event ) {
                    // Figure out which function to execute
                    var lastToggle = ( jQuery._data( this, "lastToggle" + fn.guid ) || 0 ) % i;
                    jQuery._data( this, "lastToggle" + fn.guid, lastToggle + 1 );

                    // Make sure that clicks stop
                    event.preventDefault();

                    // and execute the function
                    return args[ lastToggle ].apply( this, arguments ) || false;
                };

            // link all the functions, so any of them can unbind this click handler
            toggler.guid = guid;
            while ( i < args.length ) {
                args[ i++ ].guid = guid;
            }

            return this.click( toggler );
        },

        hover: function( fnOver, fnOut ) {
            return this.mouseenter( fnOver ).mouseleave( fnOut || fnOver );
        }
    });

    jQuery.each( ("blur focus focusin focusout load resize scroll unload click dblclick " +
        "mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave " +
        "change select submit keydown keypress keyup error contextmenu").split(" "), function( i, name ) {

        // Handle event binding
        jQuery.fn[ name ] = function( data, fn ) {
            if ( fn == null ) {
                fn = data;
                data = null;
            }

            return arguments.length > 0 ?
                this.on( name, null, data, fn ) :
                this.trigger( name );
        };

        if ( rkeyEvent.test( name ) ) {
            jQuery.event.fixHooks[ name ] = jQuery.event.keyHooks;
        }

        if ( rmouseEvent.test( name ) ) {
            jQuery.event.fixHooks[ name ] = jQuery.event.mouseHooks;
        }
    });
    /*!
     * Sizzle CSS Selector Engine
     *  Copyright 2012 jQuery Foundation and other contributors
     *  Released under the MIT license
     *  http://sizzlejs.com/
     */
    (function( window, undefined ) {

        var dirruns,
            cachedruns,
            assertGetIdNotName,
            Expr,
            getText,
            isXML,
            contains,
            compile,
            sortOrder,
            hasDuplicate,

            baseHasDuplicate = true,
            strundefined = "undefined",

            expando = ( "sizcache" + Math.random() ).replace( ".", "" ),

            document = window.document,
            docElem = document.documentElement,
            done = 0,
            slice = [].slice,
            push = [].push,

        // Augment a function for special use by Sizzle
            markFunction = function( fn, value ) {
                fn[ expando ] = value || true;
                return fn;
            },

            createCache = function() {
                var cache = {},
                    keys = [];

                return markFunction(function( key, value ) {
                    // Only keep the most recent entries
                    if ( keys.push( key ) > Expr.cacheLength ) {
                        delete cache[ keys.shift() ];
                    }

                    return (cache[ key ] = value);
                }, cache );
            },

            classCache = createCache(),
            tokenCache = createCache(),
            compilerCache = createCache(),

        // Regex

        // Whitespace characters http://www.w3.org/TR/css3-selectors/#whitespace
            whitespace = "[\\x20\\t\\r\\n\\f]",
        // http://www.w3.org/TR/css3-syntax/#characters
            characterEncoding = "(?:\\\\.|[-\\w]|[^\\x00-\\xa0])+",

        // Loosely modeled on CSS identifier characters
        // An unquoted value should be a CSS identifier (http://www.w3.org/TR/css3-selectors/#attribute-selectors)
        // Proper syntax: http://www.w3.org/TR/CSS21/syndata.html#value-def-identifier
            identifier = characterEncoding.replace( "w", "w#" ),

        // Acceptable operators http://www.w3.org/TR/selectors/#attribute-selectors
            operators = "([*^$|!~]?=)",
            attributes = "\\[" + whitespace + "*(" + characterEncoding + ")" + whitespace +
                "*(?:" + operators + whitespace + "*(?:(['\"])((?:\\\\.|[^\\\\])*?)\\3|(" + identifier + ")|)|)" + whitespace + "*\\]",

        // Prefer arguments not in parens/brackets,
        //   then attribute selectors and non-pseudos (denoted by :),
        //   then anything else
        // These preferences are here to reduce the number of selectors
        //   needing tokenize in the PSEUDO preFilter
            pseudos = ":(" + characterEncoding + ")(?:\\((?:(['\"])((?:\\\\.|[^\\\\])*?)\\2|([^()[\\]]*|(?:(?:" + attributes + ")|[^:]|\\\\.)*|.*))\\)|)",

        // For matchExpr.POS and matchExpr.needsContext
            pos = ":(nth|eq|gt|lt|first|last|even|odd)(?:\\(((?:-\\d)?\\d*)\\)|)(?=[^-]|$)",

        // Leading and non-escaped trailing whitespace, capturing some non-whitespace characters preceding the latter
            rtrim = new RegExp( "^" + whitespace + "+|((?:^|[^\\\\])(?:\\\\.)*)" + whitespace + "+$", "g" ),

            rcomma = new RegExp( "^" + whitespace + "*," + whitespace + "*" ),
            rcombinators = new RegExp( "^" + whitespace + "*([\\x20\\t\\r\\n\\f>+~])" + whitespace + "*" ),
            rpseudo = new RegExp( pseudos ),

        // Easily-parseable/retrievable ID or TAG or CLASS selectors
            rquickExpr = /^(?:#([\w\-]+)|(\w+)|\.([\w\-]+))$/,

            rnot = /^:not/,
            rsibling = /[\x20\t\r\n\f]*[+~]/,
            rendsWithNot = /:not\($/,

            rheader = /h\d/i,
            rinputs = /input|select|textarea|button/i,

            rbackslash = /\\(?!\\)/g,

            matchExpr = {
                "ID": new RegExp( "^#(" + characterEncoding + ")" ),
                "CLASS": new RegExp( "^\\.(" + characterEncoding + ")" ),
                "NAME": new RegExp( "^\\[name=['\"]?(" + characterEncoding + ")['\"]?\\]" ),
                "TAG": new RegExp( "^(" + characterEncoding.replace( "w", "w*" ) + ")" ),
                "ATTR": new RegExp( "^" + attributes ),
                "PSEUDO": new RegExp( "^" + pseudos ),
                "CHILD": new RegExp( "^:(only|nth|last|first)-child(?:\\(" + whitespace +
                    "*(even|odd|(([+-]|)(\\d*)n|)" + whitespace + "*(?:([+-]|)" + whitespace +
                    "*(\\d+)|))" + whitespace + "*\\)|)", "i" ),
                "POS": new RegExp( pos, "ig" ),
                // For use in libraries implementing .is()
                "needsContext": new RegExp( "^" + whitespace + "*[>+~]|" + pos, "i" )
            },

        // Support

        // Used for testing something on an element
            assert = function( fn ) {
                var div = document.createElement("div");

                try {
                    return fn( div );
                } catch (e) {
                    return false;
                } finally {
                    // release memory in IE
                    div = null;
                }
            },

        // Check if getElementsByTagName("*") returns only elements
            assertTagNameNoComments = assert(function( div ) {
                div.appendChild( document.createComment("") );
                return !div.getElementsByTagName("*").length;
            }),

        // Check if getAttribute returns normalized href attributes
            assertHrefNotNormalized = assert(function( div ) {
                div.innerHTML = "<a href='#'></a>";
                return div.firstChild && typeof div.firstChild.getAttribute !== strundefined &&
                    div.firstChild.getAttribute("href") === "#";
            }),

        // Check if attributes should be retrieved by attribute nodes
            assertAttributes = assert(function( div ) {
                div.innerHTML = "<select></select>";
                var type = typeof div.lastChild.getAttribute("multiple");
                // IE8 returns a string for some attributes even when not present
                return type !== "boolean" && type !== "string";
            }),

        // Check if getElementsByClassName can be trusted
            assertUsableClassName = assert(function( div ) {
                // Opera can't find a second classname (in 9.6)
                div.innerHTML = "<div class='hidden e'></div><div class='hidden'></div>";
                if ( !div.getElementsByClassName || !div.getElementsByClassName("e").length ) {
                    return false;
                }

                // Safari 3.2 caches class attributes and doesn't catch changes
                div.lastChild.className = "e";
                return div.getElementsByClassName("e").length === 2;
            }),

        // Check if getElementById returns elements by name
        // Check if getElementsByName privileges form controls or returns elements by ID
            assertUsableName = assert(function( div ) {
                // Inject content
                div.id = expando + 0;
                div.innerHTML = "<a name='" + expando + "'></a><div name='" + expando + "'></div>";
                docElem.insertBefore( div, docElem.firstChild );

                // Test
                var pass = document.getElementsByName &&
                    // buggy browsers will return fewer than the correct 2
                    document.getElementsByName( expando ).length === 2 +
                        // buggy browsers will return more than the correct 0
                        document.getElementsByName( expando + 0 ).length;
                assertGetIdNotName = !document.getElementById( expando );

                // Cleanup
                docElem.removeChild( div );

                return pass;
            });

// If slice is not available, provide a backup
        try {
            slice.call( docElem.childNodes, 0 )[0].nodeType;
        } catch ( e ) {
            slice = function( i ) {
                var elem, results = [];
                for ( ; (elem = this[i]); i++ ) {
                    results.push( elem );
                }
                return results;
            };
        }

        function Sizzle( selector, context, results, seed ) {
            results = results || [];
            context = context || document;
            var match, elem, xml, m,
                nodeType = context.nodeType;

            if ( nodeType !== 1 && nodeType !== 9 ) {
                return [];
            }

            if ( !selector || typeof selector !== "string" ) {
                return results;
            }

            xml = isXML( context );

            if ( !xml && !seed ) {
                if ( (match = rquickExpr.exec( selector )) ) {
                    // Speed-up: Sizzle("#ID")
                    if ( (m = match[1]) ) {
                        if ( nodeType === 9 ) {
                            elem = context.getElementById( m );
                            // Check parentNode to catch when Blackberry 4.6 returns
                            // nodes that are no longer in the document #6963
                            if ( elem && elem.parentNode ) {
                                // Handle the case where IE, Opera, and Webkit return items
                                // by name instead of ID
                                if ( elem.id === m ) {
                                    results.push( elem );
                                    return results;
                                }
                            } else {
                                return results;
                            }
                        } else {
                            // Context is not a document
                            if ( context.ownerDocument && (elem = context.ownerDocument.getElementById( m )) &&
                                contains( context, elem ) && elem.id === m ) {
                                results.push( elem );
                                return results;
                            }
                        }

                        // Speed-up: Sizzle("TAG")
                    } else if ( match[2] ) {
                        push.apply( results, slice.call(context.getElementsByTagName( selector ), 0) );
                        return results;

                        // Speed-up: Sizzle(".CLASS")
                    } else if ( (m = match[3]) && assertUsableClassName && context.getElementsByClassName ) {
                        push.apply( results, slice.call(context.getElementsByClassName( m ), 0) );
                        return results;
                    }
                }
            }

            // All others
            return select( selector, context, results, seed, xml );
        }

        Sizzle.matches = function( expr, elements ) {
            return Sizzle( expr, null, null, elements );
        };

        Sizzle.matchesSelector = function( elem, expr ) {
            return Sizzle( expr, null, null, [ elem ] ).length > 0;
        };

// Returns a function to use in pseudos for input types
        function createInputPseudo( type ) {
            return function( elem ) {
                var name = elem.nodeName.toLowerCase();
                return name === "input" && elem.type === type;
            };
        }

// Returns a function to use in pseudos for buttons
        function createButtonPseudo( type ) {
            return function( elem ) {
                var name = elem.nodeName.toLowerCase();
                return (name === "input" || name === "button") && elem.type === type;
            };
        }

        /**
         * Utility function for retrieving the text value of an array of DOM nodes
         * @param {Array|Element} elem
         */
        getText = Sizzle.getText = function( elem ) {
            var node,
                ret = "",
                i = 0,
                nodeType = elem.nodeType;

            if ( nodeType ) {
                if ( nodeType === 1 || nodeType === 9 || nodeType === 11 ) {
                    // Use textContent for elements
                    // innerText usage removed for consistency of new lines (see #11153)
                    if ( typeof elem.textContent === "string" ) {
                        return elem.textContent;
                    } else {
                        // Traverse its children
                        for ( elem = elem.firstChild; elem; elem = elem.nextSibling ) {
                            ret += getText( elem );
                        }
                    }
                } else if ( nodeType === 3 || nodeType === 4 ) {
                    return elem.nodeValue;
                }
                // Do not include comment or processing instruction nodes
            } else {

                // If no nodeType, this is expected to be an array
                for ( ; (node = elem[i]); i++ ) {
                    // Do not traverse comment nodes
                    ret += getText( node );
                }
            }
            return ret;
        };

        isXML = Sizzle.isXML = function isXML( elem ) {
            // documentElement is verified for cases where it doesn't yet exist
            // (such as loading iframes in IE - #4833)
            var documentElement = elem && (elem.ownerDocument || elem).documentElement;
            return documentElement ? documentElement.nodeName !== "HTML" : false;
        };

// Element contains another
        contains = Sizzle.contains = docElem.contains ?
            function( a, b ) {
                var adown = a.nodeType === 9 ? a.documentElement : a,
                    bup = b && b.parentNode;
                return a === bup || !!( bup && bup.nodeType === 1 && adown.contains && adown.contains(bup) );
            } :
            docElem.compareDocumentPosition ?
                function( a, b ) {
                    return b && !!( a.compareDocumentPosition( b ) & 16 );
                } :
                function( a, b ) {
                    while ( (b = b.parentNode) ) {
                        if ( b === a ) {
                            return true;
                        }
                    }
                    return false;
                };

        Sizzle.attr = function( elem, name ) {
            var attr,
                xml = isXML( elem );

            if ( !xml ) {
                name = name.toLowerCase();
            }
            if ( Expr.attrHandle[ name ] ) {
                return Expr.attrHandle[ name ]( elem );
            }
            if ( assertAttributes || xml ) {
                return elem.getAttribute( name );
            }
            attr = elem.getAttributeNode( name );
            return attr ?
                typeof elem[ name ] === "boolean" ?
                    elem[ name ] ? name : null :
                    attr.specified ? attr.value : null :
                null;
        };

        Expr = Sizzle.selectors = {

            // Can be adjusted by the user
            cacheLength: 50,

            createPseudo: markFunction,

            match: matchExpr,

            order: new RegExp( "ID|TAG" +
                (assertUsableName ? "|NAME" : "") +
                (assertUsableClassName ? "|CLASS" : "")
            ),

            // IE6/7 return a modified href
            attrHandle: assertHrefNotNormalized ?
            {} :
            {
                "href": function( elem ) {
                    return elem.getAttribute( "href", 2 );
                },
                "type": function( elem ) {
                    return elem.getAttribute("type");
                }
            },

            find: {
                "ID": assertGetIdNotName ?
                    function( id, context, xml ) {
                        if ( typeof context.getElementById !== strundefined && !xml ) {
                            var m = context.getElementById( id );
                            // Check parentNode to catch when Blackberry 4.6 returns
                            // nodes that are no longer in the document #6963
                            return m && m.parentNode ? [m] : [];
                        }
                    } :
                    function( id, context, xml ) {
                        if ( typeof context.getElementById !== strundefined && !xml ) {
                            var m = context.getElementById( id );

                            return m ?
                                m.id === id || typeof m.getAttributeNode !== strundefined && m.getAttributeNode("id").value === id ?
                                    [m] :
                                    undefined :
                                [];
                        }
                    },

                "TAG": assertTagNameNoComments ?
                    function( tag, context ) {
                        if ( typeof context.getElementsByTagName !== strundefined ) {
                            return context.getElementsByTagName( tag );
                        }
                    } :
                    function( tag, context ) {
                        var results = context.getElementsByTagName( tag );

                        // Filter out possible comments
                        if ( tag === "*" ) {
                            var elem,
                                tmp = [],
                                i = 0;

                            for ( ; (elem = results[i]); i++ ) {
                                if ( elem.nodeType === 1 ) {
                                    tmp.push( elem );
                                }
                            }

                            return tmp;
                        }
                        return results;
                    },

                "NAME": function( tag, context ) {
                    if ( typeof context.getElementsByName !== strundefined ) {
                        return context.getElementsByName( name );
                    }
                },

                "CLASS": function( className, context, xml ) {
                    if ( typeof context.getElementsByClassName !== strundefined && !xml ) {
                        return context.getElementsByClassName( className );
                    }
                }
            },

            relative: {
                ">": { dir: "parentNode", first: true },
                " ": { dir: "parentNode" },
                "+": { dir: "previousSibling", first: true },
                "~": { dir: "previousSibling" }
            },

            preFilter: {
                "ATTR": function( match ) {
                    match[1] = match[1].replace( rbackslash, "" );

                    // Move the given value to match[3] whether quoted or unquoted
                    match[3] = ( match[4] || match[5] || "" ).replace( rbackslash, "" );

                    if ( match[2] === "~=" ) {
                        match[3] = " " + match[3] + " ";
                    }

                    return match.slice( 0, 4 );
                },

                "CHILD": function( match ) {
                    /* matches from matchExpr.CHILD
                     1 type (only|nth|...)
                     2 argument (even|odd|\d*|\d*n([+-]\d+)?|...)
                     3 xn-component of xn+y argument ([+-]?\d*n|)
                     4 sign of xn-component
                     5 x of xn-component
                     6 sign of y-component
                     7 y of y-component
                     */
                    match[1] = match[1].toLowerCase();

                    if ( match[1] === "nth" ) {
                        // nth-child requires argument
                        if ( !match[2] ) {
                            Sizzle.error( match[0] );
                        }

                        // numeric x and y parameters for Expr.filter.CHILD
                        // remember that false/true cast respectively to 0/1
                        match[3] = +( match[3] ? match[4] + (match[5] || 1) : 2 * ( match[2] === "even" || match[2] === "odd" ) );
                        match[4] = +( ( match[6] + match[7] ) || match[2] === "odd" );

                        // other types prohibit arguments
                    } else if ( match[2] ) {
                        Sizzle.error( match[0] );
                    }

                    return match;
                },

                "PSEUDO": function( match, context, xml ) {
                    var unquoted, excess;
                    if ( matchExpr["CHILD"].test( match[0] ) ) {
                        return null;
                    }

                    if ( match[3] ) {
                        match[2] = match[3];
                    } else if ( (unquoted = match[4]) ) {
                        // Only check arguments that contain a pseudo
                        if ( rpseudo.test(unquoted) &&
                            // Get excess from tokenize (recursively)
                            (excess = tokenize( unquoted, context, xml, true )) &&
                            // advance to the next closing parenthesis
                            (excess = unquoted.indexOf( ")", unquoted.length - excess ) - unquoted.length) ) {

                            // excess is a negative index
                            unquoted = unquoted.slice( 0, excess );
                            match[0] = match[0].slice( 0, excess );
                        }
                        match[2] = unquoted;
                    }

                    // Return only captures needed by the pseudo filter method (type and argument)
                    return match.slice( 0, 3 );
                }
            },

            filter: {
                "ID": assertGetIdNotName ?
                    function( id ) {
                        id = id.replace( rbackslash, "" );
                        return function( elem ) {
                            return elem.getAttribute("id") === id;
                        };
                    } :
                    function( id ) {
                        id = id.replace( rbackslash, "" );
                        return function( elem ) {
                            var node = typeof elem.getAttributeNode !== strundefined && elem.getAttributeNode("id");
                            return node && node.value === id;
                        };
                    },

                "TAG": function( nodeName ) {
                    if ( nodeName === "*" ) {
                        return function() { return true; };
                    }
                    nodeName = nodeName.replace( rbackslash, "" ).toLowerCase();

                    return function( elem ) {
                        return elem.nodeName && elem.nodeName.toLowerCase() === nodeName;
                    };
                },

                "CLASS": function( className ) {
                    var pattern = classCache[ expando ][ className ];
                    if ( !pattern ) {
                        pattern = classCache( className, new RegExp("(^|" + whitespace + ")" + className + "(" + whitespace + "|$)") );
                    }
                    return function( elem ) {
                        return pattern.test( elem.className || (typeof elem.getAttribute !== strundefined && elem.getAttribute("class")) || "" );
                    };
                },

                "ATTR": function( name, operator, check ) {
                    if ( !operator ) {
                        return function( elem ) {
                            return Sizzle.attr( elem, name ) != null;
                        };
                    }

                    return function( elem ) {
                        var result = Sizzle.attr( elem, name ),
                            value = result + "";

                        if ( result == null ) {
                            return operator === "!=";
                        }

                        switch ( operator ) {
                            case "=":
                                return value === check;
                            case "!=":
                                return value !== check;
                            case "^=":
                                return check && value.indexOf( check ) === 0;
                            case "*=":
                                return check && value.indexOf( check ) > -1;
                            case "$=":
                                return check && value.substr( value.length - check.length ) === check;
                            case "~=":
                                return ( " " + value + " " ).indexOf( check ) > -1;
                            case "|=":
                                return value === check || value.substr( 0, check.length + 1 ) === check + "-";
                        }
                    };
                },

                "CHILD": function( type, argument, first, last ) {

                    if ( type === "nth" ) {
                        var doneName = done++;

                        return function( elem ) {
                            var parent, diff,
                                count = 0,
                                node = elem;

                            if ( first === 1 && last === 0 ) {
                                return true;
                            }

                            parent = elem.parentNode;

                            if ( parent && (parent[ expando ] !== doneName || !elem.sizset) ) {
                                for ( node = parent.firstChild; node; node = node.nextSibling ) {
                                    if ( node.nodeType === 1 ) {
                                        node.sizset = ++count;
                                        if ( node === elem ) {
                                            break;
                                        }
                                    }
                                }

                                parent[ expando ] = doneName;
                            }

                            diff = elem.sizset - last;

                            if ( first === 0 ) {
                                return diff === 0;

                            } else {
                                return ( diff % first === 0 && diff / first >= 0 );
                            }
                        };
                    }

                    return function( elem ) {
                        var node = elem;

                        switch ( type ) {
                            case "only":
                            case "first":
                                while ( (node = node.previousSibling) ) {
                                    if ( node.nodeType === 1 ) {
                                        return false;
                                    }
                                }

                                if ( type === "first" ) {
                                    return true;
                                }

                                node = elem;

                            /* falls through */
                            case "last":
                                while ( (node = node.nextSibling) ) {
                                    if ( node.nodeType === 1 ) {
                                        return false;
                                    }
                                }

                                return true;
                        }
                    };
                },

                "PSEUDO": function( pseudo, argument, context, xml ) {
                    // pseudo-class names are case-insensitive
                    // http://www.w3.org/TR/selectors/#pseudo-classes
                    // Prioritize by case sensitivity in case custom pseudos are added with uppercase letters
                    var args,
                        fn = Expr.pseudos[ pseudo ] || Expr.pseudos[ pseudo.toLowerCase() ];

                    if ( !fn ) {
                        Sizzle.error( "unsupported pseudo: " + pseudo );
                    }

                    // The user may use createPseudo to indicate that
                    // arguments are needed to create the filter function
                    // just as Sizzle does
                    if ( !fn[ expando ] ) {
                        if ( fn.length > 1 ) {
                            args = [ pseudo, pseudo, "", argument ];
                            return function( elem ) {
                                return fn( elem, 0, args );
                            };
                        }
                        return fn;
                    }

                    return fn( argument, context, xml );
                }
            },

            pseudos: {
                "not": markFunction(function( selector, context, xml ) {
                    // Trim the selector passed to compile
                    // to avoid treating leading and trailing
                    // spaces as combinators
                    var matcher = compile( selector.replace( rtrim, "$1" ), context, xml );
                    return function( elem ) {
                        return !matcher( elem );
                    };
                }),

                "enabled": function( elem ) {
                    return elem.disabled === false;
                },

                "disabled": function( elem ) {
                    return elem.disabled === true;
                },

                "checked": function( elem ) {
                    // In CSS3, :checked should return both checked and selected elements
                    // http://www.w3.org/TR/2011/REC-css3-selectors-20110929/#checked
                    var nodeName = elem.nodeName.toLowerCase();
                    return (nodeName === "input" && !!elem.checked) || (nodeName === "option" && !!elem.selected);
                },

                "selected": function( elem ) {
                    // Accessing this property makes selected-by-default
                    // options in Safari work properly
                    if ( elem.parentNode ) {
                        elem.parentNode.selectedIndex;
                    }

                    return elem.selected === true;
                },

                "parent": function( elem ) {
                    return !Expr.pseudos["empty"]( elem );
                },

                "empty": function( elem ) {
                    // http://www.w3.org/TR/selectors/#empty-pseudo
                    // :empty is only affected by element nodes and content nodes(including text(3), cdata(4)),
                    //   not comment, processing instructions, or others
                    // Thanks to Diego Perini for the nodeName shortcut
                    //   Greater than "@" means alpha characters (specifically not starting with "#" or "?")
                    var nodeType;
                    elem = elem.firstChild;
                    while ( elem ) {
                        if ( elem.nodeName > "@" || (nodeType = elem.nodeType) === 3 || nodeType === 4 ) {
                            return false;
                        }
                        elem = elem.nextSibling;
                    }
                    return true;
                },

                "contains": markFunction(function( text ) {
                    return function( elem ) {
                        return ( elem.textContent || elem.innerText || getText( elem ) ).indexOf( text ) > -1;
                    };
                }),

                "has": markFunction(function( selector ) {
                    return function( elem ) {
                        return Sizzle( selector, elem ).length > 0;
                    };
                }),

                "header": function( elem ) {
                    return rheader.test( elem.nodeName );
                },

                "text": function( elem ) {
                    var type, attr;
                    // IE6 and 7 will map elem.type to 'text' for new HTML5 types (search, etc)
                    // use getAttribute instead to test this case
                    return elem.nodeName.toLowerCase() === "input" &&
                        (type = elem.type) === "text" &&
                        ( (attr = elem.getAttribute("type")) == null || attr.toLowerCase() === type );
                },

                // Input types
                "radio": createInputPseudo("radio"),
                "checkbox": createInputPseudo("checkbox"),
                "file": createInputPseudo("file"),
                "password": createInputPseudo("password"),
                "image": createInputPseudo("image"),

                "submit": createButtonPseudo("submit"),
                "reset": createButtonPseudo("reset"),

                "button": function( elem ) {
                    var name = elem.nodeName.toLowerCase();
                    return name === "input" && elem.type === "button" || name === "button";
                },

                "input": function( elem ) {
                    return rinputs.test( elem.nodeName );
                },

                "focus": function( elem ) {
                    var doc = elem.ownerDocument;
                    return elem === doc.activeElement && (!doc.hasFocus || doc.hasFocus()) && !!(elem.type || elem.href);
                },

                "active": function( elem ) {
                    return elem === elem.ownerDocument.activeElement;
                }
            },

            setFilters: {
                "first": function( elements, argument, not ) {
                    return not ? elements.slice( 1 ) : [ elements[0] ];
                },

                "last": function( elements, argument, not ) {
                    var elem = elements.pop();
                    return not ? elements : [ elem ];
                },

                "even": function( elements, argument, not ) {
                    var results = [],
                        i = not ? 1 : 0,
                        len = elements.length;
                    for ( ; i < len; i = i + 2 ) {
                        results.push( elements[i] );
                    }
                    return results;
                },

                "odd": function( elements, argument, not ) {
                    var results = [],
                        i = not ? 0 : 1,
                        len = elements.length;
                    for ( ; i < len; i = i + 2 ) {
                        results.push( elements[i] );
                    }
                    return results;
                },

                "lt": function( elements, argument, not ) {
                    return not ? elements.slice( +argument ) : elements.slice( 0, +argument );
                },

                "gt": function( elements, argument, not ) {
                    return not ? elements.slice( 0, +argument + 1 ) : elements.slice( +argument + 1 );
                },

                "eq": function( elements, argument, not ) {
                    var elem = elements.splice( +argument, 1 );
                    return not ? elements : elem;
                }
            }
        };

        function siblingCheck( a, b, ret ) {
            if ( a === b ) {
                return ret;
            }

            var cur = a.nextSibling;

            while ( cur ) {
                if ( cur === b ) {
                    return -1;
                }

                cur = cur.nextSibling;
            }

            return 1;
        }

        sortOrder = docElem.compareDocumentPosition ?
            function( a, b ) {
                if ( a === b ) {
                    hasDuplicate = true;
                    return 0;
                }

                return ( !a.compareDocumentPosition || !b.compareDocumentPosition ?
                    a.compareDocumentPosition :
                    a.compareDocumentPosition(b) & 4
                    ) ? -1 : 1;
            } :
            function( a, b ) {
                // The nodes are identical, we can exit early
                if ( a === b ) {
                    hasDuplicate = true;
                    return 0;

                    // Fallback to using sourceIndex (in IE) if it's available on both nodes
                } else if ( a.sourceIndex && b.sourceIndex ) {
                    return a.sourceIndex - b.sourceIndex;
                }

                var al, bl,
                    ap = [],
                    bp = [],
                    aup = a.parentNode,
                    bup = b.parentNode,
                    cur = aup;

                // If the nodes are siblings (or identical) we can do a quick check
                if ( aup === bup ) {
                    return siblingCheck( a, b );

                    // If no parents were found then the nodes are disconnected
                } else if ( !aup ) {
                    return -1;

                } else if ( !bup ) {
                    return 1;
                }

                // Otherwise they're somewhere else in the tree so we need
                // to build up a full list of the parentNodes for comparison
                while ( cur ) {
                    ap.unshift( cur );
                    cur = cur.parentNode;
                }

                cur = bup;

                while ( cur ) {
                    bp.unshift( cur );
                    cur = cur.parentNode;
                }

                al = ap.length;
                bl = bp.length;

                // Start walking down the tree looking for a discrepancy
                for ( var i = 0; i < al && i < bl; i++ ) {
                    if ( ap[i] !== bp[i] ) {
                        return siblingCheck( ap[i], bp[i] );
                    }
                }

                // We ended someplace up the tree so do a sibling check
                return i === al ?
                    siblingCheck( a, bp[i], -1 ) :
                    siblingCheck( ap[i], b, 1 );
            };

// Always assume the presence of duplicates if sort doesn't
// pass them to our comparison function (as in Google Chrome).
        [0, 0].sort( sortOrder );
        baseHasDuplicate = !hasDuplicate;

// Document sorting and removing duplicates
        Sizzle.uniqueSort = function( results ) {
            var elem,
                i = 1;

            hasDuplicate = baseHasDuplicate;
            results.sort( sortOrder );

            if ( hasDuplicate ) {
                for ( ; (elem = results[i]); i++ ) {
                    if ( elem === results[ i - 1 ] ) {
                        results.splice( i--, 1 );
                    }
                }
            }

            return results;
        };

        Sizzle.error = function( msg ) {
            throw new Error( "Syntax error, unrecognized expression: " + msg );
        };

        function tokenize( selector, context, xml, parseOnly ) {
            var matched, match, tokens, type,
                soFar, groups, group, i,
                preFilters, filters,
                checkContext = !xml && context !== document,
            // Token cache should maintain spaces
                key = ( checkContext ? "<s>" : "" ) + selector.replace( rtrim, "$1<s>" ),
                cached = tokenCache[ expando ][ key ];

            if ( cached ) {
                return parseOnly ? 0 : slice.call( cached, 0 );
            }

            soFar = selector;
            groups = [];
            i = 0;
            preFilters = Expr.preFilter;
            filters = Expr.filter;

            while ( soFar ) {

                // Comma and first run
                if ( !matched || (match = rcomma.exec( soFar )) ) {
                    if ( match ) {
                        soFar = soFar.slice( match[0].length );
                        tokens.selector = group;
                    }
                    groups.push( tokens = [] );
                    group = "";

                    // Need to make sure we're within a narrower context if necessary
                    // Adding a descendant combinator will generate what is needed
                    if ( checkContext ) {
                        soFar = " " + soFar;
                    }
                }

                matched = false;

                // Combinators
                if ( (match = rcombinators.exec( soFar )) ) {
                    group += match[0];
                    soFar = soFar.slice( match[0].length );

                    // Cast descendant combinators to space
                    matched = tokens.push({
                        part: match.pop().replace( rtrim, " " ),
                        string: match[0],
                        captures: match
                    });
                }

                // Filters
                for ( type in filters ) {
                    if ( (match = matchExpr[ type ].exec( soFar )) && (!preFilters[ type ] ||
                        ( match = preFilters[ type ](match, context, xml) )) ) {

                        group += match[0];
                        soFar = soFar.slice( match[0].length );
                        matched = tokens.push({
                            part: type,
                            string: match.shift(),
                            captures: match
                        });
                    }
                }

                if ( !matched ) {
                    break;
                }
            }

            // Attach the full group as a selector
            if ( group ) {
                tokens.selector = group;
            }

            // Return the length of the invalid excess
            // if we're just parsing
            // Otherwise, throw an error or return tokens
            return parseOnly ?
                soFar.length :
                soFar ?
                    Sizzle.error( selector ) :
                    // Cache the tokens
                    slice.call( tokenCache(key, groups), 0 );
        }

        function addCombinator( matcher, combinator, context, xml ) {
            var dir = combinator.dir,
                doneName = done++;

            if ( !matcher ) {
                // If there is no matcher to check, check against the context
                matcher = function( elem ) {
                    return elem === context;
                };
            }
            return combinator.first ?
                function( elem ) {
                    while ( (elem = elem[ dir ]) ) {
                        if ( elem.nodeType === 1 ) {
                            return matcher( elem ) && elem;
                        }
                    }
                } :
                xml ?
                    function( elem ) {
                        while ( (elem = elem[ dir ]) ) {
                            if ( elem.nodeType === 1 ) {
                                if ( matcher( elem ) ) {
                                    return elem;
                                }
                            }
                        }
                    } :
                    function( elem ) {
                        var cache,
                            dirkey = doneName + "." + dirruns,
                            cachedkey = dirkey + "." + cachedruns;
                        while ( (elem = elem[ dir ]) ) {
                            if ( elem.nodeType === 1 ) {
                                if ( (cache = elem[ expando ]) === cachedkey ) {
                                    return elem.sizset;
                                } else if ( typeof cache === "string" && cache.indexOf(dirkey) === 0 ) {
                                    if ( elem.sizset ) {
                                        return elem;
                                    }
                                } else {
                                    elem[ expando ] = cachedkey;
                                    if ( matcher( elem ) ) {
                                        elem.sizset = true;
                                        return elem;
                                    }
                                    elem.sizset = false;
                                }
                            }
                        }
                    };
        }

        function addMatcher( higher, deeper ) {
            return higher ?
                function( elem ) {
                    var result = deeper( elem );
                    return result && higher( result === true ? elem : result );
                } :
                deeper;
        }

// ["TAG", ">", "ID", " ", "CLASS"]
        function matcherFromTokens( tokens, context, xml ) {
            var token, matcher,
                i = 0;

            for ( ; (token = tokens[i]); i++ ) {
                if ( Expr.relative[ token.part ] ) {
                    matcher = addCombinator( matcher, Expr.relative[ token.part ], context, xml );
                } else {
                    matcher = addMatcher( matcher, Expr.filter[ token.part ].apply(null, token.captures.concat( context, xml )) );
                }
            }

            return matcher;
        }

        function matcherFromGroupMatchers( matchers ) {
            return function( elem ) {
                var matcher,
                    j = 0;
                for ( ; (matcher = matchers[j]); j++ ) {
                    if ( matcher(elem) ) {
                        return true;
                    }
                }
                return false;
            };
        }

        compile = Sizzle.compile = function( selector, context, xml ) {
            var group, i, len,
                cached = compilerCache[ expando ][ selector ];

            // Return a cached group function if already generated (context dependent)
            if ( cached && cached.context === context ) {
                return cached;
            }

            // Generate a function of recursive functions that can be used to check each element
            group = tokenize( selector, context, xml );
            for ( i = 0, len = group.length; i < len; i++ ) {
                group[i] = matcherFromTokens(group[i], context, xml);
            }

            // Cache the compiled function
            cached = compilerCache( selector, matcherFromGroupMatchers(group) );
            cached.context = context;
            cached.runs = cached.dirruns = 0;
            return cached;
        };

        function multipleContexts( selector, contexts, results, seed ) {
            var i = 0,
                len = contexts.length;
            for ( ; i < len; i++ ) {
                Sizzle( selector, contexts[i], results, seed );
            }
        }

        function handlePOSGroup( selector, posfilter, argument, contexts, seed, not ) {
            var results,
                fn = Expr.setFilters[ posfilter.toLowerCase() ];

            if ( !fn ) {
                Sizzle.error( posfilter );
            }

            if ( selector || !(results = seed) ) {
                multipleContexts( selector || "*", contexts, (results = []), seed );
            }

            return results.length > 0 ? fn( results, argument, not ) : [];
        }

        function handlePOS( groups, context, results, seed ) {
            var group, part, j, groupLen, token, selector,
                anchor, elements, match, matched,
                lastIndex, currentContexts, not,
                i = 0,
                len = groups.length,
                rpos = matchExpr["POS"],
            // This is generated here in case matchExpr["POS"] is extended
                rposgroups = new RegExp( "^" + rpos.source + "(?!" + whitespace + ")", "i" ),
            // This is for making sure non-participating
            // matching groups are represented cross-browser (IE6-8)
                setUndefined = function() {
                    var i = 1,
                        len = arguments.length - 2;
                    for ( ; i < len; i++ ) {
                        if ( arguments[i] === undefined ) {
                            match[i] = undefined;
                        }
                    }
                };

            for ( ; i < len; i++ ) {
                group = groups[i];
                part = "";
                elements = seed;
                for ( j = 0, groupLen = group.length; j < groupLen; j++ ) {
                    token = group[j];
                    selector = token.string;
                    if ( token.part === "PSEUDO" ) {
                        // Reset regex index to 0
                        rpos.exec("");
                        anchor = 0;
                        while ( (match = rpos.exec( selector )) ) {
                            matched = true;
                            lastIndex = rpos.lastIndex = match.index + match[0].length;
                            if ( lastIndex > anchor ) {
                                part += selector.slice( anchor, match.index );
                                anchor = lastIndex;
                                currentContexts = [ context ];

                                if ( rcombinators.test(part) ) {
                                    if ( elements ) {
                                        currentContexts = elements;
                                    }
                                    elements = seed;
                                }

                                if ( (not = rendsWithNot.test( part )) ) {
                                    part = part.slice( 0, -5 ).replace( rcombinators, "$&*" );
                                    anchor++;
                                }

                                if ( match.length > 1 ) {
                                    match[0].replace( rposgroups, setUndefined );
                                }
                                elements = handlePOSGroup( part, match[1], match[2], currentContexts, elements, not );
                            }
                            part = "";
                        }

                    }

                    if ( !matched ) {
                        part += selector;
                    }
                    matched = false;
                }

                if ( part ) {
                    if ( rcombinators.test(part) ) {
                        multipleContexts( part, elements || [ context ], results, seed );
                    } else {
                        Sizzle( part, context, results, seed ? seed.concat(elements) : elements );
                    }
                } else {
                    push.apply( results, elements );
                }
            }

            // Do not sort if this is a single filter
            return len === 1 ? results : Sizzle.uniqueSort( results );
        }

        function select( selector, context, results, seed, xml ) {
            // Remove excessive whitespace
            selector = selector.replace( rtrim, "$1" );
            var elements, matcher, cached, elem,
                i, tokens, token, lastToken, findContext, type,
                match = tokenize( selector, context, xml ),
                contextNodeType = context.nodeType;

            // POS handling
            if ( matchExpr["POS"].test(selector) ) {
                return handlePOS( match, context, results, seed );
            }

            if ( seed ) {
                elements = slice.call( seed, 0 );

                // To maintain document order, only narrow the
                // set if there is one group
            } else if ( match.length === 1 ) {

                // Take a shortcut and set the context if the root selector is an ID
                if ( (tokens = slice.call( match[0], 0 )).length > 2 &&
                    (token = tokens[0]).part === "ID" &&
                    contextNodeType === 9 && !xml &&
                    Expr.relative[ tokens[1].part ] ) {

                    context = Expr.find["ID"]( token.captures[0].replace( rbackslash, "" ), context, xml )[0];
                    if ( !context ) {
                        return results;
                    }

                    selector = selector.slice( tokens.shift().string.length );
                }

                findContext = ( (match = rsibling.exec( tokens[0].string )) && !match.index && context.parentNode ) || context;

                // Reduce the set if possible
                lastToken = "";
                for ( i = tokens.length - 1; i >= 0; i-- ) {
                    token = tokens[i];
                    type = token.part;
                    lastToken = token.string + lastToken;
                    if ( Expr.relative[ type ] ) {
                        break;
                    }
                    if ( Expr.order.test(type) ) {
                        elements = Expr.find[ type ]( token.captures[0].replace( rbackslash, "" ), findContext, xml );
                        if ( elements == null ) {
                            continue;
                        } else {
                            selector = selector.slice( 0, selector.length - lastToken.length ) +
                                lastToken.replace( matchExpr[ type ], "" );

                            if ( !selector ) {
                                push.apply( results, slice.call(elements, 0) );
                            }

                            break;
                        }
                    }
                }
            }

            // Only loop over the given elements once
            if ( selector ) {
                matcher = compile( selector, context, xml );
                dirruns = matcher.dirruns++;
                if ( elements == null ) {
                    elements = Expr.find["TAG"]( "*", (rsibling.test( selector ) && context.parentNode) || context );
                }

                for ( i = 0; (elem = elements[i]); i++ ) {
                    cachedruns = matcher.runs++;
                    if ( matcher(elem) ) {
                        results.push( elem );
                    }
                }
            }

            return results;
        }

        if ( document.querySelectorAll ) {
            (function() {
                var disconnectedMatch,
                    oldSelect = select,
                    rescape = /'|\\/g,
                    rattributeQuotes = /\=[\x20\t\r\n\f]*([^'"\]]*)[\x20\t\r\n\f]*\]/g,
                    rbuggyQSA = [],
                // matchesSelector(:active) reports false when true (IE9/Opera 11.5)
                // A support test would require too much code (would include document ready)
                // just skip matchesSelector for :active
                    rbuggyMatches = [":active"],
                    matches = docElem.matchesSelector ||
                        docElem.mozMatchesSelector ||
                        docElem.webkitMatchesSelector ||
                        docElem.oMatchesSelector ||
                        docElem.msMatchesSelector;

                // Build QSA regex
                // Regex strategy adopted from Diego Perini
                assert(function( div ) {
                    // Select is set to empty string on purpose
                    // This is to test IE's treatment of not explictly
                    // setting a boolean content attribute,
                    // since its presence should be enough
                    // http://bugs.jquery.com/ticket/12359
                    div.innerHTML = "<select><option selected=''></option></select>";

                    // IE8 - Some boolean attributes are not treated correctly
                    if ( !div.querySelectorAll("[selected]").length ) {
                        rbuggyQSA.push( "\\[" + whitespace + "*(?:checked|disabled|ismap|multiple|readonly|selected|value)" );
                    }

                    // Webkit/Opera - :checked should return selected option elements
                    // http://www.w3.org/TR/2011/REC-css3-selectors-20110929/#checked
                    // IE8 throws error here (do not put tests after this one)
                    if ( !div.querySelectorAll(":checked").length ) {
                        rbuggyQSA.push(":checked");
                    }
                });

                assert(function( div ) {

                    // Opera 10-12/IE9 - ^= $= *= and empty values
                    // Should not select anything
                    div.innerHTML = "<p test=''></p>";
                    if ( div.querySelectorAll("[test^='']").length ) {
                        rbuggyQSA.push( "[*^$]=" + whitespace + "*(?:\"\"|'')" );
                    }

                    // FF 3.5 - :enabled/:disabled and hidden elements (hidden elements are still enabled)
                    // IE8 throws error here (do not put tests after this one)
                    div.innerHTML = "<input type='hidden'/>";
                    if ( !div.querySelectorAll(":enabled").length ) {
                        rbuggyQSA.push(":enabled", ":disabled");
                    }
                });

                rbuggyQSA = rbuggyQSA.length && new RegExp( rbuggyQSA.join("|") );

                select = function( selector, context, results, seed, xml ) {
                    // Only use querySelectorAll when not filtering,
                    // when this is not xml,
                    // and when no QSA bugs apply
                    if ( !seed && !xml && (!rbuggyQSA || !rbuggyQSA.test( selector )) ) {
                        if ( context.nodeType === 9 ) {
                            try {
                                push.apply( results, slice.call(context.querySelectorAll( selector ), 0) );
                                return results;
                            } catch(qsaError) {}
                            // qSA works strangely on Element-rooted queries
                            // We can work around this by specifying an extra ID on the root
                            // and working up from there (Thanks to Andrew Dupont for the technique)
                            // IE 8 doesn't work on object elements
                        } else if ( context.nodeType === 1 && context.nodeName.toLowerCase() !== "object" ) {
                            var groups, i, len,
                                old = context.getAttribute("id"),
                                nid = old || expando,
                                newContext = rsibling.test( selector ) && context.parentNode || context;

                            if ( old ) {
                                nid = nid.replace( rescape, "\\$&" );
                            } else {
                                context.setAttribute( "id", nid );
                            }

                            groups = tokenize(selector, context, xml);
                            // Trailing space is unnecessary
                            // There is always a context check
                            nid = "[id='" + nid + "']";
                            for ( i = 0, len = groups.length; i < len; i++ ) {
                                groups[i] = nid + groups[i].selector;
                            }
                            try {
                                push.apply( results, slice.call( newContext.querySelectorAll(
                                    groups.join(",")
                                ), 0 ) );
                                return results;
                            } catch(qsaError) {
                            } finally {
                                if ( !old ) {
                                    context.removeAttribute("id");
                                }
                            }
                        }
                    }

                    return oldSelect( selector, context, results, seed, xml );
                };

                if ( matches ) {
                    assert(function( div ) {
                        // Check to see if it's possible to do matchesSelector
                        // on a disconnected node (IE 9)
                        disconnectedMatch = matches.call( div, "div" );

                        // This should fail with an exception
                        // Gecko does not error, returns false instead
                        try {
                            matches.call( div, "[test!='']:sizzle" );
                            rbuggyMatches.push( matchExpr["PSEUDO"].source, matchExpr["POS"].source, "!=" );
                        } catch ( e ) {}
                    });

                    // rbuggyMatches always contains :active, so no need for a length check
                    rbuggyMatches = /* rbuggyMatches.length && */ new RegExp( rbuggyMatches.join("|") );

                    Sizzle.matchesSelector = function( elem, expr ) {
                        // Make sure that attribute selectors are quoted
                        expr = expr.replace( rattributeQuotes, "='$1']" );

                        // rbuggyMatches always contains :active, so no need for an existence check
                        if ( !isXML( elem ) && !rbuggyMatches.test( expr ) && (!rbuggyQSA || !rbuggyQSA.test( expr )) ) {
                            try {
                                var ret = matches.call( elem, expr );

                                // IE 9's matchesSelector returns false on disconnected nodes
                                if ( ret || disconnectedMatch ||
                                    // As well, disconnected nodes are said to be in a document
                                    // fragment in IE 9
                                    elem.document && elem.document.nodeType !== 11 ) {
                                    return ret;
                                }
                            } catch(e) {}
                        }

                        return Sizzle( expr, null, null, [ elem ] ).length > 0;
                    };
                }
            })();
        }

// Deprecated
        Expr.setFilters["nth"] = Expr.setFilters["eq"];

// Back-compat
        Expr.filters = Expr.pseudos;

// Override sizzle attribute retrieval
        Sizzle.attr = jQuery.attr;
        jQuery.find = Sizzle;
        jQuery.expr = Sizzle.selectors;
        jQuery.expr[":"] = jQuery.expr.pseudos;
        jQuery.unique = Sizzle.uniqueSort;
        jQuery.text = Sizzle.getText;
        jQuery.isXMLDoc = Sizzle.isXML;
        jQuery.contains = Sizzle.contains;


    })( window );
    var runtil = /Until$/,
        rparentsprev = /^(?:parents|prev(?:Until|All))/,
        isSimple = /^.[^:#\[\.,]*$/,
        rneedsContext = jQuery.expr.match.needsContext,
    // methods guaranteed to produce a unique set when starting from a unique set
        guaranteedUnique = {
            children: true,
            contents: true,
            next: true,
            prev: true
        };

    jQuery.fn.extend({
        find: function( selector ) {
            var i, l, length, n, r, ret,
                self = this;

            if ( typeof selector !== "string" ) {
                return jQuery( selector ).filter(function() {
                    for ( i = 0, l = self.length; i < l; i++ ) {
                        if ( jQuery.contains( self[ i ], this ) ) {
                            return true;
                        }
                    }
                });
            }

            ret = this.pushStack( "", "find", selector );

            for ( i = 0, l = this.length; i < l; i++ ) {
                length = ret.length;
                jQuery.find( selector, this[i], ret );

                if ( i > 0 ) {
                    // Make sure that the results are unique
                    for ( n = length; n < ret.length; n++ ) {
                        for ( r = 0; r < length; r++ ) {
                            if ( ret[r] === ret[n] ) {
                                ret.splice(n--, 1);
                                break;
                            }
                        }
                    }
                }
            }

            return ret;
        },

        has: function( target ) {
            var i,
                targets = jQuery( target, this ),
                len = targets.length;

            return this.filter(function() {
                for ( i = 0; i < len; i++ ) {
                    if ( jQuery.contains( this, targets[i] ) ) {
                        return true;
                    }
                }
            });
        },

        not: function( selector ) {
            return this.pushStack( winnow(this, selector, false), "not", selector);
        },

        filter: function( selector ) {
            return this.pushStack( winnow(this, selector, true), "filter", selector );
        },

        is: function( selector ) {
            return !!selector && (
                typeof selector === "string" ?
                    // If this is a positional/relative selector, check membership in the returned set
                    // so $("p:first").is("p:last") won't return true for a doc with two "p".
                    rneedsContext.test( selector ) ?
                        jQuery( selector, this.context ).index( this[0] ) >= 0 :
                        jQuery.filter( selector, this ).length > 0 :
                    this.filter( selector ).length > 0 );
        },

        closest: function( selectors, context ) {
            var cur,
                i = 0,
                l = this.length,
                ret = [],
                pos = rneedsContext.test( selectors ) || typeof selectors !== "string" ?
                    jQuery( selectors, context || this.context ) :
                    0;

            for ( ; i < l; i++ ) {
                cur = this[i];

                while ( cur && cur.ownerDocument && cur !== context && cur.nodeType !== 11 ) {
                    if ( pos ? pos.index(cur) > -1 : jQuery.find.matchesSelector(cur, selectors) ) {
                        ret.push( cur );
                        break;
                    }
                    cur = cur.parentNode;
                }
            }

            ret = ret.length > 1 ? jQuery.unique( ret ) : ret;

            return this.pushStack( ret, "closest", selectors );
        },

        // Determine the position of an element within
        // the matched set of elements
        index: function( elem ) {

            // No argument, return index in parent
            if ( !elem ) {
                return ( this[0] && this[0].parentNode ) ? this.prevAll().length : -1;
            }

            // index in selector
            if ( typeof elem === "string" ) {
                return jQuery.inArray( this[0], jQuery( elem ) );
            }

            // Locate the position of the desired element
            return jQuery.inArray(
                // If it receives a jQuery object, the first element is used
                elem.jquery ? elem[0] : elem, this );
        },

        add: function( selector, context ) {
            var set = typeof selector === "string" ?
                    jQuery( selector, context ) :
                    jQuery.makeArray( selector && selector.nodeType ? [ selector ] : selector ),
                all = jQuery.merge( this.get(), set );

            return this.pushStack( isDisconnected( set[0] ) || isDisconnected( all[0] ) ?
                all :
                jQuery.unique( all ) );
        },

        addBack: function( selector ) {
            return this.add( selector == null ?
                this.prevObject : this.prevObject.filter(selector)
            );
        }
    });

    jQuery.fn.andSelf = jQuery.fn.addBack;

// A painfully simple check to see if an element is disconnected
// from a document (should be improved, where feasible).
    function isDisconnected( node ) {
        return !node || !node.parentNode || node.parentNode.nodeType === 11;
    }

    function sibling( cur, dir ) {
        do {
            cur = cur[ dir ];
        } while ( cur && cur.nodeType !== 1 );

        return cur;
    }

    jQuery.each({
        parent: function( elem ) {
            var parent = elem.parentNode;
            return parent && parent.nodeType !== 11 ? parent : null;
        },
        parents: function( elem ) {
            return jQuery.dir( elem, "parentNode" );
        },
        parentsUntil: function( elem, i, until ) {
            return jQuery.dir( elem, "parentNode", until );
        },
        next: function( elem ) {
            return sibling( elem, "nextSibling" );
        },
        prev: function( elem ) {
            return sibling( elem, "previousSibling" );
        },
        nextAll: function( elem ) {
            return jQuery.dir( elem, "nextSibling" );
        },
        prevAll: function( elem ) {
            return jQuery.dir( elem, "previousSibling" );
        },
        nextUntil: function( elem, i, until ) {
            return jQuery.dir( elem, "nextSibling", until );
        },
        prevUntil: function( elem, i, until ) {
            return jQuery.dir( elem, "previousSibling", until );
        },
        siblings: function( elem ) {
            return jQuery.sibling( ( elem.parentNode || {} ).firstChild, elem );
        },
        children: function( elem ) {
            return jQuery.sibling( elem.firstChild );
        },
        contents: function( elem ) {
            return jQuery.nodeName( elem, "iframe" ) ?
                elem.contentDocument || elem.contentWindow.document :
                jQuery.merge( [], elem.childNodes );
        }
    }, function( name, fn ) {
        jQuery.fn[ name ] = function( until, selector ) {
            var ret = jQuery.map( this, fn, until );

            if ( !runtil.test( name ) ) {
                selector = until;
            }

            if ( selector && typeof selector === "string" ) {
                ret = jQuery.filter( selector, ret );
            }

            ret = this.length > 1 && !guaranteedUnique[ name ] ? jQuery.unique( ret ) : ret;

            if ( this.length > 1 && rparentsprev.test( name ) ) {
                ret = ret.reverse();
            }

            return this.pushStack( ret, name, core_slice.call( arguments ).join(",") );
        };
    });

    jQuery.extend({
        filter: function( expr, elems, not ) {
            if ( not ) {
                expr = ":not(" + expr + ")";
            }

            return elems.length === 1 ?
                jQuery.find.matchesSelector(elems[0], expr) ? [ elems[0] ] : [] :
                jQuery.find.matches(expr, elems);
        },

        dir: function( elem, dir, until ) {
            var matched = [],
                cur = elem[ dir ];

            while ( cur && cur.nodeType !== 9 && (until === undefined || cur.nodeType !== 1 || !jQuery( cur ).is( until )) ) {
                if ( cur.nodeType === 1 ) {
                    matched.push( cur );
                }
                cur = cur[dir];
            }
            return matched;
        },

        sibling: function( n, elem ) {
            var r = [];

            for ( ; n; n = n.nextSibling ) {
                if ( n.nodeType === 1 && n !== elem ) {
                    r.push( n );
                }
            }

            return r;
        }
    });

// Implement the identical functionality for filter and not
    function winnow( elements, qualifier, keep ) {

        // Can't pass null or undefined to indexOf in Firefox 4
        // Set to 0 to skip string check
        qualifier = qualifier || 0;

        if ( jQuery.isFunction( qualifier ) ) {
            return jQuery.grep(elements, function( elem, i ) {
                var retVal = !!qualifier.call( elem, i, elem );
                return retVal === keep;
            });

        } else if ( qualifier.nodeType ) {
            return jQuery.grep(elements, function( elem, i ) {
                return ( elem === qualifier ) === keep;
            });

        } else if ( typeof qualifier === "string" ) {
            var filtered = jQuery.grep(elements, function( elem ) {
                return elem.nodeType === 1;
            });

            if ( isSimple.test( qualifier ) ) {
                return jQuery.filter(qualifier, filtered, !keep);
            } else {
                qualifier = jQuery.filter( qualifier, filtered );
            }
        }

        return jQuery.grep(elements, function( elem, i ) {
            return ( jQuery.inArray( elem, qualifier ) >= 0 ) === keep;
        });
    }
    function createSafeFragment( document ) {
        var list = nodeNames.split( "|" ),
            safeFrag = document.createDocumentFragment();

        if ( safeFrag.createElement ) {
            while ( list.length ) {
                safeFrag.createElement(
                    list.pop()
                );
            }
        }
        return safeFrag;
    }

    var nodeNames = "abbr|article|aside|audio|bdi|canvas|data|datalist|details|figcaption|figure|footer|" +
            "header|hgroup|mark|meter|nav|output|progress|section|summary|time|video",
        rinlinejQuery = / jQuery\d+="(?:null|\d+)"/g,
        rleadingWhitespace = /^\s+/,
        rxhtmlTag = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi,
        rtagName = /<([\w:]+)/,
        rtbody = /<tbody/i,
        rhtml = /<|&#?\w+;/,
        rnoInnerhtml = /<(?:script|style|link)/i,
        rnocache = /<(?:script|object|embed|option|style)/i,
        rnoshimcache = new RegExp("<(?:" + nodeNames + ")[\\s/>]", "i"),
        rcheckableType = /^(?:checkbox|radio)$/,
    // checked="checked" or checked
        rchecked = /checked\s*(?:[^=]|=\s*.checked.)/i,
        rscriptType = /\/(java|ecma)script/i,
        rcleanScript = /^\s*<!(?:\[CDATA\[|\-\-)|[\]\-]{2}>\s*$/g,
        wrapMap = {
            option: [ 1, "<select multiple='multiple'>", "</select>" ],
            legend: [ 1, "<fieldset>", "</fieldset>" ],
            thead: [ 1, "<table>", "</table>" ],
            tr: [ 2, "<table><tbody>", "</tbody></table>" ],
            td: [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ],
            col: [ 2, "<table><tbody></tbody><colgroup>", "</colgroup></table>" ],
            area: [ 1, "<map>", "</map>" ],
            _default: [ 0, "", "" ]
        },
        safeFragment = createSafeFragment( document ),
        fragmentDiv = safeFragment.appendChild( document.createElement("div") );

    wrapMap.optgroup = wrapMap.option;
    wrapMap.tbody = wrapMap.tfoot = wrapMap.colgroup = wrapMap.caption = wrapMap.thead;
    wrapMap.th = wrapMap.td;

// IE6-8 can't serialize link, script, style, or any html5 (NoScope) tags,
// unless wrapped in a div with non-breaking characters in front of it.
    if ( !jQuery.support.htmlSerialize ) {
        wrapMap._default = [ 1, "X<div>", "</div>" ];
    }

    jQuery.fn.extend({
        text: function( value ) {
            return jQuery.access( this, function( value ) {
                return value === undefined ?
                    jQuery.text( this ) :
                    this.empty().append( ( this[0] && this[0].ownerDocument || document ).createTextNode( value ) );
            }, null, value, arguments.length );
        },

        wrapAll: function( html ) {
            if ( jQuery.isFunction( html ) ) {
                return this.each(function(i) {
                    jQuery(this).wrapAll( html.call(this, i) );
                });
            }

            if ( this[0] ) {
                // The elements to wrap the target around
                var wrap = jQuery( html, this[0].ownerDocument ).eq(0).clone(true);

                if ( this[0].parentNode ) {
                    wrap.insertBefore( this[0] );
                }

                wrap.map(function() {
                    var elem = this;

                    while ( elem.firstChild && elem.firstChild.nodeType === 1 ) {
                        elem = elem.firstChild;
                    }

                    return elem;
                }).append( this );
            }

            return this;
        },

        wrapInner: function( html ) {
            if ( jQuery.isFunction( html ) ) {
                return this.each(function(i) {
                    jQuery(this).wrapInner( html.call(this, i) );
                });
            }

            return this.each(function() {
                var self = jQuery( this ),
                    contents = self.contents();

                if ( contents.length ) {
                    contents.wrapAll( html );

                } else {
                    self.append( html );
                }
            });
        },

        wrap: function( html ) {
            var isFunction = jQuery.isFunction( html );

            return this.each(function(i) {
                jQuery( this ).wrapAll( isFunction ? html.call(this, i) : html );
            });
        },

        unwrap: function() {
            return this.parent().each(function() {
                if ( !jQuery.nodeName( this, "body" ) ) {
                    jQuery( this ).replaceWith( this.childNodes );
                }
            }).end();
        },

        append: function() {
            return this.domManip(arguments, true, function( elem ) {
                if ( this.nodeType === 1 || this.nodeType === 11 ) {
                    this.appendChild( elem );
                }
            });
        },

        prepend: function() {
            return this.domManip(arguments, true, function( elem ) {
                if ( this.nodeType === 1 || this.nodeType === 11 ) {
                    this.insertBefore( elem, this.firstChild );
                }
            });
        },

        before: function() {
            if ( !isDisconnected( this[0] ) ) {
                return this.domManip(arguments, false, function( elem ) {
                    this.parentNode.insertBefore( elem, this );
                });
            }

            if ( arguments.length ) {
                var set = jQuery.clean( arguments );
                return this.pushStack( jQuery.merge( set, this ), "before", this.selector );
            }
        },

        after: function() {
            if ( !isDisconnected( this[0] ) ) {
                return this.domManip(arguments, false, function( elem ) {
                    this.parentNode.insertBefore( elem, this.nextSibling );
                });
            }

            if ( arguments.length ) {
                var set = jQuery.clean( arguments );
                return this.pushStack( jQuery.merge( this, set ), "after", this.selector );
            }
        },

        // keepData is for internal use only--do not document
        remove: function( selector, keepData ) {
            var elem,
                i = 0;

            for ( ; (elem = this[i]) != null; i++ ) {
                if ( !selector || jQuery.filter( selector, [ elem ] ).length ) {
                    if ( !keepData && elem.nodeType === 1 ) {
                        jQuery.cleanData( elem.getElementsByTagName("*") );
                        jQuery.cleanData( [ elem ] );
                    }

                    if ( elem.parentNode ) {
                        elem.parentNode.removeChild( elem );
                    }
                }
            }

            return this;
        },

        empty: function() {
            var elem,
                i = 0;

            for ( ; (elem = this[i]) != null; i++ ) {
                // Remove element nodes and prevent memory leaks
                if ( elem.nodeType === 1 ) {
                    jQuery.cleanData( elem.getElementsByTagName("*") );
                }

                // Remove any remaining nodes
                while ( elem.firstChild ) {
                    elem.removeChild( elem.firstChild );
                }
            }

            return this;
        },

        clone: function( dataAndEvents, deepDataAndEvents ) {
            dataAndEvents = dataAndEvents == null ? false : dataAndEvents;
            deepDataAndEvents = deepDataAndEvents == null ? dataAndEvents : deepDataAndEvents;

            return this.map( function () {
                return jQuery.clone( this, dataAndEvents, deepDataAndEvents );
            });
        },

        html: function( value ) {
            return jQuery.access( this, function( value ) {
                var elem = this[0] || {},
                    i = 0,
                    l = this.length;

                if ( value === undefined ) {
                    return elem.nodeType === 1 ?
                        elem.innerHTML.replace( rinlinejQuery, "" ) :
                        undefined;
                }

                // See if we can take a shortcut and just use innerHTML
                if ( typeof value === "string" && !rnoInnerhtml.test( value ) &&
                    ( jQuery.support.htmlSerialize || !rnoshimcache.test( value )  ) &&
                    ( jQuery.support.leadingWhitespace || !rleadingWhitespace.test( value ) ) &&
                    !wrapMap[ ( rtagName.exec( value ) || ["", ""] )[1].toLowerCase() ] ) {

                    value = value.replace( rxhtmlTag, "<$1></$2>" );

                    try {
                        for (; i < l; i++ ) {
                            // Remove element nodes and prevent memory leaks
                            elem = this[i] || {};
                            if ( elem.nodeType === 1 ) {
                                jQuery.cleanData( elem.getElementsByTagName( "*" ) );
                                elem.innerHTML = value;
                            }
                        }

                        elem = 0;

                        // If using innerHTML throws an exception, use the fallback method
                    } catch(e) {}
                }

                if ( elem ) {
                    this.empty().append( value );
                }
            }, null, value, arguments.length );
        },

        replaceWith: function( value ) {
            if ( !isDisconnected( this[0] ) ) {
                // Make sure that the elements are removed from the DOM before they are inserted
                // this can help fix replacing a parent with child elements
                if ( jQuery.isFunction( value ) ) {
                    return this.each(function(i) {
                        var self = jQuery(this), old = self.html();
                        self.replaceWith( value.call( this, i, old ) );
                    });
                }

                if ( typeof value !== "string" ) {
                    value = jQuery( value ).detach();
                }

                return this.each(function() {
                    var next = this.nextSibling,
                        parent = this.parentNode;

                    jQuery( this ).remove();

                    if ( next ) {
                        jQuery(next).before( value );
                    } else {
                        jQuery(parent).append( value );
                    }
                });
            }

            return this.length ?
                this.pushStack( jQuery(jQuery.isFunction(value) ? value() : value), "replaceWith", value ) :
                this;
        },

        detach: function( selector ) {
            return this.remove( selector, true );
        },

        domManip: function( args, table, callback ) {

            // Flatten any nested arrays
            args = [].concat.apply( [], args );

            var results, first, fragment, iNoClone,
                i = 0,
                value = args[0],
                scripts = [],
                l = this.length;

            // We can't cloneNode fragments that contain checked, in WebKit
            if ( !jQuery.support.checkClone && l > 1 && typeof value === "string" && rchecked.test( value ) ) {
                return this.each(function() {
                    jQuery(this).domManip( args, table, callback );
                });
            }

            if ( jQuery.isFunction(value) ) {
                return this.each(function(i) {
                    var self = jQuery(this);
                    args[0] = value.call( this, i, table ? self.html() : undefined );
                    self.domManip( args, table, callback );
                });
            }

            if ( this[0] ) {
                results = jQuery.buildFragment( args, this, scripts );
                fragment = results.fragment;
                first = fragment.firstChild;

                if ( fragment.childNodes.length === 1 ) {
                    fragment = first;
                }

                if ( first ) {
                    table = table && jQuery.nodeName( first, "tr" );

                    // Use the original fragment for the last item instead of the first because it can end up
                    // being emptied incorrectly in certain situations (#8070).
                    // Fragments from the fragment cache must always be cloned and never used in place.
                    for ( iNoClone = results.cacheable || l - 1; i < l; i++ ) {
                        callback.call(
                            table && jQuery.nodeName( this[i], "table" ) ?
                                findOrAppend( this[i], "tbody" ) :
                                this[i],
                            i === iNoClone ?
                                fragment :
                                jQuery.clone( fragment, true, true )
                        );
                    }
                }

                // Fix #11809: Avoid leaking memory
                fragment = first = null;

                if ( scripts.length ) {
                    jQuery.each( scripts, function( i, elem ) {
                        if ( elem.src ) {
                            if ( jQuery.ajax ) {
                                jQuery.ajax({
                                    url: elem.src,
                                    type: "GET",
                                    dataType: "script",
                                    async: false,
                                    global: false,
                                    "throws": true
                                });
                            } else {
                                jQuery.error("no ajax");
                            }
                        } else {
                            jQuery.globalEval( ( elem.text || elem.textContent || elem.innerHTML || "" ).replace( rcleanScript, "" ) );
                        }

                        if ( elem.parentNode ) {
                            elem.parentNode.removeChild( elem );
                        }
                    });
                }
            }

            return this;
        }
    });

    function findOrAppend( elem, tag ) {
        return elem.getElementsByTagName( tag )[0] || elem.appendChild( elem.ownerDocument.createElement( tag ) );
    }

    function cloneCopyEvent( src, dest ) {

        if ( dest.nodeType !== 1 || !jQuery.hasData( src ) ) {
            return;
        }

        var type, i, l,
            oldData = jQuery._data( src ),
            curData = jQuery._data( dest, oldData ),
            events = oldData.events;

        if ( events ) {
            delete curData.handle;
            curData.events = {};

            for ( type in events ) {
                for ( i = 0, l = events[ type ].length; i < l; i++ ) {
                    jQuery.event.add( dest, type, events[ type ][ i ] );
                }
            }
        }

        // make the cloned public data object a copy from the original
        if ( curData.data ) {
            curData.data = jQuery.extend( {}, curData.data );
        }
    }

    function cloneFixAttributes( src, dest ) {
        var nodeName;

        // We do not need to do anything for non-Elements
        if ( dest.nodeType !== 1 ) {
            return;
        }

        // clearAttributes removes the attributes, which we don't want,
        // but also removes the attachEvent events, which we *do* want
        if ( dest.clearAttributes ) {
            dest.clearAttributes();
        }

        // mergeAttributes, in contrast, only merges back on the
        // original attributes, not the events
        if ( dest.mergeAttributes ) {
            dest.mergeAttributes( src );
        }

        nodeName = dest.nodeName.toLowerCase();

        if ( nodeName === "object" ) {
            // IE6-10 improperly clones children of object elements using classid.
            // IE10 throws NoModificationAllowedError if parent is null, #12132.
            if ( dest.parentNode ) {
                dest.outerHTML = src.outerHTML;
            }

            // This path appears unavoidable for IE9. When cloning an object
            // element in IE9, the outerHTML strategy above is not sufficient.
            // If the src has innerHTML and the destination does not,
            // copy the src.innerHTML into the dest.innerHTML. #10324
            if ( jQuery.support.html5Clone && (src.innerHTML && !jQuery.trim(dest.innerHTML)) ) {
                dest.innerHTML = src.innerHTML;
            }

        } else if ( nodeName === "input" && rcheckableType.test( src.type ) ) {
            // IE6-8 fails to persist the checked state of a cloned checkbox
            // or radio button. Worse, IE6-7 fail to give the cloned element
            // a checked appearance if the defaultChecked value isn't also set

            dest.defaultChecked = dest.checked = src.checked;

            // IE6-7 get confused and end up setting the value of a cloned
            // checkbox/radio button to an empty string instead of "on"
            if ( dest.value !== src.value ) {
                dest.value = src.value;
            }

            // IE6-8 fails to return the selected option to the default selected
            // state when cloning options
        } else if ( nodeName === "option" ) {
            dest.selected = src.defaultSelected;

            // IE6-8 fails to set the defaultValue to the correct value when
            // cloning other types of input fields
        } else if ( nodeName === "input" || nodeName === "textarea" ) {
            dest.defaultValue = src.defaultValue;

            // IE blanks contents when cloning scripts
        } else if ( nodeName === "script" && dest.text !== src.text ) {
            dest.text = src.text;
        }

        // Event data gets referenced instead of copied if the expando
        // gets copied too
        dest.removeAttribute( jQuery.expando );
    }

    jQuery.buildFragment = function( args, context, scripts ) {
        var fragment, cacheable, cachehit,
            first = args[ 0 ];

        // Set context from what may come in as undefined or a jQuery collection or a node
        // Updated to fix #12266 where accessing context[0] could throw an exception in IE9/10 &
        // also doubles as fix for #8950 where plain objects caused createDocumentFragment exception
        context = context || document;
        context = !context.nodeType && context[0] || context;
        context = context.ownerDocument || context;

        // Only cache "small" (1/2 KB) HTML strings that are associated with the main document
        // Cloning options loses the selected state, so don't cache them
        // IE 6 doesn't like it when you put <object> or <embed> elements in a fragment
        // Also, WebKit does not clone 'checked' attributes on cloneNode, so don't cache
        // Lastly, IE6,7,8 will not correctly reuse cached fragments that were created from unknown elems #10501
        if ( args.length === 1 && typeof first === "string" && first.length < 512 && context === document &&
            first.charAt(0) === "<" && !rnocache.test( first ) &&
            (jQuery.support.checkClone || !rchecked.test( first )) &&
            (jQuery.support.html5Clone || !rnoshimcache.test( first )) ) {

            // Mark cacheable and look for a hit
            cacheable = true;
            fragment = jQuery.fragments[ first ];
            cachehit = fragment !== undefined;
        }

        if ( !fragment ) {
            fragment = context.createDocumentFragment();
            jQuery.clean( args, context, fragment, scripts );

            // Update the cache, but only store false
            // unless this is a second parsing of the same content
            if ( cacheable ) {
                jQuery.fragments[ first ] = cachehit && fragment;
            }
        }

        return { fragment: fragment, cacheable: cacheable };
    };

    jQuery.fragments = {};

    jQuery.each({
        appendTo: "append",
        prependTo: "prepend",
        insertBefore: "before",
        insertAfter: "after",
        replaceAll: "replaceWith"
    }, function( name, original ) {
        jQuery.fn[ name ] = function( selector ) {
            var elems,
                i = 0,
                ret = [],
                insert = jQuery( selector ),
                l = insert.length,
                parent = this.length === 1 && this[0].parentNode;

            if ( (parent == null || parent && parent.nodeType === 11 && parent.childNodes.length === 1) && l === 1 ) {
                insert[ original ]( this[0] );
                return this;
            } else {
                for ( ; i < l; i++ ) {
                    elems = ( i > 0 ? this.clone(true) : this ).get();
                    jQuery( insert[i] )[ original ]( elems );
                    ret = ret.concat( elems );
                }

                return this.pushStack( ret, name, insert.selector );
            }
        };
    });

    function getAll( elem ) {
        if ( typeof elem.getElementsByTagName !== "undefined" ) {
            return elem.getElementsByTagName( "*" );

        } else if ( typeof elem.querySelectorAll !== "undefined" ) {
            return elem.querySelectorAll( "*" );

        } else {
            return [];
        }
    }

// Used in clean, fixes the defaultChecked property
    function fixDefaultChecked( elem ) {
        if ( rcheckableType.test( elem.type ) ) {
            elem.defaultChecked = elem.checked;
        }
    }

    jQuery.extend({
        clone: function( elem, dataAndEvents, deepDataAndEvents ) {
            var srcElements,
                destElements,
                i,
                clone;

            if ( jQuery.support.html5Clone || jQuery.isXMLDoc(elem) || !rnoshimcache.test( "<" + elem.nodeName + ">" ) ) {
                clone = elem.cloneNode( true );

                // IE<=8 does not properly clone detached, unknown element nodes
            } else {
                fragmentDiv.innerHTML = elem.outerHTML;
                fragmentDiv.removeChild( clone = fragmentDiv.firstChild );
            }

            if ( (!jQuery.support.noCloneEvent || !jQuery.support.noCloneChecked) &&
                (elem.nodeType === 1 || elem.nodeType === 11) && !jQuery.isXMLDoc(elem) ) {
                // IE copies events bound via attachEvent when using cloneNode.
                // Calling detachEvent on the clone will also remove the events
                // from the original. In order to get around this, we use some
                // proprietary methods to clear the events. Thanks to MooTools
                // guys for this hotness.

                cloneFixAttributes( elem, clone );

                // Using Sizzle here is crazy slow, so we use getElementsByTagName instead
                srcElements = getAll( elem );
                destElements = getAll( clone );

                // Weird iteration because IE will replace the length property
                // with an element if you are cloning the body and one of the
                // elements on the page has a name or id of "length"
                for ( i = 0; srcElements[i]; ++i ) {
                    // Ensure that the destination node is not null; Fixes #9587
                    if ( destElements[i] ) {
                        cloneFixAttributes( srcElements[i], destElements[i] );
                    }
                }
            }

            // Copy the events from the original to the clone
            if ( dataAndEvents ) {
                cloneCopyEvent( elem, clone );

                if ( deepDataAndEvents ) {
                    srcElements = getAll( elem );
                    destElements = getAll( clone );

                    for ( i = 0; srcElements[i]; ++i ) {
                        cloneCopyEvent( srcElements[i], destElements[i] );
                    }
                }
            }

            srcElements = destElements = null;

            // Return the cloned set
            return clone;
        },

        clean: function( elems, context, fragment, scripts ) {
            var i, j, elem, tag, wrap, depth, div, hasBody, tbody, len, handleScript, jsTags,
                safe = context === document && safeFragment,
                ret = [];

            // Ensure that context is a document
            if ( !context || typeof context.createDocumentFragment === "undefined" ) {
                context = document;
            }

            // Use the already-created safe fragment if context permits
            for ( i = 0; (elem = elems[i]) != null; i++ ) {
                if ( typeof elem === "number" ) {
                    elem += "";
                }

                if ( !elem ) {
                    continue;
                }

                // Convert html string into DOM nodes
                if ( typeof elem === "string" ) {
                    if ( !rhtml.test( elem ) ) {
                        elem = context.createTextNode( elem );
                    } else {
                        // Ensure a safe container in which to render the html
                        safe = safe || createSafeFragment( context );
                        div = context.createElement("div");
                        safe.appendChild( div );

                        // Fix "XHTML"-style tags in all browsers
                        elem = elem.replace(rxhtmlTag, "<$1></$2>");

                        // Go to html and back, then peel off extra wrappers
                        tag = ( rtagName.exec( elem ) || ["", ""] )[1].toLowerCase();
                        wrap = wrapMap[ tag ] || wrapMap._default;
                        depth = wrap[0];
                        div.innerHTML = wrap[1] + elem + wrap[2];

                        // Move to the right depth
                        while ( depth-- ) {
                            div = div.lastChild;
                        }

                        // Remove IE's autoinserted <tbody> from table fragments
                        if ( !jQuery.support.tbody ) {

                            // String was a <table>, *may* have spurious <tbody>
                            hasBody = rtbody.test(elem);
                            tbody = tag === "table" && !hasBody ?
                                div.firstChild && div.firstChild.childNodes :

                                // String was a bare <thead> or <tfoot>
                                wrap[1] === "<table>" && !hasBody ?
                                    div.childNodes :
                                    [];

                            for ( j = tbody.length - 1; j >= 0 ; --j ) {
                                if ( jQuery.nodeName( tbody[ j ], "tbody" ) && !tbody[ j ].childNodes.length ) {
                                    tbody[ j ].parentNode.removeChild( tbody[ j ] );
                                }
                            }
                        }

                        // IE completely kills leading whitespace when innerHTML is used
                        if ( !jQuery.support.leadingWhitespace && rleadingWhitespace.test( elem ) ) {
                            div.insertBefore( context.createTextNode( rleadingWhitespace.exec(elem)[0] ), div.firstChild );
                        }

                        elem = div.childNodes;

                        // Take out of fragment container (we need a fresh div each time)
                        div.parentNode.removeChild( div );
                    }
                }

                if ( elem.nodeType ) {
                    ret.push( elem );
                } else {
                    jQuery.merge( ret, elem );
                }
            }

            // Fix #11356: Clear elements from safeFragment
            if ( div ) {
                elem = div = safe = null;
            }

            // Reset defaultChecked for any radios and checkboxes
            // about to be appended to the DOM in IE 6/7 (#8060)
            if ( !jQuery.support.appendChecked ) {
                for ( i = 0; (elem = ret[i]) != null; i++ ) {
                    if ( jQuery.nodeName( elem, "input" ) ) {
                        fixDefaultChecked( elem );
                    } else if ( typeof elem.getElementsByTagName !== "undefined" ) {
                        jQuery.grep( elem.getElementsByTagName("input"), fixDefaultChecked );
                    }
                }
            }

            // Append elements to a provided document fragment
            if ( fragment ) {
                // Special handling of each script element
                handleScript = function( elem ) {
                    // Check if we consider it executable
                    if ( !elem.type || rscriptType.test( elem.type ) ) {
                        // Detach the script and store it in the scripts array (if provided) or the fragment
                        // Return truthy to indicate that it has been handled
                        return scripts ?
                            scripts.push( elem.parentNode ? elem.parentNode.removeChild( elem ) : elem ) :
                            fragment.appendChild( elem );
                    }
                };

                for ( i = 0; (elem = ret[i]) != null; i++ ) {
                    // Check if we're done after handling an executable script
                    if ( !( jQuery.nodeName( elem, "script" ) && handleScript( elem ) ) ) {
                        // Append to fragment and handle embedded scripts
                        fragment.appendChild( elem );
                        if ( typeof elem.getElementsByTagName !== "undefined" ) {
                            // handleScript alters the DOM, so use jQuery.merge to ensure snapshot iteration
                            jsTags = jQuery.grep( jQuery.merge( [], elem.getElementsByTagName("script") ), handleScript );

                            // Splice the scripts into ret after their former ancestor and advance our index beyond them
                            ret.splice.apply( ret, [i + 1, 0].concat( jsTags ) );
                            i += jsTags.length;
                        }
                    }
                }
            }

            return ret;
        },

        cleanData: function( elems, /* internal */ acceptData ) {
            var data, id, elem, type,
                i = 0,
                internalKey = jQuery.expando,
                cache = jQuery.cache,
                deleteExpando = jQuery.support.deleteExpando,
                special = jQuery.event.special;

            for ( ; (elem = elems[i]) != null; i++ ) {

                if ( acceptData || jQuery.acceptData( elem ) ) {

                    id = elem[ internalKey ];
                    data = id && cache[ id ];

                    if ( data ) {
                        if ( data.events ) {
                            for ( type in data.events ) {
                                if ( special[ type ] ) {
                                    jQuery.event.remove( elem, type );

                                    // This is a shortcut to avoid jQuery.event.remove's overhead
                                } else {
                                    jQuery.removeEvent( elem, type, data.handle );
                                }
                            }
                        }

                        // Remove cache only if it was not already removed by jQuery.event.remove
                        if ( cache[ id ] ) {

                            delete cache[ id ];

                            // IE does not allow us to delete expando properties from nodes,
                            // nor does it have a removeAttribute function on Document nodes;
                            // we must handle all of these cases
                            if ( deleteExpando ) {
                                delete elem[ internalKey ];

                            } else if ( elem.removeAttribute ) {
                                elem.removeAttribute( internalKey );

                            } else {
                                elem[ internalKey ] = null;
                            }

                            jQuery.deletedIds.push( id );
                        }
                    }
                }
            }
        }
    });
// Limit scope pollution from any deprecated API
    (function() {

        var matched, browser;

// Use of jQuery.browser is frowned upon.
// More details: http://api.jquery.com/jQuery.browser
// jQuery.uaMatch maintained for back-compat
        jQuery.uaMatch = function( ua ) {
            ua = ua.toLowerCase();

            var match = /(chrome)[ \/]([\w.]+)/.exec( ua ) ||
                /(webkit)[ \/]([\w.]+)/.exec( ua ) ||
                /(opera)(?:.*version|)[ \/]([\w.]+)/.exec( ua ) ||
                /(msie) ([\w.]+)/.exec( ua ) ||
                ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec( ua ) ||
                [];

            return {
                browser: match[ 1 ] || "",
                version: match[ 2 ] || "0"
            };
        };

        matched = jQuery.uaMatch( navigator.userAgent );
        browser = {};

        if ( matched.browser ) {
            browser[ matched.browser ] = true;
            browser.version = matched.version;
        }

// Chrome is Webkit, but Webkit is also Safari.
        if ( browser.chrome ) {
            browser.webkit = true;
        } else if ( browser.webkit ) {
            browser.safari = true;
        }

        jQuery.browser = browser;

        jQuery.sub = function() {
            function jQuerySub( selector, context ) {
                return new jQuerySub.fn.init( selector, context );
            }
            jQuery.extend( true, jQuerySub, this );
            jQuerySub.superclass = this;
            jQuerySub.fn = jQuerySub.prototype = this();
            jQuerySub.fn.constructor = jQuerySub;
            jQuerySub.sub = this.sub;
            jQuerySub.fn.init = function init( selector, context ) {
                if ( context && context instanceof jQuery && !(context instanceof jQuerySub) ) {
                    context = jQuerySub( context );
                }

                return jQuery.fn.init.call( this, selector, context, rootjQuerySub );
            };
            jQuerySub.fn.init.prototype = jQuerySub.fn;
            var rootjQuerySub = jQuerySub(document);
            return jQuerySub;
        };

    })();
    var curCSS, iframe, iframeDoc,
        ralpha = /alpha\([^)]*\)/i,
        ropacity = /opacity=([^)]*)/,
        rposition = /^(top|right|bottom|left)$/,
    // swappable if display is none or starts with table except "table", "table-cell", or "table-caption"
    // see here for display values: https://developer.mozilla.org/en-US/docs/CSS/display
        rdisplayswap = /^(none|table(?!-c[ea]).+)/,
        rmargin = /^margin/,
        rnumsplit = new RegExp( "^(" + core_pnum + ")(.*)$", "i" ),
        rnumnonpx = new RegExp( "^(" + core_pnum + ")(?!px)[a-z%]+$", "i" ),
        rrelNum = new RegExp( "^([-+])=(" + core_pnum + ")", "i" ),
        elemdisplay = {},

        cssShow = { position: "absolute", visibility: "hidden", display: "block" },
        cssNormalTransform = {
            letterSpacing: 0,
            fontWeight: 400
        },

        cssExpand = [ "Top", "Right", "Bottom", "Left" ],
        cssPrefixes = [ "Webkit", "O", "Moz", "ms" ],

        eventsToggle = jQuery.fn.toggle;

// return a css property mapped to a potentially vendor prefixed property
    function vendorPropName( style, name ) {

        // shortcut for names that are not vendor prefixed
        if ( name in style ) {
            return name;
        }

        // check for vendor prefixed names
        var capName = name.charAt(0).toUpperCase() + name.slice(1),
            origName = name,
            i = cssPrefixes.length;

        while ( i-- ) {
            name = cssPrefixes[ i ] + capName;
            if ( name in style ) {
                return name;
            }
        }

        return origName;
    }

    function isHidden( elem, el ) {
        elem = el || elem;
        return jQuery.css( elem, "display" ) === "none" || !jQuery.contains( elem.ownerDocument, elem );
    }

    function showHide( elements, show ) {
        var elem, display,
            values = [],
            index = 0,
            length = elements.length;

        for ( ; index < length; index++ ) {
            elem = elements[ index ];
            if ( !elem.style ) {
                continue;
            }
            values[ index ] = jQuery._data( elem, "olddisplay" );
            if ( show ) {
                // Reset the inline display of this element to learn if it is
                // being hidden by cascaded rules or not
                if ( !values[ index ] && elem.style.display === "none" ) {
                    elem.style.display = "";
                }

                // Set elements which have been overridden with display: none
                // in a stylesheet to whatever the default browser style is
                // for such an element
                if ( elem.style.display === "" && isHidden( elem ) ) {
                    values[ index ] = jQuery._data( elem, "olddisplay", css_defaultDisplay(elem.nodeName) );
                }
            } else {
                display = curCSS( elem, "display" );

                if ( !values[ index ] && display !== "none" ) {
                    jQuery._data( elem, "olddisplay", display );
                }
            }
        }

        // Set the display of most of the elements in a second loop
        // to avoid the constant reflow
        for ( index = 0; index < length; index++ ) {
            elem = elements[ index ];
            if ( !elem.style ) {
                continue;
            }
            if ( !show || elem.style.display === "none" || elem.style.display === "" ) {
                elem.style.display = show ? values[ index ] || "" : "none";
            }
        }

        return elements;
    }

    jQuery.fn.extend({
        css: function( name, value ) {
            return jQuery.access( this, function( elem, name, value ) {
                return value !== undefined ?
                    jQuery.style( elem, name, value ) :
                    jQuery.css( elem, name );
            }, name, value, arguments.length > 1 );
        },
        show: function() {
            return showHide( this, true );
        },
        hide: function() {
            return showHide( this );
        },
        toggle: function( state, fn2 ) {
            var bool = typeof state === "boolean";

            if ( jQuery.isFunction( state ) && jQuery.isFunction( fn2 ) ) {
                return eventsToggle.apply( this, arguments );
            }

            return this.each(function() {
                if ( bool ? state : isHidden( this ) ) {
                    jQuery( this ).show();
                } else {
                    jQuery( this ).hide();
                }
            });
        }
    });

    jQuery.extend({
        // Add in style property hooks for overriding the default
        // behavior of getting and setting a style property
        cssHooks: {
            opacity: {
                get: function( elem, computed ) {
                    if ( computed ) {
                        // We should always get a number back from opacity
                        var ret = curCSS( elem, "opacity" );
                        return ret === "" ? "1" : ret;

                    }
                }
            }
        },

        // Exclude the following css properties to add px
        cssNumber: {
            "fillOpacity": true,
            "fontWeight": true,
            "lineHeight": true,
            "opacity": true,
            "orphans": true,
            "widows": true,
            "zIndex": true,
            "zoom": true
        },

        // Add in properties whose names you wish to fix before
        // setting or getting the value
        cssProps: {
            // normalize float css property
            "float": jQuery.support.cssFloat ? "cssFloat" : "styleFloat"
        },

        // Get and set the style property on a DOM Node
        style: function( elem, name, value, extra ) {
            // Don't set styles on text and comment nodes
            if ( !elem || elem.nodeType === 3 || elem.nodeType === 8 || !elem.style ) {
                return;
            }

            // Make sure that we're working with the right name
            var ret, type, hooks,
                origName = jQuery.camelCase( name ),
                style = elem.style;

            name = jQuery.cssProps[ origName ] || ( jQuery.cssProps[ origName ] = vendorPropName( style, origName ) );

            // gets hook for the prefixed version
            // followed by the unprefixed version
            hooks = jQuery.cssHooks[ name ] || jQuery.cssHooks[ origName ];

            // Check if we're setting a value
            if ( value !== undefined ) {
                type = typeof value;

                // convert relative number strings (+= or -=) to relative numbers. #7345
                if ( type === "string" && (ret = rrelNum.exec( value )) ) {
                    value = ( ret[1] + 1 ) * ret[2] + parseFloat( jQuery.css( elem, name ) );
                    // Fixes bug #9237
                    type = "number";
                }

                // Make sure that NaN and null values aren't set. See: #7116
                if ( value == null || type === "number" && isNaN( value ) ) {
                    return;
                }

                // If a number was passed in, add 'px' to the (except for certain CSS properties)
                if ( type === "number" && !jQuery.cssNumber[ origName ] ) {
                    value += "px";
                }

                // If a hook was provided, use that value, otherwise just set the specified value
                if ( !hooks || !("set" in hooks) || (value = hooks.set( elem, value, extra )) !== undefined ) {
                    // Wrapped to prevent IE from throwing errors when 'invalid' values are provided
                    // Fixes bug #5509
                    try {
                        style[ name ] = value;
                    } catch(e) {}
                }

            } else {
                // If a hook was provided get the non-computed value from there
                if ( hooks && "get" in hooks && (ret = hooks.get( elem, false, extra )) !== undefined ) {
                    return ret;
                }

                // Otherwise just get the value from the style object
                return style[ name ];
            }
        },

        css: function( elem, name, numeric, extra ) {
            var val, num, hooks,
                origName = jQuery.camelCase( name );

            // Make sure that we're working with the right name
            name = jQuery.cssProps[ origName ] || ( jQuery.cssProps[ origName ] = vendorPropName( elem.style, origName ) );

            // gets hook for the prefixed version
            // followed by the unprefixed version
            hooks = jQuery.cssHooks[ name ] || jQuery.cssHooks[ origName ];

            // If a hook was provided get the computed value from there
            if ( hooks && "get" in hooks ) {
                val = hooks.get( elem, true, extra );
            }

            // Otherwise, if a way to get the computed value exists, use that
            if ( val === undefined ) {
                val = curCSS( elem, name );
            }

            //convert "normal" to computed value
            if ( val === "normal" && name in cssNormalTransform ) {
                val = cssNormalTransform[ name ];
            }

            // Return, converting to number if forced or a qualifier was provided and val looks numeric
            if ( numeric || extra !== undefined ) {
                num = parseFloat( val );
                return numeric || jQuery.isNumeric( num ) ? num || 0 : val;
            }
            return val;
        },

        // A method for quickly swapping in/out CSS properties to get correct calculations
        swap: function( elem, options, callback ) {
            var ret, name,
                old = {};

            // Remember the old values, and insert the new ones
            for ( name in options ) {
                old[ name ] = elem.style[ name ];
                elem.style[ name ] = options[ name ];
            }

            ret = callback.call( elem );

            // Revert the old values
            for ( name in options ) {
                elem.style[ name ] = old[ name ];
            }

            return ret;
        }
    });

// NOTE: To any future maintainer, we've window.getComputedStyle
// because jsdom on node.js will break without it.
    if ( window.getComputedStyle ) {
        curCSS = function( elem, name ) {
            var ret, width, minWidth, maxWidth,
                computed = window.getComputedStyle( elem, null ),
                style = elem.style;

            if ( computed ) {

                ret = computed[ name ];
                if ( ret === "" && !jQuery.contains( elem.ownerDocument, elem ) ) {
                    ret = jQuery.style( elem, name );
                }

                // A tribute to the "awesome hack by Dean Edwards"
                // Chrome < 17 and Safari 5.0 uses "computed value" instead of "used value" for margin-right
                // Safari 5.1.7 (at least) returns percentage for a larger set of values, but width seems to be reliably pixels
                // this is against the CSSOM draft spec: http://dev.w3.org/csswg/cssom/#resolved-values
                if ( rnumnonpx.test( ret ) && rmargin.test( name ) ) {
                    width = style.width;
                    minWidth = style.minWidth;
                    maxWidth = style.maxWidth;

                    style.minWidth = style.maxWidth = style.width = ret;
                    ret = computed.width;

                    style.width = width;
                    style.minWidth = minWidth;
                    style.maxWidth = maxWidth;
                }
            }

            return ret;
        };
    } else if ( document.documentElement.currentStyle ) {
        curCSS = function( elem, name ) {
            var left, rsLeft,
                ret = elem.currentStyle && elem.currentStyle[ name ],
                style = elem.style;

            // Avoid setting ret to empty string here
            // so we don't default to auto
            if ( ret == null && style && style[ name ] ) {
                ret = style[ name ];
            }

            // From the awesome hack by Dean Edwards
            // http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291

            // If we're not dealing with a regular pixel number
            // but a number that has a weird ending, we need to convert it to pixels
            // but not position css attributes, as those are proportional to the parent element instead
            // and we can't measure the parent instead because it might trigger a "stacking dolls" problem
            if ( rnumnonpx.test( ret ) && !rposition.test( name ) ) {

                // Remember the original values
                left = style.left;
                rsLeft = elem.runtimeStyle && elem.runtimeStyle.left;

                // Put in the new values to get a computed value out
                if ( rsLeft ) {
                    elem.runtimeStyle.left = elem.currentStyle.left;
                }
                style.left = name === "fontSize" ? "1em" : ret;
                ret = style.pixelLeft + "px";

                // Revert the changed values
                style.left = left;
                if ( rsLeft ) {
                    elem.runtimeStyle.left = rsLeft;
                }
            }

            return ret === "" ? "auto" : ret;
        };
    }

    function setPositiveNumber( elem, value, subtract ) {
        var matches = rnumsplit.exec( value );
        return matches ?
            Math.max( 0, matches[ 1 ] - ( subtract || 0 ) ) + ( matches[ 2 ] || "px" ) :
            value;
    }

    function augmentWidthOrHeight( elem, name, extra, isBorderBox ) {
        var i = extra === ( isBorderBox ? "border" : "content" ) ?
                // If we already have the right measurement, avoid augmentation
                4 :
                // Otherwise initialize for horizontal or vertical properties
                name === "width" ? 1 : 0,

            val = 0;

        for ( ; i < 4; i += 2 ) {
            // both box models exclude margin, so add it if we want it
            if ( extra === "margin" ) {
                // we use jQuery.css instead of curCSS here
                // because of the reliableMarginRight CSS hook!
                val += jQuery.css( elem, extra + cssExpand[ i ], true );
            }

            // From this point on we use curCSS for maximum performance (relevant in animations)
            if ( isBorderBox ) {
                // border-box includes padding, so remove it if we want content
                if ( extra === "content" ) {
                    val -= parseFloat( curCSS( elem, "padding" + cssExpand[ i ] ) ) || 0;
                }

                // at this point, extra isn't border nor margin, so remove border
                if ( extra !== "margin" ) {
                    val -= parseFloat( curCSS( elem, "border" + cssExpand[ i ] + "Width" ) ) || 0;
                }
            } else {
                // at this point, extra isn't content, so add padding
                val += parseFloat( curCSS( elem, "padding" + cssExpand[ i ] ) ) || 0;

                // at this point, extra isn't content nor padding, so add border
                if ( extra !== "padding" ) {
                    val += parseFloat( curCSS( elem, "border" + cssExpand[ i ] + "Width" ) ) || 0;
                }
            }
        }

        return val;
    }

    function getWidthOrHeight( elem, name, extra ) {

        // Start with offset property, which is equivalent to the border-box value
        var val = name === "width" ? elem.offsetWidth : elem.offsetHeight,
            valueIsBorderBox = true,
            isBorderBox = jQuery.support.boxSizing && jQuery.css( elem, "boxSizing" ) === "border-box";

        // some non-html elements return undefined for offsetWidth, so check for null/undefined
        // svg - https://bugzilla.mozilla.org/show_bug.cgi?id=649285
        // MathML - https://bugzilla.mozilla.org/show_bug.cgi?id=491668
        if ( val <= 0 || val == null ) {
            // Fall back to computed then uncomputed css if necessary
            val = curCSS( elem, name );
            if ( val < 0 || val == null ) {
                val = elem.style[ name ];
            }

            // Computed unit is not pixels. Stop here and return.
            if ( rnumnonpx.test(val) ) {
                return val;
            }

            // we need the check for style in case a browser which returns unreliable values
            // for getComputedStyle silently falls back to the reliable elem.style
            valueIsBorderBox = isBorderBox && ( jQuery.support.boxSizingReliable || val === elem.style[ name ] );

            // Normalize "", auto, and prepare for extra
            val = parseFloat( val ) || 0;
        }

        // use the active box-sizing model to add/subtract irrelevant styles
        return ( val +
            augmentWidthOrHeight(
                elem,
                name,
                extra || ( isBorderBox ? "border" : "content" ),
                valueIsBorderBox
            )
            ) + "px";
    }


// Try to determine the default display value of an element
    function css_defaultDisplay( nodeName ) {
        if ( elemdisplay[ nodeName ] ) {
            return elemdisplay[ nodeName ];
        }

        var elem = jQuery( "<" + nodeName + ">" ).appendTo( document.body ),
            display = elem.css("display");
        elem.remove();

        // If the simple way fails,
        // get element's real default display by attaching it to a temp iframe
        if ( display === "none" || display === "" ) {
            // Use the already-created iframe if possible
            iframe = document.body.appendChild(
                iframe || jQuery.extend( document.createElement("iframe"), {
                    frameBorder: 0,
                    width: 0,
                    height: 0
                })
            );

            // Create a cacheable copy of the iframe document on first call.
            // IE and Opera will allow us to reuse the iframeDoc without re-writing the fake HTML
            // document to it; WebKit & Firefox won't allow reusing the iframe document.
            if ( !iframeDoc || !iframe.createElement ) {
                iframeDoc = ( iframe.contentWindow || iframe.contentDocument ).document;
                iframeDoc.write("<!doctype html><html><body>");
                iframeDoc.close();
            }

            elem = iframeDoc.body.appendChild( iframeDoc.createElement(nodeName) );

            display = curCSS( elem, "display" );
            document.body.removeChild( iframe );
        }

        // Store the correct default display
        elemdisplay[ nodeName ] = display;

        return display;
    }

    jQuery.each([ "height", "width" ], function( i, name ) {
        jQuery.cssHooks[ name ] = {
            get: function( elem, computed, extra ) {
                if ( computed ) {
                    // certain elements can have dimension info if we invisibly show them
                    // however, it must have a current display style that would benefit from this
                    if ( elem.offsetWidth === 0 && rdisplayswap.test( curCSS( elem, "display" ) ) ) {
                        return jQuery.swap( elem, cssShow, function() {
                            return getWidthOrHeight( elem, name, extra );
                        });
                    } else {
                        return getWidthOrHeight( elem, name, extra );
                    }
                }
            },

            set: function( elem, value, extra ) {
                return setPositiveNumber( elem, value, extra ?
                    augmentWidthOrHeight(
                        elem,
                        name,
                        extra,
                        jQuery.support.boxSizing && jQuery.css( elem, "boxSizing" ) === "border-box"
                    ) : 0
                );
            }
        };
    });

    if ( !jQuery.support.opacity ) {
        jQuery.cssHooks.opacity = {
            get: function( elem, computed ) {
                // IE uses filters for opacity
                return ropacity.test( (computed && elem.currentStyle ? elem.currentStyle.filter : elem.style.filter) || "" ) ?
                    ( 0.01 * parseFloat( RegExp.$1 ) ) + "" :
                    computed ? "1" : "";
            },

            set: function( elem, value ) {
                var style = elem.style,
                    currentStyle = elem.currentStyle,
                    opacity = jQuery.isNumeric( value ) ? "alpha(opacity=" + value * 100 + ")" : "",
                    filter = currentStyle && currentStyle.filter || style.filter || "";

                // IE has trouble with opacity if it does not have layout
                // Force it by setting the zoom level
                style.zoom = 1;

                // if setting opacity to 1, and no other filters exist - attempt to remove filter attribute #6652
                if ( value >= 1 && jQuery.trim( filter.replace( ralpha, "" ) ) === "" &&
                    style.removeAttribute ) {

                    // Setting style.filter to null, "" & " " still leave "filter:" in the cssText
                    // if "filter:" is present at all, clearType is disabled, we want to avoid this
                    // style.removeAttribute is IE Only, but so apparently is this code path...
                    style.removeAttribute( "filter" );

                    // if there there is no filter style applied in a css rule, we are done
                    if ( currentStyle && !currentStyle.filter ) {
                        return;
                    }
                }

                // otherwise, set new filter values
                style.filter = ralpha.test( filter ) ?
                    filter.replace( ralpha, opacity ) :
                    filter + " " + opacity;
            }
        };
    }

// These hooks cannot be added until DOM ready because the support test
// for it is not run until after DOM ready
    jQuery(function() {
        if ( !jQuery.support.reliableMarginRight ) {
            jQuery.cssHooks.marginRight = {
                get: function( elem, computed ) {
                    // WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
                    // Work around by temporarily setting element display to inline-block
                    return jQuery.swap( elem, { "display": "inline-block" }, function() {
                        if ( computed ) {
                            return curCSS( elem, "marginRight" );
                        }
                    });
                }
            };
        }

        // Webkit bug: https://bugs.webkit.org/show_bug.cgi?id=29084
        // getComputedStyle returns percent when specified for top/left/bottom/right
        // rather than make the css module depend on the offset module, we just check for it here
        if ( !jQuery.support.pixelPosition && jQuery.fn.position ) {
            jQuery.each( [ "top", "left" ], function( i, prop ) {
                jQuery.cssHooks[ prop ] = {
                    get: function( elem, computed ) {
                        if ( computed ) {
                            var ret = curCSS( elem, prop );
                            // if curCSS returns percentage, fallback to offset
                            return rnumnonpx.test( ret ) ? jQuery( elem ).position()[ prop ] + "px" : ret;
                        }
                    }
                };
            });
        }

    });

    if ( jQuery.expr && jQuery.expr.filters ) {
        jQuery.expr.filters.hidden = function( elem ) {
            return ( elem.offsetWidth === 0 && elem.offsetHeight === 0 ) || (!jQuery.support.reliableHiddenOffsets && ((elem.style && elem.style.display) || curCSS( elem, "display" )) === "none");
        };

        jQuery.expr.filters.visible = function( elem ) {
            return !jQuery.expr.filters.hidden( elem );
        };
    }

// These hooks are used by animate to expand properties
    jQuery.each({
        margin: "",
        padding: "",
        border: "Width"
    }, function( prefix, suffix ) {
        jQuery.cssHooks[ prefix + suffix ] = {
            expand: function( value ) {
                var i,

                // assumes a single number if not a string
                    parts = typeof value === "string" ? value.split(" ") : [ value ],
                    expanded = {};

                for ( i = 0; i < 4; i++ ) {
                    expanded[ prefix + cssExpand[ i ] + suffix ] =
                        parts[ i ] || parts[ i - 2 ] || parts[ 0 ];
                }

                return expanded;
            }
        };

        if ( !rmargin.test( prefix ) ) {
            jQuery.cssHooks[ prefix + suffix ].set = setPositiveNumber;
        }
    });
    var r20 = /%20/g,
        rbracket = /\[\]$/,
        rCRLF = /\r?\n/g,
        rinput = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
        rselectTextarea = /^(?:select|textarea)/i;

    jQuery.fn.extend({
        serialize: function() {
            return jQuery.param( this.serializeArray() );
        },
        serializeArray: function() {
            return this.map(function(){
                return this.elements ? jQuery.makeArray( this.elements ) : this;
            })
                .filter(function(){
                    return this.name && !this.disabled &&
                        ( this.checked || rselectTextarea.test( this.nodeName ) ||
                            rinput.test( this.type ) );
                })
                .map(function( i, elem ){
                    var val = jQuery( this ).val();

                    return val == null ?
                        null :
                        jQuery.isArray( val ) ?
                            jQuery.map( val, function( val, i ){
                                return { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
                            }) :
                        { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
                }).get();
        }
    });

//Serialize an array of form elements or a set of
//key/values into a query string
    jQuery.param = function( a, traditional ) {
        var prefix,
            s = [],
            add = function( key, value ) {
                // If value is a function, invoke it and return its value
                value = jQuery.isFunction( value ) ? value() : ( value == null ? "" : value );
                s[ s.length ] = encodeURIComponent( key ) + "=" + encodeURIComponent( value );
            };

        // Set traditional to true for jQuery <= 1.3.2 behavior.
        if ( traditional === undefined ) {
            traditional = jQuery.ajaxSettings && jQuery.ajaxSettings.traditional;
        }

        // If an array was passed in, assume that it is an array of form elements.
        if ( jQuery.isArray( a ) || ( a.jquery && !jQuery.isPlainObject( a ) ) ) {
            // Serialize the form elements
            jQuery.each( a, function() {
                add( this.name, this.value );
            });

        } else {
            // If traditional, encode the "old" way (the way 1.3.2 or older
            // did it), otherwise encode params recursively.
            for ( prefix in a ) {
                buildParams( prefix, a[ prefix ], traditional, add );
            }
        }

        // Return the resulting serialization
        return s.join( "&" ).replace( r20, "+" );
    };

    function buildParams( prefix, obj, traditional, add ) {
        var name;

        if ( jQuery.isArray( obj ) ) {
            // Serialize array item.
            jQuery.each( obj, function( i, v ) {
                if ( traditional || rbracket.test( prefix ) ) {
                    // Treat each array item as a scalar.
                    add( prefix, v );

                } else {
                    // If array item is non-scalar (array or object), encode its
                    // numeric index to resolve deserialization ambiguity issues.
                    // Note that rack (as of 1.0.0) can't currently deserialize
                    // nested arrays properly, and attempting to do so may cause
                    // a server error. Possible fixes are to modify rack's
                    // deserialization algorithm or to provide an option or flag
                    // to force array serialization to be shallow.
                    buildParams( prefix + "[" + ( typeof v === "object" ? i : "" ) + "]", v, traditional, add );
                }
            });

        } else if ( !traditional && jQuery.type( obj ) === "object" ) {
            // Serialize object item.
            for ( name in obj ) {
                buildParams( prefix + "[" + name + "]", obj[ name ], traditional, add );
            }

        } else {
            // Serialize scalar item.
            add( prefix, obj );
        }
    }
    var // Document location
        ajaxLocation,
    // Document location segments
        ajaxLocParts,

        rhash = /#.*$/,
        rheaders = /^(.*?):[ \t]*([^\r\n]*)\r?$/mg, // IE leaves an \r character at EOL
    // #7653, #8125, #8152: local protocol detection
        rlocalProtocol = /^(?:about|app|app\-storage|.+\-extension|file|res|widget):$/,
        rnoContent = /^(?:GET|HEAD)$/,
        rprotocol = /^\/\//,
        rquery = /\?/,
        rscript = /asdflkasdjfñalsdfkjañsdflkajsdfñalsdkfjañdlkfjfjf/gi,
        rts = /([?&])_=[^&]*/,
        rurl = /^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+)|)|)/,

    // Keep a copy of the old load method
        _load = jQuery.fn.load,

    /* Prefilters
     * 1) They are useful to introduce custom dataTypes (see ajax/jsonp.js for an example)
     * 2) These are called:
     *    - BEFORE asking for a transport
     *    - AFTER param serialization (s.data is a string if s.processData is true)
     * 3) key is the dataType
     * 4) the catchall symbol "*" can be used
     * 5) execution will start with transport dataType and THEN continue down to "*" if needed
     */
        prefilters = {},

    /* Transports bindings
     * 1) key is the dataType
     * 2) the catchall symbol "*" can be used
     * 3) selection will start with transport dataType and THEN go to "*" if needed
     */
        transports = {},

    // Avoid comment-prolog char sequence (#10098); must appease lint and evade compression
        allTypes = ["*/"] + ["*"];

// #8138, IE may throw an exception when accessing
// a field from window.location if document.domain has been set
    try {
        ajaxLocation = location.href;
    } catch( e ) {
        // Use the href attribute of an A element
        // since IE will modify it given document.location
        ajaxLocation = document.createElement( "a" );
        ajaxLocation.href = "";
        ajaxLocation = ajaxLocation.href;
    }

// Segment location into parts
    ajaxLocParts = rurl.exec( ajaxLocation.toLowerCase() ) || [];

// Base "constructor" for jQuery.ajaxPrefilter and jQuery.ajaxTransport
    function addToPrefiltersOrTransports( structure ) {

        // dataTypeExpression is optional and defaults to "*"
        return function( dataTypeExpression, func ) {

            if ( typeof dataTypeExpression !== "string" ) {
                func = dataTypeExpression;
                dataTypeExpression = "*";
            }

            var dataType, list, placeBefore,
                dataTypes = dataTypeExpression.toLowerCase().split( core_rspace ),
                i = 0,
                length = dataTypes.length;

            if ( jQuery.isFunction( func ) ) {
                // For each dataType in the dataTypeExpression
                for ( ; i < length; i++ ) {
                    dataType = dataTypes[ i ];
                    // We control if we're asked to add before
                    // any existing element
                    placeBefore = /^\+/.test( dataType );
                    if ( placeBefore ) {
                        dataType = dataType.substr( 1 ) || "*";
                    }
                    list = structure[ dataType ] = structure[ dataType ] || [];
                    // then we add to the structure accordingly
                    list[ placeBefore ? "unshift" : "push" ]( func );
                }
            }
        };
    }

// Base inspection function for prefilters and transports
    function inspectPrefiltersOrTransports( structure, options, originalOptions, jqXHR,
                                            dataType /* internal */, inspected /* internal */ ) {

        dataType = dataType || options.dataTypes[ 0 ];
        inspected = inspected || {};

        inspected[ dataType ] = true;

        var selection,
            list = structure[ dataType ],
            i = 0,
            length = list ? list.length : 0,
            executeOnly = ( structure === prefilters );

        for ( ; i < length && ( executeOnly || !selection ); i++ ) {
            selection = list[ i ]( options, originalOptions, jqXHR );
            // If we got redirected to another dataType
            // we try there if executing only and not done already
            if ( typeof selection === "string" ) {
                if ( !executeOnly || inspected[ selection ] ) {
                    selection = undefined;
                } else {
                    options.dataTypes.unshift( selection );
                    selection = inspectPrefiltersOrTransports(
                        structure, options, originalOptions, jqXHR, selection, inspected );
                }
            }
        }
        // If we're only executing or nothing was selected
        // we try the catchall dataType if not done already
        if ( ( executeOnly || !selection ) && !inspected[ "*" ] ) {
            selection = inspectPrefiltersOrTransports(
                structure, options, originalOptions, jqXHR, "*", inspected );
        }
        // unnecessary when only executing (prefilters)
        // but it'll be ignored by the caller in that case
        return selection;
    }

// A special extend for ajax options
// that takes "flat" options (not to be deep extended)
// Fixes #9887
    function ajaxExtend( target, src ) {
        var key, deep,
            flatOptions = jQuery.ajaxSettings.flatOptions || {};
        for ( key in src ) {
            if ( src[ key ] !== undefined ) {
                ( flatOptions[ key ] ? target : ( deep || ( deep = {} ) ) )[ key ] = src[ key ];
            }
        }
        if ( deep ) {
            jQuery.extend( true, target, deep );
        }
    }

    jQuery.fn.load = function( url, params, callback ) {
        if ( typeof url !== "string" && _load ) {
            return _load.apply( this, arguments );
        }

        // Don't do a request if no elements are being requested
        if ( !this.length ) {
            return this;
        }

        var selector, type, response,
            self = this,
            off = url.indexOf(" ");

        if ( off >= 0 ) {
            selector = url.slice( off, url.length );
            url = url.slice( 0, off );
        }

        // If it's a function
        if ( jQuery.isFunction( params ) ) {

            // We assume that it's the callback
            callback = params;
            params = undefined;

            // Otherwise, build a param string
        } else if ( params && typeof params === "object" ) {
            type = "POST";
        }

        // Request the remote document
        jQuery.ajax({
            url: url,

            // if "type" variable is undefined, then "GET" method will be used
            type: type,
            dataType: "html",
            data: params,
            complete: function( jqXHR, status ) {
                if ( callback ) {
                    self.each( callback, response || [ jqXHR.responseText, status, jqXHR ] );
                }
            }
        }).done(function( responseText ) {

                // Save response for use in complete callback
                response = arguments;

                // See if a selector was specified
                self.html( selector ?

                    // Create a dummy div to hold the results
                    jQuery("<div>")

                        // inject the contents of the document in, removing the scripts
                        // to avoid any 'Permission Denied' errors in IE
                        .append( responseText.replace( rscript, "" ) )

                        // Locate the specified elements
                        .find( selector ) :

                    // If not, just inject the full result
                    responseText );

            });

        return this;
    };

// Attach a bunch of functions for handling common AJAX events
    jQuery.each( "ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split( " " ), function( i, o ){
        jQuery.fn[ o ] = function( f ){
            return this.on( o, f );
        };
    });

    jQuery.each( [ "get", "post" ], function( i, method ) {
        jQuery[ method ] = function( url, data, callback, type ) {
            // shift arguments if data argument was omitted
            if ( jQuery.isFunction( data ) ) {
                type = type || callback;
                callback = data;
                data = undefined;
            }

            return jQuery.ajax({
                type: method,
                url: url,
                data: data,
                success: callback,
                dataType: type
            });
        };
    });

    jQuery.extend({

        getScript: function( url, callback ) {
            return jQuery.get( url, undefined, callback, "script" );
        },

        getJSON: function( url, data, callback ) {
            return jQuery.get( url, data, callback, "json" );
        },

        // Creates a full fledged settings object into target
        // with both ajaxSettings and settings fields.
        // If target is omitted, writes into ajaxSettings.
        ajaxSetup: function( target, settings ) {
            if ( settings ) {
                // Building a settings object
                ajaxExtend( target, jQuery.ajaxSettings );
            } else {
                // Extending ajaxSettings
                settings = target;
                target = jQuery.ajaxSettings;
            }
            ajaxExtend( target, settings );
            return target;
        },

        ajaxSettings: {
            url: ajaxLocation,
            isLocal: rlocalProtocol.test( ajaxLocParts[ 1 ] ),
            global: true,
            type: "GET",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            processData: true,
            async: true,
            /*
             timeout: 0,
             data: null,
             dataType: null,
             username: null,
             password: null,
             cache: null,
             throws: false,
             traditional: false,
             headers: {},
             */

            accepts: {
                xml: "application/xml, text/xml",
                html: "text/html",
                text: "text/plain",
                json: "application/json, text/javascript",
                "*": allTypes
            },

            contents: {
                xml: /xml/,
                html: /html/,
                json: /json/
            },

            responseFields: {
                xml: "responseXML",
                text: "responseText"
            },

            // List of data converters
            // 1) key format is "source_type destination_type" (a single space in-between)
            // 2) the catchall symbol "*" can be used for source_type
            converters: {

                // Convert anything to text
                "* text": window.String,

                // Text to html (true = no transformation)
                "text html": true,

                // Evaluate text as a json expression
                "text json": jQuery.parseJSON,

                // Parse text as xml
                "text xml": jQuery.parseXML
            },

            // For options that shouldn't be deep extended:
            // you can add your own custom options here if
            // and when you create one that shouldn't be
            // deep extended (see ajaxExtend)
            flatOptions: {
                context: true,
                url: true
            }
        },

        ajaxPrefilter: addToPrefiltersOrTransports( prefilters ),
        ajaxTransport: addToPrefiltersOrTransports( transports ),

        // Main method
        ajax: function( url, options ) {

            // If url is an object, simulate pre-1.5 signature
            if ( typeof url === "object" ) {
                options = url;
                url = undefined;
            }

            // Force options to be an object
            options = options || {};

            var // ifModified key
                ifModifiedKey,
            // Response headers
                responseHeadersString,
                responseHeaders,
            // transport
                transport,
            // timeout handle
                timeoutTimer,
            // Cross-domain detection vars
                parts,
            // To know if global events are to be dispatched
                fireGlobals,
            // Loop variable
                i,
            // Create the final options object
                s = jQuery.ajaxSetup( {}, options ),
            // Callbacks context
                callbackContext = s.context || s,
            // Context for global events
            // It's the callbackContext if one was provided in the options
            // and if it's a DOM node or a jQuery collection
                globalEventContext = callbackContext !== s &&
                    ( callbackContext.nodeType || callbackContext instanceof jQuery ) ?
                    jQuery( callbackContext ) : jQuery.event,
            // Deferreds
                deferred = jQuery.Deferred(),
                completeDeferred = jQuery.Callbacks( "once memory" ),
            // Status-dependent callbacks
                statusCode = s.statusCode || {},
            // Headers (they are sent all at once)
                requestHeaders = {},
                requestHeadersNames = {},
            // The jqXHR state
                state = 0,
            // Default abort message
                strAbort = "canceled",
            // Fake xhr
                jqXHR = {

                    readyState: 0,

                    // Caches the header
                    setRequestHeader: function( name, value ) {
                        if ( !state ) {
                            var lname = name.toLowerCase();
                            name = requestHeadersNames[ lname ] = requestHeadersNames[ lname ] || name;
                            requestHeaders[ name ] = value;
                        }
                        return this;
                    },

                    // Raw string
                    getAllResponseHeaders: function() {
                        return state === 2 ? responseHeadersString : null;
                    },

                    // Builds headers hashtable if needed
                    getResponseHeader: function( key ) {
                        var match;
                        if ( state === 2 ) {
                            if ( !responseHeaders ) {
                                responseHeaders = {};
                                while( ( match = rheaders.exec( responseHeadersString ) ) ) {
                                    responseHeaders[ match[1].toLowerCase() ] = match[ 2 ];
                                }
                            }
                            match = responseHeaders[ key.toLowerCase() ];
                        }
                        return match === undefined ? null : match;
                    },

                    // Overrides response content-type header
                    overrideMimeType: function( type ) {
                        if ( !state ) {
                            s.mimeType = type;
                        }
                        return this;
                    },

                    // Cancel the request
                    abort: function( statusText ) {
                        statusText = statusText || strAbort;
                        if ( transport ) {
                            transport.abort( statusText );
                        }
                        done( 0, statusText );
                        return this;
                    }
                };

            // Callback for when everything is done
            // It is defined here because jslint complains if it is declared
            // at the end of the function (which would be more logical and readable)
            function done( status, nativeStatusText, responses, headers ) {
                var isSuccess, success, error, response, modified,
                    statusText = nativeStatusText;

                // Called once
                if ( state === 2 ) {
                    return;
                }

                // State is "done" now
                state = 2;

                // Clear timeout if it exists
                if ( timeoutTimer ) {
                    clearTimeout( timeoutTimer );
                }

                // Dereference transport for early garbage collection
                // (no matter how long the jqXHR object will be used)
                transport = undefined;

                // Cache response headers
                responseHeadersString = headers || "";

                // Set readyState
                jqXHR.readyState = status > 0 ? 4 : 0;

                // Get response data
                if ( responses ) {
                    response = ajaxHandleResponses( s, jqXHR, responses );
                }

                // If successful, handle type chaining
                if ( status >= 200 && status < 300 || status === 304 ) {

                    // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
                    if ( s.ifModified ) {

                        modified = jqXHR.getResponseHeader("Last-Modified");
                        if ( modified ) {
                            jQuery.lastModified[ ifModifiedKey ] = modified;
                        }
                        modified = jqXHR.getResponseHeader("Etag");
                        if ( modified ) {
                            jQuery.etag[ ifModifiedKey ] = modified;
                        }
                    }

                    // If not modified
                    if ( status === 304 ) {

                        statusText = "notmodified";
                        isSuccess = true;

                        // If we have data
                    } else {

                        isSuccess = ajaxConvert( s, response );
                        statusText = isSuccess.state;
                        success = isSuccess.data;
                        error = isSuccess.error;
                        isSuccess = !error;
                    }
                } else {
                    // We extract error from statusText
                    // then normalize statusText and status for non-aborts
                    error = statusText;
                    if ( !statusText || status ) {
                        statusText = "error";
                        if ( status < 0 ) {
                            status = 0;
                        }
                    }
                }

                // Set data for the fake xhr object
                jqXHR.status = status;
                jqXHR.statusText = "" + ( nativeStatusText || statusText );

                // Success/Error
                if ( isSuccess ) {
                    deferred.resolveWith( callbackContext, [ success, statusText, jqXHR ] );
                } else {
                    deferred.rejectWith( callbackContext, [ jqXHR, statusText, error ] );
                }

                // Status-dependent callbacks
                jqXHR.statusCode( statusCode );
                statusCode = undefined;

                if ( fireGlobals ) {
                    globalEventContext.trigger( "ajax" + ( isSuccess ? "Success" : "Error" ),
                        [ jqXHR, s, isSuccess ? success : error ] );
                }

                // Complete
                completeDeferred.fireWith( callbackContext, [ jqXHR, statusText ] );

                if ( fireGlobals ) {
                    globalEventContext.trigger( "ajaxComplete", [ jqXHR, s ] );
                    // Handle the global AJAX counter
                    if ( !( --jQuery.active ) ) {
                        jQuery.event.trigger( "ajaxStop" );
                    }
                }
            }

            // Attach deferreds
            deferred.promise( jqXHR );
            jqXHR.success = jqXHR.done;
            jqXHR.error = jqXHR.fail;
            jqXHR.complete = completeDeferred.add;

            // Status-dependent callbacks
            jqXHR.statusCode = function( map ) {
                if ( map ) {
                    var tmp;
                    if ( state < 2 ) {
                        for ( tmp in map ) {
                            statusCode[ tmp ] = [ statusCode[tmp], map[tmp] ];
                        }
                    } else {
                        tmp = map[ jqXHR.status ];
                        jqXHR.always( tmp );
                    }
                }
                return this;
            };

            // Remove hash character (#7531: and string promotion)
            // Add protocol if not provided (#5866: IE7 issue with protocol-less urls)
            // We also use the url parameter if available
            s.url = ( ( url || s.url ) + "" ).replace( rhash, "" ).replace( rprotocol, ajaxLocParts[ 1 ] + "//" );

            // Extract dataTypes list
            s.dataTypes = jQuery.trim( s.dataType || "*" ).toLowerCase().split( core_rspace );

            // Determine if a cross-domain request is in order
            if ( s.crossDomain == null ) {
                parts = rurl.exec( s.url.toLowerCase() );
                s.crossDomain = !!( parts &&
                    ( parts[ 1 ] != ajaxLocParts[ 1 ] || parts[ 2 ] != ajaxLocParts[ 2 ] ||
                        ( parts[ 3 ] || ( parts[ 1 ] === "http:" ? 80 : 443 ) ) !=
                            ( ajaxLocParts[ 3 ] || ( ajaxLocParts[ 1 ] === "http:" ? 80 : 443 ) ) )
                    );
            }

            // Convert data if not already a string
            if ( s.data && s.processData && typeof s.data !== "string" ) {
                s.data = jQuery.param( s.data, s.traditional );
            }

            // Apply prefilters
            inspectPrefiltersOrTransports( prefilters, s, options, jqXHR );

            // If request was aborted inside a prefilter, stop there
            if ( state === 2 ) {
                return jqXHR;
            }

            // We can fire global events as of now if asked to
            fireGlobals = s.global;

            // Uppercase the type
            s.type = s.type.toUpperCase();

            // Determine if request has content
            s.hasContent = !rnoContent.test( s.type );

            // Watch for a new set of requests
            if ( fireGlobals && jQuery.active++ === 0 ) {
                jQuery.event.trigger( "ajaxStart" );
            }

            // More options handling for requests with no content
            if ( !s.hasContent ) {

                // If data is available, append data to url
                if ( s.data ) {
                    s.url += ( rquery.test( s.url ) ? "&" : "?" ) + s.data;
                    // #9682: remove data so that it's not used in an eventual retry
                    delete s.data;
                }

                // Get ifModifiedKey before adding the anti-cache parameter
                ifModifiedKey = s.url;

                // Add anti-cache in url if needed
                if ( s.cache === false ) {

                    var ts = jQuery.now(),
                    // try replacing _= if it is there
                        ret = s.url.replace( rts, "$1_=" + ts );

                    // if nothing was replaced, add timestamp to the end
                    s.url = ret + ( ( ret === s.url ) ? ( rquery.test( s.url ) ? "&" : "?" ) + "_=" + ts : "" );
                }
            }

            // Set the correct header, if data is being sent
            if ( s.data && s.hasContent && s.contentType !== false || options.contentType ) {
                jqXHR.setRequestHeader( "Content-Type", s.contentType );
            }

            // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
            if ( s.ifModified ) {
                ifModifiedKey = ifModifiedKey || s.url;
                if ( jQuery.lastModified[ ifModifiedKey ] ) {
                    jqXHR.setRequestHeader( "If-Modified-Since", jQuery.lastModified[ ifModifiedKey ] );
                }
                if ( jQuery.etag[ ifModifiedKey ] ) {
                    jqXHR.setRequestHeader( "If-None-Match", jQuery.etag[ ifModifiedKey ] );
                }
            }

            // Set the Accepts header for the server, depending on the dataType
            jqXHR.setRequestHeader(
                "Accept",
                s.dataTypes[ 0 ] && s.accepts[ s.dataTypes[0] ] ?
                    s.accepts[ s.dataTypes[0] ] + ( s.dataTypes[ 0 ] !== "*" ? ", " + allTypes + "; q=0.01" : "" ) :
                    s.accepts[ "*" ]
            );

            // Check for headers option
            for ( i in s.headers ) {
                jqXHR.setRequestHeader( i, s.headers[ i ] );
            }

            // Allow custom headers/mimetypes and early abort
            if ( s.beforeSend && ( s.beforeSend.call( callbackContext, jqXHR, s ) === false || state === 2 ) ) {
                // Abort if not done already and return
                return jqXHR.abort();

            }

            // aborting is no longer a cancellation
            strAbort = "abort";

            // Install callbacks on deferreds
            for ( i in { success: 1, error: 1, complete: 1 } ) {
                jqXHR[ i ]( s[ i ] );
            }

            // Get transport
            transport = inspectPrefiltersOrTransports( transports, s, options, jqXHR );

            // If no transport, we auto-abort
            if ( !transport ) {
                done( -1, "No Transport" );
            } else {
                jqXHR.readyState = 1;
                // Send global event
                if ( fireGlobals ) {
                    globalEventContext.trigger( "ajaxSend", [ jqXHR, s ] );
                }
                // Timeout
                if ( s.async && s.timeout > 0 ) {
                    timeoutTimer = setTimeout( function(){
                        jqXHR.abort( "timeout" );
                    }, s.timeout );
                }

                try {
                    state = 1;
                    transport.send( requestHeaders, done );
                } catch (e) {
                    // Propagate exception as error if not done
                    if ( state < 2 ) {
                        done( -1, e );
                        // Simply rethrow otherwise
                    } else {
                        throw e;
                    }
                }
            }

            return jqXHR;
        },

        // Counter for holding the number of active queries
        active: 0,

        // Last-Modified header cache for next request
        lastModified: {},
        etag: {}

    });

    /* Handles responses to an ajax request:
     * - sets all responseXXX fields accordingly
     * - finds the right dataType (mediates between content-type and expected dataType)
     * - returns the corresponding response
     */
    function ajaxHandleResponses( s, jqXHR, responses ) {

        var ct, type, finalDataType, firstDataType,
            contents = s.contents,
            dataTypes = s.dataTypes,
            responseFields = s.responseFields;

        // Fill responseXXX fields
        for ( type in responseFields ) {
            if ( type in responses ) {
                jqXHR[ responseFields[type] ] = responses[ type ];
            }
        }

        // Remove auto dataType and get content-type in the process
        while( dataTypes[ 0 ] === "*" ) {
            dataTypes.shift();
            if ( ct === undefined ) {
                ct = s.mimeType || jqXHR.getResponseHeader( "content-type" );
            }
        }

        // Check if we're dealing with a known content-type
        if ( ct ) {
            for ( type in contents ) {
                if ( contents[ type ] && contents[ type ].test( ct ) ) {
                    dataTypes.unshift( type );
                    break;
                }
            }
        }

        // Check to see if we have a response for the expected dataType
        if ( dataTypes[ 0 ] in responses ) {
            finalDataType = dataTypes[ 0 ];
        } else {
            // Try convertible dataTypes
            for ( type in responses ) {
                if ( !dataTypes[ 0 ] || s.converters[ type + " " + dataTypes[0] ] ) {
                    finalDataType = type;
                    break;
                }
                if ( !firstDataType ) {
                    firstDataType = type;
                }
            }
            // Or just use first one
            finalDataType = finalDataType || firstDataType;
        }

        // If we found a dataType
        // We add the dataType to the list if needed
        // and return the corresponding response
        if ( finalDataType ) {
            if ( finalDataType !== dataTypes[ 0 ] ) {
                dataTypes.unshift( finalDataType );
            }
            return responses[ finalDataType ];
        }
    }

// Chain conversions given the request and the original response
    function ajaxConvert( s, response ) {

        var conv, conv2, current, tmp,
        // Work with a copy of dataTypes in case we need to modify it for conversion
            dataTypes = s.dataTypes.slice(),
            prev = dataTypes[ 0 ],
            converters = {},
            i = 0;

        // Apply the dataFilter if provided
        if ( s.dataFilter ) {
            response = s.dataFilter( response, s.dataType );
        }

        // Create converters map with lowercased keys
        if ( dataTypes[ 1 ] ) {
            for ( conv in s.converters ) {
                converters[ conv.toLowerCase() ] = s.converters[ conv ];
            }
        }

        // Convert to each sequential dataType, tolerating list modification
        for ( ; (current = dataTypes[++i]); ) {

            // There's only work to do if current dataType is non-auto
            if ( current !== "*" ) {

                // Convert response if prev dataType is non-auto and differs from current
                if ( prev !== "*" && prev !== current ) {

                    // Seek a direct converter
                    conv = converters[ prev + " " + current ] || converters[ "* " + current ];

                    // If none found, seek a pair
                    if ( !conv ) {
                        for ( conv2 in converters ) {

                            // If conv2 outputs current
                            tmp = conv2.split(" ");
                            if ( tmp[ 1 ] === current ) {

                                // If prev can be converted to accepted input
                                conv = converters[ prev + " " + tmp[ 0 ] ] ||
                                    converters[ "* " + tmp[ 0 ] ];
                                if ( conv ) {
                                    // Condense equivalence converters
                                    if ( conv === true ) {
                                        conv = converters[ conv2 ];

                                        // Otherwise, insert the intermediate dataType
                                    } else if ( converters[ conv2 ] !== true ) {
                                        current = tmp[ 0 ];
                                        dataTypes.splice( i--, 0, current );
                                    }

                                    break;
                                }
                            }
                        }
                    }

                    // Apply converter (if not an equivalence)
                    if ( conv !== true ) {

                        // Unless errors are allowed to bubble, catch and return them
                        if ( conv && s["throws"] ) {
                            response = conv( response );
                        } else {
                            try {
                                response = conv( response );
                            } catch ( e ) {
                                return { state: "parsererror", error: conv ? e : "No conversion from " + prev + " to " + current };
                            }
                        }
                    }
                }

                // Update prev for next iteration
                prev = current;
            }
        }

        return { state: "success", data: response };
    }
    var oldCallbacks = [],
        rquestion = /\?/,
        rjsonp = /(=)\?(?=&|$)|\?\?/,
        nonce = jQuery.now();

// Default jsonp settings
    jQuery.ajaxSetup({
        jsonp: "callback",
        jsonpCallback: function() {
            var callback = oldCallbacks.pop() || ( jQuery.expando + "_" + ( nonce++ ) );
            this[ callback ] = true;
            return callback;
        }
    });

// Detect, normalize options and install callbacks for jsonp requests
    jQuery.ajaxPrefilter( "json jsonp", function( s, originalSettings, jqXHR ) {

        var callbackName, overwritten, responseContainer,
            data = s.data,
            url = s.url,
            hasCallback = s.jsonp !== false,
            replaceInUrl = hasCallback && rjsonp.test( url ),
            replaceInData = hasCallback && !replaceInUrl && typeof data === "string" &&
                !( s.contentType || "" ).indexOf("application/x-www-form-urlencoded") &&
                rjsonp.test( data );

        // Handle iff the expected data type is "jsonp" or we have a parameter to set
        if ( s.dataTypes[ 0 ] === "jsonp" || replaceInUrl || replaceInData ) {

            // Get callback name, remembering preexisting value associated with it
            callbackName = s.jsonpCallback = jQuery.isFunction( s.jsonpCallback ) ?
                s.jsonpCallback() :
                s.jsonpCallback;
            overwritten = window[ callbackName ];

            // Insert callback into url or form data
            if ( replaceInUrl ) {
                s.url = url.replace( rjsonp, "$1" + callbackName );
            } else if ( replaceInData ) {
                s.data = data.replace( rjsonp, "$1" + callbackName );
            } else if ( hasCallback ) {
                s.url += ( rquestion.test( url ) ? "&" : "?" ) + s.jsonp + "=" + callbackName;
            }

            // Use data converter to retrieve json after script execution
            s.converters["script json"] = function() {
                if ( !responseContainer ) {
                    jQuery.error( callbackName + " was not called" );
                }
                return responseContainer[ 0 ];
            };

            // force json dataType
            s.dataTypes[ 0 ] = "json";

            // Install callback
            window[ callbackName ] = function() {
                responseContainer = arguments;
            };

            // Clean-up function (fires after converters)
            jqXHR.always(function() {
                // Restore preexisting value
                window[ callbackName ] = overwritten;

                // Save back as free
                if ( s[ callbackName ] ) {
                    // make sure that re-using the options doesn't screw things around
                    s.jsonpCallback = originalSettings.jsonpCallback;

                    // save the callback name for future use
                    oldCallbacks.push( callbackName );
                }

                // Call if it was a function and we have a response
                if ( responseContainer && jQuery.isFunction( overwritten ) ) {
                    overwritten( responseContainer[ 0 ] );
                }

                responseContainer = overwritten = undefined;
            });

            // Delegate to script
            return "script";
        }
    });
// Install script dataType
    jQuery.ajaxSetup({
        accepts: {
            script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"
        },
        contents: {
            script: /javascript|ecmascript/
        },
        converters: {
            "text script": function( text ) {
                jQuery.globalEval( text );
                return text;
            }
        }
    });

// Handle cache's special case and global
    jQuery.ajaxPrefilter( "script", function( s ) {
        if ( s.cache === undefined ) {
            s.cache = false;
        }
        if ( s.crossDomain ) {
            s.type = "GET";
            s.global = false;
        }
    });

// Bind script tag hack transport
    jQuery.ajaxTransport( "script", function(s) {

        // This transport only deals with cross domain requests
        if ( s.crossDomain ) {

            var script,
                head = document.head || document.getElementsByTagName( "head" )[0] || document.documentElement;

            return {

                send: function( _, callback ) {

                    script = document.createElement( "script" );

                    script.async = "async";

                    if ( s.scriptCharset ) {
                        script.charset = s.scriptCharset;
                    }

                    script.src = s.url;

                    // Attach handlers for all browsers
                    script.onload = script.onreadystatechange = function( _, isAbort ) {

                        if ( isAbort || !script.readyState || /loaded|complete/.test( script.readyState ) ) {

                            // Handle memory leak in IE
                            script.onload = script.onreadystatechange = null;

                            // Remove the script
                            if ( head && script.parentNode ) {
                                head.removeChild( script );
                            }

                            // Dereference the script
                            script = undefined;

                            // Callback if not abort
                            if ( !isAbort ) {
                                callback( 200, "success" );
                            }
                        }
                    };
                    // Use insertBefore instead of appendChild  to circumvent an IE6 bug.
                    // This arises when a base node is used (#2709 and #4378).
                    head.insertBefore( script, head.firstChild );
                },

                abort: function() {
                    if ( script ) {
                        script.onload( 0, 1 );
                    }
                }
            };
        }
    });
    var xhrCallbacks,
    // #5280: Internet Explorer will keep connections alive if we don't abort on unload
        xhrOnUnloadAbort = window.ActiveXObject ? function() {
            // Abort all pending requests
            for ( var key in xhrCallbacks ) {
                xhrCallbacks[ key ]( 0, 1 );
            }
        } : false,
        xhrId = 0;

// Functions to create xhrs
    function createStandardXHR() {
        try {
            return new window.XMLHttpRequest();
        } catch( e ) {}
    }

    function createActiveXHR() {
        try {
            return new window.ActiveXObject( "Microsoft.XMLHTTP" );
        } catch( e ) {}
    }

// Create the request object
// (This is still attached to ajaxSettings for backward compatibility)
    jQuery.ajaxSettings.xhr = window.ActiveXObject ?
        /* Microsoft failed to properly
         * implement the XMLHttpRequest in IE7 (can't request local files),
         * so we use the ActiveXObject when it is available
         * Additionally XMLHttpRequest can be disabled in IE7/IE8 so
         * we need a fallback.
         */
        function() {
            return !this.isLocal && createStandardXHR() || createActiveXHR();
        } :
        // For all other browsers, use the standard XMLHttpRequest object
        createStandardXHR;

// Determine support properties
    (function( xhr ) {
        jQuery.extend( jQuery.support, {
            ajax: !!xhr,
            cors: !!xhr && ( "withCredentials" in xhr )
        });
    })( jQuery.ajaxSettings.xhr() );

// Create transport if the browser can provide an xhr
    if ( jQuery.support.ajax ) {

        jQuery.ajaxTransport(function( s ) {
            // Cross domain only allowed if supported through XMLHttpRequest
            if ( !s.crossDomain || jQuery.support.cors ) {

                var callback;

                return {
                    send: function( headers, complete ) {

                        // Get a new xhr
                        var handle, i,
                            xhr = s.xhr();

                        // Open the socket
                        // Passing null username, generates a login popup on Opera (#2865)
                        if ( s.username ) {
                            xhr.open( s.type, s.url, s.async, s.username, s.password );
                        } else {
                            xhr.open( s.type, s.url, s.async );
                        }

                        // Apply custom fields if provided
                        if ( s.xhrFields ) {
                            for ( i in s.xhrFields ) {
                                xhr[ i ] = s.xhrFields[ i ];
                            }
                        }

                        // Override mime type if needed
                        if ( s.mimeType && xhr.overrideMimeType ) {
                            xhr.overrideMimeType( s.mimeType );
                        }

                        // X-Requested-With header
                        // For cross-domain requests, seeing as conditions for a preflight are
                        // akin to a jigsaw puzzle, we simply never set it to be sure.
                        // (it can always be set on a per-request basis or even using ajaxSetup)
                        // For same-domain requests, won't change header if already provided.
                        if ( !s.crossDomain && !headers["X-Requested-With"] ) {
                            headers[ "X-Requested-With" ] = "XMLHttpRequest";
                        }

                        // Need an extra try/catch for cross domain requests in Firefox 3
                        try {
                            for ( i in headers ) {
                                xhr.setRequestHeader( i, headers[ i ] );
                            }
                        } catch( _ ) {}

                        // Do send the request
                        // This may raise an exception which is actually
                        // handled in jQuery.ajax (so no try/catch here)
                        xhr.send( ( s.hasContent && s.data ) || null );

                        // Listener
                        callback = function( _, isAbort ) {

                            var status,
                                statusText,
                                responseHeaders,
                                responses,
                                xml;

                            // Firefox throws exceptions when accessing properties
                            // of an xhr when a network error occurred
                            // http://helpful.knobs-dials.com/index.php/Component_returned_failure_code:_0x80040111_(NS_ERROR_NOT_AVAILABLE)
                            try {

                                // Was never called and is aborted or complete
                                if ( callback && ( isAbort || xhr.readyState === 4 ) ) {

                                    // Only called once
                                    callback = undefined;

                                    // Do not keep as active anymore
                                    if ( handle ) {
                                        xhr.onreadystatechange = jQuery.noop;
                                        if ( xhrOnUnloadAbort ) {
                                            delete xhrCallbacks[ handle ];
                                        }
                                    }

                                    // If it's an abort
                                    if ( isAbort ) {
                                        // Abort it manually if needed
                                        if ( xhr.readyState !== 4 ) {
                                            xhr.abort();
                                        }
                                    } else {
                                        status = xhr.status;
                                        responseHeaders = xhr.getAllResponseHeaders();
                                        responses = {};
                                        xml = xhr.responseXML;

                                        // Construct response list
                                        if ( xml && xml.documentElement /* #4958 */ ) {
                                            responses.xml = xml;
                                        }

                                        // When requesting binary data, IE6-9 will throw an exception
                                        // on any attempt to access responseText (#11426)
                                        try {
                                            responses.text = xhr.responseText;
                                        } catch( _ ) {
                                        }

                                        // Firefox throws an exception when accessing
                                        // statusText for faulty cross-domain requests
                                        try {
                                            statusText = xhr.statusText;
                                        } catch( e ) {
                                            // We normalize with Webkit giving an empty statusText
                                            statusText = "";
                                        }

                                        // Filter status for non standard behaviors

                                        // If the request is local and we have data: assume a success
                                        // (success with no data won't get notified, that's the best we
                                        // can do given current implementations)
                                        if ( !status && s.isLocal && !s.crossDomain ) {
                                            status = responses.text ? 200 : 404;
                                            // IE - #1450: sometimes returns 1223 when it should be 204
                                        } else if ( status === 1223 ) {
                                            status = 204;
                                        }
                                    }
                                }
                            } catch( firefoxAccessException ) {
                                if ( !isAbort ) {
                                    complete( -1, firefoxAccessException );
                                }
                            }

                            // Call complete if needed
                            if ( responses ) {
                                complete( status, statusText, responses, responseHeaders );
                            }
                        };

                        if ( !s.async ) {
                            // if we're in sync mode we fire the callback
                            callback();
                        } else if ( xhr.readyState === 4 ) {
                            // (IE6 & IE7) if it's in cache and has been
                            // retrieved directly we need to fire the callback
                            setTimeout( callback, 0 );
                        } else {
                            handle = ++xhrId;
                            if ( xhrOnUnloadAbort ) {
                                // Create the active xhrs callbacks list if needed
                                // and attach the unload handler
                                if ( !xhrCallbacks ) {
                                    xhrCallbacks = {};
                                    jQuery( window ).unload( xhrOnUnloadAbort );
                                }
                                // Add to list of active xhrs callbacks
                                xhrCallbacks[ handle ] = callback;
                            }
                            xhr.onreadystatechange = callback;
                        }
                    },

                    abort: function() {
                        if ( callback ) {
                            callback(0,1);
                        }
                    }
                };
            }
        });
    }
    var fxNow, timerId,
        rfxtypes = /^(?:toggle|show|hide)$/,
        rfxnum = new RegExp( "^(?:([-+])=|)(" + core_pnum + ")([a-z%]*)$", "i" ),
        rrun = /queueHooks$/,
        animationPrefilters = [ defaultPrefilter ],
        tweeners = {
            "*": [function( prop, value ) {
                var end, unit, prevScale,
                    tween = this.createTween( prop, value ),
                    parts = rfxnum.exec( value ),
                    target = tween.cur(),
                    start = +target || 0,
                    scale = 1;

                if ( parts ) {
                    end = +parts[2];
                    unit = parts[3] || ( jQuery.cssNumber[ prop ] ? "" : "px" );

                    // We need to compute starting value
                    if ( unit !== "px" && start ) {
                        // Iteratively approximate from a nonzero starting point
                        // Prefer the current property, because this process will be trivial if it uses the same units
                        // Fallback to end or a simple constant
                        start = jQuery.css( tween.elem, prop, true ) || end || 1;

                        do {
                            // If previous iteration zeroed out, double until we get *something*
                            // Use a string for doubling factor so we don't accidentally see scale as unchanged below
                            prevScale = scale = scale || ".5";

                            // Adjust and apply
                            start = start / scale;
                            jQuery.style( tween.elem, prop, start + unit );

                            // Update scale, tolerating zeroes from tween.cur()
                            scale = tween.cur() / target;

                            // Stop looping if we've hit the mark or scale is unchanged
                        } while ( scale !== 1 && scale !== prevScale );
                    }

                    tween.unit = unit;
                    tween.start = start;
                    // If a +=/-= token was provided, we're doing a relative animation
                    tween.end = parts[1] ? start + ( parts[1] + 1 ) * end : end;
                }
                return tween;
            }]
        };

// Animations created synchronously will run synchronously
    function createFxNow() {
        setTimeout(function() {
            fxNow = undefined;
        }, 0 );
        return ( fxNow = jQuery.now() );
    }

    function createTweens( animation, props ) {
        jQuery.each( props, function( prop, value ) {
            var collection = ( tweeners[ prop ] || [] ).concat( tweeners[ "*" ] ),
                index = 0,
                length = collection.length;
            for ( ; index < length; index++ ) {
                if ( collection[ index ].call( animation, prop, value ) ) {

                    // we're done with this property
                    return;
                }
            }
        });
    }

    function Animation( elem, properties, options ) {
        var result,
            index = 0,
            tweenerIndex = 0,
            length = animationPrefilters.length,
            deferred = jQuery.Deferred().always( function() {
                // don't match elem in the :animated selector
                delete tick.elem;
            }),
            tick = function() {
                var currentTime = fxNow || createFxNow(),
                    remaining = Math.max( 0, animation.startTime + animation.duration - currentTime ),
                    percent = 1 - ( remaining / animation.duration || 0 ),
                    index = 0,
                    length = animation.tweens.length;

                for ( ; index < length ; index++ ) {
                    animation.tweens[ index ].run( percent );
                }

                deferred.notifyWith( elem, [ animation, percent, remaining ]);

                if ( percent < 1 && length ) {
                    return remaining;
                } else {
                    deferred.resolveWith( elem, [ animation ] );
                    return false;
                }
            },
            animation = deferred.promise({
                elem: elem,
                props: jQuery.extend( {}, properties ),
                opts: jQuery.extend( true, { specialEasing: {} }, options ),
                originalProperties: properties,
                originalOptions: options,
                startTime: fxNow || createFxNow(),
                duration: options.duration,
                tweens: [],
                createTween: function( prop, end, easing ) {
                    var tween = jQuery.Tween( elem, animation.opts, prop, end,
                        animation.opts.specialEasing[ prop ] || animation.opts.easing );
                    animation.tweens.push( tween );
                    return tween;
                },
                stop: function( gotoEnd ) {
                    var index = 0,
                    // if we are going to the end, we want to run all the tweens
                    // otherwise we skip this part
                        length = gotoEnd ? animation.tweens.length : 0;

                    for ( ; index < length ; index++ ) {
                        animation.tweens[ index ].run( 1 );
                    }

                    // resolve when we played the last frame
                    // otherwise, reject
                    if ( gotoEnd ) {
                        deferred.resolveWith( elem, [ animation, gotoEnd ] );
                    } else {
                        deferred.rejectWith( elem, [ animation, gotoEnd ] );
                    }
                    return this;
                }
            }),
            props = animation.props;

        propFilter( props, animation.opts.specialEasing );

        for ( ; index < length ; index++ ) {
            result = animationPrefilters[ index ].call( animation, elem, props, animation.opts );
            if ( result ) {
                return result;
            }
        }

        createTweens( animation, props );

        if ( jQuery.isFunction( animation.opts.start ) ) {
            animation.opts.start.call( elem, animation );
        }

        jQuery.fx.timer(
            jQuery.extend( tick, {
                anim: animation,
                queue: animation.opts.queue,
                elem: elem
            })
        );

        // attach callbacks from options
        return animation.progress( animation.opts.progress )
            .done( animation.opts.done, animation.opts.complete )
            .fail( animation.opts.fail )
            .always( animation.opts.always );
    }

    function propFilter( props, specialEasing ) {
        var index, name, easing, value, hooks;

        // camelCase, specialEasing and expand cssHook pass
        for ( index in props ) {
            name = jQuery.camelCase( index );
            easing = specialEasing[ name ];
            value = props[ index ];
            if ( jQuery.isArray( value ) ) {
                easing = value[ 1 ];
                value = props[ index ] = value[ 0 ];
            }

            if ( index !== name ) {
                props[ name ] = value;
                delete props[ index ];
            }

            hooks = jQuery.cssHooks[ name ];
            if ( hooks && "expand" in hooks ) {
                value = hooks.expand( value );
                delete props[ name ];

                // not quite $.extend, this wont overwrite keys already present.
                // also - reusing 'index' from above because we have the correct "name"
                for ( index in value ) {
                    if ( !( index in props ) ) {
                        props[ index ] = value[ index ];
                        specialEasing[ index ] = easing;
                    }
                }
            } else {
                specialEasing[ name ] = easing;
            }
        }
    }

    jQuery.Animation = jQuery.extend( Animation, {

        tweener: function( props, callback ) {
            if ( jQuery.isFunction( props ) ) {
                callback = props;
                props = [ "*" ];
            } else {
                props = props.split(" ");
            }

            var prop,
                index = 0,
                length = props.length;

            for ( ; index < length ; index++ ) {
                prop = props[ index ];
                tweeners[ prop ] = tweeners[ prop ] || [];
                tweeners[ prop ].unshift( callback );
            }
        },

        prefilter: function( callback, prepend ) {
            if ( prepend ) {
                animationPrefilters.unshift( callback );
            } else {
                animationPrefilters.push( callback );
            }
        }
    });

    function defaultPrefilter( elem, props, opts ) {
        var index, prop, value, length, dataShow, tween, hooks, oldfire,
            anim = this,
            style = elem.style,
            orig = {},
            handled = [],
            hidden = elem.nodeType && isHidden( elem );

        // handle queue: false promises
        if ( !opts.queue ) {
            hooks = jQuery._queueHooks( elem, "fx" );
            if ( hooks.unqueued == null ) {
                hooks.unqueued = 0;
                oldfire = hooks.empty.fire;
                hooks.empty.fire = function() {
                    if ( !hooks.unqueued ) {
                        oldfire();
                    }
                };
            }
            hooks.unqueued++;

            anim.always(function() {
                // doing this makes sure that the complete handler will be called
                // before this completes
                anim.always(function() {
                    hooks.unqueued--;
                    if ( !jQuery.queue( elem, "fx" ).length ) {
                        hooks.empty.fire();
                    }
                });
            });
        }

        // height/width overflow pass
        if ( elem.nodeType === 1 && ( "height" in props || "width" in props ) ) {
            // Make sure that nothing sneaks out
            // Record all 3 overflow attributes because IE does not
            // change the overflow attribute when overflowX and
            // overflowY are set to the same value
            opts.overflow = [ style.overflow, style.overflowX, style.overflowY ];

            // Set display property to inline-block for height/width
            // animations on inline elements that are having width/height animated
            if ( jQuery.css( elem, "display" ) === "inline" &&
                jQuery.css( elem, "float" ) === "none" ) {

                // inline-level elements accept inline-block;
                // block-level elements need to be inline with layout
                if ( !jQuery.support.inlineBlockNeedsLayout || css_defaultDisplay( elem.nodeName ) === "inline" ) {
                    style.display = "inline-block";

                } else {
                    style.zoom = 1;
                }
            }
        }

        if ( opts.overflow ) {
            style.overflow = "hidden";
            if ( !jQuery.support.shrinkWrapBlocks ) {
                anim.done(function() {
                    style.overflow = opts.overflow[ 0 ];
                    style.overflowX = opts.overflow[ 1 ];
                    style.overflowY = opts.overflow[ 2 ];
                });
            }
        }


        // show/hide pass
        for ( index in props ) {
            value = props[ index ];
            if ( rfxtypes.exec( value ) ) {
                delete props[ index ];
                if ( value === ( hidden ? "hide" : "show" ) ) {
                    continue;
                }
                handled.push( index );
            }
        }

        length = handled.length;
        if ( length ) {
            dataShow = jQuery._data( elem, "fxshow" ) || jQuery._data( elem, "fxshow", {} );
            if ( hidden ) {
                jQuery( elem ).show();
            } else {
                anim.done(function() {
                    jQuery( elem ).hide();
                });
            }
            anim.done(function() {
                var prop;
                jQuery.removeData( elem, "fxshow", true );
                for ( prop in orig ) {
                    jQuery.style( elem, prop, orig[ prop ] );
                }
            });
            for ( index = 0 ; index < length ; index++ ) {
                prop = handled[ index ];
                tween = anim.createTween( prop, hidden ? dataShow[ prop ] : 0 );
                orig[ prop ] = dataShow[ prop ] || jQuery.style( elem, prop );

                if ( !( prop in dataShow ) ) {
                    dataShow[ prop ] = tween.start;
                    if ( hidden ) {
                        tween.end = tween.start;
                        tween.start = prop === "width" || prop === "height" ? 1 : 0;
                    }
                }
            }
        }
    }

    function Tween( elem, options, prop, end, easing ) {
        return new Tween.prototype.init( elem, options, prop, end, easing );
    }
    jQuery.Tween = Tween;

    Tween.prototype = {
        constructor: Tween,
        init: function( elem, options, prop, end, easing, unit ) {
            this.elem = elem;
            this.prop = prop;
            this.easing = easing || "swing";
            this.options = options;
            this.start = this.now = this.cur();
            this.end = end;
            this.unit = unit || ( jQuery.cssNumber[ prop ] ? "" : "px" );
        },
        cur: function() {
            var hooks = Tween.propHooks[ this.prop ];

            return hooks && hooks.get ?
                hooks.get( this ) :
                Tween.propHooks._default.get( this );
        },
        run: function( percent ) {
            var eased,
                hooks = Tween.propHooks[ this.prop ];

            if ( this.options.duration ) {
                this.pos = eased = jQuery.easing[ this.easing ](
                    percent, this.options.duration * percent, 0, 1, this.options.duration
                );
            } else {
                this.pos = eased = percent;
            }
            this.now = ( this.end - this.start ) * eased + this.start;

            if ( this.options.step ) {
                this.options.step.call( this.elem, this.now, this );
            }

            if ( hooks && hooks.set ) {
                hooks.set( this );
            } else {
                Tween.propHooks._default.set( this );
            }
            return this;
        }
    };

    Tween.prototype.init.prototype = Tween.prototype;

    Tween.propHooks = {
        _default: {
            get: function( tween ) {
                var result;

                if ( tween.elem[ tween.prop ] != null &&
                    (!tween.elem.style || tween.elem.style[ tween.prop ] == null) ) {
                    return tween.elem[ tween.prop ];
                }

                // passing any value as a 4th parameter to .css will automatically
                // attempt a parseFloat and fallback to a string if the parse fails
                // so, simple values such as "10px" are parsed to Float.
                // complex values such as "rotate(1rad)" are returned as is.
                result = jQuery.css( tween.elem, tween.prop, false, "" );
                // Empty strings, null, undefined and "auto" are converted to 0.
                return !result || result === "auto" ? 0 : result;
            },
            set: function( tween ) {
                // use step hook for back compat - use cssHook if its there - use .style if its
                // available and use plain properties where available
                if ( jQuery.fx.step[ tween.prop ] ) {
                    jQuery.fx.step[ tween.prop ]( tween );
                } else if ( tween.elem.style && ( tween.elem.style[ jQuery.cssProps[ tween.prop ] ] != null || jQuery.cssHooks[ tween.prop ] ) ) {
                    jQuery.style( tween.elem, tween.prop, tween.now + tween.unit );
                } else {
                    tween.elem[ tween.prop ] = tween.now;
                }
            }
        }
    };

// Remove in 2.0 - this supports IE8's panic based approach
// to setting things on disconnected nodes

    Tween.propHooks.scrollTop = Tween.propHooks.scrollLeft = {
        set: function( tween ) {
            if ( tween.elem.nodeType && tween.elem.parentNode ) {
                tween.elem[ tween.prop ] = tween.now;
            }
        }
    };

    jQuery.each([ "toggle", "show", "hide" ], function( i, name ) {
        var cssFn = jQuery.fn[ name ];
        jQuery.fn[ name ] = function( speed, easing, callback ) {
            return speed == null || typeof speed === "boolean" ||
                // special check for .toggle( handler, handler, ... )
                ( !i && jQuery.isFunction( speed ) && jQuery.isFunction( easing ) ) ?
                cssFn.apply( this, arguments ) :
                this.animate( genFx( name, true ), speed, easing, callback );
        };
    });

    jQuery.fn.extend({
        fadeTo: function( speed, to, easing, callback ) {

            // show any hidden elements after setting opacity to 0
            return this.filter( isHidden ).css( "opacity", 0 ).show()

                // animate to the value specified
                .end().animate({ opacity: to }, speed, easing, callback );
        },
        animate: function( prop, speed, easing, callback ) {
            var empty = jQuery.isEmptyObject( prop ),
                optall = jQuery.speed( speed, easing, callback ),
                doAnimation = function() {
                    // Operate on a copy of prop so per-property easing won't be lost
                    var anim = Animation( this, jQuery.extend( {}, prop ), optall );

                    // Empty animations resolve immediately
                    if ( empty ) {
                        anim.stop( true );
                    }
                };

            return empty || optall.queue === false ?
                this.each( doAnimation ) :
                this.queue( optall.queue, doAnimation );
        },
        stop: function( type, clearQueue, gotoEnd ) {
            var stopQueue = function( hooks ) {
                var stop = hooks.stop;
                delete hooks.stop;
                stop( gotoEnd );
            };

            if ( typeof type !== "string" ) {
                gotoEnd = clearQueue;
                clearQueue = type;
                type = undefined;
            }
            if ( clearQueue && type !== false ) {
                this.queue( type || "fx", [] );
            }

            return this.each(function() {
                var dequeue = true,
                    index = type != null && type + "queueHooks",
                    timers = jQuery.timers,
                    data = jQuery._data( this );

                if ( index ) {
                    if ( data[ index ] && data[ index ].stop ) {
                        stopQueue( data[ index ] );
                    }
                } else {
                    for ( index in data ) {
                        if ( data[ index ] && data[ index ].stop && rrun.test( index ) ) {
                            stopQueue( data[ index ] );
                        }
                    }
                }

                for ( index = timers.length; index--; ) {
                    if ( timers[ index ].elem === this && (type == null || timers[ index ].queue === type) ) {
                        timers[ index ].anim.stop( gotoEnd );
                        dequeue = false;
                        timers.splice( index, 1 );
                    }
                }

                // start the next in the queue if the last step wasn't forced
                // timers currently will call their complete callbacks, which will dequeue
                // but only if they were gotoEnd
                if ( dequeue || !gotoEnd ) {
                    jQuery.dequeue( this, type );
                }
            });
        }
    });

// Generate parameters to create a standard animation
    function genFx( type, includeWidth ) {
        var which,
            attrs = { height: type },
            i = 0;

        // if we include width, step value is 1 to do all cssExpand values,
        // if we don't include width, step value is 2 to skip over Left and Right
        includeWidth = includeWidth? 1 : 0;
        for( ; i < 4 ; i += 2 - includeWidth ) {
            which = cssExpand[ i ];
            attrs[ "margin" + which ] = attrs[ "padding" + which ] = type;
        }

        if ( includeWidth ) {
            attrs.opacity = attrs.width = type;
        }

        return attrs;
    }

// Generate shortcuts for custom animations
    jQuery.each({
        slideDown: genFx("show"),
        slideUp: genFx("hide"),
        slideToggle: genFx("toggle"),
        fadeIn: { opacity: "show" },
        fadeOut: { opacity: "hide" },
        fadeToggle: { opacity: "toggle" }
    }, function( name, props ) {
        jQuery.fn[ name ] = function( speed, easing, callback ) {
            return this.animate( props, speed, easing, callback );
        };
    });

    jQuery.speed = function( speed, easing, fn ) {
        var opt = speed && typeof speed === "object" ? jQuery.extend( {}, speed ) : {
            complete: fn || !fn && easing ||
                jQuery.isFunction( speed ) && speed,
            duration: speed,
            easing: fn && easing || easing && !jQuery.isFunction( easing ) && easing
        };

        opt.duration = jQuery.fx.off ? 0 : typeof opt.duration === "number" ? opt.duration :
            opt.duration in jQuery.fx.speeds ? jQuery.fx.speeds[ opt.duration ] : jQuery.fx.speeds._default;

        // normalize opt.queue - true/undefined/null -> "fx"
        if ( opt.queue == null || opt.queue === true ) {
            opt.queue = "fx";
        }

        // Queueing
        opt.old = opt.complete;

        opt.complete = function() {
            if ( jQuery.isFunction( opt.old ) ) {
                opt.old.call( this );
            }

            if ( opt.queue ) {
                jQuery.dequeue( this, opt.queue );
            }
        };

        return opt;
    };

    jQuery.easing = {
        linear: function( p ) {
            return p;
        },
        swing: function( p ) {
            return 0.5 - Math.cos( p*Math.PI ) / 2;
        }
    };

    jQuery.timers = [];
    jQuery.fx = Tween.prototype.init;
    jQuery.fx.tick = function() {
        var timer,
            timers = jQuery.timers,
            i = 0;

        for ( ; i < timers.length; i++ ) {
            timer = timers[ i ];
            // Checks the timer has not already been removed
            if ( !timer() && timers[ i ] === timer ) {
                timers.splice( i--, 1 );
            }
        }

        if ( !timers.length ) {
            jQuery.fx.stop();
        }
    };

    jQuery.fx.timer = function( timer ) {
        if ( timer() && jQuery.timers.push( timer ) && !timerId ) {
            timerId = setInterval( jQuery.fx.tick, jQuery.fx.interval );
        }
    };

    jQuery.fx.interval = 13;

    jQuery.fx.stop = function() {
        clearInterval( timerId );
        timerId = null;
    };

    jQuery.fx.speeds = {
        slow: 600,
        fast: 200,
        // Default speed
        _default: 400
    };

// Back Compat <1.8 extension point
    jQuery.fx.step = {};

    if ( jQuery.expr && jQuery.expr.filters ) {
        jQuery.expr.filters.animated = function( elem ) {
            return jQuery.grep(jQuery.timers, function( fn ) {
                return elem === fn.elem;
            }).length;
        };
    }
    var rroot = /^(?:body|html)$/i;

    jQuery.fn.offset = function( options ) {
        if ( arguments.length ) {
            return options === undefined ?
                this :
                this.each(function( i ) {
                    jQuery.offset.setOffset( this, options, i );
                });
        }

        var box, docElem, body, win, clientTop, clientLeft, scrollTop, scrollLeft, top, left,
            elem = this[ 0 ],
            doc = elem && elem.ownerDocument;

        if ( !doc ) {
            return;
        }

        if ( (body = doc.body) === elem ) {
            return jQuery.offset.bodyOffset( elem );
        }

        docElem = doc.documentElement;

        // Make sure we're not dealing with a disconnected DOM node
        if ( !jQuery.contains( docElem, elem ) ) {
            return { top: 0, left: 0 };
        }

        box = elem.getBoundingClientRect();
        win = getWindow( doc );
        clientTop  = docElem.clientTop  || body.clientTop  || 0;
        clientLeft = docElem.clientLeft || body.clientLeft || 0;
        scrollTop  = win.pageYOffset || docElem.scrollTop;
        scrollLeft = win.pageXOffset || docElem.scrollLeft;
        top  = box.top  + scrollTop  - clientTop;
        left = box.left + scrollLeft - clientLeft;

        return { top: top, left: left };
    };

    jQuery.offset = {

        bodyOffset: function( body ) {
            var top = body.offsetTop,
                left = body.offsetLeft;

            if ( jQuery.support.doesNotIncludeMarginInBodyOffset ) {
                top  += parseFloat( jQuery.css(body, "marginTop") ) || 0;
                left += parseFloat( jQuery.css(body, "marginLeft") ) || 0;
            }

            return { top: top, left: left };
        },

        setOffset: function( elem, options, i ) {
            var position = jQuery.css( elem, "position" );

            // set position first, in-case top/left are set even on static elem
            if ( position === "static" ) {
                elem.style.position = "relative";
            }

            var curElem = jQuery( elem ),
                curOffset = curElem.offset(),
                curCSSTop = jQuery.css( elem, "top" ),
                curCSSLeft = jQuery.css( elem, "left" ),
                calculatePosition = ( position === "absolute" || position === "fixed" ) && jQuery.inArray("auto", [curCSSTop, curCSSLeft]) > -1,
                props = {}, curPosition = {}, curTop, curLeft;

            // need to be able to calculate position if either top or left is auto and position is either absolute or fixed
            if ( calculatePosition ) {
                curPosition = curElem.position();
                curTop = curPosition.top;
                curLeft = curPosition.left;
            } else {
                curTop = parseFloat( curCSSTop ) || 0;
                curLeft = parseFloat( curCSSLeft ) || 0;
            }

            if ( jQuery.isFunction( options ) ) {
                options = options.call( elem, i, curOffset );
            }

            if ( options.top != null ) {
                props.top = ( options.top - curOffset.top ) + curTop;
            }
            if ( options.left != null ) {
                props.left = ( options.left - curOffset.left ) + curLeft;
            }

            if ( "using" in options ) {
                options.using.call( elem, props );
            } else {
                curElem.css( props );
            }
        }
    };


    jQuery.fn.extend({

        position: function() {
            if ( !this[0] ) {
                return;
            }

            var elem = this[0],

            // Get *real* offsetParent
                offsetParent = this.offsetParent(),

            // Get correct offsets
                offset       = this.offset(),
                parentOffset = rroot.test(offsetParent[0].nodeName) ? { top: 0, left: 0 } : offsetParent.offset();

            // Subtract element margins
            // note: when an element has margin: auto the offsetLeft and marginLeft
            // are the same in Safari causing offset.left to incorrectly be 0
            offset.top  -= parseFloat( jQuery.css(elem, "marginTop") ) || 0;
            offset.left -= parseFloat( jQuery.css(elem, "marginLeft") ) || 0;

            // Add offsetParent borders
            parentOffset.top  += parseFloat( jQuery.css(offsetParent[0], "borderTopWidth") ) || 0;
            parentOffset.left += parseFloat( jQuery.css(offsetParent[0], "borderLeftWidth") ) || 0;

            // Subtract the two offsets
            return {
                top:  offset.top  - parentOffset.top,
                left: offset.left - parentOffset.left
            };
        },

        offsetParent: function() {
            return this.map(function() {
                var offsetParent = this.offsetParent || document.body;
                while ( offsetParent && (!rroot.test(offsetParent.nodeName) && jQuery.css(offsetParent, "position") === "static") ) {
                    offsetParent = offsetParent.offsetParent;
                }
                return offsetParent || document.body;
            });
        }
    });


// Create scrollLeft and scrollTop methods
    jQuery.each( {scrollLeft: "pageXOffset", scrollTop: "pageYOffset"}, function( method, prop ) {
        var top = /Y/.test( prop );

        jQuery.fn[ method ] = function( val ) {
            return jQuery.access( this, function( elem, method, val ) {
                var win = getWindow( elem );

                if ( val === undefined ) {
                    return win ? (prop in win) ? win[ prop ] :
                        win.document.documentElement[ method ] :
                        elem[ method ];
                }

                if ( win ) {
                    win.scrollTo(
                        !top ? val : jQuery( win ).scrollLeft(),
                        top ? val : jQuery( win ).scrollTop()
                    );

                } else {
                    elem[ method ] = val;
                }
            }, method, val, arguments.length, null );
        };
    });

    function getWindow( elem ) {
        return jQuery.isWindow( elem ) ?
            elem :
            elem.nodeType === 9 ?
                elem.defaultView || elem.parentWindow :
                false;
    }
// Create innerHeight, innerWidth, height, width, outerHeight and outerWidth methods
    jQuery.each( { Height: "height", Width: "width" }, function( name, type ) {
        jQuery.each( { padding: "inner" + name, content: type, "": "outer" + name }, function( defaultExtra, funcName ) {
            // margin is only for outerHeight, outerWidth
            jQuery.fn[ funcName ] = function( margin, value ) {
                var chainable = arguments.length && ( defaultExtra || typeof margin !== "boolean" ),
                    extra = defaultExtra || ( margin === true || value === true ? "margin" : "border" );

                return jQuery.access( this, function( elem, type, value ) {
                    var doc;

                    if ( jQuery.isWindow( elem ) ) {
                        // As of 5/8/2012 this will yield incorrect results for Mobile Safari, but there
                        // isn't a whole lot we can do. See pull request at this URL for discussion:
                        // https://github.com/jquery/jquery/pull/764
                        return elem.document.documentElement[ "client" + name ];
                    }

                    // Get document width or height
                    if ( elem.nodeType === 9 ) {
                        doc = elem.documentElement;

                        // Either scroll[Width/Height] or offset[Width/Height] or client[Width/Height], whichever is greatest
                        // unfortunately, this causes bug #3838 in IE6/8 only, but there is currently no good, small way to fix it.
                        return Math.max(
                            elem.body[ "scroll" + name ], doc[ "scroll" + name ],
                            elem.body[ "offset" + name ], doc[ "offset" + name ],
                            doc[ "client" + name ]
                        );
                    }

                    return value === undefined ?
                        // Get width or height on the element, requesting but not forcing parseFloat
                        jQuery.css( elem, type, value, extra ) :

                        // Set width or height on the element
                        jQuery.style( elem, type, value, extra );
                }, type, chainable ? margin : undefined, chainable, null );
            };
        });
    });
// Expose jQuery to the global object
    window.jQuery = window.$ = jQuery;

// Expose jQuery as an AMD module, but only for AMD loaders that
// understand the issues with loading multiple versions of jQuery
// in a page that all might call define(). The loader will indicate
// they have special allowances for multiple jQuery versions by
// specifying define.amd.jQuery = true. Register as a named module,
// since jQuery can be concatenated with other files that may use define,
// but not use a proper concatenation script that understands anonymous
// AMD modules. A named AMD is safest and most robust way to register.
// Lowercase jquery is used because AMD module names are derived from
// file names, and jQuery is normally delivered in a lowercase file name.
// Do this after creating the global so that if an AMD module wants to call
// noConflict to hide this version of jQuery, it will work.
    if ( typeof define === "function" && define.amd && define.amd.jQuery ) {
        define( "jquery", [], function () { return jQuery; } );
    }

})( window );
// lib/handlebars/base.js
var Handlebars = {};

Handlebars.VERSION = "1.0.beta.6";

Handlebars.helpers  = {};
Handlebars.partials = {};

Handlebars.registerHelper = function(name, fn, inverse) {
  if(inverse) { fn.not = inverse; }
  this.helpers[name] = fn;
};

Handlebars.registerPartial = function(name, str) {
  this.partials[name] = str;
};

Handlebars.registerHelper('helperMissing', function(arg) {
  if(arguments.length === 2) {
    return undefined;
  } else {
    throw new Error("Could not find property '" + arg + "'");
  }
});

var toString = Object.prototype.toString, functionType = "[object Function]";

Handlebars.registerHelper('blockHelperMissing', function(context, options) {
  var inverse = options.inverse || function() {}, fn = options.fn;


  var ret = "";
  var type = toString.call(context);

  if(type === functionType) { context = context.call(this); }

  if(context === true) {
    return fn(this);
  } else if(context === false || context == null) {
    return inverse(this);
  } else if(type === "[object Array]") {
    if(context.length > 0) {
      for(var i=0, j=context.length; i<j; i++) {
        ret = ret + fn(context[i]);
      }
    } else {
      ret = inverse(this);
    }
    return ret;
  } else {
    return fn(context);
  }
});

Handlebars.registerHelper('each', function(context, options) {
  var fn = options.fn, inverse = options.inverse;
  var ret = "";

  if(context && context.length > 0) {
    for(var i=0, j=context.length; i<j; i++) {
      ret = ret + fn(context[i]);
    }
  } else {
    ret = inverse(this);
  }
  return ret;
});

Handlebars.registerHelper('if', function(context, options) {
  var type = toString.call(context);
  if(type === functionType) { context = context.call(this); }

  if(!context || Handlebars.Utils.isEmpty(context)) {
    return options.inverse(this);
  } else {
    return options.fn(this);
  }
});

Handlebars.registerHelper('unless', function(context, options) {
  var fn = options.fn, inverse = options.inverse;
  options.fn = inverse;
  options.inverse = fn;

  return Handlebars.helpers['if'].call(this, context, options);
});

Handlebars.registerHelper('with', function(context, options) {
  return options.fn(context);
});

Handlebars.registerHelper('log', function(context) {
  Handlebars.log(context);
});
;
// lib/handlebars/utils.js
Handlebars.Exception = function(message) {
  var tmp = Error.prototype.constructor.apply(this, arguments);

  for (var p in tmp) {
    if (tmp.hasOwnProperty(p)) { this[p] = tmp[p]; }
  }

  this.message = tmp.message;
};
Handlebars.Exception.prototype = new Error;

// Build out our basic SafeString type
Handlebars.SafeString = function(string) {
  this.string = string;
};
Handlebars.SafeString.prototype.toString = function() {
  return this.string.toString();
};

(function() {
  var escape = {
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#x27;",
    "`": "&#x60;"
  };

  var badChars = /&(?!\w+;)|[<>"'`]/g;
  var possible = /[&<>"'`]/;

  var escapeChar = function(chr) {
    return escape[chr] || "&amp;";
  };

  Handlebars.Utils = {
    escapeExpression: function(string) {
      // don't escape SafeStrings, since they're already safe
      if (string instanceof Handlebars.SafeString) {
        return string.toString();
      } else if (string == null || string === false) {
        return "";
      }

      if(!possible.test(string)) { return string; }
      return string.replace(badChars, escapeChar);
    },

    isEmpty: function(value) {
      if (typeof value === "undefined") {
        return true;
      } else if (value === null) {
        return true;
      } else if (value === false) {
        return true;
      } else if(Object.prototype.toString.call(value) === "[object Array]" && value.length === 0) {
        return true;
      } else {
        return false;
      }
    }
  };
})();;
// lib/handlebars/runtime.js
Handlebars.VM = {
  template: function(templateSpec) {
    // Just add water
    var container = {
      escapeExpression: Handlebars.Utils.escapeExpression,
      invokePartial: Handlebars.VM.invokePartial,
      programs: [],
      program: function(i, fn, data) {
        var programWrapper = this.programs[i];
        if(data) {
          return Handlebars.VM.program(fn, data);
        } else if(programWrapper) {
          return programWrapper;
        } else {
          programWrapper = this.programs[i] = Handlebars.VM.program(fn);
          return programWrapper;
        }
      },
      programWithDepth: Handlebars.VM.programWithDepth,
      noop: Handlebars.VM.noop
    };

    return function(context, options) {
      options = options || {};
      return templateSpec.call(container, Handlebars, context, options.helpers, options.partials, options.data);
    };
  },

  programWithDepth: function(fn, data, $depth) {
    var args = Array.prototype.slice.call(arguments, 2);

    return function(context, options) {
      options = options || {};

      return fn.apply(this, [context, options.data || data].concat(args));
    };
  },
  program: function(fn, data) {
    return function(context, options) {
      options = options || {};

      return fn(context, options.data || data);
    };
  },
  noop: function() { return ""; },
  invokePartial: function(partial, name, context, helpers, partials, data) {
    options = { helpers: helpers, partials: partials, data: data };

    if(partial === undefined) {
      throw new Handlebars.Exception("The partial " + name + " could not be found");
    } else if(partial instanceof Function) {
      return partial(context, options);
    } else if (!Handlebars.compile) {
      throw new Handlebars.Exception("The partial " + name + " could not be compiled when running in runtime-only mode");
    } else {
      partials[name] = Handlebars.compile(partial);
      return partials[name](context, options);
    }
  }
};

Handlebars.template = Handlebars.VM.template;
;

/**
 *
 * jquery.sparkline.js
 *
 * v2.1.1
 * (c) Splunk, Inc
 * Contact: Gareth Watts (gareth@splunk.com)
 * http://omnipotent.net/jquery.sparkline/
 *
 * Generates inline sparkline charts from data supplied either to the method
 * or inline in HTML
 *
 * Compatible with Internet Explorer 6.0+ and modern browsers equipped with the canvas tag
 * (Firefox 2.0+, Safari, Opera, etc)
 *
 * License: New BSD License
 *
 * Copyright (c) 2012, Splunk Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of Splunk Inc nor the names of its contributors may
 *       be used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * Usage:
 *  $(selector).sparkline(values, options)
 *
 * If values is undefined or set to 'html' then the data values are read from the specified tag:
 *   <p>Sparkline: <span class="sparkline">1,4,6,6,8,5,3,5</span></p>
 *   $('.sparkline').sparkline();
 * There must be no spaces in the enclosed data set
 *
 * Otherwise values must be an array of numbers or null values
 *    <p>Sparkline: <span id="sparkline1">This text replaced if the browser is compatible</span></p>
 *    $('#sparkline1').sparkline([1,4,6,6,8,5,3,5])
 *    $('#sparkline2').sparkline([1,4,6,null,null,5,3,5])
 *
 * Values can also be specified in an HTML comment, or as a values attribute:
 *    <p>Sparkline: <span class="sparkline"><!--1,4,6,6,8,5,3,5 --></span></p>
 *    <p>Sparkline: <span class="sparkline" values="1,4,6,6,8,5,3,5"></span></p>
 *    $('.sparkline').sparkline();
 *
 * For line charts, x values can also be specified:
 *   <p>Sparkline: <span class="sparkline">1:1,2.7:4,3.4:6,5:6,6:8,8.7:5,9:3,10:5</span></p>
 *    $('#sparkline1').sparkline([ [1,1], [2.7,4], [3.4,6], [5,6], [6,8], [8.7,5], [9,3], [10,5] ])
 *
 * By default, options should be passed in as teh second argument to the sparkline function:
 *   $('.sparkline').sparkline([1,2,3,4], {type: 'bar'})
 *
 * Options can also be set by passing them on the tag itself.  This feature is disabled by default though
 * as there's a slight performance overhead:
 *   $('.sparkline').sparkline([1,2,3,4], {enableTagOptions: true})
 *   <p>Sparkline: <span class="sparkline" sparkType="bar" sparkBarColor="red">loading</span></p>
 * Prefix all options supplied as tag attribute with "spark" (configurable by setting tagOptionPrefix)
 *
 * Supported options:
 *   lineColor - Color of the line used for the chart
 *   fillColor - Color used to fill in the chart - Set to '' or false for a transparent chart
 *   width - Width of the chart - Defaults to 3 times the number of values in pixels
 *   height - Height of the chart - Defaults to the height of the containing element
 *   chartRangeMin - Specify the minimum value to use for the Y range of the chart - Defaults to the minimum value supplied
 *   chartRangeMax - Specify the maximum value to use for the Y range of the chart - Defaults to the maximum value supplied
 *   chartRangeClip - Clip out of range values to the max/min specified by chartRangeMin and chartRangeMax
 *   chartRangeMinX - Specify the minimum value to use for the X range of the chart - Defaults to the minimum value supplied
 *   chartRangeMaxX - Specify the maximum value to use for the X range of the chart - Defaults to the maximum value supplied
 *   composite - If true then don't erase any existing chart attached to the tag, but draw
 *           another chart over the top - Note that width and height are ignored if an
 *           existing chart is detected.
 *   tagValuesAttribute - Name of tag attribute to check for data values - Defaults to 'values'
 *   enableTagOptions - Whether to check tags for sparkline options
 *   tagOptionPrefix - Prefix used for options supplied as tag attributes - Defaults to 'spark'
 *   disableHiddenCheck - If set to true, then the plugin will assume that charts will never be drawn into a
 *           hidden dom element, avoding a browser reflow
 *   disableInteraction - If set to true then all mouseover/click interaction behaviour will be disabled,
 *       making the plugin perform much like it did in 1.x
 *   disableTooltips - If set to true then tooltips will be disabled - Defaults to false (tooltips enabled)
 *   disableHighlight - If set to true then highlighting of selected chart elements on mouseover will be disabled
 *       defaults to false (highlights enabled)
 *   highlightLighten - Factor to lighten/darken highlighted chart values by - Defaults to 1.4 for a 40% increase
 *   tooltipContainer - Specify which DOM element the tooltip should be rendered into - defaults to document.body
 *   tooltipClassname - Optional CSS classname to apply to tooltips - If not specified then a default style will be applied
 *   tooltipOffsetX - How many pixels away from the mouse pointer to render the tooltip on the X axis
 *   tooltipOffsetY - How many pixels away from the mouse pointer to render the tooltip on the r axis
 *   tooltipFormatter  - Optional callback that allows you to override the HTML displayed in the tooltip
 *       callback is given arguments of (sparkline, options, fields)
 *   tooltipChartTitle - If specified then the tooltip uses the string specified by this setting as a title
 *   tooltipFormat - A format string or SPFormat object  (or an array thereof for multiple entries)
 *       to control the format of the tooltip
 *   tooltipPrefix - A string to prepend to each field displayed in a tooltip
 *   tooltipSuffix - A string to append to each field displayed in a tooltip
 *   tooltipSkipNull - If true then null values will not have a tooltip displayed (defaults to true)
 *   tooltipValueLookups - An object or range map to map field values to tooltip strings
 *       (eg. to map -1 to "Lost", 0 to "Draw", and 1 to "Win")
 *   numberFormatter - Optional callback for formatting numbers in tooltips
 *   numberDigitGroupSep - Character to use for group separator in numbers "1,234" - Defaults to ","
 *   numberDecimalMark - Character to use for the decimal point when formatting numbers - Defaults to "."
 *   numberDigitGroupCount - Number of digits between group separator - Defaults to 3
 *
 * There are 7 types of sparkline, selected by supplying a "type" option of 'line' (default),
 * 'bar', 'tristate', 'bullet', 'discrete', 'pie' or 'box'
 *    line - Line chart.  Options:
 *       spotColor - Set to '' to not end each line in a circular spot
 *       minSpotColor - If set, color of spot at minimum value
 *       maxSpotColor - If set, color of spot at maximum value
 *       spotRadius - Radius in pixels
 *       lineWidth - Width of line in pixels
 *       normalRangeMin
 *       normalRangeMax - If set draws a filled horizontal bar between these two values marking the "normal"
 *                      or expected range of values
 *       normalRangeColor - Color to use for the above bar
 *       drawNormalOnTop - Draw the normal range above the chart fill color if true
 *       defaultPixelsPerValue - Defaults to 3 pixels of width for each value in the chart
 *       highlightSpotColor - The color to use for drawing a highlight spot on mouseover - Set to null to disable
 *       highlightLineColor - The color to use for drawing a highlight line on mouseover - Set to null to disable
 *       valueSpots - Specify which points to draw spots on, and in which color.  Accepts a range map
 *
 *   bar - Bar chart.  Options:
 *       barColor - Color of bars for postive values
 *       negBarColor - Color of bars for negative values
 *       zeroColor - Color of bars with zero values
 *       nullColor - Color of bars with null values - Defaults to omitting the bar entirely
 *       barWidth - Width of bars in pixels
 *       colorMap - Optional mappnig of values to colors to override the *BarColor values above
 *                  can be an Array of values to control the color of individual bars or a range map
 *                  to specify colors for individual ranges of values
 *       barSpacing - Gap between bars in pixels
 *       zeroAxis - Centers the y-axis around zero if true
 *
 *   tristate - Charts values of win (>0), lose (<0) or draw (=0)
 *       posBarColor - Color of win values
 *       negBarColor - Color of lose values
 *       zeroBarColor - Color of draw values
 *       barWidth - Width of bars in pixels
 *       barSpacing - Gap between bars in pixels
 *       colorMap - Optional mappnig of values to colors to override the *BarColor values above
 *                  can be an Array of values to control the color of individual bars or a range map
 *                  to specify colors for individual ranges of values
 *
 *   discrete - Options:
 *       lineHeight - Height of each line in pixels - Defaults to 30% of the graph height
 *       thesholdValue - Values less than this value will be drawn using thresholdColor instead of lineColor
 *       thresholdColor
 *
 *   bullet - Values for bullet graphs msut be in the order: target, performance, range1, range2, range3, ...
 *       options:
 *       targetColor - The color of the vertical target marker
 *       targetWidth - The width of the target marker in pixels
 *       performanceColor - The color of the performance measure horizontal bar
 *       rangeColors - Colors to use for each qualitative range background color
 *
 *   pie - Pie chart. Options:
 *       sliceColors - An array of colors to use for pie slices
 *       offset - Angle in degrees to offset the first slice - Try -90 or +90
 *       borderWidth - Width of border to draw around the pie chart, in pixels - Defaults to 0 (no border)
 *       borderColor - Color to use for the pie chart border - Defaults to #000
 *
 *   box - Box plot. Options:
 *       raw - Set to true to supply pre-computed plot points as values
 *             values should be: low_outlier, low_whisker, q1, median, q3, high_whisker, high_outlier
 *             When set to false you can supply any number of values and the box plot will
 *             be computed for you.  Default is false.
 *       showOutliers - Set to true (default) to display outliers as circles
 *       outlierIQR - Interquartile range used to determine outliers.  Default 1.5
 *       boxLineColor - Outline color of the box
 *       boxFillColor - Fill color for the box
 *       whiskerColor - Line color used for whiskers
 *       outlierLineColor - Outline color of outlier circles
 *       outlierFillColor - Fill color of the outlier circles
 *       spotRadius - Radius of outlier circles
 *       medianColor - Line color of the median line
 *       target - Draw a target cross hair at the supplied value (default undefined)
 *
 *
 *
 *   Examples:
 *   $('#sparkline1').sparkline(myvalues, { lineColor: '#f00', fillColor: false });
 *   $('.barsparks').sparkline('html', { type:'bar', height:'40px', barWidth:5 });
 *   $('#tristate').sparkline([1,1,-1,1,0,0,-1], { type:'tristate' }):
 *   $('#discrete').sparkline([1,3,4,5,5,3,4,5], { type:'discrete' });
 *   $('#bullet').sparkline([10,12,12,9,7], { type:'bullet' });
 *   $('#pie').sparkline([1,1,2], { type:'pie' });
 */

/*jslint regexp: true, browser: true, jquery: true, white: true, nomen: false, plusplus: false, maxerr: 500, indent: 4 */

(function(factory) {
    factory(jQuery);
}
(function($) {
    'use strict';

    var UNSET_OPTION = {},
        getDefaults, createClass, SPFormat, clipval, quartile, normalizeValue, normalizeValues,
        remove, isNumber, all, sum, addCSS, ensureArray, formatNumber, RangeMap,
        MouseHandler, Tooltip, barHighlightMixin,
        line, bar, tristate, discrete, bullet, pie, box, defaultStyles, initStyles,
        VShape, VCanvas_base, VCanvas_canvas, VCanvas_vml, pending, shapeCount = 0;

    /**
     * Default configuration settings
     */
    getDefaults = function () {
        return {
            // Settings common to most/all chart types
            common: {
                type: 'line',
                lineColor: '#00f',
                fillColor: '#cdf',
                defaultPixelsPerValue: 3,
                width: 'auto',
                height: 'auto',
                composite: false,
                tagValuesAttribute: 'values',
                tagOptionsPrefix: 'spark',
                enableTagOptions: false,
                enableHighlight: true,
                highlightLighten: 1.4,
                tooltipSkipNull: true,
                tooltipPrefix: '',
                tooltipSuffix: '',
                disableHiddenCheck: false,
                numberFormatter: false,
                numberDigitGroupCount: 3,
                numberDigitGroupSep: ',',
                numberDecimalMark: '.',
                disableTooltips: false,
                disableInteraction: false
            },
            // Defaults for line charts
            line: {
                spotColor: '#f80',
                highlightSpotColor: '#5f5',
                highlightLineColor: '#f22',
                spotRadius: 1.5,
                minSpotColor: '#f80',
                maxSpotColor: '#f80',
                lineWidth: 1,
                normalRangeMin: undefined,
                normalRangeMax: undefined,
                normalRangeColor: '#ccc',
                drawNormalOnTop: false,
                chartRangeMin: undefined,
                chartRangeMax: undefined,
                chartRangeMinX: undefined,
                chartRangeMaxX: undefined,
                tooltipFormat: new SPFormat('<span style="color: {{color}}">&#9679;</span> {{prefix}}{{y}}{{suffix}}')
            },
            // Defaults for bar charts
            bar: {
                barColor: '#3366cc',
                negBarColor: '#f44',
                stackedBarColor: ['#3366cc', '#dc3912', '#ff9900', '#109618', '#66aa00',
                    '#dd4477', '#0099c6', '#990099'],
                zeroColor: undefined,
                nullColor: undefined,
                zeroAxis: true,
                barWidth: 4,
                barSpacing: 1,
                chartRangeMax: undefined,
                chartRangeMin: undefined,
                chartRangeClip: false,
                colorMap: undefined,
                tooltipFormat: new SPFormat('<span style="color: {{color}}">&#9679;</span> {{prefix}}{{value}}{{suffix}}')
            },
            // Defaults for tristate charts
            tristate: {
                barWidth: 4,
                barSpacing: 1,
                posBarColor: '#6f6',
                negBarColor: '#f44',
                zeroBarColor: '#999',
                colorMap: {},
                tooltipFormat: new SPFormat('<span style="color: {{color}}">&#9679;</span> {{value:map}}'),
                tooltipValueLookups: { map: { '-1': 'Loss', '0': 'Draw', '1': 'Win' } }
            },
            // Defaults for discrete charts
            discrete: {
                lineHeight: 'auto',
                thresholdColor: undefined,
                thresholdValue: 0,
                chartRangeMax: undefined,
                chartRangeMin: undefined,
                chartRangeClip: false,
                tooltipFormat: new SPFormat('{{prefix}}{{value}}{{suffix}}')
            },
            // Defaults for bullet charts
            bullet: {
                targetColor: '#f33',
                targetWidth: 3, // width of the target bar in pixels
                performanceColor: '#33f',
                rangeColors: ['#d3dafe', '#a8b6ff', '#7f94ff'],
                base: undefined, // set this to a number to change the base start number
                tooltipFormat: new SPFormat('{{fieldkey:fields}} - {{value}}'),
                tooltipValueLookups: { fields: {r: 'Range', p: 'Performance', t: 'Target'} }
            },
            // Defaults for pie charts
            pie: {
                offset: 0,
                sliceColors: ['#3366cc', '#dc3912', '#ff9900', '#109618', '#66aa00',
                    '#dd4477', '#0099c6', '#990099'],
                borderWidth: 0,
                borderColor: '#000',
                tooltipFormat: new SPFormat('<span style="color: {{color}}">&#9679;</span> {{value}} ({{percent.1}}%)')
            },
            // Defaults for box plots
            box: {
                raw: false,
                boxLineColor: '#000',
                boxFillColor: '#cdf',
                whiskerColor: '#000',
                outlierLineColor: '#333',
                outlierFillColor: '#fff',
                medianColor: '#f00',
                showOutliers: true,
                outlierIQR: 1.5,
                spotRadius: 1.5,
                target: undefined,
                targetColor: '#4a2',
                chartRangeMax: undefined,
                chartRangeMin: undefined,
                tooltipFormat: new SPFormat('{{field:fields}}: {{value}}'),
                tooltipFormatFieldlistKey: 'field',
                tooltipValueLookups: { fields: { lq: 'Lower Quartile', med: 'Median',
                    uq: 'Upper Quartile', lo: 'Left Outlier', ro: 'Right Outlier',
                    lw: 'Left Whisker', rw: 'Right Whisker'} }
            }
        };
    };

    // You can have tooltips use a css class other than jqstooltip by specifying tooltipClassname
    defaultStyles = '.jqstooltip { ' +
        'position: absolute;' +
        'left: 0px;' +
        'top: 0px;' +
        'visibility: hidden;' +
        'background: rgb(0, 0, 0) transparent;' +
        'background-color: rgba(0,0,0,0.6);' +
        'filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000);' +
        '-ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000)";' +
        'color: white;' +
        'font: 10px arial, san serif;' +
        'text-align: left;' +
        'white-space: nowrap;' +
        'padding: 5px;' +
        'border: 1px solid white;' +
        'z-index: 10000;' +
        '}' +
        '.jqsfield { ' +
        'color: white;' +
        'font: 10px arial, san serif;' +
        'text-align: left;' +
        '}';

    /**
     * Utilities
     */

    createClass = function (/* [baseclass, [mixin, ...]], definition */) {
        var Class, args;
        Class = function () {
            this.init.apply(this, arguments);
        };
        if (arguments.length > 1) {
            if (arguments[0]) {
                Class.prototype = $.extend(new arguments[0](), arguments[arguments.length - 1]);
                Class._super = arguments[0].prototype;
            } else {
                Class.prototype = arguments[arguments.length - 1];
            }
            if (arguments.length > 2) {
                args = Array.prototype.slice.call(arguments, 1, -1);
                args.unshift(Class.prototype);
                $.extend.apply($, args);
            }
        } else {
            Class.prototype = arguments[0];
        }
        Class.prototype.cls = Class;
        return Class;
    };

    /**
     * Wraps a format string for tooltips
     * {{x}}
     * {{x.2}
     * {{x:months}}
     */
    $.SPFormatClass = SPFormat = createClass({
        fre: /\{\{([\w.]+?)(:(.+?))?\}\}/g,
        precre: /(\w+)\.(\d+)/,

        init: function (format, fclass) {
            this.format = format;
            this.fclass = fclass;
        },

        render: function (fieldset, lookups, options) {
            var self = this,
                fields = fieldset,
                match, token, lookupkey, fieldvalue, prec;
            return this.format.replace(this.fre, function () {
                var lookup;
                token = arguments[1];
                lookupkey = arguments[3];
                match = self.precre.exec(token);
                if (match) {
                    prec = match[2];
                    token = match[1];
                } else {
                    prec = false;
                }
                fieldvalue = fields[token];
                if (fieldvalue === undefined) {
                    return '';
                }
                if (lookupkey && lookups && lookups[lookupkey]) {
                    lookup = lookups[lookupkey];
                    if (lookup.get) { // RangeMap
                        return lookups[lookupkey].get(fieldvalue) || fieldvalue;
                    } else {
                        return lookups[lookupkey][fieldvalue] || fieldvalue;
                    }
                }
                if (isNumber(fieldvalue)) {
                    if (options.get('numberFormatter')) {
                        fieldvalue = options.get('numberFormatter')(fieldvalue);
                    } else {
                        fieldvalue = formatNumber(fieldvalue, prec,
                            options.get('numberDigitGroupCount'),
                            options.get('numberDigitGroupSep'),
                            options.get('numberDecimalMark'));
                    }
                }
                return fieldvalue;
            });
        }
    });

    // convience method to avoid needing the new operator
    $.spformat = function(format, fclass) {
        return new SPFormat(format, fclass);
    };

    clipval = function (val, min, max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    };

    quartile = function (values, q) {
        var vl;
        if (q === 2) {
            vl = Math.floor(values.length / 2);
            return values.length % 2 ? values[vl] : (values[vl-1] + values[vl]) / 2;
        } else {
            if (values.length % 2 ) { // odd
                vl = (values.length * q + q) / 4;
                return vl % 1 ? (values[Math.floor(vl)] + values[Math.floor(vl) - 1]) / 2 : values[vl-1];
            } else { //even
                vl = (values.length * q + 2) / 4;
                return vl % 1 ? (values[Math.floor(vl)] + values[Math.floor(vl) - 1]) / 2 :  values[vl-1];

            }
        }
    };

    normalizeValue = function (val) {
        var nf;
        switch (val) {
            case 'undefined':
                val = undefined;
                break;
            case 'null':
                val = null;
                break;
            case 'true':
                val = true;
                break;
            case 'false':
                val = false;
                break;
            default:
                nf = parseFloat(val);
                if (val == nf) {
                    val = nf;
                }
        }
        return val;
    };

    normalizeValues = function (vals) {
        var i, result = [];
        for (i = vals.length; i--;) {
            result[i] = normalizeValue(vals[i]);
        }
        return result;
    };

    remove = function (vals, filter) {
        var i, vl, result = [];
        for (i = 0, vl = vals.length; i < vl; i++) {
            if (vals[i] !== filter) {
                result.push(vals[i]);
            }
        }
        return result;
    };

    isNumber = function (num) {
        return !isNaN(parseFloat(num)) && isFinite(num);
    };

    formatNumber = function (num, prec, groupsize, groupsep, decsep) {
        var p, i;
        num = (prec === false ? parseFloat(num).toString() : num.toFixed(prec)).split('');
        p = (p = $.inArray('.', num)) < 0 ? num.length : p;
        if (p < num.length) {
            num[p] = decsep;
        }
        for (i = p - groupsize; i > 0; i -= groupsize) {
            num.splice(i, 0, groupsep);
        }
        return num.join('');
    };

    // determine if all values of an array match a value
    // returns true if the array is empty
    all = function (val, arr, ignoreNull) {
        var i;
        for (i = arr.length; i--; ) {
            if (ignoreNull && arr[i] === null) continue;
            if (arr[i] !== val) {
                return false;
            }
        }
        return true;
    };

    // sums the numeric values in an array, ignoring other values
    sum = function (vals) {
        var total = 0, i;
        for (i = vals.length; i--;) {
            total += typeof vals[i] === 'number' ? vals[i] : 0;
        }
        return total;
    };

    ensureArray = function (val) {
        return $.isArray(val) ? val : [val];
    };

    // http://paulirish.com/2008/bookmarklet-inject-new-css-rules/
    addCSS = function(css) {
        var tag;
        //if ('\v' == 'v') /* ie only */ {
        if (document.createStyleSheet) {
            document.createStyleSheet().cssText = css;
        } else {
            tag = document.createElement('style');
            tag.type = 'text/css';
            document.getElementsByTagName('head')[0].appendChild(tag);
            tag[(typeof document.body.style.WebkitAppearance == 'string') /* webkit only */ ? 'innerText' : 'innerHTML'] = css;
        }
    };

    // Provide a cross-browser interface to a few simple drawing primitives
    $.fn.simpledraw = function (width, height, useExisting, interact) {
        var target, mhandler;
        if (useExisting && (target = this.data('_jqs_vcanvas'))) {
            return target;
        }
        if (width === undefined) {
            width = $(this).innerWidth();
        }
        if (height === undefined) {
            height = $(this).innerHeight();
        }
        if ($.fn.sparkline.hasCanvas) {
            target = new VCanvas_canvas(width, height, this, interact);
        } else if ($.fn.sparkline.hasVML) {
            target = new VCanvas_vml(width, height, this);
        } else {
            return false;
        }
        mhandler = $(this).data('_jqs_mhandler');
        if (mhandler) {
            mhandler.registerCanvas(target);
        }
        return target;
    };

    $.fn.cleardraw = function () {
        var target = this.data('_jqs_vcanvas');
        if (target) {
            target.reset();
        }
    };

    $.RangeMapClass = RangeMap = createClass({
        init: function (map) {
            var key, range, rangelist = [];
            for (key in map) {
                if (map.hasOwnProperty(key) && typeof key === 'string' && key.indexOf(':') > -1) {
                    range = key.split(':');
                    range[0] = range[0].length === 0 ? -Infinity : parseFloat(range[0]);
                    range[1] = range[1].length === 0 ? Infinity : parseFloat(range[1]);
                    range[2] = map[key];
                    rangelist.push(range);
                }
            }
            this.map = map;
            this.rangelist = rangelist || false;
        },

        get: function (value) {
            var rangelist = this.rangelist,
                i, range, result;
            if ((result = this.map[value]) !== undefined) {
                return result;
            }
            if (rangelist) {
                for (i = rangelist.length; i--;) {
                    range = rangelist[i];
                    if (range[0] <= value && range[1] >= value) {
                        return range[2];
                    }
                }
            }
            return undefined;
        }
    });

    // Convenience function
    $.range_map = function(map) {
        return new RangeMap(map);
    };

    MouseHandler = createClass({
        init: function (el, options) {
            var $el = $(el);
            this.$el = $el;
            this.options = options;
            this.currentPageX = 0;
            this.currentPageY = 0;
            this.el = el;
            this.splist = [];
            this.tooltip = null;
            this.over = false;
            this.displayTooltips = !options.get('disableTooltips');
            this.highlightEnabled = !options.get('disableHighlight');
        },

        registerSparkline: function (sp) {
            this.splist.push(sp);
            if (this.over) {
                this.updateDisplay();
            }
        },

        registerCanvas: function (canvas) {
            var $canvas = $(canvas.canvas);
            this.canvas = canvas;
            this.$canvas = $canvas;
            $canvas.mouseenter($.proxy(this.mouseenter, this));
            $canvas.mouseleave($.proxy(this.mouseleave, this));
            $canvas.click($.proxy(this.mouseclick, this));
        },

        reset: function (removeTooltip) {
            this.splist = [];
            if (this.tooltip && removeTooltip) {
                this.tooltip.remove();
                this.tooltip = undefined;
            }
        },

        mouseclick: function (e) {
            var clickEvent = $.Event('sparklineClick');
            clickEvent.originalEvent = e;
            clickEvent.sparklines = this.splist;
            this.$el.trigger(clickEvent);
        },

        mouseenter: function (e) {
            $(document.body).unbind('mousemove.jqs');
            $(document.body).bind('mousemove.jqs', $.proxy(this.mousemove, this));
            this.over = true;
            this.currentPageX = e.pageX;
            this.currentPageY = e.pageY;
            this.currentEl = e.target;
            if (!this.tooltip && this.displayTooltips) {
                this.tooltip = new Tooltip(this.options);
                this.tooltip.updatePosition(e.pageX, e.pageY);
            }
            this.updateDisplay();
        },

        mouseleave: function () {
            $(document.body).unbind('mousemove.jqs');
            var splist = this.splist,
                spcount = splist.length,
                needsRefresh = false,
                sp, i;
            this.over = false;
            this.currentEl = null;

            if (this.tooltip) {
                this.tooltip.remove();
                this.tooltip = null;
            }

            for (i = 0; i < spcount; i++) {
                sp = splist[i];
                if (sp.clearRegionHighlight()) {
                    needsRefresh = true;
                }
            }

            if (needsRefresh) {
                this.canvas.render();
            }
        },

        mousemove: function (e) {
            this.currentPageX = e.pageX;
            this.currentPageY = e.pageY;
            this.currentEl = e.target;
            if (this.tooltip) {
                this.tooltip.updatePosition(e.pageX, e.pageY);
            }
            this.updateDisplay();
        },

        updateDisplay: function () {
            var splist = this.splist,
                spcount = splist.length,
                needsRefresh = false,
                offset = this.$canvas.offset(),
                localX = this.currentPageX - offset.left,
                localY = this.currentPageY - offset.top,
                tooltiphtml, sp, i, result, changeEvent;
            if (!this.over) {
                return;
            }
            for (i = 0; i < spcount; i++) {
                sp = splist[i];
                result = sp.setRegionHighlight(this.currentEl, localX, localY);
                if (result) {
                    needsRefresh = true;
                }
            }
            if (needsRefresh) {
                changeEvent = $.Event('sparklineRegionChange');
                changeEvent.sparklines = this.splist;
                this.$el.trigger(changeEvent);
                if (this.tooltip) {
                    tooltiphtml = '';
                    for (i = 0; i < spcount; i++) {
                        sp = splist[i];
                        tooltiphtml += sp.getCurrentRegionTooltip();
                    }
                    this.tooltip.setContent(tooltiphtml);
                }
                if (!this.disableHighlight) {
                    this.canvas.render();
                }
            }
            if (result === null) {
                this.mouseleave();
            }
        }
    });


    Tooltip = createClass({
        sizeStyle: 'position: static !important;' +
            'display: block !important;' +
            'visibility: hidden !important;' +
            'float: left !important;',

        init: function (options) {
            var tooltipClassname = options.get('tooltipClassname', 'jqstooltip'),
                sizetipStyle = this.sizeStyle,
                offset;
            this.container = options.get('tooltipContainer') || document.body;
            this.tooltipOffsetX = options.get('tooltipOffsetX', 10);
            this.tooltipOffsetY = options.get('tooltipOffsetY', 12);
            // remove any previous lingering tooltip
            $('#jqssizetip').remove();
            $('#jqstooltip').remove();
            this.sizetip = $('<div/>', {
                id: 'jqssizetip',
                style: sizetipStyle,
                'class': tooltipClassname
            });
            this.tooltip = $('<div/>', {
                id: 'jqstooltip',
                'class': tooltipClassname
            }).appendTo(this.container);
            // account for the container's location
            offset = this.tooltip.offset();
            this.offsetLeft = offset.left;
            this.offsetTop = offset.top;
            this.hidden = true;
            $(window).unbind('resize.jqs scroll.jqs');
            $(window).bind('resize.jqs scroll.jqs', $.proxy(this.updateWindowDims, this));
            this.updateWindowDims();
        },

        updateWindowDims: function () {
            this.scrollTop = $(window).scrollTop();
            this.scrollLeft = $(window).scrollLeft();
            this.scrollRight = this.scrollLeft + $(window).width();
            this.updatePosition();
        },

        getSize: function (content) {
            this.sizetip.html(content).appendTo(this.container);
            this.width = this.sizetip.width() + 1;
            this.height = this.sizetip.height();
            this.sizetip.remove();
        },

        setContent: function (content) {
            if (!content) {
                this.tooltip.css('visibility', 'hidden');
                this.hidden = true;
                return;
            }
            this.getSize(content);
            this.tooltip.html(content)
                .css({
                    'width': this.width,
                    'height': this.height,
                    'visibility': 'visible'
                });
            if (this.hidden) {
                this.hidden = false;
                this.updatePosition();
            }
        },

        updatePosition: function (x, y) {
            if (x === undefined) {
                if (this.mousex === undefined) {
                    return;
                }
                x = this.mousex - this.offsetLeft;
                y = this.mousey - this.offsetTop;

            } else {
                this.mousex = x = x - this.offsetLeft;
                this.mousey = y = y - this.offsetTop;
            }
            if (!this.height || !this.width || this.hidden) {
                return;
            }

            y -= this.height + this.tooltipOffsetY;
            x += this.tooltipOffsetX;

            if (y < this.scrollTop) {
                y = this.scrollTop;
            }
            if (x < this.scrollLeft) {
                x = this.scrollLeft;
            } else if (x + this.width > this.scrollRight) {
                x = this.scrollRight - this.width;
            }

            this.tooltip.css({
                'left': x,
                'top': y
            });
        },

        remove: function () {
            this.tooltip.remove();
            this.sizetip.remove();
            this.sizetip = this.tooltip = undefined;
            $(window).unbind('resize.jqs scroll.jqs');
        }
    });

    initStyles = function() {
        addCSS(defaultStyles);
    };

    $(initStyles);

    pending = [];
    $.fn.sparkline = function (userValues, userOptions) {
        return this.each(function () {
            var options = new $.fn.sparkline.options(this, userOptions),
                $this = $(this),
                render, i;
            render = function () {
                var values, width, height, tmp, mhandler, sp, vals;
                if (userValues === 'html' || userValues === undefined) {
                    vals = this.getAttribute(options.get('tagValuesAttribute'));
                    if (vals === undefined || vals === null) {
                        vals = $this.html();
                    }
                    values = vals.replace(/(^\s*<!--)|(-->\s*$)|\s+/g, '').split(',');
                } else {
                    values = userValues;
                }

                width = options.get('width') === 'auto' ? values.length * options.get('defaultPixelsPerValue') : options.get('width');
                if (options.get('height') === 'auto') {
                    if (!options.get('composite') || !$.data(this, '_jqs_vcanvas')) {
                        // must be a better way to get the line height
                        tmp = document.createElement('span');
                        tmp.innerHTML = 'a';
                        $this.html(tmp);
                        height = $(tmp).innerHeight() || $(tmp).height();
                        $(tmp).remove();
                        tmp = null;
                    }
                } else {
                    height = options.get('height');
                }

                if (!options.get('disableInteraction')) {
                    mhandler = $.data(this, '_jqs_mhandler');
                    if (!mhandler) {
                        mhandler = new MouseHandler(this, options);
                        $.data(this, '_jqs_mhandler', mhandler);
                    } else if (!options.get('composite')) {
                        mhandler.reset();
                    }
                } else {
                    mhandler = false;
                }

                if (options.get('composite') && !$.data(this, '_jqs_vcanvas')) {
                    if (!$.data(this, '_jqs_errnotify')) {
                        alert('Attempted to attach a composite sparkline to an element with no existing sparkline');
                        $.data(this, '_jqs_errnotify', true);
                    }
                    return;
                }

                sp = new $.fn.sparkline[options.get('type')](this, values, options, width, height);

                sp.render();

                if (mhandler) {
                    mhandler.registerSparkline(sp);
                }
            };
            // jQuery 1.3.0 completely changed the meaning of :hidden :-/
            if (($(this).html() && !options.get('disableHiddenCheck') && $(this).is(':hidden')) || ($.fn.jquery < '1.3.0' && $(this).parents().is(':hidden')) || !$(this).parents('body').length) {
                if (!options.get('composite') && $.data(this, '_jqs_pending')) {
                    // remove any existing references to the element
                    for (i = pending.length; i; i--) {
                        if (pending[i - 1][0] == this) {
                            pending.splice(i - 1, 1);
                        }
                    }
                }
                pending.push([this, render]);
                $.data(this, '_jqs_pending', true);
            } else {
                render.call(this);
            }
        });
    };

    $.fn.sparkline.defaults = getDefaults();


    $.sparkline_display_visible = function () {
        var el, i, pl;
        var done = [];
        for (i = 0, pl = pending.length; i < pl; i++) {
            el = pending[i][0];
            if ($(el).is(':visible') && !$(el).parents().is(':hidden')) {
                pending[i][1].call(el);
                $.data(pending[i][0], '_jqs_pending', false);
                done.push(i);
            } else if (!$(el).closest('html').length && !$.data(el, '_jqs_pending')) {
                // element has been inserted and removed from the DOM
                // If it was not yet inserted into the dom then the .data request
                // will return true.
                // removing from the dom causes the data to be removed.
                $.data(pending[i][0], '_jqs_pending', false);
                done.push(i);
            }
        }
        for (i = done.length; i; i--) {
            pending.splice(done[i - 1], 1);
        }
    };


    /**
     * User option handler
     */
    $.fn.sparkline.options = createClass({
        init: function (tag, userOptions) {
            var extendedOptions, defaults, base, tagOptionType;
            this.userOptions = userOptions = userOptions || {};
            this.tag = tag;
            this.tagValCache = {};
            defaults = $.fn.sparkline.defaults;
            base = defaults.common;
            this.tagOptionsPrefix = userOptions.enableTagOptions && (userOptions.tagOptionsPrefix || base.tagOptionsPrefix);

            tagOptionType = this.getTagSetting('type');
            if (tagOptionType === UNSET_OPTION) {
                extendedOptions = defaults[userOptions.type || base.type];
            } else {
                extendedOptions = defaults[tagOptionType];
            }
            this.mergedOptions = $.extend({}, base, extendedOptions, userOptions);
        },


        getTagSetting: function (key) {
            var prefix = this.tagOptionsPrefix,
                val, i, pairs, keyval;
            if (prefix === false || prefix === undefined) {
                return UNSET_OPTION;
            }
            if (this.tagValCache.hasOwnProperty(key)) {
                val = this.tagValCache.key;
            } else {
                val = this.tag.getAttribute(prefix + key);
                if (val === undefined || val === null) {
                    val = UNSET_OPTION;
                } else if (val.substr(0, 1) === '[') {
                    val = val.substr(1, val.length - 2).split(',');
                    for (i = val.length; i--;) {
                        val[i] = normalizeValue(val[i].replace(/(^\s*)|(\s*$)/g, ''));
                    }
                } else if (val.substr(0, 1) === '{') {
                    pairs = val.substr(1, val.length - 2).split(',');
                    val = {};
                    for (i = pairs.length; i--;) {
                        keyval = pairs[i].split(':', 2);
                        val[keyval[0].replace(/(^\s*)|(\s*$)/g, '')] = normalizeValue(keyval[1].replace(/(^\s*)|(\s*$)/g, ''));
                    }
                } else {
                    val = normalizeValue(val);
                }
                this.tagValCache.key = val;
            }
            return val;
        },

        get: function (key, defaultval) {
            var tagOption = this.getTagSetting(key),
                result;
            if (tagOption !== UNSET_OPTION) {
                return tagOption;
            }
            return (result = this.mergedOptions[key]) === undefined ? defaultval : result;
        }
    });


    $.fn.sparkline._base = createClass({
        disabled: false,

        init: function (el, values, options, width, height) {
            this.el = el;
            this.$el = $(el);
            this.values = values;
            this.options = options;
            this.width = width;
            this.height = height;
            this.currentRegion = undefined;
        },

        /**
         * Setup the canvas
         */
        initTarget: function () {
            var interactive = !this.options.get('disableInteraction');
            if (!(this.target = this.$el.simpledraw(this.width, this.height, this.options.get('composite'), interactive))) {
                this.disabled = true;
            } else {
                this.canvasWidth = this.target.pixelWidth;
                this.canvasHeight = this.target.pixelHeight;
            }
        },

        /**
         * Actually render the chart to the canvas
         */
        render: function () {
            if (this.disabled) {
                this.el.innerHTML = '';
                return false;
            }
            return true;
        },

        /**
         * Return a region id for a given x/y co-ordinate
         */
        getRegion: function (x, y) {
        },

        /**
         * Highlight an item based on the moused-over x,y co-ordinate
         */
        setRegionHighlight: function (el, x, y) {
            var currentRegion = this.currentRegion,
                highlightEnabled = !this.options.get('disableHighlight'),
                newRegion;
            if (x > this.canvasWidth || y > this.canvasHeight || x < 0 || y < 0) {
                return null;
            }
            newRegion = this.getRegion(el, x, y);
            if (currentRegion !== newRegion) {
                if (currentRegion !== undefined && highlightEnabled) {
                    this.removeHighlight();
                }
                this.currentRegion = newRegion;
                if (newRegion !== undefined && highlightEnabled) {
                    this.renderHighlight();
                }
                return true;
            }
            return false;
        },

        /**
         * Reset any currently highlighted item
         */
        clearRegionHighlight: function () {
            if (this.currentRegion !== undefined) {
                this.removeHighlight();
                this.currentRegion = undefined;
                return true;
            }
            return false;
        },

        renderHighlight: function () {
            this.changeHighlight(true);
        },

        removeHighlight: function () {
            this.changeHighlight(false);
        },

        changeHighlight: function (highlight)  {},

        /**
         * Fetch the HTML to display as a tooltip
         */
        getCurrentRegionTooltip: function () {
            var options = this.options,
                header = '',
                entries = [],
                fields, formats, formatlen, fclass, text, i,
                showFields, showFieldsKey, newFields, fv,
                formatter, format, fieldlen, j;
            if (this.currentRegion === undefined) {
                return '';
            }
            fields = this.getCurrentRegionFields();
            formatter = options.get('tooltipFormatter');
            if (formatter) {
                return formatter(this, options, fields);
            }
            if (options.get('tooltipChartTitle')) {
                header += '<div class="jqs jqstitle">' + options.get('tooltipChartTitle') + '</div>\n';
            }
            formats = this.options.get('tooltipFormat');
            if (!formats) {
                return '';
            }
            if (!$.isArray(formats)) {
                formats = [formats];
            }
            if (!$.isArray(fields)) {
                fields = [fields];
            }
            showFields = this.options.get('tooltipFormatFieldlist');
            showFieldsKey = this.options.get('tooltipFormatFieldlistKey');
            if (showFields && showFieldsKey) {
                // user-selected ordering of fields
                newFields = [];
                for (i = fields.length; i--;) {
                    fv = fields[i][showFieldsKey];
                    if ((j = $.inArray(fv, showFields)) != -1) {
                        newFields[j] = fields[i];
                    }
                }
                fields = newFields;
            }
            formatlen = formats.length;
            fieldlen = fields.length;
            for (i = 0; i < formatlen; i++) {
                format = formats[i];
                if (typeof format === 'string') {
                    format = new SPFormat(format);
                }
                fclass = format.fclass || 'jqsfield';
                for (j = 0; j < fieldlen; j++) {
                    if (!fields[j].isNull || !options.get('tooltipSkipNull')) {
                        $.extend(fields[j], {
                            prefix: options.get('tooltipPrefix'),
                            suffix: options.get('tooltipSuffix')
                        });
                        text = format.render(fields[j], options.get('tooltipValueLookups'), options);
                        entries.push('<div class="' + fclass + '">' + text + '</div>');
                    }
                }
            }
            if (entries.length) {
                return header + entries.join('\n');
            }
            return '';
        },

        getCurrentRegionFields: function () {},

        calcHighlightColor: function (color, options) {
            var highlightColor = options.get('highlightColor'),
                lighten = options.get('highlightLighten'),
                parse, mult, rgbnew, i;
            if (highlightColor) {
                return highlightColor;
            }
            if (lighten) {
                // extract RGB values
                parse = /^#([0-9a-f])([0-9a-f])([0-9a-f])$/i.exec(color) || /^#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})$/i.exec(color);
                if (parse) {
                    rgbnew = [];
                    mult = color.length === 4 ? 16 : 1;
                    for (i = 0; i < 3; i++) {
                        rgbnew[i] = clipval(Math.round(parseInt(parse[i + 1], 16) * mult * lighten), 0, 255);
                    }
                    return 'rgb(' + rgbnew.join(',') + ')';
                }

            }
            return color;
        }

    });

    barHighlightMixin = {
        changeHighlight: function (highlight) {
            var currentRegion = this.currentRegion,
                target = this.target,
                shapeids = this.regionShapes[currentRegion],
                newShapes;
            // will be null if the region value was null
            if (shapeids) {
                newShapes = this.renderRegion(currentRegion, highlight);
                if ($.isArray(newShapes) || $.isArray(shapeids)) {
                    target.replaceWithShapes(shapeids, newShapes);
                    this.regionShapes[currentRegion] = $.map(newShapes, function (newShape) {
                        return newShape.id;
                    });
                } else {
                    target.replaceWithShape(shapeids, newShapes);
                    this.regionShapes[currentRegion] = newShapes.id;
                }
            }
        },

        render: function () {
            var values = this.values,
                target = this.target,
                regionShapes = this.regionShapes,
                shapes, ids, i, j;

            if (!this.cls._super.render.call(this)) {
                return;
            }
            for (i = values.length; i--;) {
                shapes = this.renderRegion(i);
                if (shapes) {
                    if ($.isArray(shapes)) {
                        ids = [];
                        for (j = shapes.length; j--;) {
                            shapes[j].append();
                            ids.push(shapes[j].id);
                        }
                        regionShapes[i] = ids;
                    } else {
                        shapes.append();
                        regionShapes[i] = shapes.id; // store just the shapeid
                    }
                } else {
                    // null value
                    regionShapes[i] = null;
                }
            }
            target.render();
        }
    };

    /**
     * Line charts
     */
    $.fn.sparkline.line = line = createClass($.fn.sparkline._base, {
        type: 'line',

        init: function (el, values, options, width, height) {
            line._super.init.call(this, el, values, options, width, height);
            this.vertices = [];
            this.regionMap = [];
            this.xvalues = [];
            this.yvalues = [];
            this.yminmax = [];
            this.hightlightSpotId = null;
            this.lastShapeId = null;
            this.initTarget();
        },

        getRegion: function (el, x, y) {
            var i,
                regionMap = this.regionMap; // maps regions to value positions
            for (i = regionMap.length; i--;) {
                if (regionMap[i] !== null && x >= regionMap[i][0] && x <= regionMap[i][1]) {
                    return regionMap[i][2];
                }
            }
            return undefined;
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                isNull: this.yvalues[currentRegion] === null,
                x: this.xvalues[currentRegion],
                y: this.yvalues[currentRegion],
                color: this.options.get('lineColor'),
                fillColor: this.options.get('fillColor'),
                offset: currentRegion
            };
        },

        renderHighlight: function () {
            var currentRegion = this.currentRegion,
                target = this.target,
                vertex = this.vertices[currentRegion],
                options = this.options,
                spotRadius = options.get('spotRadius'),
                highlightSpotColor = options.get('highlightSpotColor'),
                highlightLineColor = options.get('highlightLineColor'),
                highlightSpot, highlightLine;

            if (!vertex) {
                return;
            }
            if (spotRadius && highlightSpotColor) {
                highlightSpot = target.drawCircle(vertex[0], vertex[1],
                    spotRadius, undefined, highlightSpotColor);
                this.highlightSpotId = highlightSpot.id;
                target.insertAfterShape(this.lastShapeId, highlightSpot);
            }
            if (highlightLineColor) {
                highlightLine = target.drawLine(vertex[0], this.canvasTop, vertex[0],
                    this.canvasTop + this.canvasHeight, highlightLineColor);
                this.highlightLineId = highlightLine.id;
                target.insertAfterShape(this.lastShapeId, highlightLine);
            }
        },

        removeHighlight: function () {
            var target = this.target;
            if (this.highlightSpotId) {
                target.removeShapeId(this.highlightSpotId);
                this.highlightSpotId = null;
            }
            if (this.highlightLineId) {
                target.removeShapeId(this.highlightLineId);
                this.highlightLineId = null;
            }
        },

        scanValues: function () {
            var values = this.values,
                valcount = values.length,
                xvalues = this.xvalues,
                yvalues = this.yvalues,
                yminmax = this.yminmax,
                i, val, isStr, isArray, sp;
            for (i = 0; i < valcount; i++) {
                val = values[i];
                isStr = typeof(values[i]) === 'string';
                isArray = typeof(values[i]) === 'object' && values[i] instanceof Array;
                sp = isStr && values[i].split(':');
                if (isStr && sp.length === 2) { // x:y
                    xvalues.push(Number(sp[0]));
                    yvalues.push(Number(sp[1]));
                    yminmax.push(Number(sp[1]));
                } else if (isArray) {
                    xvalues.push(val[0]);
                    yvalues.push(val[1]);
                    yminmax.push(val[1]);
                } else {
                    xvalues.push(i);
                    if (values[i] === null || values[i] === 'null') {
                        yvalues.push(null);
                    } else {
                        yvalues.push(Number(val));
                        yminmax.push(Number(val));
                    }
                }
            }
            if (this.options.get('xvalues')) {
                xvalues = this.options.get('xvalues');
            }

            this.maxy = this.maxyorg = Math.max.apply(Math, yminmax);
            this.miny = this.minyorg = Math.min.apply(Math, yminmax);

            this.maxx = Math.max.apply(Math, xvalues);
            this.minx = Math.min.apply(Math, xvalues);

            this.xvalues = xvalues;
            this.yvalues = yvalues;
            this.yminmax = yminmax;

        },

        processRangeOptions: function () {
            var options = this.options,
                normalRangeMin = options.get('normalRangeMin'),
                normalRangeMax = options.get('normalRangeMax');

            if (normalRangeMin !== undefined) {
                if (normalRangeMin < this.miny) {
                    this.miny = normalRangeMin;
                }
                if (normalRangeMax > this.maxy) {
                    this.maxy = normalRangeMax;
                }
            }
            if (options.get('chartRangeMin') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMin') < this.miny)) {
                this.miny = options.get('chartRangeMin');
            }
            if (options.get('chartRangeMax') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMax') > this.maxy)) {
                this.maxy = options.get('chartRangeMax');
            }
            if (options.get('chartRangeMinX') !== undefined && (options.get('chartRangeClipX') || options.get('chartRangeMinX') < this.minx)) {
                this.minx = options.get('chartRangeMinX');
            }
            if (options.get('chartRangeMaxX') !== undefined && (options.get('chartRangeClipX') || options.get('chartRangeMaxX') > this.maxx)) {
                this.maxx = options.get('chartRangeMaxX');
            }

        },

        drawNormalRange: function (canvasLeft, canvasTop, canvasHeight, canvasWidth, rangey) {
            var normalRangeMin = this.options.get('normalRangeMin'),
                normalRangeMax = this.options.get('normalRangeMax'),
                ytop = canvasTop + Math.round(canvasHeight - (canvasHeight * ((normalRangeMax - this.miny) / rangey))),
                height = Math.round((canvasHeight * (normalRangeMax - normalRangeMin)) / rangey);
            this.target.drawRect(canvasLeft, ytop, canvasWidth, height, undefined, this.options.get('normalRangeColor')).append();
        },

        render: function () {
            var options = this.options,
                target = this.target,
                canvasWidth = this.canvasWidth,
                canvasHeight = this.canvasHeight,
                vertices = this.vertices,
                spotRadius = options.get('spotRadius'),
                regionMap = this.regionMap,
                rangex, rangey, yvallast,
                canvasTop, canvasLeft,
                vertex, path, paths, x, y, xnext, xpos, xposnext,
                last, next, yvalcount, lineShapes, fillShapes, plen,
                valueSpots, hlSpotsEnabled, color, xvalues, yvalues, i;

            if (!line._super.render.call(this)) {
                return;
            }

            this.scanValues();
            this.processRangeOptions();

            xvalues = this.xvalues;
            yvalues = this.yvalues;

            if (!this.yminmax.length || this.yvalues.length < 2) {
                // empty or all null valuess
                return;
            }

            canvasTop = canvasLeft = 0;

            rangex = this.maxx - this.minx === 0 ? 1 : this.maxx - this.minx;
            rangey = this.maxy - this.miny === 0 ? 1 : this.maxy - this.miny;
            yvallast = this.yvalues.length - 1;

            if (spotRadius && (canvasWidth < (spotRadius * 4) || canvasHeight < (spotRadius * 4))) {
                spotRadius = 0;
            }
            if (spotRadius) {
                // adjust the canvas size as required so that spots will fit
                hlSpotsEnabled = options.get('highlightSpotColor') &&  !options.get('disableInteraction');
                if (hlSpotsEnabled || options.get('minSpotColor') || (options.get('spotColor') && yvalues[yvallast] === this.miny)) {
                    canvasHeight -= Math.ceil(spotRadius);
                }
                if (hlSpotsEnabled || options.get('maxSpotColor') || (options.get('spotColor') && yvalues[yvallast] === this.maxy)) {
                    canvasHeight -= Math.ceil(spotRadius);
                    canvasTop += Math.ceil(spotRadius);
                }
                if (hlSpotsEnabled ||
                    ((options.get('minSpotColor') || options.get('maxSpotColor')) && (yvalues[0] === this.miny || yvalues[0] === this.maxy))) {
                    canvasLeft += Math.ceil(spotRadius);
                    canvasWidth -= Math.ceil(spotRadius);
                }
                if (hlSpotsEnabled || options.get('spotColor') ||
                    (options.get('minSpotColor') || options.get('maxSpotColor') &&
                        (yvalues[yvallast] === this.miny || yvalues[yvallast] === this.maxy))) {
                    canvasWidth -= Math.ceil(spotRadius);
                }
            }


            canvasHeight--;

            if (options.get('normalRangeMin') !== undefined && !options.get('drawNormalOnTop')) {
                this.drawNormalRange(canvasLeft, canvasTop, canvasHeight, canvasWidth, rangey);
            }

            path = [];
            paths = [path];
            last = next = null;
            yvalcount = yvalues.length;
            for (i = 0; i < yvalcount; i++) {
                x = xvalues[i];
                xnext = xvalues[i + 1];
                y = yvalues[i];
                xpos = canvasLeft + Math.round((x - this.minx) * (canvasWidth / rangex));
                xposnext = i < yvalcount - 1 ? canvasLeft + Math.round((xnext - this.minx) * (canvasWidth / rangex)) : canvasWidth;
                next = xpos + ((xposnext - xpos) / 2);
                regionMap[i] = [last || 0, next, i];
                last = next;
                if (y === null) {
                    if (i) {
                        if (yvalues[i - 1] !== null) {
                            path = [];
                            paths.push(path);
                        }
                        vertices.push(null);
                    }
                } else {
                    if (y < this.miny) {
                        y = this.miny;
                    }
                    if (y > this.maxy) {
                        y = this.maxy;
                    }
                    if (!path.length) {
                        // previous value was null
                        path.push([xpos, canvasTop + canvasHeight]);
                    }
                    vertex = [xpos, canvasTop + Math.round(canvasHeight - (canvasHeight * ((y - this.miny) / rangey)))];
                    path.push(vertex);
                    vertices.push(vertex);
                }
            }

            lineShapes = [];
            fillShapes = [];
            plen = paths.length;
            for (i = 0; i < plen; i++) {
                path = paths[i];
                if (path.length) {
                    if (options.get('fillColor')) {
                        path.push([path[path.length - 1][0], (canvasTop + canvasHeight)]);
                        fillShapes.push(path.slice(0));
                        path.pop();
                    }
                    // if there's only a single point in this path, then we want to display it
                    // as a vertical line which means we keep path[0]  as is
                    if (path.length > 2) {
                        // else we want the first value
                        path[0] = [path[0][0], path[1][1]];
                    }
                    lineShapes.push(path);
                }
            }

            // draw the fill first, then optionally the normal range, then the line on top of that
            plen = fillShapes.length;
            for (i = 0; i < plen; i++) {
                target.drawShape(fillShapes[i],
                    options.get('fillColor'), options.get('fillColor')).append();
            }

            if (options.get('normalRangeMin') !== undefined && options.get('drawNormalOnTop')) {
                this.drawNormalRange(canvasLeft, canvasTop, canvasHeight, canvasWidth, rangey);
            }

            plen = lineShapes.length;
            for (i = 0; i < plen; i++) {
                target.drawShape(lineShapes[i], options.get('lineColor'), undefined,
                    options.get('lineWidth')).append();
            }

            if (spotRadius && options.get('valueSpots')) {
                valueSpots = options.get('valueSpots');
                if (valueSpots.get === undefined) {
                    valueSpots = new RangeMap(valueSpots);
                }
                for (i = 0; i < yvalcount; i++) {
                    color = valueSpots.get(yvalues[i]);
                    if (color) {
                        target.drawCircle(canvasLeft + Math.round((xvalues[i] - this.minx) * (canvasWidth / rangex)),
                            canvasTop + Math.round(canvasHeight - (canvasHeight * ((yvalues[i] - this.miny) / rangey))),
                            spotRadius, undefined,
                            color).append();
                    }
                }

            }
            if (spotRadius && options.get('spotColor') && yvalues[yvallast] !== null) {
                target.drawCircle(canvasLeft + Math.round((xvalues[xvalues.length - 1] - this.minx) * (canvasWidth / rangex)),
                    canvasTop + Math.round(canvasHeight - (canvasHeight * ((yvalues[yvallast] - this.miny) / rangey))),
                    spotRadius, undefined,
                    options.get('spotColor')).append();
            }
            if (this.maxy !== this.minyorg) {
                if (spotRadius && options.get('minSpotColor')) {
                    x = xvalues[$.inArray(this.minyorg, yvalues)];
                    target.drawCircle(canvasLeft + Math.round((x - this.minx) * (canvasWidth / rangex)),
                        canvasTop + Math.round(canvasHeight - (canvasHeight * ((this.minyorg - this.miny) / rangey))),
                        spotRadius, undefined,
                        options.get('minSpotColor')).append();
                }
                if (spotRadius && options.get('maxSpotColor')) {
                    x = xvalues[$.inArray(this.maxyorg, yvalues)];
                    target.drawCircle(canvasLeft + Math.round((x - this.minx) * (canvasWidth / rangex)),
                        canvasTop + Math.round(canvasHeight - (canvasHeight * ((this.maxyorg - this.miny) / rangey))),
                        spotRadius, undefined,
                        options.get('maxSpotColor')).append();
                }
            }

            this.lastShapeId = target.getLastShapeId();
            this.canvasTop = canvasTop;
            target.render();
        }
    });

    /**
     * Bar charts
     */
    $.fn.sparkline.bar = bar = createClass($.fn.sparkline._base, barHighlightMixin, {
        type: 'bar',

        init: function (el, values, options, width, height) {
            var barWidth = parseInt(options.get('barWidth'), 10),
                barSpacing = parseInt(options.get('barSpacing'), 10),
                chartRangeMin = options.get('chartRangeMin'),
                chartRangeMax = options.get('chartRangeMax'),
                chartRangeClip = options.get('chartRangeClip'),
                stackMin = Infinity,
                stackMax = -Infinity,
                isStackString, groupMin, groupMax, stackRanges,
                numValues, i, vlen, range, zeroAxis, xaxisOffset, min, max, clipMin, clipMax,
                stacked, vlist, j, slen, svals, val, yoffset, yMaxCalc, canvasHeightEf;
            bar._super.init.call(this, el, values, options, width, height);

            // scan values to determine whether to stack bars
            for (i = 0, vlen = values.length; i < vlen; i++) {
                val = values[i];
                isStackString = typeof(val) === 'string' && val.indexOf(':') > -1;
                if (isStackString || $.isArray(val)) {
                    stacked = true;
                    if (isStackString) {
                        val = values[i] = normalizeValues(val.split(':'));
                    }
                    val = remove(val, null); // min/max will treat null as zero
                    groupMin = Math.min.apply(Math, val);
                    groupMax = Math.max.apply(Math, val);
                    if (groupMin < stackMin) {
                        stackMin = groupMin;
                    }
                    if (groupMax > stackMax) {
                        stackMax = groupMax;
                    }
                }
            }

            this.stacked = stacked;
            this.regionShapes = {};
            this.barWidth = barWidth;
            this.barSpacing = barSpacing;
            this.totalBarWidth = barWidth + barSpacing;
            this.width = width = (values.length * barWidth) + ((values.length - 1) * barSpacing);

            this.initTarget();

            if (chartRangeClip) {
                clipMin = chartRangeMin === undefined ? -Infinity : chartRangeMin;
                clipMax = chartRangeMax === undefined ? Infinity : chartRangeMax;
            }

            numValues = [];
            stackRanges = stacked ? [] : numValues;
            var stackTotals = [];
            var stackRangesNeg = [];
            for (i = 0, vlen = values.length; i < vlen; i++) {
                if (stacked) {
                    vlist = values[i];
                    values[i] = svals = [];
                    stackTotals[i] = 0;
                    stackRanges[i] = stackRangesNeg[i] = 0;
                    for (j = 0, slen = vlist.length; j < slen; j++) {
                        val = svals[j] = chartRangeClip ? clipval(vlist[j], clipMin, clipMax) : vlist[j];
                        if (val !== null) {
                            if (val > 0) {
                                stackTotals[i] += val;
                            }
                            if (stackMin < 0 && stackMax > 0) {
                                if (val < 0) {
                                    stackRangesNeg[i] += Math.abs(val);
                                } else {
                                    stackRanges[i] += val;
                                }
                            } else {
                                stackRanges[i] += Math.abs(val - (val < 0 ? stackMax : stackMin));
                            }
                            numValues.push(val);
                        }
                    }
                } else {
                    val = chartRangeClip ? clipval(values[i], clipMin, clipMax) : values[i];
                    val = values[i] = normalizeValue(val);
                    if (val !== null) {
                        numValues.push(val);
                    }
                }
            }
            this.max = max = Math.max.apply(Math, numValues);
            this.min = min = Math.min.apply(Math, numValues);
            this.stackMax = stackMax = stacked ? Math.max.apply(Math, stackTotals) : max;
            this.stackMin = stackMin = stacked ? Math.min.apply(Math, numValues) : min;

            if (options.get('chartRangeMin') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMin') < min)) {
                min = options.get('chartRangeMin');
            }
            if (options.get('chartRangeMax') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMax') > max)) {
                max = options.get('chartRangeMax');
            }

            this.zeroAxis = zeroAxis = options.get('zeroAxis', true);
            if (min <= 0 && max >= 0 && zeroAxis) {
                xaxisOffset = 0;
            } else if (zeroAxis == false) {
                xaxisOffset = min;
            } else if (min > 0) {
                xaxisOffset = min;
            } else {
                xaxisOffset = max;
            }
            this.xaxisOffset = xaxisOffset;

            range = stacked ? (Math.max.apply(Math, stackRanges) + Math.max.apply(Math, stackRangesNeg)) : max - min;

            // as we plot zero/min values a single pixel line, we add a pixel to all other
            // values - Reduce the effective canvas size to suit
            this.canvasHeightEf = (zeroAxis && min < 0) ? this.canvasHeight - 2 : this.canvasHeight - 1;

            if (min < xaxisOffset) {
                yMaxCalc = (stacked && max >= 0) ? stackMax : max;
                yoffset = (yMaxCalc - xaxisOffset) / range * this.canvasHeight;
                if (yoffset !== Math.ceil(yoffset)) {
                    this.canvasHeightEf -= 2;
                    yoffset = Math.ceil(yoffset);
                }
            } else {
                yoffset = this.canvasHeight;
            }
            this.yoffset = yoffset;

            if ($.isArray(options.get('colorMap'))) {
                this.colorMapByIndex = options.get('colorMap');
                this.colorMapByValue = null;
            } else {
                this.colorMapByIndex = null;
                this.colorMapByValue = options.get('colorMap');
                if (this.colorMapByValue && this.colorMapByValue.get === undefined) {
                    this.colorMapByValue = new RangeMap(this.colorMapByValue);
                }
            }

            this.range = range;
        },

        getRegion: function (el, x, y) {
            var result = Math.floor(x / this.totalBarWidth);
            return (result < 0 || result >= this.values.length) ? undefined : result;
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion,
                values = ensureArray(this.values[currentRegion]),
                result = [],
                value, i;
            for (i = values.length; i--;) {
                value = values[i];
                result.push({
                    isNull: value === null,
                    value: value,
                    color: this.calcColor(i, value, currentRegion),
                    offset: currentRegion
                });
            }
            return result;
        },

        calcColor: function (stacknum, value, valuenum) {
            var colorMapByIndex = this.colorMapByIndex,
                colorMapByValue = this.colorMapByValue,
                options = this.options,
                color, newColor;
            if (this.stacked) {
                color = options.get('stackedBarColor');
            } else {
                color = (value < 0) ? options.get('negBarColor') : options.get('barColor');
            }
            if (value === 0 && options.get('zeroColor') !== undefined) {
                color = options.get('zeroColor');
            }
            if (colorMapByValue && (newColor = colorMapByValue.get(value))) {
                color = newColor;
            } else if (colorMapByIndex && colorMapByIndex.length > valuenum) {
                color = colorMapByIndex[valuenum];
            }
            return $.isArray(color) ? color[stacknum % color.length] : color;
        },

        /**
         * Render bar(s) for a region
         */
        renderRegion: function (valuenum, highlight) {
            var vals = this.values[valuenum],
                options = this.options,
                xaxisOffset = this.xaxisOffset,
                result = [],
                range = this.range,
                stacked = this.stacked,
                target = this.target,
                x = valuenum * this.totalBarWidth,
                canvasHeightEf = this.canvasHeightEf,
                yoffset = this.yoffset,
                y, height, color, isNull, yoffsetNeg, i, valcount, val, minPlotted, allMin;

            vals = $.isArray(vals) ? vals : [vals];
            valcount = vals.length;
            val = vals[0];
            isNull = all(null, vals);
            allMin = all(xaxisOffset, vals, true);

            if (isNull) {
                if (options.get('nullColor')) {
                    color = highlight ? options.get('nullColor') : this.calcHighlightColor(options.get('nullColor'), options);
                    y = (yoffset > 0) ? yoffset - 1 : yoffset;
                    return target.drawRect(x, y, this.barWidth - 1, 0, color, color);
                } else {
                    return undefined;
                }
            }
            yoffsetNeg = yoffset;
            for (i = 0; i < valcount; i++) {
                val = vals[i];

                if (stacked && val === xaxisOffset) {
                    if (!allMin || minPlotted) {
                        continue;
                    }
                    minPlotted = true;
                }

                if (range > 0) {
                    height = Math.floor(canvasHeightEf * ((Math.abs(val - xaxisOffset) / range))) + 1;
                } else {
                    height = 1;
                }
                if (val < xaxisOffset || (val === xaxisOffset && yoffset === 0)) {
                    y = yoffsetNeg;
                    yoffsetNeg += height;
                } else {
                    y = yoffset - height;
                    yoffset -= height;
                }
                color = this.calcColor(i, val, valuenum);
                if (highlight) {
                    color = this.calcHighlightColor(color, options);
                }
                result.push(target.drawRect(x, y, this.barWidth - 1, height - 1, color, color));
            }
            if (result.length === 1) {
                return result[0];
            }
            return result;
        }
    });

    /**
     * Tristate charts
     */
    $.fn.sparkline.tristate = tristate = createClass($.fn.sparkline._base, barHighlightMixin, {
        type: 'tristate',

        init: function (el, values, options, width, height) {
            var barWidth = parseInt(options.get('barWidth'), 10),
                barSpacing = parseInt(options.get('barSpacing'), 10);
            tristate._super.init.call(this, el, values, options, width, height);

            this.regionShapes = {};
            this.barWidth = barWidth;
            this.barSpacing = barSpacing;
            this.totalBarWidth = barWidth + barSpacing;
            this.values = $.map(values, Number);
            this.width = width = (values.length * barWidth) + ((values.length - 1) * barSpacing);

            if ($.isArray(options.get('colorMap'))) {
                this.colorMapByIndex = options.get('colorMap');
                this.colorMapByValue = null;
            } else {
                this.colorMapByIndex = null;
                this.colorMapByValue = options.get('colorMap');
                if (this.colorMapByValue && this.colorMapByValue.get === undefined) {
                    this.colorMapByValue = new RangeMap(this.colorMapByValue);
                }
            }
            this.initTarget();
        },

        getRegion: function (el, x, y) {
            return Math.floor(x / this.totalBarWidth);
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                isNull: this.values[currentRegion] === undefined,
                value: this.values[currentRegion],
                color: this.calcColor(this.values[currentRegion], currentRegion),
                offset: currentRegion
            };
        },

        calcColor: function (value, valuenum) {
            var values = this.values,
                options = this.options,
                colorMapByIndex = this.colorMapByIndex,
                colorMapByValue = this.colorMapByValue,
                color, newColor;

            if (colorMapByValue && (newColor = colorMapByValue.get(value))) {
                color = newColor;
            } else if (colorMapByIndex && colorMapByIndex.length > valuenum) {
                color = colorMapByIndex[valuenum];
            } else if (values[valuenum] < 0) {
                color = options.get('negBarColor');
            } else if (values[valuenum] > 0) {
                color = options.get('posBarColor');
            } else {
                color = options.get('zeroBarColor');
            }
            return color;
        },

        renderRegion: function (valuenum, highlight) {
            var values = this.values,
                options = this.options,
                target = this.target,
                canvasHeight, height, halfHeight,
                x, y, color;

            canvasHeight = target.pixelHeight;
            halfHeight = Math.round(canvasHeight / 2);

            x = valuenum * this.totalBarWidth;
            if (values[valuenum] < 0) {
                y = halfHeight;
                height = halfHeight - 1;
            } else if (values[valuenum] > 0) {
                y = 0;
                height = halfHeight - 1;
            } else {
                y = halfHeight - 1;
                height = 2;
            }
            color = this.calcColor(values[valuenum], valuenum);
            if (color === null) {
                return;
            }
            if (highlight) {
                color = this.calcHighlightColor(color, options);
            }
            return target.drawRect(x, y, this.barWidth - 1, height - 1, color, color);
        }
    });

    /**
     * Discrete charts
     */
    $.fn.sparkline.discrete = discrete = createClass($.fn.sparkline._base, barHighlightMixin, {
        type: 'discrete',

        init: function (el, values, options, width, height) {
            discrete._super.init.call(this, el, values, options, width, height);

            this.regionShapes = {};
            this.values = values = $.map(values, Number);
            this.min = Math.min.apply(Math, values);
            this.max = Math.max.apply(Math, values);
            this.range = this.max - this.min;
            this.width = width = options.get('width') === 'auto' ? values.length * 2 : this.width;
            this.interval = Math.floor(width / values.length);
            this.itemWidth = width / values.length;
            if (options.get('chartRangeMin') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMin') < this.min)) {
                this.min = options.get('chartRangeMin');
            }
            if (options.get('chartRangeMax') !== undefined && (options.get('chartRangeClip') || options.get('chartRangeMax') > this.max)) {
                this.max = options.get('chartRangeMax');
            }
            this.initTarget();
            if (this.target) {
                this.lineHeight = options.get('lineHeight') === 'auto' ? Math.round(this.canvasHeight * 0.3) : options.get('lineHeight');
            }
        },

        getRegion: function (el, x, y) {
            return Math.floor(x / this.itemWidth);
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                isNull: this.values[currentRegion] === undefined,
                value: this.values[currentRegion],
                offset: currentRegion
            };
        },

        renderRegion: function (valuenum, highlight) {
            var values = this.values,
                options = this.options,
                min = this.min,
                max = this.max,
                range = this.range,
                interval = this.interval,
                target = this.target,
                canvasHeight = this.canvasHeight,
                lineHeight = this.lineHeight,
                pheight = canvasHeight - lineHeight,
                ytop, val, color, x;

            val = clipval(values[valuenum], min, max);
            x = valuenum * interval;
            ytop = Math.round(pheight - pheight * ((val - min) / range));
            color = (options.get('thresholdColor') && val < options.get('thresholdValue')) ? options.get('thresholdColor') : options.get('lineColor');
            if (highlight) {
                color = this.calcHighlightColor(color, options);
            }
            return target.drawLine(x, ytop, x, ytop + lineHeight, color);
        }
    });

    /**
     * Bullet charts
     */
    $.fn.sparkline.bullet = bullet = createClass($.fn.sparkline._base, {
        type: 'bullet',

        init: function (el, values, options, width, height) {
            var min, max, vals;
            bullet._super.init.call(this, el, values, options, width, height);

            // values: target, performance, range1, range2, range3
            this.values = values = normalizeValues(values);
            // target or performance could be null
            vals = values.slice();
            vals[0] = vals[0] === null ? vals[2] : vals[0];
            vals[1] = values[1] === null ? vals[2] : vals[1];
            min = Math.min.apply(Math, values);
            max = Math.max.apply(Math, values);
            if (options.get('base') === undefined) {
                min = min < 0 ? min : 0;
            } else {
                min = options.get('base');
            }
            this.min = min;
            this.max = max;
            this.range = max - min;
            this.shapes = {};
            this.valueShapes = {};
            this.regiondata = {};
            this.width = width = options.get('width') === 'auto' ? '4.0em' : width;
            this.target = this.$el.simpledraw(width, height, options.get('composite'));
            if (!values.length) {
                this.disabled = true;
            }
            this.initTarget();
        },

        getRegion: function (el, x, y) {
            var shapeid = this.target.getShapeAt(el, x, y);
            return (shapeid !== undefined && this.shapes[shapeid] !== undefined) ? this.shapes[shapeid] : undefined;
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                fieldkey: currentRegion.substr(0, 1),
                value: this.values[currentRegion.substr(1)],
                region: currentRegion
            };
        },

        changeHighlight: function (highlight) {
            var currentRegion = this.currentRegion,
                shapeid = this.valueShapes[currentRegion],
                shape;
            delete this.shapes[shapeid];
            switch (currentRegion.substr(0, 1)) {
                case 'r':
                    shape = this.renderRange(currentRegion.substr(1), highlight);
                    break;
                case 'p':
                    shape = this.renderPerformance(highlight);
                    break;
                case 't':
                    shape = this.renderTarget(highlight);
                    break;
            }
            this.valueShapes[currentRegion] = shape.id;
            this.shapes[shape.id] = currentRegion;
            this.target.replaceWithShape(shapeid, shape);
        },

        renderRange: function (rn, highlight) {
            var rangeval = this.values[rn],
                rangewidth = Math.round(this.canvasWidth * ((rangeval - this.min) / this.range)),
                color = this.options.get('rangeColors')[rn - 2];
            if (highlight) {
                color = this.calcHighlightColor(color, this.options);
            }
            return this.target.drawRect(0, 0, rangewidth - 1, this.canvasHeight - 1, color, color);
        },

        renderPerformance: function (highlight) {
            var perfval = this.values[1],
                perfwidth = Math.round(this.canvasWidth * ((perfval - this.min) / this.range)),
                color = this.options.get('performanceColor');
            if (highlight) {
                color = this.calcHighlightColor(color, this.options);
            }
            return this.target.drawRect(0, Math.round(this.canvasHeight * 0.3), perfwidth - 1,
                Math.round(this.canvasHeight * 0.4) - 1, color, color);
        },

        renderTarget: function (highlight) {
            var targetval = this.values[0],
                x = Math.round(this.canvasWidth * ((targetval - this.min) / this.range) - (this.options.get('targetWidth') / 2)),
                targettop = Math.round(this.canvasHeight * 0.10),
                targetheight = this.canvasHeight - (targettop * 2),
                color = this.options.get('targetColor');
            if (highlight) {
                color = this.calcHighlightColor(color, this.options);
            }
            return this.target.drawRect(x, targettop, this.options.get('targetWidth') - 1, targetheight - 1, color, color);
        },

        render: function () {
            var vlen = this.values.length,
                target = this.target,
                i, shape;
            if (!bullet._super.render.call(this)) {
                return;
            }
            for (i = 2; i < vlen; i++) {
                shape = this.renderRange(i).append();
                this.shapes[shape.id] = 'r' + i;
                this.valueShapes['r' + i] = shape.id;
            }
            if (this.values[1] !== null) {
                shape = this.renderPerformance().append();
                this.shapes[shape.id] = 'p1';
                this.valueShapes.p1 = shape.id;
            }
            if (this.values[0] !== null) {
                shape = this.renderTarget().append();
                this.shapes[shape.id] = 't0';
                this.valueShapes.t0 = shape.id;
            }
            target.render();
        }
    });

    /**
     * Pie charts
     */
    $.fn.sparkline.pie = pie = createClass($.fn.sparkline._base, {
        type: 'pie',

        init: function (el, values, options, width, height) {
            var total = 0, i;

            pie._super.init.call(this, el, values, options, width, height);

            this.shapes = {}; // map shape ids to value offsets
            this.valueShapes = {}; // maps value offsets to shape ids
            this.values = values = $.map(values, Number);

            if (options.get('width') === 'auto') {
                this.width = this.height;
            }

            if (values.length > 0) {
                for (i = values.length; i--;) {
                    total += values[i];
                }
            }
            this.total = total;
            this.initTarget();
            this.radius = Math.floor(Math.min(this.canvasWidth, this.canvasHeight) / 2);
        },

        getRegion: function (el, x, y) {
            var shapeid = this.target.getShapeAt(el, x, y);
            return (shapeid !== undefined && this.shapes[shapeid] !== undefined) ? this.shapes[shapeid] : undefined;
        },

        getCurrentRegionFields: function () {
            var currentRegion = this.currentRegion;
            return {
                isNull: this.values[currentRegion] === undefined,
                value: this.values[currentRegion],
                percent: this.values[currentRegion] / this.total * 100,
                color: this.options.get('sliceColors')[currentRegion % this.options.get('sliceColors').length],
                offset: currentRegion
            };
        },

        changeHighlight: function (highlight) {
            var currentRegion = this.currentRegion,
                newslice = this.renderSlice(currentRegion, highlight),
                shapeid = this.valueShapes[currentRegion];
            delete this.shapes[shapeid];
            this.target.replaceWithShape(shapeid, newslice);
            this.valueShapes[currentRegion] = newslice.id;
            this.shapes[newslice.id] = currentRegion;
        },

        renderSlice: function (valuenum, highlight) {
            var target = this.target,
                options = this.options,
                radius = this.radius,
                borderWidth = options.get('borderWidth'),
                offset = options.get('offset'),
                circle = 2 * Math.PI,
                values = this.values,
                total = this.total,
                next = offset ? (2*Math.PI)*(offset/360) : 0,
                start, end, i, vlen, color;

            vlen = values.length;
            for (i = 0; i < vlen; i++) {
                start = next;
                end = next;
                if (total > 0) {  // avoid divide by zero
                    end = next + (circle * (values[i] / total));
                }
                if (valuenum === i) {
                    color = options.get('sliceColors')[i % options.get('sliceColors').length];
                    if (highlight) {
                        color = this.calcHighlightColor(color, options);
                    }

                    return target.drawPieSlice(radius, radius, radius - borderWidth, start, end, undefined, color);
                }
                next = end;
            }
        },

        render: function () {
            var target = this.target,
                values = this.values,
                options = this.options,
                radius = this.radius,
                borderWidth = options.get('borderWidth'),
                shape, i;

            if (!pie._super.render.call(this)) {
                return;
            }
            if (borderWidth) {
                target.drawCircle(radius, radius, Math.floor(radius - (borderWidth / 2)),
                    options.get('borderColor'), undefined, borderWidth).append();
            }
            for (i = values.length; i--;) {
                if (values[i]) { // don't render zero values
                    shape = this.renderSlice(i).append();
                    this.valueShapes[i] = shape.id; // store just the shapeid
                    this.shapes[shape.id] = i;
                }
            }
            target.render();
        }
    });

    /**
     * Box plots
     */
    $.fn.sparkline.box = box = createClass($.fn.sparkline._base, {
        type: 'box',

        init: function (el, values, options, width, height) {
            box._super.init.call(this, el, values, options, width, height);
            this.values = $.map(values, Number);
            this.width = options.get('width') === 'auto' ? '4.0em' : width;
            this.initTarget();
            if (!this.values.length) {
                this.disabled = 1;
            }
        },

        /**
         * Simulate a single region
         */
        getRegion: function () {
            return 1;
        },

        getCurrentRegionFields: function () {
            var result = [
                { field: 'lq', value: this.quartiles[0] },
                { field: 'med', value: this.quartiles[1] },
                { field: 'uq', value: this.quartiles[2] }
            ];
            if (this.loutlier !== undefined) {
                result.push({ field: 'lo', value: this.loutlier});
            }
            if (this.routlier !== undefined) {
                result.push({ field: 'ro', value: this.routlier});
            }
            if (this.lwhisker !== undefined) {
                result.push({ field: 'lw', value: this.lwhisker});
            }
            if (this.rwhisker !== undefined) {
                result.push({ field: 'rw', value: this.rwhisker});
            }
            return result;
        },

        render: function () {
            var target = this.target,
                values = this.values,
                vlen = values.length,
                options = this.options,
                canvasWidth = this.canvasWidth,
                canvasHeight = this.canvasHeight,
                minValue = options.get('chartRangeMin') === undefined ? Math.min.apply(Math, values) : options.get('chartRangeMin'),
                maxValue = options.get('chartRangeMax') === undefined ? Math.max.apply(Math, values) : options.get('chartRangeMax'),
                canvasLeft = 0,
                lwhisker, loutlier, iqr, q1, q2, q3, rwhisker, routlier, i,
                size, unitSize;

            if (!box._super.render.call(this)) {
                return;
            }

            if (options.get('raw')) {
                if (options.get('showOutliers') && values.length > 5) {
                    loutlier = values[0];
                    lwhisker = values[1];
                    q1 = values[2];
                    q2 = values[3];
                    q3 = values[4];
                    rwhisker = values[5];
                    routlier = values[6];
                } else {
                    lwhisker = values[0];
                    q1 = values[1];
                    q2 = values[2];
                    q3 = values[3];
                    rwhisker = values[4];
                }
            } else {
                values.sort(function (a, b) { return a - b; });
                q1 = quartile(values, 1);
                q2 = quartile(values, 2);
                q3 = quartile(values, 3);
                iqr = q3 - q1;
                if (options.get('showOutliers')) {
                    lwhisker = rwhisker = undefined;
                    for (i = 0; i < vlen; i++) {
                        if (lwhisker === undefined && values[i] > q1 - (iqr * options.get('outlierIQR'))) {
                            lwhisker = values[i];
                        }
                        if (values[i] < q3 + (iqr * options.get('outlierIQR'))) {
                            rwhisker = values[i];
                        }
                    }
                    loutlier = values[0];
                    routlier = values[vlen - 1];
                } else {
                    lwhisker = values[0];
                    rwhisker = values[vlen - 1];
                }
            }
            this.quartiles = [q1, q2, q3];
            this.lwhisker = lwhisker;
            this.rwhisker = rwhisker;
            this.loutlier = loutlier;
            this.routlier = routlier;

            unitSize = canvasWidth / (maxValue - minValue + 1);
            if (options.get('showOutliers')) {
                canvasLeft = Math.ceil(options.get('spotRadius'));
                canvasWidth -= 2 * Math.ceil(options.get('spotRadius'));
                unitSize = canvasWidth / (maxValue - minValue + 1);
                if (loutlier < lwhisker) {
                    target.drawCircle((loutlier - minValue) * unitSize + canvasLeft,
                        canvasHeight / 2,
                        options.get('spotRadius'),
                        options.get('outlierLineColor'),
                        options.get('outlierFillColor')).append();
                }
                if (routlier > rwhisker) {
                    target.drawCircle((routlier - minValue) * unitSize + canvasLeft,
                        canvasHeight / 2,
                        options.get('spotRadius'),
                        options.get('outlierLineColor'),
                        options.get('outlierFillColor')).append();
                }
            }

            // box
            target.drawRect(
                Math.round((q1 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight * 0.1),
                Math.round((q3 - q1) * unitSize),
                Math.round(canvasHeight * 0.8),
                options.get('boxLineColor'),
                options.get('boxFillColor')).append();
            // left whisker
            target.drawLine(
                Math.round((lwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 2),
                Math.round((q1 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 2),
                options.get('lineColor')).append();
            target.drawLine(
                Math.round((lwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 4),
                Math.round((lwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight - canvasHeight / 4),
                options.get('whiskerColor')).append();
            // right whisker
            target.drawLine(Math.round((rwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 2),
                Math.round((q3 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 2),
                options.get('lineColor')).append();
            target.drawLine(
                Math.round((rwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight / 4),
                Math.round((rwhisker - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight - canvasHeight / 4),
                options.get('whiskerColor')).append();
            // median line
            target.drawLine(
                Math.round((q2 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight * 0.1),
                Math.round((q2 - minValue) * unitSize + canvasLeft),
                Math.round(canvasHeight * 0.9),
                options.get('medianColor')).append();
            if (options.get('target')) {
                size = Math.ceil(options.get('spotRadius'));
                target.drawLine(
                    Math.round((options.get('target') - minValue) * unitSize + canvasLeft),
                    Math.round((canvasHeight / 2) - size),
                    Math.round((options.get('target') - minValue) * unitSize + canvasLeft),
                    Math.round((canvasHeight / 2) + size),
                    options.get('targetColor')).append();
                target.drawLine(
                    Math.round((options.get('target') - minValue) * unitSize + canvasLeft - size),
                    Math.round(canvasHeight / 2),
                    Math.round((options.get('target') - minValue) * unitSize + canvasLeft + size),
                    Math.round(canvasHeight / 2),
                    options.get('targetColor')).append();
            }
            target.render();
        }
    });

    // Setup a very simple "virtual canvas" to make drawing the few shapes we need easier
    // This is accessible as $(foo).simpledraw()

    // Detect browser renderer support
    (function() {
        if (document.namespaces && !document.namespaces.v) {
            $.fn.sparkline.hasVML = true;
            document.namespaces.add('v', 'urn:schemas-microsoft-com:vml', '#default#VML');
        } else {
            $.fn.sparkline.hasVML = false;
        }

        var el = document.createElement('canvas');
        $.fn.sparkline.hasCanvas = !!(el.getContext && el.getContext('2d'));

    })()

    VShape = createClass({
        init: function (target, id, type, args) {
            this.target = target;
            this.id = id;
            this.type = type;
            this.args = args;
        },
        append: function () {
            this.target.appendShape(this);
            return this;
        }
    });

    VCanvas_base = createClass({
        _pxregex: /(\d+)(px)?\s*$/i,

        init: function (width, height, target) {
            if (!width) {
                return;
            }
            this.width = width;
            this.height = height;
            this.target = target;
            this.lastShapeId = null;
            if (target[0]) {
                target = target[0];
            }
            $.data(target, '_jqs_vcanvas', this);
        },

        drawLine: function (x1, y1, x2, y2, lineColor, lineWidth) {
            return this.drawShape([[x1, y1], [x2, y2]], lineColor, lineWidth);
        },

        drawShape: function (path, lineColor, fillColor, lineWidth) {
            return this._genShape('Shape', [path, lineColor, fillColor, lineWidth]);
        },

        drawCircle: function (x, y, radius, lineColor, fillColor, lineWidth) {
            return this._genShape('Circle', [x, y, radius, lineColor, fillColor, lineWidth]);
        },

        drawPieSlice: function (x, y, radius, startAngle, endAngle, lineColor, fillColor) {
            return this._genShape('PieSlice', [x, y, radius, startAngle, endAngle, lineColor, fillColor]);
        },

        drawRect: function (x, y, width, height, lineColor, fillColor) {
            return this._genShape('Rect', [x, y, width, height, lineColor, fillColor]);
        },

        getElement: function () {
            return this.canvas;
        },

        /**
         * Return the most recently inserted shape id
         */
        getLastShapeId: function () {
            return this.lastShapeId;
        },

        /**
         * Clear and reset the canvas
         */
        reset: function () {
            alert('reset not implemented');
        },

        _insert: function (el, target) {
            $(target).html(el);
        },

        /**
         * Calculate the pixel dimensions of the canvas
         */
        _calculatePixelDims: function (width, height, canvas) {
            // XXX This should probably be a configurable option
            var match;
            match = this._pxregex.exec(height);
            if (match) {
                this.pixelHeight = match[1];
            } else {
                this.pixelHeight = $(canvas).height();
            }
            match = this._pxregex.exec(width);
            if (match) {
                this.pixelWidth = match[1];
            } else {
                this.pixelWidth = $(canvas).width();
            }
        },

        /**
         * Generate a shape object and id for later rendering
         */
        _genShape: function (shapetype, shapeargs) {
            var id = shapeCount++;
            shapeargs.unshift(id);
            return new VShape(this, id, shapetype, shapeargs);
        },

        /**
         * Add a shape to the end of the render queue
         */
        appendShape: function (shape) {
            alert('appendShape not implemented');
        },

        /**
         * Replace one shape with another
         */
        replaceWithShape: function (shapeid, shape) {
            alert('replaceWithShape not implemented');
        },

        /**
         * Insert one shape after another in the render queue
         */
        insertAfterShape: function (shapeid, shape) {
            alert('insertAfterShape not implemented');
        },

        /**
         * Remove a shape from the queue
         */
        removeShapeId: function (shapeid) {
            alert('removeShapeId not implemented');
        },

        /**
         * Find a shape at the specified x/y co-ordinates
         */
        getShapeAt: function (el, x, y) {
            alert('getShapeAt not implemented');
        },

        /**
         * Render all queued shapes onto the canvas
         */
        render: function () {
            alert('render not implemented');
        }
    });

    VCanvas_canvas = createClass(VCanvas_base, {
        init: function (width, height, target, interact) {
            VCanvas_canvas._super.init.call(this, width, height, target);
            this.canvas = document.createElement('canvas');
            if (target[0]) {
                target = target[0];
            }
            $.data(target, '_jqs_vcanvas', this);
            $(this.canvas).css({ display: 'inline-block', width: width, height: height, verticalAlign: 'top' });
            this._insert(this.canvas, target);
            this._calculatePixelDims(width, height, this.canvas);
            this.canvas.width = this.pixelWidth;
            this.canvas.height = this.pixelHeight;
            this.interact = interact;
            this.shapes = {};
            this.shapeseq = [];
            this.currentTargetShapeId = undefined;
            $(this.canvas).css({width: this.pixelWidth, height: this.pixelHeight});
        },

        _getContext: function (lineColor, fillColor, lineWidth) {
            var context = this.canvas.getContext('2d');
            if (lineColor !== undefined) {
                context.strokeStyle = lineColor;
            }
            context.lineWidth = lineWidth === undefined ? 1 : lineWidth;
            if (fillColor !== undefined) {
                context.fillStyle = fillColor;
            }
            return context;
        },

        reset: function () {
            var context = this._getContext();
            context.clearRect(0, 0, this.pixelWidth, this.pixelHeight);
            this.shapes = {};
            this.shapeseq = [];
            this.currentTargetShapeId = undefined;
        },

        _drawShape: function (shapeid, path, lineColor, fillColor, lineWidth) {
            var context = this._getContext(lineColor, fillColor, lineWidth),
                i, plen;
            context.beginPath();
            context.moveTo(path[0][0] + 0.5, path[0][1] + 0.5);
            for (i = 1, plen = path.length; i < plen; i++) {
                context.lineTo(path[i][0] + 0.5, path[i][1] + 0.5); // the 0.5 offset gives us crisp pixel-width lines
            }
            if (lineColor !== undefined) {
                context.stroke();
            }
            if (fillColor !== undefined) {
                context.fill();
            }
            if (this.targetX !== undefined && this.targetY !== undefined &&
                context.isPointInPath(this.targetX, this.targetY)) {
                this.currentTargetShapeId = shapeid;
            }
        },

        _drawCircle: function (shapeid, x, y, radius, lineColor, fillColor, lineWidth) {
            var context = this._getContext(lineColor, fillColor, lineWidth);
            context.beginPath();
            context.arc(x, y, radius, 0, 2 * Math.PI, false);
            if (this.targetX !== undefined && this.targetY !== undefined &&
                context.isPointInPath(this.targetX, this.targetY)) {
                this.currentTargetShapeId = shapeid;
            }
            if (lineColor !== undefined) {
                context.stroke();
            }
            if (fillColor !== undefined) {
                context.fill();
            }
        },

        _drawPieSlice: function (shapeid, x, y, radius, startAngle, endAngle, lineColor, fillColor) {
            var context = this._getContext(lineColor, fillColor);
            context.beginPath();
            context.moveTo(x, y);
            context.arc(x, y, radius, startAngle, endAngle, false);
            context.lineTo(x, y);
            context.closePath();
            if (lineColor !== undefined) {
                context.stroke();
            }
            if (fillColor) {
                context.fill();
            }
            if (this.targetX !== undefined && this.targetY !== undefined &&
                context.isPointInPath(this.targetX, this.targetY)) {
                this.currentTargetShapeId = shapeid;
            }
        },

        _drawRect: function (shapeid, x, y, width, height, lineColor, fillColor) {
            return this._drawShape(shapeid, [[x, y], [x + width, y], [x + width, y + height], [x, y + height], [x, y]], lineColor, fillColor);
        },

        appendShape: function (shape) {
            this.shapes[shape.id] = shape;
            this.shapeseq.push(shape.id);
            this.lastShapeId = shape.id;
            return shape.id;
        },

        replaceWithShape: function (shapeid, shape) {
            var shapeseq = this.shapeseq,
                i;
            this.shapes[shape.id] = shape;
            for (i = shapeseq.length; i--;) {
                if (shapeseq[i] == shapeid) {
                    shapeseq[i] = shape.id;
                }
            }
            delete this.shapes[shapeid];
        },

        replaceWithShapes: function (shapeids, shapes) {
            var shapeseq = this.shapeseq,
                shapemap = {},
                sid, i, first;

            for (i = shapeids.length; i--;) {
                shapemap[shapeids[i]] = true;
            }
            for (i = shapeseq.length; i--;) {
                sid = shapeseq[i];
                if (shapemap[sid]) {
                    shapeseq.splice(i, 1);
                    delete this.shapes[sid];
                    first = i;
                }
            }
            for (i = shapes.length; i--;) {
                shapeseq.splice(first, 0, shapes[i].id);
                this.shapes[shapes[i].id] = shapes[i];
            }

        },

        insertAfterShape: function (shapeid, shape) {
            var shapeseq = this.shapeseq,
                i;
            for (i = shapeseq.length; i--;) {
                if (shapeseq[i] === shapeid) {
                    shapeseq.splice(i + 1, 0, shape.id);
                    this.shapes[shape.id] = shape;
                    return;
                }
            }
        },

        removeShapeId: function (shapeid) {
            var shapeseq = this.shapeseq,
                i;
            for (i = shapeseq.length; i--;) {
                if (shapeseq[i] === shapeid) {
                    shapeseq.splice(i, 1);
                    break;
                }
            }
            delete this.shapes[shapeid];
        },

        getShapeAt: function (el, x, y) {
            this.targetX = x;
            this.targetY = y;
            this.render();
            return this.currentTargetShapeId;
        },

        render: function () {
            var shapeseq = this.shapeseq,
                shapes = this.shapes,
                shapeCount = shapeseq.length,
                context = this._getContext(),
                shapeid, shape, i;
            context.clearRect(0, 0, this.pixelWidth, this.pixelHeight);
            for (i = 0; i < shapeCount; i++) {
                shapeid = shapeseq[i];
                shape = shapes[shapeid];
                this['_draw' + shape.type].apply(this, shape.args);
            }
            if (!this.interact) {
                // not interactive so no need to keep the shapes array
                this.shapes = {};
                this.shapeseq = [];
            }
        }

    });

    VCanvas_vml = createClass(VCanvas_base, {
        init: function (width, height, target) {
            var groupel;
            VCanvas_vml._super.init.call(this, width, height, target);
            if (target[0]) {
                target = target[0];
            }
            $.data(target, '_jqs_vcanvas', this);
            this.canvas = document.createElement('span');
            $(this.canvas).css({ display: 'inline-block', position: 'relative', overflow: 'hidden', width: width, height: height, margin: '0px', padding: '0px', verticalAlign: 'top'});
            this._insert(this.canvas, target);
            this._calculatePixelDims(width, height, this.canvas);
            this.canvas.width = this.pixelWidth;
            this.canvas.height = this.pixelHeight;
            groupel = '<v:group coordorigin="0 0" coordsize="' + this.pixelWidth + ' ' + this.pixelHeight + '"' +
                ' style="position:absolute;top:0;left:0;width:' + this.pixelWidth + 'px;height=' + this.pixelHeight + 'px;"></v:group>';
            this.canvas.insertAdjacentHTML('beforeEnd', groupel);
            this.group = $(this.canvas).children()[0];
            this.rendered = false;
            this.prerender = '';
        },

        _drawShape: function (shapeid, path, lineColor, fillColor, lineWidth) {
            var vpath = [],
                initial, stroke, fill, closed, vel, plen, i;
            for (i = 0, plen = path.length; i < plen; i++) {
                vpath[i] = '' + (path[i][0]) + ',' + (path[i][1]);
            }
            initial = vpath.splice(0, 1);
            lineWidth = lineWidth === undefined ? 1 : lineWidth;
            stroke = lineColor === undefined ? ' stroked="false" ' : ' strokeWeight="' + lineWidth + 'px" strokeColor="' + lineColor + '" ';
            fill = fillColor === undefined ? ' filled="false"' : ' fillColor="' + fillColor + '" filled="true" ';
            closed = vpath[0] === vpath[vpath.length - 1] ? 'x ' : '';
            vel = '<v:shape coordorigin="0 0" coordsize="' + this.pixelWidth + ' ' + this.pixelHeight + '" ' +
                ' id="jqsshape' + shapeid + '" ' +
                stroke +
                fill +
                ' style="position:absolute;left:0px;top:0px;height:' + this.pixelHeight + 'px;width:' + this.pixelWidth + 'px;padding:0px;margin:0px;" ' +
                ' path="m ' + initial + ' l ' + vpath.join(', ') + ' ' + closed + 'e">' +
                ' </v:shape>';
            return vel;
        },

        _drawCircle: function (shapeid, x, y, radius, lineColor, fillColor, lineWidth) {
            var stroke, fill, vel;
            x -= radius;
            y -= radius;
            stroke = lineColor === undefined ? ' stroked="false" ' : ' strokeWeight="' + lineWidth + 'px" strokeColor="' + lineColor + '" ';
            fill = fillColor === undefined ? ' filled="false"' : ' fillColor="' + fillColor + '" filled="true" ';
            vel = '<v:oval ' +
                ' id="jqsshape' + shapeid + '" ' +
                stroke +
                fill +
                ' style="position:absolute;top:' + y + 'px; left:' + x + 'px; width:' + (radius * 2) + 'px; height:' + (radius * 2) + 'px"></v:oval>';
            return vel;

        },

        _drawPieSlice: function (shapeid, x, y, radius, startAngle, endAngle, lineColor, fillColor) {
            var vpath, startx, starty, endx, endy, stroke, fill, vel;
            if (startAngle === endAngle) {
                return '';  // VML seems to have problem when start angle equals end angle.
            }
            if ((endAngle - startAngle) === (2 * Math.PI)) {
                startAngle = 0.0;  // VML seems to have a problem when drawing a full circle that doesn't start 0
                endAngle = (2 * Math.PI);
            }

            startx = x + Math.round(Math.cos(startAngle) * radius);
            starty = y + Math.round(Math.sin(startAngle) * radius);
            endx = x + Math.round(Math.cos(endAngle) * radius);
            endy = y + Math.round(Math.sin(endAngle) * radius);

            if (startx === endx && starty === endy) {
                if ((endAngle - startAngle) < Math.PI) {
                    // Prevent very small slices from being mistaken as a whole pie
                    return '';
                }
                // essentially going to be the entire circle, so ignore startAngle
                startx = endx = x + radius;
                starty = endy = y;
            }

            if (startx === endx && starty === endy && (endAngle - startAngle) < Math.PI) {
                return '';
            }

            vpath = [x - radius, y - radius, x + radius, y + radius, startx, starty, endx, endy];
            stroke = lineColor === undefined ? ' stroked="false" ' : ' strokeWeight="1px" strokeColor="' + lineColor + '" ';
            fill = fillColor === undefined ? ' filled="false"' : ' fillColor="' + fillColor + '" filled="true" ';
            vel = '<v:shape coordorigin="0 0" coordsize="' + this.pixelWidth + ' ' + this.pixelHeight + '" ' +
                ' id="jqsshape' + shapeid + '" ' +
                stroke +
                fill +
                ' style="position:absolute;left:0px;top:0px;height:' + this.pixelHeight + 'px;width:' + this.pixelWidth + 'px;padding:0px;margin:0px;" ' +
                ' path="m ' + x + ',' + y + ' wa ' + vpath.join(', ') + ' x e">' +
                ' </v:shape>';
            return vel;
        },

        _drawRect: function (shapeid, x, y, width, height, lineColor, fillColor) {
            return this._drawShape(shapeid, [[x, y], [x, y + height], [x + width, y + height], [x + width, y], [x, y]], lineColor, fillColor);
        },

        reset: function () {
            this.group.innerHTML = '';
        },

        appendShape: function (shape) {
            var vel = this['_draw' + shape.type].apply(this, shape.args);
            if (this.rendered) {
                this.group.insertAdjacentHTML('beforeEnd', vel);
            } else {
                this.prerender += vel;
            }
            this.lastShapeId = shape.id;
            return shape.id;
        },

        replaceWithShape: function (shapeid, shape) {
            var existing = $('#jqsshape' + shapeid),
                vel = this['_draw' + shape.type].apply(this, shape.args);
            existing[0].outerHTML = vel;
        },

        replaceWithShapes: function (shapeids, shapes) {
            // replace the first shapeid with all the new shapes then toast the remaining old shapes
            var existing = $('#jqsshape' + shapeids[0]),
                replace = '',
                slen = shapes.length,
                i;
            for (i = 0; i < slen; i++) {
                replace += this['_draw' + shapes[i].type].apply(this, shapes[i].args);
            }
            existing[0].outerHTML = replace;
            for (i = 1; i < shapeids.length; i++) {
                $('#jqsshape' + shapeids[i]).remove();
            }
        },

        insertAfterShape: function (shapeid, shape) {
            var existing = $('#jqsshape' + shapeid),
                vel = this['_draw' + shape.type].apply(this, shape.args);
            existing[0].insertAdjacentHTML('afterEnd', vel);
        },

        removeShapeId: function (shapeid) {
            var existing = $('#jqsshape' + shapeid);
            this.group.removeChild(existing[0]);
        },

        getShapeAt: function (el, x, y) {
            var shapeid = el.id.substr(8);
            return shapeid;
        },

        render: function () {
            if (!this.rendered) {
                // batch the intial render into a single repaint
                this.group.innerHTML = this.prerender;
                this.rendered = true;
            }
        }
    });

}));
/*!
 * Raphael 1.5.2 - JavaScript Vector Library
 *
 * Copyright (c) 2010 Dmitry Baranovskiy (http://raphaeljs.com)
 * Licensed under the MIT (http://raphaeljs.com/license.html) license.
 */
(function () {
    function R() {
        if (R.is(arguments[0], array)) {
            var a = arguments[0],
                cnv = create[apply](R, a.splice(0, 3 + R.is(a[0], nu))),
                res = cnv.set();
            for (var i = 0, ii = a[length]; i < ii; i++) {
                var j = a[i] || {};
                elements[has](j.type) && res[push](cnv[j.type]().attr(j));
            }
            return res;
        }
        return create[apply](R, arguments);
    }
    R.version = "1.5.2";
    var separator = /[, ]+/,
        elements = {circle: 1, rect: 1, path: 1, ellipse: 1, text: 1, image: 1},
        formatrg = /\{(\d+)\}/g,
        proto = "prototype",
        has = "hasOwnProperty",
        doc = document,
        win = window,
        oldRaphael = {
            was: Object[proto][has].call(win, "Raphael"),
            is: win.Raphael
        },
        Paper = function () {
            this.customAttributes = {};
        },
        paperproto,
        appendChild = "appendChild",
        apply = "apply",
        concat = "concat",
        supportsTouch = "createTouch" in doc,
        E = "",
        S = " ",
        Str = String,
        split = "split",
        events = "click dblclick mousedown mousemove mouseout mouseover mouseup touchstart touchmove touchend orientationchange touchcancel gesturestart gesturechange gestureend"[split](S),
        touchMap = {
            mousedown: "touchstart",
            mousemove: "touchmove",
            mouseup: "touchend"
        },
        join = "join",
        length = "length",
        lowerCase = Str[proto].toLowerCase,
        math = Math,
        mmax = math.max,
        mmin = math.min,
        abs = math.abs,
        pow = math.pow,
        PI = math.PI,
        nu = "number",
        string = "string",
        array = "array",
        toString = "toString",
        fillString = "fill",
        objectToString = Object[proto][toString],
        paper = {},
        push = "push",
        ISURL = /^url\(['"]?([^\)]+?)['"]?\)$/i,
        colourRegExp = /^\s*((#[a-f\d]{6})|(#[a-f\d]{3})|rgba?\(\s*([\d\.]+%?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?)%?\s*\)|hsba?\(\s*([\d\.]+(?:deg|\xb0|%)?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?)%?\s*\)|hsla?\(\s*([\d\.]+(?:deg|\xb0|%)?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?)%?\s*\))\s*$/i,
        isnan = {"NaN": 1, "Infinity": 1, "-Infinity": 1},
        bezierrg = /^(?:cubic-)?bezier\(([^,]+),([^,]+),([^,]+),([^\)]+)\)/,
        round = math.round,
        setAttribute = "setAttribute",
        toFloat = parseFloat,
        toInt = parseInt,
        ms = " progid:DXImageTransform.Microsoft",
        upperCase = Str[proto].toUpperCase,
        availableAttrs = {blur: 0, "clip-rect": "0 0 1e9 1e9", cursor: "default", cx: 0, cy: 0, fill: "#fff", "fill-opacity": 1, font: '10px "Arial"', "font-family": '"Arial"', "font-size": "10", "font-style": "normal", "font-weight": 400, gradient: 0, height: 0, href: "http://raphaeljs.com/", opacity: 1, path: "M0,0", r: 0, rotation: 0, rx: 0, ry: 0, scale: "1 1", src: "", stroke: "#000", "stroke-dasharray": "", "stroke-linecap": "butt", "stroke-linejoin": "butt", "stroke-miterlimit": 0, "stroke-opacity": 1, "stroke-width": 1, target: "_blank", "text-anchor": "middle", title: "Raphael", translation: "0 0", width: 0, x: 0, y: 0},
        availableAnimAttrs = {along: "along", blur: nu, "clip-rect": "csv", cx: nu, cy: nu, fill: "colour", "fill-opacity": nu, "font-size": nu, height: nu, opacity: nu, path: "path", r: nu, rotation: "csv", rx: nu, ry: nu, scale: "csv", stroke: "colour", "stroke-opacity": nu, "stroke-width": nu, translation: "csv", width: nu, x: nu, y: nu},
        rp = "replace",
        animKeyFrames= /^(from|to|\d+%?)$/,
        commaSpaces = /\s*,\s*/,
        hsrg = {hs: 1, rg: 1},
        p2s = /,?([achlmqrstvxz]),?/gi,
        pathCommand = /([achlmqstvz])[\s,]*((-?\d*\.?\d*(?:e[-+]?\d+)?\s*,?\s*)+)/ig,
        pathValues = /(-?\d*\.?\d*(?:e[-+]?\d+)?)\s*,?\s*/ig,
        radial_gradient = /^r(?:\(([^,]+?)\s*,\s*([^\)]+?)\))?/,
        sortByKey = function (a, b) {
            return a.key - b.key;
        };

    R.type = (win.SVGAngle || doc.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "1.1") ? "SVG" : "VML");
    if (R.type == "VML") {
        var d = doc.createElement("div"),
            b;
        d.innerHTML = '<v:shape adj="1"/>';
        b = d.firstChild;
        b.style.behavior = "url(#default#VML)";
        if (!(b && typeof b.adj == "object")) {
            return R.type = null;
        }
        d = null;
    }
    R.svg = !(R.vml = R.type == "VML");
    Paper[proto] = R[proto];
    paperproto = Paper[proto];
    R._id = 0;
    R._oid = 0;
    R.fn = {};
    R.is = function (o, type) {
        type = lowerCase.call(type);
        if (type == "finite") {
            return !isnan[has](+o);
        }
        return  (type == "null" && o === null) ||
            (type == typeof o) ||
            (type == "object" && o === Object(o)) ||
            (type == "array" && Array.isArray && Array.isArray(o)) ||
            objectToString.call(o).slice(8, -1).toLowerCase() == type;
    };
    R.angle = function (x1, y1, x2, y2, x3, y3) {
        if (x3 == null) {
            var x = x1 - x2,
                y = y1 - y2;
            if (!x && !y) {
                return 0;
            }
            return ((x < 0) * 180 + math.atan(-y / -x) * 180 / PI + 360) % 360;
        } else {
            return R.angle(x1, y1, x3, y3) - R.angle(x2, y2, x3, y3);
        }
    };
    R.rad = function (deg) {
        return deg % 360 * PI / 180;
    };
    R.deg = function (rad) {
        return rad * 180 / PI % 360;
    };
    R.snapTo = function (values, value, tolerance) {
        tolerance = R.is(tolerance, "finite") ? tolerance : 10;
        if (R.is(values, array)) {
            var i = values.length;
            while (i--) if (abs(values[i] - value) <= tolerance) {
                return values[i];
            }
        } else {
            values = +values;
            var rem = value % values;
            if (rem < tolerance) {
                return value - rem;
            }
            if (rem > values - tolerance) {
                return value - rem + values;
            }
        }
        return value;
    };
    function createUUID() {
        // http://www.ietf.org/rfc/rfc4122.txt
        var s = [],
            i = 0;
        for (; i < 32; i++) {
            s[i] = (~~(math.random() * 16))[toString](16);
        }
        s[12] = 4;  // bits 12-15 of the time_hi_and_version field to 0010
        s[16] = ((s[16] & 3) | 8)[toString](16);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
        return "r-" + s[join]("");
    }

    R.setWindow = function (newwin) {
        win = newwin;
        doc = win.document;
    };
    // colour utilities
    var toHex = function (color) {
            if (R.vml) {
                // http://dean.edwards.name/weblog/2009/10/convert-any-colour-value-to-hex-in-msie/
                var trim = /^\s+|\s+$/g;
                var bod;
                try {
                    var docum = new ActiveXObject("htmlfile");
                    docum.write("<body>");
                    docum.close();
                    bod = docum.body;
                } catch(e) {
                    bod = createPopup().document.body;
                }
                var range = bod.createTextRange();
                toHex = cacher(function (color) {
                    try {
                        bod.style.color = Str(color)[rp](trim, E);
                        var value = range.queryCommandValue("ForeColor");
                        value = ((value & 255) << 16) | (value & 65280) | ((value & 16711680) >>> 16);
                        return "#" + ("000000" + value[toString](16)).slice(-6);
                    } catch(e) {
                        return "none";
                    }
                });
            } else {
                var i = doc.createElement("i");
                i.title = "Rapha\xebl Colour Picker";
                i.style.display = "none";
                doc.body[appendChild](i);
                toHex = cacher(function (color) {
                    i.style.color = color;
                    return doc.defaultView.getComputedStyle(i, E).getPropertyValue("color");
                });
            }
            return toHex(color);
        },
        hsbtoString = function () {
            return "hsb(" + [this.h, this.s, this.b] + ")";
        },
        hsltoString = function () {
            return "hsl(" + [this.h, this.s, this.l] + ")";
        },
        rgbtoString = function () {
            return this.hex;
        };
    R.hsb2rgb = function (h, s, b, o) {
        if (R.is(h, "object") && "h" in h && "s" in h && "b" in h) {
            b = h.b;
            s = h.s;
            h = h.h;
            o = h.o;
        }
        return R.hsl2rgb(h, s, b / 2, o);
    };
    R.hsl2rgb = function (h, s, l, o) {
        if (R.is(h, "object") && "h" in h && "s" in h && "l" in h) {
            l = h.l;
            s = h.s;
            h = h.h;
        }
        if (h > 1 || s > 1 || l > 1) {
            h /= 360;
            s /= 100;
            l /= 100;
        }
        var rgb = {},
            channels = ["r", "g", "b"],
            t2, t1, t3, r, g, b;
        if (!s) {
            rgb = {
                r: l,
                g: l,
                b: l
            };
        } else {
            if (l < .5) {
                t2 = l * (1 + s);
            } else {
                t2 = l + s - l * s;
            }
            t1 = 2 * l - t2;
            for (var i = 0; i < 3; i++) {
                t3 = h + 1 / 3 * -(i - 1);
                t3 < 0 && t3++;
                t3 > 1 && t3--;
                if (t3 * 6 < 1) {
                    rgb[channels[i]] = t1 + (t2 - t1) * 6 * t3;
                } else if (t3 * 2 < 1) {
                    rgb[channels[i]] = t2;
                } else if (t3 * 3 < 2) {
                    rgb[channels[i]] = t1 + (t2 - t1) * (2 / 3 - t3) * 6;
                } else {
                    rgb[channels[i]] = t1;
                }
            }
        }
        rgb.r *= 255;
        rgb.g *= 255;
        rgb.b *= 255;
        rgb.hex = "#" + (16777216 | rgb.b | (rgb.g << 8) | (rgb.r << 16)).toString(16).slice(1);
        R.is(o, "finite") && (rgb.opacity = o);
        rgb.toString = rgbtoString;
        return rgb;
    };
    R.rgb2hsb = function (red, green, blue) {
        if (green == null && R.is(red, "object") && "r" in red && "g" in red && "b" in red) {
            blue = red.b;
            green = red.g;
            red = red.r;
        }
        if (green == null && R.is(red, string)) {
            var clr = R.getRGB(red);
            red = clr.r;
            green = clr.g;
            blue = clr.b;
        }
        if (red > 1 || green > 1 || blue > 1) {
            red /= 255;
            green /= 255;
            blue /= 255;
        }
        var max = mmax(red, green, blue),
            min = mmin(red, green, blue),
            hue,
            saturation,
            brightness = max;
        if (min == max) {
            return {h: 0, s: 0, b: max, toString: hsbtoString};
        } else {
            var delta = (max - min);
            saturation = delta / max;
            if (red == max) {
                hue = (green - blue) / delta;
            } else if (green == max) {
                hue = 2 + ((blue - red) / delta);
            } else {
                hue = 4 + ((red - green) / delta);
            }
            hue /= 6;
            hue < 0 && hue++;
            hue > 1 && hue--;
        }
        return {h: hue, s: saturation, b: brightness, toString: hsbtoString};
    };
    R.rgb2hsl = function (red, green, blue) {
        if (green == null && R.is(red, "object") && "r" in red && "g" in red && "b" in red) {
            blue = red.b;
            green = red.g;
            red = red.r;
        }
        if (green == null && R.is(red, string)) {
            var clr = R.getRGB(red);
            red = clr.r;
            green = clr.g;
            blue = clr.b;
        }
        if (red > 1 || green > 1 || blue > 1) {
            red /= 255;
            green /= 255;
            blue /= 255;
        }
        var max = mmax(red, green, blue),
            min = mmin(red, green, blue),
            h,
            s,
            l = (max + min) / 2,
            hsl;
        if (min == max) {
            hsl =  {h: 0, s: 0, l: l};
        } else {
            var delta = max - min;
            s = l < .5 ? delta / (max + min) : delta / (2 - max - min);
            if (red == max) {
                h = (green - blue) / delta;
            } else if (green == max) {
                h = 2 + (blue - red) / delta;
            } else {
                h = 4 + (red - green) / delta;
            }
            h /= 6;
            h < 0 && h++;
            h > 1 && h--;
            hsl = {h: h, s: s, l: l};
        }
        hsl.toString = hsltoString;
        return hsl;
    };
    R._path2string = function () {
        return this.join(",")[rp](p2s, "$1");
    };
    function cacher(f, scope, postprocessor) {
        function newf() {
            var arg = Array[proto].slice.call(arguments, 0),
                args = arg[join]("\u25ba"),
                cache = newf.cache = newf.cache || {},
                count = newf.count = newf.count || [];
            if (cache[has](args)) {
                return postprocessor ? postprocessor(cache[args]) : cache[args];
            }
            count[length] >= 1e3 && delete cache[count.shift()];
            count[push](args);
            cache[args] = f[apply](scope, arg);
            return postprocessor ? postprocessor(cache[args]) : cache[args];
        }
        return newf;
    }

    R.getRGB = cacher(function (colour) {
        if (!colour || !!((colour = Str(colour)).indexOf("-") + 1)) {
            return {r: -1, g: -1, b: -1, hex: "none", error: 1};
        }
        if (colour == "none") {
            return {r: -1, g: -1, b: -1, hex: "none"};
        }
        !(hsrg[has](colour.toLowerCase().substring(0, 2)) || colour.charAt() == "#") && (colour = toHex(colour));
        var res,
            red,
            green,
            blue,
            opacity,
            t,
            values,
            rgb = colour.match(colourRegExp);
        if (rgb) {
            if (rgb[2]) {
                blue = toInt(rgb[2].substring(5), 16);
                green = toInt(rgb[2].substring(3, 5), 16);
                red = toInt(rgb[2].substring(1, 3), 16);
            }
            if (rgb[3]) {
                blue = toInt((t = rgb[3].charAt(3)) + t, 16);
                green = toInt((t = rgb[3].charAt(2)) + t, 16);
                red = toInt((t = rgb[3].charAt(1)) + t, 16);
            }
            if (rgb[4]) {
                values = rgb[4][split](commaSpaces);
                red = toFloat(values[0]);
                values[0].slice(-1) == "%" && (red *= 2.55);
                green = toFloat(values[1]);
                values[1].slice(-1) == "%" && (green *= 2.55);
                blue = toFloat(values[2]);
                values[2].slice(-1) == "%" && (blue *= 2.55);
                rgb[1].toLowerCase().slice(0, 4) == "rgba" && (opacity = toFloat(values[3]));
                values[3] && values[3].slice(-1) == "%" && (opacity /= 100);
            }
            if (rgb[5]) {
                values = rgb[5][split](commaSpaces);
                red = toFloat(values[0]);
                values[0].slice(-1) == "%" && (red *= 2.55);
                green = toFloat(values[1]);
                values[1].slice(-1) == "%" && (green *= 2.55);
                blue = toFloat(values[2]);
                values[2].slice(-1) == "%" && (blue *= 2.55);
                (values[0].slice(-3) == "deg" || values[0].slice(-1) == "\xb0") && (red /= 360);
                rgb[1].toLowerCase().slice(0, 4) == "hsba" && (opacity = toFloat(values[3]));
                values[3] && values[3].slice(-1) == "%" && (opacity /= 100);
                return R.hsb2rgb(red, green, blue, opacity);
            }
            if (rgb[6]) {
                values = rgb[6][split](commaSpaces);
                red = toFloat(values[0]);
                values[0].slice(-1) == "%" && (red *= 2.55);
                green = toFloat(values[1]);
                values[1].slice(-1) == "%" && (green *= 2.55);
                blue = toFloat(values[2]);
                values[2].slice(-1) == "%" && (blue *= 2.55);
                (values[0].slice(-3) == "deg" || values[0].slice(-1) == "\xb0") && (red /= 360);
                rgb[1].toLowerCase().slice(0, 4) == "hsla" && (opacity = toFloat(values[3]));
                values[3] && values[3].slice(-1) == "%" && (opacity /= 100);
                return R.hsl2rgb(red, green, blue, opacity);
            }
            rgb = {r: red, g: green, b: blue};
            rgb.hex = "#" + (16777216 | blue | (green << 8) | (red << 16)).toString(16).slice(1);
            R.is(opacity, "finite") && (rgb.opacity = opacity);
            return rgb;
        }
        return {r: -1, g: -1, b: -1, hex: "none", error: 1};
    }, R);
    R.getColor = function (value) {
        var start = this.getColor.start = this.getColor.start || {h: 0, s: 1, b: value || .75},
            rgb = this.hsb2rgb(start.h, start.s, start.b);
        start.h += .075;
        if (start.h > 1) {
            start.h = 0;
            start.s -= .2;
            start.s <= 0 && (this.getColor.start = {h: 0, s: 1, b: start.b});
        }
        return rgb.hex;
    };
    R.getColor.reset = function () {
        delete this.start;
    };
    // path utilities
    R.parsePathString = cacher(function (pathString) {
        if (!pathString) {
            return null;
        }
        var paramCounts = {a: 7, c: 6, h: 1, l: 2, m: 2, q: 4, s: 4, t: 2, v: 1, z: 0},
            data = [];
        if (R.is(pathString, array) && R.is(pathString[0], array)) { // rough assumption
            data = pathClone(pathString);
        }
        if (!data[length]) {
            Str(pathString)[rp](pathCommand, function (a, b, c) {
                var params = [],
                    name = lowerCase.call(b);
                c[rp](pathValues, function (a, b) {
                    b && params[push](+b);
                });
                if (name == "m" && params[length] > 2) {
                    data[push]([b][concat](params.splice(0, 2)));
                    name = "l";
                    b = b == "m" ? "l" : "L";
                }
                while (params[length] >= paramCounts[name]) {
                    data[push]([b][concat](params.splice(0, paramCounts[name])));
                    if (!paramCounts[name]) {
                        break;
                    }
                }
            });
        }
        data[toString] = R._path2string;
        return data;
    });
    R.findDotsAtSegment = function (p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y, t) {
        var t1 = 1 - t,
            x = pow(t1, 3) * p1x + pow(t1, 2) * 3 * t * c1x + t1 * 3 * t * t * c2x + pow(t, 3) * p2x,
            y = pow(t1, 3) * p1y + pow(t1, 2) * 3 * t * c1y + t1 * 3 * t * t * c2y + pow(t, 3) * p2y,
            mx = p1x + 2 * t * (c1x - p1x) + t * t * (c2x - 2 * c1x + p1x),
            my = p1y + 2 * t * (c1y - p1y) + t * t * (c2y - 2 * c1y + p1y),
            nx = c1x + 2 * t * (c2x - c1x) + t * t * (p2x - 2 * c2x + c1x),
            ny = c1y + 2 * t * (c2y - c1y) + t * t * (p2y - 2 * c2y + c1y),
            ax = (1 - t) * p1x + t * c1x,
            ay = (1 - t) * p1y + t * c1y,
            cx = (1 - t) * c2x + t * p2x,
            cy = (1 - t) * c2y + t * p2y,
            alpha = (90 - math.atan((mx - nx) / (my - ny)) * 180 / PI);
        (mx > nx || my < ny) && (alpha += 180);
        return {x: x, y: y, m: {x: mx, y: my}, n: {x: nx, y: ny}, start: {x: ax, y: ay}, end: {x: cx, y: cy}, alpha: alpha};
    };
    var pathDimensions = cacher(function (path) {
            if (!path) {
                return {x: 0, y: 0, width: 0, height: 0};
            }
            path = path2curve(path);
            var x = 0,
                y = 0,
                X = [],
                Y = [],
                p;
            for (var i = 0, ii = path[length]; i < ii; i++) {
                p = path[i];
                if (p[0] == "M") {
                    x = p[1];
                    y = p[2];
                    X[push](x);
                    Y[push](y);
                } else {
                    var dim = curveDim(x, y, p[1], p[2], p[3], p[4], p[5], p[6]);
                    X = X[concat](dim.min.x, dim.max.x);
                    Y = Y[concat](dim.min.y, dim.max.y);
                    x = p[5];
                    y = p[6];
                }
            }
            var xmin = mmin[apply](0, X),
                ymin = mmin[apply](0, Y);
            return {
                x: xmin,
                y: ymin,
                width: mmax[apply](0, X) - xmin,
                height: mmax[apply](0, Y) - ymin
            };
        }),
        pathClone = function (pathArray) {
            var res = [];
            if (!R.is(pathArray, array) || !R.is(pathArray && pathArray[0], array)) { // rough assumption
                pathArray = R.parsePathString(pathArray);
            }
            for (var i = 0, ii = pathArray[length]; i < ii; i++) {
                res[i] = [];
                for (var j = 0, jj = pathArray[i][length]; j < jj; j++) {
                    res[i][j] = pathArray[i][j];
                }
            }
            res[toString] = R._path2string;
            return res;
        },
        pathToRelative = cacher(function (pathArray) {
            if (!R.is(pathArray, array) || !R.is(pathArray && pathArray[0], array)) { // rough assumption
                pathArray = R.parsePathString(pathArray);
            }
            var res = [],
                x = 0,
                y = 0,
                mx = 0,
                my = 0,
                start = 0;
            if (pathArray[0][0] == "M") {
                x = pathArray[0][1];
                y = pathArray[0][2];
                mx = x;
                my = y;
                start++;
                res[push](["M", x, y]);
            }
            for (var i = start, ii = pathArray[length]; i < ii; i++) {
                var r = res[i] = [],
                    pa = pathArray[i];
                if (pa[0] != lowerCase.call(pa[0])) {
                    r[0] = lowerCase.call(pa[0]);
                    switch (r[0]) {
                        case "a":
                            r[1] = pa[1];
                            r[2] = pa[2];
                            r[3] = pa[3];
                            r[4] = pa[4];
                            r[5] = pa[5];
                            r[6] = +(pa[6] - x).toFixed(3);
                            r[7] = +(pa[7] - y).toFixed(3);
                            break;
                        case "v":
                            r[1] = +(pa[1] - y).toFixed(3);
                            break;
                        case "m":
                            mx = pa[1];
                            my = pa[2];
                        default:
                            for (var j = 1, jj = pa[length]; j < jj; j++) {
                                r[j] = +(pa[j] - ((j % 2) ? x : y)).toFixed(3);
                            }
                    }
                } else {
                    r = res[i] = [];
                    if (pa[0] == "m") {
                        mx = pa[1] + x;
                        my = pa[2] + y;
                    }
                    for (var k = 0, kk = pa[length]; k < kk; k++) {
                        res[i][k] = pa[k];
                    }
                }
                var len = res[i][length];
                switch (res[i][0]) {
                    case "z":
                        x = mx;
                        y = my;
                        break;
                    case "h":
                        x += +res[i][len - 1];
                        break;
                    case "v":
                        y += +res[i][len - 1];
                        break;
                    default:
                        x += +res[i][len - 2];
                        y += +res[i][len - 1];
                }
            }
            res[toString] = R._path2string;
            return res;
        }, 0, pathClone),
        pathToAbsolute = cacher(function (pathArray) {
            if (!R.is(pathArray, array) || !R.is(pathArray && pathArray[0], array)) { // rough assumption
                pathArray = R.parsePathString(pathArray);
            }
            var res = [],
                x = 0,
                y = 0,
                mx = 0,
                my = 0,
                start = 0;
            if (pathArray[0][0] == "M") {
                x = +pathArray[0][1];
                y = +pathArray[0][2];
                mx = x;
                my = y;
                start++;
                res[0] = ["M", x, y];
            }
            for (var i = start, ii = pathArray[length]; i < ii; i++) {
                var r = res[i] = [],
                    pa = pathArray[i];
                if (pa[0] != upperCase.call(pa[0])) {
                    r[0] = upperCase.call(pa[0]);
                    switch (r[0]) {
                        case "A":
                            r[1] = pa[1];
                            r[2] = pa[2];
                            r[3] = pa[3];
                            r[4] = pa[4];
                            r[5] = pa[5];
                            r[6] = +(pa[6] + x);
                            r[7] = +(pa[7] + y);
                            break;
                        case "V":
                            r[1] = +pa[1] + y;
                            break;
                        case "H":
                            r[1] = +pa[1] + x;
                            break;
                        case "M":
                            mx = +pa[1] + x;
                            my = +pa[2] + y;
                        default:
                            for (var j = 1, jj = pa[length]; j < jj; j++) {
                                r[j] = +pa[j] + ((j % 2) ? x : y);
                            }
                    }
                } else {
                    for (var k = 0, kk = pa[length]; k < kk; k++) {
                        res[i][k] = pa[k];
                    }
                }
                switch (r[0]) {
                    case "Z":
                        x = mx;
                        y = my;
                        break;
                    case "H":
                        x = r[1];
                        break;
                    case "V":
                        y = r[1];
                        break;
                    case "M":
                        mx = res[i][res[i][length] - 2];
                        my = res[i][res[i][length] - 1];
                    default:
                        x = res[i][res[i][length] - 2];
                        y = res[i][res[i][length] - 1];
                }
            }
            res[toString] = R._path2string;
            return res;
        }, null, pathClone),
        l2c = function (x1, y1, x2, y2) {
            return [x1, y1, x2, y2, x2, y2];
        },
        q2c = function (x1, y1, ax, ay, x2, y2) {
            var _13 = 1 / 3,
                _23 = 2 / 3;
            return [
                _13 * x1 + _23 * ax,
                _13 * y1 + _23 * ay,
                _13 * x2 + _23 * ax,
                _13 * y2 + _23 * ay,
                x2,
                y2
            ];
        },
        a2c = function (x1, y1, rx, ry, angle, large_arc_flag, sweep_flag, x2, y2, recursive) {
            // for more information of where this math came from visit:
            // http://www.w3.org/TR/SVG11/implnote.html#ArcImplementationNotes
            var _120 = PI * 120 / 180,
                rad = PI / 180 * (+angle || 0),
                res = [],
                xy,
                rotate = cacher(function (x, y, rad) {
                    var X = x * math.cos(rad) - y * math.sin(rad),
                        Y = x * math.sin(rad) + y * math.cos(rad);
                    return {x: X, y: Y};
                });
            if (!recursive) {
                xy = rotate(x1, y1, -rad);
                x1 = xy.x;
                y1 = xy.y;
                xy = rotate(x2, y2, -rad);
                x2 = xy.x;
                y2 = xy.y;
                var cos = math.cos(PI / 180 * angle),
                    sin = math.sin(PI / 180 * angle),
                    x = (x1 - x2) / 2,
                    y = (y1 - y2) / 2;
                var h = (x * x) / (rx * rx) + (y * y) / (ry * ry);
                if (h > 1) {
                    h = math.sqrt(h);
                    rx = h * rx;
                    ry = h * ry;
                }
                var rx2 = rx * rx,
                    ry2 = ry * ry,
                    k = (large_arc_flag == sweep_flag ? -1 : 1) *
                        math.sqrt(abs((rx2 * ry2 - rx2 * y * y - ry2 * x * x) / (rx2 * y * y + ry2 * x * x))),
                    cx = k * rx * y / ry + (x1 + x2) / 2,
                    cy = k * -ry * x / rx + (y1 + y2) / 2,
                    f1 = math.asin(((y1 - cy) / ry).toFixed(9)),
                    f2 = math.asin(((y2 - cy) / ry).toFixed(9));

                f1 = x1 < cx ? PI - f1 : f1;
                f2 = x2 < cx ? PI - f2 : f2;
                f1 < 0 && (f1 = PI * 2 + f1);
                f2 < 0 && (f2 = PI * 2 + f2);
                if (sweep_flag && f1 > f2) {
                    f1 = f1 - PI * 2;
                }
                if (!sweep_flag && f2 > f1) {
                    f2 = f2 - PI * 2;
                }
            } else {
                f1 = recursive[0];
                f2 = recursive[1];
                cx = recursive[2];
                cy = recursive[3];
            }
            var df = f2 - f1;
            if (abs(df) > _120) {
                var f2old = f2,
                    x2old = x2,
                    y2old = y2;
                f2 = f1 + _120 * (sweep_flag && f2 > f1 ? 1 : -1);
                x2 = cx + rx * math.cos(f2);
                y2 = cy + ry * math.sin(f2);
                res = a2c(x2, y2, rx, ry, angle, 0, sweep_flag, x2old, y2old, [f2, f2old, cx, cy]);
            }
            df = f2 - f1;
            var c1 = math.cos(f1),
                s1 = math.sin(f1),
                c2 = math.cos(f2),
                s2 = math.sin(f2),
                t = math.tan(df / 4),
                hx = 4 / 3 * rx * t,
                hy = 4 / 3 * ry * t,
                m1 = [x1, y1],
                m2 = [x1 + hx * s1, y1 - hy * c1],
                m3 = [x2 + hx * s2, y2 - hy * c2],
                m4 = [x2, y2];
            m2[0] = 2 * m1[0] - m2[0];
            m2[1] = 2 * m1[1] - m2[1];
            if (recursive) {
                return [m2, m3, m4][concat](res);
            } else {
                res = [m2, m3, m4][concat](res)[join]()[split](",");
                var newres = [];
                for (var i = 0, ii = res[length]; i < ii; i++) {
                    newres[i] = i % 2 ? rotate(res[i - 1], res[i], rad).y : rotate(res[i], res[i + 1], rad).x;
                }
                return newres;
            }
        },
        findDotAtSegment = function (p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y, t) {
            var t1 = 1 - t;
            return {
                x: pow(t1, 3) * p1x + pow(t1, 2) * 3 * t * c1x + t1 * 3 * t * t * c2x + pow(t, 3) * p2x,
                y: pow(t1, 3) * p1y + pow(t1, 2) * 3 * t * c1y + t1 * 3 * t * t * c2y + pow(t, 3) * p2y
            };
        },
        curveDim = cacher(function (p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y) {
            var a = (c2x - 2 * c1x + p1x) - (p2x - 2 * c2x + c1x),
                b = 2 * (c1x - p1x) - 2 * (c2x - c1x),
                c = p1x - c1x,
                t1 = (-b + math.sqrt(b * b - 4 * a * c)) / 2 / a,
                t2 = (-b - math.sqrt(b * b - 4 * a * c)) / 2 / a,
                y = [p1y, p2y],
                x = [p1x, p2x],
                dot;
            abs(t1) > "1e12" && (t1 = .5);
            abs(t2) > "1e12" && (t2 = .5);
            if (t1 > 0 && t1 < 1) {
                dot = findDotAtSegment(p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y, t1);
                x[push](dot.x);
                y[push](dot.y);
            }
            if (t2 > 0 && t2 < 1) {
                dot = findDotAtSegment(p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y, t2);
                x[push](dot.x);
                y[push](dot.y);
            }
            a = (c2y - 2 * c1y + p1y) - (p2y - 2 * c2y + c1y);
            b = 2 * (c1y - p1y) - 2 * (c2y - c1y);
            c = p1y - c1y;
            t1 = (-b + math.sqrt(b * b - 4 * a * c)) / 2 / a;
            t2 = (-b - math.sqrt(b * b - 4 * a * c)) / 2 / a;
            abs(t1) > "1e12" && (t1 = .5);
            abs(t2) > "1e12" && (t2 = .5);
            if (t1 > 0 && t1 < 1) {
                dot = findDotAtSegment(p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y, t1);
                x[push](dot.x);
                y[push](dot.y);
            }
            if (t2 > 0 && t2 < 1) {
                dot = findDotAtSegment(p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y, t2);
                x[push](dot.x);
                y[push](dot.y);
            }
            return {
                min: {x: mmin[apply](0, x), y: mmin[apply](0, y)},
                max: {x: mmax[apply](0, x), y: mmax[apply](0, y)}
            };
        }),
        path2curve = cacher(function (path, path2) {
            var p = pathToAbsolute(path),
                p2 = path2 && pathToAbsolute(path2),
                attrs = {x: 0, y: 0, bx: 0, by: 0, X: 0, Y: 0, qx: null, qy: null},
                attrs2 = {x: 0, y: 0, bx: 0, by: 0, X: 0, Y: 0, qx: null, qy: null},
                processPath = function (path, d) {
                    var nx, ny;
                    if (!path) {
                        return ["C", d.x, d.y, d.x, d.y, d.x, d.y];
                    }
                    !(path[0] in {T:1, Q:1}) && (d.qx = d.qy = null);
                    switch (path[0]) {
                        case "M":
                            d.X = path[1];
                            d.Y = path[2];
                            break;
                        case "A":
                            path = ["C"][concat](a2c[apply](0, [d.x, d.y][concat](path.slice(1))));
                            break;
                        case "S":
                            nx = d.x + (d.x - (d.bx || d.x));
                            ny = d.y + (d.y - (d.by || d.y));
                            path = ["C", nx, ny][concat](path.slice(1));
                            break;
                        case "T":
                            d.qx = d.x + (d.x - (d.qx || d.x));
                            d.qy = d.y + (d.y - (d.qy || d.y));
                            path = ["C"][concat](q2c(d.x, d.y, d.qx, d.qy, path[1], path[2]));
                            break;
                        case "Q":
                            d.qx = path[1];
                            d.qy = path[2];
                            path = ["C"][concat](q2c(d.x, d.y, path[1], path[2], path[3], path[4]));
                            break;
                        case "L":
                            path = ["C"][concat](l2c(d.x, d.y, path[1], path[2]));
                            break;
                        case "H":
                            path = ["C"][concat](l2c(d.x, d.y, path[1], d.y));
                            break;
                        case "V":
                            path = ["C"][concat](l2c(d.x, d.y, d.x, path[1]));
                            break;
                        case "Z":
                            path = ["C"][concat](l2c(d.x, d.y, d.X, d.Y));
                            break;
                    }
                    return path;
                },
                fixArc = function (pp, i) {
                    if (pp[i][length] > 7) {
                        pp[i].shift();
                        var pi = pp[i];
                        while (pi[length]) {
                            pp.splice(i++, 0, ["C"][concat](pi.splice(0, 6)));
                        }
                        pp.splice(i, 1);
                        ii = mmax(p[length], p2 && p2[length] || 0);
                    }
                },
                fixM = function (path1, path2, a1, a2, i) {
                    if (path1 && path2 && path1[i][0] == "M" && path2[i][0] != "M") {
                        path2.splice(i, 0, ["M", a2.x, a2.y]);
                        a1.bx = 0;
                        a1.by = 0;
                        a1.x = path1[i][1];
                        a1.y = path1[i][2];
                        ii = mmax(p[length], p2 && p2[length] || 0);
                    }
                };
            for (var i = 0, ii = mmax(p[length], p2 && p2[length] || 0); i < ii; i++) {
                p[i] = processPath(p[i], attrs);
                fixArc(p, i);
                p2 && (p2[i] = processPath(p2[i], attrs2));
                p2 && fixArc(p2, i);
                fixM(p, p2, attrs, attrs2, i);
                fixM(p2, p, attrs2, attrs, i);
                var seg = p[i],
                    seg2 = p2 && p2[i],
                    seglen = seg[length],
                    seg2len = p2 && seg2[length];
                attrs.x = seg[seglen - 2];
                attrs.y = seg[seglen - 1];
                attrs.bx = toFloat(seg[seglen - 4]) || attrs.x;
                attrs.by = toFloat(seg[seglen - 3]) || attrs.y;
                attrs2.bx = p2 && (toFloat(seg2[seg2len - 4]) || attrs2.x);
                attrs2.by = p2 && (toFloat(seg2[seg2len - 3]) || attrs2.y);
                attrs2.x = p2 && seg2[seg2len - 2];
                attrs2.y = p2 && seg2[seg2len - 1];
            }
            return p2 ? [p, p2] : p;
        }, null, pathClone),
        parseDots = cacher(function (gradient) {
            var dots = [];
            for (var i = 0, ii = gradient[length]; i < ii; i++) {
                var dot = {},
                    par = gradient[i].match(/^([^:]*):?([\d\.]*)/);
                dot.color = R.getRGB(par[1]);
                if (dot.color.error) {
                    return null;
                }
                dot.color = dot.color.hex;
                par[2] && (dot.offset = par[2] + "%");
                dots[push](dot);
            }
            for (i = 1, ii = dots[length] - 1; i < ii; i++) {
                if (!dots[i].offset) {
                    var start = toFloat(dots[i - 1].offset || 0),
                        end = 0;
                    for (var j = i + 1; j < ii; j++) {
                        if (dots[j].offset) {
                            end = dots[j].offset;
                            break;
                        }
                    }
                    if (!end) {
                        end = 100;
                        j = ii;
                    }
                    end = toFloat(end);
                    var d = (end - start) / (j - i + 1);
                    for (; i < j; i++) {
                        start += d;
                        dots[i].offset = start + "%";
                    }
                }
            }
            return dots;
        }),
        getContainer = function (x, y, w, h) {
            var container;
            if (R.is(x, string) || R.is(x, "object")) {
                container = R.is(x, string) ? doc.getElementById(x) : x;
                if (container.tagName) {
                    if (y == null) {
                        return {
                            container: container,
                            width: container.style.pixelWidth || container.offsetWidth,
                            height: container.style.pixelHeight || container.offsetHeight
                        };
                    } else {
                        return {container: container, width: y, height: w};
                    }
                }
            } else {
                return {container: 1, x: x, y: y, width: w, height: h};
            }
        },
        plugins = function (con, add) {
            var that = this;
            for (var prop in add) {
                if (add[has](prop) && !(prop in con)) {
                    switch (typeof add[prop]) {
                        case "function":
                            (function (f) {
                                con[prop] = con === that ? f : function () { return f[apply](that, arguments); };
                            })(add[prop]);
                            break;
                        case "object":
                            con[prop] = con[prop] || {};
                            plugins.call(this, con[prop], add[prop]);
                            break;
                        default:
                            con[prop] = add[prop];
                            break;
                    }
                }
            }
        },
        tear = function (el, paper) {
            el == paper.top && (paper.top = el.prev);
            el == paper.bottom && (paper.bottom = el.next);
            el.next && (el.next.prev = el.prev);
            el.prev && (el.prev.next = el.next);
        },
        tofront = function (el, paper) {
            if (paper.top === el) {
                return;
            }
            tear(el, paper);
            el.next = null;
            el.prev = paper.top;
            paper.top.next = el;
            paper.top = el;
        },
        toback = function (el, paper) {
            if (paper.bottom === el) {
                return;
            }
            tear(el, paper);
            el.next = paper.bottom;
            el.prev = null;
            paper.bottom.prev = el;
            paper.bottom = el;
        },
        insertafter = function (el, el2, paper) {
            tear(el, paper);
            el2 == paper.top && (paper.top = el);
            el2.next && (el2.next.prev = el);
            el.next = el2.next;
            el.prev = el2;
            el2.next = el;
        },
        insertbefore = function (el, el2, paper) {
            tear(el, paper);
            el2 == paper.bottom && (paper.bottom = el);
            el2.prev && (el2.prev.next = el);
            el.prev = el2.prev;
            el2.prev = el;
            el.next = el2;
        },
        removed = function (methodname) {
            return function () {
                throw new Error("Rapha\xebl: you are calling to method \u201c" + methodname + "\u201d of removed object");
            };
        };
    R.pathToRelative = pathToRelative;
    // SVG
    if (R.svg) {
        paperproto.svgns = "http://www.w3.org/2000/svg";
        paperproto.xlink = "http://www.w3.org/1999/xlink";
        round = function (num) {
            return +num + (~~num === num) * .5;
        };
        var $ = function (el, attr) {
            if (attr) {
                for (var key in attr) {
                    if (attr[has](key)) {
                        el[setAttribute](key, Str(attr[key]));
                    }
                }
            } else {
                el = doc.createElementNS(paperproto.svgns, el);
                el.style.webkitTapHighlightColor = "rgba(0,0,0,0)";
                return el;
            }
        };
        R[toString] = function () {
            return  "Your browser supports SVG.\nYou are running Rapha\xebl " + this.version;
        };
        var thePath = function (pathString, SVG) {
            var el = $("path");
            SVG.canvas && SVG.canvas[appendChild](el);
            var p = new Element(el, SVG);
            p.type = "path";
            setFillAndStroke(p, {fill: "none", stroke: "#000", path: pathString});
            return p;
        };
        var addGradientFill = function (o, gradient, SVG) {
            var type = "linear",
                fx = .5, fy = .5,
                s = o.style;
            gradient = Str(gradient)[rp](radial_gradient, function (all, _fx, _fy) {
                type = "radial";
                if (_fx && _fy) {
                    fx = toFloat(_fx);
                    fy = toFloat(_fy);
                    var dir = ((fy > .5) * 2 - 1);
                    pow(fx - .5, 2) + pow(fy - .5, 2) > .25 &&
                        (fy = math.sqrt(.25 - pow(fx - .5, 2)) * dir + .5) &&
                        fy != .5 &&
                    (fy = fy.toFixed(5) - 1e-5 * dir);
                }
                return E;
            });
            gradient = gradient[split](/\s*\-\s*/);
            if (type == "linear") {
                var angle = gradient.shift();
                angle = -toFloat(angle);
                if (isNaN(angle)) {
                    return null;
                }
                var vector = [0, 0, math.cos(angle * PI / 180), math.sin(angle * PI / 180)],
                    max = 1 / (mmax(abs(vector[2]), abs(vector[3])) || 1);
                vector[2] *= max;
                vector[3] *= max;
                if (vector[2] < 0) {
                    vector[0] = -vector[2];
                    vector[2] = 0;
                }
                if (vector[3] < 0) {
                    vector[1] = -vector[3];
                    vector[3] = 0;
                }
            }
            var dots = parseDots(gradient);
            if (!dots) {
                return null;
            }
            var id = o.getAttribute(fillString);
            id = id.match(/^url\(#(.*)\)$/);
            id && SVG.defs.removeChild(doc.getElementById(id[1]));

            var el = $(type + "Gradient");
            el.id = createUUID();
            $(el, type == "radial" ? {fx: fx, fy: fy} : {x1: vector[0], y1: vector[1], x2: vector[2], y2: vector[3]});
            SVG.defs[appendChild](el);
            for (var i = 0, ii = dots[length]; i < ii; i++) {
                var stop = $("stop");
                $(stop, {
                    offset: dots[i].offset ? dots[i].offset : !i ? "0%" : "100%",
                    "stop-color": dots[i].color || "#fff"
                });
                el[appendChild](stop);
            }
            $(o, {
                fill: "url(#" + el.id + ")",
                opacity: 1,
                "fill-opacity": 1
            });
            s.fill = E;
            s.opacity = 1;
            s.fillOpacity = 1;
            return 1;
        };
        var updatePosition = function (o) {
            var bbox = o.getBBox();
            $(o.pattern, {patternTransform: R.format("translate({0},{1})", bbox.x, bbox.y)});
        };
        var setFillAndStroke = function (o, params) {
            var dasharray = {
                    "": [0],
                    "none": [0],
                    "-": [3, 1],
                    ".": [1, 1],
                    "-.": [3, 1, 1, 1],
                    "-..": [3, 1, 1, 1, 1, 1],
                    ". ": [1, 3],
                    "- ": [4, 3],
                    "--": [8, 3],
                    "- .": [4, 3, 1, 3],
                    "--.": [8, 3, 1, 3],
                    "--..": [8, 3, 1, 3, 1, 3]
                },
                node = o.node,
                attrs = o.attrs,
                rot = o.rotate(),
                addDashes = function (o, value) {
                    value = dasharray[lowerCase.call(value)];
                    if (value) {
                        var width = o.attrs["stroke-width"] || "1",
                            butt = {round: width, square: width, butt: 0}[o.attrs["stroke-linecap"] || params["stroke-linecap"]] || 0,
                            dashes = [];
                        var i = value[length];
                        while (i--) {
                            dashes[i] = value[i] * width + ((i % 2) ? 1 : -1) * butt;
                        }
                        $(node, {"stroke-dasharray": dashes[join](",")});
                    }
                };
            params[has]("rotation") && (rot = params.rotation);
            var rotxy = Str(rot)[split](separator);
            if (!(rotxy.length - 1)) {
                rotxy = null;
            } else {
                rotxy[1] = +rotxy[1];
                rotxy[2] = +rotxy[2];
            }
            toFloat(rot) && o.rotate(0, true);
            for (var att in params) {
                if (params[has](att)) {
                    if (!availableAttrs[has](att)) {
                        continue;
                    }
                    var value = params[att];
                    attrs[att] = value;
                    switch (att) {
                        case "blur":
                            o.blur(value);
                            break;
                        case "rotation":
                            o.rotate(value, true);
                            break;
                        case "href":
                        case "title":
                        case "target":
                            var pn = node.parentNode;
                            if (lowerCase.call(pn.tagName) != "a") {
                                var hl = $("a");
                                pn.insertBefore(hl, node);
                                hl[appendChild](node);
                                pn = hl;
                            }
                            if (att == "target" && value == "blank") {
                                pn.setAttributeNS(o.paper.xlink, "show", "new");
                            } else {
                                pn.setAttributeNS(o.paper.xlink, att, value);
                            }
                            break;
                        case "cursor":
                            node.style.cursor = value;
                            break;
                        case "clip-rect":
                            var rect = Str(value)[split](separator);
                            if (rect[length] == 4) {
                                o.clip && o.clip.parentNode.parentNode.removeChild(o.clip.parentNode);
                                var el = $("clipPath"),
                                    rc = $("rect");
                                el.id = createUUID();
                                $(rc, {
                                    x: rect[0],
                                    y: rect[1],
                                    width: rect[2],
                                    height: rect[3]
                                });
                                el[appendChild](rc);
                                o.paper.defs[appendChild](el);
                                $(node, {"clip-path": "url(#" + el.id + ")"});
                                o.clip = rc;
                            }
                            if (!value) {
                                var clip = doc.getElementById(node.getAttribute("clip-path")[rp](/(^url\(#|\)$)/g, E));
                                clip && clip.parentNode.removeChild(clip);
                                $(node, {"clip-path": E});
                                delete o.clip;
                            }
                            break;
                        case "path":
                            if (o.type == "path") {
                                $(node, {d: value ? attrs.path = pathToAbsolute(value) : "M0,0"});
                            }
                            break;
                        case "width":
                            node[setAttribute](att, value);
                            if (attrs.fx) {
                                att = "x";
                                value = attrs.x;
                            } else {
                                break;
                            }
                        case "x":
                            if (attrs.fx) {
                                value = -attrs.x - (attrs.width || 0);
                            }
                        case "rx":
                            if (att == "rx" && o.type == "rect") {
                                break;
                            }
                        case "cx":
                            rotxy && (att == "x" || att == "cx") && (rotxy[1] += value - attrs[att]);
                            node[setAttribute](att, value);
                            o.pattern && updatePosition(o);
                            break;
                        case "height":
                            node[setAttribute](att, value);
                            if (attrs.fy) {
                                att = "y";
                                value = attrs.y;
                            } else {
                                break;
                            }
                        case "y":
                            if (attrs.fy) {
                                value = -attrs.y - (attrs.height || 0);
                            }
                        case "ry":
                            if (att == "ry" && o.type == "rect") {
                                break;
                            }
                        case "cy":
                            rotxy && (att == "y" || att == "cy") && (rotxy[2] += value - attrs[att]);
                            node[setAttribute](att, value);
                            o.pattern && updatePosition(o);
                            break;
                        case "r":
                            if (o.type == "rect") {
                                $(node, {rx: value, ry: value});
                            } else {
                                node[setAttribute](att, value);
                            }
                            break;
                        case "src":
                            if (o.type == "image") {
                                node.setAttributeNS(o.paper.xlink, "href", value);
                            }
                            break;
                        case "stroke-width":
                            node.style.strokeWidth = value;
                            // Need following line for Firefox
                            node[setAttribute](att, value);
                            if (attrs["stroke-dasharray"]) {
                                addDashes(o, attrs["stroke-dasharray"]);
                            }
                            break;
                        case "stroke-dasharray":
                            addDashes(o, value);
                            break;
                        case "translation":
                            var xy = Str(value)[split](separator);
                            xy[0] = +xy[0] || 0;
                            xy[1] = +xy[1] || 0;
                            if (rotxy) {
                                rotxy[1] += xy[0];
                                rotxy[2] += xy[1];
                            }
                            translate.call(o, xy[0], xy[1]);
                            break;
                        case "scale":
                            xy = Str(value)[split](separator);
                            o.scale(+xy[0] || 1, +xy[1] || +xy[0] || 1, isNaN(toFloat(xy[2])) ? null : +xy[2], isNaN(toFloat(xy[3])) ? null : +xy[3]);
                            break;
                        case fillString:
                            var isURL = Str(value).match(ISURL);
                            if (isURL) {
                                el = $("pattern");
                                var ig = $("image");
                                el.id = createUUID();
                                $(el, {x: 0, y: 0, patternUnits: "userSpaceOnUse", height: 1, width: 1});
                                $(ig, {x: 0, y: 0});
                                ig.setAttributeNS(o.paper.xlink, "href", isURL[1]);
                                el[appendChild](ig);

                                var img = doc.createElement("img");
                                img.style.cssText = "position:absolute;left:-9999em;top-9999em";
                                img.onload = function () {
                                    $(el, {width: this.offsetWidth, height: this.offsetHeight});
                                    $(ig, {width: this.offsetWidth, height: this.offsetHeight});
                                    doc.body.removeChild(this);
                                    o.paper.safari();
                                };
                                doc.body[appendChild](img);
                                img.src = isURL[1];
                                o.paper.defs[appendChild](el);
                                node.style.fill = "url(#" + el.id + ")";
                                $(node, {fill: "url(#" + el.id + ")"});
                                o.pattern = el;
                                o.pattern && updatePosition(o);
                                break;
                            }
                            var clr = R.getRGB(value);
                            if (!clr.error) {
                                delete params.gradient;
                                delete attrs.gradient;
                                !R.is(attrs.opacity, "undefined") &&
                                    R.is(params.opacity, "undefined") &&
                                $(node, {opacity: attrs.opacity});
                                !R.is(attrs["fill-opacity"], "undefined") &&
                                    R.is(params["fill-opacity"], "undefined") &&
                                $(node, {"fill-opacity": attrs["fill-opacity"]});
                            } else if ((({circle: 1, ellipse: 1})[has](o.type) || Str(value).charAt() != "r") && addGradientFill(node, value, o.paper)) {
                                attrs.gradient = value;
                                attrs.fill = "none";
                                break;
                            }
                            clr[has]("opacity") && $(node, {"fill-opacity": clr.opacity > 1 ? clr.opacity / 100 : clr.opacity});
                        case "stroke":
                            clr = R.getRGB(value);
                            node[setAttribute](att, clr.hex);
                            att == "stroke" && clr[has]("opacity") && $(node, {"stroke-opacity": clr.opacity > 1 ? clr.opacity / 100 : clr.opacity});
                            break;
                        case "gradient":
                            (({circle: 1, ellipse: 1})[has](o.type) || Str(value).charAt() != "r") && addGradientFill(node, value, o.paper);
                            break;
                        case "opacity":
                            if (attrs.gradient && !attrs[has]("stroke-opacity")) {
                                $(node, {"stroke-opacity": value > 1 ? value / 100 : value});
                            }
                        // fall
                        case "fill-opacity":
                            if (attrs.gradient) {
                                var gradient = doc.getElementById(node.getAttribute(fillString)[rp](/^url\(#|\)$/g, E));
                                if (gradient) {
                                    var stops = gradient.getElementsByTagName("stop");
                                    stops[stops[length] - 1][setAttribute]("stop-opacity", value);
                                }
                                break;
                            }
                        default:
                            att == "font-size" && (value = toInt(value, 10) + "px");
                            var cssrule = att[rp](/(\-.)/g, function (w) {
                                return upperCase.call(w.substring(1));
                            });
                            node.style[cssrule] = value;
                            // Need following line for Firefox
                            node[setAttribute](att, value);
                            break;
                    }
                }
            }

            tuneText(o, params);
            if (rotxy) {
                o.rotate(rotxy.join(S));
            } else {
                toFloat(rot) && o.rotate(rot, true);
            }
        };
        var leading = 1.2,
            tuneText = function (el, params) {
                if (el.type != "text" || !(params[has]("text") || params[has]("font") || params[has]("font-size") || params[has]("x") || params[has]("y"))) {
                    return;
                }
                var a = el.attrs,
                    node = el.node,
                    fontSize = node.firstChild ? toInt(doc.defaultView.getComputedStyle(node.firstChild, E).getPropertyValue("font-size"), 10) : 10;

                if (params[has]("text")) {
                    a.text = params.text;
                    while (node.firstChild) {
                        node.removeChild(node.firstChild);
                    }
                    var texts = Str(params.text)[split]("\n");
                    for (var i = 0, ii = texts[length]; i < ii; i++) if (texts[i]) {
                        var tspan = $("tspan");
                        i && $(tspan, {dy: fontSize * leading, x: a.x});
                        tspan[appendChild](doc.createTextNode(texts[i]));
                        node[appendChild](tspan);
                    }
                } else {
                    texts = node.getElementsByTagName("tspan");
                    for (i = 0, ii = texts[length]; i < ii; i++) {
                        i && $(texts[i], {dy: fontSize * leading, x: a.x});
                    }
                }
                $(node, {y: a.y});
                var bb = el.getBBox(),
                    dif = a.y - (bb.y + bb.height / 2);
                dif && R.is(dif, "finite") && $(node, {y: a.y + dif});
            },
            Element = function (node, svg) {
                var X = 0,
                    Y = 0;
                this[0] = node;
                this.id = R._oid++;
                this.node = node;
                node.raphael = this;
                this.paper = svg;
                this.attrs = this.attrs || {};
                this.transformations = []; // rotate, translate, scale
                this._ = {
                    tx: 0,
                    ty: 0,
                    rt: {deg: 0, cx: 0, cy: 0},
                    sx: 1,
                    sy: 1
                };
                !svg.bottom && (svg.bottom = this);
                this.prev = svg.top;
                svg.top && (svg.top.next = this);
                svg.top = this;
                this.next = null;
            };
        var elproto = Element[proto];
        Element[proto].rotate = function (deg, cx, cy) {
            if (this.removed) {
                return this;
            }
            if (deg == null) {
                if (this._.rt.cx) {
                    return [this._.rt.deg, this._.rt.cx, this._.rt.cy][join](S);
                }
                return this._.rt.deg;
            }
            var bbox = this.getBBox();
            deg = Str(deg)[split](separator);
            if (deg[length] - 1) {
                cx = toFloat(deg[1]);
                cy = toFloat(deg[2]);
            }
            deg = toFloat(deg[0]);
            if (cx != null && cx !== false) {
                this._.rt.deg = deg;
            } else {
                this._.rt.deg += deg;
            }
            (cy == null) && (cx = null);
            this._.rt.cx = cx;
            this._.rt.cy = cy;
            cx = cx == null ? bbox.x + bbox.width / 2 : cx;
            cy = cy == null ? bbox.y + bbox.height / 2 : cy;
            if (this._.rt.deg) {
                this.transformations[0] = R.format("rotate({0} {1} {2})", this._.rt.deg, cx, cy);
                this.clip && $(this.clip, {transform: R.format("rotate({0} {1} {2})", -this._.rt.deg, cx, cy)});
            } else {
                this.transformations[0] = E;
                this.clip && $(this.clip, {transform: E});
            }
            $(this.node, {transform: this.transformations[join](S)});
            return this;
        };
        Element[proto].hide = function () {
            !this.removed && (this.node.style.display = "none");
            return this;
        };
        Element[proto].show = function () {
            !this.removed && (this.node.style.display = "");
            return this;
        };
        Element[proto].remove = function () {
            if (this.removed) {
                return;
            }
            tear(this, this.paper);
            this.node.parentNode.removeChild(this.node);
            for (var i in this) {
                delete this[i];
            }
            this.removed = true;
        };
        Element[proto].getBBox = function () {
            if (this.removed) {
                return this;
            }
            if (this.type == "path") {
                return pathDimensions(this.attrs.path);
            }
            if (this.node.style.display == "none") {
                this.show();
                var hide = true;
            }
            var bbox = {};
            try {
                bbox = this.node.getBBox();
            } catch(e) {
                // Firefox 3.0.x plays badly here
            } finally {
                bbox = bbox || {};
            }
            if (this.type == "text") {
                bbox = {x: bbox.x, y: Infinity, width: 0, height: 0};
                for (var i = 0, ii = this.node.getNumberOfChars(); i < ii; i++) {
                    var bb = this.node.getExtentOfChar(i);
                    (bb.y < bbox.y) && (bbox.y = bb.y);
                    (bb.y + bb.height - bbox.y > bbox.height) && (bbox.height = bb.y + bb.height - bbox.y);
                    (bb.x + bb.width - bbox.x > bbox.width) && (bbox.width = bb.x + bb.width - bbox.x);
                }
            }
            hide && this.hide();
            return bbox;
        };
        Element[proto].attr = function (name, value) {
            if (this.removed) {
                return this;
            }
            if (name == null) {
                var res = {};
                for (var i in this.attrs) if (this.attrs[has](i)) {
                    res[i] = this.attrs[i];
                }
                this._.rt.deg && (res.rotation = this.rotate());
                (this._.sx != 1 || this._.sy != 1) && (res.scale = this.scale());
                res.gradient && res.fill == "none" && (res.fill = res.gradient) && delete res.gradient;
                return res;
            }
            if (value == null && R.is(name, string)) {
                if (name == "translation") {
                    return translate.call(this);
                }
                if (name == "rotation") {
                    return this.rotate();
                }
                if (name == "scale") {
                    return this.scale();
                }
                if (name == fillString && this.attrs.fill == "none" && this.attrs.gradient) {
                    return this.attrs.gradient;
                }
                return this.attrs[name];
            }
            if (value == null && R.is(name, array)) {
                var values = {};
                for (var j = 0, jj = name.length; j < jj; j++) {
                    values[name[j]] = this.attr(name[j]);
                }
                return values;
            }
            if (value != null) {
                var params = {};
                params[name] = value;
            } else if (name != null && R.is(name, "object")) {
                params = name;
            }
            for (var key in this.paper.customAttributes) if (this.paper.customAttributes[has](key) && params[has](key) && R.is(this.paper.customAttributes[key], "function")) {
                var par = this.paper.customAttributes[key].apply(this, [][concat](params[key]));
                this.attrs[key] = params[key];
                for (var subkey in par) if (par[has](subkey)) {
                    params[subkey] = par[subkey];
                }
            }
            setFillAndStroke(this, params);
            return this;
        };
        Element[proto].toFront = function () {
            if (this.removed) {
                return this;
            }
            this.node.parentNode[appendChild](this.node);
            var svg = this.paper;
            svg.top != this && tofront(this, svg);
            return this;
        };
        Element[proto].toBack = function () {
            if (this.removed) {
                return this;
            }
            if (this.node.parentNode.firstChild != this.node) {
                this.node.parentNode.insertBefore(this.node, this.node.parentNode.firstChild);
                toback(this, this.paper);
                var svg = this.paper;
            }
            return this;
        };
        Element[proto].insertAfter = function (element) {
            if (this.removed) {
                return this;
            }
            var node = element.node || element[element.length - 1].node;
            if (node.nextSibling) {
                node.parentNode.insertBefore(this.node, node.nextSibling);
            } else {
                node.parentNode[appendChild](this.node);
            }
            insertafter(this, element, this.paper);
            return this;
        };
        Element[proto].insertBefore = function (element) {
            if (this.removed) {
                return this;
            }
            var node = element.node || element[0].node;
            node.parentNode.insertBefore(this.node, node);
            insertbefore(this, element, this.paper);
            return this;
        };
        Element[proto].blur = function (size) {
            // Experimental. No Safari support. Use it on your own risk.
            var t = this;
            if (+size !== 0) {
                var fltr = $("filter"),
                    blur = $("feGaussianBlur");
                t.attrs.blur = size;
                fltr.id = createUUID();
                $(blur, {stdDeviation: +size || 1.5});
                fltr.appendChild(blur);
                t.paper.defs.appendChild(fltr);
                t._blur = fltr;
                $(t.node, {filter: "url(#" + fltr.id + ")"});
            } else {
                if (t._blur) {
                    t._blur.parentNode.removeChild(t._blur);
                    delete t._blur;
                    delete t.attrs.blur;
                }
                t.node.removeAttribute("filter");
            }
        };
        var theCircle = function (svg, x, y, r) {
                var el = $("circle");
                svg.canvas && svg.canvas[appendChild](el);
                var res = new Element(el, svg);
                res.attrs = {cx: x, cy: y, r: r, fill: "none", stroke: "#000"};
                res.type = "circle";
                $(el, res.attrs);
                return res;
            },
            theRect = function (svg, x, y, w, h, r) {
                var el = $("rect");
                svg.canvas && svg.canvas[appendChild](el);
                var res = new Element(el, svg);
                res.attrs = {x: x, y: y, width: w, height: h, r: r || 0, rx: r || 0, ry: r || 0, fill: "none", stroke: "#000"};
                res.type = "rect";
                $(el, res.attrs);
                return res;
            },
            theEllipse = function (svg, x, y, rx, ry) {
                var el = $("ellipse");
                svg.canvas && svg.canvas[appendChild](el);
                var res = new Element(el, svg);
                res.attrs = {cx: x, cy: y, rx: rx, ry: ry, fill: "none", stroke: "#000"};
                res.type = "ellipse";
                $(el, res.attrs);
                return res;
            },
            theImage = function (svg, src, x, y, w, h) {
                var el = $("image");
                $(el, {x: x, y: y, width: w, height: h, preserveAspectRatio: "none"});
                el.setAttributeNS(svg.xlink, "href", src);
                svg.canvas && svg.canvas[appendChild](el);
                var res = new Element(el, svg);
                res.attrs = {x: x, y: y, width: w, height: h, src: src};
                res.type = "image";
                return res;
            },
            theText = function (svg, x, y, text) {
                var el = $("text");
                $(el, {x: x, y: y, "text-anchor": "middle"});
                svg.canvas && svg.canvas[appendChild](el);
                var res = new Element(el, svg);
                res.attrs = {x: x, y: y, "text-anchor": "middle", text: text, font: availableAttrs.font, stroke: "none", fill: "#000"};
                res.type = "text";
                setFillAndStroke(res, res.attrs);
                return res;
            },
            setSize = function (width, height) {
                this.width = width || this.width;
                this.height = height || this.height;
                this.canvas[setAttribute]("width", this.width);
                this.canvas[setAttribute]("height", this.height);
                return this;
            },
            create = function () {
                var con = getContainer[apply](0, arguments),
                    container = con && con.container,
                    x = con.x,
                    y = con.y,
                    width = con.width,
                    height = con.height;
                if (!container) {
                    throw new Error("SVG container not found.");
                }
                var cnvs = $("svg");
                x = x || 0;
                y = y || 0;
                width = width || 512;
                height = height || 342;
                $(cnvs, {
                    xmlns: "http://www.w3.org/2000/svg",
                    version: 1.1,
                    width: width,
                    height: height
                });
                if (container == 1) {
                    cnvs.style.cssText = "position:absolute;left:" + x + "px;top:" + y + "px";
                    doc.body[appendChild](cnvs);
                } else {
                    if (container.firstChild) {
                        container.insertBefore(cnvs, container.firstChild);
                    } else {
                        container[appendChild](cnvs);
                    }
                }
                container = new Paper;
                container.width = width;
                container.height = height;
                container.canvas = cnvs;
                plugins.call(container, container, R.fn);
                container.clear();
                return container;
            };
        paperproto.clear = function () {
            var c = this.canvas;
            while (c.firstChild) {
                c.removeChild(c.firstChild);
            }
            this.bottom = this.top = null;
            (this.desc = $("desc"))[appendChild](doc.createTextNode("Created with Rapha\xebl"));
            c[appendChild](this.desc);
            c[appendChild](this.defs = $("defs"));
        };
        paperproto.remove = function () {
            this.canvas.parentNode && this.canvas.parentNode.removeChild(this.canvas);
            for (var i in this) {
                this[i] = removed(i);
            }
        };
    }

    // VML
    if (R.vml) {
        var map = {M: "m", L: "l", C: "c", Z: "x", m: "t", l: "r", c: "v", z: "x"},
            bites = /([clmz]),?([^clmz]*)/gi,
            blurregexp = / progid:\S+Blur\([^\)]+\)/g,
            val = /-?[^,\s-]+/g,
            coordsize = 1e3 + S + 1e3,
            zoom = 10,
            pathlike = {path: 1, rect: 1},
            path2vml = function (path) {
                var total =  /[ahqstv]/ig,
                    command = pathToAbsolute;
                Str(path).match(total) && (command = path2curve);
                total = /[clmz]/g;
                if (command == pathToAbsolute && !Str(path).match(total)) {
                    var res = Str(path)[rp](bites, function (all, command, args) {
                        var vals = [],
                            isMove = lowerCase.call(command) == "m",
                            res = map[command];
                        args[rp](val, function (value) {
                            if (isMove && vals[length] == 2) {
                                res += vals + map[command == "m" ? "l" : "L"];
                                vals = [];
                            }
                            vals[push](round(value * zoom));
                        });
                        return res + vals;
                    });
                    return res;
                }
                var pa = command(path), p, r;
                res = [];
                for (var i = 0, ii = pa[length]; i < ii; i++) {
                    p = pa[i];
                    r = lowerCase.call(pa[i][0]);
                    r == "z" && (r = "x");
                    for (var j = 1, jj = p[length]; j < jj; j++) {
                        r += round(p[j] * zoom) + (j != jj - 1 ? "," : E);
                    }
                    res[push](r);
                }
                return res[join](S);
            };

        R[toString] = function () {
            return  "Your browser doesn\u2019t support SVG. Falling down to VML.\nYou are running Rapha\xebl " + this.version;
        };
        thePath = function (pathString, vml) {
            var g = createNode("group");
            g.style.cssText = "position:absolute;left:0;top:0;width:" + vml.width + "px;height:" + vml.height + "px";
            g.coordsize = vml.coordsize;
            g.coordorigin = vml.coordorigin;
            var el = createNode("shape"), ol = el.style;
            ol.width = vml.width + "px";
            ol.height = vml.height + "px";
            el.coordsize = coordsize;
            el.coordorigin = vml.coordorigin;
            g[appendChild](el);
            var p = new Element(el, g, vml),
                attr = {fill: "none", stroke: "#000"};
            pathString && (attr.path = pathString);
            p.type = "path";
            p.path = [];
            p.Path = E;
            setFillAndStroke(p, attr);
            vml.canvas[appendChild](g);
            return p;
        };
        setFillAndStroke = function (o, params) {
            o.attrs = o.attrs || {};
            var node = o.node,
                a = o.attrs,
                s = node.style,
                xy,
                newpath = (params.x != a.x || params.y != a.y || params.width != a.width || params.height != a.height || params.r != a.r) && o.type == "rect",
                res = o;

            for (var par in params) if (params[has](par)) {
                a[par] = params[par];
            }
            if (newpath) {
                a.path = rectPath(a.x, a.y, a.width, a.height, a.r);
                o.X = a.x;
                o.Y = a.y;
                o.W = a.width;
                o.H = a.height;
            }
            params.href && (node.href = params.href);
            params.title && (node.title = params.title);
            params.target && (node.target = params.target);
            params.cursor && (s.cursor = params.cursor);
            "blur" in params && o.blur(params.blur);
            if (params.path && o.type == "path" || newpath) {
                node.path = path2vml(a.path);
            }
            if (params.rotation != null) {
                o.rotate(params.rotation, true);
            }
            if (params.translation) {
                xy = Str(params.translation)[split](separator);
                translate.call(o, xy[0], xy[1]);
                if (o._.rt.cx != null) {
                    o._.rt.cx +=+ xy[0];
                    o._.rt.cy +=+ xy[1];
                    o.setBox(o.attrs, xy[0], xy[1]);
                }
            }
            if (params.scale) {
                xy = Str(params.scale)[split](separator);
                o.scale(+xy[0] || 1, +xy[1] || +xy[0] || 1, +xy[2] || null, +xy[3] || null);
            }
            if ("clip-rect" in params) {
                var rect = Str(params["clip-rect"])[split](separator);
                if (rect[length] == 4) {
                    rect[2] = +rect[2] + (+rect[0]);
                    rect[3] = +rect[3] + (+rect[1]);
                    var div = node.clipRect || doc.createElement("div"),
                        dstyle = div.style,
                        group = node.parentNode;
                    dstyle.clip = R.format("rect({1}px {2}px {3}px {0}px)", rect);
                    if (!node.clipRect) {
                        dstyle.position = "absolute";
                        dstyle.top = 0;
                        dstyle.left = 0;
                        dstyle.width = o.paper.width + "px";
                        dstyle.height = o.paper.height + "px";
                        group.parentNode.insertBefore(div, group);
                        div[appendChild](group);
                        node.clipRect = div;
                    }
                }
                if (!params["clip-rect"]) {
                    node.clipRect && (node.clipRect.style.clip = E);
                }
            }
            if (o.type == "image" && params.src) {
                node.src = params.src;
            }
            if (o.type == "image" && params.opacity) {
                node.filterOpacity = ms + ".Alpha(opacity=" + (params.opacity * 100) + ")";
                s.filter = (node.filterMatrix || E) + (node.filterOpacity || E);
            }
            params.font && (s.font = params.font);
            params["font-family"] && (s.fontFamily = '"' + params["font-family"][split](",")[0][rp](/^['"]+|['"]+$/g, E) + '"');
            params["font-size"] && (s.fontSize = params["font-size"]);
            params["font-weight"] && (s.fontWeight = params["font-weight"]);
            params["font-style"] && (s.fontStyle = params["font-style"]);
            if (params.opacity != null ||
                params["stroke-width"] != null ||
                params.fill != null ||
                params.stroke != null ||
                params["stroke-width"] != null ||
                params["stroke-opacity"] != null ||
                params["fill-opacity"] != null ||
                params["stroke-dasharray"] != null ||
                params["stroke-miterlimit"] != null ||
                params["stroke-linejoin"] != null ||
                params["stroke-linecap"] != null) {
                node = o.shape || node;
                var fill = (node.getElementsByTagName(fillString) && node.getElementsByTagName(fillString)[0]),
                    newfill = false;
                !fill && (newfill = fill = createNode(fillString));
                if ("fill-opacity" in params || "opacity" in params) {
                    var opacity = ((+a["fill-opacity"] + 1 || 2) - 1) * ((+a.opacity + 1 || 2) - 1) * ((+R.getRGB(params.fill).o + 1 || 2) - 1);
                    opacity = mmin(mmax(opacity, 0), 1);
                    fill.opacity = opacity;
                }
                params.fill && (fill.on = true);
                if (fill.on == null || params.fill == "none") {
                    fill.on = false;
                }
                if (fill.on && params.fill) {
                    var isURL = params.fill.match(ISURL);
                    if (isURL) {
                        fill.src = isURL[1];
                        fill.type = "tile";
                    } else {
                        fill.color = R.getRGB(params.fill).hex;
                        fill.src = E;
                        fill.type = "solid";
                        if (R.getRGB(params.fill).error && (res.type in {circle: 1, ellipse: 1} || Str(params.fill).charAt() != "r") && addGradientFill(res, params.fill)) {
                            a.fill = "none";
                            a.gradient = params.fill;
                        }
                    }
                }
                newfill && node[appendChild](fill);
                var stroke = (node.getElementsByTagName("stroke") && node.getElementsByTagName("stroke")[0]),
                    newstroke = false;
                !stroke && (newstroke = stroke = createNode("stroke"));
                if ((params.stroke && params.stroke != "none") ||
                    params["stroke-width"] ||
                    params["stroke-opacity"] != null ||
                    params["stroke-dasharray"] ||
                    params["stroke-miterlimit"] ||
                    params["stroke-linejoin"] ||
                    params["stroke-linecap"]) {
                    stroke.on = true;
                }
                (params.stroke == "none" || stroke.on == null || params.stroke == 0 || params["stroke-width"] == 0) && (stroke.on = false);
                var strokeColor = R.getRGB(params.stroke);
                stroke.on && params.stroke && (stroke.color = strokeColor.hex);
                opacity = ((+a["stroke-opacity"] + 1 || 2) - 1) * ((+a.opacity + 1 || 2) - 1) * ((+strokeColor.o + 1 || 2) - 1);
                var width = (toFloat(params["stroke-width"]) || 1) * .75;
                opacity = mmin(mmax(opacity, 0), 1);
                params["stroke-width"] == null && (width = a["stroke-width"]);
                params["stroke-width"] && (stroke.weight = width);
                width && width < 1 && (opacity *= width) && (stroke.weight = 1);
                stroke.opacity = opacity;

                params["stroke-linejoin"] && (stroke.joinstyle = params["stroke-linejoin"] || "miter");
                stroke.miterlimit = params["stroke-miterlimit"] || 8;
                params["stroke-linecap"] && (stroke.endcap = params["stroke-linecap"] == "butt" ? "flat" : params["stroke-linecap"] == "square" ? "square" : "round");
                if (params["stroke-dasharray"]) {
                    var dasharray = {
                        "-": "shortdash",
                        ".": "shortdot",
                        "-.": "shortdashdot",
                        "-..": "shortdashdotdot",
                        ". ": "dot",
                        "- ": "dash",
                        "--": "longdash",
                        "- .": "dashdot",
                        "--.": "longdashdot",
                        "--..": "longdashdotdot"
                    };
                    stroke.dashstyle = dasharray[has](params["stroke-dasharray"]) ? dasharray[params["stroke-dasharray"]] : E;
                }
                newstroke && node[appendChild](stroke);
            }
            if (res.type == "text") {
                s = res.paper.span.style;
                a.font && (s.font = a.font);
                a["font-family"] && (s.fontFamily = a["font-family"]);
                a["font-size"] && (s.fontSize = a["font-size"]);
                a["font-weight"] && (s.fontWeight = a["font-weight"]);
                a["font-style"] && (s.fontStyle = a["font-style"]);
                res.node.string && (res.paper.span.innerHTML = Str(res.node.string)[rp](/</g, "&#60;")[rp](/&/g, "&#38;")[rp](/\n/g, "<br>"));
                res.W = a.w = res.paper.span.offsetWidth;
                res.H = a.h = res.paper.span.offsetHeight;
                res.X = a.x;
                res.Y = a.y + round(res.H / 2);

                // text-anchor emulationm
                switch (a["text-anchor"]) {
                    case "start":
                        res.node.style["v-text-align"] = "left";
                        res.bbx = round(res.W / 2);
                        break;
                    case "end":
                        res.node.style["v-text-align"] = "right";
                        res.bbx = -round(res.W / 2);
                        break;
                    default:
                        res.node.style["v-text-align"] = "center";
                        break;
                }
            }
        };
        addGradientFill = function (o, gradient) {
            o.attrs = o.attrs || {};
            var attrs = o.attrs,
                fill,
                type = "linear",
                fxfy = ".5 .5";
            o.attrs.gradient = gradient;
            gradient = Str(gradient)[rp](radial_gradient, function (all, fx, fy) {
                type = "radial";
                if (fx && fy) {
                    fx = toFloat(fx);
                    fy = toFloat(fy);
                    pow(fx - .5, 2) + pow(fy - .5, 2) > .25 && (fy = math.sqrt(.25 - pow(fx - .5, 2)) * ((fy > .5) * 2 - 1) + .5);
                    fxfy = fx + S + fy;
                }
                return E;
            });
            gradient = gradient[split](/\s*\-\s*/);
            if (type == "linear") {
                var angle = gradient.shift();
                angle = -toFloat(angle);
                if (isNaN(angle)) {
                    return null;
                }
            }
            var dots = parseDots(gradient);
            if (!dots) {
                return null;
            }
            o = o.shape || o.node;
            fill = o.getElementsByTagName(fillString)[0] || createNode(fillString);
            !fill.parentNode && o.appendChild(fill);
            if (dots[length]) {
                fill.on = true;
                fill.method = "none";
                fill.color = dots[0].color;
                fill.color2 = dots[dots[length] - 1].color;
                var clrs = [];
                for (var i = 0, ii = dots[length]; i < ii; i++) {
                    dots[i].offset && clrs[push](dots[i].offset + S + dots[i].color);
                }
                fill.colors && (fill.colors.value = clrs[length] ? clrs[join]() : "0% " + fill.color);
                if (type == "radial") {
                    fill.type = "gradientradial";
                    fill.focus = "100%";
                    fill.focussize = fxfy;
                    fill.focusposition = fxfy;
                } else {
                    fill.type = "gradient";
                    fill.angle = (270 - angle) % 360;
                }
            }
            return 1;
        };
        Element = function (node, group, vml) {
            var Rotation = 0,
                RotX = 0,
                RotY = 0,
                Scale = 1;
            this[0] = node;
            this.id = R._oid++;
            this.node = node;
            node.raphael = this;
            this.X = 0;
            this.Y = 0;
            this.attrs = {};
            this.Group = group;
            this.paper = vml;
            this._ = {
                tx: 0,
                ty: 0,
                rt: {deg:0},
                sx: 1,
                sy: 1
            };
            !vml.bottom && (vml.bottom = this);
            this.prev = vml.top;
            vml.top && (vml.top.next = this);
            vml.top = this;
            this.next = null;
        };
        elproto = Element[proto];
        elproto.rotate = function (deg, cx, cy) {
            if (this.removed) {
                return this;
            }
            if (deg == null) {
                if (this._.rt.cx) {
                    return [this._.rt.deg, this._.rt.cx, this._.rt.cy][join](S);
                }
                return this._.rt.deg;
            }
            deg = Str(deg)[split](separator);
            if (deg[length] - 1) {
                cx = toFloat(deg[1]);
                cy = toFloat(deg[2]);
            }
            deg = toFloat(deg[0]);
            if (cx != null) {
                this._.rt.deg = deg;
            } else {
                this._.rt.deg += deg;
            }
            cy == null && (cx = null);
            this._.rt.cx = cx;
            this._.rt.cy = cy;
            this.setBox(this.attrs, cx, cy);
            this.Group.style.rotation = this._.rt.deg;
            // gradient fix for rotation. TODO
            // var fill = (this.shape || this.node).getElementsByTagName(fillString);
            // fill = fill[0] || {};
            // var b = ((360 - this._.rt.deg) - 270) % 360;
            // !R.is(fill.angle, "undefined") && (fill.angle = b);
            return this;
        };
        elproto.setBox = function (params, cx, cy) {
            if (this.removed) {
                return this;
            }
            var gs = this.Group.style,
                os = (this.shape && this.shape.style) || this.node.style;
            params = params || {};
            for (var i in params) if (params[has](i)) {
                this.attrs[i] = params[i];
            }
            cx = cx || this._.rt.cx;
            cy = cy || this._.rt.cy;
            var attr = this.attrs,
                x,
                y,
                w,
                h;
            switch (this.type) {
                case "circle":
                    x = attr.cx - attr.r;
                    y = attr.cy - attr.r;
                    w = h = attr.r * 2;
                    break;
                case "ellipse":
                    x = attr.cx - attr.rx;
                    y = attr.cy - attr.ry;
                    w = attr.rx * 2;
                    h = attr.ry * 2;
                    break;
                case "image":
                    x = +attr.x;
                    y = +attr.y;
                    w = attr.width || 0;
                    h = attr.height || 0;
                    break;
                case "text":
                    this.textpath.v = ["m", round(attr.x), ", ", round(attr.y - 2), "l", round(attr.x) + 1, ", ", round(attr.y - 2)][join](E);
                    x = attr.x - round(this.W / 2);
                    y = attr.y - this.H / 2;
                    w = this.W;
                    h = this.H;
                    break;
                case "rect":
                case "path":
                    if (!this.attrs.path) {
                        x = 0;
                        y = 0;
                        w = this.paper.width;
                        h = this.paper.height;
                    } else {
                        var dim = pathDimensions(this.attrs.path);
                        x = dim.x;
                        y = dim.y;
                        w = dim.width;
                        h = dim.height;
                    }
                    break;
                default:
                    x = 0;
                    y = 0;
                    w = this.paper.width;
                    h = this.paper.height;
                    break;
            }
            cx = (cx == null) ? x + w / 2 : cx;
            cy = (cy == null) ? y + h / 2 : cy;
            var left = cx - this.paper.width / 2,
                top = cy - this.paper.height / 2, t;
            gs.left != (t = left + "px") && (gs.left = t);
            gs.top != (t = top + "px") && (gs.top = t);
            this.X = pathlike[has](this.type) ? -left : x;
            this.Y = pathlike[has](this.type) ? -top : y;
            this.W = w;
            this.H = h;
            if (pathlike[has](this.type)) {
                os.left != (t = -left * zoom + "px") && (os.left = t);
                os.top != (t = -top * zoom + "px") && (os.top = t);
            } else if (this.type == "text") {
                os.left != (t = -left + "px") && (os.left = t);
                os.top != (t = -top + "px") && (os.top = t);
            } else {
                gs.width != (t = this.paper.width + "px") && (gs.width = t);
                gs.height != (t = this.paper.height + "px") && (gs.height = t);
                os.left != (t = x - left + "px") && (os.left = t);
                os.top != (t = y - top + "px") && (os.top = t);
                os.width != (t = w + "px") && (os.width = t);
                os.height != (t = h + "px") && (os.height = t);
            }
        };
        elproto.hide = function () {
            !this.removed && (this.Group.style.display = "none");
            return this;
        };
        elproto.show = function () {
            !this.removed && (this.Group.style.display = "block");
            return this;
        };
        elproto.getBBox = function () {
            if (this.removed) {
                return this;
            }
            if (pathlike[has](this.type)) {
                return pathDimensions(this.attrs.path);
            }
            return {
                x: this.X + (this.bbx || 0),
                y: this.Y,
                width: this.W,
                height: this.H
            };
        };
        elproto.remove = function () {
            if (this.removed) {
                return;
            }
            tear(this, this.paper);
            this.node.parentNode.removeChild(this.node);
            this.Group.parentNode.removeChild(this.Group);
            this.shape && this.shape.parentNode.removeChild(this.shape);
            for (var i in this) {
                delete this[i];
            }
            this.removed = true;
        };
        elproto.attr = function (name, value) {
            if (this.removed) {
                return this;
            }
            if (name == null) {
                var res = {};
                for (var i in this.attrs) if (this.attrs[has](i)) {
                    res[i] = this.attrs[i];
                }
                this._.rt.deg && (res.rotation = this.rotate());
                (this._.sx != 1 || this._.sy != 1) && (res.scale = this.scale());
                res.gradient && res.fill == "none" && (res.fill = res.gradient) && delete res.gradient;
                return res;
            }
            if (value == null && R.is(name, "string")) {
                if (name == "translation") {
                    return translate.call(this);
                }
                if (name == "rotation") {
                    return this.rotate();
                }
                if (name == "scale") {
                    return this.scale();
                }
                if (name == fillString && this.attrs.fill == "none" && this.attrs.gradient) {
                    return this.attrs.gradient;
                }
                return this.attrs[name];
            }
            if (this.attrs && value == null && R.is(name, array)) {
                var ii, values = {};
                for (i = 0, ii = name[length]; i < ii; i++) {
                    values[name[i]] = this.attr(name[i]);
                }
                return values;
            }
            var params;
            if (value != null) {
                params = {};
                params[name] = value;
            }
            value == null && R.is(name, "object") && (params = name);
            if (params) {
                for (var key in this.paper.customAttributes) if (this.paper.customAttributes[has](key) && params[has](key) && R.is(this.paper.customAttributes[key], "function")) {
                    var par = this.paper.customAttributes[key].apply(this, [][concat](params[key]));
                    this.attrs[key] = params[key];
                    for (var subkey in par) if (par[has](subkey)) {
                        params[subkey] = par[subkey];
                    }
                }
                if (params.text && this.type == "text") {
                    this.node.string = params.text;
                }
                setFillAndStroke(this, params);
                if (params.gradient && (({circle: 1, ellipse: 1})[has](this.type) || Str(params.gradient).charAt() != "r")) {
                    addGradientFill(this, params.gradient);
                }
                (!pathlike[has](this.type) || this._.rt.deg) && this.setBox(this.attrs);
            }
            return this;
        };
        elproto.toFront = function () {
            !this.removed && this.Group.parentNode[appendChild](this.Group);
            this.paper.top != this && tofront(this, this.paper);
            return this;
        };
        elproto.toBack = function () {
            if (this.removed) {
                return this;
            }
            if (this.Group.parentNode.firstChild != this.Group) {
                this.Group.parentNode.insertBefore(this.Group, this.Group.parentNode.firstChild);
                toback(this, this.paper);
            }
            return this;
        };
        elproto.insertAfter = function (element) {
            if (this.removed) {
                return this;
            }
            if (element.constructor == Set) {
                element = element[element.length - 1];
            }
            if (element.Group.nextSibling) {
                element.Group.parentNode.insertBefore(this.Group, element.Group.nextSibling);
            } else {
                element.Group.parentNode[appendChild](this.Group);
            }
            insertafter(this, element, this.paper);
            return this;
        };
        elproto.insertBefore = function (element) {
            if (this.removed) {
                return this;
            }
            if (element.constructor == Set) {
                element = element[0];
            }
            element.Group.parentNode.insertBefore(this.Group, element.Group);
            insertbefore(this, element, this.paper);
            return this;
        };
        elproto.blur = function (size) {
            var s = this.node.runtimeStyle,
                f = s.filter;
            f = f.replace(blurregexp, E);
            if (+size !== 0) {
                this.attrs.blur = size;
                s.filter = f + S + ms + ".Blur(pixelradius=" + (+size || 1.5) + ")";
                s.margin = R.format("-{0}px 0 0 -{0}px", round(+size || 1.5));
            } else {
                s.filter = f;
                s.margin = 0;
                delete this.attrs.blur;
            }
        };

        theCircle = function (vml, x, y, r) {
            var g = createNode("group"),
                o = createNode("oval"),
                ol = o.style;
            g.style.cssText = "position:absolute;left:0;top:0;width:" + vml.width + "px;height:" + vml.height + "px";
            g.coordsize = coordsize;
            g.coordorigin = vml.coordorigin;
            g[appendChild](o);
            var res = new Element(o, g, vml);
            res.type = "circle";
            setFillAndStroke(res, {stroke: "#000", fill: "none"});
            res.attrs.cx = x;
            res.attrs.cy = y;
            res.attrs.r = r;
            res.setBox({x: x - r, y: y - r, width: r * 2, height: r * 2});
            vml.canvas[appendChild](g);
            return res;
        };
        function rectPath(x, y, w, h, r) {
            if (r) {
                return R.format("M{0},{1}l{2},0a{3},{3},0,0,1,{3},{3}l0,{5}a{3},{3},0,0,1,{4},{3}l{6},0a{3},{3},0,0,1,{4},{4}l0,{7}a{3},{3},0,0,1,{3},{4}z", x + r, y, w - r * 2, r, -r, h - r * 2, r * 2 - w, r * 2 - h);
            } else {
                return R.format("M{0},{1}l{2},0,0,{3},{4},0z", x, y, w, h, -w);
            }
        }
        theRect = function (vml, x, y, w, h, r) {
            var path = rectPath(x, y, w, h, r),
                res = vml.path(path),
                a = res.attrs;
            res.X = a.x = x;
            res.Y = a.y = y;
            res.W = a.width = w;
            res.H = a.height = h;
            a.r = r;
            a.path = path;
            res.type = "rect";
            return res;
        };
        theEllipse = function (vml, x, y, rx, ry) {
            var g = createNode("group"),
                o = createNode("oval"),
                ol = o.style;
            g.style.cssText = "position:absolute;left:0;top:0;width:" + vml.width + "px;height:" + vml.height + "px";
            g.coordsize = coordsize;
            g.coordorigin = vml.coordorigin;
            g[appendChild](o);
            var res = new Element(o, g, vml);
            res.type = "ellipse";
            setFillAndStroke(res, {stroke: "#000"});
            res.attrs.cx = x;
            res.attrs.cy = y;
            res.attrs.rx = rx;
            res.attrs.ry = ry;
            res.setBox({x: x - rx, y: y - ry, width: rx * 2, height: ry * 2});
            vml.canvas[appendChild](g);
            return res;
        };
        theImage = function (vml, src, x, y, w, h) {
            var g = createNode("group"),
                o = createNode("image");
            g.style.cssText = "position:absolute;left:0;top:0;width:" + vml.width + "px;height:" + vml.height + "px";
            g.coordsize = coordsize;
            g.coordorigin = vml.coordorigin;
            o.src = src;
            g[appendChild](o);
            var res = new Element(o, g, vml);
            res.type = "image";
            res.attrs.src = src;
            res.attrs.x = x;
            res.attrs.y = y;
            res.attrs.w = w;
            res.attrs.h = h;
            res.setBox({x: x, y: y, width: w, height: h});
            vml.canvas[appendChild](g);
            return res;
        };
        theText = function (vml, x, y, text) {
            var g = createNode("group"),
                el = createNode("shape"),
                ol = el.style,
                path = createNode("path"),
                ps = path.style,
                o = createNode("textpath");
            g.style.cssText = "position:absolute;left:0;top:0;width:" + vml.width + "px;height:" + vml.height + "px";
            g.coordsize = coordsize;
            g.coordorigin = vml.coordorigin;
            path.v = R.format("m{0},{1}l{2},{1}", round(x * 10), round(y * 10), round(x * 10) + 1);
            path.textpathok = true;
            ol.width = vml.width;
            ol.height = vml.height;
            o.string = Str(text);
            o.on = true;
            el[appendChild](o);
            el[appendChild](path);
            g[appendChild](el);
            var res = new Element(o, g, vml);
            res.shape = el;
            res.textpath = path;
            res.type = "text";
            res.attrs.text = text;
            res.attrs.x = x;
            res.attrs.y = y;
            res.attrs.w = 1;
            res.attrs.h = 1;
            setFillAndStroke(res, {font: availableAttrs.font, stroke: "none", fill: "#000"});
            res.setBox();
            vml.canvas[appendChild](g);
            return res;
        };
        setSize = function (width, height) {
            var cs = this.canvas.style;
            width == +width && (width += "px");
            height == +height && (height += "px");
            cs.width = width;
            cs.height = height;
            cs.clip = "rect(0 " + width + " " + height + " 0)";
            return this;
        };
        var createNode;
        doc.createStyleSheet().addRule(".rvml", "behavior:url(#default#VML)");
        try {
            !doc.namespaces.rvml && doc.namespaces.add("rvml", "urn:schemas-microsoft-com:vml");
            createNode = function (tagName) {
                return doc.createElement('<rvml:' + tagName + ' class="rvml">');
            };
        } catch (e) {
            createNode = function (tagName) {
                return doc.createElement('<' + tagName + ' xmlns="urn:schemas-microsoft.com:vml" class="rvml">');
            };
        }
        create = function () {
            var con = getContainer[apply](0, arguments),
                container = con.container,
                height = con.height,
                s,
                width = con.width,
                x = con.x,
                y = con.y;
            if (!container) {
                throw new Error("VML container not found.");
            }
            var res = new Paper,
                c = res.canvas = doc.createElement("div"),
                cs = c.style;
            x = x || 0;
            y = y || 0;
            width = width || 512;
            height = height || 342;
            width == +width && (width += "px");
            height == +height && (height += "px");
            res.width = 1e3;
            res.height = 1e3;
            res.coordsize = zoom * 1e3 + S + zoom * 1e3;
            res.coordorigin = "0 0";
            res.span = doc.createElement("span");
            res.span.style.cssText = "position:absolute;left:-9999em;top:-9999em;padding:0;margin:0;line-height:1;display:inline;";
            c[appendChild](res.span);
            cs.cssText = R.format("top:0;left:0;width:{0};height:{1};display:inline-block;position:relative;clip:rect(0 {0} {1} 0);overflow:hidden", width, height);
            if (container == 1) {
                doc.body[appendChild](c);
                cs.left = x + "px";
                cs.top = y + "px";
                cs.position = "absolute";
            } else {
                if (container.firstChild) {
                    container.insertBefore(c, container.firstChild);
                } else {
                    container[appendChild](c);
                }
            }
            plugins.call(res, res, R.fn);
            return res;
        };
        paperproto.clear = function () {
            this.canvas.innerHTML = E;
            this.span = doc.createElement("span");
            this.span.style.cssText = "position:absolute;left:-9999em;top:-9999em;padding:0;margin:0;line-height:1;display:inline;";
            this.canvas[appendChild](this.span);
            this.bottom = this.top = null;
        };
        paperproto.remove = function () {
            this.canvas.parentNode.removeChild(this.canvas);
            for (var i in this) {
                this[i] = removed(i);
            }
            return true;
        };
    }

    // rest
    // WebKit rendering bug workaround method
    var version = navigator.userAgent.match(/Version\/(.*?)\s/);
    if ((navigator.vendor == "Apple Computer, Inc.") && (version && version[1] < 4 || navigator.platform.slice(0, 2) == "iP")) {
        paperproto.safari = function () {
            var rect = this.rect(-99, -99, this.width + 99, this.height + 99).attr({stroke: "none"});
            win.setTimeout(function () {rect.remove();});
        };
    } else {
        paperproto.safari = function () {};
    }

    // Events
    var preventDefault = function () {
            this.returnValue = false;
        },
        preventTouch = function () {
            return this.originalEvent.preventDefault();
        },
        stopPropagation = function () {
            this.cancelBubble = true;
        },
        stopTouch = function () {
            return this.originalEvent.stopPropagation();
        },
        addEvent = (function () {
            if (doc.addEventListener) {
                return function (obj, type, fn, element) {
                    var realName = supportsTouch && touchMap[type] ? touchMap[type] : type;
                    var f = function (e) {
                        if (supportsTouch && touchMap[has](type)) {
                            for (var i = 0, ii = e.targetTouches && e.targetTouches.length; i < ii; i++) {
                                if (e.targetTouches[i].target == obj) {
                                    var olde = e;
                                    e = e.targetTouches[i];
                                    e.originalEvent = olde;
                                    e.preventDefault = preventTouch;
                                    e.stopPropagation = stopTouch;
                                    break;
                                }
                            }
                        }
                        return fn.call(element, e);
                    };
                    obj.addEventListener(realName, f, false);
                    return function () {
                        obj.removeEventListener(realName, f, false);
                        return true;
                    };
                };
            } else if (doc.attachEvent) {
                return function (obj, type, fn, element) {
                    var f = function (e) {
                        e = e || win.event;
                        e.preventDefault = e.preventDefault || preventDefault;
                        e.stopPropagation = e.stopPropagation || stopPropagation;
                        return fn.call(element, e);
                    };
                    obj.attachEvent("on" + type, f);
                    var detacher = function () {
                        obj.detachEvent("on" + type, f);
                        return true;
                    };
                    return detacher;
                };
            }
        })(),
        drag = [],
        dragMove = function (e) {
            var x = e.clientX,
                y = e.clientY,
                scrollY = doc.documentElement.scrollTop || doc.body.scrollTop,
                scrollX = doc.documentElement.scrollLeft || doc.body.scrollLeft,
                dragi,
                j = drag.length;
            while (j--) {
                dragi = drag[j];
                if (supportsTouch) {
                    var i = e.touches.length,
                        touch;
                    while (i--) {
                        touch = e.touches[i];
                        if (touch.identifier == dragi.el._drag.id) {
                            x = touch.clientX;
                            y = touch.clientY;
                            (e.originalEvent ? e.originalEvent : e).preventDefault();
                            break;
                        }
                    }
                } else {
                    e.preventDefault();
                }
                x += scrollX;
                y += scrollY;
                dragi.move && dragi.move.call(dragi.move_scope || dragi.el, x - dragi.el._drag.x, y - dragi.el._drag.y, x, y, e);
            }
        },
        dragUp = function (e) {
            R.unmousemove(dragMove).unmouseup(dragUp);
            var i = drag.length,
                dragi;
            while (i--) {
                dragi = drag[i];
                dragi.el._drag = {};
                dragi.end && dragi.end.call(dragi.end_scope || dragi.start_scope || dragi.move_scope || dragi.el, e);
            }
            drag = [];
        };
    for (var i = events[length]; i--;) {
        (function (eventName) {
            R[eventName] = Element[proto][eventName] = function (fn, scope) {
                if (R.is(fn, "function")) {
                    this.events = this.events || [];
                    this.events.push({name: eventName, f: fn, unbind: addEvent(this.shape || this.node || doc, eventName, fn, scope || this)});
                }
                return this;
            };
            R["un" + eventName] = Element[proto]["un" + eventName] = function (fn) {
                var events = this.events,
                    l = events[length];
                while (l--) if (events[l].name == eventName && events[l].f == fn) {
                    events[l].unbind();
                    events.splice(l, 1);
                    !events.length && delete this.events;
                    return this;
                }
                return this;
            };
        })(events[i]);
    }
    elproto.hover = function (f_in, f_out, scope_in, scope_out) {
        return this.mouseover(f_in, scope_in).mouseout(f_out, scope_out || scope_in);
    };
    elproto.unhover = function (f_in, f_out) {
        return this.unmouseover(f_in).unmouseout(f_out);
    };
    elproto.drag = function (onmove, onstart, onend, move_scope, start_scope, end_scope) {
        this._drag = {};
        this.mousedown(function (e) {
            (e.originalEvent || e).preventDefault();
            var scrollY = doc.documentElement.scrollTop || doc.body.scrollTop,
                scrollX = doc.documentElement.scrollLeft || doc.body.scrollLeft;
            this._drag.x = e.clientX + scrollX;
            this._drag.y = e.clientY + scrollY;
            this._drag.id = e.identifier;
            onstart && onstart.call(start_scope || move_scope || this, e.clientX + scrollX, e.clientY + scrollY, e);
            !drag.length && R.mousemove(dragMove).mouseup(dragUp);
            drag.push({el: this, move: onmove, end: onend, move_scope: move_scope, start_scope: start_scope, end_scope: end_scope});
        });
        return this;
    };
    elproto.undrag = function (onmove, onstart, onend) {
        var i = drag.length;
        while (i--) {
            drag[i].el == this && (drag[i].move == onmove && drag[i].end == onend) && drag.splice(i++, 1);
        }
        !drag.length && R.unmousemove(dragMove).unmouseup(dragUp);
    };
    paperproto.circle = function (x, y, r) {
        return theCircle(this, x || 0, y || 0, r || 0);
    };
    paperproto.rect = function (x, y, w, h, r) {
        return theRect(this, x || 0, y || 0, w || 0, h || 0, r || 0);
    };
    paperproto.ellipse = function (x, y, rx, ry) {
        return theEllipse(this, x || 0, y || 0, rx || 0, ry || 0);
    };
    paperproto.path = function (pathString) {
        pathString && !R.is(pathString, string) && !R.is(pathString[0], array) && (pathString += E);
        return thePath(R.format[apply](R, arguments), this);
    };
    paperproto.image = function (src, x, y, w, h) {
        return theImage(this, src || "about:blank", x || 0, y || 0, w || 0, h || 0);
    };
    paperproto.text = function (x, y, text) {
        return theText(this, x || 0, y || 0, Str(text));
    };
    paperproto.set = function (itemsArray) {
        arguments[length] > 1 && (itemsArray = Array[proto].splice.call(arguments, 0, arguments[length]));
        return new Set(itemsArray);
    };
    paperproto.setSize = setSize;
    paperproto.top = paperproto.bottom = null;
    paperproto.raphael = R;
    function x_y() {
        return this.x + S + this.y;
    }
    elproto.resetScale = function () {
        if (this.removed) {
            return this;
        }
        this._.sx = 1;
        this._.sy = 1;
        this.attrs.scale = "1 1";
    };
    elproto.scale = function (x, y, cx, cy) {
        if (this.removed) {
            return this;
        }
        if (x == null && y == null) {
            return {
                x: this._.sx,
                y: this._.sy,
                toString: x_y
            };
        }
        y = y || x;
        !+y && (y = x);
        var dx,
            dy,
            dcx,
            dcy,
            a = this.attrs;
        if (x != 0) {
            var bb = this.getBBox(),
                rcx = bb.x + bb.width / 2,
                rcy = bb.y + bb.height / 2,
                kx = abs(x / this._.sx),
                ky = abs(y / this._.sy);
            cx = (+cx || cx == 0) ? cx : rcx;
            cy = (+cy || cy == 0) ? cy : rcy;
            var posx = this._.sx > 0,
                posy = this._.sy > 0,
                dirx = ~~(x / abs(x)),
                diry = ~~(y / abs(y)),
                dkx = kx * dirx,
                dky = ky * diry,
                s = this.node.style,
                ncx = cx + abs(rcx - cx) * dkx * (rcx > cx == posx ? 1 : -1),
                ncy = cy + abs(rcy - cy) * dky * (rcy > cy == posy ? 1 : -1),
                fr = (x * dirx > y * diry ? ky : kx);
            switch (this.type) {
                case "rect":
                case "image":
                    var neww = a.width * kx,
                        newh = a.height * ky;
                    this.attr({
                        height: newh,
                        r: a.r * fr,
                        width: neww,
                        x: ncx - neww / 2,
                        y: ncy - newh / 2
                    });
                    break;
                case "circle":
                case "ellipse":
                    this.attr({
                        rx: a.rx * kx,
                        ry: a.ry * ky,
                        r: a.r * fr,
                        cx: ncx,
                        cy: ncy
                    });
                    break;
                case "text":
                    this.attr({
                        x: ncx,
                        y: ncy
                    });
                    break;
                case "path":
                    var path = pathToRelative(a.path),
                        skip = true,
                        fx = posx ? dkx : kx,
                        fy = posy ? dky : ky;
                    for (var i = 0, ii = path[length]; i < ii; i++) {
                        var p = path[i],
                            P0 = upperCase.call(p[0]);
                        if (P0 == "M" && skip) {
                            continue;
                        } else {
                            skip = false;
                        }
                        if (P0 == "A") {
                            p[path[i][length] - 2] *= fx;
                            p[path[i][length] - 1] *= fy;
                            p[1] *= kx;
                            p[2] *= ky;
                            p[5] = +(dirx + diry ? !!+p[5] : !+p[5]);
                        } else if (P0 == "H") {
                            for (var j = 1, jj = p[length]; j < jj; j++) {
                                p[j] *= fx;
                            }
                        } else if (P0 == "V") {
                            for (j = 1, jj = p[length]; j < jj; j++) {
                                p[j] *= fy;
                            }
                        } else {
                            for (j = 1, jj = p[length]; j < jj; j++) {
                                p[j] *= (j % 2) ? fx : fy;
                            }
                        }
                    }
                    var dim2 = pathDimensions(path);
                    dx = ncx - dim2.x - dim2.width / 2;
                    dy = ncy - dim2.y - dim2.height / 2;
                    path[0][1] += dx;
                    path[0][2] += dy;
                    this.attr({path: path});
                    break;
            }
            if (this.type in {text: 1, image:1} && (dirx != 1 || diry != 1)) {
                if (this.transformations) {
                    this.transformations[2] = "scale("[concat](dirx, ",", diry, ")");
                    this.node[setAttribute]("transform", this.transformations[join](S));
                    dx = (dirx == -1) ? -a.x - (neww || 0) : a.x;
                    dy = (diry == -1) ? -a.y - (newh || 0) : a.y;
                    this.attr({x: dx, y: dy});
                    a.fx = dirx - 1;
                    a.fy = diry - 1;
                } else {
                    this.node.filterMatrix = ms + ".Matrix(M11="[concat](dirx,
                        ", M12=0, M21=0, M22=", diry,
                        ", Dx=0, Dy=0, sizingmethod='auto expand', filtertype='bilinear')");
                    s.filter = (this.node.filterMatrix || E) + (this.node.filterOpacity || E);
                }
            } else {
                if (this.transformations) {
                    this.transformations[2] = E;
                    this.node[setAttribute]("transform", this.transformations[join](S));
                    a.fx = 0;
                    a.fy = 0;
                } else {
                    this.node.filterMatrix = E;
                    s.filter = (this.node.filterMatrix || E) + (this.node.filterOpacity || E);
                }
            }
            a.scale = [x, y, cx, cy][join](S);
            this._.sx = x;
            this._.sy = y;
        }
        return this;
    };
    elproto.clone = function () {
        if (this.removed) {
            return null;
        }
        var attr = this.attr();
        delete attr.scale;
        delete attr.translation;
        return this.paper[this.type]().attr(attr);
    };
    var curveslengths = {},
        getPointAtSegmentLength = function (p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y, length) {
            var len = 0,
                precision = 100,
                name = [p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y].join(),
                cache = curveslengths[name],
                old, dot;
            !cache && (curveslengths[name] = cache = {data: []});
            cache.timer && clearTimeout(cache.timer);
            cache.timer = setTimeout(function () {delete curveslengths[name];}, 2000);
            if (length != null) {
                var total = getPointAtSegmentLength(p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y);
                precision = ~~total * 10;
            }
            for (var i = 0; i < precision + 1; i++) {
                if (cache.data[length] > i) {
                    dot = cache.data[i * precision];
                } else {
                    dot = R.findDotsAtSegment(p1x, p1y, c1x, c1y, c2x, c2y, p2x, p2y, i / precision);
                    cache.data[i] = dot;
                }
                i && (len += pow(pow(old.x - dot.x, 2) + pow(old.y - dot.y, 2), .5));
                if (length != null && len >= length) {
                    return dot;
                }
                old = dot;
            }
            if (length == null) {
                return len;
            }
        },
        getLengthFactory = function (istotal, subpath) {
            return function (path, length, onlystart) {
                path = path2curve(path);
                var x, y, p, l, sp = "", subpaths = {}, point,
                    len = 0;
                for (var i = 0, ii = path.length; i < ii; i++) {
                    p = path[i];
                    if (p[0] == "M") {
                        x = +p[1];
                        y = +p[2];
                    } else {
                        l = getPointAtSegmentLength(x, y, p[1], p[2], p[3], p[4], p[5], p[6]);
                        if (len + l > length) {
                            if (subpath && !subpaths.start) {
                                point = getPointAtSegmentLength(x, y, p[1], p[2], p[3], p[4], p[5], p[6], length - len);
                                sp += ["C", point.start.x, point.start.y, point.m.x, point.m.y, point.x, point.y];
                                if (onlystart) {return sp;}
                                subpaths.start = sp;
                                sp = ["M", point.x, point.y + "C", point.n.x, point.n.y, point.end.x, point.end.y, p[5], p[6]][join]();
                                len += l;
                                x = +p[5];
                                y = +p[6];
                                continue;
                            }
                            if (!istotal && !subpath) {
                                point = getPointAtSegmentLength(x, y, p[1], p[2], p[3], p[4], p[5], p[6], length - len);
                                return {x: point.x, y: point.y, alpha: point.alpha};
                            }
                        }
                        len += l;
                        x = +p[5];
                        y = +p[6];
                    }
                    sp += p;
                }
                subpaths.end = sp;
                point = istotal ? len : subpath ? subpaths : R.findDotsAtSegment(x, y, p[1], p[2], p[3], p[4], p[5], p[6], 1);
                point.alpha && (point = {x: point.x, y: point.y, alpha: point.alpha});
                return point;
            };
        };
    var getTotalLength = getLengthFactory(1),
        getPointAtLength = getLengthFactory(),
        getSubpathsAtLength = getLengthFactory(0, 1);
    elproto.getTotalLength = function () {
        if (this.type != "path") {return;}
        if (this.node.getTotalLength) {
            return this.node.getTotalLength();
        }
        return getTotalLength(this.attrs.path);
    };
    elproto.getPointAtLength = function (length) {
        if (this.type != "path") {return;}
        return getPointAtLength(this.attrs.path, length);
    };
    elproto.getSubpath = function (from, to) {
        if (this.type != "path") {return;}
        if (abs(this.getTotalLength() - to) < "1e-6") {
            return getSubpathsAtLength(this.attrs.path, from).end;
        }
        var a = getSubpathsAtLength(this.attrs.path, to, 1);
        return from ? getSubpathsAtLength(a, from).end : a;
    };

    // animation easing formulas
    R.easing_formulas = {
        linear: function (n) {
            return n;
        },
        "<": function (n) {
            return pow(n, 3);
        },
        ">": function (n) {
            return pow(n - 1, 3) + 1;
        },
        "<>": function (n) {
            n = n * 2;
            if (n < 1) {
                return pow(n, 3) / 2;
            }
            n -= 2;
            return (pow(n, 3) + 2) / 2;
        },
        backIn: function (n) {
            var s = 1.70158;
            return n * n * ((s + 1) * n - s);
        },
        backOut: function (n) {
            n = n - 1;
            var s = 1.70158;
            return n * n * ((s + 1) * n + s) + 1;
        },
        elastic: function (n) {
            if (n == 0 || n == 1) {
                return n;
            }
            var p = .3,
                s = p / 4;
            return pow(2, -10 * n) * math.sin((n - s) * (2 * PI) / p) + 1;
        },
        bounce: function (n) {
            var s = 7.5625,
                p = 2.75,
                l;
            if (n < (1 / p)) {
                l = s * n * n;
            } else {
                if (n < (2 / p)) {
                    n -= (1.5 / p);
                    l = s * n * n + .75;
                } else {
                    if (n < (2.5 / p)) {
                        n -= (2.25 / p);
                        l = s * n * n + .9375;
                    } else {
                        n -= (2.625 / p);
                        l = s * n * n + .984375;
                    }
                }
            }
            return l;
        }
    };

    var animationElements = [],
        animation = function () {
            var Now = +new Date;
            for (var l = 0; l < animationElements[length]; l++) {
                var e = animationElements[l];
                if (e.stop || e.el.removed) {
                    continue;
                }
                var time = Now - e.start,
                    ms = e.ms,
                    easing = e.easing,
                    from = e.from,
                    diff = e.diff,
                    to = e.to,
                    t = e.t,
                    that = e.el,
                    set = {},
                    now;
                if (time < ms) {
                    var pos = easing(time / ms);
                    for (var attr in from) if (from[has](attr)) {
                        switch (availableAnimAttrs[attr]) {
                            case "along":
                                now = pos * ms * diff[attr];
                                to.back && (now = to.len - now);
                                var point = getPointAtLength(to[attr], now);
                                that.translate(diff.sx - diff.x || 0, diff.sy - diff.y || 0);
                                diff.x = point.x;
                                diff.y = point.y;
                                that.translate(point.x - diff.sx, point.y - diff.sy);
                                to.rot && that.rotate(diff.r + point.alpha, point.x, point.y);
                                break;
                            case nu:
                                now = +from[attr] + pos * ms * diff[attr];
                                break;
                            case "colour":
                                now = "rgb(" + [
                                    upto255(round(from[attr].r + pos * ms * diff[attr].r)),
                                    upto255(round(from[attr].g + pos * ms * diff[attr].g)),
                                    upto255(round(from[attr].b + pos * ms * diff[attr].b))
                                ][join](",") + ")";
                                break;
                            case "path":
                                now = [];
                                for (var i = 0, ii = from[attr][length]; i < ii; i++) {
                                    now[i] = [from[attr][i][0]];
                                    for (var j = 1, jj = from[attr][i][length]; j < jj; j++) {
                                        now[i][j] = +from[attr][i][j] + pos * ms * diff[attr][i][j];
                                    }
                                    now[i] = now[i][join](S);
                                }
                                now = now[join](S);
                                break;
                            case "csv":
                                switch (attr) {
                                    case "translation":
                                        var x = pos * ms * diff[attr][0] - t.x,
                                            y = pos * ms * diff[attr][1] - t.y;
                                        t.x += x;
                                        t.y += y;
                                        now = x + S + y;
                                        break;
                                    case "rotation":
                                        now = +from[attr][0] + pos * ms * diff[attr][0];
                                        from[attr][1] && (now += "," + from[attr][1] + "," + from[attr][2]);
                                        break;
                                    case "scale":
                                        now = [+from[attr][0] + pos * ms * diff[attr][0], +from[attr][1] + pos * ms * diff[attr][1], (2 in to[attr] ? to[attr][2] : E), (3 in to[attr] ? to[attr][3] : E)][join](S);
                                        break;
                                    case "clip-rect":
                                        now = [];
                                        i = 4;
                                        while (i--) {
                                            now[i] = +from[attr][i] + pos * ms * diff[attr][i];
                                        }
                                        break;
                                }
                                break;
                            default:
                                var from2 = [].concat(from[attr]);
                                now = [];
                                i = that.paper.customAttributes[attr].length;
                                while (i--) {
                                    now[i] = +from2[i] + pos * ms * diff[attr][i];
                                }
                                break;
                        }
                        set[attr] = now;
                    }
                    that.attr(set);
                    that._run && that._run.call(that);
                } else {
                    if (to.along) {
                        point = getPointAtLength(to.along, to.len * !to.back);
                        that.translate(diff.sx - (diff.x || 0) + point.x - diff.sx, diff.sy - (diff.y || 0) + point.y - diff.sy);
                        to.rot && that.rotate(diff.r + point.alpha, point.x, point.y);
                    }
                    (t.x || t.y) && that.translate(-t.x, -t.y);
                    to.scale && (to.scale += E);
                    that.attr(to);
                    animationElements.splice(l--, 1);
                }
            }
            R.svg && that && that.paper && that.paper.safari();
            animationElements[length] && setTimeout(animation);
        },
        keyframesRun = function (attr, element, time, prev, prevcallback) {
            var dif = time - prev;
            element.timeouts.push(setTimeout(function () {
                R.is(prevcallback, "function") && prevcallback.call(element);
                element.animate(attr, dif, attr.easing);
            }, prev));
        },
        upto255 = function (color) {
            return mmax(mmin(color, 255), 0);
        },
        translate = function (x, y) {
            if (x == null) {
                return {x: this._.tx, y: this._.ty, toString: x_y};
            }
            this._.tx += +x;
            this._.ty += +y;
            switch (this.type) {
                case "circle":
                case "ellipse":
                    this.attr({cx: +x + this.attrs.cx, cy: +y + this.attrs.cy});
                    break;
                case "rect":
                case "image":
                case "text":
                    this.attr({x: +x + this.attrs.x, y: +y + this.attrs.y});
                    break;
                case "path":
                    var path = pathToRelative(this.attrs.path);
                    path[0][1] += +x;
                    path[0][2] += +y;
                    this.attr({path: path});
                    break;
            }
            return this;
        };
    elproto.animateWith = function (element, params, ms, easing, callback) {
        for (var i = 0, ii = animationElements.length; i < ii; i++) {
            if (animationElements[i].el.id == element.id) {
                params.start = animationElements[i].start;
            }
        }
        return this.animate(params, ms, easing, callback);
    };
    elproto.animateAlong = along();
    elproto.animateAlongBack = along(1);
    function along(isBack) {
        return function (path, ms, rotate, callback) {
            var params = {back: isBack};
            R.is(rotate, "function") ? (callback = rotate) : (params.rot = rotate);
            path && path.constructor == Element && (path = path.attrs.path);
            path && (params.along = path);
            return this.animate(params, ms, callback);
        };
    }
    function CubicBezierAtTime(t, p1x, p1y, p2x, p2y, duration) {
        var cx = 3 * p1x,
            bx = 3 * (p2x - p1x) - cx,
            ax = 1 - cx - bx,
            cy = 3 * p1y,
            by = 3 * (p2y - p1y) - cy,
            ay = 1 - cy - by;
        function sampleCurveX(t) {
            return ((ax * t + bx) * t + cx) * t;
        }
        function solve(x, epsilon) {
            var t = solveCurveX(x, epsilon);
            return ((ay * t + by) * t + cy) * t;
        }
        function solveCurveX(x, epsilon) {
            var t0, t1, t2, x2, d2, i;
            for(t2 = x, i = 0; i < 8; i++) {
                x2 = sampleCurveX(t2) - x;
                if (abs(x2) < epsilon) {
                    return t2;
                }
                d2 = (3 * ax * t2 + 2 * bx) * t2 + cx;
                if (abs(d2) < 1e-6) {
                    break;
                }
                t2 = t2 - x2 / d2;
            }
            t0 = 0;
            t1 = 1;
            t2 = x;
            if (t2 < t0) {
                return t0;
            }
            if (t2 > t1) {
                return t1;
            }
            while (t0 < t1) {
                x2 = sampleCurveX(t2);
                if (abs(x2 - x) < epsilon) {
                    return t2;
                }
                if (x > x2) {
                    t0 = t2;
                } else {
                    t1 = t2;
                }
                t2 = (t1 - t0) / 2 + t0;
            }
            return t2;
        }
        return solve(t, 1 / (200 * duration));
    }
    elproto.onAnimation = function (f) {
        this._run = f || 0;
        return this;
    };
    elproto.animate = function (params, ms, easing, callback) {
        var element = this;
        element.timeouts = element.timeouts || [];
        if (R.is(easing, "function") || !easing) {
            callback = easing || null;
        }
        if (element.removed) {
            callback && callback.call(element);
            return element;
        }
        var from = {},
            to = {},
            animateable = false,
            diff = {};
        for (var attr in params) if (params[has](attr)) {
            if (availableAnimAttrs[has](attr) || element.paper.customAttributes[has](attr)) {
                animateable = true;
                from[attr] = element.attr(attr);
                (from[attr] == null) && (from[attr] = availableAttrs[attr]);
                to[attr] = params[attr];
                switch (availableAnimAttrs[attr]) {
                    case "along":
                        var len = getTotalLength(params[attr]);
                        var point = getPointAtLength(params[attr], len * !!params.back);
                        var bb = element.getBBox();
                        diff[attr] = len / ms;
                        diff.tx = bb.x;
                        diff.ty = bb.y;
                        diff.sx = point.x;
                        diff.sy = point.y;
                        to.rot = params.rot;
                        to.back = params.back;
                        to.len = len;
                        params.rot && (diff.r = toFloat(element.rotate()) || 0);
                        break;
                    case nu:
                        diff[attr] = (to[attr] - from[attr]) / ms;
                        break;
                    case "colour":
                        from[attr] = R.getRGB(from[attr]);
                        var toColour = R.getRGB(to[attr]);
                        diff[attr] = {
                            r: (toColour.r - from[attr].r) / ms,
                            g: (toColour.g - from[attr].g) / ms,
                            b: (toColour.b - from[attr].b) / ms
                        };
                        break;
                    case "path":
                        var pathes = path2curve(from[attr], to[attr]);
                        from[attr] = pathes[0];
                        var toPath = pathes[1];
                        diff[attr] = [];
                        for (var i = 0, ii = from[attr][length]; i < ii; i++) {
                            diff[attr][i] = [0];
                            for (var j = 1, jj = from[attr][i][length]; j < jj; j++) {
                                diff[attr][i][j] = (toPath[i][j] - from[attr][i][j]) / ms;
                            }
                        }
                        break;
                    case "csv":
                        var values = Str(params[attr])[split](separator),
                            from2 = Str(from[attr])[split](separator);
                        switch (attr) {
                            case "translation":
                                from[attr] = [0, 0];
                                diff[attr] = [values[0] / ms, values[1] / ms];
                                break;
                            case "rotation":
                                from[attr] = (from2[1] == values[1] && from2[2] == values[2]) ? from2 : [0, values[1], values[2]];
                                diff[attr] = [(values[0] - from[attr][0]) / ms, 0, 0];
                                break;
                            case "scale":
                                params[attr] = values;
                                from[attr] = Str(from[attr])[split](separator);
                                diff[attr] = [(values[0] - from[attr][0]) / ms, (values[1] - from[attr][1]) / ms, 0, 0];
                                break;
                            case "clip-rect":
                                from[attr] = Str(from[attr])[split](separator);
                                diff[attr] = [];
                                i = 4;
                                while (i--) {
                                    diff[attr][i] = (values[i] - from[attr][i]) / ms;
                                }
                                break;
                        }
                        to[attr] = values;
                        break;
                    default:
                        values = [].concat(params[attr]);
                        from2 = [].concat(from[attr]);
                        diff[attr] = [];
                        i = element.paper.customAttributes[attr][length];
                        while (i--) {
                            diff[attr][i] = ((values[i] || 0) - (from2[i] || 0)) / ms;
                        }
                        break;
                }
            }
        }
        if (!animateable) {
            var attrs = [],
                lastcall;
            for (var key in params) if (params[has](key) && animKeyFrames.test(key)) {
                attr = {value: params[key]};
                key == "from" && (key = 0);
                key == "to" && (key = 100);
                attr.key = toInt(key, 10);
                attrs.push(attr);
            }
            attrs.sort(sortByKey);
            if (attrs[0].key) {
                attrs.unshift({key: 0, value: element.attrs});
            }
            for (i = 0, ii = attrs[length]; i < ii; i++) {
                keyframesRun(attrs[i].value, element, ms / 100 * attrs[i].key, ms / 100 * (attrs[i - 1] && attrs[i - 1].key || 0), attrs[i - 1] && attrs[i - 1].value.callback);
            }
            lastcall = attrs[attrs[length] - 1].value.callback;
            if (lastcall) {
                element.timeouts.push(setTimeout(function () {lastcall.call(element);}, ms));
            }
        } else {
            var easyeasy = R.easing_formulas[easing];
            if (!easyeasy) {
                easyeasy = Str(easing).match(bezierrg);
                if (easyeasy && easyeasy[length] == 5) {
                    var curve = easyeasy;
                    easyeasy = function (t) {
                        return CubicBezierAtTime(t, +curve[1], +curve[2], +curve[3], +curve[4], ms);
                    };
                } else {
                    easyeasy = function (t) {
                        return t;
                    };
                }
            }
            animationElements.push({
                start: params.start || +new Date,
                ms: ms,
                easing: easyeasy,
                from: from,
                diff: diff,
                to: to,
                el: element,
                t: {x: 0, y: 0}
            });
            R.is(callback, "function") && (element._ac = setTimeout(function () {
                callback.call(element);
            }, ms));
            animationElements[length] == 1 && setTimeout(animation);
        }
        return this;
    };
    elproto.stop = function () {
        for (var i = 0; i < animationElements.length; i++) {
            animationElements[i].el.id == this.id && animationElements.splice(i--, 1);
        }
        for (i = 0, ii = this.timeouts && this.timeouts.length; i < ii; i++) {
            clearTimeout(this.timeouts[i]);
        }
        this.timeouts = [];
        clearTimeout(this._ac);
        delete this._ac;
        return this;
    };
    elproto.translate = function (x, y) {
        return this.attr({translation: x + " " + y});
    };
    elproto[toString] = function () {
        return "Rapha\xebl\u2019s object";
    };
    R.ae = animationElements;

    // Set
    var Set = function (items) {
        this.items = [];
        this[length] = 0;
        this.type = "set";
        if (items) {
            for (var i = 0, ii = items[length]; i < ii; i++) {
                if (items[i] && (items[i].constructor == Element || items[i].constructor == Set)) {
                    this[this.items[length]] = this.items[this.items[length]] = items[i];
                    this[length]++;
                }
            }
        }
    };
    Set[proto][push] = function () {
        var item,
            len;
        for (var i = 0, ii = arguments[length]; i < ii; i++) {
            item = arguments[i];
            if (item && (item.constructor == Element || item.constructor == Set)) {
                len = this.items[length];
                this[len] = this.items[len] = item;
                this[length]++;
            }
        }
        return this;
    };
    Set[proto].pop = function () {
        delete this[this[length]--];
        return this.items.pop();
    };
    for (var method in elproto) if (elproto[has](method)) {
        Set[proto][method] = (function (methodname) {
            return function () {
                for (var i = 0, ii = this.items[length]; i < ii; i++) {
                    this.items[i][methodname][apply](this.items[i], arguments);
                }
                return this;
            };
        })(method);
    }
    Set[proto].attr = function (name, value) {
        if (name && R.is(name, array) && R.is(name[0], "object")) {
            for (var j = 0, jj = name[length]; j < jj; j++) {
                this.items[j].attr(name[j]);
            }
        } else {
            for (var i = 0, ii = this.items[length]; i < ii; i++) {
                this.items[i].attr(name, value);
            }
        }
        return this;
    };
    Set[proto].animate = function (params, ms, easing, callback) {
        (R.is(easing, "function") || !easing) && (callback = easing || null);
        var len = this.items[length],
            i = len,
            item,
            set = this,
            collector;
        callback && (collector = function () {
            !--len && callback.call(set);
        });
        easing = R.is(easing, string) ? easing : collector;
        item = this.items[--i].animate(params, ms, easing, collector);
        while (i--) {
            this.items[i] && !this.items[i].removed && this.items[i].animateWith(item, params, ms, easing, collector);
        }
        return this;
    };
    Set[proto].insertAfter = function (el) {
        var i = this.items[length];
        while (i--) {
            this.items[i].insertAfter(el);
        }
        return this;
    };
    Set[proto].getBBox = function () {
        var x = [],
            y = [],
            w = [],
            h = [];
        for (var i = this.items[length]; i--;) {
            var box = this.items[i].getBBox();
            x[push](box.x);
            y[push](box.y);
            w[push](box.x + box.width);
            h[push](box.y + box.height);
        }
        x = mmin[apply](0, x);
        y = mmin[apply](0, y);
        return {
            x: x,
            y: y,
            width: mmax[apply](0, w) - x,
            height: mmax[apply](0, h) - y
        };
    };
    Set[proto].clone = function (s) {
        s = new Set;
        for (var i = 0, ii = this.items[length]; i < ii; i++) {
            s[push](this.items[i].clone());
        }
        return s;
    };

    R.registerFont = function (font) {
        if (!font.face) {
            return font;
        }
        this.fonts = this.fonts || {};
        var fontcopy = {
                w: font.w,
                face: {},
                glyphs: {}
            },
            family = font.face["font-family"];
        for (var prop in font.face) if (font.face[has](prop)) {
            fontcopy.face[prop] = font.face[prop];
        }
        if (this.fonts[family]) {
            this.fonts[family][push](fontcopy);
        } else {
            this.fonts[family] = [fontcopy];
        }
        if (!font.svg) {
            fontcopy.face["units-per-em"] = toInt(font.face["units-per-em"], 10);
            for (var glyph in font.glyphs) if (font.glyphs[has](glyph)) {
                var path = font.glyphs[glyph];
                fontcopy.glyphs[glyph] = {
                    w: path.w,
                    k: {},
                    d: path.d && "M" + path.d[rp](/[mlcxtrv]/g, function (command) {
                        return {l: "L", c: "C", x: "z", t: "m", r: "l", v: "c"}[command] || "M";
                    }) + "z"
                };
                if (path.k) {
                    for (var k in path.k) if (path[has](k)) {
                        fontcopy.glyphs[glyph].k[k] = path.k[k];
                    }
                }
            }
        }
        return font;
    };
    paperproto.getFont = function (family, weight, style, stretch) {
        stretch = stretch || "normal";
        style = style || "normal";
        weight = +weight || {normal: 400, bold: 700, lighter: 300, bolder: 800}[weight] || 400;
        if (!R.fonts) {
            return;
        }
        var font = R.fonts[family];
        if (!font) {
            var name = new RegExp("(^|\\s)" + family[rp](/[^\w\d\s+!~.:_-]/g, E) + "(\\s|$)", "i");
            for (var fontName in R.fonts) if (R.fonts[has](fontName)) {
                if (name.test(fontName)) {
                    font = R.fonts[fontName];
                    break;
                }
            }
        }
        var thefont;
        if (font) {
            for (var i = 0, ii = font[length]; i < ii; i++) {
                thefont = font[i];
                if (thefont.face["font-weight"] == weight && (thefont.face["font-style"] == style || !thefont.face["font-style"]) && thefont.face["font-stretch"] == stretch) {
                    break;
                }
            }
        }
        return thefont;
    };
    paperproto.print = function (x, y, string, font, size, origin, letter_spacing) {
        origin = origin || "middle"; // baseline|middle
        letter_spacing = mmax(mmin(letter_spacing || 0, 1), -1);
        var out = this.set(),
            letters = Str(string)[split](E),
            shift = 0,
            path = E,
            scale;
        R.is(font, string) && (font = this.getFont(font));
        if (font) {
            scale = (size || 16) / font.face["units-per-em"];
            var bb = font.face.bbox.split(separator),
                top = +bb[0],
                height = +bb[1] + (origin == "baseline" ? bb[3] - bb[1] + (+font.face.descent) : (bb[3] - bb[1]) / 2);
            for (var i = 0, ii = letters[length]; i < ii; i++) {
                var prev = i && font.glyphs[letters[i - 1]] || {},
                    curr = font.glyphs[letters[i]];
                shift += i ? (prev.w || font.w) + (prev.k && prev.k[letters[i]] || 0) + (font.w * letter_spacing) : 0;
                curr && curr.d && out[push](this.path(curr.d).attr({fill: "#000", stroke: "none", translation: [shift, 0]}));
            }
            out.scale(scale, scale, top, height).translate(x - top, y - height);
        }
        return out;
    };

    R.format = function (token, params) {
        var args = R.is(params, array) ? [0][concat](params) : arguments;
        token && R.is(token, string) && args[length] - 1 && (token = token[rp](formatrg, function (str, i) {
            return args[++i] == null ? E : args[i];
        }));
        return token || E;
    };
    R.ninja = function () {
        oldRaphael.was ? (win.Raphael = oldRaphael.is) : delete Raphael;
        return R;
    };
    R.el = elproto;
    R.st = Set[proto];

    oldRaphael.was ? (win.Raphael = R) : (Raphael = R);
})();

/********* Source File: src/elycharts_defaults.js*********/
/*!*********************************************************************
 * ELYCHARTS v2.1.4-SNAPSHOT $Id: elycharts.js 52 2011-08-07 19:57:09Z stefano.bagnara@gmail.com $
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

;(function($) {
    if (!$.elycharts)
        $.elycharts = {};

    /***********************************************************************
     * DEFAULT OPTIONS
     **********************************************************************/

    $.elycharts.templates = {

        common : {
            // Tipo di grafico
            // type : 'line|pie|funnel|barline'

            // Permette di specificare una configurazione di default da utilizzare (definita in $.elycharts.templates.NOME)
            // La configurazione completa ï¿½ quindi data da tutti i valori della conf di default alla quale viene unita (con sovrascrittura) la conf corrente
            // Il parametro ï¿½ ricorsivo (la configurazione di default puo' a sua volta avere una configurazione di default)
            // Se non specificato, la configurazione di default ï¿½ quella con lo stesso nome del tipo di grafico
            // template : 'NOME',

            /* DATI:
             // I valori associati a ogni serie del grafico. Ogni serie ï¿½ associata a una chiave dell'oggetto value, il cui 
             // valore ï¿½ l'array di dati relativi
             values : {},

             // Label associate ai valori del grafico
             // Solo in caso di label gestite da labelmanager (quindi per pie e funnel) e per label.html = true e' possibile inserire 
             // degli elementi DOM/JQUERY che verranno presi e posizionati correttament.
             labels : [],

             // Anchor per la gestione mediante anchormanager. Possono essere stringhe e oggetti DOM/JQUERY che verranno riposizionati
             anchors : {},

             tooltips : {},

             legend : [],
             */

            // Per impostare una dimensione diversa da quella del container settare width e height
            //width : x,
            //height : y

            // I margini del grafico rispetto al frame complessivo. Da notare che riguardano la posizione del grafico
            // principale, e NON degli elementi aggiuntivi (legenda, label e titoli degli assi...). Quindi i margini devono
            // essere impostati in genere proprio per lasciare lo spazio per questi elementi
            // Sintassi: [top, right, bottom, left]
            margins: [10, 10, 10, 10],

            // style : {},

            // Per gestire al meglio l'interattivita' del grafico (tooltip, highlight, anchor...) viene inserito un secondo
            // layer per le parti sensibili al mouse. Se si sa che il grafico non avra' alcuna interattivita' si puo' impostare 
            // questo valore a false per evitare di creare il layer (ottimizzando leggermente la pagina)
            interactive : true,

            // Dati da applicare a tutte le serie del grafico
            defaultSeries : {
                // Impostare a false per disabilitare la visualizzazione della serie
                visible : true,

                // Impostare color qui permette di impostare velocemente plotProps.stroke+fill, tooltip.frameProps.stroke, dotProps.stroke e fillProps.fill (se non specificati)
                //color: 'blue',

                //plotProps : { },

                // Impostazioni dei tooltip
                tooltip : {
                    active : true,
                    // Se width ed height vengono impostati a 0 o ad "auto" (equivalenti) non vengono fissate dimensioni, quindi il contenuto si autodimensiona in funzione del tooltip
                    // Impostare a 0|auto ï¿½ incompatibile con il frame SVG, quindi viene automaticamente disabilitato (come se frameProps = false)
                    width: 100, height: 50,
                    roundedCorners: 5,
                    padding: [6, 6] /* y, x */,
                    offset: [20, 0] /* y, x */,
                    // Se frameProps = false non disegna la cornice del tooltip (ad es. per permettere di definire la propria cornice HTML)
                    frameProps : { fill: "white", "stroke-width": 2 },
                    contentStyle : { "font-family": "Arial", "font-size": "12px", "line-height": "16px", color: "black" }
                },

                // Highlight feature
                highlight : {
                    // Cambia le dimensioni dell'elemento quando deve essere evidenziato
                    //scale : [x, y],
                    // Opzioni di animazione effetto "scale"
                    scaleSpeed : 100, scaleEasing : '',
                    // Cambia gli attributi dell'elemento quando evidenziato
                    //newProps : { opacity : 1 },
                    // Inserisce un layer con gli attributi specificati sopra quello da evidenziare
                    //overlayProps : {"fill" : "white", "fill-opacity" : .3, "stroke-width" : 0}
                    // Muove l'area evidenziata. E' possibile specificare un valore X o un array [X, Y]
                    //move : 10,
                    // Opzioni di animazione effetto "move"
                    moveSpeed : 100, moveEasing : '',
                    // Opzioni di animazione da usare per riportare l'oggetto alle situazione iniziale
                    restoreSpeed : 0, restoreEasing : ''
                },

                anchor : {
                    // Aggiunge alle anchor esterne la classe selezionata quando il mouse passa sull'area
                    //addClass : "",
                    // Evidenzia la serie al passaggio del mouse
                    //highlight : "",
                    // Se impostato a true usa gli eventi mouseenter/mouseleave invece di mouseover/mouseout per l'highlight
                    //useMouseEnter : false,
                },

                // Opzioni per la generazione animata dei grafici
                startAnimation : {
                    //active : true,
                    type : 'simple',
                    speed : 600,
                    delay : 0,
                    propsFrom : {}, // applicate a tutte le props di plot
                    propsTo : {}, // applicate a tutte le props di plot
                    easing : '' // easing raphael: >, <, <>, backIn, backOut, bounce, elastic

                    // Opzionale per alcune animazioni, permette di specificare un sotto-tipo
                    // subType : 0|1|2
                },

                // Opzioni per le transizioni dei grafici durante un cambiamento di configurazione
                /* stepAnimation : {
                 speed : 600,
                 delay : 0,
                 easing : '' // easing raphael: >, <, <>, backIn, backOut, bounce, elastic
                 },*/

                label : {
                    // Disegna o meno la label interna al grafico
                    active : false,
                    // Imposta un offset [X,Y] per la label (le coordinate sono relative al sistema di assi dello specifico settore disegnato. 
                    // Ad es. per il piechart la X ï¿½ la distanza dal centro, la Y lo spostamento ortogonale
                    //offset : [x, y],
                    html : false,
                    // Proprieta' della label (per HTML = false)
                    props : { fill: 'black', stroke: "none", "font-family": 'Arial', "font-size": "16px" },
                    // Stile CSS della label (per HTML = true)
                    style : { cursor : 'default' }
                    // Posizionamento della label rispetto al punto centrale (+offset) identificato
                    //frameAnchor : ['start|middle|end', 'top|middle|bottom']
                }

                /*legend : {
                 dotType : 'rect',
                 dotWidth : 10, dotHeight : 10, dotR : 4,
                 dotProps : { },
                 textProps : { font: '12px Arial', fill: "#000" }
                 }*/
            },

            series : {
                // Serie specifica usata quando ci sono "dati vuoti" (ad esempio quando un piechart e' a 0)
                empty : {
                    //plotProps : { fill : "#D0D0D0" },
                    label : { active : false },
                    tooltip : { active : false }
                }
                /*root : {
                 values : []
                 }*/
            },

            features : {
                tooltip : {
                    // Imposta una posizione fissa per tutti i tooltip
                    //fixedPos : [ x,  y]
                    // Velocita' del fade
                    fadeDelay : 100,
                    // Velocita' dello spostamento del tip da un'area all'altra
                    moveDelay : 300
                    // E' possibile specificare una funzione che filtra le coordinate del tooltip prima di mostrarlo, permettendo di modificarle
                    // Nota: le coordinate del mouse sono in mouseAreaData.event.pageX/pageY, e nel caso va ritornato [mouseAreaData.event.pageX, mouseAreaData.event.pageY, true] per indicare che il sistema e' relativo alla pagina)
                    //positionHandler : function(env, tooltipConf, mouseAreaData, suggestedX, suggestedY) { return [suggestedX, suggestedY] }
                },
                mousearea : {
                    // 'single' le aree sensibili sono relative a ogni valore di ogni serie, se 'index' il mouse attiva tutte le serie per un indice
                    type : 'single',
                    // In caso di type = 'index', indica se le aree si basano sulle barre ('bar') o sui punti di una linea ('line'). Specificare 'auto' per scegliere automaticamente
                    indexCenter : 'auto',
                    // Quanto tempo puo' passare nel passaggio da un'area all'altra per considerarlo uno spostamento di puntatore
                    areaMoveDelay : 500,
                    // Se diversi chart specificano lo stesso syncTag quando si attiva l'area di uno si disattivano quelle degli altri
                    syncTag: false,
                    // Callback for mouse actions. Parameters passed: (env, serie, index, mouseAreaData)
                    onMouseEnter : false,
                    onMouseExit : false,
                    onMouseChanged : false,
                    onMouseOver : false,
                    onMouseOut : false
                },
                highlight : {
                    // Evidenzia tutto l'indice con una barra ("bar"), una linea ("line") o una linea centrata sulle barre ("barline"). Se "auto" decide in autonomia tra bar e line
                    //indexHighlight : 'barline',
                    indexHighlightProps : { opacity : 1 /*fill : 'yellow', opacity : .3, scale : ".5 1"*/ }
                },
                animation : {
                    // Valore di default per la generazione animata degli elementi del grafico (anche per le non-serie: label, grid...)
                    startAnimation : {
                        //active : true,
                        //propsFrom : {}, // applicate a tutte le props di plot
                        //propsTo : {}, // applicate a tutte le props di plot
                        speed : 600,
                        delay : 0,
                        easing : '' // easing raphael: >, <, <>, backIn, backOut, bounce, elastic
                    },
                    // Valore di default per la transizione animata degli elementi del grafico (anche per le non-serie: label, grid...)
                    stepAnimation : {
                        speed : 600,
                        delay : 0,
                        easing : '' // easing raphael: >, <, <>, backIn, backOut, bounce, elastic
                    }
                },
                frameAnimation : {
                    active : false,
                    cssFrom : { opacity : 0},
                    cssTo : { opacity: 1 },
                    speed : 'slow',
                    easing : 'linear' // easing jQuery: 'linear' o 'swing'
                },
                pixelWorkAround : {
                    active : true
                },
                label : {},
                shadows : {
                    active : false,
                    offset : [2, 2], // Per attivare l'ombra, [y, x]
                    props : {"stroke-width": 0, "stroke-opacity": 0, "fill": "black", "fill-opacity": .3}
                },
                // BALLOONS: Applicabile solo al funnel (per ora)
                balloons : {
                    active : false,
                    // Width: se non specificato e' automatico
                    //width : 200,
                    // Height: se non specificato e' automatico
                    //height : 50,
                    // Lo stile CSS da applicare a ogni balloon
                    style : {  },
                    // Padding 
                    padding : [ 5, 5 ],
                    // La distanza dal bordo sinistro
                    left : 10,
                    // Percorso della linea: [ [ x, y iniziali (rispetto al punto di inizio standard)], ... [x, y intermedi (rispetto al punto di inizio standard)] ..., [x, y finale (rispetto all'angolo del balloon piï¿½ vicino al punto di inizio)] ]
                    line : [ [ 0, 0 ], [0, 0] ],
                    // Proprietï¿½ della linea
                    lineProps : { }
                },
                legend : {
                    horizontal : false,
                    x : 'auto', // X | auto, (auto solo per horizontal = true)
                    y : 10,
                    width : 'auto', // X | auto, (auto solo per horizontal = true)
                    height : 20,
                    itemWidth : "fixed", // fixed | auto, solo per horizontal = true
                    margins : [0, 0, 0, 0],
                    dotMargins : [10, 5], // sx, dx
                    borderProps : { fill : "white", stroke : "black", "stroke-width" : 1 },
                    dotType : 'rect',
                    dotWidth : 10, dotHeight : 10, dotR : 4,
                    dotProps : { type : "rect", width : 10, height : 10 },
                    textProps : { font: '12px Arial', fill: "#000" }
                },
                debug : {
                    active : false
                }
            },

            nop : 0
        },

        line : {
            template : 'common',

            barMargins : 0,

            // Axis
            defaultAxis : {
                // [non per asse x] Normalizza il valore massimo dell'asse in modo che tutte le label abbiamo al massimo N cifre significative
                // (Es: se il max e' 135 e normalize = 2 verra' impostato il max a 140, ma se il numero di label in y e' 3 verrï¿½ impostato 150)
                normalize: 2,
                // Permette di impostare i valori minimi e massimi di asse (invece di autorilevarli)
                min: 0, //max: x,
                // Imposta un testo da usare come prefisso e suffisso delle label
                //prefix : "", suffix : "",
                // Visualizza o meno le label dell'asse
                labels: false,
                // Distanza tra le label e l'asse relativo
                labelsDistance: 8,
                // [solo asse x] Rotazione (in gradi) delle label. Se specificato ignora i valori di labelsAnchor e labelsProps['text-anchor']
                labelsRotate: 0,
                // Proprieta' grafiche delle label
                labelsProps : {font: '10px Arial', fill: "#000"},
                // Compatta il numero mostrato nella label usando i suffissi specificati per migliaia, milioni...
                //labelsCompactUnits : ['k', 'M'],
                // Permette di specificare una funzione esterna che si occupa di formattare (o in generale trasformare) la label
                //labelsFormatHandler : function (label) { return label },
                // Salta le prime N label
                //labelsSkip : 0, 
                // Force alignment for the label. Auto will automatically center it for x axis (also considering labelsRotate), "end" for l axis, "start" for the right axis.
                //labelsAnchor : "auto"
                // [solo asse x] Force an alternative position for the X axis labels. Auto will automatically choose the right position depending on "labelsCenter", the type of charts (bars vs lines), and labelsRotate.
                //labelsPos : "auto",
                // Automatically hide labels that would overlap previous labels.
                //labelsHideCovered : true, 
                // Inserisce un margine alla label (a sinistra se in asse x, in alto se in altri assi)
                //labelsMargin: 10,  
                // [solo asse x] If labelsHideCovered = true, make sure each label have at least this space before the next one. 
                //labelsMarginRight: 0, 
                // Distanza del titolo dall'asse
                titleDistance : 25, titleDistanceIE : .75,
                // Proprieta' grafiche del titolo
                titleProps : {font: '12px Arial', fill: "#000", "font-weight": "bold"}
            },
            axis : {
                x : { titleDistanceIE : 1.2 }
            },

            defaultSeries : {
                // Tipo di serie, puo' essere 'line' o 'bar'
                type : 'line',
                // L'asse di riferimento della serie. Gli assi "l" ed "r" sono i 2 assi visibili destro e sinistro. 
                // E' possibile inserire anche un asse arbitrario (che non sarï¿½ visibile)
                axis : 'l',
                // Specificare cumulative = true se i valori inseriti per la serie sono cumulativi
                cumulative : false,
                // In caso di type="line" indica l'arrotondamento della linea
                rounded : 1,
                // Mette il punto di intersezione al centro dell'intervallo invece che al limite (per allineamento con bars). Se 'auto' decide autonomamente
                lineCenter : 'auto',
                // Permette di impilare le serie (i valori di uno iniziano dove finiscono quelli del precedente) con un altra (purche' dello stesso tipo)
                // Specificare "true" per impilare con la serie visibile precedente, oppure il nome della serie sulla quale impilare
                // stacked : false,

                plotProps : {"stroke-width": 1, "stroke-linejoin": "round"},

                barWidthPerc: 100,
                //DELETED: barProps : {"width-perc" : 100, "stroke-width": 1, "fill-opacity" : .3},

                // Attiva o disattiva il riempimento
                fill : false,
                fillProps : {stroke: "none", "stroke-width" : 0, "stroke-opacity": 0, opacity: .3},

                dot : false,
                dotProps : {size: 4, stroke: "#000", zindex: 5},
                dotShowOnNull : false,

                mouseareaShowOnNull : false,

                startAnimation : {
                    plotPropsFrom : false,
                    // DELETED linePropsFrom : false,
                    fillPropsFrom : false,
                    dotPropsFrom : false,
                    //DELETED barPropsFrom : false,
                    shadowPropsFrom : false
                }

            },

            features : {
                grid : {
                    // N. di divisioni sull'asse X. Se "auto" si basa sulla label da visualizzare. Se "0" imposta draw[vertical] = false
                    // Da notare che se "auto" allora la prima e l'ultima linea (bordi) le fa vedere sempre (se ci sono le label). Se invece e' un numero si comporta come ny: fa vedere i bordi solo se forzato con forceBorder
                    nx : "auto",
                    // N. di divisione sull'asse Y. Se "0" imposta draw[horizontal] = false
                    ny : 4,
                    // Disegna o meno la griglia. Si puo' specificare un array [horizontal, vertical]
                    draw : false,
                    // Forza la visualizzazione dei bordi/assi. Se true disegna comunque i bordi (anche se draw = false o se non ci sono label), 
                    // altrimenti si basa sulle regole standard di draw e presenza label (per asse x)
                    // Puo' essere un booleano singolo o un array di bordi [up, dx, down, sx]
                    forceBorder : false,
                    // Proprieta' di visualizzazione griglia
                    props : {stroke: '#e0e0e0', "stroke-width": 1},
                    // Dimensioni extra delle rette [up, dx, down, sx]
                    extra : [0, 0, 0, 0],
                    // Indica se le label (e le rispettive linee del grid) vanno centrate sulle barre (true), quindi tra 2 linee, o sui punti della serie (false), quindi su una sola linea
                    // Se specificato "auto" decide in autonomia
                    labelsCenter : "auto",

                    // Display a rectangular region with properties specied for every even/odd vertical/horizontal grid division
                    evenVProps : false,
                    oddVProps : false,
                    evenHProps : false,
                    oddHProps : false,

                    ticks : {
                        // Attiva le barrette sugli assi [x, l, r]
                        active : [false, false, false],
                        // Dimensioni da prima dell'asse a dopo l'asse
                        size : [10, 10],
                        // Proprieta' di visualizzazione griglia
                        props : {stroke: '#e0e0e0', "stroke-width": 1}
                    }
                }
            },

            nop : 0
        },

        pie : {
            template : 'common',

            // Coordinate del centro, se non specificate vengono autodeterminate
            //cx : 0, cy : 0,
            // Raggio della torta, se non specificato viene autodeterminato
            //r : 0
            // Angolo dal quale iniziare a disegnare le fette, in gradi
            startAngle : 0,
            // Disegna la torta con le fette in senso orario (invece dell'orientamento standard per gradi, in senso antiorario)
            clockwise : false,
            // Soglia (rapporto sul totale) entro la quale una fetta non viene visualizzata
            valueThresold : 0.006,

            defaultSeries : {
                // r: .5, raggio usato solo per questo spicchio, se <=1 e' in rapporto al raggio generale
                // inside: X, inserisce questo spicchio dentro un altro (funziona solo inside: precedente, e non gestisce + spicchi dentro l'altro)
            }
        },

        funnel : {
            template : 'common',

            rh: 0, // height of ellipsis (for top and bottom cuts)
            method: 'width', // width/cutarea
            topSector: 0, // height factor of top cylinder
            topSectorProps : { fill: "#d0d0d0" },
            bottomSector: .1, // height factor of bottom cylinder
            bottomSectorProps : { fill: "#d0d0d0" },
            edgeProps : { fill: "#c0c0c0", "stroke-width": 1, opacity: 1 },

            nop : 0
        },

        barline : {
            template : 'common',

            // Imposta il valore massimo per la scala (altrimenti prende il valore + alto)
            // max : X

            // Impostare direction = rtl per creare un grafico che va da destra a sinistra
            direction : 'ltr'
        }
    }

})(jQuery);
/********* Source File: src/elycharts_core.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {
    if (!$.elycharts)
        $.elycharts = {};

    $.elycharts.lastId = 0;

    /***********************************************************************
     * INITIALIZATION / MAIN CALL
     **********************************************************************/

    $.fn.chart = function($options) {
        if (!this.length)
            return this;

        var $env = this.data('elycharts_env');

        if (typeof $options == "string") {
            if ($options.toLowerCase() == "config")
                return $env ? $env.opt : false;
            if ($options.toLowerCase() == "clear") {
                if ($env) {
                    // TODO Bisogna chiamare il destroy delle feature?
                    $env.paper.clear();
                    this.html("");
                    this.data('elycharts_env', false);
                }
            }
        }
        else if (!$env) {
            // First call, initialization

            if ($options)
                $options = _extendAndNormalizeOptions($options);

            if (!$options || !$options.type || !$.elycharts.templates[$options.type]) {
                alert('ElyCharts ERROR: chart type is not specified');
                return false;
            }
            $env = _initEnv(this, $options);

            _processGenericConfig($env, $options);
            $env.pieces = $.elycharts[$env.opt.type].draw($env);

            this.data('elycharts_env', $env);

        } else {
            $options = _normalizeOptions($options, $env.opt);

            // Already initialized
            $env.oldopt = common._clone($env.opt);
            $env.opt = $.extend(true, $env.opt, $options);
            $env.newopt = $options;

            _processGenericConfig($env, $options);
            $env.pieces = $.elycharts[$env.opt.type].draw($env);
        }

        return this;
    }

    /**
     * Must be called only in first call to .chart, to initialize elycharts environment.
     */
    function _initEnv($container, $options) {
        if (!$options.width)
            $options.width = $container.width();
        if (!$options.height)
            $options.height = $container.height();

        var $env = {
            id : $.elycharts.lastId ++,
            paper : common._RaphaelInstance($container.get()[0], $options.width, $options.height),
            container : $container,
            plots : [],
            opt : $options
        };

        // Rendering a transparent pixel up-left. Thay way SVG area is well-covered (else the position starts at first real object, and that mess-ups everything)
        $env.paper.rect(0,0,1,1).attr({opacity: 0});

        $.elycharts[$options.type].init($env);

        return $env;
    }

    function _processGenericConfig($env, $options) {
        if ($options.style)
            $env.container.css($options.style);
    }

    /**
     * Must be called in first call to .chart, to build the full config structure and normalize it.
     */
    function _extendAndNormalizeOptions($options) {
        var k;
        // Compatibility with old $.elysia_charts.default_options and $.elysia_charts.templates
        if ($.elysia_charts) {
            if ($.elysia_charts.default_options)
                for (k in $.elysia_charts.default_options)
                    $.elycharts.templates[k] = $.elysia_charts.default_options[k];
            if ($.elysia_charts.templates)
                for (k in $.elysia_charts.templates)
                    $.elycharts.templates[k] = $.elysia_charts.templates[k];
        }

        // TODO Optimize extend cicle
        while ($options.template) {
            var d = $options.template;
            delete $options.template;
            $options = $.extend(true, {}, $.elycharts.templates[d], $options);
        }
        if (!$options.template && $options.type) {
            $options.template = $options.type;
            while ($options.template) {
                d = $options.template;
                delete $options.template;
                $options = $.extend(true, {}, $.elycharts.templates[d], $options);
            }
        }

        return _normalizeOptions($options, $options);
    }

    /**
     * Normalize options passed (primarly for backward compatibility)
     */
    function _normalizeOptions($options, $fullopt) {
        if ($options.type == 'pie' || $options.type == 'funnel') {
            if ($options.values && $.isArray($options.values) && !$.isArray($options.values[0]))
                $options.values = { root : $options.values };
            if ($options.tooltips && $.isArray($options.tooltips) && !$.isArray($options.tooltips[0]))
                $options.tooltips = { root : $options.tooltips };
            if ($options.anchors && $.isArray($options.anchors) && !$.isArray($options.anchors[0]))
                $options.anchors = { root : $options.anchors };
            if ($options.balloons && $.isArray($options.balloons) && !$.isArray($options.balloons[0]))
                $options.balloons = { root : $options.balloons };
            if ($options.legend && $.isArray($options.legend) && !$.isArray($options.legend[0]))
                $options.legend = { root : $options.legend };
        }

        if ($options.defaultSeries) {
            var deftype = $fullopt.type != 'line' ? $fullopt.type : ($options.defaultSeries.type ? $options.defaultSeries.type : ($fullopt.defaultSeries.type ? $fullopt.defaultSeries.type : 'line'));
            _normalizeOptionsColor($options.defaultSeries, deftype, $fullopt);
            if ($options.defaultSeries.stackedWith) {
                $options.defaultSeries.stacked = $options.defaultSeries.stackedWith;
                delete $options.defaultSeries.stackedWith;
            }
        }

        if ($options.series)
            for (var serie in $options.series) {
                var type = $fullopt.type != 'line' ? $fullopt.type : ($options.series[serie].type ? $options.series[serie].type : ($fullopt.series[serie].type ? $fullopt.series[serie].type : (deftype ? deftype : 'line')));
                _normalizeOptionsColor($options.series[serie], type, $fullopt);
                if ($options.series[serie].values)
                    for (var value in $options.series[serie].values)
                        _normalizeOptionsColor($options.series[serie].values[value], type, $fullopt);

                if ($options.series[serie].stackedWith) {
                    $options.series[serie].stacked = $options.series[serie].stackedWith;
                    delete $options.series[serie].stackedWith;
                }
            }

        if ($options.type == 'line') {
            if (!$options.features)
                $options.features = {};
            if (!$options.features.grid)
                $options.features.grid = {};

            if (typeof $options.gridNX != 'undefined') {
                $options.features.grid.nx = $options.gridNX;
                delete $options.gridNX;
            }
            if (typeof $options.gridNY != 'undefined') {
                $options.features.grid.ny = $options.gridNY;
                delete $options.gridNY;
            }
            if (typeof $options.gridProps != 'undefined') {
                $options.features.grid.props = $options.gridProps;
                delete $options.gridProps;
            }
            if (typeof $options.gridExtra != 'undefined') {
                $options.features.grid.extra = $options.gridExtra;
                delete $options.gridExtra;
            }
            if (typeof $options.gridForceBorder != 'undefined') {
                $options.features.grid.forceBorder = $options.gridForceBorder;
                delete $options.gridForceBorder;
            }

            if ($options.defaultAxis && $options.defaultAxis.normalize && ($options.defaultAxis.normalize == 'auto' || $options.defaultAxis.normalize == 'autony'))
                $options.defaultAxis.normalize = 2;

            if ($options.axis)
                for (var axis in $options.axis)
                    if ($options.axis[axis] && $options.axis[axis].normalize && ($options.axis[axis].normalize == 'auto' || $options.axis[axis].normalize == 'autony'))
                        $options.axis[axis].normalize = 2;
        }

        return $options;
    }

    /**
     * Manage "color" attribute.
     * @param $section Section part of external conf passed
     * @param $type Type of plot (for line chart can be "line" or "bar", for other types is equal to chart type)
     */
    function _normalizeOptionsColor($section, $type, $fullopt) {
        if ($section.color) {
            var color = $section.color;

            if (!$section.plotProps)
                $section.plotProps = {};

            if ($type == 'line') {
                if ($section.plotProps && !$section.plotProps.stroke && !$fullopt.defaultSeries.plotProps.stroke)
                    $section.plotProps.stroke = color;
            } else {
                if ($section.plotProps && !$section.plotProps.fill && !$fullopt.defaultSeries.plotProps.fill)
                    $section.plotProps.fill = color;
            }

            if (!$section.tooltip)
                $section.tooltip = {};
            // Is disabled in defaultSetting i should not set color
            if (!$section.tooltip.frameProps && $fullopt.defaultSeries.tooltip.frameProps)
                $section.tooltip.frameProps = {};
            if ($section.tooltip && $section.tooltip.frameProps && !$section.tooltip.frameProps.stroke && !$fullopt.defaultSeries.tooltip.frameProps.stroke)
                $section.tooltip.frameProps.stroke = color;

            if (!$section.legend)
                $section.legend = {};
            if (!$section.legend.dotProps)
                $section.legend.dotProps = {};
            if ($section.legend.dotProps && !$section.legend.dotProps.fill)
                $section.legend.dotProps.fill = color;

            if ($type == 'line') {
                if (!$section.dotProps)
                    $section.dotProps = {};
                if ($section.dotProps && !$section.dotProps.fill && !$fullopt.defaultSeries.dotProps.fill)
                    $section.dotProps.fill = color;

                if (!$section.fillProps)
                    $section.fillProps = {};
                if ($section.fillProps && !$section.fillProps.fill && !$fullopt.defaultSeries.fillProps.fill)
                    $section.fillProps.fill = color;
            }
        }
    }

    /***********************************************************************
     * COMMON
     **********************************************************************/

    $.elycharts.common = {
        _RaphaelInstance : function(c, w, h) {
            var r = Raphael(c, w, h);

            r.customAttributes.slice = function (cx, cy, r, rint, aa1, aa2) {
                // Method body is for clockwise angles, but parameters passed are ccw
                a1 = 360 - aa2; a2 = 360 - aa1;
                //a1 = aa1; a2 = aa2;
                var flag = (a2 - a1) > 180;
                a1 = (a1 % 360) * Math.PI / 180;
                a2 = (a2 % 360) * Math.PI / 180;
                // a1 == a2  (but they where different before) means that there is a complete round (eg: 0-360). This should be shown
                if (a1 == a2 && aa1 != aa2)
                    a2 += 359.99 * Math.PI / 180;

                return { path : rint ? [
                    ["M", cx + r * Math.cos(a1), cy + r * Math.sin(a1)],
                    ["A", r, r, 0, +flag, 1, cx + r * Math.cos(a2), cy + r * Math.sin(a2)],
                    ["L", cx + rint * Math.cos(a2), cy + rint * Math.sin(a2)],
                    //["L", cx + rint * Math.cos(a1), cy + rint * Math.sin(a1)], 
                    ["A", rint, rint, 0, +flag, 0, cx + rint * Math.cos(a1), cy + rint * Math.sin(a1)],
                    ["z"]
                ] : [
                    ["M", cx, cy],
                    ["l", r * Math.cos(a1), r * Math.sin(a1)],
                    ["A", r, r, 0, +flag, 1, cx + r * Math.cos(a2), cy + r * Math.sin(a2)],
                    ["z"]
                ] };
            };

            return r;
        },

        _clone : function(obj){
            if(obj == null || typeof(obj) != 'object')
                return obj;
            if (obj.constructor == Array)
                return [].concat(obj);
            var temp = new obj.constructor(); // changed (twice)
            for(var key in obj)
                temp[key] = this._clone(obj[key]);
            return temp;
        },

        _mergeObjects : function(o1, o2) {
            return $.extend(true, o1, o2);
            /*
             if (typeof o1 == 'undefined')
             return o2;
             if (typeof o2 == 'undefined')
             return o1;

             for (var idx in o2)
             if (typeof o1[idx] == 'undefined')
             o1[idx] = this._clone(o2[idx]);
             else if (typeof o2[idx] == 'object') {
             if (typeof o1[idx] == 'object')
             o1[idx] = this._mergeObjects(o1[idx], o2[idx]);
             else
             o1[idx] = this._mergeObjects({}, o2[idx]);
             }
             else 
             o1[idx] = this._clone(o2[idx]);
             return o1;*/
        },

        compactUnits : function(val, units) {
            for (var i = units.length - 1; i >= 0; i--) {
                var v = val / Math.pow(1000, i + 1);
                //console.warn(i, units[i], v, v * 10 % 10);
                if (v >= 1 && v * 10 % 10 == 0)
                    return v + units[i];
            }
            return val;
        },

        getElementOriginalAttrs : function(element) {
            var attr = $(element.node).data('original-attr');
            if (!attr) {
                attr = element.attr();
                $(element.node).data('original-attr', attr);
            }
            return attr;
        },

        findInPieces : function(pieces, section, serie, index, subsection) {
            for (var i = 0; i < pieces.length; i++) {
                if (
                    (typeof section == undefined || section == -1 || section == false || pieces[i].section == section) &&
                        (typeof serie == undefined || serie == -1 || serie == false || pieces[i].serie == serie) &&
                        (typeof index == undefined || index == -1 || index == false || pieces[i].index == index) &&
                        (typeof subsection == undefined || subsection == -1 || subsection == false || pieces[i].subSection == subsection)
                    )
                    return pieces[i];
            }
            return false;
        },

        samePiecePath : function(piece1, piece2) {
            return (((typeof piece1.section == undefined || piece1.section == -1 || piece1.section == false) && (typeof piece2.section == undefined || piece2.section == -1 || piece2.section == false)) || piece1.section == piece2.section) &&
                (((typeof piece1.serie == undefined || piece1.serie == -1 || piece1.serie == false) && (typeof piece2.serie == undefined || piece2.serie == -1 || piece2.serie == false)) || piece1.serie == piece2.serie) &&
                (((typeof piece1.index == undefined || piece1.index == -1 || piece1.index == false) && (typeof piece2.index == undefined || piece2.index == -1 || piece2.index == false)) || piece1.index == piece2.index) &&
                (((typeof piece1.subSection == undefined || piece1.subSection == -1 || piece1.subSection == false) && (typeof piece2.subSection == undefined || piece2.subSection == -1 || piece2.subSection == false)) || piece1.subSection == piece2.subSection);
        },

        executeIfChanged : function(env, changes) {
            if (!env.newopt)
                return true;

            for (var i = 0; i < changes.length; i++) {
                if (changes[i][changes[i].length - 1] == "*") {
                    for (var j in env.newopt)
                        if (j.substring(0, changes[i].length - 1) + "*" == changes[i])
                            return true;
                }
                else if (changes[i] == 'series' && (env.newopt.series || env.newopt.defaultSeries))
                    return true;
                else if (changes[i] == 'axis' && (env.newopt.axis || env.newopt.defaultAxis))
                    return true;
                else if (changes[i].substring(0, 9) == "features.") {
                    changes[i] = changes[i].substring(9);
                    if (env.newopt.features && env.newopt.features[changes[i]])
                        return true;
                }
                else if (typeof env.newopt[changes[i]] != 'undefined')
                    return true;
            }
            return false;
        },

        /**
         * Ottiene le proprietÃ  di una "Area" definita nella configurazione (options),
         * identificata da section / serie / index / subsection, e facendo il merge
         * di tutti i defaults innestati.
         */
        areaProps : function(env, section, serie, index, subsection) {
            var props;

            // TODO fare una cache e fix del toLowerCase (devono solo fare la prima lettera
            if (!subsection) {
                if (typeof serie == 'undefined' || !serie)
                    props = env.opt[section.toLowerCase()];

                else {
                    props = this._clone(env.opt['default' + section]);
                    if (env.opt[section .toLowerCase()] && env.opt[section.toLowerCase()][serie])
                        props = this._mergeObjects(props, env.opt[section.toLowerCase()][serie]);

                    if ((typeof index != 'undefined') && index >= 0 && props['values'] && props['values'][index])
                        props = this._mergeObjects(props, props['values'][index]);
                }

            } else {
                props = this._clone(env.opt[subsection.toLowerCase()]);

                if (typeof serie == 'undefined' || !serie) {
                    if (env.opt[section.toLowerCase()] && env.opt[section.toLowerCase()][subsection.toLowerCase()])
                        props = this._mergeObjects(props, env.opt[section.toLowerCase()][subsection.toLowerCase()]);

                } else {
                    if (env.opt['default' + section] && env.opt['default' + section][subsection.toLowerCase()])
                        props = this._mergeObjects(props, env.opt['default' + section][subsection.toLowerCase()]);

                    if (env.opt[section .toLowerCase()] && env.opt[section.toLowerCase()][serie] && env.opt[section.toLowerCase()][serie][subsection.toLowerCase()])
                        props = this._mergeObjects(props, env.opt[section.toLowerCase()][serie][subsection.toLowerCase()]);

                    if (props && (typeof index != 'undefined') && index > 0 && props['values'] && props['values'][index])
                        props = this._mergeObjects(props, props['values'][index]);
                }
            }

            return props;
        },

        absrectpath : function(x1, y1, x2, y2, r) {
            // TODO Supportare r
            return [['M', x1, y1], ['L', x1, y2], ['L', x2, y2], ['L', x2, y1], ['z']];
        },

        linepathAnchors : function(p1x, p1y, p2x, p2y, p3x, p3y, rounded) {
            var method = 1;
            if (rounded && rounded.length) {
                method = rounded[1];
                rounded = rounded[0];
            }
            if (!rounded)
                rounded = 1;
            var l1 = (p2x - p1x) / 2,
                l2 = (p3x - p2x) / 2,
                a = Math.atan((p2x - p1x) / Math.abs(p2y - p1y)),
                b = Math.atan((p3x - p2x) / Math.abs(p2y - p3y));
            a = p1y < p2y ? Math.PI - a : a;
            b = p3y < p2y ? Math.PI - b : b;
            if (method == 2) {
                // If added by Bago to avoid curves beyond min or max
                if ((a - Math.PI / 2) * (b - Math.PI / 2) > 0) {
                    a = 0;
                    b = 0;
                } else {
                    if (Math.abs(a - Math.PI / 2) < Math.abs(b - Math.PI / 2))
                        b = Math.PI - a;
                    else
                        a = Math.PI - b;
                }
            }

            var alpha = Math.PI / 2 - ((a + b) % (Math.PI * 2)) / 2,
                dx1 = l1 * Math.sin(alpha + a) / 2 / rounded,
                dy1 = l1 * Math.cos(alpha + a) / 2 / rounded,
                dx2 = l2 * Math.sin(alpha + b) / 2 / rounded,
                dy2 = l2 * Math.cos(alpha + b) / 2 / rounded;
            return {
                x1: p2x - dx1,
                y1: p2y + dy1,
                x2: p2x + dx2,
                y2: p2y + dy2
            };
        },

        linepathRevert : function(path) {
            var rev = [], anc = false;
            for (var i = path.length - 1; i >= 0; i--) {
                switch (path[i][0]) {
                    case "M" : case "L" :
                    if (!anc)
                        rev.push( [ rev.length ? "L" : "M", path[i][1], path[i][2] ] );
                    else
                        rev.push( [ "C", anc[0], anc[1], anc[2], anc[3], path[i][1], path[i][2] ] );
                    anc = false;

                    break;
                    case "C" :
                        if (!anc)
                            rev.push( [ rev.length ? "L" : "M", path[i][5], path[i][6] ] );
                        else
                            rev.push( [ "C", anc[0], anc[1], anc[2], anc[3], path[i][5], path[i][6] ] );
                        anc = [ path[i][3], path[i][4], path[i][1], path[i][2] ];
                }
            }
            return rev;
        },

        linepath : function ( points, rounded ) {
            var path = [];
            if (rounded) {
                var anc = false;
                for (var j = 0, jj = points.length - 1; j < jj ; j++) {
                    if (j) {
                        var a = this.linepathAnchors(points[j - 1][0], points[j - 1][1], points[j][0], points[j][1], points[j + 1][0], points[j + 1][1], rounded);
                        path.push([ "C", anc[0], anc[1], a.x1, a.y1, points[j][0], points[j][1] ]);
                        anc = [ a.x2, a.y2 ];
                    } else {
                        path.push([ "M", points[j][0], points[j][1] ]);
                        anc = [ points[j][0], points[j][1] ];
                    }
                }
                if (anc)
                    path.push([ "C", anc[0], anc[1], points[jj][0], points[jj][1], points[jj][0], points[jj][1] ]);

            } else
                for (var i = 0; i < points.length; i++) {
                    var x = points[i][0], y = points[i][1];
                    path.push([i == 0 ? "M" : "L", x, y]);
                }

            return path;
        },

        lineareapath : function (points1, points2, rounded) {
            var path = this.linepath(points1, rounded), path2 = this.linepathRevert(this.linepath(points2, rounded));

            for (var i = 0; i < path2.length; i++)
                path.push( !i ? [ "L", path2[0][1], path2[0][2] ] : path2[i] );

            if (path.length)
                path.push(['z']);

            return path;
        },

        /**
         * Prende la coordinata X di un passo di un path
         */
        getX : function(p, pos) {
            switch (p[0]) {
                case 'CIRCLE':
                    return p[1];
                case 'RECT':
                    return p[!pos ? 1 : 3];
                case 'SLICE':
                    return p[1];
                default:
                    return p[p.length - 2];
            }
        },

        /**
         * Prende la coordinata Y di un passo di un path
         */
        getY : function(p, pos) {
            switch (p[0]) {
                case 'CIRCLE':
                    return p[2];
                case 'RECT':
                    return p[!pos ? 2 : 4];
                case 'SLICE':
                    return p[2];
                default:
                    return p[p.length - 1];
            }
        },

        /**
         * Prende il centro di un path
         *
         * @param offset un offset [x,y] da applicare. Da notare che gli assi potrebbero essere dipendenti dalla figura
         *        (ad esempio per lo SLICE x e' l'asse che passa dal centro del cerchio, y l'ortogonale).
         */
        getCenter: function(path, offset) {
            if (!path.path)
                return false;
            if (path.path.length == 0)
                return false;
            if (!offset)
                offset = [0, 0];

            if (path.center)
                return [path.center[0] + offset[0], path.center[1] + offset[1]];

            var p = path.path[0];
            switch (p[0]) {
                case 'CIRCLE':
                    return [p[1] + offset[0], p[2] + offset[1]];
                case 'RECT':
                    return [(p[1] + p[2])/2 + offset[0], (p[3] + p[4])/2 + offset[1]];
                case 'SLICE':
                    var popangle = p[5] + (p[6] - p[5]) / 2;
                    var rad = Math.PI / 180;
                    return [
                        p[1] + (p[4] + ((p[3] - p[4]) / 2) + offset[0]) * Math.cos(-popangle * rad) + offset[1] * Math.cos((-popangle-90) * rad),
                        p[2] + (p[4] + ((p[3] - p[4]) / 2) + offset[0]) * Math.sin(-popangle * rad) + offset[1] * Math.sin((-popangle-90) * rad)
                    ];
            }

            // WARN Complex paths not supported
            alert('ElyCharts: getCenter with complex path not supported');

            return false;
        },

        /**
         * Sposta il path passato di un offset [x,y]
         * Il risultato e' il nuovo path
         *
         * @param offset un offset [x,y] da applicare. Da notare che gli assi potrebbero essere dipendenti dalla figura
         *        (ad esempio per lo SLICE x e' l'asse che passa dal centro del cerchio, y l'ortogonale).
         * @param marginlimit se true non sposta oltre i margini del grafico (applicabile solo su path standard o RECT)
         * @param simple se true lo spostamento e' sempre fatto sul sistema [x, y] complessivo (altrimenti alcuni elementi, come lo SLICE,
         *        si muovono sul proprio sistema di coordinate - la x muove lungo il raggio e la y lungo l'ortogonale)
         */
        movePath : function(env, path, offset, marginlimit, simple) {
            var p = [], i;
            if (path.length == 1 && path[0][0] == 'RECT')
                return [ [path[0][0], this._movePathX(env, path[0][1], offset[0], marginlimit), this._movePathY(env, path[0][2], offset[1], marginlimit), this._movePathX(env, path[0][3], offset[0], marginlimit), this._movePathY(env, path[0][4], offset[1], marginlimit)] ];
            if (path.length == 1 && path[0][0] == 'SLICE') {
                if (!simple) {
                    var popangle = path[0][5] + (path[0][6] - path[0][5]) / 2;
                    var rad = Math.PI / 180;
                    var x = path[0][1] + offset[0] * Math.cos(- popangle * rad) + offset[1] * Math.cos((-popangle-90) * rad);
                    var y = path[0][2] + offset[0] * Math.sin(- popangle * rad) + offset[1] * Math.cos((-popangle-90) * rad);
                    return [ [path[0][0], x, y, path[0][3], path[0][4], path[0][5], path[0][6] ] ];
                }
                else
                    return [ [ path[0][0], path[0][1] + offset[0], path[0][2] + offset[1], path[0][3], path[0][4], path[0][5], path[0][6] ] ];
            }
            if (path.length == 1 && path[0][0] == 'CIRCLE')
                return [ [ path[0][0], path[0][1] + offset[0], path[0][2] + offset[1], path[0][3] ] ];
            if (path.length == 1 && path[0][0] == 'TEXT')
                return [ [ path[0][0], path[0][1], path[0][2] + offset[0], path[0][3] + offset[1] ] ];
            if (path.length == 1 && path[0][0] == 'LINE') {
                for (i = 0; i < path[0][1].length; i++)
                    p.push( [ this._movePathX(env, path[0][1][i][0], offset[0], marginlimit), this._movePathY(env, path[0][1][i][1], offset[1], marginlimit) ] );
                return [ [ path[0][0], p, path[0][2] ] ];
            }
            if (path.length == 1 && path[0][0] == 'LINEAREA') {
                for (i = 0; i < path[0][1].length; i++)
                    p.push( [ this._movePathX(env, path[0][1][i][0], offset[0], marginlimit), this._movePathY(env, path[0][1][i][1], offset[1], marginlimit) ] );
                var pp = [];
                for (i = 0; i < path[0][2].length; i++)
                    pp.push( [ this._movePathX(env, path[0][2][i][0], offset[0], marginlimit), this._movePathY(env, path[0][2][i][1], offset[1], marginlimit) ] );
                return [ [ path[0][0], p, pp, path[0][3] ] ];
            }

            var newpath = [];
            // http://www.w3.org/TR/SVG/paths.html#PathData
            for (var j = 0; j < path.length; j++) {
                var o = path[j];
                switch (o[0]) {
                    case 'M': case 'm': case 'L': case 'l': case 'T': case 't':
                    // (x y)+
                    newpath.push([o[0], this._movePathX(env, o[1], offset[0], marginlimit), this._movePathY(env, o[2], offset[1], marginlimit)]);
                    break;
                    case 'A': case 'a':
                    // (rx ry x-axis-rotation large-arc-flag sweep-flag x y)+
                    newpath.push([o[0], o[1], o[2], o[3], o[4], o[5], this._movePathX(env, o[6], offset[0], marginlimit), this._movePathY(env, o[7], offset[1], marginlimit)]);
                    break;
                    case 'C': case 'c':
                    // (x1 y1 x2 y2 x y)+
                    newpath.push([o[0], o[1], o[2], o[3], o[4], this._movePathX(env, o[5], offset[0], marginlimit), this._movePathY(env, o[6], offset[1], marginlimit)]);
                    break;
                    case 'S': case 's': case 'Q': case 'q':
                    // (x1 y1 x y)+
                    newpath.push([o[0], o[1], o[2], this._movePathX(env, o[3], offset[0], marginlimit), this._movePathY(env, o[4], offset[1], marginlimit)]);
                    break;
                    case 'z': case 'Z':
                    newpath.push([o[0]]);
                    break;
                }
            }

            return newpath;
        },

        _movePathX : function(env, x, dx, marginlimit) {
            if (!marginlimit)
                return x + dx;
            x = x + dx;
            return dx > 0 && x > env.opt.width - env.opt.margins[1] ? env.opt.width - env.opt.margins[1] : (dx < 0 && x < env.opt.margins[3] ? env.opt.margins[3] : x);
        },

        _movePathY : function(env, y, dy, marginlimit) {
            if (!marginlimit)
                return y + dy;
            y = y + dy;
            return dy > 0 && y > env.opt.height - env.opt.margins[2] ? env.opt.height - env.opt.margins[2] : (dy < 0 && y < env.opt.margins[0] ? env.opt.margins[0] : y);
        },

        /**
         * Ritorna le proprieta SVG da impostare per visualizzare il path non SVG passato (se applicabile, per CIRCLE e TEXT non lo e')
         */
        getSVGProps : function(path, prevprops) {
            var props = prevprops ? prevprops : {};
            var type = 'path', value;

            if (path.length == 1 && path[0][0] == 'RECT')
                value = common.absrectpath(path[0][1], path[0][2], path[0][3], path[0][4], path[0][5]);
            else if (path.length == 1 && path[0][0] == 'SLICE') {
                type = 'slice';
                value = [ path[0][1], path[0][2], path[0][3], path[0][4], path[0][5], path[0][6] ];
            } else if (path.length == 1 && path[0][0] == 'LINE')
                value = common.linepath( path[0][1], path[0][2] );
            else if (path.length == 1 && path[0][0] == 'LINEAREA')
                value = common.lineareapath( path[0][1], path[0][2], path[0][3] );
            else if (path.length == 1 && (path[0][0] == 'CIRCLE' || path[0][0] == 'TEXT' || path[0][0] == 'DOMELEMENT' || path[0][0] == 'RELEMENT'))
                return prevprops ? prevprops : false;
            else
                value = path;

            if (type != 'path' || (value && value.length > 0))
                props[type] = value;
            else if (!prevprops)
                return false;
            return props;
        },

        /**
         * Disegna il path passato
         * Gestisce la feature pixelWorkAround
         */
        showPath : function(env, path, paper) {
            path = this.preparePathShow(env, path);

            if (!paper)
                paper = env.paper;
            if (path.length == 1 && path[0][0] == 'CIRCLE')
                return paper.circle(path[0][1], path[0][2], path[0][3]);
            if (path.length == 1 && path[0][0] == 'TEXT')
                return paper.text(path[0][2], path[0][3], path[0][1]);
            var props = this.getSVGProps(path);

            // Props must be with some data in it
            var hasdata = false;
            for (var k in props) {
                hasdata = true;
                break;
            }

            return props && hasdata ? paper.path().attr(props) : false;
        },

        /**
         * Applica al path le modifiche per poterlo visualizzare
         * Per ora applica solo pixelWorkAround
         */
        preparePathShow : function(env, path) {
            return env.opt.features.pixelWorkAround.active ? this.movePath(env, this._clone(path), [.5, .5], false, true) : path;
        },

        /**
         * Ritorna gli attributi Raphael completi di un piece
         * Per attributi completi si intende l'insieme di attributi specificato,
         * assieme a tutti gli attributi calcolati che determinano lo stato
         * iniziale di un piece (e permettono di farlo ritornare a tale stato).
         * In genere viene aggiunto il path SVG, per il circle vengono aggiunti
         * i dati x,y,r
         */
        getPieceFullAttr : function(env, piece) {
            if (!piece.fullattr) {
                piece.fullattr = this._clone(piece.attr);
                if (piece.path)
                    switch (piece.path[0][0]) {
                        case 'CIRCLE':
                            var ppath = this.preparePathShow(env, piece.path);
                            piece.fullattr.cx = ppath[0][1];
                            piece.fullattr.cy = ppath[0][2];
                            piece.fullattr.r = ppath[0][3];
                            break;
                        case 'TEXT': case 'DOMELEMENT': case 'RELEMENT':
                        break;
                        default:
                            piece.fullattr = this.getSVGProps(this.preparePathShow(env, piece.path), piece.fullattr);
                    }
                if (typeof piece.fullattr.opacity == 'undefined')
                    piece.fullattr.opacity = 1;
            }
            return piece.fullattr;
        },


        show : function(env, pieces) {
            pieces = this.getSortedPathData(pieces);

            common.animationStackStart(env);

            var previousElement = false;
            for (var i = 0; i < pieces.length; i++) {
                var piece = pieces[i];

                if (typeof piece.show == 'undefined' || piece.show) {
                    // If there is piece.animation.element, this is the old element that must be transformed to the new one
                    piece.element = piece.animation && piece.animation.element ? piece.animation.element : false;
                    piece.hide = false;

                    if (!piece.path) {
                        // Element should not be shown or must be hidden: nothing to prepare
                        piece.hide = true;

                    } else if (piece.path.length == 1 && piece.path[0][0] == 'TEXT') {
                        // TEXT
                        // Animation is not supported, so if there's an old element i must hide it (with force = true to hide it for sure, even if there's a new version of same element)
                        if (piece.element) {
                            common.animationStackPush(env, piece, piece.element, false, piece.animation.speed, piece.animation.easing, piece.animation.delay, true);
                            piece.animation.element = false;
                        }
                        piece.element = this.showPath(env, piece.path);
                        // If this is a transition i must position new element
                        if (piece.element && env.newopt && previousElement)
                            piece.element.insertAfter(previousElement);

                    } else if (piece.path.length == 1 && piece.path[0][0] == 'DOMELEMENT') {
                        // DOMELEMENT
                        // Already shown
                        // Animation not supported

                    } else if (piece.path.length == 1 && piece.path[0][0] == 'RELEMENT') {
                        // RAPHAEL ELEMENT
                        // Already shown
                        // Animation is not supported, so if there's an old element i must hide it (with force = true to hide it for sure, even if there's a new version of same element)
                        if (piece.element) {
                            common.animationStackPush(env, piece, piece.element, false, piece.animation.speed, piece.animation.easing, piece.animation.delay, true);
                            piece.animation.element = false;
                        }

                        piece.element = piece.path[0][1];
                        if (piece.element && previousElement)
                            piece.element.insertAfter(previousElement);
                        piece.attr = false;

                    } else {
                        // OTHERS
                        if (!piece.element) {
                            if (piece.animation && piece.animation.startPath && piece.animation.startPath.length)
                                piece.element = this.showPath(env, piece.animation.startPath);
                            else
                                piece.element = this.showPath(env, piece.path);

                            // If this is a transition i must position new element
                            if (piece.element && env.newopt && previousElement)
                                piece.element.insertAfter(previousElement);
                        }
                    }

                    if (piece.element) {
                        if (piece.attr) {
                            if (!piece.animation) {
                                // Standard piece visualization
                                if (typeof piece.attr.opacity == 'undefined')
                                    piece.attr.opacity = 1;
                                piece.element.attr(piece.attr);

                            } else {
                                // Piece animation
                                if (!piece.animation.element)
                                    piece.element.attr(piece.animation.startAttr ? piece.animation.startAttr : piece.attr);
                                //if (typeof animationAttr.opacity == 'undefined')
                                //  animationAttr.opacity = 1;
                                common.animationStackPush(env, piece, piece.element, this.getPieceFullAttr(env, piece), piece.animation.speed, piece.animation.easing, piece.animation.delay);
                            }
                        } else if (piece.hide)
                        // Hide the piece
                            common.animationStackPush(env, piece, piece.element, false, piece.animation.speed, piece.animation.easing, piece.animation.delay);

                        previousElement = piece.element;
                    }
                }
            }

            common.animationStackEnd(env);
        },

        /**
         * Given an array of pieces, return an array of single pathdata contained in pieces, sorted by zindex
         */
        getSortedPathData : function(pieces) {
            res = [];

            for (var i = 0; i < pieces.length; i++) {
                var piece = pieces[i];
                if (piece.paths) {
                    for (var j = 0; j < piece.paths.length; j++) {
                        piece.paths[j].pos = res.length;
                        piece.paths[j].parent = piece;
                        res.push(piece.paths[j]);
                    }
                } else {
                    piece.pos = res.length;
                    piece.parent = false;
                    res.push(piece);
                }
            }
            return res.sort(function (a, b) {
                var za = typeof a.attr == 'undefined' || typeof a.attr.zindex == 'undefined' ? ( !a.parent || typeof a.parent.attr == 'undefined' || typeof a.parent.attr.zindex == 'undefined' ? 0 : a.parent.attr.zindex ) : a.attr.zindex;
                var zb = typeof b.attr == 'undefined' || typeof b.attr.zindex == 'undefined' ? ( !b.parent || typeof b.parent.attr == 'undefined' || typeof b.parent.attr.zindex == 'undefined' ? 0 : b.parent.attr.zindex ) : b.attr.zindex;
                return za < zb ? -1 : (za > zb ? 1 : (a.pos < b.pos ? -1 : (a.pos > b.pos ? 1 : 0)));
            });
        },

        animationStackStart : function(env) {
            if (!env.animationStackDepth || env.animationStackDepth == 0) {
                env.animationStackDepth = 0;
                env.animationStack = {};
            }
            env.animationStackDepth ++;
        },

        animationStackEnd : function(env) {
            env.animationStackDepth --;
            if (env.animationStackDepth == 0) {
                for (var delay in env.animationStack) {
                    this._animationStackAnimate(env.animationStack[delay], delay);
                    delete env.animationStack[delay];
                }
                env.animationStack = {};
            }
        },

        /**
         * Inserisce l'animazione richiesta nello stack di animazioni.
         * Nel caso lo stack non sia inizializzato esegue subito l'animazione.
         */
        animationStackPush : function(env, piece, element, newattr, speed, easing, delay, force) {
            if (typeof delay == 'undefined')
                delay = 0;

            if (!env.animationStackDepth || env.animationStackDepth == 0) {
                this._animationStackAnimate([{piece : piece, object : element, props : newattr, speed: speed, easing : easing, force : force}], delay);

            } else {
                if (!env.animationStack[delay])
                    env.animationStack[delay] = [];

                env.animationStack[delay].push({piece : piece, object : element, props : newattr, speed: speed, easing : easing, force : force});
            }
        },

        _animationStackAnimate : function(stack, delay) {
            var caller = this;
            var func = function() {
                var a = stack.pop();
                caller._animationStackAnimateElement(a);

                while (stack.length > 0) {
                    var b = stack.pop();
                    caller._animationStackAnimateElement(b, a);
                }
            }
            if (delay > 0)
                setTimeout(func, delay);
            else
                func();
        },

        _animationStackAnimateElement : function (a, awith) {
            //console.warn('call', a.piece.animationInProgress, a.force, a.piece.path, a.piece);

            if (a.force || !a.piece.animationInProgress) {

                // Metodo non documentato per bloccare l'animazione corrente
                a.object.stop();
                if (!a.props)
                    a.props = { opacity : 0 }; // TODO Sarebbe da rimuovere l'elemento alla fine

                if (!a.speed || a.speed <= 0) {
                    //console.warn('direct');
                    a.object.attr(a.props);
                    a.piece.animationInProgress = false;
                    return;
                }

                a.piece.animationInProgress = true;
                //console.warn('START', a.piece.animationInProgress, a.piece.path, a.piece);

                // NOTA onEnd non viene chiamato se l'animazione viene bloccata con stop
                var onEnd = function() {
                    //console.warn('END', a.piece.animationInProgress, a.piece); 
                    a.piece.animationInProgress = false
                }

                if (awith)
                    a.object.animateWith(awith, a.props, a.speed, a.easing ? a.easing : 'linear', onEnd);
                else
                    a.object.animate(a.props, a.speed, a.easing ? a.easing : 'linear', onEnd);
            }
            //else console.warn('SKIP', a.piece.animationInProgress, a.piece.path, a.piece);
        }
    }

    var common = $.elycharts.common;

    /***********************************************************************
     * FEATURESMANAGER
     **********************************************************************/

    $.elycharts.featuresmanager = {

        managers : [],
        initialized : false,

        register : function(manager, priority) {
            $.elycharts.featuresmanager.managers.push([priority, manager]);
            $.elycharts.featuresmanager.initialized = false;
        },

        init : function() {
            $.elycharts.featuresmanager.managers.sort(function(a, b) { return a[0] < b[0] ? -1 : (a[0] == b[0] ? 0 : 1) });
            $.elycharts.featuresmanager.initialized = true;
        },

        beforeShow : function(env, pieces) {
            if (!$.elycharts.featuresmanager.initialized)
                this.init();
            for (var i = 0; i < $.elycharts.featuresmanager.managers.length; i++)
                if ($.elycharts.featuresmanager.managers[i][1].beforeShow)
                    $.elycharts.featuresmanager.managers[i][1].beforeShow(env, pieces);
        },

        afterShow : function(env, pieces) {
            if (!$.elycharts.featuresmanager.initialized)
                this.init();
            for (var i = 0; i < $.elycharts.featuresmanager.managers.length; i++)
                if ($.elycharts.featuresmanager.managers[i][1].afterShow)
                    $.elycharts.featuresmanager.managers[i][1].afterShow(env, pieces);
        },

        onMouseOver : function(env, serie, index, mouseAreaData) {
            if (!$.elycharts.featuresmanager.initialized)
                this.init();
            for (var i = 0; i < $.elycharts.featuresmanager.managers.length; i++)
                if ($.elycharts.featuresmanager.managers[i][1].onMouseOver)
                    $.elycharts.featuresmanager.managers[i][1].onMouseOver(env, serie, index, mouseAreaData);
        },

        onMouseOut : function(env, serie, index, mouseAreaData) {
            if (!$.elycharts.featuresmanager.initialized)
                this.init();
            for (var i = 0; i < $.elycharts.featuresmanager.managers.length; i++)
                if ($.elycharts.featuresmanager.managers[i][1].onMouseOut)
                    $.elycharts.featuresmanager.managers[i][1].onMouseOut(env, serie, index, mouseAreaData);
        },

        onMouseEnter : function(env, serie, index, mouseAreaData) {
            if (!$.elycharts.featuresmanager.initialized)
                this.init();
            for (var i = 0; i < $.elycharts.featuresmanager.managers.length; i++)
                if ($.elycharts.featuresmanager.managers[i][1].onMouseEnter)
                    $.elycharts.featuresmanager.managers[i][1].onMouseEnter(env, serie, index, mouseAreaData);
        },

        onMouseChanged : function(env, serie, index, mouseAreaData) {
            if (!$.elycharts.featuresmanager.initialized)
                this.init();
            for (var i = 0; i < $.elycharts.featuresmanager.managers.length; i++)
                if ($.elycharts.featuresmanager.managers[i][1].onMouseChanged)
                    $.elycharts.featuresmanager.managers[i][1].onMouseChanged(env, serie, index, mouseAreaData);
        },

        onMouseExit : function(env, serie, index, mouseAreaData) {
            if (!$.elycharts.featuresmanager.initialized)
                this.init();
            for (var i = 0; i < $.elycharts.featuresmanager.managers.length; i++)
                if ($.elycharts.featuresmanager.managers[i][1].onMouseExit)
                    $.elycharts.featuresmanager.managers[i][1].onMouseExit(env, serie, index, mouseAreaData);
        }
    }

})(jQuery);

/***********************************************

 * OGGETTI USATI:

 PIECE:
 Contiene un elemento da visualizzare nel grafico. E' un oggetto con queste proprietÃ :

 - section,[serie],[index],[subsection]: Dati che permettono di identificare che tipo
 di elemento Ã¨ e a quale blocco della configurazione appartiene.
 Ad esempio gli elementi principali del chart hanno
 section="Series", serie=nome della serie, subSection = 'Plot'
 - [paths]: Contiene un array di pathdata, nel caso questo piece Ã¨ costituito da
 piu' sottoelementi (ad esempio i Dots, o gli elementi di un Pie o Funnel)
 - [PATHDATA.*]: Se questo piece e' costituito da un solo elemento, i suoi dati sono
 memorizzati direttamente nella root di PIECE.
 - show: Proprieta' usata internamente per decidere se questo piece dovrÃ  essere
 visualizzato o meno (in genere nel caso di una transizione che non ha variato
 questo piece, che quindi puo' essere lasciato allo stato precedente)
 - hide: Proprieta' usata internamente per decidere se l'elemento va nascosto,
 usato in caso di transizione se l'elemento non Ã¨ piu' presente.

 PATHDATA:
 I dati utili per visualizzare un path nel canvas:

 - PATH: Il path che permette di disegnare l'elemento. Se NULL l'elemento Ã¨ vuoto/ da
 non visualizzare (instanziato solo come placeholder)
 - attr: gli attributi Raphael dell'elemento. NULL se path Ã¨ NULL.
 - [center]: centro del path
 - [rect]: rettangolo che include il path

 PATH:
 Un array in cui ogni elemento determina un passo del percorso per disegnare il grafico.
 E' una astrazione sul PATH SVG effettivo, e puo' avere alcuni valori speciali:
 [ [ 'TEXT',  testo, x, y ] ]
 [ [ 'CIRCLE', x, y, raggio ] ]
 [ [ 'RECT', x1, y1, x2, y2, rounded ] ] (x1,y1 dovrebbero essere sempre le coordinate in alto a sx)
 [ [ 'SLICE', x, y, raggio, raggio int, angolo1, angolo2 ] ] (gli angoli sono in gradi)
 [ [ 'RELEMENT', element ] ] (elemento Raphael gia' disegnato)
 [ [ 'DOMELEMENT', element ] ] (elemento DOM - in genere un DIV html - giÃ  disegnato)
 [ ... Path SVG ... ]

 ------------------------------------------------------------------------

 Z-INDEX:
 0 : base
 10 : tooltip
 20 : interactive area (tutti gli elementi innescati dalla interactive area dovrebbero essere < 20)
 25 : label / balloons (potrebbero essere resi cliccabili dall'esterno, quindi > 20)

 ------------------------------------------------------------------------

 USEFUL RESOURCES:

 http://docs.jquery.com/Plugins/Authoring
 http://www.learningjquery.com/2007/10/a-plugin-development-pattern
 http://dean.edwards.name/packer/2/usage/#special-chars

 http://raphaeljs.com/reference.html#attr

 TODO
 * ottimizzare common.areaProps
 * rifare la posizione del tooltip del pie
 * ripristinare shadow

 *********************************************/
/********* Source File: src/elycharts_manager_anchor.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {

    var featuresmanager = $.elycharts.featuresmanager;
    var common = $.elycharts.common;

    /***********************************************************************
     * FEATURE: ANCHOR
     *
     * Permette di collegare i dati del grafico con delle aree esterne,
     * identificate dal loro selettore CSS, e di interagire con esse.
     **********************************************************************/

    $.elycharts.anchormanager = {

        afterShow : function(env, pieces) {
            // Prendo le aree gestite da mouseAreas, e metto i miei listener
            // Non c'e' bisogno di gestire il clean per una chiamata successiva, lo fa gia' il mouseareamanager
            // Tranne per i bind degli eventi jquery

            if (!env.opt.anchors)
                return;

            if (!env.anchorBinds)
                env.anchorBinds = [];

            while (env.anchorBinds.length) {
                var b = env.anchorBinds.pop();
                $(b[0]).unbind(b[1], b[2]);
            }

            for (var i = 0; i < env.mouseAreas.length; i++) {
                var serie = env.mouseAreas[i].piece ? env.mouseAreas[i].piece.serie : false;
                var anc;
                if (serie)
                    anc = env.opt.anchors[serie][env.mouseAreas[i].index];
                else
                    anc = env.opt.anchors[env.mouseAreas[i].index];

                if (anc && env.mouseAreas[i].props.anchor && env.mouseAreas[i].props.anchor.highlight) {

                    (function(env, mouseAreaData, anc, caller) {

                        var f1 = function() { caller.anchorMouseOver(env, mouseAreaData); };
                        var f2 = function() { caller.anchorMouseOut(env, mouseAreaData); };
                        if (!env.mouseAreas[i].props.anchor.useMouseEnter) {
                            env.anchorBinds.push([anc, 'mouseover', f1]);
                            env.anchorBinds.push([anc, 'mouseout', f2]);
                            $(anc).mouseover(f1);
                            $(anc).mouseout(f2);
                        } else {
                            env.anchorBinds.push([anc, 'mouseenter', f1]);
                            env.anchorBinds.push([anc, 'mouseleave', f2]);
                            $(anc).mouseenter(f1);
                            $(anc).mouseleave(f2);
                        }
                    })(env, env.mouseAreas[i], anc, this);
                }
            }

            env.onAnchors = [];
        },

        anchorMouseOver : function(env, mouseAreaData) {
            $.elycharts.highlightmanager.onMouseOver(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
        },

        anchorMouseOut : function(env, mouseAreaData) {
            $.elycharts.highlightmanager.onMouseOut(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
        },

        onMouseOver : function(env, serie, index, mouseAreaData) {
            if (!env.opt.anchors)
                return;

            if (mouseAreaData.props.anchor && mouseAreaData.props.anchor.addClass) {
                //var serie = mouseAreaData.piece ? mouseAreaData.piece.serie : false;
                var anc;
                if (serie)
                    anc = env.opt.anchors[serie][mouseAreaData.index];
                else
                    anc = env.opt.anchors[mouseAreaData.index];
                if (anc) {
                    $(anc).addClass(mouseAreaData.props.anchor.addClass);
                    env.onAnchors.push([anc, mouseAreaData.props.anchor.addClass]);
                }
            }
        },

        onMouseOut : function(env, serie, index, mouseAreaData) {
            if (!env.opt.anchors)
                return;

            while (env.onAnchors.length > 0) {
                var o = env.onAnchors.pop();
                $(o[0]).removeClass(o[1]);
            }
        }
    }

    $.elycharts.featuresmanager.register($.elycharts.anchormanager, 30);

})(jQuery);
/********* Source File: src/elycharts_manager_animation.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {

//var featuresmanager = $.elycharts.featuresmanager;
    var common = $.elycharts.common;

    /***********************************************************************
     * ANIMATIONMANAGER
     **********************************************************************/

    $.elycharts.animationmanager = {

        beforeShow : function(env, pieces) {
            if (!env.newopt)
                this.startAnimation(env, pieces);
            else
                this.stepAnimation(env, pieces);
        },

        stepAnimation : function(env, pieces) {
            // env.pieces sono i vecchi pieces, ed e' sempre un array completo di tutte le sezioni
            // pieces sono i nuovi pezzi da mostrare, e potrebbe essere parziale
            //console.warn('from1', common._clone(env.pieces));
            //console.warn('from2', common._clone(pieces));
            pieces = this._stepAnimationInt(env, env.pieces, pieces);
            //console.warn('to', common._clone(pieces));
        },

        _stepAnimationInt : function(env, pieces1, pieces2, section, serie, internal) {
            // Se pieces2 == null deve essere nascosto tutto pieces1

            var newpieces = [], newpiece;
            var j = 0;
            for (var i = 0; i < pieces1.length; i ++) {
                var animationProps = common.areaProps(env, section ? section : pieces1[i].section, serie ? serie : pieces1[i].serie);
                if (animationProps && animationProps.stepAnimation)
                    animationProps = animationProps.stepAnimation;
                else
                    animationProps = env.opt.features.animation.stepAnimation;

                // Se il piece attuale c'e' solo in pieces2 lo riporto nei nuovi, impostando come gia' mostrato
                // A meno che internal = true (siamo in un multipath, nel caso se una cosa non c'e' va considerata da togliere)
                if (pieces2 && (j >= pieces2.length || !common.samePiecePath(pieces1[i], pieces2[j]))) {
                    if (!internal) {
                        pieces1[i].show = false;
                        newpieces.push(pieces1[i]);
                    } else {
                        newpiece = { path : false, attr : false, show : true };
                        newpiece.animation = {
                            element : pieces1[i].element ? pieces1[i].element : false,
                            speed : animationProps && animationProps.speed ? animationProps.speed : 300,
                            easing : animationProps && animationProps.easing ? animationProps.easing : '',
                            delay : animationProps && animationProps.delay ? animationProps.delay : 0
                        }
                        newpieces.push(newpiece);
                    }
                }
                // Bisogna gestire la transizione dal vecchio piece al nuovo
                else {
                    newpiece = pieces2 ? pieces2[j] : { path : false, attr : false };
                    newpiece.show = true;
                    if (typeof pieces1[i].paths == 'undefined') {
                        // Piece a singolo path
                        newpiece.animation = {
                            element : pieces1[i].element ? pieces1[i].element : false,
                            speed : animationProps && animationProps.speed ? animationProps.speed : 300,
                            easing : animationProps && animationProps.easing ? animationProps.easing : '',
                            delay : animationProps && animationProps.delay ? animationProps.delay : 0
                        }
                        // Se non c'era elemento precedente deve gestire il fadeIn
                        if (!pieces1[i].element)
                            newpiece.animation.startAttr = {opacity : 0};

                    } else {
                        // Multiple path piece
                        newpiece.paths = this._stepAnimationInt(env, pieces1[i].paths, pieces2[j].paths, pieces1[i].section, pieces1[i].serie, true);
                    }
                    newpieces.push(newpiece);
                    j++;
                }
            }
            // If there are pieces left in pieces2 i must add them unchanged
            if (pieces2)
                for (; j < pieces2.length; j++)
                    newpieces.push(pieces2[j]);

            return newpieces;
        },

        startAnimation : function(env, pieces) {
            for (var i = 0; i < pieces.length; i++)
                if (pieces[i].paths || pieces[i].path) {
                    var props = common.areaProps(env, pieces[i].section, pieces[i].serie);
                    if (props && props.startAnimation)
                        props = props.startAnimation;
                    else
                        props = env.opt.features.animation.startAnimation;

                    if (props.active) {
                        if (props.type == 'simple' || pieces[i].section != 'Series')
                            this.animationSimple(env, props, pieces[i]);
                        if (props.type == 'grow')
                            this.animationGrow(env, props, pieces[i]);
                        if (props.type == 'avg')
                            this.animationAvg(env, props, pieces[i]);
                        if (props.type == 'reg')
                            this.animationReg(env, props, pieces[i]);
                    }
                }
        },

        /**
         * Inserisce i dati base di animazione del piece e la transizione di attributi
         */
        _animationPiece : function(piece, animationProps, subSection) {
            if (piece.paths) {
                for (var i = 0; i < piece.paths.length; i++)
                    this._animationPiece(piece.paths[i], animationProps, subSection);
            } else if (piece.path) {
                piece.animation = {
                    speed : animationProps.speed,
                    easing : animationProps.easing,
                    delay : animationProps.delay,
                    startPath : [],
                    startAttr : common._clone(piece.attr)
                };
                if (animationProps.propsTo)
                    piece.attr = common._mergeObjects(piece.attr, animationProps.propsTo);
                if (animationProps.propsFrom)
                    piece.animation.startAttr = common._mergeObjects(piece.animation.startAttr, animationProps.propsFrom);
                if (subSection && animationProps[subSection.toLowerCase() + 'PropsFrom'])
                    piece.animation.startAttr = common._mergeObjects(piece.animation.startAttr, animationProps[subSection.toLowerCase() + 'PropsFrom']);

                if (typeof piece.animation.startAttr.opacity != 'undefined' && typeof piece.attr.opacity == 'undefined')
                    piece.attr.opacity = 1;
            }
        },

        animationSimple : function(env, props, piece) {
            this._animationPiece(piece, props, piece.subSection);
        },

        animationGrow : function(env, props, piece) {
            this._animationPiece(piece, props, piece.subSection);
            var i, npath, y;

            switch (env.opt.type) {
                case 'line':
                    y = env.opt.height - env.opt.margins[2];
                    switch (piece.subSection) {
                        case 'Plot':
                            if (!piece.paths) {
                                npath = [ 'LINE', [], piece.path[0][2]];
                                for (i = 0; i < piece.path[0][1].length; i++)
                                    npath[1].push([ piece.path[0][1][i][0], y ]);
                                piece.animation.startPath.push(npath);

                            } else {
                                for (i = 0; i < piece.paths.length; i++)
                                    if (piece.paths[i].path)
                                        piece.paths[i].animation.startPath.push([ 'RECT', piece.paths[i].path[0][1], y, piece.paths[i].path[0][3], y ]);
                            }
                            break;
                        case 'Fill':
                            npath = [ 'LINEAREA', [], [], piece.path[0][3]];
                            for (i = 0; i < piece.path[0][1].length; i++) {
                                npath[1].push([ piece.path[0][1][i][0], y ]);
                                npath[2].push([ piece.path[0][2][i][0], y ]);
                            }
                            piece.animation.startPath.push(npath);

                            break;
                        case 'Dot':
                            for (i = 0; i < piece.paths.length; i++)
                                if (piece.paths[i].path)
                                    piece.paths[i].animation.startPath.push(['CIRCLE', piece.paths[i].path[0][1], y, piece.paths[i].path[0][3]]);
                            break;
                    }
                    break;

                case 'pie':
                    if (piece.subSection == 'Plot')
                        for (i = 0; i < piece.paths.length; i++)
                            if (piece.paths[i].path && piece.paths[i].path[0][0] == 'SLICE')
                                piece.paths[i].animation.startPath.push([ 'SLICE', piece.paths[i].path[0][1], piece.paths[i].path[0][2], piece.paths[i].path[0][4] + piece.paths[i].path[0][3] * 0.1, piece.paths[i].path[0][4], piece.paths[i].path[0][5], piece.paths[i].path[0][6] ]);

                    break;

                case 'funnel':
                    alert('Unsupported animation GROW for funnel');
                    break;

                case 'barline':
                    var x;
                    if (piece.section == 'Series' && piece.subSection == 'Plot') {
                        if (!props.subType)
                            x = env.opt.direction != 'rtl' ? env.opt.margins[3] : env.opt.width - env.opt.margins[1];
                        else if (props.subType == 1)
                            x = env.opt.direction != 'rtl' ? env.opt.width - env.opt.margins[1] : env.opt.margins[3];
                        for (i = 0; i < piece.paths.length; i++)
                            if (piece.paths[i].path) {
                                if (!props.subType || props.subType == 1)
                                    piece.paths[i].animation.startPath.push([ 'RECT', x, piece.paths[i].path[0][2], x, piece.paths[i].path[0][4], piece.paths[i].path[0][5] ]);
                                else {
                                    y = (piece.paths[i].path[0][2] + piece.paths[i].path[0][4]) / 2;
                                    piece.paths[i].animation.startPath.push([ 'RECT', piece.paths[i].path[0][1], y, piece.paths[i].path[0][3], y, piece.paths[i].path[0][5] ]);
                                }
                            }
                    }

                    break;
            }
        },

        _animationAvgXYArray : function(arr) {
            var res = [], avg = 0, i;
            for (i = 0; i < arr.length; i++)
                avg += arr[i][1];
            avg = avg / arr.length;
            for (i = 0; i < arr.length; i++)
                res.push([ arr[i][0], avg ]);
            return res;
        },

        animationAvg : function(env, props, piece) {
            this._animationPiece(piece, props, piece.subSection);

            var avg = 0, i, l;
            switch (env.opt.type) {
                case 'line':
                    switch (piece.subSection) {
                        case 'Plot':
                            if (!piece.paths) {
                                // LINE
                                piece.animation.startPath.push([ 'LINE', this._animationAvgXYArray(piece.path[0][1]), piece.path[0][2] ]);

                            } else {
                                // BAR
                                l = 0;
                                for (i = 0; i < piece.paths.length; i++)
                                    if (piece.paths[i].path) {
                                        l ++;
                                        avg += piece.paths[i].path[0][2];
                                    }
                                avg = avg / l;
                                for (i = 0; i < piece.paths.length; i++)
                                    if (piece.paths[i].path)
                                        piece.paths[i].animation.startPath.push([ "RECT", piece.paths[i].path[0][1], avg, piece.paths[i].path[0][3], piece.paths[i].path[0][4] ]);
                            }
                            break;

                        case 'Fill':
                            piece.animation.startPath.push([ 'LINEAREA', this._animationAvgXYArray(piece.path[0][1]), this._animationAvgXYArray(piece.path[0][2]), piece.path[0][3] ]);

                            break;

                        case 'Dot':
                            l = 0;
                            for (i = 0; i < piece.paths.length; i++)
                                if (piece.paths[i].path) {
                                    l ++;
                                    avg += piece.paths[i].path[0][2];
                                }
                            avg = avg / l;
                            for (i = 0; i < piece.paths.length; i++)
                                if (piece.paths[i].path)
                                    piece.paths[i].animation.startPath.push(['CIRCLE', piece.paths[i].path[0][1], avg, piece.paths[i].path[0][3]]);
                            break;
                    }
                    break;

                case 'pie':
                    var delta = 360 / piece.paths.length;

                    if (piece.subSection == 'Plot')
                        for (i = 0; i < piece.paths.length; i++)
                            if (piece.paths[i].path && piece.paths[i].path[0][0] == 'SLICE')
                                piece.paths[i].animation.startPath.push([ 'SLICE', piece.paths[i].path[0][1], piece.paths[i].path[0][2], piece.paths[i].path[0][3], piece.paths[i].path[0][4], i * delta, (i + 1) * delta ]);

                    break;

                case 'funnel':
                    alert('Unsupported animation AVG for funnel');
                    break;

                case 'barline':
                    alert('Unsupported animation AVG for barline');
                    break;
            }
        },

        _animationRegXYArray : function(arr) {
            var res = [];
            var c = arr.length;
            var y1 = arr[0][1];
            var y2 = arr[c - 1][1];

            for (var i = 0; i < arr.length; i++)
                res.push([arr[i][0], y1 + (y2 - y1) / (c - 1) * i]);

            return res;
        },

        animationReg : function(env, props, piece) {
            this._animationPiece(piece, props, piece.subSection);
            var i, c, y1, y2;

            switch (env.opt.type) {
                case 'line':
                    switch (piece.subSection) {
                        case 'Plot':
                            if (!piece.paths) {
                                // LINE
                                piece.animation.startPath.push([ 'LINE', this._animationRegXYArray(piece.path[0][1]), piece.path[0][2] ]);

                            } else {
                                // BAR
                                c = piece.paths.length;
                                if (c > 1) {
                                    for (i = 0; !piece.paths[i].path && i < piece.paths.length; i++) {}
                                    y1 = piece.paths[i].path ? common.getY(piece.paths[i].path[0]) : 0;
                                    for (i = piece.paths.length - 1; !piece.paths[i].path && i >= 0; i--) {}
                                    y2 = piece.paths[i].path ? common.getY(piece.paths[i].path[0]) : 0;

                                    for (i = 0; i < piece.paths.length; i++)
                                        if (piece.paths[i].path)
                                            piece.paths[i].animation.startPath.push([ "RECT", piece.paths[i].path[0][1], y1 + (y2 - y1) / (c - 1) * i, piece.paths[i].path[0][3], piece.paths[i].path[0][4] ]);
                                }
                            }
                            break;

                        case 'Fill':
                            piece.animation.startPath.push([ 'LINEAREA', this._animationRegXYArray(piece.path[0][1]), this._animationRegXYArray(piece.path[0][2]), piece.path[0][3] ]);
                            break;

                        case 'Dot':
                            c = piece.paths.length;
                            if (c > 1) {
                                for (i = 0; !piece.paths[i].path && i < piece.paths.length; i++) {}
                                y1 = piece.paths[i].path ? common.getY(piece.paths[i].path[0]) : 0;
                                for (i = piece.paths.length - 1; !piece.paths[i].path && i >= 0; i--) {}
                                y2 = piece.paths[i].path ? common.getY(piece.paths[i].path[0]) : 0;

                                for (i = 0; i < piece.paths.length; i++)
                                    if (piece.paths[i].path)
                                        piece.paths[i].animation.startPath.push(['CIRCLE', piece.paths[i].path[0][1], y1 + (y2 - y1) / (c - 1) * i, piece.paths[i].path[0][3]]);
                            }
                            break;
                    }
                    break;

                case 'pie':
                    alert('Unsupported animation REG for pie');
                    break;

                case 'funnel':
                    alert('Unsupported animation REG for funnel');
                    break;

                case 'barline':
                    alert('Unsupported animation REG for barline');
                    break;
            }
        }
    }

    $.elycharts.featuresmanager.register($.elycharts.animationmanager, 10);

    /***********************************************************************
     * FRAMEANIMATIONMANAGER
     **********************************************************************/

    $.elycharts.frameanimationmanager = {

        beforeShow : function(env, pieces) {
            if (env.opt.features.frameAnimation.active)
                $(env.container.get(0)).css(env.opt.features.frameAnimation.cssFrom);
        },

        afterShow : function(env, pieces) {
            if (env.opt.features.frameAnimation.active)
                env.container.animate(env.opt.features.frameAnimation.cssTo, env.opt.features.frameAnimation.speed, env.opt.features.frameAnimation.easing);
        }
    };

    $.elycharts.featuresmanager.register($.elycharts.frameanimationmanager, 90);

})(jQuery);
/********* Source File: src/elycharts_manager_highlight.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {

//var featuresmanager = $.elycharts.featuresmanager;
    var common = $.elycharts.common;

    /***********************************************************************
     * FEATURE: HIGHLIGHT
     *
     * Permette di evidenziare in vari modi l'area in cui si passa con il
     * mouse.
     **********************************************************************/

    $.elycharts.highlightmanager = {

        removeHighlighted : function(env, full) {
            if (env.highlighted)
                while (env.highlighted.length > 0) {
                    var o = env.highlighted.pop();
                    if (o.piece) {
                        if (full)
                            common.animationStackPush(env, o.piece, o.piece.element, common.getPieceFullAttr(env, o.piece), o.cfg.restoreSpeed, o.cfg.restoreEasing, 0, true);
                    } else
                        o.element.remove();
                }
        },

        afterShow : function(env, pieces) {
            if (env.highlighted && env.highlighted.length > 0)
                this.removeHighlighted(env, false);
            env.highlighted = [];
        },

        onMouseOver : function(env, serie, index, mouseAreaData) {
            var path, element;
            // TODO Se non e' attivo l'overlay (per la serie o per tutto) e' inutile fare il resto

            // Cerco i piece da evidenziare (tutti quelli che sono costituiti da path multipli)
            for (var i = 0; i < mouseAreaData.pieces.length; i++)

                // Il loop sotto estrae solo i pieces con array di path (quindi non i line o i fill del linechart ... ma il resto si)
                if (mouseAreaData.pieces[i].section == 'Series' && mouseAreaData.pieces[i].paths
                    && (!serie || mouseAreaData.pieces[i].serie == serie)
                    && mouseAreaData.pieces[i].paths[index] && mouseAreaData.pieces[i].paths[index].element) {
                    var piece = mouseAreaData.pieces[i].paths[index];
                    element = piece.element;
                    path = piece.path;
                    var attr = common.getElementOriginalAttrs(element);
                    var newattr = false; // In caso la geometria dell'oggetto Ã¨ modificata mediante attr (es: per circle) qui memorizza i nuovi attributi
                    var props = serie ? mouseAreaData.props : common.areaProps(env, mouseAreaData.pieces[i].section, mouseAreaData.pieces[i].serie);
                    var pelement, ppiece, ppath;
                    if (path && props.highlight) {
                        if (props.highlight.scale) {
                            var scale = props.highlight.scale;
                            if (typeof scale == 'number')
                                scale = [scale, scale];

                            if (path[0][0] == 'RECT') {
                                var w = path[0][3] - path[0][1];
                                var h = path[0][4] - path[0][2];
                                path = [ [ 'RECT', path[0][1], path[0][2] - h * (scale[1] - 1), path[0][3] + w * (scale[0] - 1), path[0][4] ] ];
                                common.animationStackPush(env, piece, element, common.getSVGProps(common.preparePathShow(env, path)), props.highlight.scaleSpeed, props.highlight.scaleEasing);
                            }
                            else if (path[0][0] == 'CIRCLE') {
                                // I pass directly new radius
                                newattr = {r : path[0][3] * scale[0]};
                                common.animationStackPush(env, piece, element, newattr, props.highlight.scaleSpeed, props.highlight.scaleEasing);
                            }
                            else if (path[0][0] == 'SLICE') {
                                // Per lo slice x e' il raggio, y e' l'angolo
                                var d = (path[0][6] - path[0][5]) * (scale[1] - 1) / 2;
                                if (d > 90)
                                    d = 90;
                                path = [ [ 'SLICE', path[0][1], path[0][1], path[0][3] * scale[0], path[0][4], path[0][5] - d, path[0][6] + d ] ];
                                common.animationStackPush(env, piece, element, common.getSVGProps(common.preparePathShow(env, path)), props.highlight.scaleSpeed, props.highlight.scaleEasing);

                            } else if (env.opt.type == 'funnel') {
                                var dx = (piece.rect[2] - piece.rect[0]) * (scale[0] - 1) / 2;
                                var dy = (piece.rect[3] - piece.rect[1]) * (scale[1] - 1) / 2;

                                // Specifico di un settore del funnel
                                common.animationStackStart(env);
                                path = [ common.movePath(env, [ path[0]], [-dx, -dy])[0],
                                    common.movePath(env, [ path[1]], [+dx, -dy])[0],
                                    common.movePath(env, [ path[2]], [+dx, +dy])[0],
                                    common.movePath(env, [ path[3]], [-dx, +dy])[0],
                                    path[4] ];
                                common.animationStackPush(env, piece, element, common.getSVGProps(common.preparePathShow(env, path)), props.highlight.scaleSpeed, props.highlight.scaleEasing, 0, true);

                                // Se c'e' un piece precedente lo usa, altrimenti cerca un topSector per la riduzione
                                pelement = false;
                                if (index > 0) {
                                    ppiece = mouseAreaData.pieces[i].paths[index - 1];
                                    pelement = ppiece.element;
                                    ppath = ppiece.path;
                                } else {
                                    ppiece = common.findInPieces(mouseAreaData.pieces, 'Sector', 'top');
                                    if (ppiece) {
                                        pelement = ppiece.element;
                                        ppath = ppiece.path;
                                    }
                                }
                                if (pelement) {
                                    //pattr = common.getElementOriginalAttrs(pelement);
                                    ppath = [
                                        ppath[0], ppath[1],
                                        common.movePath(env, [ ppath[2]], [+dx, -dy])[0],
                                        common.movePath(env, [ ppath[3]], [-dx, -dy])[0],
                                        ppath[4] ];
                                    common.animationStackPush(env, ppiece, pelement, common.getSVGProps(common.preparePathShow(env, ppath)), props.highlight.scaleSpeed, props.highlight.scaleEasing, 0, true);
                                    env.highlighted.push({piece : ppiece, cfg : props.highlight});
                                }

                                // Se c'e' un piece successivo lo usa, altrimenti cerca un bottomSector per la riduzione
                                pelement = false;
                                if (index < mouseAreaData.pieces[i].paths.length - 1) {
                                    ppiece = mouseAreaData.pieces[i].paths[index + 1];
                                    pelement = ppiece.element;
                                    ppath = ppiece.path;
                                } else {
                                    ppiece = common.findInPieces(mouseAreaData.pieces, 'Sector', 'bottom');
                                    if (ppiece) {
                                        pelement = ppiece.element;
                                        ppath = ppiece.path;
                                    }
                                }
                                if (pelement) {
                                    //var pattr = common.getElementOriginalAttrs(pelement);
                                    ppath = [
                                        common.movePath(env, [ ppath[0]], [-dx, +dy])[0],
                                        common.movePath(env, [ ppath[1]], [+dx, +dy])[0],
                                        ppath[2], ppath[3],
                                        ppath[4] ];
                                    common.animationStackPush(env, ppiece, pelement, common.getSVGProps(common.preparePathShow(env, ppath)), props.highlight.scaleSpeed, props.highlight.scaleEasing, 0, true);
                                    env.highlighted.push({piece : ppiece, cfg : props.highlight});
                                }

                                common.animationStackEnd(env);
                            }
                            /* Con scale non va bene
                             if (!attr.scale)
                             attr.scale = [1, 1];
                             element.attr({scale : [scale[0], scale[1]]}); */
                        }
                        if (props.highlight.newProps) {
                            for (var a in props.highlight.newProps)
                                if (typeof attr[a] == 'undefined')
                                    attr[a] = false;
                            common.animationStackPush(env, piece, element, props.highlight.newProps);
                        }
                        if (props.highlight.move) {
                            var offset = $.isArray(props.highlight.move) ? props.highlight.move : [props.highlight.move, 0];
                            path = common.movePath(env, path, offset);
                            common.animationStackPush(env, piece, element, common.getSVGProps(common.preparePathShow(env, path)), props.highlight.moveSpeed, props.highlight.moveEasing);
                        }

                        //env.highlighted.push({element : element, attr : attr});
                        env.highlighted.push({piece : piece, cfg : props.highlight});

                        if (props.highlight.overlayProps) {
                            // NOTA: path e' il path modificato dai precedenti (cosi' l'overlay tiene conto della cosa), deve guardare anche a newattr
                            //BIND: mouseAreaData.listenerDisabled = true;
                            element = common.showPath(env, path);
                            if (newattr)
                                element.attr(newattr);
                            element.attr(props.highlight.overlayProps);
                            //BIND: $(element.node).unbind().mouseover(mouseAreaData.mouseover).mouseout(mouseAreaData.mouseout);
                            // Se metto immediatamente il mouseAreaData.listenerDisabled poi va comunque un mouseout dalla vecchia area e va
                            // in loop. TODO Rivedere e sistemare anche per tooltip
                            //BIND: setTimeout(function() { mouseAreaData.listenerDisabled = false; }, 10);
                            attr = false;
                            env.highlighted.push({element : element, attr : attr, cfg : props.highlight});
                        }
                    }
                }

            if (env.opt.features.highlight.indexHighlight && env.opt.type == 'line') {
                var t = env.opt.features.highlight.indexHighlight;
                if (t == 'auto')
                    t = (env.indexCenter == 'bar' ? 'bar' : 'line');

                var delta1 = (env.opt.width - env.opt.margins[3] - env.opt.margins[1]) / (env.opt.labels.length > 0 ? env.opt.labels.length : 1);
                var delta2 = (env.opt.width - env.opt.margins[3] - env.opt.margins[1]) / (env.opt.labels.length > 1 ? env.opt.labels.length - 1 : 1);
                var lineCenter = true;

                switch (t) {
                    case 'bar':
                        path = [ ['RECT', env.opt.margins[3] + index * delta1, env.opt.margins[0] ,
                            env.opt.margins[3] + (index + 1) * delta1, env.opt.height - env.opt.margins[2] ] ];
                        break;

                    case 'line':
                        lineCenter = false;
                    case 'barline':
                        var x = Math.round((lineCenter ? delta1 / 2 : 0) + env.opt.margins[3] + index * (lineCenter ? delta1 : delta2));
                        path = [[ 'M', x, env.opt.margins[0]], ['L', x, env.opt.height - env.opt.margins[2]]];
                }
                if (path) {
                    //BIND: mouseAreaData.listenerDisabled = true;
                    element = common.showPath(env, path).attr(env.opt.features.highlight.indexHighlightProps);
                    //BIND: $(element.node).unbind().mouseover(mouseAreaData.mouseover).mouseout(mouseAreaData.mouseout);
                    //BIND: setTimeout(function() { mouseAreaData.listenerDisabled = false; }, 10);
                    env.highlighted.push({element : element, attr : false, cfg : env.opt.features.highlight});
                }
            }
        },

        onMouseOut : function(env, serie, index, mouseAreaData) {
            this.removeHighlighted(env, true);
        }

    };

    $.elycharts.featuresmanager.register($.elycharts.highlightmanager, 21);

})(jQuery);
/********* Source File: src/elycharts_manager_label.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {

//var featuresmanager = $.elycharts.featuresmanager;
    var common = $.elycharts.common;

    /***********************************************************************
     * FEATURE: LABELS
     *
     * Permette di visualizzare in vari modi le label del grafico.
     * In particolare per pie e funnel permette la visualizzazione all'interno
     * delle fette.
     * Per i line chart le label sono visualizzate giÃ  nella gestione assi.
     *
     * TODO:
     * - Comunque per i line chart si potrebbe gestire la visualizzazione
     *   all'interno delle barre, o sopra i punti.
     **********************************************************************/

    $.elycharts.labelmanager = {

        beforeShow : function(env, pieces) {

            if (!common.executeIfChanged(env, ['labels', 'values', 'series']))
                return;

            if (env.opt.labels && (env.opt.type == 'pie' || env.opt.type == 'funnel')) {
                var /*lastSerie = false, */lastIndex = false;
                var paths;

                for (var i = 0; i < pieces.length; i++) {
                    if (pieces[i].section == 'Series' && pieces[i].subSection == 'Plot') {
                        var props = common.areaProps(env, 'Series', pieces[i].serie);
                        if (env.emptySeries && env.opt.series.empty)
                            props.label = $.extend(true, props.label, env.opt.series.empty.label);
                        if (props && props.label && props.label.active) {
                            paths = [];
                            for (var index = 0; index < pieces[i].paths.length; index++)
                                if (pieces[i].paths[index].path) {
                                    //lastSerie = pieces[i].serie;
                                    lastIndex = index;
                                    paths.push(this.showLabel(env, pieces[i], pieces[i].paths[index], pieces[i].serie, index, pieces));
                                } else
                                    paths.push({ path : false, attr : false });
                            pieces.push({ section : pieces[i].section, serie : pieces[i].serie, subSection : 'Label', paths: paths });
                        }
                    }
                    else if (pieces[i].section == 'Sector' && pieces[i].serie == 'bottom' && !pieces[i].subSection && lastIndex < env.opt.labels.length - 1) {
                        paths = [];
                        paths.push(this.showLabel(env, pieces[i], pieces[i], 'Series', env.opt.labels.length - 1, pieces));
                        pieces.push({ section : pieces[i].section, serie : pieces[i].serie, subSection : 'Label', paths: paths });
                    }
                }

            }
        },

        showLabel : function(env, piece, path, serie, index, pieces) {
            var pp = common.areaProps(env, 'Series', serie, index);
            if (env.opt.labels[index] || pp.label.label) {
                var p = path;
                var label = pp.label.label ? pp.label.label : env.opt.labels[index];
                var center = common.getCenter(p, pp.label.offset);
                if (!pp.label.html) {
                    var attr = pp.label.props;
                    if (pp.label.frameAnchor) {
                        attr = common._clone(pp.label.props);
                        attr['text-anchor'] = pp.label.frameAnchor[0];
                        attr['alignment-baseline'] = pp.label.frameAnchor[1];
                    }
                    /*pieces.push({
                     path : [ [ 'TEXT', label, center[0], center[1] ] ], attr : attr, 
                     section: 'Series', serie : serie, index : index, subSection : 'Label'
                     });*/
                    return { path : [ [ 'TEXT', label, center[0], center[1] ] ], attr : attr };

                } else {
                    var opacity = 1;
                    var style = common._clone(pp.label.style);
                    var set_opacity = (typeof style.opacity != 'undefined')
                    if (set_opacity) {
                        opacity = style.opacity;
                        style.opacity = 0;
                    }
                    style.position = 'absolute';
                    style['z-index'] = 25;

                    var el;
                    if (typeof label == 'string')
                        el = $('<div>' + label + '</div>').css(style).prependTo(env.container);
                    else
                        el = $(label).css(style).prependTo(env.container);

                    // Centramento corretto label
                    if (env.opt.features.debug.active && el.height() == 0)
                        alert('DEBUG: Al gestore label e\' stata passata una label ancora senza dimensioni, quindi ancora non disegnata. Per questo motivo il posizionamento potrebbe non essere correto.');
                    var posX = center[0];
                    var posY = center[1];
                    if (!pp.label.frameAnchor || pp.label.frameAnchor[0] == 'middle')
                        posX -= el.width() / 2;
                    else if (pp.label.frameAnchor && pp.label.frameAnchor[0] == 'end')
                        posX -= el.width();
                    if (!pp.label.frameAnchor || pp.label.frameAnchor[1] == 'middle')
                        posY -= el.height() / 2;
                    else if (pp.label.frameAnchor && pp.label.frameAnchor[1] == 'top')
                        posY -= el.height();
                    if (set_opacity)
                        el.css({ margin: posY + 'px 0 0 ' + posX + 'px', opacity : opacity});
                    else
                        el.css({ margin: posY + 'px 0 0 ' + posX + 'px'});

                    /*pieces.push({
                     path : [ [ 'DOMELEMENT', el ] ], attr : false,
                     section: 'Series', serie : serie, index : index, subSection : 'Label'
                     });*/
                    return { path : [ [ 'DOMELEMENT', el ] ], attr : false };

                }
            }
            return false;
        }
    }

    $.elycharts.featuresmanager.register($.elycharts.labelmanager, 5);

})(jQuery);
/********* Source File: src/elycharts_manager_legend.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {

//var featuresmanager = $.elycharts.featuresmanager;
    var common = $.elycharts.common;

    /***********************************************************************
     * FEATURE: LEGEND
     **********************************************************************/

    $.elycharts.legendmanager = {

        afterShow : function(env, pieces) {
            if (!env.opt.legend || env.opt.legend.length == 0)
                return;

            var props = env.opt.features.legend;

            if (props.x == 'auto') {
                var autox = 1;
                props.x = 0;
            }
            if (props.width == 'auto') {
                var autowidth = 1;
                props.width = env.opt.width;
            }

            var borderPath = [ [ 'RECT', props.x, props.y, props.x + props.width, props.y + props.height, props.r ] ];
            var border = common.showPath(env, borderPath).attr(props.borderProps);
            if (autox || autowidth)
                border.hide();

            var wauto = 0;
            var items = [];
            // env.opt.legend normalmente Ã¨ { serie : 'Legend', ... }, per i pie invece { serie : ['Legend', ...], ... }
            var legendCount = 0;
            var serie, data, h, w, x, y, xd;
            for (serie in env.opt.legend) {
                if (env.opt.type != 'pie')
                    legendCount ++;
                else
                    legendCount += env.opt.legend[serie].length;
            }
            var i = 0;
            for (serie in env.opt.legend) {
                if (env.opt.type != 'pie')
                    data = [ env.opt.legend[serie] ];
                else
                    data = env.opt.legend[serie];

                for (var j = 0; j < data.length; j++) {
                    var sprops = common.areaProps(env, 'Series', serie, env.opt.type == 'pie' ? j : false);
                    var dprops = $.extend(true, {}, props.dotProps);
                    if (sprops.legend && sprops.legend.dotProps)
                        dprops = $.extend(true, dprops, sprops.legend.dotProps);
                    if (!dprops.fill && env.opt.type == 'pie') {
                        if (sprops.color)
                            dprops.fill = sprops.color;
                        if (sprops.plotProps && sprops.plotProps.fill)
                            dprops.fill = sprops.plotProps.fill;
                    }
                    var dtype = sprops.legend && sprops.legend.dotType ? sprops.legend.dotType : props.dotType;
                    var dwidth = sprops.legend && sprops.legend.dotWidth ? sprops.legend.dotWidth : props.dotWidth;
                    var dheight = sprops.legend && sprops.legend.dotHeight ? sprops.legend.dotHeight : props.dotHeight;
                    var dr = sprops.legend && sprops.legend.dotR ? sprops.legend.dotR : props.dotR;
                    var tprops = sprops.legend && sprops.legend.textProps ? sprops.legend.textProps : props.textProps;

                    if (!props.horizontal) {
                        // Posizione dell'angolo in alto a sinistra
                        h = (props.height - props.margins[0] - props.margins[2]) / legendCount;
                        w = props.width - props.margins[1] - props.margins[3];
                        x = Math.floor(props.x + props.margins[3]);
                        y = Math.floor(props.y + props.margins[0] + h * i);
                    } else {
                        h = props.height - props.margins[0] - props.margins[2];
                        if (!props.itemWidth || props.itemWidth == 'fixed') {
                            w = (props.width - props.margins[1] - props.margins[3]) / legendCount;
                            x = Math.floor(props.x + props.margins[3] + w * i);
                        } else {
                            w = (props.width - props.margins[1] - props.margins[3]) - wauto;
                            x = props.x + props.margins[3] + wauto;
                        }
                        y = Math.floor(props.y + props.margins[0]);
                    }

                    if (dtype == "rect") {
                        items.push(common.showPath(env, [ [ 'RECT', props.dotMargins[0] + x, y + Math.floor((h - dheight) / 2), props.dotMargins[0] + x + dwidth, y + Math.floor((h - dheight) / 2) + dheight, dr ] ]).attr(dprops));
                        xd = props.dotMargins[0] + dwidth + props.dotMargins[1];
                    } else if (dtype == "circle") {
                        items.push(common.showPath(env, [ [ 'CIRCLE', props.dotMargins[0] + x + dr, y + (h / 2), dr ] ]).attr(dprops));
                        xd = props.dotMargins[0] + dr * 2 + props.dotMargins[1];
                    }

                    var text = data[j];
                    var t = common.showPath(env, [ [ 'TEXT', text, x + xd, y + Math.ceil(h / 2) + ($.browser.msie ? 2 : 0) ] ]).attr({"text-anchor" : "start"}).attr(tprops); //.hide();
                    items.push(t);
                    while (t.getBBox().width > (w - xd) && t.getBBox().width > 10) {
                        text = text.substring(0, text.length - 1);
                        t.attr({text : text});
                    }
                    t.show();

                    if (props.horizontal && props.itemWidth == 'auto')
                        wauto += xd + t.getBBox().width + 4;
                    else if (!props.horizontal && autowidth)
                        wauto = t.getBBox().width + xd > wauto ? t.getBBox().width + xd : wauto;
                    else
                        wauto += w;

                    i++;
                }
            }

            if (autowidth)
                props.width = wauto + props.margins[3] + props.margins[1] - 1;
            if (autox) {
                props.x = Math.floor((env.opt.width - props.width) / 2);
                for (i in items) {
                    if (items[i].attrs.x)
                        items[i].attr('x', items[i].attrs.x + props.x);
                    else
                        items[i].attr('path', common.movePath(env, items[i].attrs.path, [props.x, 0]));
                }
            }
            if (autowidth || autox) {
                borderPath = [ [ 'RECT', props.x, props.y, props.x + props.width, props.y + props.height, props.r ] ];
                border.attr(common.getSVGProps(common.preparePathShow(env, borderPath)));
                //border.attr({path : common.preparePathShow(env, common.getSVGPath(borderPath))});
                border.show();
            }
        }
    }

    $.elycharts.featuresmanager.register($.elycharts.legendmanager, 90);

})(jQuery);
/********* Source File: src/elycharts_manager_mouse.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {

    var featuresmanager = $.elycharts.featuresmanager;
    var common = $.elycharts.common;

    /***********************************************************************
     * MOUSEMANAGER
     **********************************************************************/

    $.elycharts.mousemanager = {

        afterShow : function(env, pieces) {
            if (!env.opt.interactive)
                return;

            if (env.mouseLayer) {
                env.mouseLayer.remove();
                env.mouseLayer = null;
                env.mousePaper.remove();
                env.mousePaper = null;
                env.mouseTimer = null;
                env.mouseAreas = null;
                // Meglio fare anche l'unbind???
            }

            env.mouseLayer = $('<div></div>').css({position : 'absolute', 'z-index' : 20, opacity : 0}).prependTo(env.container);
            env.mousePaper = common._RaphaelInstance(env.mouseLayer.get(0), env.opt.width, env.opt.height);
            var paper = env.mousePaper;

            if (env.opt.features.debug.active && typeof DP_Debug != 'undefined') {
                env.paper.text(env.opt.width, env.opt.height - 5, 'DEBUG').attr({ 'text-anchor' : 'end', stroke: 'red', opacity: .1 });
                paper.text(env.opt.width, env.opt.height - 5, 'DEBUG').attr({ 'text-anchor' : 'end', stroke: 'red', opacity: .1 }).click(function() {
                    DP_Debug.dump(env.opt, '', false, 4);
                });
            }

            var i, j;

            // Adding mouseover only in right area, based on pieces
            env.mouseAreas = [];
            if (env.opt.features.mousearea.type == 'single') {
                // SINGLE: Every serie's index is an area
                for (i = 0; i < pieces.length; i++) {
                    if (pieces[i].mousearea) {
                        // pathstep
                        if (!pieces[i].paths) {
                            // path standard, generating an area for each point
                            if (pieces[i].path.length >= 1 && (pieces[i].path[0][0] == 'LINE' || pieces[i].path[0][0] == 'LINEAREA'))
                                for (j = 0; j < pieces[i].path[0][1].length; j++) {
                                    var props = common.areaProps(env, pieces[i].section, pieces[i].serie);
                                    if (props.mouseareaShowOnNull || pieces[i].section != 'Series' || env.opt.values[pieces[i].serie][j] != null)
                                        env.mouseAreas.push({
                                            path : [ [ 'CIRCLE', pieces[i].path[0][1][j][0], pieces[i].path[0][1][j][1], 10 ] ],
                                            piece : pieces[i],
                                            pieces : pieces,
                                            index : j,
                                            props : props
                                        });
                                }

                            else // Code below is only for standard path - it should be useless now (now there are only LINE and LINEAREA)
                            // TODO DELETE
                                for (j = 0; j < pieces[i].path.length; j++) {
                                    env.mouseAreas.push({
                                        path : [ [ 'CIRCLE', common.getX(pieces[i].path[j]), common.getY(pieces[i].path[j]), 10 ] ],
                                        piece : pieces[i],
                                        pieces : pieces,
                                        index : j,
                                        props : common.areaProps(env, pieces[i].section, pieces[i].serie)
                                    });
                                }

                            // paths
                        } else if (pieces[i].paths) {
                            // Set of paths (bar graph?), generating overlapped areas
                            for (j = 0; j < pieces[i].paths.length; j++)
                                if (pieces[i].paths[j].path)
                                    env.mouseAreas.push({
                                        path : pieces[i].paths[j].path,
                                        piece : pieces[i],
                                        pieces : pieces,
                                        index : j,
                                        props : common.areaProps(env, pieces[i].section, pieces[i].serie)
                                    });
                        }
                    }
                }
            } else {
                // INDEX: Each index (in every serie) is an area
                var indexCenter = env.opt.features.mousearea.indexCenter;
                if (indexCenter == 'auto')
                    indexCenter = env.indexCenter;
                var start, delta;
                if (indexCenter == 'bar') {
                    delta = (env.opt.width - env.opt.margins[3] - env.opt.margins[1]) / (env.opt.labels.length > 0 ? env.opt.labels.length : 1);
                    start = env.opt.margins[3];
                } else {
                    delta = (env.opt.width - env.opt.margins[3] - env.opt.margins[1]) / (env.opt.labels.length > 1 ? env.opt.labels.length - 1 : 1);
                    start = env.opt.margins[3] - delta / 2;
                }

                for (var index in env.opt.labels) {
                    env.mouseAreas.push({
                        path : [ [ 'RECT', start + index * delta, env.opt.margins[0], start + (index + 1) * delta, env.opt.height - env.opt.margins[2] ] ],
                        piece : false,
                        pieces : pieces,
                        index : parseInt(index),
                        props : env.opt.defaultSeries // TODO common.areaProps(env, 'Plot')
                    });
                }
            }

            var syncenv = false;
            if (!env.opt.features.mousearea.syncTag) {
                env.mouseareaenv = { chartEnv : false, mouseObj : false, caller : false, inArea : -1, timer : false };
                syncenv = env.mouseareaenv;
            } else {
                if (!$.elycharts.mouseareaenv)
                    $.elycharts.mouseareaenv = {};
                if (!$.elycharts.mouseareaenv[env.opt.features.mousearea.syncTag])
                    $.elycharts.mouseareaenv[env.opt.features.mousearea.syncTag] = { chartEnv : false, mouseObj : false, caller : false, inArea : -1, timer : false };
                syncenv = $.elycharts.mouseareaenv[env.opt.features.mousearea.syncTag];
            }
            for (i = 0; i < env.mouseAreas.length; i++) {
                env.mouseAreas[i].area = common.showPath(env, env.mouseAreas[i].path, paper).attr({stroke: "none", fill: "#fff", opacity: 0});

                (function(env, obj, objidx, caller, syncenv) {
                    var piece = obj.piece;
                    var index = obj.index;

                    obj.mouseover = function(e) {
                        //BIND: if (obj.listenerDisabled) return;
                        obj.event = e;
                        clearTimeout(syncenv.timer);
                        caller.onMouseOverArea(env, piece, index, obj);

                        if (syncenv.chartEnv && syncenv.chartEnv.id != env.id) {
                            // Chart changed, removing old one
                            syncenv.caller.onMouseExitArea(syncenv.chartEnv, syncenv.mouseObj.piece, syncenv.mouseObj.index, syncenv.mouseObj);
                            caller.onMouseEnterArea(env, piece, index, obj);
                        }
                        else if (syncenv.inArea != objidx) {
                            if (syncenv.inArea < 0)
                                caller.onMouseEnterArea(env, piece, index, obj);
                            else
                                caller.onMouseChangedArea(env, piece, index, obj);
                        }
                        syncenv.chartEnv = env;
                        syncenv.mouseObj = obj;
                        syncenv.caller = caller;
                        syncenv.inArea = objidx;
                    };
                    obj.mouseout = function(e) {
                        //BIND: if (obj.listenerDisabled) return;
                        obj.event = e;
                        clearTimeout(syncenv.timer);
                        caller.onMouseOutArea(env, piece, index, obj);
                        syncenv.timer = setTimeout(function() {
                            syncenv.timer = false;
                            caller.onMouseExitArea(env, piece, index, obj);
                            syncenv.chartEnv = false;
                            syncenv.inArea = -1;
                        }, env.opt.features.mousearea.areaMoveDelay);
                    };

                    $(obj.area.node).mouseover(obj.mouseover);
                    $(obj.area.node).mouseout(obj.mouseout);
                })(env, env.mouseAreas[i], i, this, syncenv);
            }
        },

        // Called when mouse enter an area
        onMouseOverArea : function(env, piece, index, mouseAreaData) {
            //console.warn('over', piece.serie, index);
            if (env.opt.features.mousearea.onMouseOver)
                env.opt.features.mousearea.onMouseOver(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
            featuresmanager.onMouseOver(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
        },

        // Called when mouse exit from an area
        onMouseOutArea : function(env, piece, index, mouseAreaData) {
            //console.warn('out', piece.serie, index);
            if (env.opt.features.mousearea.onMouseOut)
                env.opt.features.mousearea.onMouseOut(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
            featuresmanager.onMouseOut(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
        },

        // Called when mouse enter an area from empty space (= it was in no area before)
        onMouseEnterArea : function(env, piece, index, mouseAreaData) {
            //console.warn('enter', piece.serie, index);
            if (env.opt.features.mousearea.onMouseEnter)
                env.opt.features.mousearea.onMouseEnter(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
            featuresmanager.onMouseEnter(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
        },

        // Called when mouse enter an area and it was on another area
        onMouseChangedArea : function(env, piece, index, mouseAreaData) {
            //console.warn('changed', piece.serie, index);
            if (env.opt.features.mousearea.onMouseChanged)
                env.opt.features.mousearea.onMouseChanged(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
            featuresmanager.onMouseChanged(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
        },

        // Called when mouse leaves an area and does not enter in another one (timeout check)
        onMouseExitArea : function(env, piece, index, mouseAreaData) {
            //console.warn('exit', piece.serie, index);
            if (env.opt.features.mousearea.onMouseExit)
                env.opt.features.mousearea.onMouseExit(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
            featuresmanager.onMouseExit(env, mouseAreaData.piece ? mouseAreaData.piece.serie : false, mouseAreaData.index, mouseAreaData);
        }

    }

    $.elycharts.featuresmanager.register($.elycharts.mousemanager, 0);

})(jQuery);
/********* Source File: src/elycharts_manager_tooltip.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {

//var featuresmanager = $.elycharts.featuresmanager;
    var common = $.elycharts.common;

    /***********************************************************************
     * FEATURE: TOOLTIP
     **********************************************************************/

    $.elycharts.tooltipmanager = {

        afterShow : function(env, pieces) {
            if (env.tooltipContainer) {
                env.tooltipFrame.remove();
                env.tooltipFrame = null;
                env.tooltipFrameElement = null;
                env.tooltipContent.remove();
                env.tooltipContent = null;
                env.tooltipContainer.remove();
                env.tooltipContainer = null;
            }

            if (!$.elycharts.tooltipid)
                $.elycharts.tooltipid = 0;
            $.elycharts.tooltipid ++;

            // Preparo il tooltip
            env.tooltipContainer = $('<div id="elycharts_tooltip_' + $.elycharts.tooltipid + '" style="position: absolute; top: 100; left: 100; z-index: 10; overflow: hidden; white-space: nowrap; display: none"><div id="elycharts_tooltip_' + $.elycharts.tooltipid + '_frame" style="position: absolute; top: 0; left: 0; z-index: -1"></div><div id="elycharts_tooltip_' + $.elycharts.tooltipid + '_content" style="cursor: default"></div></div>').appendTo(document.body);
            env.tooltipFrame = common._RaphaelInstance('elycharts_tooltip_' + $.elycharts.tooltipid + '_frame', 500, 500);
            env.tooltipContent = $('#elycharts_tooltip_' + $.elycharts.tooltipid + '_content');
        },

        _prepareShow : function(env, props, mouseAreaData, tip) {
            if (env.tooltipFrameElement)
                env.tooltipFrameElement.attr(props.frameProps);
            if (props.padding)
                env.tooltipContent.css({ padding : props.padding[0] + 'px ' + props.padding[1] + 'px' });
            env.tooltipContent.css(props.contentStyle);
            env.tooltipContent.html(tip);

            //BIND: env.tooltipContainer.unbind().mouseover(mouseAreaData.mouseover).mouseout(mouseAreaData.mouseout);

            // WARN: Prendendo env.paper.canvas non va bene...
            //var offset = $(env.paper.canvas).offset();
            var offset = $(env.container).offset();

            if (env.opt.features.tooltip.fixedPos) {
                offset.top += env.opt.features.tooltip.fixedPos[1];
                offset.left += env.opt.features.tooltip.fixedPos[0];

            } else {
                var coord = this.getXY(env, props, mouseAreaData);
                if (!coord[2]) {
                    offset.left += coord[0];
                    while (offset.top + coord[1] < 0)
                        coord[1] += 20;
                    offset.top += coord[1];
                } else {
                    offset.left = coord[0];
                    offset.top = coord[1];
                }
            }

            return { top : offset.top, left : offset.left };
        },

        /**
         * Ritorna [x, y] oppure [x, y, true] se le coordinate sono relative alla pagina (e non al grafico)
         */
        getXY : function(env, props, mouseAreaData) {
            // NOTA Posizione mouse: mouseAreaData.event.pageX/pageY
            var x = 0, y = 0;
            if (mouseAreaData.path[0][0] == 'RECT') {
                // L'area e' su un rettangolo (un bar o un indice completo), il tooltip lo faccio subito sopra
                // Nota: per capire se e' sull'indice completo basta guardare mouseAreaData.piece == null
                x = common.getX(mouseAreaData.path[0]) - props.offset[1];
                y = common.getY(mouseAreaData.path[0]) - props.height - props.offset[0];
            }
            else if (mouseAreaData.path[0][0] == 'CIRCLE') {
                // L'area e' su un cerchio (punto di un line)
                x = common.getX(mouseAreaData.path[0]) - props.offset[1];
                y = common.getY(mouseAreaData.path[0]) - props.height - props.offset[0];
            }
            else if (mouseAreaData.path[0][0] == 'SLICE') {
                // L'area Ã¨ su una fetta di torta (pie)
                var path = mouseAreaData.path[0];

                // Genera la posizione del tip considerando che deve stare all'interno di un cerchio che Ã¨ sempre dalla parte opposta dell'area
                // e deve essere il piu' vicino possibile all'area
                var w = props.width && props.width != 'auto' ? props.width : 100;
                var h = props.height && props.height != 'auto' ? props.height : 100;
                // Raggio del cerchio che contiene il tip
                var cr = Math.sqrt(Math.pow(w,2) + Math.pow(h,2)) / 2;
                if (cr > env.opt.r)
                    cr = env.opt.r;

                var tipangle = path[5] + (path[6] - path[5]) / 2 + 180;
                var rad = Math.PI / 180;
                x = path[1] + cr * Math.cos(- tipangle * rad) - w / 2;
                y = path[2] + cr * Math.sin(- tipangle * rad) - h / 2;
            }
            else if (mouseAreaData.piece && mouseAreaData.piece.paths && mouseAreaData.index >= 0 && mouseAreaData.piece.paths[mouseAreaData.index] && mouseAreaData.piece.paths[mouseAreaData.index].rect) {
                // L'area ha una forma complessa, ma abbiamo il rettangolo di contenimento (funnel)
                var rect = mouseAreaData.piece.paths[mouseAreaData.index].rect;
                x = rect[0] - props.offset[1];
                y = rect[1] - props.height - props.offset[0];
            }

            if (env.opt.features.tooltip.positionHandler)
                return env.opt.features.tooltip.positionHandler(env, props, mouseAreaData, x, y);
            else
                return [x, y];
        },

        getTip : function(env, serie, index) {
            var tip = false;
            if (env.opt.tooltips) {
                if (typeof env.opt.tooltips == 'function')
                    tip = env.opt.tooltips(env, serie, index, serie && env.opt.values[serie] && env.opt.values[serie][index] ? env.opt.values[serie][index] : false, env.opt.labels && env.opt.labels[index] ? env.opt.labels[index] : false);
                else {
                    if (serie && env.opt.tooltips[serie] && env.opt.tooltips[serie][index])
                        tip = env.opt.tooltips[serie][index];
                    else if (!serie && env.opt.tooltips[index])
                        tip = env.opt.tooltips[index];
                }
            }
            return tip;
        },

        onMouseEnter : function(env, serie, index, mouseAreaData) {
            var props = mouseAreaData.props.tooltip;
            if (env.emptySeries && env.opt.series.empty)
                props = $.extend(true, props, env.opt.series.empty.tooltip);
            if (!props || !props.active)
                return false;

            var tip = this.getTip(env, serie, index);
            if (!tip)
                return this.onMouseExit(env, serie, index, mouseAreaData);

            //if (!env.opt.tooltips || (serie && (!env.opt.tooltips[serie] || !env.opt.tooltips[serie][index])) || (!serie && !env.opt.tooltips[index]))
            //  return this.onMouseExit(env, serie, index, mouseAreaData);
            //var tip = serie ? env.opt.tooltips[serie][index] : env.opt.tooltips[index];

            // Il dimensionamento del tooltip e la view del frame SVG, lo fa solo se width ed height sono specificati
            if (props.width && props.width != 'auto' && props.height && props.height != 'auto') {
                var delta = props.frameProps && props.frameProps['stroke-width'] ? props.frameProps['stroke-width'] : 0;
                env.tooltipContainer.width(props.width + delta + 1).height(props.height + delta + 1);
                if (!env.tooltipFrameElement && props.frameProps)
                    env.tooltipFrameElement = env.tooltipFrame.rect(delta / 2, delta / 2, props.width, props.height, props.roundedCorners);
            }

            env.tooltipContainer.css(this._prepareShow(env, props, mouseAreaData, tip)).fadeIn(env.opt.features.tooltip.fadeDelay);

            return true;
        },

        onMouseChanged : function(env, serie, index, mouseAreaData) {
            var props = mouseAreaData.props.tooltip;
            if (env.emptySeries && env.opt.series.empty)
                props = $.extend(true, props, env.opt.series.empty.tooltip);
            if (!props || !props.active)
                return false;

            var tip = this.getTip(env, serie, index);
            if (!tip)
                return this.onMouseExit(env, serie, index, mouseAreaData);

            /*if (!env.opt.tooltips || (serie && (!env.opt.tooltips[serie] || !env.opt.tooltips[serie][index])) || (!serie && !env.opt.tooltips[index]))
             return this.onMouseExit(env, serie, index, mouseAreaData);
             var tip = serie ? env.opt.tooltips[serie][index] : env.opt.tooltips[index];*/

            env.tooltipContainer.clearQueue();
            // Nota: Non passo da animationStackPush, i tooltip non sono legati a piece
            env.tooltipContainer.animate(this._prepareShow(env, props, mouseAreaData, tip), env.opt.features.tooltip.moveDelay, 'linear' /*swing*/);

            return true;
        },

        onMouseExit : function(env, serie, index, mouseAreaData) {
            var props = mouseAreaData.props.tooltip;
            if (env.emptySeries && env.opt.series.empty)
                props = $.extend(true, props, env.opt.series.empty.tooltip);
            if (!props || !props.active)
                return false;

            //env.tooltipContainer.unbind();
            env.tooltipContainer.fadeOut(env.opt.features.tooltip.fadeDelay);

            return true;
        }
    }

    $.elycharts.featuresmanager.register($.elycharts.tooltipmanager, 20);

})(jQuery);
/********* Source File: src/elycharts_chart_line.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {

    var featuresmanager = $.elycharts.featuresmanager;
    var common = $.elycharts.common;

    /***********************************************************************
     * CHART: LINE/BAR
     **********************************************************************/

    $.elycharts.line = {
        init : function($env) {
        },

        draw : function(env) {
            if (common.executeIfChanged(env, ['values', 'series'])) {
                env.plots = {};
                env.axis = { x : {} };
                env.barno = 0;
                env.indexCenter = 'line';
            }

            var opt = env.opt;
            var plots = env.plots;
            var axis = env.axis;
            var paper = env.paper;

            var values = env.opt.values;
            var labels = env.opt.labels;
            var i, cum, props, serie, plot, labelsCount;

            // Valorizzazione di tutte le opzioni utili e le impostazioni interne di ogni grafico e dell'ambiente di lavoro
            if (common.executeIfChanged(env, ['values', 'series'])) {
                var idx = 0;
                var prevVisibleSerie = false;
                for (serie in values) {
                    plot = {
                        index : idx,
                        type : false,
                        visible : false
                    };
                    plots[serie] = plot;
                    if (values[serie]) {
                        props = common.areaProps(env, 'Series', serie);
                        plot.type = props.type;
                        if (props.type == 'bar')
                            env.indexCenter = 'bar';

                        if (props.visible) {
                            plot.visible = true;
                            if (!labelsCount || labelsCount < values[serie].length)
                                labelsCount = values[serie].length;

                            // Values
                            // showValues: manage NULL elements (doing an avg of near points) for line serie
                            var showValues = []
                            for (i = 0; i < values[serie].length; i++) {
                                var val = values[serie][i];
                                if (val == null) {
                                    if (props.type == 'bar')
                                        val = 0;
                                    else {
                                        for (var j = i + 1; j < values[serie].length && values[serie][j] == null; j++) {}
                                        var next = j < values[serie].length ? values[serie][j] : null;
                                        for (var k = i -1; k >= 0 && values[serie][k] == null; k--) {}
                                        var prev = k >= 0 ? values[serie][k] : null;
                                        val = next != null ? (prev != null ? (next * (i - k) + prev * (j - i)) / (j - k) : next) : prev;
                                    }
                                }
                                showValues.push(val);
                            }

                            if (props.stacked && !(typeof props.stacked == 'string'))
                                props.stacked = prevVisibleSerie;

                            if (typeof props.stacked == 'undefined' || props.stacked == serie || props.stacked < 0 || !plots[props.stacked] || !plots[props.stacked].visible || plots[props.stacked].type != plot.type) {
                                // NOT Stacked
                                plot.ref = serie;
                                if (props.type == 'bar')
                                    plot.barno = env.barno ++;
                                plot.from = [];
                                if (!props.cumulative)
                                    plot.to = showValues;
                                else {
                                    plot.to = [];
                                    cum = 0;
                                    for (i = 0; i < showValues.length; i++)
                                        plot.to.push(cum += showValues[i]);
                                }
                                for (i = 0; i < showValues.length; i++)
                                    plot.from.push(0);

                            } else {
                                // Stacked
                                plot.ref = props.stacked;
                                if (props.type == 'bar')
                                    plot.barno = plots[props.stacked].barno;
                                plot.from = plots[props.stacked].stack;
                                plot.to = [];
                                cum = 0;
                                if (!props.cumulative)
                                    for (i = 0; i < showValues.length; i++)
                                        plot.to.push(plot.from[i] + showValues[i]);
                                else
                                    for (i = 0; i < showValues.length; i++)
                                        plot.to.push(plot.from[i] + (cum += showValues[i]));
                                plots[props.stacked].stack = plot.to;
                            }

                            plot.stack = plot.to;
                            plot.max = Math.max.apply(Math, plot.from.concat(plot.to));
                            plot.min = Math.min.apply(Math, plot.from.concat(plot.to));

                            // Assi (DEP: values, series)
                            if (props.axis) {
                                if (!axis[props.axis])
                                    axis[props.axis] = { plots : [] };
                                axis[props.axis].plots.push(serie);
                                if (typeof axis[props.axis].max == 'undefined')
                                    axis[props.axis].max = plot.max;
                                else
                                    axis[props.axis].max = Math.max(axis[props.axis].max, plot.max);
                                if (typeof axis[props.axis].min == 'undefined')
                                    axis[props.axis].min = plot.min;
                                else
                                    axis[props.axis].min = Math.min(axis[props.axis].min, plot.min);
                            }

                            prevVisibleSerie = serie;
                        }
                    }
                }
            }

            // Labels normalization (if not set or less  than values)
            if (!labels)
                labels = [];
            while (labelsCount > labels.length)
                labels.push(null);
            labelsCount = labels.length;
            env.opt.labels = labels;

            // Prepare axis scale (values, series, axis)
            if (common.executeIfChanged(env, ['values', 'series', 'axis'])) {
                for (var lidx in axis) {
                    props = common.areaProps(env, 'Axis', lidx);
                    axis[lidx].props = props;

                    if (typeof props.max != 'undefined')
                        axis[lidx].max = props.max;
                    if (typeof props.min != 'undefined')
                        axis[lidx].min = props.min;

                    if (axis[lidx].min == axis[lidx].max)
                        axis[lidx].max = axis[lidx].min + 1;

                    if (props.normalize && props.normalize > 0) {
                        var v = Math.abs(axis[lidx].max);
                        if (axis[lidx].min && Math.abs(axis[lidx].min) > v)
                            v = Math.abs(axis[lidx].min);
                        if (v) {
                            var basev = Math.floor(Math.log(v)/Math.LN10) - (props.normalize - 1);
                            // NOTE: On firefox Math.pow(10, -X) sometimes results in number noise (0.89999...), it's better to do 1/Math.pow(10,X)
                            basev = basev >= 0 ? Math.pow(10, basev) : 1 / Math.pow(10, -basev);
                            v = Math.ceil(v / basev / (opt.features.grid.ny ? opt.features.grid.ny : 1)) * basev * (opt.features.grid.ny ? opt.features.grid.ny : 1);
                            // Calculation above, with decimal number sometimes insert some noise in numbers (eg: 8.899999... instead of 0.9), so i need to round result with proper precision
                            v = Math.round(v / basev) * basev;
                            // I need to store the normalization base for further roundin (eg: in axis label, sometimes calculation results in "number noise", so i need to round them with proper precision)
                            axis[lidx].normalizationBase = basev;
                            if (axis[lidx].max)
                                axis[lidx].max = Math.ceil(axis[lidx].max / v) * v;
                            if (axis[lidx].min)
                                axis[lidx].min = Math.floor(axis[lidx].min / v) * v;
                        }
                    }
                    if (axis[lidx].plots)
                        for (var ii = 0; ii < axis[lidx].plots.length; ii++) {
                            plots[axis[lidx].plots[ii]].max = axis[lidx].max;
                            plots[axis[lidx].plots[ii]].min = axis[lidx].min;
                        }
                }
            }

            var pieces = [];

            this.grid(env, pieces);

            // DEP: *
            var deltaX = (opt.width - opt.margins[3] - opt.margins[1]) / (labels.length > 1 ? labels.length - 1 : 1);
            var deltaBarX = (opt.width - opt.margins[3] - opt.margins[1]) / (labels.length > 0 ? labels.length : 1);

            for (serie in values) {
                props = common.areaProps(env, 'Series', serie);
                plot = plots[serie];

                // TODO Settare una props in questo modo potrebbe incasinare la gestione degli update parziali (se iso "lineCenter: auto" e passo da un grafico con indexCenter = bar a uno con indexCenter = line)
                if (props.lineCenter && props.lineCenter == 'auto')
                    props.lineCenter = (env.indexCenter == 'bar');
                else if (props.lineCenter && env.indexCenter == 'line')
                    env.indexCenter = 'bar';

                if (values[serie] && props.visible) {
                    var deltaY = (opt.height - opt.margins[2] - opt.margins[0]) / (plot.max - plot.min);

                    if (props.type == 'line') {
                        // LINE CHART
                        var linePath = [ 'LINE', [], props.rounded ];
                        var fillPath = [ 'LINEAREA', [], [], props.rounded ];
                        var dotPieces = [];

                        for (i = 0, ii = labels.length; i < ii; i++)
                            if (plot.to.length > i) {
                                var indexProps = common.areaProps(env, 'Series', serie, i);

                                var d = plot.to[i] > plot.max ? plot.max : (plot.to[i] < plot.min ? plot.min : plot.to[i]);
                                var x = Math.round((props.lineCenter ? deltaBarX / 2 : 0) + opt.margins[3] + i * (props.lineCenter ? deltaBarX : deltaX));
                                var y = Math.round(opt.height - opt.margins[2] - deltaY * (d - plot.min));
                                var dd = plot.from[i] > plot.max ? plot.max : (plot.from[i] < plot.min ? plot.min : plot.from[i]);
                                var yy = Math.round(opt.height - opt.margins[2] - deltaY * (dd - plot.min)) + ($.browser.msie ? 1 : 0);

                                linePath[1].push([x, y]);

                                if (props.fill) {
                                    fillPath[1].push([x, y]);
                                    fillPath[2].push([x, yy]);
                                }
                                if (indexProps.dot) {
                                    if (values[serie][i] == null && !indexProps.dotShowOnNull)
                                        dotPieces.push({path : false, attr : false});
                                    else
                                        dotPieces.push({path : [ [ 'CIRCLE', x, y, indexProps.dotProps.size ] ], attr : indexProps.dotProps}); // TODO Size should not be in dotProps (not an svg props)
                                }
                            }

                        if (props.fill)
                            pieces.push({ section : 'Series', serie : serie, subSection : 'Fill', path : [ fillPath ], attr : props.fillProps });
                        else
                            pieces.push({ section : 'Series', serie : serie, subSection : 'Fill', path : false, attr : false });
                        pieces.push({ section : 'Series', serie : serie, subSection : 'Plot', path : [ linePath ], attr : props.plotProps , mousearea : 'pathsteps'});

                        if (dotPieces.length)
                            pieces.push({ section : 'Series', serie : serie, subSection : 'Dot', paths : dotPieces });
                        else
                            pieces.push({ section : 'Series', serie : serie, subSection : 'Dot', path : false, attr : false });

                    } else {
                        pieceBar = [];

                        // BAR CHART
                        for (i = 0, ii = labels.length; i < ii; i++)
                            if (plot.to.length > i) {
                                if (plot.from[i] != plot.to[i]) {
                                    var bwid = Math.floor((deltaBarX - opt.barMargins) / env.barno);
                                    var bpad = bwid * (100 - props.barWidthPerc) / 200;
                                    var boff = opt.barMargins / 2 + plot.barno * bwid;

                                    var x1 = Math.floor(opt.margins[3] + i * deltaBarX + boff + bpad);
                                    var y1 = Math.round(opt.height - opt.margins[2] - deltaY * (plot.to[i] - plot.min));
                                    var y2 = Math.round(opt.height - opt.margins[2] - deltaY * (plot.from[i] - plot.min));

                                    pieceBar.push({path : [ [ 'RECT', x1, y1, x1 + bwid - bpad * 2, y2 ] ], attr : props.plotProps });
                                } else
                                    pieceBar.push({path : false, attr : false });
                            }

                        if (pieceBar.length)
                            pieces.push({ section : 'Series', serie : serie, subSection : 'Plot', paths: pieceBar, mousearea : 'paths' });
                        else
                            pieces.push({ section : 'Series', serie : serie, subSection : 'Plot', path: false, attr: false, mousearea : 'paths' });
                    }

                } else {
                    // Grafico non visibile / senza dati, deve comunque inserire i piece vuoti (NELLO STESSO ORDINE SOPRA!)
                    if (props.type == 'line')
                        pieces.push({ section : 'Series', serie : serie, subSection : 'Fill', path : false, attr : false });
                    pieces.push({ section : 'Series', serie : serie, subSection : 'Plot', path: false, attr: false, mousearea : 'paths' });
                    if (props.type == 'line')
                        pieces.push({ section : 'Series', serie : serie, subSection : 'Dot', path : false, attr : false });
                }
            }
            featuresmanager.beforeShow(env, pieces);
            common.show(env, pieces);
            featuresmanager.afterShow(env, pieces);
            return pieces;
        },

        grid : function(env, pieces) {

            // DEP: axis, [=> series, values], labels, margins, width, height, grid*
            if (common.executeIfChanged(env, ['values', 'series', 'axis', 'labels', 'margins', 'width', 'height', 'features.grid'])) {
                var opt = env.opt;
                var props = env.opt.features.grid;
                var paper = env.paper;
                var axis = env.axis;
                var labels = env.opt.labels;
                var deltaX = (opt.width - opt.margins[3] - opt.margins[1]) / (labels.length > 1 ? labels.length - 1 : 1);
                var deltaBarX = (opt.width - opt.margins[3] - opt.margins[1]) / (labels.length > 0 ? labels.length : 1);
                var i, j, x, y, lw, labx, laby, labe, val, txt;
                // Label X axis
                var paths = [];
                var labelsCenter = props.labelsCenter;
                if (labelsCenter == 'auto')
                    labelsCenter = (env.indexCenter == 'bar');

                if (axis.x && axis.x.props.labels) {
                    // used in case of labelsHideCovered, contains a "rotated" representation of the rect coordinates occupied by the last shown label
                    var lastShownLabelRect = false;
                    // labelsAnchor is "auto" by default. Can be "start","middle" or "end". If "auto" then it is automatically set depending on labelsRotate.
                    var labelsAnchor = axis.x.props.labelsAnchor || 'auto';
                    // Automatic labelsAnchor is "middle" on no rotation, otherwise the anchor is the higher side of the label.
                    if (labelsAnchor == 'auto')
                        labelsAnchor = axis.x.props.labelsRotate > 0 ? "start" : (axis.x.props.labelsRotate == 0 ? "middle" : "end");
                    // labelsPos is "auto" by default. Can be "start", "middle" or "end". If "auto" then it is automatically set depending on labelsCenter and labelsRotate and labelsAnchor.
                    var labelsPos = axis.x.props.labelsPos || 'auto';
                    // in labelsCenter (bar) it is middle when there is no rotation, equals to labelsAnchor on rotation.
                    // in !labelsCenter (line) is is always 'start';
                    if (labelsPos == 'auto')
                        labelsPos = labelsCenter ? (axis.x.props.labelsRotate == 0 ? labelsAnchor : 'middle') : 'start';

                    for (i = 0; i < labels.length; i++)
                        if ((typeof labels[i] != 'boolean' && labels[i] != null) || labels[i]) {

                            if (!axis.x.props.labelsSkip || i >= axis.x.props.labelsSkip) {
                                val = labels[i];

                                if (axis.x.props.labelsFormatHandler)
                                    val = axis.x.props.labelsFormatHandler(val);
                                txt = (axis.x.props.prefix ? axis.x.props.prefix : "") + val + (axis.x.props.suffix ? axis.x.props.suffix : "");

                                labx = opt.margins[3] + i * (labelsCenter ? deltaBarX : deltaX) + (axis.x.props.labelsMargin ? axis.x.props.labelsMargin : 0);
                                if (labelsPos == 'middle') labx += (labelsCenter ? deltaBarX : deltaX) / 2;
                                if (labelsPos == 'end') labx += (labelsCenter ? deltaBarX : deltaX);

                                laby = opt.height - opt.margins[2] + axis.x.props.labelsDistance;
                                labe = paper.text(labx, laby, txt).attr(axis.x.props.labelsProps).toBack();

                                labe.attr({"text-anchor" : labelsAnchor});

                                // will contain the boundingbox size, or false if it is hidden.
                                var boundingbox = false;
                                var bbox = labe.getBBox();
                                var p1 = {x: bbox.x, y: bbox.y};
                                var p2 = {x: bbox.x+bbox.width, y: bbox.y+bbox.height};
                                var o1 = {x: labx, y: laby};

                                rotate = function (p, rad) {
                                    var X = p.x * Math.cos(rad) - p.y * Math.sin(rad),
                                        Y = p.x * Math.sin(rad) + p.y * Math.cos(rad);
                                    return {x: X, y: Y};
                                };
                                // calculate collision between non rotated rects with vertext p1-p2 and t1-t2
                                // this algorythm works only for horizontal rects (alpha = 0)
                                // "dist" is the length added as a margin to the rects before collision detection
                                collide = function(r1,r2,dist) {
                                    xor = function(a,b) {
                                        return ( a || b ) && !( a && b );
                                    }
                                    if (r1.alpha != r2.alpha) throw "collide doens't support rects with different rotations";
                                    var r1p1r = rotate({x: r1.p1.x-dist, y:r1.p1.y-dist}, -r1.alpha);
                                    var r1p2r = rotate({x: r1.p2.x+dist, y:r1.p2.y+dist}, -r1.alpha);
                                    var r2p1r = rotate({x: r2.p1.x-dist, y:r2.p1.y-dist}, -r2.alpha);
                                    var r2p2r = rotate({x: r2.p2.x+dist, y:r2.p2.y+dist}, -r2.alpha);
                                    return !xor(Math.min(r1p1r.x,r1p2r.x) > Math.max(r2p1r.x,r2p2r.x), Math.max(r1p1r.x,r1p2r.x) < Math.min(r2p1r.x,r2p2r.x)) &&
                                        !xor(Math.min(r1p1r.y,r1p2r.y) > Math.max(r2p1r.y,r2p2r.y), Math.max(r1p1r.y,r1p2r.y) < Math.min(r2p1r.y,r2p2r.y));
                                }
                                // compute equivalent orizontal rotated rect
                                rotated = function(rect, origin, alpha) {
                                    translate = function (p1, p2) {
                                        return {x: p1.x+p2.x, y: p1.y+p2.y};
                                    };
                                    negate = function(p1) {
                                        return {x: -p1.x, y: -p1.y};
                                    };
                                    var p1trt = translate(rotate(translate(rect.p1,negate(origin)), alpha),origin);
                                    var p2trt = translate(rotate(translate(rect.p2,negate(origin)), alpha),origin);
                                    return { p1: p1trt, p2: p2trt, alpha: rect.alpha+alpha };
                                }
                                bbox = function(rect) {
                                    if (rect.alpha == 0) {
                                        return { x: rect.p1.x, y: rect.p1.y, width: rect.p2.x-rect.p1.x, height: rect.p2.y-rect.p1.y };
                                    } else {
                                        var points = [];
                                        points.push({ x: 0, y: 0 });
                                        points.push({ x: rect.p2.x-rect.p1.x, y: 0 });
                                        points.push({ x: 0, y: rect.p2.y-rect.p1.y });
                                        points.push({ x: rect.p2.x-rect.p1.x, y: rect.p2.y-rect.p1.y });
                                        var bb = [];
                                        bb['left'] = 0; bb['right'] = 0; bb['top'] = 0; bb['bottom'] = 0;
                                        for (_px = 0; _px < points.length; _px++) {
                                            var p = points[_px];
                                            var newX = parseInt((p.x * Math.cos(rect.alpha)) + (p.y * Math.sin(rect.alpha)));
                                            var newY = parseInt((p.x * Math.sin(rect.alpha)) + (p.y * Math.cos(rect.alpha)));
                                            bb['left'] = Math.min(bb['left'], newX);
                                            bb['right'] = Math.max(bb['right'], newX);
                                            bb['top'] = Math.min(bb['top'], newY);
                                            bb['bottom'] = Math.max(bb['bottom'], newY);
                                        }
                                        var newWidth = parseInt(Math.abs(bb['right'] - bb['left']));
                                        var newHeight = parseInt(Math.abs(bb['bottom'] - bb['top']));
                                        var newX = ((rect.p1.x + rect.p2.x) / 2) - newWidth / 2;
                                        var newY = ((rect.p1.y + rect.p2.y) / 2) - newHeight / 2;
                                        return { x: newX, y: newY, width: newWidth, height: newHeight };
                                    }
                                }

                                var alpha = Raphael.rad(axis.x.props.labelsRotate);
                                // compute used "rect" so to be able to check if there is overlapping with previous ones.
                                var rect = rotated({p1: p1, p2: p2, alpha: 0}, o1, alpha);

                                //console.log('bbox ',p1, p2, rect, props.nx, val, rect.p1, rect.p2, rect.alpha, boundingbox, opt.width);
                                // se collide con l'ultimo mostrato non lo mostro.
                                var dist = axis.x.props.labelsMarginRight ? axis.x.props.labelsMarginRight / 2 : 0;
                                if (axis.x.props.labelsHideCovered && lastShownLabelRect && collide(rect, lastShownLabelRect, dist)) {
                                    labe.hide();
                                    labels[i] = false;
                                } else {
                                    boundingbox = bbox(rect);
                                    // Manage label overflow
                                    if (props.nx == 'auto' && (boundingbox.x < 0 || boundingbox.x+boundingbox.width > opt.width)) {
                                        labe.hide();
                                        labels[i] = false;
                                    } else {
                                        lastShownLabelRect = rect;
                                    }
                                }

                                // Apply rotation to the element.
                                if (axis.x.props.labelsRotate) {
                                    labe.rotate(axis.x.props.labelsRotate, labx, laby).toBack();
                                }

                                paths.push({ path : [ [ 'RELEMENT', labe ] ], attr : false });
                            }
                        }
                }
                pieces.push({ section : 'Axis', serie : 'x', subSection : 'Label', paths : paths });

                // Title X Axis
                if (axis.x && axis.x.props.title) {
                    x = opt.margins[3] + Math.floor((opt.width - opt.margins[1] - opt.margins[3]) / 2);
                    y = opt.height - opt.margins[2] + axis.x.props.titleDistance * ($.browser.msie ? axis.x.props.titleDistanceIE : 1);
                    //paper.text(x, y, axis.x.props.title).attr(axis.x.props.titleProps);
                    pieces.push({ section : 'Axis', serie : 'x', subSection : 'Title', path : [ [ 'TEXT', axis.x.props.title, x, y ] ], attr : axis.x.props.titleProps });
                } else
                    pieces.push({ section : 'Axis', serie : 'x', subSection : 'Title', path : false, attr : false });

                // Label + Title L/R Axis
                for (var jj in ['l', 'r']) {
                    j = ['l', 'r'][jj];
                    if (axis[j] && axis[j].props.labels && props.ny) {
                        paths = [];
                        for (i = axis[j].props.labelsSkip ? axis[j].props.labelsSkip : 0; i <= props.ny; i++) {
                            var deltaY = (opt.height - opt.margins[2] - opt.margins[0]) / props.ny;
                            if (j == 'r') {
                                labx = opt.width - opt.margins[1] + axis[j].props.labelsDistance;
                                if (!axis[j].props.labelsProps["text-anchor"])
                                    axis[j].props.labelsProps["text-anchor"] = "start";
                            } else {
                                labx = opt.margins[3] - axis[j].props.labelsDistance;
                                if (!axis[j].props.labelsProps["text-anchor"])
                                    axis[j].props.labelsProps["text-anchor"] = "end";
                            }
                            if (axis[j].props.labelsAnchor && axis[j].props.labelsAnchor != 'auto')
                                axis[j].props.labelsProps["text-anchor"] = axis[j].props.labelsAnchor;
                            // NOTE: Parenthesis () around division are useful to keep right number precision
                            val = (axis[j].min + (i * ((axis[j].max - axis[j].min) / props.ny)));
                            // Rounding with proper precision for "number sharpening"
                            if (axis[j].normalizationBase)
                            // I use (1 / ( 1 / norm ) ) to avoid some noise
                                val = Math.round(val / axis[j].normalizationBase) / ( 1 / axis[j].normalizationBase );

                            if (axis[j].props.labelsFormatHandler)
                                val = axis[j].props.labelsFormatHandler(val);
                            if (axis[j].props.labelsCompactUnits)
                                val = common.compactUnits(val, axis[j].props.labelsCompactUnits);
                            txt = (axis[j].props.prefix ? axis[j].props.prefix : "") + val + (axis[j].props.suffix ? axis[j].props.suffix : "");
                            laby = opt.height - opt.margins[2] - i * deltaY;
                            //var labe = paper.text(labx, laby + (axis[j].props.labelsMargin ? axis[j].props.labelsMargin : 0), txt).attr(axis[j].props.labelsProps).toBack();
                            paths.push( { path : [ [ 'TEXT', txt, labx, laby + (axis[j].props.labelsMargin ? axis[j].props.labelsMargin : 0) ] ], attr : axis[j].props.labelsProps });
                        }
                        pieces.push({ section : 'Axis', serie : j, subSection : 'Label', paths : paths });
                    } else
                        pieces.push({ section : 'Axis', serie : j, subSection : 'Label', paths : [] });

                    if (axis[j] && axis[j].props.title) {
                        if (j == 'r')
                            x = opt.width - opt.margins[1] + axis[j].props.titleDistance * ($.browser.msie ? axis[j].props.titleDistanceIE : 1);
                        else
                            x = opt.margins[3] - axis[j].props.titleDistance * ($.browser.msie ? axis[j].props.titleDistanceIE : 1);
                        //paper.text(x, opt.margins[0] + Math.floor((opt.height - opt.margins[0] - opt.margins[2]) / 2), axis[j].props.title).attr(axis[j].props.titleProps).attr({rotation : j == 'l' ? 270 : 90});
                        var attr = common._clone(axis[j].props.titleProps);
                        attr.rotation = j == 'l' ? 270 : 90
                        pieces.push({ section : 'Axis', serie : j, subSection : 'Title', path : [ [ 'TEXT', axis[j].props.title, x, opt.margins[0] + Math.floor((opt.height - opt.margins[0] - opt.margins[2]) / 2) ] ], attr : attr });
                    } else
                        pieces.push({ section : 'Axis', serie : j, subSection : 'Title', path : false, attr : false });
                }

                // Grid
                if (props.nx || props.ny) {
                    var path = [], bandsH = [], bandsV = [],
                        nx = props.nx == 'auto' ? (labelsCenter ? labels.length : labels.length - 1) : props.nx,
                        ny = props.ny,
                        rowHeight = (opt.height - opt.margins[2] - opt.margins[0]) / (ny ? ny : 1),
                        columnWidth = (opt.width - opt.margins[1] - opt.margins[3]) / (nx ? nx : 1),
                        forceBorderX1 = typeof props.forceBorder == 'object' ? props.forceBorder[3] : props.forceBorder,
                        forceBorderX2 = typeof props.forceBorder == 'object' ? props.forceBorder[1] : props.forceBorder,
                        forceBorderY1 = typeof props.forceBorder == 'object' ? props.forceBorder[0] : props.forceBorder,
                        forceBorderY2 = typeof props.forceBorder == 'object' ? props.forceBorder[2] : props.forceBorder,
                        drawH = ny > 0 ? (typeof props.draw == 'object' ? props.draw[0] : props.draw) : false,
                        drawV = nx > 0 ? typeof props.draw == 'object' ? props.draw[1] : props.draw : false;

                    if (ny > 0)
                        for (i = 0; i < ny + 1; i++) {
                            if (
                                forceBorderY1 && i == 0 || // Show top line only if forced
                                    forceBorderY2 && i == ny ||  // Show bottom line only if forced
                                    drawH && i > 0 && i < ny // Show  other lines if draw = true
                                ) {
                                path.push(["M", opt.margins[3] - props.extra[3], opt.margins[0] + Math.round(i * rowHeight) ]);
                                path.push(["L", opt.width - opt.margins[1] + props.extra[1], opt.margins[0] + Math.round(i * rowHeight)]);
                            }
                            if (i < ny) {
                                if (i % 2 == 0 && props.evenHProps || i % 2 == 1 && props.oddHProps)
                                    bandsH.push({path : [ [ 'RECT',
                                        opt.margins[3] - props.extra[3], opt.margins[0] + Math.round(i * rowHeight), // x1, y1
                                        opt.width - opt.margins[1] + props.extra[1], opt.margins[0] + Math.round((i + 1) * rowHeight) // x2, y2
                                    ] ], attr : i % 2 == 0 ? props.evenHProps : props.oddHProps });
                                else
                                    bandsH.push({ path : false, attr: false})
                            }
                        }

                    for (i = 0; i < nx + 1; i++) {
                        if (
                            forceBorderX1 && i == 0 || // Always show first line if forced
                                forceBorderX2 && i == nx || // Always show last line if forced
                                drawV && ( // To show other lines draw must be true
                                    (props.nx != 'auto' && i > 0 && i < nx) || // If nx = [number] show other lines (first and last are managed above with forceBorder)
                                        (props.nx == 'auto' && (typeof labels[i] != 'boolean' || labels[i])) // if nx = 'auto' show all lines if a label is associated
                                    )
                        // Show all lines if props.nx is a number, or if label != false, AND draw must be true
                            ) {
                            path.push(["M", opt.margins[3] + Math.round(i * columnWidth), opt.margins[0] - props.extra[0] ]); //(t ? props.extra[0] : 0)]);
                            path.push(["L", opt.margins[3] + Math.round(i * columnWidth), opt.height - opt.margins[2] + props.extra[2] ]); //(t ? props.extra[2] : 0)]);
                        }
                        if (i < nx) {
                            if (i % 2 == 0 && props.evenVProps || i % 2 == 1 && props.oddVProps)
                                bandsV.push({path : [ [ 'RECT',
                                    opt.margins[3] + Math.round(i * columnWidth), opt.margins[0] - props.extra[0], // x1, y1
                                    opt.margins[3] + Math.round((i + 1) * columnWidth), opt.height - opt.margins[2] + props.extra[2], // x2, y2
                                ] ], attr : i % 2 == 0 ? props.evenVProps : props.oddVProps });
                            else
                                bandsV.push({ path : false, attr: false})
                        }
                    }

                    pieces.push({ section : 'Grid', path : path.length ? path : false, attr : path.length ? props.props : false });
                    pieces.push({ section : 'GridBandH', paths : bandsH });
                    pieces.push({ section : 'GridBandV', paths : bandsV });

                    var tpath = [];

                    // Ticks asse X
                    if (props.ticks.active && (typeof props.ticks.active != 'object' || props.ticks.active[0])) {
                        for (i = 0; i < nx + 1; i++) {
                            if (props.nx != 'auto' || typeof labels[i] != 'boolean' || labels[i]) {
                                tpath.push(["M", opt.margins[3] + Math.round(i * columnWidth), opt.height - opt.margins[2] - props.ticks.size[1] ]);
                                tpath.push(["L", opt.margins[3] + Math.round(i * columnWidth), opt.height - opt.margins[2] + props.ticks.size[0] ]);
                            }
                        }
                    }
                    // Ticks asse L
                    if (props.ticks.active && (typeof props.ticks.active != 'object' || props.ticks.active[1]))
                        for (i = 0; i < ny + 1; i++) {
                            tpath.push(["M", opt.margins[3] - props.ticks.size[0], opt.margins[0] + Math.round(i * rowHeight) ]);
                            tpath.push(["L", opt.margins[3] + props.ticks.size[1], opt.margins[0] + Math.round(i * rowHeight)]);
                        }
                    // Ticks asse R
                    if (props.ticks.active && (typeof props.ticks.active != 'object' || props.ticks.active[2]))
                        for (i = 0; i < ny + 1; i++) {
                            tpath.push(["M", opt.width - opt.margins[1] - props.ticks.size[1], opt.margins[0] + Math.round(i * rowHeight) ]);
                            tpath.push(["L", opt.width - opt.margins[1] + props.ticks.size[0], opt.margins[0] + Math.round(i * rowHeight)]);
                        }

                    pieces.push({ section : 'Ticks', path : tpath.length ? tpath : false, attr : tpath.length ? props.ticks.props : false });
                }
            }
        }
    }

})(jQuery);
/********* Source File: src/elycharts_chart_pie.js*********/
/**********************************************************************
 * ELYCHARTS
 * A Javascript library to generate interactive charts with vectorial graphics.
 *
 * Copyright (c) 2010 Void Labs s.n.c. (http://void.it)
 * Licensed under the MIT (http://creativecommons.org/licenses/MIT/) license.
 **********************************************************************/

(function($) {

    var featuresmanager = $.elycharts.featuresmanager;
    var common = $.elycharts.common;

    /***********************************************************************
     * CHART: PIE
     **********************************************************************/

    $.elycharts.pie = {
        init : function($env) {
        },

        draw : function(env) {
            //var paper = env.paper;
            var opt = env.opt;

            var w = env.opt.width;
            var h = env.opt.height;
            var r = env.opt.r ? env.opt.r : Math.floor((w < h ? w : h) / 2.5);
            var cx = env.opt.cx ? env.opt.cx : Math.floor(w / 2);
            var cy = env.opt.cy ? env.opt.cx : Math.floor(h / 2);

            var cnt = 0, i, ii, serie, plot, props;
            for (serie in opt.values) {
                plot = {
                    visible : false,
                    total : 0,
                    values : []
                };
                env.plots[serie] = plot;
                var serieProps = common.areaProps(env, 'Series', serie);
                if (serieProps.visible) {
                    plot.visible = true;
                    cnt ++;
                    plot.values = opt.values[serie];
                    for (i = 0, ii = plot.values.length; i < ii; i++)
                        if (plot.values[i] > 0) {
                            props = common.areaProps(env, 'Series', serie, i);
                            if (typeof props.inside == 'undefined' || props.inside < 0)
                                plot.total += plot.values[i];
                        }
                    for (i = 0; i < ii; i++)
                        if (plot.values[i] < plot.total * opt.valueThresold) {
                            plot.total = plot.total - plot.values[i];
                            plot.values[i] = 0;
                        }
                }
            }

            var rstep = r / cnt;
            var rstart = -rstep, rend = 0;

            var pieces = [];
            for (serie in opt.values) {
                plot = env.plots[serie];
                var paths = [];
                if (plot.visible) {
                    rstart += rstep;
                    rend += rstep;
                    var angle = env.opt.startAngle, angleplus = 0, anglelimit = 0;

                    if (plot.total == 0) {
                        env.emptySeries = true;
                        props = common.areaProps(env, 'Series', 'empty');
                        paths.push({ path : [ [ 'CIRCLE', cx, cy, r ] ], attr : props.plotProps });

                    } else {
                        env.emptySeries = false;
                        for (i = 0, ii = plot.values.length; i < ii; i++) {
                            var value = plot.values[i];
                            if (value > 0) {
                                props = common.areaProps(env, 'Series', serie, i);
                                if (typeof props.inside == 'undefined' || props.inside < 0) {
                                    angle += anglelimit;
                                    angleplus = 360 * value / plot.total;
                                    anglelimit = angleplus;
                                } else {
                                    angleplus = 360 * values[props.inside] / plot.total * value / values[props.inside];
                                }
                                var rrstart = rstart, rrend = rend;
                                if (props.r) {
                                    if (props.r > 0) {
                                        if (props.r <= 1)
                                            rrend = rstart + rstep * props.r;
                                        else
                                            rrend = rstart + props.r;
                                    } else {
                                        if (props.r >= -1)
                                            rrstart = rstart + rstep * (-props.r);
                                        else
                                            rrstart = rstart - props.r;
                                    }
                                }

                                if (!env.opt.clockwise)
                                    paths.push({ path : [ [ 'SLICE', cx, cy, rrend, rrstart, angle, angle + angleplus ] ], attr : props.plotProps });
                                else
                                    paths.push({ path : [ [ 'SLICE', cx, cy, rrend, rrstart, - angle - angleplus, - angle ] ], attr : props.plotProps });
                            } else
                                paths.push({ path : false, attr : false });
                        }
                    }
                } else {
                    // Even if serie is not visible it's better to put some empty path (for better transitions). It's not mandatory, just better
                    if (opt.values[serie] && opt.values[serie].length)
                        for (i = 0, ii = opt.values[serie].length; i < ii; i++)
                            paths.push({ path : false, attr : false });
                }

                pieces.push({ section : 'Series', serie : serie, subSection : 'Plot', paths : paths , mousearea : 'paths'});
            }

            featuresmanager.beforeShow(env, pieces);
            common.show(env, pieces);
            featuresmanager.afterShow(env, pieces);
            return pieces;
        }
    }

})(jQuery);
//Analytics
var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-28460379-1']);
_gaq.push(['_trackPageview']);

(function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['container'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, foundHelper, functionType="function", escapeExpression=this.escapeExpression;


  buffer += "<div class=\"istac-widget-title\">\r\n    <a class=\"istac-widget-title-text\" href=\"#\" target=\"_blank\"></a>\r\n</div>\r\n<div class=\"istac-widget-body\">\r\n    <div class=\"istac-widget-content\">\r\n    </div>\r\n    <div class=\"istac-widget-body-embed\">\r\n        <textarea rows=\"10\"></textarea>\r\n        <a href=\"#\" class=\"hideEmbed\">Cerrar</a>\r\n    </div>\r\n    <div class=\"istac-widget-body-allIndicators\">\r\n        <p class=\"istac-widget-embed\"><a href=\"#\" title=\"Incrustar widget\">&lt;/&gt;</a></p>\r\n        <span class=\"istac-widget-body-allIndicators-text\">\r\n            <img src=\"http://www.gobiernodecanarias.org/opencms8/export/system/modules/es.gobcan.istac.web/resources/images/maslight.gif\"><a target=\"_blank\" href=\"#\">más indicadores</a>\r\n        </span>\r\n    </div>\r\n    <div class=\"istac-widget-footer\">\r\n        <p class=\"istac-widget-credits\"><a target=\"_blank\" href=\"";
  foundHelper = helpers.widgetsTypeUrl;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.widgetsTypeUrl; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">widget facilitado por ISTAC</a></p>\r\n    </div>\r\n</div>\r\n";
  return buffer;});

templates['embed'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  


  return "<div id=\"istac-widget\"></div>\r\n";});

templates['last-data-lateral'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, functionType="function", escapeExpression=this.escapeExpression, self=this;

function program1(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n        <div class=\"istac-widget-lateral-dataset\">\r\n            <div class=\"istac-widget-lateral-title\"><a href=\"";
  foundHelper = helpers.jaxiUrl;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.jaxiUrl; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">";
  foundHelper = helpers.title;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.title; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</a></div>\r\n            ";
  stack1 = depth0.observations;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(2, program2, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n        </div>\r\n    ";
  return buffer;}
function program2(depth0,data) {
  
  var buffer = "", stack1;
  buffer += "\r\n                ";
  stack1 = depth0.hasValue;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.programWithDepth(program3, data, depth0)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n            ";
  return buffer;}
function program3(depth0,data,depth1) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n                    <div class=\"istac-widget-lateral-subtitle\">";
  stack1 = depth1.temporalLabel;
  stack1 = typeof stack1 === functionType ? stack1() : stack1;
  buffer += escapeExpression(stack1) + " - ";
  foundHelper = helpers.measure;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.measure; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + " (";
  foundHelper = helpers.unit;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.unit; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + ")</div>\r\n                    <div class=\"istac-widget-lateral-value\">";
  stack1 = depth0.showSparkline;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(4, program4, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += " ";
  foundHelper = helpers.value;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.value; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</div>\r\n                ";
  return buffer;}
function program4(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "<span class=\"inlinesparkline\" data-time=\"";
  foundHelper = helpers.timeTitles;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.timeTitles; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\" data-unit=\"";
  foundHelper = helpers.unit;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.unit; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">";
  foundHelper = helpers.values;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.values; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</span>";
  return buffer;}

  buffer += "<div class=\"istac-widget-lateral\">\r\n    ";
  stack1 = depth0.datasets;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(1, program1, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n</div>";
  return buffer;});

templates['last-data-table'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, functionType="function", escapeExpression=this.escapeExpression, self=this;

function program1(depth0,data) {
  
  var buffer = "";
  buffer += "\r\n                <th>";
  depth0 = typeof depth0 === functionType ? depth0() : depth0;
  buffer += escapeExpression(depth0) + "</th>\r\n            ";
  return buffer;}

function program3(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n            <tr data-tooltip=\"";
  foundHelper = helpers.description;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.description; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\" class=\"";
  foundHelper = helpers.cssClass;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.cssClass; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">\r\n                <td class=\"istac-widget-last-data-indicator-column\">\r\n                    <div class=\"istac-widget-lastData-title\"><a href=\"";
  foundHelper = helpers.titleLink;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.titleLink; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">";
  foundHelper = helpers.title;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.title; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</a></div>\r\n                    <div class=\"istac-widget-unit\">";
  foundHelper = helpers.temporalLabel;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.temporalLabel; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</div>\r\n                </td>\r\n                ";
  stack1 = depth0.observations;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(4, program4, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n            </tr>\r\n        ";
  return buffer;}
function program4(depth0,data) {
  
  var buffer = "", stack1;
  buffer += "\r\n                <td>\r\n                    ";
  stack1 = depth0.value;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.program(11, program11, data),fn:self.program(5, program5, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n                </td>\r\n                ";
  return buffer;}
function program5(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n                        <div>\r\n                            ";
  stack1 = depth0.showSparkline;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.program(8, program8, data),fn:self.program(6, program6, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n                            <span class=\"istac-widget-observation\">";
  foundHelper = helpers.value;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.value; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + " </span>\r\n                        </div>\r\n                        <div class=\"istac-widget-unit\"> ";
  foundHelper = helpers.unit;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.unit; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</div>\r\n                    ";
  return buffer;}
function program6(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n                                <span class=\"inlinesparkline\" data-time=\"";
  foundHelper = helpers.timeTitles;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.timeTitles; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\" data-unit=\"";
  foundHelper = helpers.unit;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.unit; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">";
  foundHelper = helpers.values;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.values; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</span>\r\n                            ";
  return buffer;}

function program8(depth0,data) {
  
  var buffer = "", stack1;
  buffer += "\r\n                                ";
  stack1 = depth0.anySparkline;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(9, program9, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n                            ";
  return buffer;}
function program9(depth0,data) {
  
  
  return "\r\n                                    <span class=\"inlinesparkline\"></span>\r\n                                ";}

function program11(depth0,data) {
  
  
  return "\r\n                        -\r\n                    ";}

  buffer += "<table>\r\n    <thead>\r\n        <tr>\r\n            <th class=\"istac-widget-last-data-indicator-column istac-widget-lastData-title\">Indicador</th>\r\n            ";
  stack1 = depth0.measures;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(1, program1, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n        </tr>\r\n    </thead>\r\n    <tbody>\r\n        ";
  stack1 = depth0.datasets;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(3, program3, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n    </tbody>\r\n</table>";
  return buffer;});

templates['uwaContainer'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, foundHelper, functionType="function", escapeExpression=this.escapeExpression;


  buffer += "<div class=\"istac-widget-uwa-body\">\r\n    <div class=\"istac-widget-content\">\r\n    </div>\r\n    <div class=\"istac-widget-body-embed\">\r\n        <textarea rows=\"10\"></textarea>\r\n        <a href=\"#\" class=\"hideEmbed\">Cerrar</a>\r\n    </div>\r\n    <div class=\"istac-widget-footer\">\r\n        <p class=\"istac-widget-embed\"><a href=\"#\" title=\"Incrustar widget\">&lt;/&gt;</a></p>\r\n        <p class=\"istac-widget-credits\"><a href=\"";
  foundHelper = helpers.widgetsTypeUrl;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.widgetsTypeUrl; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">widget facilitado por ISTAC</a></p>\r\n    </div>\r\n</div>";
  return buffer;});
})();
window.Istac = {
    widget : {

    }
};

(function ($) {

    Istac.widget.helper = {};

    Istac.widget.helper.tooltip = function ($el, text) {
        var $tooltip = $(".istact-widget-tooltip");

        // Lazy init
        if ($tooltip.length === 0) {
            $tooltip = $('<p class="istact-widget-tooltip"></p>');
            $("body").append($tooltip);
        }

        var xOffset = 10;
        var yOffset = 20;

        if (text) {
            $tooltip.text(text);

            var fnHoverIn = function (e) {
                $tooltip
                    .css("top", (e.pageY - xOffset) + "px")
                    .css("left", (e.pageX + yOffset) + "px")
                    .fadeIn("fast");
            };
            var fnHoverOut = function () {
                $tooltip.fadeOut("fast");
            };

            $el.hover(fnHoverIn, fnHoverOut);
            $el.mousemove(function (e) {
                $tooltip
                    .css("top", (e.pageY - xOffset) + "px")
                    .css("left", (e.pageX + yOffset) + "px");
            });
        }
    };

    Istac.widget.helper.addThousandSeparator = function (nStr) {
        nStr += '';
        var x = nStr.split(',');
        var x1 = x[0];
        var x2 = x.length > 1 ? ',' + x[1] : '';
        var rgx = /(\d+)(\d{3})/;
        while (rgx.test(x1)) {
            x1 = x1.replace(rgx, '$1' + '.' + '$2');
        }
        return x1 + x2;
    };

    Istac.widget.helper.getHost = function (url) {
        var l = document.createElement("a");
        l.href = url;
        return l.host;
    };

    Istac.widget.helper.absoluteUrl = function (url) {
        var a = document.createElement('a');
        a.href = url;
        return a.href;
    };

    Istac.widget.helper.getKeys = function (hash) {
        var keys = [];
        for (var i in hash) {
            if (hash.hasOwnProperty(i)) {
                keys.push(i);
            }
        }
        return keys;
    };

    Istac.widget.helper.firstKey = function (hash) {
        var key;
        for (var i in hash) {
            if (hash.hasOwnProperty(i)) {
                key = i;
                break;
            }
        }
        return key;
    };

    Istac.widget.helper.firstValue = function (hash) {
        var key = this.firstKey(hash);
        var result;
        if (key) {
            result = hash[key];
        }
        return result;
    }

    var colorHueDistributor = function (n) {
        var result = [0];
        var total = 400;
        var divisions = 3;
        var step = total / divisions;
        var offset = 0;

        for(var i = 1; i < n; i ++) {
            if ((i != 0) && ((i * step) % total === 0)) {
                offset = step / 2;
            }
            result[i] = (result[i - 1] + step + offset) % total;
            if ((i != 0) && ((i * step) % total === 0)) {
                step = step / 2;
            }
        }

        result = _.map(result, function (color) {
            return color / total;
        });

        return result;
    };

    var hsvToRgb = function(h, s, v){
        var r, g, b;

        var i = Math.floor(h * 6);
        var f = h * 6 - i;
        var p = v * (1 - s);
        var q = v * (1 - f * s);
        var t = v * (1 - (1 - f) * s);

        switch(i % 6){
            case 0: r = v, g = t, b = p; break;
            case 1: r = q, g = v, b = p; break;
            case 2: r = p, g = v, b = t; break;
            case 3: r = p, g = q, b = v; break;
            case 4: r = t, g = p, b = v; break;
            case 5: r = v, g = p, b = q; break;
        }

        return [r * 255, g * 255, b * 255];
    };

    var componentToHex = function (c) {
        var hex = c.toString(16);
        return hex.length == 1 ? "0" + hex : hex;
    };

    var rgbToHex = function (r, g, b) {
        return "#" + componentToHex(r) + componentToHex(g) + componentToHex(b);
    };

    Istac.widget.helper.colorPaletteGenerator = function (n) {
        var hues = colorHueDistributor(n);
        return _.map(hues, function (hue) {
            var rgb = hsvToRgb(hue, 0.84, 0.84);
            rgb = _.map(rgb, function (component) {
                return Math.round(component);
            });
            return rgbToHex.apply(null, rgb);
        });
    }

}(jQuery));
(function ($) {

    var Dataset = function () {
        this.data = {};
        this.metadata = {};
    };

    Dataset.fromRequest = function (request) {
        var dataset = new Dataset();
        dataset.request = request;
        dataset.metadata = request.metadata;
        dataset.data = request.data;
        return dataset;
    };

    Dataset.prototype = {

        getObservationIndex : function (geo, time, measure) {
            var data = this.data;

            var geoIdIndex = $.inArray("GEOGRAPHICAL", data.format);
            var timeIdIndex = $.inArray("TIME", data.format);
            var measureIdIndex = $.inArray("MEASURE", data.format);

            var geoIndex = data.dimension.GEOGRAPHICAL.representation.index[geo];
            var timeIndex = data.dimension.TIME.representation.index[time];
            var measureIndex = data.dimension.MEASURE.representation.index[measure];

            //Calculo del multiplicador
            var m = 1;
            var mult = [];
            var dimensionsOrder = data.format;
            for (var i = 0; i < dimensionsOrder.length; i++) {
                if (i > 0) {
                    var dimension = dimensionsOrder[dimensionsOrder.length - i];
                    m *= data.dimension[dimension].representation.size;
                }
                mult.unshift(m);
            }

            var res = mult[geoIdIndex] * geoIndex +
                mult[timeIdIndex] * timeIndex +
                mult[measureIdIndex] * measureIndex;
            return res;
        },

        _observationToNumber : function (observation) {
            if (this._isValidObservation(observation)) {
                return parseFloat(observation);
            }
        },

        getObservation : function (geo, time, measure) {
            if (this.data.observation) {
                var index = this.getObservationIndex(geo, time, measure);
                var observation = this.data.observation[index];
                var res = this._observationToNumber(observation);
                return res;
            }
            return null;
        },

        getUnit : function (measure) {
            var result = "";

            if (this.metadata) {
                var measureRepresentation = _.find(this.metadata.dimension.MEASURE.representation, function (representation) {return representation.code === measure;});
                if (measureRepresentation) {
                    var quantity = measureRepresentation.quantity;

                    if (!_.isUndefined(quantity.unitMultiplier)) {
                        var unitMultiplierTitle = quantity.unitMultiplier.__default__;
                        if (unitMultiplierTitle !== "Unidades") {
                            result = result + unitMultiplierTitle + " de ";
                        }
                    }

                    if (quantity.unitSymbol) {
                        result = result + quantity.unitSymbol;
                    } else {
                        result = result + quantity.unit.__default__;
                    }
                }
            }
            return result;
        },

        _isValidObservation : function (observation) {
            var result = (!_.isUndefined(observation) && !_.isNull(observation) && observation !== ".");
            return result;
        },

        getObservationStr : function (geo, time, measure) {
            var res = null;
            if (this.data.observation) {
                var index = this.getObservationIndex(geo, time, measure);
                var observation = this.data.observation[index];

                if (this._isValidObservation(observation)) {
                    res = observation;
                    res = res.replace("\.", ",");
                    res = Istac.widget.helper.addThousandSeparator(res);
                }
            }
            return res;
        },

        getObservationsByGeoAndMeasure : function (geo, measure) {
            var timeValues = this.getTimeValues();
            var result = _.map(timeValues, function (time) {
                return this.getObservation(geo, time, measure)
            }, this);
            return result;
        },

        _getTitles : function (data, locale) {
            var result = {};
            if (data) {
                for (var i = 0; i < data.length; i++) {
                    var valor = data[i];
                    if (valor.title[locale]) {
                        result[valor.code] = valor.title[locale];
                    } else {
                        result[valor.code] = valor.title.__default__;
                    }
                }
            }
            return result;
        },

        getDescription : function (locale) {
            return this._getLabel(this.request.conceptDescription, locale);
        },

        getTimeValues : function () {
            var timeValues = [];
            if (this.data.dimension.TIME.representation.index) {
                timeValues = _.chain(this.data.dimension.TIME.representation.index)
                    .map(function (value, key) {
                        return {key : key, value : value};
                    }).sortBy(function (dimension) {
                        return dimension.value;
                    }).map(function (dimension) {
                        return dimension.key;
                    }).value().reverse();
            }
            return timeValues;
        },

        getTimeValuesTitles : function (locale) {
            if (this.metadata.dimension) {
                var titles = this._getTitles(this.metadata.dimension.TIME.representation, locale);
                return titles;
            } else {
                return [];
            }
        },

        getGeographicalValues : function () {
            if (this.data.dimension) {
                return Istac.widget.helper.getKeys(this.data.dimension.GEOGRAPHICAL.representation.index);
            }
            return [];
        },

        getGeographicalValuesTitles : function (locale) {
            if (this.metadata.dimension) {
                return this._getTitles(this.metadata.dimension.GEOGRAPHICAL.representation, locale);
            } else {
                return [];
            }
        },

        getLastTimeValue : function () {
            var timeValues = this.getTimeValues();
            if (timeValues.length > 0) {
                return timeValues[timeValues.length - 1];
            } else {
                return null;
            }
        },

        _getLabel : function (iString, locale) {
            if (iString) {
                return iString[locale] || iString["__default__"];
            }
        },

        getTitle : function (locale) {
            return this._getLabel(this.request.title, locale);
        },

        _emptyIsUndefined : function (value) {
            return value || "";
        }

    };

    Istac.widget.Dataset = Dataset;

}(jQuery));
(function ($) {

    var DatasetRequestBuilder = function (options) {
        this.apiUrl = options.apiUrl;
    };

    DatasetRequestBuilder.prototype = {

        _toInParameters : function (list) {
            var stringItems = _.map(list,function (item) {
                return '"' + item + '"';
            }).join(", ");
            return "(" + stringItems + ")";
        },

        _eqOrIn : function (arg) {
            if (arg.length > 1) {
                return "IN " + this._toInParameters(arg);
            } else {
                return 'EQ "' + arg[0] + '"';
            }
        },

        _validateDefined : function (arg) {
            if (_.isUndefined(arg)) {
                throw new Error("undefined");
            }
        },

        _validateOneOrMore : function (arg) {
            if (_.isUndefined(arg) || !_.isArray(arg) || arg.length === 0) {
                throw new Error("array one or more");
            }
        },

        _validateOne : function (arg) {
            this._validateDefined(arg);
            if (!_.isArray(arg) || arg.length !== 1) {
                throw new Error("array one");
            }
        },

        _fieldsParameter : function () {
            return "&fields=%2Bdata,%2Bmetadata";
        },

        _representation : function (options) {
            this._validateOneOrMore(options.geographicalValues);
            var geographicalRepresentation = options.geographicalValues.join("|");
            return "&representation=GEOGRAPHICAL[" + geographicalRepresentation + "]";
        },

        _selectedInstancesRequest : function (options) {
            this._validateDefined(options.indicatorSystem);
            this._validateOneOrMore(options.instances);

            return this.apiUrl + "/indicatorsSystems/" +
                options.indicatorSystem +
                "/indicatorsInstances/?q=id "
                + this._eqOrIn(options.instances)
                + this._fieldsParameter()
                + this._representation(options);

        },

        _selectedIndicatorsRequest : function (options) {
            this._validateOneOrMore(options.indicators);

            return this.apiUrl + "/indicators/?q=id "
                + this._eqOrIn(options.indicators)
                + this._fieldsParameter()
                + this._representation(options);
        },

        _recentInstancesRequest : function (options) {
            this._validateDefined(options.nrecent);
            this._validateDefined(options.indicatorSystem);
            this._validateOne(options.geographicalValues);

            var geographicalValue = options.geographicalValues[0];
            return this.apiUrl + "/indicatorsSystems/"
                + options.indicatorSystem
                + '/indicatorsInstances/?q=geographicalValue EQ "'
                + geographicalValue
                + '"&order=update DESC, id DESC&limit='
                + options.nrecent
                + this._fieldsParameter()
                + this._representation(options);
        },

        _recentIndicatorsRequest : function (options) {
            this._validateDefined(options.nrecent);
            this._validateDefined(options.subjectCode);
            this._validateOne(options.geographicalValues);

            var geographicalValue = options.geographicalValues[0];
            return this.apiUrl + '/indicators/?q=subjectCode EQ "'
                + options.subjectCode
                + '" AND geographicalValue EQ "'
                + geographicalValue
                + '"&order=update DESC, id DESC&limit='
                + options.nrecent
                + this._fieldsParameter()
                + this._representation(options);
        },

        _temporalRequest : function (options) {
            this._validateDefined(options.indicatorSystem);
            this._validateOne(options.instances);

            return this.apiUrl + "/indicatorsSystems/"
                + options.indicatorSystem
                + '/indicatorsInstances/?q=id EQ "'
                + options.instances[0]
                + '"'
                + this._fieldsParameter()
                + this._representation(options);
        },

        request : function (options) {
            var result;
            try {
                if (options.type === 'lastData') {
                    if (options.groupType === 'system') {
                        result = this._selectedInstancesRequest(options);
                    } else if (options.groupType === 'subject') {
                        result = this._selectedIndicatorsRequest(options);
                    }
                } else if (options.type === 'recent') {
                    if (options.groupType === 'system') {
                        result = this._recentInstancesRequest(options);
                    } else if (options.groupType === 'subject') {
                        result = this._recentIndicatorsRequest(options);
                    }
                } else if (options.type === 'temporal') {
                    result = this._temporalRequest(options);
                }
            } catch (e) {
            }
            return result;
        }

    };

    Istac.widget.DatasetRequestBuilder = DatasetRequestBuilder;

}(jQuery));
(function ($) {

    var Dataset = Istac.widget.Dataset;
    var Datasets = Istac.widget.Datasets;
    var DatasetRequestBuilder = Istac.widget.DatasetRequestBuilder;

    Istac.widget.Base = function (options) {
        this.init(options);
    };

    Istac.widget.Base.prototype = {

        _defaultOptions : {
            title : 'default title',
            width : 300,
            height : 400,
            headerColor : '#0F5B95',
            borderColor : '#EBEBEB',
            textColor : '#000000',
            indicatorSystem : "",
            subjectCode : "",
            indicators : [],
            instances : [],
            groupType : 'system', // or subject
            measures : [
                "ABSOLUTE",
                "ANNUAL_PERCENTAGE_RATE",
                "ANNUAL_PUNTUAL_RATE",
                "INTERPERIOD_PERCENTAGE_RATE",
                "INTERPERIOD_PUNTUAL_RATE"
            ],
            showLabels : true,
            showLegend : true,
            sideView : false,
            sparkline_ABSOLUTE : false,
            sparkline_ANNUAL_PERCENTAGE_RATE : false,
            sparkline_ANNUAL_PUNTUAL_RATE : false,
            sparkline_INTERPERIOD_PERCENTAGE_RATE : false,
            sparkline_INTERPERIOD_PUNTUAL_RATE : false,
            sparklineType : 'line',
            sparklineWidth : 30,
            sparklineHeight : 20,
            sparklineLineColor : "#0058B0",
            sparklineFillColor : null,
            uwa : false,
            shadow : true,
            borderRadius : true,
            scale : "natural"
        },

        _containerTemplate : Handlebars.templates.container,

        _uwaContainerTemplate : Handlebars.templates.uwaContainer,

        init : function (options) {
            this.options = _.extend({}, this._defaultOptions, options);
            this.el = $(options.el);
            this.type = options.type;
            this.measures = options.measures || this._defaultOptions.measures;
            this.geographicalValues = options.geographicalValues;

            // urls
            this.url = options.url || "";
            this.apiUrl = this.url + "/api/indicators/v1.0";
            this.jaxiUrl = options.jaxiUrl || "";

            // Request builder
            this.datasetRequestBuilder = new DatasetRequestBuilder({apiUrl : this.apiUrl});

            // locale
            this.locale = options.locale || "es";

            this.datasets = [];

            //Create containers
            var templateOptions = { widgetsTypeUrl : Istac.widget.configuration['indicators.widgets.typelist.url'] };

            if (this.options.uwa) {
                this.el.html(this._uwaContainerTemplate(templateOptions));
                //this.titleLink = $('');
                this.titleContainer = $('');
            } else {
                this.el.html(this._containerTemplate(templateOptions));
                //this.titleLink = this.el.find('.istac-widget-title-text');
                this.titleContainer = this.el.find('.istac-widget-title');
            }

            this.bodyContainer = this.el.find('.istac-widget-body');
            this.contentContainer = this.el.find('.istac-widget-content');
            this.creditsContainer = this.el.find('.istac-widget-credits');
            this.embedContainer = this.el.find('.istac-widget-body-embed');
            this.allIndicatorsContainer = this.el.find('.istac-widget-body-allIndicators');

            this.el.addClass("istac-widget");
            this.el.addClass(this.containerClass);

            var includeLogo = this.options.uwa ? true : !this.isIstacDomain();
            this.includeLogo(includeLogo);
            this.initializeEmbed();

            // Initialize style

            var realWidth = this.options.uwa ? "100%" : options.width;

            this.set('textColor', options.textColor);
            this.set('borderColor', options.borderColor);
            this.set('headerColor', options.headerColor);
            this.set('title', options.title);
            this.set('width', realWidth);
            this.set('showLabels', options.showLabels);
            this.set('showLegend', options.showLegend);
            this.set('indicatorSystem', options.indicatorSystem);
            this.set('borderRadius', options.borderRadius);
            this.set('shadow', options.shadow);
            this.set('style', options.style);
            this.set('gobcanStyleColor', options.gobcanStyleColor);
            this.reloadData();
        },

        set : function (property, value) {
            this[property] = value;
            this.options[property] = value;

            var setter = this[this._getSetterMethodName(property)];
            if (_.isFunction(setter)) {
                setter.call(this, value);
            }
        },

        _getSetterMethodName : function (property) {
            var upper = property[0].toUpperCase() + property.substring(1);
            return 'set' + upper;
        },

        setTextColor : function (textColor) {
            this.el.css('color', textColor);
        },

        _getContrast50 : function (hexcolor) {
            if (hexcolor && hexcolor.length > 0) {
                if (hexcolor[0] === '#') {
                    hexcolor = hexcolor.substring(1);
                }
                return (parseInt(hexcolor, 16) > 0xffffff / 2) ? '#333' : 'white';
            }
        },

        setBorderColor : function (borderColor) {
            this.bodyContainer.css('border-color', borderColor);
        },

        _cssBorderRadius : function (el, radius) {
            el.css('border-radius', radius);
            el.css('-webkit-border-radius', radius);
            el.css('-moz-border-radius', radius);
        },

        setBorderRadius : function (enable) {
            if (enable) {
                this._cssBorderRadius(this.titleContainer, "4px 4px 0 0");
                this._cssBorderRadius(this.bodyContainer, "0 0 12px 12px");
            } else {
                this._cssBorderRadius(this.titleContainer, "0");
                this._cssBorderRadius(this.bodyContainer, "0");
            }
        },

        setShadow : function (enable) {
            if (enable) {
                this.bodyContainer.css('box-shadow', 'rgba(0, 0, 0, 0.18) 0px 3px 5px -2px');
            } else {
                this.bodyContainer.css('box-shadow', 'none');
            }
        },

        setHeaderColor : function (color) {
            this.titleContainer.css('background-color', color);
            var contrastColor = this._getContrast50(color);

            this.titleContainer.css('color', contrastColor);
            this.titleContainer.find("a").css('color', contrastColor);
        },

        setWidth : function (width) {
            this.width = width;
            this.el.css('width', width);
        },

        updateTitle : function () {
            this.setTitle(this.options.title);
        },

        _getDefaultTitle : function () {
            var title;
            if (this.options.type === "lastData") {
                title = "Últimos datos";

                if (this.datasets && this.datasets.length > 0) {
                    var geographicalValue = this.options.geographicalValues[0];
                    title += ". " + this.datasets[0].getGeographicalValuesTitles()[geographicalValue];
                }
            } else if (this.options.type === "recent") {
                title = "Últimos indicadores actualizados";

                if (this.datasets && this.datasets.length > 0) {
                    var geographicalValue = this.options.geographicalValues[0];
                    title += ". " + this.datasets[0].getGeographicalValuesTitles()[geographicalValue];
                }
            } else if (this.options.type === "temporal") {
                if (this.datasets && this.datasets.length > 0) {
                    title = this.datasets[0].getTitle();
                } else {
                    title = "Serie temporal";
                }
            }
            return title;
        },

        setTitle : function (title) {
            if (_.isUndefined(title) || title.length === 0) {
                title = this._getDefaultTitle();
            }

            var $link = this.titleContainer.find('a');
            if ($link.length > 0) {
                $link.text(title);
            } else {
                this.titleContainer.text(title);
            }

        },

        setIndicatorSystem : function () {
            this._updateLinks();
        },

        setGroupType : function () {
            this._updateLinks();
        },

        _updateLinks : function () {
            var systemId = this.options.indicatorSystem;
            var url;
            var hasSystemId = this.options.type === 'temporal' || (this.options.groupType === 'system');
            if (hasSystemId) {
                var systemIdStr = systemId || "";
                url = this.url + "/indicatorsSystems/" + systemIdStr;
            }


            if (url) {
                this.titleContainer.html('<a class="istac-widget-title-text" href="' + url + '" target="_blank"></a>');
                this.setTitle(this.title);
                this.setHeaderColor(this.headerColor);
            } else {
                this.titleContainer.html('');
                this.setTitle(this.title);
                this.setHeaderColor(this.headerColor);
            }


            this.allIndicatorsContainer.find('a').attr('href', url);
            this.el.find('.istac-widget-body-allIndicators-text').toggle(hasSystemId);
        },

        setStyle : function (style) {
            var isGobcan = style === "gobcan";
            this.el.toggleClass("gobcan-style", isGobcan);
            this.set('gobcanStyleColor', this.gobcanStyleColor);
            if (isGobcan) {
                this.set('textColor', '#000000');

                var width = this.options.sideView ? 151 : 423;
                this.set('width', width);
            }
        },

        setGobcanStyleColor : function (color) {
            if (this.options.style === "gobcan") {
                this.el.toggleClass("blue", color === "blue");
                this.el.toggleClass("lightBlue", color === "lightBlue");
                if (color === "blue") {
                    this.set('headerColor', "#0F5B95");
                } else if (color === "lightBlue") {
                    this.set('headerColor', "#C4D0DC");
                }
            }
        },

        isIstacDomain : function () {
            var result;
            var absoluteUrl = Istac.widget.helper.absoluteUrl(this.url);
            if (window.location.href.substring(0, absoluteUrl.length) === absoluteUrl) {
                // Si estoy mostrando el widget en la misma página dodne está la API
                // se muestra el footer para que el usuario lo vea igual que cuando
                // lo va a incrustar
                result = false;
            } else {
                result = window.location.host === Istac.widget.helper.getHost(absoluteUrl);
            }
            return result;
        },

        includeLogo : function (include) {
            this.creditsContainer.toggle(include);
        },

        initializeEmbed : function () {
            this.hideEmbed();
            var self = this;
            this.embedContainer.find('.hideEmbed').click(function () {
                self.hideEmbed();
                return false;
            });
            this.el.find('.istac-widget-embed a').click(function () {
                self.showEmbed();
                return false;
            });
        },

        openTag : function (tag, parameters) {
            var result = '<' + tag;

            _.each(parameters, function (value, key) {
                result += " " + key + "='" + value + "'";
            });

            result += '>';
            return result;
        },

        closeTag : function (tag) {
            return '</' + tag + '>';
        },

        showEmbed : function () {
            var filteredOptions = _.omit(this.options, "el");

            var closeScript = this.closeTag('script');
            var code = '<div id="istac-widget"></div>';

            code += this.openTag('script', {src : this.url + "/theme/js/widgets/widget.min.all.js" }) + closeScript;
            code += this.openTag('script') + "new IstacWidget(" + JSON.stringify(filteredOptions, null, 4) + ")" + closeScript;

            this.embedContainer.find('textarea').val(code);
            this.embedContainer.show();
        },

        hideEmbed : function () {
            this.embedContainer.hide();
        },

        reloadData : function () {
            var self = this;
            var requestUrl = this.datasetRequestBuilder.request(this.options);
            if (requestUrl) {
                var req = $.ajax({
                    url : requestUrl,
                    dataType : 'jsonp',
                    jsonp : "_callback"
                });
                req.success(function (response) {
                    var datasets = _.map(response.items, function (item) {
                        return Dataset.fromRequest(item);
                    });
                    self.datasets = datasets;
                    self.render();
                });
            } else {
                self.datasets = [];
                self.render();
            }
        }


    };

}(jQuery));
(function ($) {

    var measuresLabels = {
        'ABSOLUTE' : 'Dato',
        'ANNUAL_PERCENTAGE_RATE' : 'Tasa variación anual',
        'ANNUAL_PUNTUAL_RATE' : 'Variación anual',
        'INTERPERIOD_PERCENTAGE_RATE' : 'Tasa variación interperiódica',
        'INTERPERIOD_PUNTUAL_RATE' : 'Variación interperiódica'
    };

    Istac.widget.LastData = function (options) {
        this.init(options);
    };

    Istac.widget.LastData.prototype = _.extend(
        {},
        Istac.widget.Base.prototype,
        {
            _limitMaxSparkLine : function (arr) {
                return _.last(arr, Istac.widget.configuration["indicators.widgets.sparkline.max"]);
            },

            parseDataset : function (dataset) {
                var lastTimeValue = dataset.getLastTimeValue();
                var geographicalValue = this.geographicalValues[0];

                var self = this;
                var anySparkline = _.chain(this.measures).map(function (measure) {
                    return self.options['sparkline_' + measure];
                }).reduce(function (memo, current) {
                        return memo || current;
                    }, false).value();

                var observations = _.map(this.measures, function (measure) {
                    var values = dataset.getObservationsByGeoAndMeasure(geographicalValue, measure);
                    values = _.map(values, function (value) { // The library needs explicit null to draw a gap
                        return value === undefined ? "null" : value;
                    });

                    var value = dataset.getObservationStr(geographicalValue, lastTimeValue, measure);
                    var unit = dataset.getUnit(measure);

                    var showSparkline = this.options['sparkline_' + measure];

                    var timeValues = dataset.getTimeValues();
                    var timeValuesTitles = dataset.getTimeValuesTitles();

                    var timeTitles = _.map(timeValues, function (timeValue) {
                        return timeValuesTitles[timeValue];
                    });

                    values = this._limitMaxSparkLine(values);
                    timeTitles = this._limitMaxSparkLine(timeTitles);

                    return {
                        anySparkline : anySparkline,
                        showSparkline : showSparkline,
                        values : values.join(','),
                        value : value,
                        unit : unit,
                        timeTitles : timeTitles.join(','),
                        measure : measuresLabels[measure],
                        hasValue : value !== null && value !== "-"
                    };
                }, this);

                var temporalLabel = dataset.getTimeValuesTitles(this.locale)[lastTimeValue];

                var jaxiBaseUrl = Istac.widget.configuration['indicators.jaxi.url'];

                var jaxiUrl = jaxiBaseUrl;
                if (this.options.groupType === 'system') {
                    jaxiUrl += '/tabla.do?instanciaIndicador=' + dataset.request.id + '&sistemaIndicadores=' + this.indicatorSystem;
                } else {
                    jaxiUrl += '/tabla.do?indicador=' + dataset.request.id;
                }

                jaxiUrl += '&accion=html';
                jaxiUrl += '&measure=' + this.measures.join(",");
                jaxiUrl += "&geo=" + geographicalValue;

                return {
                    title : dataset.getTitle(this.locale),
                    titleLink : jaxiUrl,
                    temporalLabel : temporalLabel,
                    observations : observations,
                    description : dataset.getDescription(this.locale)
                };
            },

            render : function () {
                this.renderTable(this.datasets);
                this.updateTitle();
            },

            addTooltips : function ($el) {
                $el.find('[data-tooltip]').each(function (i, row) {
                    var $row = $(row);
                    Istac.widget.helper.tooltip($row, $row.data('tooltip'));
                });
            },

            addSparklines : function ($el) {
                if (this.options.showSparkline) {
                    var self = this;
                    var sparklineOptions = {
                        type : this.options.sparklineType,
                        width : this.options.sparklineWidth + "px",
                        height : this.options.sparklineHeight + "px",
                        lineColor : this.options.headerColor,
                        fillColor : this.options.sparklineFillColor,
                        lineWidth : 1.5,
                        highlightLineColor : null,
                        spotRadius : 0,
                        tooltipFormatter : function (sparkline, options, fields) {
                            if (fields.y !== null) {
                                var time = sparkline.$el.data('time');
                                var timeTitle = time.split(",")[fields.x];
                                var unit = sparkline.$el.data('unit');
                                var value = Istac.widget.helper.addThousandSeparator(fields.y);
                                return "<strong>" + value + " " + unit + "</strong><br>" + timeTitle;
                            }
                            return "";
                        }
                    };

                    $el.find('.inlinesparkline').sparkline('html', sparklineOptions);
                }
            },

            renderTable : function (datasets) {
                var context = {};
                context.measures = _.map(this.measures, function (measure) {
                    return measuresLabels[measure];
                });
                context.datasets = _.map(datasets, this.parseDataset, this);

                _.each(context.datasets, function (dataset, i) {
                    dataset.isOdd = i % 2 === 1;
                    dataset.cssClass = dataset.isOdd ? "odd" : "even";
                });


                this.el.toggleClass("istac-widget-lateral", this.options.sideView);
                this.el.toggleClass("istac-widget-lastData", !this.options.sideView);
                var template = this.options.sideView ? Handlebars.templates['last-data-lateral'] : Handlebars.templates['last-data-table'];

                var $el = $(template(context));
                this.contentContainer.html($el);

                this.addTooltips($el);
                this.addSparklines($el);
            }

        }
    );

}(jQuery));

(function () {

    Istac.widget.NaturalScale = {
        scale : function (options) {
            var ymax = options.ymax;
            var ymin = options.ymin;

            var rango = Math.abs(ymax - ymin);
            var n = 0;
            while (Math.pow(10, n) < rango) {
                n = n + 1;
            }

            var m;
            if (rango < 3 * Math.pow(10, n - 1)) {
                m = Math.pow(10, n - 2) * 5;
            } else if (rango < 5 * Math.pow(10, n - 1)) {
                m = Math.pow(10, n - 1);
            } else {
                m = 5 * Math.pow(10, n - 1) / 2;
            }

            var ydown;
            if (ymin >= 0) {
                ydown = Math.floor(ymin / m) * m;
            } else {
                ydown = (Math.ceil(ymin / m) - 1) * m;
            }

            var ytop;
            if (ymax > 0) {
                ytop = (Math.floor(ymax / m) + 1) * m;
            } else {
                ytop = Math.ceil(ymax / m) * m;
            }

            if (ydown === ymin && ymin !== 0) {
                ydown = ymin - 1;
            }

            if (ytop === ymax && ymax !== 0) {
                ytop = ymax - 1;
            }

            var ranges = Math.abs(ytop - ydown) / m;
            return {ytop : ytop, ydown : ydown, ranges : ranges};
        }
    };

}());
(function ($) {
    Istac.widget.Temporal = function (options) {
        this.init(options);
    };

    Istac.widget.Temporal.prototype = _.extend({}, Istac.widget.Base.prototype, {

        parse : function (dataset) {
            var self = this;

            var locale = this.locale;

            var measureValue = this.measures[0];
            var timeValues = dataset.getTimeValues();
            var timeValuesTitles = dataset.getTimeValuesTitles(locale);
            var geographicalValues = this.geographicalValues;
            var geographicalValuesTitles = dataset.getGeographicalValuesTitles(locale);

            var legend = {};
            var values = {};
            var tooltips = {};
            for (var i = 0; i < geographicalValues.length; i++) {
                var serie = 'serie' + i;
                var geoValue = geographicalValues[i];
                var geoValueTitle = geographicalValuesTitles[geoValue];

                var data = [];
                var tooltip = [];
                for (var j = 0; j < timeValues.length; j++) {
                    var timeValue = timeValues[j];
                    var value = dataset.getObservation(geoValue, timeValue, measureValue);
                    var valueStr = dataset.getObservationStr(geoValue, timeValue, measureValue);
                    var unit = dataset.getUnit(measureValue);

                    data.push(value);
                    tooltip.push('<div><strong>' + valueStr + ' ' + unit + '</strong></div><div>' + geoValueTitle + '</div><div>' + timeValuesTitles[timeValue] + '</div>');
                }

                legend[serie] = geoValueTitle;
                values[serie] = data;
                tooltips[serie] = tooltip;
            }

            //limit the number of labels (eje x)
            var totalLabels = 5;
            var division = Math.floor(timeValues.length / totalLabels);
            var labels = [];
            for (var i = 0; i < timeValues.length; i++) {
                if (i % division === 0) {
                    var timeValue = timeValues[i];
                    labels.push(timeValuesTitles[timeValue]);
                } else {
                    labels.push('');
                }
            }

            var isNumber = function (n) { return _.isNumber(n) };

            var maxValue = _.chain(values).values().flatten().filter(isNumber).max().value();
            var minValue = _.chain(values).values().flatten().filter(isNumber).min().value();

            var chartData = {
                labels : labels,
                values : values,
                legend : legend,
                tooltips : tooltips,
                maxValue : maxValue,
                minValue : minValue
            };
            return chartData;
        },


        _getIndicatorInstanceCode : function () {
            var instances = this.options.instances;
            if (instances && instances.length > 0) {
                return instances[0];
            }
        },

        render : function () {
            this.el.addClass('istac-widget-temporal');
            if (this.datasets && this.datasets.length > 0) {
                var chartData = this.parse(this.datasets[0]);
                this.renderChart(chartData);
            }
            this.updateTitle();
        },

        chartColors : function (chartData) {
            var nSeries = _.keys(chartData.values).length;
            var colors = Istac.widget.helper.colorPaletteGenerator(nSeries);

            var chartColors = {};
            _.each(colors, function (color, i) {
                chartColors["serie" + i] = { color : color};
            });

            return chartColors;
        },

        renderChart : function (chartData) {
            var $chartContainer = $('<div id="chart"></div>');
            $chartContainer.css('width', this.width - 20);
            $chartContainer.css('height', this.height);
            this.contentContainer.html($chartContainer);

            var renderData = chartData;
            var colors = this.chartColors(chartData);
            var legendElements = Istac.widget.helper.getKeys(chartData.legend);

            var chartWidth = this.width - 20;
            var chartHeight = 200;

            var legendWidth = this.width - 20;
            var legendHeight = this.showLegend ? legendElements.length * 20 : 0;

            var chartTopMargin = 20;
            var labelHeight = this.showLabels ? 40 : 10;
            var legendTop = chartTopMargin + chartHeight + labelHeight;

            var chartBottomMargin = legendHeight + labelHeight;
            var containerHeight = chartTopMargin + chartHeight + chartBottomMargin;

            var chartOptions = {
                type : "line",
                margins : [chartTopMargin, 1, chartBottomMargin, 65],
                defaultSeries : {
                    plotProps : {
                        "stroke-width" : 1.5
                    },
                    dot : true,
                    rounded : false,
                    stacked : false,
                    dotProps : {
                        r : 0,
                        stroke : "white",
                        "stroke-width" : 0,
                        opacity : 0
                    },
                    highlight : {
                        newProps : {
                            r : 3,
                            opacity : 1
                        },
                        overlayProps : {
                            fill : "white",
                            opacity : 0.2
                        }
                    },
                    tooltip : {
                        active : true,
                        width : 200,
                        height : 60,
                        roundedCorners : 5,
                        padding : [6, 6] /* y, x */,
                        offset : [20, 0] /* y, x */,
                        frameProps : { fill : "white", "stroke-width" : 1 },
                        contentStyle : { "font-family" : "Arial", "font-size" : "12px", "line-height" : "16px", color : "black" }
                    }
                },
                series : colors,
                defaultAxis : {
                    labels : true,
                    labelsDistance : 15
                },
                axis : {
                    l : {
                        labelsDistance : 5,
                        labelsFormatHandler : function (label) {
                            var res = label.toString().replace("\.", ",");
                            res = Istac.widget.helper.addThousandSeparator(res);
                            return res;
                        }
                    }
                },
                features : {
                    grid : {
                        draw : [true, false],
                        forceBorder : true,
                        props : {
                            "stroke-dasharray" : "-"
                        }
                    }
                },
                width : chartWidth,
                height : containerHeight,
                labels : renderData.labels,
                values : renderData.values,
                tooltips : renderData.tooltips
            };

            if (this.showLegend) {
                chartOptions.features.legend = {
                    width : legendWidth,
                    height : legendHeight,
                    x : 0,
                    y : legendTop,
                    dotType : "circle",
                    dotProps : {
                        stroke : "white",
                        "stroke-width" : 2
                    },
                    borderProps : {
                        fill : "#fff",
                        "stroke-width" : 0,
                        "stroke-opacity" : 0
                    },
                    textProps : { font : '10px Arial', fill : "#000" }
                }
                chartOptions.legend = renderData.legend;
            }

            if (!this.showLabels) {
                chartOptions.axis.x = {
                    labels : false
                }
            }

            // Scale algorithm
            if (this.options.scale === "minmax") {
                chartOptions.axis.l.normalize = 0;
                chartOptions.axis.l.min = renderData.minValue;
                chartOptions.axis.l.max = renderData.maxValue;
            } else if (this.options.scale == "natural-lib") {
                chartOptions.axis.l.normalize = 2;
                chartOptions.axis.l.min = renderData.minValue;
                chartOptions.axis.l.max = renderData.maxValue;
            } else if (this.options.scale == "natural") {
                // istac algorithm
                chartOptions.axis.l.normalize = 0;
                var scale = Istac.widget.NaturalScale.scale({ymin : renderData.minValue, ymax : renderData.maxValue });
                chartOptions.axis.l.min = scale.ydown;
                chartOptions.axis.l.max = scale.ytop;
                chartOptions.features.grid.ny = scale.ranges;
            }

            $chartContainer.chart(chartOptions);

        }

    });

}(jQuery));
(function () {

    Istac.widget.loader = {

        css : function (cssId, url) {
            var doc = document;
            if (!doc.getElementById(cssId)) {
                var head = doc.getElementsByTagName('head')[0];
                var link = doc.createElement('link');
                link.id = cssId;
                link.rel = 'stylesheet';
                link.type = 'text/css';
                link.href = url;
                link.media = 'all';
                head.appendChild(link);
            }
        },

        js : function (condition, url) {
            if (condition) {
                var head = document.getElementsByTagName('head')[0],
                    script = document.createElement('script');
                script.src = url;
                head.appendChild(script);
            }
        },

        all : function (url) {
            this.css('istac-widget-css', url + '/theme/js/widgets/widgets.css');
            this.js(!window.jQuery, url + '/theme/js/widgets/libs/jquery-1.7.1.js');
            this.js(!window.Raphael, url + '/theme/js/widgets/libs/raphael-min.js');
            this.js(!(window.jQuery && window.jQuery.elycharts), url + '/theme/js/widgets/libs/elycharts.min.js');
        }
    };

}());
(function ($) {

    var showError = function (msg) {
        $(options.el).text(msg);
    };

    Istac.widget.Factory = function (options, callback) {
        options = options || {};

        if (options.hasOwnProperty('url')) {
            $.getJSON(options.url + "/widgets/external/configuration").success(function (configuration) {
                Istac.widget.configuration = configuration;

                if (!options.uwa) {
                    Istac.widget.loader.all(options.url);
                }

                var widget;
                if (options.type === 'temporal') {
                    widget = new Istac.widget.Temporal(options);
                } else if (options.type === 'lastData' || options.type === 'recent') {
                    widget = new Istac.widget.LastData(options);
                }

                if (widget) {
                    widget.render();
                } else {
                    showError("Tipo de widget no soportado");
                }

                if (callback) {
                    callback(widget);
                }
            });
        } else {
            showError("Error, no se ha especificado la url del servicio web");
        }
    };

    //Global export
    window.IstacWidget = Istac.widget.Factory;

}(jQuery));

