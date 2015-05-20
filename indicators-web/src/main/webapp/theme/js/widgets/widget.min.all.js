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
/*
 Highcharts JS v3.0.7 (2013-10-24)

 (c) 2009-2013 Torstein Hønsi

 License: www.highcharts.com/license
*/
(function(){function s(a,b){var c;a||(a={});for(c in b)a[c]=b[c];return a}function x(){var a,b=arguments.length,c={},d=function(a,b){var c,h;typeof a!=="object"&&(a={});for(h in b)b.hasOwnProperty(h)&&(c=b[h],a[h]=c&&typeof c==="object"&&Object.prototype.toString.call(c)!=="[object Array]"&&typeof c.nodeType!=="number"?d(a[h]||{},c):b[h]);return a};for(a=0;a<b;a++)c=d(c,arguments[a]);return c}function y(a,b){return parseInt(a,b||10)}function ea(a){return typeof a==="string"}function T(a){return typeof a===
"object"}function Ia(a){return Object.prototype.toString.call(a)==="[object Array]"}function ra(a){return typeof a==="number"}function ma(a){return R.log(a)/R.LN10}function fa(a){return R.pow(10,a)}function ga(a,b){for(var c=a.length;c--;)if(a[c]===b){a.splice(c,1);break}}function u(a){return a!==v&&a!==null}function w(a,b,c){var d,e;if(ea(b))u(c)?a.setAttribute(b,c):a&&a.getAttribute&&(e=a.getAttribute(b));else if(u(b)&&T(b))for(d in b)a.setAttribute(d,b[d]);return e}function ja(a){return Ia(a)?
a:[a]}function o(){var a=arguments,b,c,d=a.length;for(b=0;b<d;b++)if(c=a[b],typeof c!=="undefined"&&c!==null)return c}function I(a,b){if(sa&&b&&b.opacity!==v)b.filter="alpha(opacity="+b.opacity*100+")";s(a.style,b)}function U(a,b,c,d,e){a=z.createElement(a);b&&s(a,b);e&&I(a,{padding:0,border:S,margin:0});c&&I(a,c);d&&d.appendChild(a);return a}function ha(a,b){var c=function(){};c.prototype=new a;s(c.prototype,b);return c}function Aa(a,b,c,d){var e=L.lang,a=+a||0,f=b===-1?(a.toString().split(".")[1]||
"").length:isNaN(b=M(b))?2:b,b=c===void 0?e.decimalPoint:c,d=d===void 0?e.thousandsSep:d,e=a<0?"-":"",c=String(y(a=M(a).toFixed(f))),g=c.length>3?c.length%3:0;return e+(g?c.substr(0,g)+d:"")+c.substr(g).replace(/(\d{3})(?=\d)/g,"$1"+d)+(f?b+M(a-c).toFixed(f).slice(2):"")}function Ba(a,b){return Array((b||2)+1-String(a).length).join(0)+a}function mb(a,b,c){var d=a[b];a[b]=function(){var a=Array.prototype.slice.call(arguments);a.unshift(d);return c.apply(this,a)}}function Ca(a,b){for(var c="{",d=!1,
e,f,g,h,i,j=[];(c=a.indexOf(c))!==-1;){e=a.slice(0,c);if(d){f=e.split(":");g=f.shift().split(".");i=g.length;e=b;for(h=0;h<i;h++)e=e[g[h]];if(f.length)f=f.join(":"),g=/\.([0-9])/,h=L.lang,i=void 0,/f$/.test(f)?(i=(i=f.match(g))?i[1]:-1,e=Aa(e,i,h.decimalPoint,f.indexOf(",")>-1?h.thousandsSep:"")):e=Ya(f,e)}j.push(e);a=a.slice(c+1);c=(d=!d)?"}":"{"}j.push(a);return j.join("")}function nb(a){return R.pow(10,P(R.log(a)/R.LN10))}function ob(a,b,c,d){var e,c=o(c,1);e=a/c;b||(b=[1,2,2.5,5,10],d&&d.allowDecimals===
!1&&(c===1?b=[1,2,5,10]:c<=0.1&&(b=[1/c])));for(d=0;d<b.length;d++)if(a=b[d],e<=(b[d]+(b[d+1]||b[d]))/2)break;a*=c;return a}function Cb(a,b){var c=b||[[Db,[1,2,5,10,20,25,50,100,200,500]],[pb,[1,2,5,10,15,30]],[Za,[1,2,5,10,15,30]],[Qa,[1,2,3,4,6,8,12]],[ta,[1,2]],[$a,[1,2]],[Ra,[1,2,3,4,6]],[Da,null]],d=c[c.length-1],e=D[d[0]],f=d[1],g;for(g=0;g<c.length;g++)if(d=c[g],e=D[d[0]],f=d[1],c[g+1]&&a<=(e*f[f.length-1]+D[c[g+1][0]])/2)break;e===D[Da]&&a<5*e&&(f=[1,2,5]);c=ob(a/e,f,d[0]===Da?r(nb(a/e),1):
1);return{unitRange:e,count:c,unitName:d[0]}}function Eb(a,b,c,d){var e=[],f={},g=L.global.useUTC,h,i=new Date(b),j=a.unitRange,k=a.count;if(u(b)){j>=D[pb]&&(i.setMilliseconds(0),i.setSeconds(j>=D[Za]?0:k*P(i.getSeconds()/k)));if(j>=D[Za])i[Fb](j>=D[Qa]?0:k*P(i[qb]()/k));if(j>=D[Qa])i[Gb](j>=D[ta]?0:k*P(i[rb]()/k));if(j>=D[ta])i[sb](j>=D[Ra]?1:k*P(i[Sa]()/k));j>=D[Ra]&&(i[Hb](j>=D[Da]?0:k*P(i[ab]()/k)),h=i[bb]());j>=D[Da]&&(h-=h%k,i[Ib](h));if(j===D[$a])i[sb](i[Sa]()-i[tb]()+o(d,1));b=1;h=i[bb]();
for(var d=i.getTime(),l=i[ab](),m=i[Sa](),p=g?0:(864E5+i.getTimezoneOffset()*6E4)%864E5;d<c;)e.push(d),j===D[Da]?d=cb(h+b*k,0):j===D[Ra]?d=cb(h,l+b*k):!g&&(j===D[ta]||j===D[$a])?d=cb(h,l,m+b*k*(j===D[ta]?1:7)):d+=j*k,b++;e.push(d);n(ub(e,function(a){return j<=D[Qa]&&a%D[ta]===p}),function(a){f[a]=ta})}e.info=s(a,{higherRanks:f,totalRange:j*k});return e}function Jb(){this.symbol=this.color=0}function Kb(a,b){var c=a.length,d,e;for(e=0;e<c;e++)a[e].ss_i=e;a.sort(function(a,c){d=b(a,c);return d===0?
a.ss_i-c.ss_i:d});for(e=0;e<c;e++)delete a[e].ss_i}function Ja(a){for(var b=a.length,c=a[0];b--;)a[b]<c&&(c=a[b]);return c}function ua(a){for(var b=a.length,c=a[0];b--;)a[b]>c&&(c=a[b]);return c}function Ka(a,b){for(var c in a)a[c]&&a[c]!==b&&a[c].destroy&&a[c].destroy(),delete a[c]}function Ta(a){db||(db=U(Ea));a&&db.appendChild(a);db.innerHTML=""}function ka(a,b){var c="Highcharts error #"+a+": www.highcharts.com/errors/"+a;if(b)throw c;else N.console&&console.log(c)}function ia(a){return parseFloat(a.toPrecision(14))}
function La(a,b){Fa=o(a,b.animation)}function Lb(){var a=L.global.useUTC,b=a?"getUTC":"get",c=a?"setUTC":"set";cb=a?Date.UTC:function(a,b,c,g,h,i){return(new Date(a,b,o(c,1),o(g,0),o(h,0),o(i,0))).getTime()};qb=b+"Minutes";rb=b+"Hours";tb=b+"Day";Sa=b+"Date";ab=b+"Month";bb=b+"FullYear";Fb=c+"Minutes";Gb=c+"Hours";sb=c+"Date";Hb=c+"Month";Ib=c+"FullYear"}function va(){}function Ma(a,b,c,d){this.axis=a;this.pos=b;this.type=c||"";this.isNew=!0;!c&&!d&&this.addLabel()}function vb(a,b){this.axis=a;if(b)this.options=
b,this.id=b.id}function Mb(a,b,c,d,e,f){var g=a.chart.inverted;this.axis=a;this.isNegative=c;this.options=b;this.x=d;this.total=null;this.points={};this.stack=e;this.percent=f==="percent";this.alignOptions={align:b.align||(g?c?"left":"right":"center"),verticalAlign:b.verticalAlign||(g?"middle":c?"bottom":"top"),y:o(b.y,g?4:c?14:-6),x:o(b.x,g?c?-6:6:0)};this.textAlign=b.textAlign||(g?c?"right":"left":"center")}function eb(){this.init.apply(this,arguments)}function wb(){this.init.apply(this,arguments)}
function xb(a,b){this.init(a,b)}function fb(a,b){this.init(a,b)}function yb(){this.init.apply(this,arguments)}var v,z=document,N=window,R=Math,t=R.round,P=R.floor,wa=R.ceil,r=R.max,J=R.min,M=R.abs,V=R.cos,ba=R.sin,xa=R.PI,Ua=xa*2/360,na=navigator.userAgent,Nb=N.opera,sa=/msie/i.test(na)&&!Nb,gb=z.documentMode===8,hb=/AppleWebKit/.test(na),ib=/Firefox/.test(na),Ob=/(Mobile|Android|Windows Phone)/.test(na),ya="http://www.w3.org/2000/svg",W=!!z.createElementNS&&!!z.createElementNS(ya,"svg").createSVGRect,
Ub=ib&&parseInt(na.split("Firefox/")[1],10)<4,ca=!W&&!sa&&!!z.createElement("canvas").getContext,Va,jb=z.documentElement.ontouchstart!==v,Pb={},zb=0,db,L,Ya,Fa,Ab,D,oa=function(){},Ga=[],Ea="div",S="none",Qb="rgba(192,192,192,"+(W?1.0E-4:0.002)+")",Db="millisecond",pb="second",Za="minute",Qa="hour",ta="day",$a="week",Ra="month",Da="year",Rb="stroke-width",cb,qb,rb,tb,Sa,ab,bb,Fb,Gb,sb,Hb,Ib,X={};N.Highcharts=N.Highcharts?ka(16,!0):{};Ya=function(a,b,c){if(!u(b)||isNaN(b))return"Invalid date";var a=
o(a,"%Y-%m-%d %H:%M:%S"),d=new Date(b),e,f=d[rb](),g=d[tb](),h=d[Sa](),i=d[ab](),j=d[bb](),k=L.lang,l=k.weekdays,d=s({a:l[g].substr(0,3),A:l[g],d:Ba(h),e:h,b:k.shortMonths[i],B:k.months[i],m:Ba(i+1),y:j.toString().substr(2,2),Y:j,H:Ba(f),I:Ba(f%12||12),l:f%12||12,M:Ba(d[qb]()),p:f<12?"AM":"PM",P:f<12?"am":"pm",S:Ba(d.getSeconds()),L:Ba(t(b%1E3),3)},Highcharts.dateFormats);for(e in d)for(;a.indexOf("%"+e)!==-1;)a=a.replace("%"+e,typeof d[e]==="function"?d[e](b):d[e]);return c?a.substr(0,1).toUpperCase()+
a.substr(1):a};Jb.prototype={wrapColor:function(a){if(this.color>=a)this.color=0},wrapSymbol:function(a){if(this.symbol>=a)this.symbol=0}};D=function(){for(var a=0,b=arguments,c=b.length,d={};a<c;a++)d[b[a++]]=b[a];return d}(Db,1,pb,1E3,Za,6E4,Qa,36E5,ta,864E5,$a,6048E5,Ra,26784E5,Da,31556952E3);Ab={init:function(a,b,c){var b=b||"",d=a.shift,e=b.indexOf("C")>-1,f=e?7:3,g,b=b.split(" "),c=[].concat(c),h,i,j=function(a){for(g=a.length;g--;)a[g]==="M"&&a.splice(g+1,0,a[g+1],a[g+2],a[g+1],a[g+2])};e&&
(j(b),j(c));a.isArea&&(h=b.splice(b.length-6,6),i=c.splice(c.length-6,6));if(d<=c.length/f&&b.length===c.length)for(;d--;)c=[].concat(c).splice(0,f).concat(c);a.shift=0;if(b.length)for(a=c.length;b.length<a;)d=[].concat(b).splice(b.length-f,f),e&&(d[f-6]=d[f-2],d[f-5]=d[f-1]),b=b.concat(d);h&&(b=b.concat(h),c=c.concat(i));return[b,c]},step:function(a,b,c,d){var e=[],f=a.length;if(c===1)e=d;else if(f===b.length&&c<1)for(;f--;)d=parseFloat(a[f]),e[f]=isNaN(d)?a[f]:c*parseFloat(b[f]-d)+d;else e=b;return e}};
(function(a){N.HighchartsAdapter=N.HighchartsAdapter||a&&{init:function(b){var c=a.fx,d=c.step,e,f=a.Tween,g=f&&f.propHooks;e=a.cssHooks.opacity;a.extend(a.easing,{easeOutQuad:function(a,b,c,d,e){return-d*(b/=e)*(b-2)+c}});a.each(["cur","_default","width","height","opacity"],function(a,b){var e=d,k,l;b==="cur"?e=c.prototype:b==="_default"&&f&&(e=g[b],b="set");(k=e[b])&&(e[b]=function(c){c=a?c:this;if(c.prop!=="align")return l=c.elem,l.attr?l.attr(c.prop,b==="cur"?v:c.now):k.apply(this,arguments)})});
mb(e,"get",function(a,b,c){return b.attr?b.opacity||0:a.call(this,b,c)});e=function(a){var c=a.elem,d;if(!a.started)d=b.init(c,c.d,c.toD),a.start=d[0],a.end=d[1],a.started=!0;c.attr("d",b.step(a.start,a.end,a.pos,c.toD))};f?g.d={set:e}:d.d=e;this.each=Array.prototype.forEach?function(a,b){return Array.prototype.forEach.call(a,b)}:function(a,b){for(var c=0,d=a.length;c<d;c++)if(b.call(a[c],a[c],c,a)===!1)return c};a.fn.highcharts=function(){var a="Chart",b=arguments,c,d;ea(b[0])&&(a=b[0],b=Array.prototype.slice.call(b,
1));c=b[0];if(c!==v)c.chart=c.chart||{},c.chart.renderTo=this[0],new Highcharts[a](c,b[1]),d=this;c===v&&(d=Ga[w(this[0],"data-highcharts-chart")]);return d}},getScript:a.getScript,inArray:a.inArray,adapterRun:function(b,c){return a(b)[c]()},grep:a.grep,map:function(a,c){for(var d=[],e=0,f=a.length;e<f;e++)d[e]=c.call(a[e],a[e],e,a);return d},offset:function(b){return a(b).offset()},addEvent:function(b,c,d){a(b).bind(c,d)},removeEvent:function(b,c,d){var e=z.removeEventListener?"removeEventListener":
"detachEvent";z[e]&&b&&!b[e]&&(b[e]=function(){});a(b).unbind(c,d)},fireEvent:function(b,c,d,e){var f=a.Event(c),g="detached"+c,h;!sa&&d&&(delete d.layerX,delete d.layerY);s(f,d);b[c]&&(b[g]=b[c],b[c]=null);a.each(["preventDefault","stopPropagation"],function(a,b){var c=f[b];f[b]=function(){try{c.call(f)}catch(a){b==="preventDefault"&&(h=!0)}}});a(b).trigger(f);b[g]&&(b[c]=b[g],b[g]=null);e&&!f.isDefaultPrevented()&&!h&&e(f)},washMouseEvent:function(a){var c=a.originalEvent||a;if(c.pageX===v)c.pageX=
a.pageX,c.pageY=a.pageY;return c},animate:function(b,c,d){var e=a(b);if(!b.style)b.style={};if(c.d)b.toD=c.d,c.d=1;e.stop();c.opacity!==v&&b.attr&&(c.opacity+="px");e.animate(c,d)},stop:function(b){a(b).stop()}}})(N.jQuery);var Y=N.HighchartsAdapter,G=Y||{};Y&&Y.init.call(Y,Ab);var kb=G.adapterRun,Vb=G.getScript,pa=G.inArray,n=G.each,ub=G.grep,Wb=G.offset,Na=G.map,K=G.addEvent,$=G.removeEvent,A=G.fireEvent,Xb=G.washMouseEvent,Bb=G.animate,Wa=G.stop,G={enabled:!0,x:0,y:15,style:{color:"#666",cursor:"default",
fontSize:"11px",lineHeight:"14px"}};L={colors:"#2f7ed8,#0d233a,#8bbc21,#910000,#1aadce,#492970,#f28f43,#77a1e5,#c42525,#a6c96a".split(","),symbols:["circle","diamond","square","triangle","triangle-down"],lang:{loading:"Loading...",months:"January,February,March,April,May,June,July,August,September,October,November,December".split(","),shortMonths:"Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec".split(","),weekdays:"Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday".split(","),decimalPoint:".",
numericSymbols:"k,M,G,T,P,E".split(","),resetZoom:"Reset zoom",resetZoomTitle:"Reset zoom level 1:1",thousandsSep:","},global:{useUTC:!0,canvasToolsURL:"http://code.highcharts.com/3.0.7/modules/canvas-tools.js",VMLRadialGradientURL:"http://code.highcharts.com/3.0.7/gfx/vml-radial-gradient.png"},chart:{borderColor:"#4572A7",borderRadius:5,defaultSeriesType:"line",ignoreHiddenSeries:!0,spacing:[10,10,15,10],style:{fontFamily:'"Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
fontSize:"12px"},backgroundColor:"#FFFFFF",plotBorderColor:"#C0C0C0",resetZoomButton:{theme:{zIndex:20},position:{align:"right",x:-10,y:10}}},title:{text:"Chart title",align:"center",margin:15,style:{color:"#274b6d",fontSize:"16px"}},subtitle:{text:"",align:"center",style:{color:"#4d759e"}},plotOptions:{line:{allowPointSelect:!1,showCheckbox:!1,animation:{duration:1E3},events:{},lineWidth:2,marker:{enabled:!0,lineWidth:0,radius:4,lineColor:"#FFFFFF",states:{hover:{enabled:!0},select:{fillColor:"#FFFFFF",
lineColor:"#000000",lineWidth:2}}},point:{events:{}},dataLabels:x(G,{align:"center",enabled:!1,formatter:function(){return this.y===null?"":Aa(this.y,-1)},verticalAlign:"bottom",y:0}),cropThreshold:300,pointRange:0,states:{hover:{marker:{}},select:{marker:{}}},stickyTracking:!0}},labels:{style:{position:"absolute",color:"#3E576F"}},legend:{enabled:!0,align:"center",layout:"horizontal",labelFormatter:function(){return this.name},borderWidth:1,borderColor:"#909090",borderRadius:5,navigation:{activeColor:"#274b6d",
inactiveColor:"#CCC"},shadow:!1,itemStyle:{cursor:"pointer",color:"#274b6d",fontSize:"12px"},itemHoverStyle:{color:"#000"},itemHiddenStyle:{color:"#CCC"},itemCheckboxStyle:{position:"absolute",width:"13px",height:"13px"},symbolWidth:16,symbolPadding:5,verticalAlign:"bottom",x:0,y:0,title:{style:{fontWeight:"bold"}}},loading:{labelStyle:{fontWeight:"bold",position:"relative",top:"1em"},style:{position:"absolute",backgroundColor:"white",opacity:0.5,textAlign:"center"}},tooltip:{enabled:!0,animation:W,
backgroundColor:"rgba(255, 255, 255, .85)",borderWidth:1,borderRadius:3,dateTimeLabelFormats:{millisecond:"%A, %b %e, %H:%M:%S.%L",second:"%A, %b %e, %H:%M:%S",minute:"%A, %b %e, %H:%M",hour:"%A, %b %e, %H:%M",day:"%A, %b %e, %Y",week:"Week from %A, %b %e, %Y",month:"%B %Y",year:"%Y"},headerFormat:'<span style="font-size: 10px">{point.key}</span><br/>',pointFormat:'<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',shadow:!0,snap:Ob?25:10,style:{color:"#333333",cursor:"default",
fontSize:"12px",padding:"8px",whiteSpace:"nowrap"}},credits:{enabled:!0,text:"Highcharts.com",href:"http://www.highcharts.com",position:{align:"right",x:-10,verticalAlign:"bottom",y:-5},style:{cursor:"pointer",color:"#909090",fontSize:"9px"}}};var Z=L.plotOptions,Y=Z.line;Lb();var Yb=/rgba\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]?(?:\.[0-9]+)?)\s*\)/,Zb=/#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})/,$b=/rgb\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*\)/,
qa=function(a){var b=[],c,d;(function(a){a&&a.stops?d=Na(a.stops,function(a){return qa(a[1])}):(c=Yb.exec(a))?b=[y(c[1]),y(c[2]),y(c[3]),parseFloat(c[4],10)]:(c=Zb.exec(a))?b=[y(c[1],16),y(c[2],16),y(c[3],16),1]:(c=$b.exec(a))&&(b=[y(c[1]),y(c[2]),y(c[3]),1])})(a);return{get:function(c){var f;d?(f=x(a),f.stops=[].concat(f.stops),n(d,function(a,b){f.stops[b]=[f.stops[b][0],a.get(c)]})):f=b&&!isNaN(b[0])?c==="rgb"?"rgb("+b[0]+","+b[1]+","+b[2]+")":c==="a"?b[3]:"rgba("+b.join(",")+")":a;return f},brighten:function(a){if(d)n(d,
function(b){b.brighten(a)});else if(ra(a)&&a!==0){var c;for(c=0;c<3;c++)b[c]+=y(a*255),b[c]<0&&(b[c]=0),b[c]>255&&(b[c]=255)}return this},rgba:b,setOpacity:function(a){b[3]=a;return this}}};va.prototype={init:function(a,b){this.element=b==="span"?U(b):z.createElementNS(ya,b);this.renderer=a;this.attrSetters={}},opacity:1,animate:function(a,b,c){b=o(b,Fa,!0);Wa(this);if(b){b=x(b);if(c)b.complete=c;Bb(this,a,b)}else this.attr(a),c&&c()},attr:function(a,b){var c,d,e,f,g=this.element,h=g.nodeName.toLowerCase(),
i=this.renderer,j,k=this.attrSetters,l=this.shadows,m,p,q=this;ea(a)&&u(b)&&(c=a,a={},a[c]=b);if(ea(a))c=a,h==="circle"?c={x:"cx",y:"cy"}[c]||c:c==="strokeWidth"&&(c="stroke-width"),q=w(g,c)||this[c]||0,c!=="d"&&c!=="visibility"&&c!=="fill"&&(q=parseFloat(q));else{for(c in a)if(j=!1,d=a[c],e=k[c]&&k[c].call(this,d,c),e!==!1){e!==v&&(d=e);if(c==="d")d&&d.join&&(d=d.join(" ")),/(NaN| {2}|^$)/.test(d)&&(d="M 0 0");else if(c==="x"&&h==="text")for(e=0;e<g.childNodes.length;e++)f=g.childNodes[e],w(f,"x")===
w(g,"x")&&w(f,"x",d);else if(this.rotation&&(c==="x"||c==="y"))p=!0;else if(c==="fill")d=i.color(d,g,c);else if(h==="circle"&&(c==="x"||c==="y"))c={x:"cx",y:"cy"}[c]||c;else if(h==="rect"&&c==="r")w(g,{rx:d,ry:d}),j=!0;else if(c==="translateX"||c==="translateY"||c==="rotation"||c==="verticalAlign"||c==="scaleX"||c==="scaleY")j=p=!0;else if(c==="stroke")d=i.color(d,g,c);else if(c==="dashstyle")if(c="stroke-dasharray",d=d&&d.toLowerCase(),d==="solid")d=S;else{if(d){d=d.replace("shortdashdotdot","3,1,1,1,1,1,").replace("shortdashdot",
"3,1,1,1").replace("shortdot","1,1,").replace("shortdash","3,1,").replace("longdash","8,3,").replace(/dot/g,"1,3,").replace("dash","4,3,").replace(/,$/,"").split(",");for(e=d.length;e--;)d[e]=y(d[e])*o(a["stroke-width"],this["stroke-width"]);d=d.join(",")}}else if(c==="width")d=y(d);else if(c==="align")c="text-anchor",d={left:"start",center:"middle",right:"end"}[d];else if(c==="title")e=g.getElementsByTagName("title")[0],e||(e=z.createElementNS(ya,"title"),g.appendChild(e)),e.textContent=d;c==="strokeWidth"&&
(c="stroke-width");if(c==="stroke-width"||c==="stroke"){this[c]=d;if(this.stroke&&this["stroke-width"])w(g,"stroke",this.stroke),w(g,"stroke-width",this["stroke-width"]),this.hasStroke=!0;else if(c==="stroke-width"&&d===0&&this.hasStroke)g.removeAttribute("stroke"),this.hasStroke=!1;j=!0}this.symbolName&&/^(x|y|width|height|r|start|end|innerR|anchorX|anchorY)/.test(c)&&(m||(this.symbolAttr(a),m=!0),j=!0);if(l&&/^(width|height|visibility|x|y|d|transform|cx|cy|r)$/.test(c))for(e=l.length;e--;)w(l[e],
c,c==="height"?r(d-(l[e].cutHeight||0),0):d);if((c==="width"||c==="height")&&h==="rect"&&d<0)d=0;this[c]=d;c==="text"?(d!==this.textStr&&delete this.bBox,this.textStr=d,this.added&&i.buildText(this)):j||w(g,c,d)}p&&this.updateTransform()}return q},addClass:function(a){var b=this.element,c=w(b,"class")||"";c.indexOf(a)===-1&&w(b,"class",c+" "+a);return this},symbolAttr:function(a){var b=this;n("x,y,r,start,end,width,height,innerR,anchorX,anchorY".split(","),function(c){b[c]=o(a[c],b[c])});b.attr({d:b.renderer.symbols[b.symbolName](b.x,
b.y,b.width,b.height,b)})},clip:function(a){return this.attr("clip-path",a?"url("+this.renderer.url+"#"+a.id+")":S)},crisp:function(a,b,c,d,e){var f,g={},h={},i,a=a||this.strokeWidth||this.attr&&this.attr("stroke-width")||0;i=t(a)%2/2;h.x=P(b||this.x||0)+i;h.y=P(c||this.y||0)+i;h.width=P((d||this.width||0)-2*i);h.height=P((e||this.height||0)-2*i);h.strokeWidth=a;for(f in h)this[f]!==h[f]&&(this[f]=g[f]=h[f]);return g},css:function(a){var b=this.element,c=this.textWidth=a&&a.width&&b.nodeName.toLowerCase()===
"text"&&y(a.width),d,e="",f=function(a,b){return"-"+b.toLowerCase()};if(a&&a.color)a.fill=a.color;this.styles=a=s(this.styles,a);c&&delete a.width;if(sa&&!W)I(this.element,a);else{for(d in a)e+=d.replace(/([A-Z])/g,f)+":"+a[d]+";";w(b,"style",e)}c&&this.added&&this.renderer.buildText(this);return this},on:function(a,b){var c=this,d=c.element;jb&&a==="click"?(d.ontouchstart=function(a){c.touchEventFired=Date.now();a.preventDefault();b.call(d,a)},d.onclick=function(a){(na.indexOf("Android")===-1||Date.now()-
(c.touchEventFired||0)>1100)&&b.call(d,a)}):d["on"+a]=b;return this},setRadialReference:function(a){this.element.radialReference=a;return this},translate:function(a,b){return this.attr({translateX:a,translateY:b})},invert:function(){this.inverted=!0;this.updateTransform();return this},htmlCss:function(a){var b=this.element;if(b=a&&b.tagName==="SPAN"&&a.width)delete a.width,this.textWidth=b,this.updateTransform();this.styles=s(this.styles,a);I(this.element,a);return this},htmlGetBBox:function(){var a=
this.element,b=this.bBox;if(!b){if(a.nodeName==="text")a.style.position="absolute";b=this.bBox={x:a.offsetLeft,y:a.offsetTop,width:a.offsetWidth,height:a.offsetHeight}}return b},htmlUpdateTransform:function(){if(this.added){var a=this.renderer,b=this.element,c=this.translateX||0,d=this.translateY||0,e=this.x||0,f=this.y||0,g=this.textAlign||"left",h={left:0,center:0.5,right:1}[g],i=g&&g!=="left",j=this.shadows;I(b,{marginLeft:c,marginTop:d});j&&n(j,function(a){I(a,{marginLeft:c+1,marginTop:d+1})});
this.inverted&&n(b.childNodes,function(c){a.invertChild(c,b)});if(b.tagName==="SPAN"){var k,l,j=this.rotation,m;k=0;var p=1,q=0,aa;m=y(this.textWidth);var B=this.xCorr||0,O=this.yCorr||0,Sb=[j,g,b.innerHTML,this.textWidth].join(",");if(Sb!==this.cTT){u(j)&&(k=j*Ua,p=V(k),q=ba(k),this.setSpanRotation(j,q,p));k=o(this.elemWidth,b.offsetWidth);l=o(this.elemHeight,b.offsetHeight);if(k>m&&/[ \-]/.test(b.textContent||b.innerText))I(b,{width:m+"px",display:"block",whiteSpace:"normal"}),k=m;m=a.fontMetrics(b.style.fontSize).b;
B=p<0&&-k;O=q<0&&-l;aa=p*q<0;B+=q*m*(aa?1-h:h);O-=p*m*(j?aa?h:1-h:1);i&&(B-=k*h*(p<0?-1:1),j&&(O-=l*h*(q<0?-1:1)),I(b,{textAlign:g}));this.xCorr=B;this.yCorr=O}I(b,{left:e+B+"px",top:f+O+"px"});if(hb)l=b.offsetHeight;this.cTT=Sb}}else this.alignOnAdd=!0},setSpanRotation:function(a){var b={};b[sa?"-ms-transform":hb?"-webkit-transform":ib?"MozTransform":Nb?"-o-transform":""]=b.transform="rotate("+a+"deg)";I(this.element,b)},updateTransform:function(){var a=this.translateX||0,b=this.translateY||0,c=
this.scaleX,d=this.scaleY,e=this.inverted,f=this.rotation;e&&(a+=this.attr("width"),b+=this.attr("height"));a=["translate("+a+","+b+")"];e?a.push("rotate(90) scale(-1,1)"):f&&a.push("rotate("+f+" "+(this.x||0)+" "+(this.y||0)+")");(u(c)||u(d))&&a.push("scale("+o(c,1)+" "+o(d,1)+")");a.length&&w(this.element,"transform",a.join(" "))},toFront:function(){var a=this.element;a.parentNode.appendChild(a);return this},align:function(a,b,c){var d,e,f,g,h={};e=this.renderer;f=e.alignedObjects;if(a){if(this.alignOptions=
a,this.alignByTranslate=b,!c||ea(c))this.alignTo=d=c||"renderer",ga(f,this),f.push(this),c=null}else a=this.alignOptions,b=this.alignByTranslate,d=this.alignTo;c=o(c,e[d],e);d=a.align;e=a.verticalAlign;f=(c.x||0)+(a.x||0);g=(c.y||0)+(a.y||0);if(d==="right"||d==="center")f+=(c.width-(a.width||0))/{right:1,center:2}[d];h[b?"translateX":"x"]=t(f);if(e==="bottom"||e==="middle")g+=(c.height-(a.height||0))/({bottom:1,middle:2}[e]||1);h[b?"translateY":"y"]=t(g);this[this.placed?"animate":"attr"](h);this.placed=
!0;this.alignAttr=h;return this},getBBox:function(){var a=this.bBox,b=this.renderer,c,d=this.rotation;c=this.element;var e=this.styles,f=d*Ua;if(!a){if(c.namespaceURI===ya||b.forExport){try{a=c.getBBox?s({},c.getBBox()):{width:c.offsetWidth,height:c.offsetHeight}}catch(g){}if(!a||a.width<0)a={width:0,height:0}}else a=this.htmlGetBBox();if(b.isSVG){b=a.width;c=a.height;if(sa&&e&&e.fontSize==="11px"&&c.toPrecision(3)==="22.7")a.height=c=14;if(d)a.width=M(c*ba(f))+M(b*V(f)),a.height=M(c*V(f))+M(b*ba(f))}this.bBox=
a}return a},show:function(){return this.attr({visibility:"visible"})},hide:function(){return this.attr({visibility:"hidden"})},fadeOut:function(a){var b=this;b.animate({opacity:0},{duration:a||150,complete:function(){b.hide()}})},add:function(a){var b=this.renderer,c=a||b,d=c.element||b.box,e=d.childNodes,f=this.element,g=w(f,"zIndex"),h;if(a)this.parentGroup=a;this.parentInverted=a&&a.inverted;this.textStr!==void 0&&b.buildText(this);if(g)c.handleZ=!0,g=y(g);if(c.handleZ)for(c=0;c<e.length;c++)if(a=
e[c],b=w(a,"zIndex"),a!==f&&(y(b)>g||!u(g)&&u(b))){d.insertBefore(f,a);h=!0;break}h||d.appendChild(f);this.added=!0;A(this,"add");return this},safeRemoveChild:function(a){var b=a.parentNode;b&&b.removeChild(a)},destroy:function(){var a=this,b=a.element||{},c=a.shadows,d=a.renderer.isSVG&&b.nodeName==="SPAN"&&a.parentGroup,e,f;b.onclick=b.onmouseout=b.onmouseover=b.onmousemove=b.point=null;Wa(a);if(a.clipPath)a.clipPath=a.clipPath.destroy();if(a.stops){for(f=0;f<a.stops.length;f++)a.stops[f]=a.stops[f].destroy();
a.stops=null}a.safeRemoveChild(b);for(c&&n(c,function(b){a.safeRemoveChild(b)});d&&d.div.childNodes.length===0;)b=d.parentGroup,a.safeRemoveChild(d.div),delete d.div,d=b;a.alignTo&&ga(a.renderer.alignedObjects,a);for(e in a)delete a[e];return null},shadow:function(a,b,c){var d=[],e,f,g=this.element,h,i,j,k;if(a){i=o(a.width,3);j=(a.opacity||0.15)/i;k=this.parentInverted?"(-1,-1)":"("+o(a.offsetX,1)+", "+o(a.offsetY,1)+")";for(e=1;e<=i;e++){f=g.cloneNode(0);h=i*2+1-2*e;w(f,{isShadow:"true",stroke:a.color||
"black","stroke-opacity":j*e,"stroke-width":h,transform:"translate"+k,fill:S});if(c)w(f,"height",r(w(f,"height")-h,0)),f.cutHeight=h;b?b.element.appendChild(f):g.parentNode.insertBefore(f,g);d.push(f)}this.shadows=d}return this}};var za=function(){this.init.apply(this,arguments)};za.prototype={Element:va,init:function(a,b,c,d){var e=location,f,g;f=this.createElement("svg").attr({version:"1.1"});g=f.element;a.appendChild(g);a.innerHTML.indexOf("xmlns")===-1&&w(g,"xmlns",ya);this.isSVG=!0;this.box=
g;this.boxWrapper=f;this.alignedObjects=[];this.url=(ib||hb)&&z.getElementsByTagName("base").length?e.href.replace(/#.*?$/,"").replace(/([\('\)])/g,"\\$1").replace(/ /g,"%20"):"";this.createElement("desc").add().element.appendChild(z.createTextNode("Created with Highcharts 3.0.7"));this.defs=this.createElement("defs").add();this.forExport=d;this.gradients={};this.setSize(b,c,!1);var h;if(ib&&a.getBoundingClientRect)this.subPixelFix=b=function(){I(a,{left:0,top:0});h=a.getBoundingClientRect();I(a,
{left:wa(h.left)-h.left+"px",top:wa(h.top)-h.top+"px"})},b(),K(N,"resize",b)},isHidden:function(){return!this.boxWrapper.getBBox().width},destroy:function(){var a=this.defs;this.box=null;this.boxWrapper=this.boxWrapper.destroy();Ka(this.gradients||{});this.gradients=null;if(a)this.defs=a.destroy();this.subPixelFix&&$(N,"resize",this.subPixelFix);return this.alignedObjects=null},createElement:function(a){var b=new this.Element;b.init(this,a);return b},draw:function(){},buildText:function(a){for(var b=
a.element,c=this,d=c.forExport,e=o(a.textStr,"").toString().replace(/<(b|strong)>/g,'<span style="font-weight:bold">').replace(/<(i|em)>/g,'<span style="font-style:italic">').replace(/<a/g,"<span").replace(/<\/(b|strong|i|em|a)>/g,"</span>").split(/<br.*?>/g),f=b.childNodes,g=/style="([^"]+)"/,h=/href="(http[^"]+)"/,i=w(b,"x"),j=a.styles,k=a.textWidth,l=j&&j.lineHeight,m=f.length;m--;)b.removeChild(f[m]);k&&!a.added&&this.box.appendChild(b);e[e.length-1]===""&&e.pop();n(e,function(e,f){var m,o=0,
e=e.replace(/<span/g,"|||<span").replace(/<\/span>/g,"</span>|||");m=e.split("|||");n(m,function(e){if(e!==""||m.length===1){var p={},n=z.createElementNS(ya,"tspan"),r;g.test(e)&&(r=e.match(g)[1].replace(/(;| |^)color([ :])/,"$1fill$2"),w(n,"style",r));h.test(e)&&!d&&(w(n,"onclick",'location.href="'+e.match(h)[1]+'"'),I(n,{cursor:"pointer"}));e=(e.replace(/<(.|\n)*?>/g,"")||" ").replace(/&lt;/g,"<").replace(/&gt;/g,">");if(e!==" "&&(n.appendChild(z.createTextNode(e)),o?p.dx=0:p.x=i,w(n,p),!o&&f&&
(!W&&d&&I(n,{display:"block"}),w(n,"dy",l||c.fontMetrics(/px$/.test(n.style.fontSize)?n.style.fontSize:j.fontSize).h,hb&&n.offsetHeight)),b.appendChild(n),o++,k))for(var e=e.replace(/([^\^])-/g,"$1- ").split(" "),u,t,p=a._clipHeight,E=[],v=y(l||16),s=1;e.length||E.length;)delete a.bBox,u=a.getBBox(),t=u.width,!W&&c.forExport&&(t=c.measureSpanWidth(n.firstChild.data,a.styles)),u=t>k,!u||e.length===1?(e=E,E=[],e.length&&(s++,p&&s*v>p?(e=["..."],a.attr("title",a.textStr)):(n=z.createElementNS(ya,"tspan"),
w(n,{dy:v,x:i}),r&&w(n,"style",r),b.appendChild(n),t>k&&(k=t)))):(n.removeChild(n.firstChild),E.unshift(e.pop())),e.length&&n.appendChild(z.createTextNode(e.join(" ").replace(/- /g,"-")))}})})},button:function(a,b,c,d,e,f,g,h,i){var j=this.label(a,b,c,i,null,null,null,null,"button"),k=0,l,m,p,q,n,o,a={x1:0,y1:0,x2:0,y2:1},e=x({"stroke-width":1,stroke:"#CCCCCC",fill:{linearGradient:a,stops:[[0,"#FEFEFE"],[1,"#F6F6F6"]]},r:2,padding:5,style:{color:"black"}},e);p=e.style;delete e.style;f=x(e,{stroke:"#68A",
fill:{linearGradient:a,stops:[[0,"#FFF"],[1,"#ACF"]]}},f);q=f.style;delete f.style;g=x(e,{stroke:"#68A",fill:{linearGradient:a,stops:[[0,"#9BD"],[1,"#CDF"]]}},g);n=g.style;delete g.style;h=x(e,{style:{color:"#CCC"}},h);o=h.style;delete h.style;K(j.element,sa?"mouseover":"mouseenter",function(){k!==3&&j.attr(f).css(q)});K(j.element,sa?"mouseout":"mouseleave",function(){k!==3&&(l=[e,f,g][k],m=[p,q,n][k],j.attr(l).css(m))});j.setState=function(a){(j.state=k=a)?a===2?j.attr(g).css(n):a===3&&j.attr(h).css(o):
j.attr(e).css(p)};return j.on("click",function(){k!==3&&d.call(j)}).attr(e).css(s({cursor:"default"},p))},crispLine:function(a,b){a[1]===a[4]&&(a[1]=a[4]=t(a[1])-b%2/2);a[2]===a[5]&&(a[2]=a[5]=t(a[2])+b%2/2);return a},path:function(a){var b={fill:S};Ia(a)?b.d=a:T(a)&&s(b,a);return this.createElement("path").attr(b)},circle:function(a,b,c){a=T(a)?a:{x:a,y:b,r:c};return this.createElement("circle").attr(a)},arc:function(a,b,c,d,e,f){if(T(a))b=a.y,c=a.r,d=a.innerR,e=a.start,f=a.end,a=a.x;a=this.symbol("arc",
a||0,b||0,c||0,c||0,{innerR:d||0,start:e||0,end:f||0});a.r=c;return a},rect:function(a,b,c,d,e,f){e=T(a)?a.r:e;e=this.createElement("rect").attr({rx:e,ry:e,fill:S});return e.attr(T(a)?a:e.crisp(f,a,b,r(c,0),r(d,0)))},setSize:function(a,b,c){var d=this.alignedObjects,e=d.length;this.width=a;this.height=b;for(this.boxWrapper[o(c,!0)?"animate":"attr"]({width:a,height:b});e--;)d[e].align()},g:function(a){var b=this.createElement("g");return u(a)?b.attr({"class":"highcharts-"+a}):b},image:function(a,b,
c,d,e){var f={preserveAspectRatio:S};arguments.length>1&&s(f,{x:b,y:c,width:d,height:e});f=this.createElement("image").attr(f);f.element.setAttributeNS?f.element.setAttributeNS("http://www.w3.org/1999/xlink","href",a):f.element.setAttribute("hc-svg-href",a);return f},symbol:function(a,b,c,d,e,f){var g,h=this.symbols[a],h=h&&h(t(b),t(c),d,e,f),i=/^url\((.*?)\)$/,j,k;if(h)g=this.path(h),s(g,{symbolName:a,x:b,y:c,width:d,height:e}),f&&s(g,f);else if(i.test(a))k=function(a,b){a.element&&(a.attr({width:b[0],
height:b[1]}),a.alignByTranslate||a.translate(t((d-b[0])/2),t((e-b[1])/2)))},j=a.match(i)[1],a=Pb[j],g=this.image(j).attr({x:b,y:c}),g.isImg=!0,a?k(g,a):(g.attr({width:0,height:0}),U("img",{onload:function(){k(g,Pb[j]=[this.width,this.height])},src:j}));return g},symbols:{circle:function(a,b,c,d){var e=0.166*c;return["M",a+c/2,b,"C",a+c+e,b,a+c+e,b+d,a+c/2,b+d,"C",a-e,b+d,a-e,b,a+c/2,b,"Z"]},square:function(a,b,c,d){return["M",a,b,"L",a+c,b,a+c,b+d,a,b+d,"Z"]},triangle:function(a,b,c,d){return["M",
a+c/2,b,"L",a+c,b+d,a,b+d,"Z"]},"triangle-down":function(a,b,c,d){return["M",a,b,"L",a+c,b,a+c/2,b+d,"Z"]},diamond:function(a,b,c,d){return["M",a+c/2,b,"L",a+c,b+d/2,a+c/2,b+d,a,b+d/2,"Z"]},arc:function(a,b,c,d,e){var f=e.start,c=e.r||c||d,g=e.end-0.001,d=e.innerR,h=e.open,i=V(f),j=ba(f),k=V(g),g=ba(g),e=e.end-f<xa?0:1;return["M",a+c*i,b+c*j,"A",c,c,0,e,1,a+c*k,b+c*g,h?"M":"L",a+d*k,b+d*g,"A",d,d,0,e,0,a+d*i,b+d*j,h?"":"Z"]}},clipRect:function(a,b,c,d){var e="highcharts-"+zb++,f=this.createElement("clipPath").attr({id:e}).add(this.defs),
a=this.rect(a,b,c,d,0).add(f);a.id=e;a.clipPath=f;return a},color:function(a,b,c){var d=this,e,f=/^rgba/,g,h,i,j,k,l,m,p=[];a&&a.linearGradient?g="linearGradient":a&&a.radialGradient&&(g="radialGradient");if(g){c=a[g];h=d.gradients;j=a.stops;b=b.radialReference;Ia(c)&&(a[g]=c={x1:c[0],y1:c[1],x2:c[2],y2:c[3],gradientUnits:"userSpaceOnUse"});g==="radialGradient"&&b&&!u(c.gradientUnits)&&(c=x(c,{cx:b[0]-b[2]/2+c.cx*b[2],cy:b[1]-b[2]/2+c.cy*b[2],r:c.r*b[2],gradientUnits:"userSpaceOnUse"}));for(m in c)m!==
"id"&&p.push(m,c[m]);for(m in j)p.push(j[m]);p=p.join(",");h[p]?a=h[p].id:(c.id=a="highcharts-"+zb++,h[p]=i=d.createElement(g).attr(c).add(d.defs),i.stops=[],n(j,function(a){f.test(a[1])?(e=qa(a[1]),k=e.get("rgb"),l=e.get("a")):(k=a[1],l=1);a=d.createElement("stop").attr({offset:a[0],"stop-color":k,"stop-opacity":l}).add(i);i.stops.push(a)}));return"url("+d.url+"#"+a+")"}else return f.test(a)?(e=qa(a),w(b,c+"-opacity",e.get("a")),e.get("rgb")):(b.removeAttribute(c+"-opacity"),a)},text:function(a,
b,c,d){var e=L.chart.style,f=ca||!W&&this.forExport;if(d&&!this.forExport)return this.html(a,b,c);b=t(o(b,0));c=t(o(c,0));a=this.createElement("text").attr({x:b,y:c,text:a}).css({fontFamily:e.fontFamily,fontSize:e.fontSize});f&&a.css({position:"absolute"});a.x=b;a.y=c;return a},html:function(a,b,c){var d=L.chart.style,e=this.createElement("span"),f=e.attrSetters,g=e.element,h=e.renderer;f.text=function(a){a!==g.innerHTML&&delete this.bBox;g.innerHTML=a;return!1};f.x=f.y=f.align=function(a,b){b===
"align"&&(b="textAlign");e[b]=a;e.htmlUpdateTransform();return!1};e.attr({text:a,x:t(b),y:t(c)}).css({position:"absolute",whiteSpace:"nowrap",fontFamily:d.fontFamily,fontSize:d.fontSize});e.css=e.htmlCss;if(h.isSVG)e.add=function(a){var b,c=h.box.parentNode,d=[];if(this.parentGroup=a){if(b=a.div,!b){for(;a;)d.push(a),a=a.parentGroup;n(d.reverse(),function(a){var d;b=a.div=a.div||U(Ea,{className:w(a.element,"class")},{position:"absolute",left:(a.translateX||0)+"px",top:(a.translateY||0)+"px"},b||c);
d=b.style;s(a.attrSetters,{translateX:function(a){d.left=a+"px"},translateY:function(a){d.top=a+"px"},visibility:function(a,b){d[b]=a}})})}}else b=c;b.appendChild(g);e.added=!0;e.alignOnAdd&&e.htmlUpdateTransform();return e};return e},fontMetrics:function(a){var a=y(a||11),a=a<24?a+4:t(a*1.2),b=t(a*0.8);return{h:a,b:b}},label:function(a,b,c,d,e,f,g,h,i){function j(){var a,b;a=o.element.style;O=(Oa===void 0||Ha===void 0||q.styles.textAlign)&&o.getBBox();q.width=(Oa||O.width||0)+2*da+lb;q.height=(Ha||
O.height||0)+2*da;w=da+p.fontMetrics(a&&a.fontSize).b;if(y){if(!B)a=t(-r*da),b=h?-w:0,q.box=B=d?p.symbol(d,a,b,q.width,q.height,Xa):p.rect(a,b,q.width,q.height,0,Xa[Rb]),B.add(q);B.isImg||B.attr(x({width:q.width,height:q.height},Xa));Xa=null}}function k(){var a=q.styles,a=a&&a.textAlign,b=lb+da*(1-r),c;c=h?0:w;if(u(Oa)&&(a==="center"||a==="right"))b+={center:0.5,right:1}[a]*(Oa-O.width);(b!==o.x||c!==o.y)&&o.attr({x:b,y:c});o.x=b;o.y=c}function l(a,b){B?B.attr(a,b):Xa[a]=b}function m(){o.add(q);q.attr({text:a,
x:b,y:c});B&&u(e)&&q.attr({anchorX:e,anchorY:f})}var p=this,q=p.g(i),o=p.text("",0,0,g).attr({zIndex:1}),B,O,r=0,da=3,lb=0,Oa,Ha,E,H,C=0,Xa={},w,g=q.attrSetters,y;K(q,"add",m);g.width=function(a){Oa=a;return!1};g.height=function(a){Ha=a;return!1};g.padding=function(a){u(a)&&a!==da&&(da=a,k());return!1};g.paddingLeft=function(a){u(a)&&a!==lb&&(lb=a,k());return!1};g.align=function(a){r={left:0,center:0.5,right:1}[a];return!1};g.text=function(a,b){o.attr(b,a);j();k();return!1};g[Rb]=function(a,b){y=
!0;C=a%2/2;l(b,a);return!1};g.stroke=g.fill=g.r=function(a,b){b==="fill"&&(y=!0);l(b,a);return!1};g.anchorX=function(a,b){e=a;l(b,a+C-E);return!1};g.anchorY=function(a,b){f=a;l(b,a-H);return!1};g.x=function(a){q.x=a;a-=r*((Oa||O.width)+da);E=t(a);q.attr("translateX",E);return!1};g.y=function(a){H=q.y=t(a);q.attr("translateY",H);return!1};var z=q.css;return s(q,{css:function(a){if(a){var b={},a=x(a);n("fontSize,fontWeight,fontFamily,color,lineHeight,width,textDecoration,textShadow".split(","),function(c){a[c]!==
v&&(b[c]=a[c],delete a[c])});o.css(b)}return z.call(q,a)},getBBox:function(){return{width:O.width+2*da,height:O.height+2*da,x:O.x-da,y:O.y-da}},shadow:function(a){B&&B.shadow(a);return q},destroy:function(){$(q,"add",m);$(q.element,"mouseenter");$(q.element,"mouseleave");o&&(o=o.destroy());B&&(B=B.destroy());va.prototype.destroy.call(q);q=p=j=k=l=m=null}})}};Va=za;var F;if(!W&&!ca){Highcharts.VMLElement=F={init:function(a,b){var c=["<",b,' filled="f" stroked="f"'],d=["position: ","absolute",";"],
e=b===Ea;(b==="shape"||e)&&d.push("left:0;top:0;width:1px;height:1px;");d.push("visibility: ",e?"hidden":"visible");c.push(' style="',d.join(""),'"/>');if(b)c=e||b==="span"||b==="img"?c.join(""):a.prepVML(c),this.element=U(c);this.renderer=a;this.attrSetters={}},add:function(a){var b=this.renderer,c=this.element,d=b.box,d=a?a.element||a:d;a&&a.inverted&&b.invertChild(c,d);d.appendChild(c);this.added=!0;this.alignOnAdd&&!this.deferUpdateTransform&&this.updateTransform();A(this,"add");return this},
updateTransform:va.prototype.htmlUpdateTransform,setSpanRotation:function(a,b,c){I(this.element,{filter:a?["progid:DXImageTransform.Microsoft.Matrix(M11=",c,", M12=",-b,", M21=",b,", M22=",c,", sizingMethod='auto expand')"].join(""):S})},pathToVML:function(a){for(var b=a.length,c=[],d;b--;)if(ra(a[b]))c[b]=t(a[b]*10)-5;else if(a[b]==="Z")c[b]="x";else if(c[b]=a[b],a.isArc&&(a[b]==="wa"||a[b]==="at"))d=a[b]==="wa"?1:-1,c[b+5]===c[b+7]&&(c[b+7]-=d),c[b+6]===c[b+8]&&(c[b+8]-=d);return c.join(" ")||"x"},
attr:function(a,b){var c,d,e,f=this.element||{},g=f.style,h=f.nodeName,i=this.renderer,j=this.symbolName,k,l=this.shadows,m,p=this.attrSetters,q=this;ea(a)&&u(b)&&(c=a,a={},a[c]=b);if(ea(a))c=a,q=c==="strokeWidth"||c==="stroke-width"?this.strokeweight:this[c];else for(c in a)if(d=a[c],m=!1,e=p[c]&&p[c].call(this,d,c),e!==!1&&d!==null){e!==v&&(d=e);if(j&&/^(x|y|r|start|end|width|height|innerR|anchorX|anchorY)/.test(c))k||(this.symbolAttr(a),k=!0),m=!0;else if(c==="d"){d=d||[];this.d=d.join(" ");f.path=
d=this.pathToVML(d);if(l)for(e=l.length;e--;)l[e].path=l[e].cutOff?this.cutOffPath(d,l[e].cutOff):d;m=!0}else if(c==="visibility"){if(l)for(e=l.length;e--;)l[e].style[c]=d;h==="DIV"&&(d=d==="hidden"?"-999em":0,gb||(g[c]=d?"visible":"hidden"),c="top");g[c]=d;m=!0}else if(c==="zIndex")d&&(g[c]=d),m=!0;else if(pa(c,["x","y","width","height"])!==-1)this[c]=d,c==="x"||c==="y"?c={x:"left",y:"top"}[c]:d=r(0,d),this.updateClipping?(this[c]=d,this.updateClipping()):g[c]=d,m=!0;else if(c==="class"&&h==="DIV")f.className=
d;else if(c==="stroke")d=i.color(d,f,c),c="strokecolor";else if(c==="stroke-width"||c==="strokeWidth")f.stroked=d?!0:!1,c="strokeweight",this[c]=d,ra(d)&&(d+="px");else if(c==="dashstyle")(f.getElementsByTagName("stroke")[0]||U(i.prepVML(["<stroke/>"]),null,null,f))[c]=d||"solid",this.dashstyle=d,m=!0;else if(c==="fill")if(h==="SPAN")g.color=d;else{if(h!=="IMG")f.filled=d!==S?!0:!1,d=i.color(d,f,c,this),c="fillcolor"}else if(c==="opacity")m=!0;else if(h==="shape"&&c==="rotation")this[c]=f.style[c]=
d,f.style.left=-t(ba(d*Ua)+1)+"px",f.style.top=t(V(d*Ua))+"px";else if(c==="translateX"||c==="translateY"||c==="rotation")this[c]=d,this.updateTransform(),m=!0;else if(c==="text")this.bBox=null,f.innerHTML=d,m=!0;m||(gb?f[c]=d:w(f,c,d))}return q},clip:function(a){var b=this,c;a?(c=a.members,ga(c,b),c.push(b),b.destroyClip=function(){ga(c,b)},a=a.getCSS(b)):(b.destroyClip&&b.destroyClip(),a={clip:gb?"inherit":"rect(auto)"});return b.css(a)},css:va.prototype.htmlCss,safeRemoveChild:function(a){a.parentNode&&
Ta(a)},destroy:function(){this.destroyClip&&this.destroyClip();return va.prototype.destroy.apply(this)},on:function(a,b){this.element["on"+a]=function(){var a=N.event;a.target=a.srcElement;b(a)};return this},cutOffPath:function(a,b){var c,a=a.split(/[ ,]/);c=a.length;if(c===9||c===11)a[c-4]=a[c-2]=y(a[c-2])-10*b;return a.join(" ")},shadow:function(a,b,c){var d=[],e,f=this.element,g=this.renderer,h,i=f.style,j,k=f.path,l,m,p,q;k&&typeof k.value!=="string"&&(k="x");m=k;if(a){p=o(a.width,3);q=(a.opacity||
0.15)/p;for(e=1;e<=3;e++){l=p*2+1-2*e;c&&(m=this.cutOffPath(k.value,l+0.5));j=['<shape isShadow="true" strokeweight="',l,'" filled="false" path="',m,'" coordsize="10 10" style="',f.style.cssText,'" />'];h=U(g.prepVML(j),null,{left:y(i.left)+o(a.offsetX,1),top:y(i.top)+o(a.offsetY,1)});if(c)h.cutOff=l+1;j=['<stroke color="',a.color||"black",'" opacity="',q*e,'"/>'];U(g.prepVML(j),null,null,h);b?b.element.appendChild(h):f.parentNode.insertBefore(h,f);d.push(h)}this.shadows=d}return this}};F=ha(va,F);
var la={Element:F,isIE8:na.indexOf("MSIE 8.0")>-1,init:function(a,b,c){var d,e;this.alignedObjects=[];d=this.createElement(Ea);e=d.element;e.style.position="relative";a.appendChild(d.element);this.isVML=!0;this.box=e;this.boxWrapper=d;this.setSize(b,c,!1);if(!z.namespaces.hcv){z.namespaces.add("hcv","urn:schemas-microsoft-com:vml");try{z.createStyleSheet().cssText="hcv\\:fill, hcv\\:path, hcv\\:shape, hcv\\:stroke{ behavior:url(#default#VML); display: inline-block; } "}catch(f){z.styleSheets[0].cssText+=
"hcv\\:fill, hcv\\:path, hcv\\:shape, hcv\\:stroke{ behavior:url(#default#VML); display: inline-block; } "}}},isHidden:function(){return!this.box.offsetWidth},clipRect:function(a,b,c,d){var e=this.createElement(),f=T(a);return s(e,{members:[],left:(f?a.x:a)+1,top:(f?a.y:b)+1,width:(f?a.width:c)-1,height:(f?a.height:d)-1,getCSS:function(a){var b=a.element,c=b.nodeName,a=a.inverted,d=this.top-(c==="shape"?b.offsetTop:0),e=this.left,b=e+this.width,f=d+this.height,d={clip:"rect("+t(a?e:d)+"px,"+t(a?f:
b)+"px,"+t(a?b:f)+"px,"+t(a?d:e)+"px)"};!a&&gb&&c==="DIV"&&s(d,{width:b+"px",height:f+"px"});return d},updateClipping:function(){n(e.members,function(a){a.css(e.getCSS(a))})}})},color:function(a,b,c,d){var e=this,f,g=/^rgba/,h,i,j=S;a&&a.linearGradient?i="gradient":a&&a.radialGradient&&(i="pattern");if(i){var k,l,m=a.linearGradient||a.radialGradient,p,q,o,B,O,r="",a=a.stops,u,t=[],v=function(){h=['<fill colors="'+t.join(",")+'" opacity="',o,'" o:opacity2="',q,'" type="',i,'" ',r,'focus="100%" method="any" />'];
U(e.prepVML(h),null,null,b)};p=a[0];u=a[a.length-1];p[0]>0&&a.unshift([0,p[1]]);u[0]<1&&a.push([1,u[1]]);n(a,function(a,b){g.test(a[1])?(f=qa(a[1]),k=f.get("rgb"),l=f.get("a")):(k=a[1],l=1);t.push(a[0]*100+"% "+k);b?(o=l,B=k):(q=l,O=k)});if(c==="fill")if(i==="gradient")c=m.x1||m[0]||0,a=m.y1||m[1]||0,p=m.x2||m[2]||0,m=m.y2||m[3]||0,r='angle="'+(90-R.atan((m-a)/(p-c))*180/xa)+'"',v();else{var j=m.r,s=j*2,E=j*2,H=m.cx,C=m.cy,x=b.radialReference,w,j=function(){x&&(w=d.getBBox(),H+=(x[0]-w.x)/w.width-
0.5,C+=(x[1]-w.y)/w.height-0.5,s*=x[2]/w.width,E*=x[2]/w.height);r='src="'+L.global.VMLRadialGradientURL+'" size="'+s+","+E+'" origin="0.5,0.5" position="'+H+","+C+'" color2="'+O+'" ';v()};d.added?j():K(d,"add",j);j=B}else j=k}else if(g.test(a)&&b.tagName!=="IMG")f=qa(a),h=["<",c,' opacity="',f.get("a"),'"/>'],U(this.prepVML(h),null,null,b),j=f.get("rgb");else{j=b.getElementsByTagName(c);if(j.length)j[0].opacity=1,j[0].type="solid";j=a}return j},prepVML:function(a){var b=this.isIE8,a=a.join("");b?
(a=a.replace("/>",' xmlns="urn:schemas-microsoft-com:vml" />'),a=a.indexOf('style="')===-1?a.replace("/>",' style="display:inline-block;behavior:url(#default#VML);" />'):a.replace('style="','style="display:inline-block;behavior:url(#default#VML);')):a=a.replace("<","<hcv:");return a},text:za.prototype.html,path:function(a){var b={coordsize:"10 10"};Ia(a)?b.d=a:T(a)&&s(b,a);return this.createElement("shape").attr(b)},circle:function(a,b,c){var d=this.symbol("circle");if(T(a))c=a.r,b=a.y,a=a.x;d.isCircle=
!0;d.r=c;return d.attr({x:a,y:b})},g:function(a){var b;a&&(b={className:"highcharts-"+a,"class":"highcharts-"+a});return this.createElement(Ea).attr(b)},image:function(a,b,c,d,e){var f=this.createElement("img").attr({src:a});arguments.length>1&&f.attr({x:b,y:c,width:d,height:e});return f},rect:function(a,b,c,d,e,f){var g=this.symbol("rect");g.r=T(a)?a.r:e;return g.attr(T(a)?a:g.crisp(f,a,b,r(c,0),r(d,0)))},invertChild:function(a,b){var c=b.style;I(a,{flip:"x",left:y(c.width)-1,top:y(c.height)-1,rotation:-90})},
symbols:{arc:function(a,b,c,d,e){var f=e.start,g=e.end,h=e.r||c||d,c=e.innerR,d=V(f),i=ba(f),j=V(g),k=ba(g);if(g-f===0)return["x"];f=["wa",a-h,b-h,a+h,b+h,a+h*d,b+h*i,a+h*j,b+h*k];e.open&&!c&&f.push("e","M",a,b);f.push("at",a-c,b-c,a+c,b+c,a+c*j,b+c*k,a+c*d,b+c*i,"x","e");f.isArc=!0;return f},circle:function(a,b,c,d,e){e&&(c=d=2*e.r);e&&e.isCircle&&(a-=c/2,b-=d/2);return["wa",a,b,a+c,b+d,a+c,b+d/2,a+c,b+d/2,"e"]},rect:function(a,b,c,d,e){var f=a+c,g=b+d,h;!u(e)||!e.r?f=za.prototype.symbols.square.apply(0,
arguments):(h=J(e.r,c,d),f=["M",a+h,b,"L",f-h,b,"wa",f-2*h,b,f,b+2*h,f-h,b,f,b+h,"L",f,g-h,"wa",f-2*h,g-2*h,f,g,f,g-h,f-h,g,"L",a+h,g,"wa",a,g-2*h,a+2*h,g,a+h,g,a,g-h,"L",a,b+h,"wa",a,b,a+2*h,b+2*h,a,b+h,a+h,b,"x","e"]);return f}}};Highcharts.VMLRenderer=F=function(){this.init.apply(this,arguments)};F.prototype=x(za.prototype,la);Va=F}za.prototype.measureSpanWidth=function(a,b){var c=z.createElement("span"),d=z.createTextNode(a);c.appendChild(d);I(c,b);this.box.appendChild(c);return c.offsetWidth};
var Tb;if(ca)Highcharts.CanVGRenderer=F=function(){ya="http://www.w3.org/1999/xhtml"},F.prototype.symbols={},Tb=function(){function a(){var a=b.length,d;for(d=0;d<a;d++)b[d]();b=[]}var b=[];return{push:function(c,d){b.length===0&&Vb(d,a);b.push(c)}}}(),Va=F;Ma.prototype={addLabel:function(){var a=this.axis,b=a.options,c=a.chart,d=a.horiz,e=a.categories,f=a.names,g=this.pos,h=b.labels,i=a.tickPositions,d=d&&e&&!h.step&&!h.staggerLines&&!h.rotation&&c.plotWidth/i.length||!d&&(c.margin[3]||c.chartWidth*
0.33),j=g===i[0],k=g===i[i.length-1],l,f=e?o(e[g],f[g],g):g,e=this.label,m=i.info;a.isDatetimeAxis&&m&&(l=b.dateTimeLabelFormats[m.higherRanks[g]||m.unitName]);this.isFirst=j;this.isLast=k;b=a.labelFormatter.call({axis:a,chart:c,isFirst:j,isLast:k,dateTimeLabelFormat:l,value:a.isLog?ia(fa(f)):f});g=d&&{width:r(1,t(d-2*(h.padding||10)))+"px"};g=s(g,h.style);if(u(e))e&&e.attr({text:b}).css(g);else{l={align:a.labelAlign};if(ra(h.rotation))l.rotation=h.rotation;if(d&&h.ellipsis)l._clipHeight=a.len/i.length;
this.label=u(b)&&h.enabled?c.renderer.text(b,0,0,h.useHTML).attr(l).css(g).add(a.labelGroup):null}},getLabelSize:function(){var a=this.label,b=this.axis;return a?(this.labelBBox=a.getBBox())[b.horiz?"height":"width"]:0},getLabelSides:function(){var a=this.axis,b=this.labelBBox.width,a=b*{left:0,center:0.5,right:1}[a.labelAlign]-a.options.labels.x;return[-a,b-a]},handleOverflow:function(a,b){var c=!0,d=this.axis,e=d.chart,f=this.isFirst,g=this.isLast,h=b.x,i=d.reversed,j=d.tickPositions;if(f||g){var k=
this.getLabelSides(),l=k[0],k=k[1],e=e.plotLeft,m=e+d.len,j=(d=d.ticks[j[a+(f?1:-1)]])&&d.label.xy&&d.label.xy.x+d.getLabelSides()[f?0:1];f&&!i||g&&i?h+l<e&&(h=e-l,d&&h+k>j&&(c=!1)):h+k>m&&(h=m-k,d&&h+l<j&&(c=!1));b.x=h}return c},getPosition:function(a,b,c,d){var e=this.axis,f=e.chart,g=d&&f.oldChartHeight||f.chartHeight;return{x:a?e.translate(b+c,null,null,d)+e.transB:e.left+e.offset+(e.opposite?(d&&f.oldChartWidth||f.chartWidth)-e.right-e.left:0),y:a?g-e.bottom+e.offset-(e.opposite?e.height:0):
g-e.translate(b+c,null,null,d)-e.transB}},getLabelPosition:function(a,b,c,d,e,f,g,h){var i=this.axis,j=i.transA,k=i.reversed,l=i.staggerLines,m=i.chart.renderer.fontMetrics(e.style.fontSize).b,p=e.rotation,a=a+e.x-(f&&d?f*j*(k?-1:1):0),b=b+e.y-(f&&!d?f*j*(k?1:-1):0);p&&i.side===2&&(b-=m-m*V(p*Ua));!u(e.y)&&!p&&(b+=m-c.getBBox().height/2);l&&(b+=g/(h||1)%l*(i.labelOffset/l));return{x:a,y:b}},getMarkPath:function(a,b,c,d,e,f){return f.crispLine(["M",a,b,"L",a+(e?0:-c),b+(e?c:0)],d)},render:function(a,
b,c){var d=this.axis,e=d.options,f=d.chart.renderer,g=d.horiz,h=this.type,i=this.label,j=this.pos,k=e.labels,l=this.gridLine,m=h?h+"Grid":"grid",p=h?h+"Tick":"tick",q=e[m+"LineWidth"],n=e[m+"LineColor"],B=e[m+"LineDashStyle"],r=e[p+"Length"],m=e[p+"Width"]||0,u=e[p+"Color"],t=e[p+"Position"],p=this.mark,s=k.step,w=!0,x=d.tickmarkOffset,E=this.getPosition(g,j,x,b),H=E.x,E=E.y,C=g&&H===d.pos+d.len||!g&&E===d.pos?-1:1,y=d.staggerLines;this.isActive=!0;if(q){j=d.getPlotLinePath(j+x,q*C,b,!0);if(l===v){l=
{stroke:n,"stroke-width":q};if(B)l.dashstyle=B;if(!h)l.zIndex=1;if(b)l.opacity=0;this.gridLine=l=q?f.path(j).attr(l).add(d.gridGroup):null}if(!b&&l&&j)l[this.isNew?"attr":"animate"]({d:j,opacity:c})}if(m&&r)t==="inside"&&(r=-r),d.opposite&&(r=-r),b=this.getMarkPath(H,E,r,m*C,g,f),p?p.animate({d:b,opacity:c}):this.mark=f.path(b).attr({stroke:u,"stroke-width":m,opacity:c}).add(d.axisGroup);if(i&&!isNaN(H))i.xy=E=this.getLabelPosition(H,E,i,g,k,x,a,s),this.isFirst&&!this.isLast&&!o(e.showFirstLabel,
1)||this.isLast&&!this.isFirst&&!o(e.showLastLabel,1)?w=!1:!y&&g&&k.overflow==="justify"&&!this.handleOverflow(a,E)&&(w=!1),s&&a%s&&(w=!1),w&&!isNaN(E.y)?(E.opacity=c,i[this.isNew?"attr":"animate"](E),this.isNew=!1):i.attr("y",-9999)},destroy:function(){Ka(this,this.axis)}};vb.prototype={render:function(){var a=this,b=a.axis,c=b.horiz,d=(b.pointRange||0)/2,e=a.options,f=e.label,g=a.label,h=e.width,i=e.to,j=e.from,k=u(j)&&u(i),l=e.value,m=e.dashStyle,p=a.svgElem,q=[],n,B=e.color,O=e.zIndex,t=e.events,
v=b.chart.renderer;b.isLog&&(j=ma(j),i=ma(i),l=ma(l));if(h){if(q=b.getPlotLinePath(l,h),d={stroke:B,"stroke-width":h},m)d.dashstyle=m}else if(k){if(j=r(j,b.min-d),i=J(i,b.max+d),q=b.getPlotBandPath(j,i,e),d={fill:B},e.borderWidth)d.stroke=e.borderColor,d["stroke-width"]=e.borderWidth}else return;if(u(O))d.zIndex=O;if(p)if(q)p.animate({d:q},null,p.onGetPath);else{if(p.hide(),p.onGetPath=function(){p.show()},g)a.label=g=g.destroy()}else if(q&&q.length&&(a.svgElem=p=v.path(q).attr(d).add(),t))for(n in e=
function(b){p.on(b,function(c){t[b].apply(a,[c])})},t)e(n);if(f&&u(f.text)&&q&&q.length&&b.width>0&&b.height>0){f=x({align:c&&k&&"center",x:c?!k&&4:10,verticalAlign:!c&&k&&"middle",y:c?k?16:10:k?6:-4,rotation:c&&!k&&90},f);if(!g)a.label=g=v.text(f.text,0,0,f.useHTML).attr({align:f.textAlign||f.align,rotation:f.rotation,zIndex:O}).css(f.style).add();b=[q[1],q[4],o(q[6],q[1])];q=[q[2],q[5],o(q[7],q[2])];c=Ja(b);k=Ja(q);g.align(f,!1,{x:c,y:k,width:ua(b)-c,height:ua(q)-k});g.show()}else g&&g.hide();return a},
destroy:function(){ga(this.axis.plotLinesAndBands,this);delete this.axis;Ka(this)}};Mb.prototype={destroy:function(){Ka(this,this.axis)},render:function(a){var b=this.options,c=b.format,c=c?Ca(c,this):b.formatter.call(this);this.label?this.label.attr({text:c,visibility:"hidden"}):this.label=this.axis.chart.renderer.text(c,0,0,b.useHTML).css(b.style).attr({align:this.textAlign,rotation:b.rotation,visibility:"hidden"}).add(a)},setOffset:function(a,b){var c=this.axis,d=c.chart,e=d.inverted,f=this.isNegative,
g=c.translate(this.percent?100:this.total,0,0,0,1),c=c.translate(0),c=M(g-c),h=d.xAxis[0].translate(this.x)+a,i=d.plotHeight,f={x:e?f?g:g-c:h,y:e?i-h-b:f?i-g-c:i-g,width:e?c:b,height:e?b:c};if(e=this.label)e.align(this.alignOptions,null,f),f=e.alignAttr,e.attr({visibility:this.options.crop===!1||d.isInsidePlot(f.x,f.y)?W?"inherit":"visible":"hidden"})}};eb.prototype={defaultOptions:{dateTimeLabelFormats:{millisecond:"%H:%M:%S.%L",second:"%H:%M:%S",minute:"%H:%M",hour:"%H:%M",day:"%e. %b",week:"%e. %b",
month:"%b '%y",year:"%Y"},endOnTick:!1,gridLineColor:"#C0C0C0",labels:G,lineColor:"#C0D0E0",lineWidth:1,minPadding:0.01,maxPadding:0.01,minorGridLineColor:"#E0E0E0",minorGridLineWidth:1,minorTickColor:"#A0A0A0",minorTickLength:2,minorTickPosition:"outside",startOfWeek:1,startOnTick:!1,tickColor:"#C0D0E0",tickLength:5,tickmarkPlacement:"between",tickPixelInterval:100,tickPosition:"outside",tickWidth:1,title:{align:"middle",style:{color:"#4d759e",fontWeight:"bold"}},type:"linear"},defaultYAxisOptions:{endOnTick:!0,
gridLineWidth:1,tickPixelInterval:72,showLastLabel:!0,labels:{x:-8,y:3},lineWidth:0,maxPadding:0.05,minPadding:0.05,startOnTick:!0,tickWidth:0,title:{rotation:270,text:"Values"},stackLabels:{enabled:!1,formatter:function(){return Aa(this.total,-1)},style:G.style}},defaultLeftAxisOptions:{labels:{x:-8,y:null},title:{rotation:270}},defaultRightAxisOptions:{labels:{x:8,y:null},title:{rotation:90}},defaultBottomAxisOptions:{labels:{x:0,y:14},title:{rotation:0}},defaultTopAxisOptions:{labels:{x:0,y:-5},
title:{rotation:0}},init:function(a,b){var c=b.isX;this.horiz=a.inverted?!c:c;this.xOrY=(this.isXAxis=c)?"x":"y";this.opposite=b.opposite;this.side=this.horiz?this.opposite?0:2:this.opposite?1:3;this.setOptions(b);var d=this.options,e=d.type;this.labelFormatter=d.labels.formatter||this.defaultLabelFormatter;this.userOptions=b;this.minPixelPadding=0;this.chart=a;this.reversed=d.reversed;this.zoomEnabled=d.zoomEnabled!==!1;this.categories=d.categories||e==="category";this.names=[];this.isLog=e==="logarithmic";
this.isDatetimeAxis=e==="datetime";this.isLinked=u(d.linkedTo);this.tickmarkOffset=this.categories&&d.tickmarkPlacement==="between"?0.5:0;this.ticks={};this.minorTicks={};this.plotLinesAndBands=[];this.alternateBands={};this.len=0;this.minRange=this.userMinRange=d.minRange||d.maxZoom;this.range=d.range;this.offset=d.offset||0;this.stacks={};this.oldStacks={};this.stackExtremes={};this.min=this.max=null;var f,d=this.options.events;pa(this,a.axes)===-1&&(a.axes.push(this),a[c?"xAxis":"yAxis"].push(this));
this.series=this.series||[];if(a.inverted&&c&&this.reversed===v)this.reversed=!0;this.removePlotLine=this.removePlotBand=this.removePlotBandOrLine;for(f in d)K(this,f,d[f]);if(this.isLog)this.val2lin=ma,this.lin2val=fa},setOptions:function(a){this.options=x(this.defaultOptions,this.isXAxis?{}:this.defaultYAxisOptions,[this.defaultTopAxisOptions,this.defaultRightAxisOptions,this.defaultBottomAxisOptions,this.defaultLeftAxisOptions][this.side],x(L[this.isXAxis?"xAxis":"yAxis"],a))},update:function(a,
b){var c=this.chart,a=c.options[this.xOrY+"Axis"][this.options.index]=x(this.userOptions,a);this.destroy(!0);this._addedPlotLB=this.userMin=this.userMax=v;this.init(c,s(a,{events:v}));c.isDirtyBox=!0;o(b,!0)&&c.redraw()},remove:function(a){var b=this.chart,c=this.xOrY+"Axis";n(this.series,function(a){a.remove(!1)});ga(b.axes,this);ga(b[c],this);b.options[c].splice(this.options.index,1);n(b[c],function(a,b){a.options.index=b});this.destroy();b.isDirtyBox=!0;o(a,!0)&&b.redraw()},defaultLabelFormatter:function(){var a=
this.axis,b=this.value,c=a.categories,d=this.dateTimeLabelFormat,e=L.lang.numericSymbols,f=e&&e.length,g,h=a.options.labels.format,a=a.isLog?b:a.tickInterval;if(h)g=Ca(h,this);else if(c)g=b;else if(d)g=Ya(d,b);else if(f&&a>=1E3)for(;f--&&g===v;)c=Math.pow(1E3,f+1),a>=c&&e[f]!==null&&(g=Aa(b/c,-1)+e[f]);g===v&&(g=b>=1E3?Aa(b,0):Aa(b,-1));return g},getSeriesExtremes:function(){var a=this,b=a.chart;a.hasVisibleSeries=!1;a.dataMin=a.dataMax=null;a.stackExtremes={};a.buildStacks();n(a.series,function(c){if(c.visible||
!b.options.chart.ignoreHiddenSeries){var d;d=c.options.threshold;var e;a.hasVisibleSeries=!0;a.isLog&&d<=0&&(d=null);if(a.isXAxis){if(d=c.xData,d.length)a.dataMin=J(o(a.dataMin,d[0]),Ja(d)),a.dataMax=r(o(a.dataMax,d[0]),ua(d))}else{c.getExtremes();e=c.dataMax;c=c.dataMin;if(u(c)&&u(e))a.dataMin=J(o(a.dataMin,c),c),a.dataMax=r(o(a.dataMax,e),e);if(u(d))if(a.dataMin>=d)a.dataMin=d,a.ignoreMinPadding=!0;else if(a.dataMax<d)a.dataMax=d,a.ignoreMaxPadding=!0}}})},translate:function(a,b,c,d,e,f){var g=
this.len,h=1,i=0,j=d?this.oldTransA:this.transA,d=d?this.oldMin:this.min,k=this.minPixelPadding,e=(this.options.ordinal||this.isLog&&e)&&this.lin2val;if(!j)j=this.transA;c&&(h*=-1,i=g);this.reversed&&(h*=-1,i-=h*g);b?(a=a*h+i,a-=k,a=a/j+d,e&&(a=this.lin2val(a))):(e&&(a=this.val2lin(a)),f==="between"&&(f=0.5),a=h*(a-d)*j+i+h*k+(ra(f)?j*f*this.pointRange:0));return a},toPixels:function(a,b){return this.translate(a,!1,!this.horiz,null,!0)+(b?0:this.pos)},toValue:function(a,b){return this.translate(a-
(b?0:this.pos),!0,!this.horiz,null,!0)},getPlotLinePath:function(a,b,c,d){var e=this.chart,f=this.left,g=this.top,h,i,j,a=this.translate(a,null,null,c),k=c&&e.oldChartHeight||e.chartHeight,l=c&&e.oldChartWidth||e.chartWidth,m;h=this.transB;c=i=t(a+h);h=j=t(k-a-h);if(isNaN(a))m=!0;else if(this.horiz){if(h=g,j=k-this.bottom,c<f||c>f+this.width)m=!0}else if(c=f,i=l-this.right,h<g||h>g+this.height)m=!0;return m&&!d?null:e.renderer.crispLine(["M",c,h,"L",i,j],b||0)},getPlotBandPath:function(a,b){var c=
this.getPlotLinePath(b),d=this.getPlotLinePath(a);d&&c?d.push(c[4],c[5],c[1],c[2]):d=null;return d},getLinearTickPositions:function(a,b,c){for(var d,b=ia(P(b/a)*a),c=ia(wa(c/a)*a),e=[];b<=c;){e.push(b);b=ia(b+a);if(b===d)break;d=b}return e},getLogTickPositions:function(a,b,c,d){var e=this.options,f=this.len,g=[];if(!d)this._minorAutoInterval=null;if(a>=0.5)a=t(a),g=this.getLinearTickPositions(a,b,c);else if(a>=0.08)for(var f=P(b),h,i,j,k,l,e=a>0.3?[1,2,4]:a>0.15?[1,2,4,6,8]:[1,2,3,4,5,6,7,8,9];f<
c+1&&!l;f++){i=e.length;for(h=0;h<i&&!l;h++)j=ma(fa(f)*e[h]),j>b&&(!d||k<=c)&&g.push(k),k>c&&(l=!0),k=j}else if(b=fa(b),c=fa(c),a=e[d?"minorTickInterval":"tickInterval"],a=o(a==="auto"?null:a,this._minorAutoInterval,(c-b)*(e.tickPixelInterval/(d?5:1))/((d?f/this.tickPositions.length:f)||1)),a=ob(a,null,nb(a)),g=Na(this.getLinearTickPositions(a,b,c),ma),!d)this._minorAutoInterval=a/5;if(!d)this.tickInterval=a;return g},getMinorTickPositions:function(){var a=this.options,b=this.tickPositions,c=this.minorTickInterval,
d=[],e;if(this.isLog){e=b.length;for(a=1;a<e;a++)d=d.concat(this.getLogTickPositions(c,b[a-1],b[a],!0))}else if(this.isDatetimeAxis&&a.minorTickInterval==="auto")d=d.concat(Eb(Cb(c),this.min,this.max,a.startOfWeek)),d[0]<this.min&&d.shift();else for(b=this.min+(b[0]-this.min)%c;b<=this.max;b+=c)d.push(b);return d},adjustForMinRange:function(){var a=this.options,b=this.min,c=this.max,d,e=this.dataMax-this.dataMin>=this.minRange,f,g,h,i,j;if(this.isXAxis&&this.minRange===v&&!this.isLog)u(a.min)||u(a.max)?
this.minRange=null:(n(this.series,function(a){i=a.xData;for(g=j=a.xIncrement?1:i.length-1;g>0;g--)if(h=i[g]-i[g-1],f===v||h<f)f=h}),this.minRange=J(f*5,this.dataMax-this.dataMin));if(c-b<this.minRange){var k=this.minRange;d=(k-c+b)/2;d=[b-d,o(a.min,b-d)];if(e)d[2]=this.dataMin;b=ua(d);c=[b+k,o(a.max,b+k)];if(e)c[2]=this.dataMax;c=Ja(c);c-b<k&&(d[0]=c-k,d[1]=o(a.min,c-k),b=ua(d))}this.min=b;this.max=c},setAxisTranslation:function(a){var b=this.max-this.min,c=0,d,e=0,f=0,g=this.linkedParent,h=this.transA;
if(this.isXAxis)g?(e=g.minPointOffset,f=g.pointRangePadding):n(this.series,function(a){var g=a.pointRange,h=a.options.pointPlacement,l=a.closestPointRange;g>b&&(g=0);c=r(c,g);e=r(e,ea(h)?0:g/2);f=r(f,h==="on"?0:g);!a.noSharedTooltip&&u(l)&&(d=u(d)?J(d,l):l)}),g=this.ordinalSlope&&d?this.ordinalSlope/d:1,this.minPointOffset=e*=g,this.pointRangePadding=f*=g,this.pointRange=J(c,b),this.closestPointRange=d;if(a)this.oldTransA=h;this.translationSlope=this.transA=h=this.len/(b+f||1);this.transB=this.horiz?
this.left:this.bottom;this.minPixelPadding=h*e},setTickPositions:function(a){var b=this,c=b.chart,d=b.options,e=b.isLog,f=b.isDatetimeAxis,g=b.isXAxis,h=b.isLinked,i=b.options.tickPositioner,j=d.maxPadding,k=d.minPadding,l=d.tickInterval,m=d.minTickInterval,p=d.tickPixelInterval,q,aa=b.categories;h?(b.linkedParent=c[g?"xAxis":"yAxis"][d.linkedTo],c=b.linkedParent.getExtremes(),b.min=o(c.min,c.dataMin),b.max=o(c.max,c.dataMax),d.type!==b.linkedParent.options.type&&ka(11,1)):(b.min=o(b.userMin,d.min,
b.dataMin),b.max=o(b.userMax,d.max,b.dataMax));if(e)!a&&J(b.min,o(b.dataMin,b.min))<=0&&ka(10,1),b.min=ia(ma(b.min)),b.max=ia(ma(b.max));if(b.range&&(b.userMin=b.min=r(b.min,b.max-b.range),b.userMax=b.max,a))b.range=null;b.beforePadding&&b.beforePadding();b.adjustForMinRange();if(!aa&&!b.usePercentage&&!h&&u(b.min)&&u(b.max)&&(c=b.max-b.min)){if(!u(d.min)&&!u(b.userMin)&&k&&(b.dataMin<0||!b.ignoreMinPadding))b.min-=c*k;if(!u(d.max)&&!u(b.userMax)&&j&&(b.dataMax>0||!b.ignoreMaxPadding))b.max+=c*j}b.min===
b.max||b.min===void 0||b.max===void 0?b.tickInterval=1:h&&!l&&p===b.linkedParent.options.tickPixelInterval?b.tickInterval=b.linkedParent.tickInterval:(b.tickInterval=o(l,aa?1:(b.max-b.min)*p/r(b.len,p)),!u(l)&&b.len<p&&!this.isRadial&&(q=!0,b.tickInterval/=4));g&&!a&&n(b.series,function(a){a.processData(b.min!==b.oldMin||b.max!==b.oldMax)});b.setAxisTranslation(!0);b.beforeSetTickPositions&&b.beforeSetTickPositions();if(b.postProcessTickInterval)b.tickInterval=b.postProcessTickInterval(b.tickInterval);
if(b.pointRange)b.tickInterval=r(b.pointRange,b.tickInterval);if(!l&&b.tickInterval<m)b.tickInterval=m;if(!f&&!e&&!l)b.tickInterval=ob(b.tickInterval,null,nb(b.tickInterval),d);b.minorTickInterval=d.minorTickInterval==="auto"&&b.tickInterval?b.tickInterval/5:d.minorTickInterval;b.tickPositions=a=d.tickPositions?[].concat(d.tickPositions):i&&i.apply(b,[b.min,b.max]);if(!a)!b.ordinalPositions&&(b.max-b.min)/b.tickInterval>r(2*b.len,200)&&ka(19,!0),a=f?(b.getNonLinearTimeTicks||Eb)(Cb(b.tickInterval,
d.units),b.min,b.max,d.startOfWeek,b.ordinalPositions,b.closestPointRange,!0):e?b.getLogTickPositions(b.tickInterval,b.min,b.max):b.getLinearTickPositions(b.tickInterval,b.min,b.max),q&&a.splice(1,a.length-2),b.tickPositions=a;if(!h)e=a[0],f=a[a.length-1],h=b.minPointOffset||0,d.startOnTick?b.min=e:b.min-h>e&&a.shift(),d.endOnTick?b.max=f:b.max+h<f&&a.pop(),a.length===1&&(b.min-=0.001,b.max+=0.001)},setMaxTicks:function(){var a=this.chart,b=a.maxTicks||{},c=this.tickPositions,d=this._maxTicksKey=
[this.xOrY,this.pos,this.len].join("-");if(!this.isLinked&&!this.isDatetimeAxis&&c&&c.length>(b[d]||0)&&this.options.alignTicks!==!1)b[d]=c.length;a.maxTicks=b},adjustTickAmount:function(){var a=this._maxTicksKey,b=this.tickPositions,c=this.chart.maxTicks;if(c&&c[a]&&!this.isDatetimeAxis&&!this.categories&&!this.isLinked&&this.options.alignTicks!==!1){var d=this.tickAmount,e=b.length;this.tickAmount=a=c[a];if(e<a){for(;b.length<a;)b.push(ia(b[b.length-1]+this.tickInterval));this.transA*=(e-1)/(a-
1);this.max=b[b.length-1]}if(u(d)&&a!==d)this.isDirty=!0}},setScale:function(){var a=this.stacks,b,c,d,e;this.oldMin=this.min;this.oldMax=this.max;this.oldAxisLength=this.len;this.setAxisSize();e=this.len!==this.oldAxisLength;n(this.series,function(a){if(a.isDirtyData||a.isDirty||a.xAxis.isDirty)d=!0});if(e||d||this.isLinked||this.forceRedraw||this.userMin!==this.oldUserMin||this.userMax!==this.oldUserMax){if(!this.isXAxis)for(b in a)for(c in a[b])a[b][c].total=null,a[b][c].cum=0;this.forceRedraw=
!1;this.getSeriesExtremes();this.setTickPositions();this.oldUserMin=this.userMin;this.oldUserMax=this.userMax;if(!this.isDirty)this.isDirty=e||this.min!==this.oldMin||this.max!==this.oldMax}else if(!this.isXAxis){if(this.oldStacks)a=this.stacks=this.oldStacks;for(b in a)for(c in a[b])a[b][c].cum=a[b][c].total}this.setMaxTicks()},setExtremes:function(a,b,c,d,e){var f=this,g=f.chart,c=o(c,!0),e=s(e,{min:a,max:b});A(f,"setExtremes",e,function(){f.userMin=a;f.userMax=b;f.eventArgs=e;f.isDirtyExtremes=
!0;c&&g.redraw(d)})},zoom:function(a,b){this.allowZoomOutside||(u(this.dataMin)&&a<=this.dataMin&&(a=v),u(this.dataMax)&&b>=this.dataMax&&(b=v));this.displayBtn=a!==v||b!==v;this.setExtremes(a,b,!1,v,{trigger:"zoom"});return!0},setAxisSize:function(){var a=this.chart,b=this.options,c=b.offsetLeft||0,d=b.offsetRight||0,e=this.horiz,f,g;this.left=g=o(b.left,a.plotLeft+c);this.top=f=o(b.top,a.plotTop);this.width=c=o(b.width,a.plotWidth-c+d);this.height=b=o(b.height,a.plotHeight);this.bottom=a.chartHeight-
b-f;this.right=a.chartWidth-c-g;this.len=r(e?c:b,0);this.pos=e?g:f},getExtremes:function(){var a=this.isLog;return{min:a?ia(fa(this.min)):this.min,max:a?ia(fa(this.max)):this.max,dataMin:this.dataMin,dataMax:this.dataMax,userMin:this.userMin,userMax:this.userMax}},getThreshold:function(a){var b=this.isLog,c=b?fa(this.min):this.min,b=b?fa(this.max):this.max;c>a||a===null?a=c:b<a&&(a=b);return this.translate(a,0,1,0,1)},addPlotBand:function(a){this.addPlotBandOrLine(a,"plotBands")},addPlotLine:function(a){this.addPlotBandOrLine(a,
"plotLines")},addPlotBandOrLine:function(a,b){var c=(new vb(this,a)).render(),d=this.userOptions;c&&(b&&(d[b]=d[b]||[],d[b].push(a)),this.plotLinesAndBands.push(c));return c},autoLabelAlign:function(a){a=(o(a,0)-this.side*90+720)%360;return a>15&&a<165?"right":a>195&&a<345?"left":"center"},getOffset:function(){var a=this,b=a.chart,c=b.renderer,d=a.options,e=a.tickPositions,f=a.ticks,g=a.horiz,h=a.side,i=b.inverted?[1,0,3,2][h]:h,j,k=0,l,m=0,p=d.title,q=d.labels,aa=0,B=b.axisOffset,t=b.clipOffset,
s=[-1,1,1,-1][h],w,x=1,y=o(q.maxStaggerLines,5),Ha,E,H,C;a.hasData=j=a.hasVisibleSeries||u(a.min)&&u(a.max)&&!!e;a.showAxis=b=j||o(d.showEmpty,!0);a.staggerLines=a.horiz&&q.staggerLines;if(!a.axisGroup)a.gridGroup=c.g("grid").attr({zIndex:d.gridZIndex||1}).add(),a.axisGroup=c.g("axis").attr({zIndex:d.zIndex||2}).add(),a.labelGroup=c.g("axis-labels").attr({zIndex:q.zIndex||7}).add();if(j||a.isLinked){a.labelAlign=o(q.align||a.autoLabelAlign(q.rotation));n(e,function(b){f[b]?f[b].addLabel():f[b]=new Ma(a,
b)});if(a.horiz&&!a.staggerLines&&y&&!q.rotation){for(w=a.reversed?[].concat(e).reverse():e;x<y;){j=[];Ha=!1;for(q=0;q<w.length;q++)E=w[q],H=(H=f[E].label&&f[E].label.getBBox())?H.width:0,C=q%x,H&&(E=a.translate(E),j[C]!==v&&E<j[C]&&(Ha=!0),j[C]=E+H);if(Ha)x++;else break}if(x>1)a.staggerLines=x}n(e,function(b){if(h===0||h===2||{1:"left",3:"right"}[h]===a.labelAlign)aa=r(f[b].getLabelSize(),aa)});if(a.staggerLines)aa*=a.staggerLines,a.labelOffset=aa}else for(w in f)f[w].destroy(),delete f[w];if(p&&
p.text&&p.enabled!==!1){if(!a.axisTitle)a.axisTitle=c.text(p.text,0,0,p.useHTML).attr({zIndex:7,rotation:p.rotation||0,align:p.textAlign||{low:"left",middle:"center",high:"right"}[p.align]}).css(p.style).add(a.axisGroup),a.axisTitle.isNew=!0;if(b)k=a.axisTitle.getBBox()[g?"height":"width"],m=o(p.margin,g?5:10),l=p.offset;a.axisTitle[b?"show":"hide"]()}a.offset=s*o(d.offset,B[h]);a.axisTitleMargin=o(l,aa+m+(h!==2&&aa&&s*d.labels[g?"y":"x"]));B[h]=r(B[h],a.axisTitleMargin+k+s*a.offset);t[i]=r(t[i],
P(d.lineWidth/2)*2)},getLinePath:function(a){var b=this.chart,c=this.opposite,d=this.offset,e=this.horiz,f=this.left+(c?this.width:0)+d,d=b.chartHeight-this.bottom-(c?this.height:0)+d;c&&(a*=-1);return b.renderer.crispLine(["M",e?this.left:f,e?d:this.top,"L",e?b.chartWidth-this.right:f,e?d:b.chartHeight-this.bottom],a)},getTitlePosition:function(){var a=this.horiz,b=this.left,c=this.top,d=this.len,e=this.options.title,f=a?b:c,g=this.opposite,h=this.offset,i=y(e.style.fontSize||12),d={low:f+(a?0:d),
middle:f+d/2,high:f+(a?d:0)}[e.align],b=(a?c+this.height:b)+(a?1:-1)*(g?-1:1)*this.axisTitleMargin+(this.side===2?i:0);return{x:a?d:b+(g?this.width:0)+h+(e.x||0),y:a?b-(g?this.height:0)+h:d+(e.y||0)}},render:function(){var a=this,b=a.chart,c=b.renderer,d=a.options,e=a.isLog,f=a.isLinked,g=a.tickPositions,h=a.axisTitle,i=a.stacks,j=a.ticks,k=a.minorTicks,l=a.alternateBands,m=d.stackLabels,p=d.alternateGridColor,q=a.tickmarkOffset,o=d.lineWidth,B,r=b.hasRendered&&u(a.oldMin)&&!isNaN(a.oldMin);B=a.hasData;
var t=a.showAxis,s,w;n([j,k,l],function(a){for(var b in a)a[b].isActive=!1});if(B||f)if(a.minorTickInterval&&!a.categories&&n(a.getMinorTickPositions(),function(b){k[b]||(k[b]=new Ma(a,b,"minor"));r&&k[b].isNew&&k[b].render(null,!0);k[b].render(null,!1,1)}),g.length&&(n(g.slice(1).concat([g[0]]),function(b,c){c=c===g.length-1?0:c+1;if(!f||b>=a.min&&b<=a.max)j[b]||(j[b]=new Ma(a,b)),r&&j[b].isNew&&j[b].render(c,!0),j[b].render(c,!1,1)}),q&&a.min===0&&(j[-1]||(j[-1]=new Ma(a,-1,null,!0)),j[-1].render(-1))),
p&&n(g,function(b,c){if(c%2===0&&b<a.max)l[b]||(l[b]=new vb(a)),s=b+q,w=g[c+1]!==v?g[c+1]+q:a.max,l[b].options={from:e?fa(s):s,to:e?fa(w):w,color:p},l[b].render(),l[b].isActive=!0}),!a._addedPlotLB)n((d.plotLines||[]).concat(d.plotBands||[]),function(b){a.addPlotBandOrLine(b)}),a._addedPlotLB=!0;n([j,k,l],function(a){var c,d,e=[],f=Fa?Fa.duration||500:0,g=function(){for(d=e.length;d--;)a[e[d]]&&!a[e[d]].isActive&&(a[e[d]].destroy(),delete a[e[d]])};for(c in a)if(!a[c].isActive)a[c].render(c,!1,0),
a[c].isActive=!1,e.push(c);a===l||!b.hasRendered||!f?g():f&&setTimeout(g,f)});if(o)B=a.getLinePath(o),a.axisLine?a.axisLine.animate({d:B}):a.axisLine=c.path(B).attr({stroke:d.lineColor,"stroke-width":o,zIndex:7}).add(a.axisGroup),a.axisLine[t?"show":"hide"]();if(h&&t)h[h.isNew?"attr":"animate"](a.getTitlePosition()),h.isNew=!1;if(m&&m.enabled){var x,y,d=a.stackTotalGroup;if(!d)a.stackTotalGroup=d=c.g("stack-labels").attr({visibility:"visible",zIndex:6}).add();d.translate(b.plotLeft,b.plotTop);for(x in i)for(y in c=
i[x],c)c[y].render(d)}a.isDirty=!1},removePlotBandOrLine:function(a){for(var b=this.plotLinesAndBands,c=this.options,d=this.userOptions,e=b.length;e--;)b[e].id===a&&b[e].destroy();n([c.plotLines||[],d.plotLines||[],c.plotBands||[],d.plotBands||[]],function(b){for(e=b.length;e--;)b[e].id===a&&ga(b,b[e])})},setTitle:function(a,b){this.update({title:a},b)},redraw:function(){var a=this.chart.pointer;a.reset&&a.reset(!0);this.render();n(this.plotLinesAndBands,function(a){a.render()});n(this.series,function(a){a.isDirty=
!0})},buildStacks:function(){var a=this.series,b=a.length;if(!this.isXAxis){for(;b--;)a[b].setStackedPoints();if(this.usePercentage)for(b=0;b<a.length;b++)a[b].setPercentStacks()}},setCategories:function(a,b){this.update({categories:a},b)},destroy:function(a){var b=this,c=b.stacks,d,e=b.plotLinesAndBands;a||$(b);for(d in c)Ka(c[d]),c[d]=null;n([b.ticks,b.minorTicks,b.alternateBands],function(a){Ka(a)});for(a=e.length;a--;)e[a].destroy();n("stackTotalGroup,axisLine,axisGroup,gridGroup,labelGroup,axisTitle".split(","),
function(a){b[a]&&(b[a]=b[a].destroy())})}};wb.prototype={init:function(a,b){var c=b.borderWidth,d=b.style,e=y(d.padding);this.chart=a;this.options=b;this.crosshairs=[];this.now={x:0,y:0};this.isHidden=!0;this.label=a.renderer.label("",0,0,b.shape,null,null,b.useHTML,null,"tooltip").attr({padding:e,fill:b.backgroundColor,"stroke-width":c,r:b.borderRadius,zIndex:8}).css(d).css({padding:0}).add().attr({y:-999});ca||this.label.shadow(b.shadow);this.shared=b.shared},destroy:function(){n(this.crosshairs,
function(a){a&&a.destroy()});if(this.label)this.label=this.label.destroy();clearTimeout(this.hideTimer);clearTimeout(this.tooltipTimeout)},move:function(a,b,c,d){var e=this,f=e.now,g=e.options.animation!==!1&&!e.isHidden;s(f,{x:g?(2*f.x+a)/3:a,y:g?(f.y+b)/2:b,anchorX:g?(2*f.anchorX+c)/3:c,anchorY:g?(f.anchorY+d)/2:d});e.label.attr(f);if(g&&(M(a-f.x)>1||M(b-f.y)>1))clearTimeout(this.tooltipTimeout),this.tooltipTimeout=setTimeout(function(){e&&e.move(a,b,c,d)},32)},hide:function(){var a=this,b;clearTimeout(this.hideTimer);
if(!this.isHidden)b=this.chart.hoverPoints,this.hideTimer=setTimeout(function(){a.label.fadeOut();a.isHidden=!0},o(this.options.hideDelay,500)),b&&n(b,function(a){a.setState()}),this.chart.hoverPoints=null},hideCrosshairs:function(){n(this.crosshairs,function(a){a&&a.hide()})},getAnchor:function(a,b){var c,d=this.chart,e=d.inverted,f=d.plotTop,g=0,h=0,i,a=ja(a);c=a[0].tooltipPos;this.followPointer&&b&&(b.chartX===v&&(b=d.pointer.normalize(b)),c=[b.chartX-d.plotLeft,b.chartY-f]);c||(n(a,function(a){i=
a.series.yAxis;g+=a.plotX;h+=(a.plotLow?(a.plotLow+a.plotHigh)/2:a.plotY)+(!e&&i?i.top-f:0)}),g/=a.length,h/=a.length,c=[e?d.plotWidth-h:g,this.shared&&!e&&a.length>1&&b?b.chartY-f:e?d.plotHeight-g:h]);return Na(c,t)},getPosition:function(a,b,c){var d=this.chart,e=d.plotLeft,f=d.plotTop,g=d.plotWidth,h=d.plotHeight,i=o(this.options.distance,12),j=c.plotX,c=c.plotY,d=j+e+(d.inverted?i:-a-i),k=c-b+f+15,l;d<7&&(d=e+r(j,0)+i);d+a>e+g&&(d-=d+a-(e+g),k=c-b+f-i,l=!0);k<f+5&&(k=f+5,l&&c>=k&&c<=k+b&&(k=c+
f+i));k+b>f+h&&(k=r(f,f+h-b-i));return{x:d,y:k}},defaultFormatter:function(a){var b=this.points||ja(this),c=b[0].series,d;d=[c.tooltipHeaderFormatter(b[0])];n(b,function(a){c=a.series;d.push(c.tooltipFormatter&&c.tooltipFormatter(a)||a.point.tooltipFormatter(c.tooltipOptions.pointFormat))});d.push(a.options.footerFormat||"");return d.join("")},refresh:function(a,b){var c=this.chart,d=this.label,e=this.options,f,g,h={},i,j=[];i=e.formatter||this.defaultFormatter;var h=c.hoverPoints,k,l=e.crosshairs,
m=this.shared;clearTimeout(this.hideTimer);this.followPointer=ja(a)[0].series.tooltipOptions.followPointer;g=this.getAnchor(a,b);f=g[0];g=g[1];m&&(!a.series||!a.series.noSharedTooltip)?(c.hoverPoints=a,h&&n(h,function(a){a.setState()}),n(a,function(a){a.setState("hover");j.push(a.getLabelConfig())}),h={x:a[0].category,y:a[0].y},h.points=j,a=a[0]):h=a.getLabelConfig();i=i.call(h,this);h=a.series;i===!1?this.hide():(this.isHidden&&(Wa(d),d.attr("opacity",1).show()),d.attr({text:i}),k=e.borderColor||
a.color||h.color||"#606060",d.attr({stroke:k}),this.updatePosition({plotX:f,plotY:g}),this.isHidden=!1);if(l){l=ja(l);for(d=l.length;d--;)if(m=a.series,e=m[d?"yAxis":"xAxis"],l[d]&&e)if(h=d?o(a.stackY,a.y):a.x,e.isLog&&(h=ma(h)),d===1&&m.modifyValue&&(h=m.modifyValue(h)),e=e.getPlotLinePath(h,1),this.crosshairs[d])this.crosshairs[d].attr({d:e,visibility:"visible"});else{h={"stroke-width":l[d].width||1,stroke:l[d].color||"#C0C0C0",zIndex:l[d].zIndex||2};if(l[d].dashStyle)h.dashstyle=l[d].dashStyle;
this.crosshairs[d]=c.renderer.path(e).attr(h).add()}}A(c,"tooltipRefresh",{text:i,x:f+c.plotLeft,y:g+c.plotTop,borderColor:k})},updatePosition:function(a){var b=this.chart,c=this.label,c=(this.options.positioner||this.getPosition).call(this,c.width,c.height,a);this.move(t(c.x),t(c.y),a.plotX+b.plotLeft,a.plotY+b.plotTop)}};xb.prototype={init:function(a,b){var c=b.chart,d=c.events,e=ca?"":c.zoomType,c=a.inverted,f;this.options=b;this.chart=a;this.zoomX=f=/x/.test(e);this.zoomY=e=/y/.test(e);this.zoomHor=
f&&!c||e&&c;this.zoomVert=e&&!c||f&&c;this.runChartClick=d&&!!d.click;this.pinchDown=[];this.lastValidTouch={};if(b.tooltip.enabled)a.tooltip=new wb(a,b.tooltip);this.setDOMEvents()},normalize:function(a,b){var c,d,a=a||N.event;if(!a.target)a.target=a.srcElement;a=Xb(a);d=a.touches?a.touches.item(0):a;if(!b)this.chartPosition=b=Wb(this.chart.container);d.pageX===v?(c=r(a.x,a.clientX-b.left),d=a.y):(c=d.pageX-b.left,d=d.pageY-b.top);return s(a,{chartX:t(c),chartY:t(d)})},getCoordinates:function(a){var b=
{xAxis:[],yAxis:[]};n(this.chart.axes,function(c){b[c.isXAxis?"xAxis":"yAxis"].push({axis:c,value:c.toValue(a[c.horiz?"chartX":"chartY"])})});return b},getIndex:function(a){var b=this.chart;return b.inverted?b.plotHeight+b.plotTop-a.chartY:a.chartX-b.plotLeft},runPointActions:function(a){var b=this.chart,c=b.series,d=b.tooltip,e,f=b.hoverPoint,g=b.hoverSeries,h,i,j=b.chartWidth,k=this.getIndex(a);if(d&&this.options.tooltip.shared&&(!g||!g.noSharedTooltip)){e=[];h=c.length;for(i=0;i<h;i++)if(c[i].visible&&
c[i].options.enableMouseTracking!==!1&&!c[i].noSharedTooltip&&c[i].tooltipPoints.length&&(b=c[i].tooltipPoints[k])&&b.series)b._dist=M(k-b.clientX),j=J(j,b._dist),e.push(b);for(h=e.length;h--;)e[h]._dist>j&&e.splice(h,1);if(e.length&&e[0].clientX!==this.hoverX)d.refresh(e,a),this.hoverX=e[0].clientX}if(g&&g.tracker){if((b=g.tooltipPoints[k])&&b!==f)b.onMouseOver(a)}else d&&d.followPointer&&!d.isHidden&&(a=d.getAnchor([{}],a),d.updatePosition({plotX:a[0],plotY:a[1]}))},reset:function(a){var b=this.chart,
c=b.hoverSeries,d=b.hoverPoint,e=b.tooltip,b=e&&e.shared?b.hoverPoints:d;(a=a&&e&&b)&&ja(b)[0].plotX===v&&(a=!1);if(a)e.refresh(b);else{if(d)d.onMouseOut();if(c)c.onMouseOut();e&&(e.hide(),e.hideCrosshairs());this.hoverX=null}},scaleGroups:function(a,b){var c=this.chart,d;n(c.series,function(e){d=a||e.getPlotBox();e.xAxis&&e.xAxis.zoomEnabled&&(e.group.attr(d),e.markerGroup&&(e.markerGroup.attr(d),e.markerGroup.clip(b?c.clipRect:null)),e.dataLabelsGroup&&e.dataLabelsGroup.attr(d))});c.clipRect.attr(b||
c.clipBox)},pinchTranslate:function(a,b,c,d,e,f,g,h){a&&this.pinchTranslateDirection(!0,c,d,e,f,g,h);b&&this.pinchTranslateDirection(!1,c,d,e,f,g,h)},pinchTranslateDirection:function(a,b,c,d,e,f,g,h){var i=this.chart,j=a?"x":"y",k=a?"X":"Y",l="chart"+k,m=a?"width":"height",p=i["plot"+(a?"Left":"Top")],q,o,n=h||1,r=i.inverted,t=i.bounds[a?"h":"v"],u=b.length===1,s=b[0][l],v=c[0][l],w=!u&&b[1][l],x=!u&&c[1][l],y,c=function(){!u&&M(s-w)>20&&(n=h||M(v-x)/M(s-w));o=(p-v)/n+s;q=i["plot"+(a?"Width":"Height")]/
n};c();b=o;b<t.min?(b=t.min,y=!0):b+q>t.max&&(b=t.max-q,y=!0);y?(v-=0.8*(v-g[j][0]),u||(x-=0.8*(x-g[j][1])),c()):g[j]=[v,x];r||(f[j]=o-p,f[m]=q);f=r?1/n:n;e[m]=q;e[j]=b;d[r?a?"scaleY":"scaleX":"scale"+k]=n;d["translate"+k]=f*p+(v-f*s)},pinch:function(a){var b=this,c=b.chart,d=b.pinchDown,e=c.tooltip&&c.tooltip.options.followTouchMove,f=a.touches,g=f.length,h=b.lastValidTouch,i=b.zoomHor||b.pinchHor,j=b.zoomVert||b.pinchVert,k=i||j,l=b.selectionMarker,m={},p=g===1&&(b.inClass(a.target,"highcharts-tracker")&&
c.runTrackerClick||c.runChartClick),q={};(k||e)&&!p&&a.preventDefault();Na(f,function(a){return b.normalize(a)});if(a.type==="touchstart")n(f,function(a,b){d[b]={chartX:a.chartX,chartY:a.chartY}}),h.x=[d[0].chartX,d[1]&&d[1].chartX],h.y=[d[0].chartY,d[1]&&d[1].chartY],n(c.axes,function(a){if(a.zoomEnabled){var b=c.bounds[a.horiz?"h":"v"],d=a.minPixelPadding,e=a.toPixels(a.dataMin),f=a.toPixels(a.dataMax),g=J(e,f),e=r(e,f);b.min=J(a.pos,g-d);b.max=r(a.pos+a.len,e+d)}});else if(d.length){if(!l)b.selectionMarker=
l=s({destroy:oa},c.plotBox);b.pinchTranslate(i,j,d,f,m,l,q,h);b.hasPinched=k;b.scaleGroups(m,q);!k&&e&&g===1&&this.runPointActions(b.normalize(a))}},dragStart:function(a){var b=this.chart;b.mouseIsDown=a.type;b.cancelClick=!1;b.mouseDownX=this.mouseDownX=a.chartX;b.mouseDownY=this.mouseDownY=a.chartY},drag:function(a){var b=this.chart,c=b.options.chart,d=a.chartX,e=a.chartY,f=this.zoomHor,g=this.zoomVert,h=b.plotLeft,i=b.plotTop,j=b.plotWidth,k=b.plotHeight,l,m=this.mouseDownX,p=this.mouseDownY;d<
h?d=h:d>h+j&&(d=h+j);e<i?e=i:e>i+k&&(e=i+k);this.hasDragged=Math.sqrt(Math.pow(m-d,2)+Math.pow(p-e,2));if(this.hasDragged>10){l=b.isInsidePlot(m-h,p-i);if(b.hasCartesianSeries&&(this.zoomX||this.zoomY)&&l&&!this.selectionMarker)this.selectionMarker=b.renderer.rect(h,i,f?1:j,g?1:k,0).attr({fill:c.selectionMarkerFill||"rgba(69,114,167,0.25)",zIndex:7}).add();this.selectionMarker&&f&&(d-=m,this.selectionMarker.attr({width:M(d),x:(d>0?0:d)+m}));this.selectionMarker&&g&&(d=e-p,this.selectionMarker.attr({height:M(d),
y:(d>0?0:d)+p}));l&&!this.selectionMarker&&c.panning&&b.pan(a,c.panning)}},drop:function(a){var b=this.chart,c=this.hasPinched;if(this.selectionMarker){var d={xAxis:[],yAxis:[],originalEvent:a.originalEvent||a},e=this.selectionMarker,f=e.x,g=e.y,h;if(this.hasDragged||c)n(b.axes,function(a){if(a.zoomEnabled){var b=a.horiz,c=a.toValue(b?f:g),b=a.toValue(b?f+e.width:g+e.height);!isNaN(c)&&!isNaN(b)&&(d[a.xOrY+"Axis"].push({axis:a,min:J(c,b),max:r(c,b)}),h=!0)}}),h&&A(b,"selection",d,function(a){b.zoom(s(a,
c?{animation:!1}:null))});this.selectionMarker=this.selectionMarker.destroy();c&&this.scaleGroups()}if(b)I(b.container,{cursor:b._cursor}),b.cancelClick=this.hasDragged>10,b.mouseIsDown=this.hasDragged=this.hasPinched=!1,this.pinchDown=[]},onContainerMouseDown:function(a){a=this.normalize(a);a.preventDefault&&a.preventDefault();this.dragStart(a)},onDocumentMouseUp:function(a){this.drop(a)},onDocumentMouseMove:function(a){var b=this.chart,c=this.chartPosition,d=b.hoverSeries,a=this.normalize(a,c);
c&&d&&!this.inClass(a.target,"highcharts-tracker")&&!b.isInsidePlot(a.chartX-b.plotLeft,a.chartY-b.plotTop)&&this.reset()},onContainerMouseLeave:function(){this.reset();this.chartPosition=null},onContainerMouseMove:function(a){var b=this.chart,a=this.normalize(a);a.returnValue=!1;b.mouseIsDown==="mousedown"&&this.drag(a);(this.inClass(a.target,"highcharts-tracker")||b.isInsidePlot(a.chartX-b.plotLeft,a.chartY-b.plotTop))&&!b.openMenu&&this.runPointActions(a)},inClass:function(a,b){for(var c;a;){if(c=
w(a,"class"))if(c.indexOf(b)!==-1)return!0;else if(c.indexOf("highcharts-container")!==-1)return!1;a=a.parentNode}},onTrackerMouseOut:function(a){var b=this.chart.hoverSeries;if(b&&!b.options.stickyTracking&&!this.inClass(a.toElement||a.relatedTarget,"highcharts-tooltip"))b.onMouseOut()},onContainerClick:function(a){var b=this.chart,c=b.hoverPoint,d=b.plotLeft,e=b.plotTop,f=b.inverted,g,h,i,a=this.normalize(a);a.cancelBubble=!0;if(!b.cancelClick)c&&this.inClass(a.target,"highcharts-tracker")?(g=this.chartPosition,
h=c.plotX,i=c.plotY,s(c,{pageX:g.left+d+(f?b.plotWidth-i:h),pageY:g.top+e+(f?b.plotHeight-h:i)}),A(c.series,"click",s(a,{point:c})),b.hoverPoint&&c.firePointEvent("click",a)):(s(a,this.getCoordinates(a)),b.isInsidePlot(a.chartX-d,a.chartY-e)&&A(b,"click",a))},onContainerTouchStart:function(a){var b=this.chart;a.touches.length===1?(a=this.normalize(a),b.isInsidePlot(a.chartX-b.plotLeft,a.chartY-b.plotTop)?(this.runPointActions(a),this.pinch(a)):this.reset()):a.touches.length===2&&this.pinch(a)},onContainerTouchMove:function(a){(a.touches.length===
1||a.touches.length===2)&&this.pinch(a)},onDocumentTouchEnd:function(a){this.drop(a)},setDOMEvents:function(){var a=this,b=a.chart.container,c;this._events=c=[[b,"onmousedown","onContainerMouseDown"],[b,"onmousemove","onContainerMouseMove"],[b,"onclick","onContainerClick"],[b,"mouseleave","onContainerMouseLeave"],[z,"mousemove","onDocumentMouseMove"],[z,"mouseup","onDocumentMouseUp"]];jb&&c.push([b,"ontouchstart","onContainerTouchStart"],[b,"ontouchmove","onContainerTouchMove"],[z,"touchend","onDocumentTouchEnd"]);
n(c,function(b){a["_"+b[2]]=function(c){a[b[2]](c)};b[1].indexOf("on")===0?b[0][b[1]]=a["_"+b[2]]:K(b[0],b[1],a["_"+b[2]])})},destroy:function(){var a=this;n(a._events,function(b){b[1].indexOf("on")===0?b[0][b[1]]=null:$(b[0],b[1],a["_"+b[2]])});delete a._events;clearInterval(a.tooltipTimeout)}};fb.prototype={init:function(a,b){var c=this,d=b.itemStyle,e=o(b.padding,8),f=b.itemMarginTop||0;this.options=b;if(b.enabled)c.baseline=y(d.fontSize)+3+f,c.itemStyle=d,c.itemHiddenStyle=x(d,b.itemHiddenStyle),
c.itemMarginTop=f,c.padding=e,c.initialItemX=e,c.initialItemY=e-5,c.maxItemWidth=0,c.chart=a,c.itemHeight=0,c.lastLineHeight=0,c.render(),K(c.chart,"endResize",function(){c.positionCheckboxes()})},colorizeItem:function(a,b){var c=this.options,d=a.legendItem,e=a.legendLine,f=a.legendSymbol,g=this.itemHiddenStyle.color,c=b?c.itemStyle.color:g,h=b?a.color:g,g=a.options&&a.options.marker,i={stroke:h,fill:h},j;d&&d.css({fill:c,color:c});e&&e.attr({stroke:h});if(f){if(g&&f.isMarker)for(j in g=a.convertAttribs(g),
g)d=g[j],d!==v&&(i[j]=d);f.attr(i)}},positionItem:function(a){var b=this.options,c=b.symbolPadding,b=!b.rtl,d=a._legendItemPos,e=d[0],d=d[1],f=a.checkbox;a.legendGroup&&a.legendGroup.translate(b?e:this.legendWidth-e-2*c-4,d);if(f)f.x=e,f.y=d},destroyItem:function(a){var b=a.checkbox;n(["legendItem","legendLine","legendSymbol","legendGroup"],function(b){a[b]&&(a[b]=a[b].destroy())});b&&Ta(a.checkbox)},destroy:function(){var a=this.group,b=this.box;if(b)this.box=b.destroy();if(a)this.group=a.destroy()},
positionCheckboxes:function(a){var b=this.group.alignAttr,c,d=this.clipHeight||this.legendHeight;if(b)c=b.translateY,n(this.allItems,function(e){var f=e.checkbox,g;f&&(g=c+f.y+(a||0)+3,I(f,{left:b.translateX+e.legendItemWidth+f.x-20+"px",top:g+"px",display:g>c-6&&g<c+d-6?"":S}))})},renderTitle:function(){var a=this.padding,b=this.options.title,c=0;if(b.text){if(!this.title)this.title=this.chart.renderer.label(b.text,a-3,a-4,null,null,null,null,null,"legend-title").attr({zIndex:1}).css(b.style).add(this.group);
a=this.title.getBBox();c=a.height;this.offsetWidth=a.width;this.contentGroup.attr({translateY:c})}this.titleHeight=c},renderItem:function(a){var C;var b=this,c=b.chart,d=c.renderer,e=b.options,f=e.layout==="horizontal",g=e.symbolWidth,h=e.symbolPadding,i=b.itemStyle,j=b.itemHiddenStyle,k=b.padding,l=f?o(e.itemDistance,8):0,m=!e.rtl,p=e.width,q=e.itemMarginBottom||0,n=b.itemMarginTop,B=b.initialItemX,t=a.legendItem,u=a.series||a,s=u.options,v=s.showCheckbox,w=e.useHTML;if(!t&&(a.legendGroup=d.g("legend-item").attr({zIndex:1}).add(b.scrollGroup),
u.drawLegendSymbol(b,a),a.legendItem=t=d.text(e.labelFormat?Ca(e.labelFormat,a):e.labelFormatter.call(a),m?g+h:-h,b.baseline,w).css(x(a.visible?i:j)).attr({align:m?"left":"right",zIndex:2}).add(a.legendGroup),(w?t:a.legendGroup).on("mouseover",function(){a.setState("hover");t.css(b.options.itemHoverStyle)}).on("mouseout",function(){t.css(a.visible?i:j);a.setState()}).on("click",function(b){var c=function(){a.setVisible()},b={browserEvent:b};a.firePointEvent?a.firePointEvent("legendItemClick",b,c):
A(a,"legendItemClick",b,c)}),b.colorizeItem(a,a.visible),s&&v))a.checkbox=U("input",{type:"checkbox",checked:a.selected,defaultChecked:a.selected},e.itemCheckboxStyle,c.container),K(a.checkbox,"click",function(b){A(a,"checkboxClick",{checked:b.target.checked},function(){a.select()})});d=t.getBBox();C=a.legendItemWidth=e.itemWidth||g+h+d.width+l+(v?20:0),e=C;b.itemHeight=g=d.height;if(f&&b.itemX-B+e>(p||c.chartWidth-2*k-B))b.itemX=B,b.itemY+=n+b.lastLineHeight+q,b.lastLineHeight=0;b.maxItemWidth=r(b.maxItemWidth,
e);b.lastItemY=n+b.itemY+q;b.lastLineHeight=r(g,b.lastLineHeight);a._legendItemPos=[b.itemX,b.itemY];f?b.itemX+=e:(b.itemY+=n+g+q,b.lastLineHeight=g);b.offsetWidth=p||r((f?b.itemX-B-l:e)+k,b.offsetWidth)},render:function(){var a=this,b=a.chart,c=b.renderer,d=a.group,e,f,g,h,i=a.box,j=a.options,k=a.padding,l=j.borderWidth,m=j.backgroundColor;a.itemX=a.initialItemX;a.itemY=a.initialItemY;a.offsetWidth=0;a.lastItemY=0;if(!d)a.group=d=c.g("legend").attr({zIndex:7}).add(),a.contentGroup=c.g().attr({zIndex:1}).add(d),
a.scrollGroup=c.g().add(a.contentGroup);a.renderTitle();e=[];n(b.series,function(a){var b=a.options;if(o(b.showInLegend,b.linkedTo===v?v:!1,!0))e=e.concat(a.legendItems||(b.legendType==="point"?a.data:a))});Kb(e,function(a,b){return(a.options&&a.options.legendIndex||0)-(b.options&&b.options.legendIndex||0)});j.reversed&&e.reverse();a.allItems=e;a.display=f=!!e.length;n(e,function(b){a.renderItem(b)});g=j.width||a.offsetWidth;h=a.lastItemY+a.lastLineHeight+a.titleHeight;h=a.handleOverflow(h);if(l||
m){g+=k;h+=k;if(i){if(g>0&&h>0)i[i.isNew?"attr":"animate"](i.crisp(null,null,null,g,h)),i.isNew=!1}else a.box=i=c.rect(0,0,g,h,j.borderRadius,l||0).attr({stroke:j.borderColor,"stroke-width":l||0,fill:m||S}).add(d).shadow(j.shadow),i.isNew=!0;i[f?"show":"hide"]()}a.legendWidth=g;a.legendHeight=h;n(e,function(b){a.positionItem(b)});f&&d.align(s({width:g,height:h},j),!0,"spacingBox");b.isResizing||this.positionCheckboxes()},handleOverflow:function(a){var b=this,c=this.chart,d=c.renderer,e=this.options,
f=e.y,f=c.spacingBox.height+(e.verticalAlign==="top"?-f:f)-this.padding,g=e.maxHeight,h=this.clipRect,i=e.navigation,j=o(i.animation,!0),k=i.arrowSize||12,l=this.nav;e.layout==="horizontal"&&(f/=2);g&&(f=J(f,g));if(a>f&&!e.useHTML){this.clipHeight=c=f-20-this.titleHeight;this.pageCount=wa(a/c);this.currentPage=o(this.currentPage,1);this.fullHeight=a;if(!h)h=b.clipRect=d.clipRect(0,0,9999,0),b.contentGroup.clip(h);h.attr({height:c});if(!l)this.nav=l=d.g().attr({zIndex:1}).add(this.group),this.up=d.symbol("triangle",
0,0,k,k).on("click",function(){b.scroll(-1,j)}).add(l),this.pager=d.text("",15,10).css(i.style).add(l),this.down=d.symbol("triangle-down",0,0,k,k).on("click",function(){b.scroll(1,j)}).add(l);b.scroll(0);a=f}else if(l)h.attr({height:c.chartHeight}),l.hide(),this.scrollGroup.attr({translateY:1}),this.clipHeight=0;return a},scroll:function(a,b){var c=this.pageCount,d=this.currentPage+a,e=this.clipHeight,f=this.options.navigation,g=f.activeColor,h=f.inactiveColor,f=this.pager,i=this.padding;d>c&&(d=
c);if(d>0)b!==v&&La(b,this.chart),this.nav.attr({translateX:i,translateY:e+7+this.titleHeight,visibility:"visible"}),this.up.attr({fill:d===1?h:g}).css({cursor:d===1?"default":"pointer"}),f.attr({text:d+"/"+this.pageCount}),this.down.attr({x:18+this.pager.getBBox().width,fill:d===c?h:g}).css({cursor:d===c?"default":"pointer"}),e=-J(e*(d-1),this.fullHeight-e+i)+1,this.scrollGroup.animate({translateY:e}),f.attr({text:d+"/"+c}),this.currentPage=d,this.positionCheckboxes(e)}};/Trident\/7\.0/.test(na)&&
mb(fb.prototype,"positionItem",function(a,b){var c=this,d=function(){a.call(c,b)};c.chart.renderer.forExport?d():setTimeout(d)});yb.prototype={init:function(a,b){var c,d=a.series;a.series=null;c=x(L,a);c.series=a.series=d;d=c.chart;this.margin=this.splashArray("margin",d);this.spacing=this.splashArray("spacing",d);var e=d.events;this.bounds={h:{},v:{}};this.callback=b;this.isResizing=0;this.options=c;this.axes=[];this.series=[];this.hasCartesianSeries=d.showAxes;var f=this,g;f.index=Ga.length;Ga.push(f);
d.reflow!==!1&&K(f,"load",function(){f.initReflow()});if(e)for(g in e)K(f,g,e[g]);f.xAxis=[];f.yAxis=[];f.animation=ca?!1:o(d.animation,!0);f.pointCount=0;f.counters=new Jb;f.firstRender()},initSeries:function(a){var b=this.options.chart;(b=X[a.type||b.type||b.defaultSeriesType])||ka(17,!0);b=new b;b.init(this,a);return b},addSeries:function(a,b,c){var d,e=this;a&&(b=o(b,!0),A(e,"addSeries",{options:a},function(){d=e.initSeries(a);e.isDirtyLegend=!0;e.linkSeries();b&&e.redraw(c)}));return d},addAxis:function(a,
b,c,d){var e=b?"xAxis":"yAxis",f=this.options;new eb(this,x(a,{index:this[e].length,isX:b}));f[e]=ja(f[e]||{});f[e].push(a);o(c,!0)&&this.redraw(d)},isInsidePlot:function(a,b,c){var d=c?b:a,a=c?a:b;return d>=0&&d<=this.plotWidth&&a>=0&&a<=this.plotHeight},adjustTickAmounts:function(){this.options.chart.alignTicks!==!1&&n(this.axes,function(a){a.adjustTickAmount()});this.maxTicks=null},redraw:function(a){var b=this.axes,c=this.series,d=this.pointer,e=this.legend,f=this.isDirtyLegend,g,h,i=this.isDirtyBox,
j=c.length,k=j,l=this.renderer,m=l.isHidden(),p=[];La(a,this);m&&this.cloneRenderTo();for(this.layOutTitles();k--;)if(a=c[k],a.options.stacking&&(g=!0,a.isDirty)){h=!0;break}if(h)for(k=j;k--;)if(a=c[k],a.options.stacking)a.isDirty=!0;n(c,function(a){a.isDirty&&a.options.legendType==="point"&&(f=!0)});if(f&&e.options.enabled)e.render(),this.isDirtyLegend=!1;g&&this.getStacks();if(this.hasCartesianSeries){if(!this.isResizing)this.maxTicks=null,n(b,function(a){a.setScale()});this.adjustTickAmounts();
this.getMargins();n(b,function(a){a.isDirty&&(i=!0)});n(b,function(a){if(a.isDirtyExtremes)a.isDirtyExtremes=!1,p.push(function(){A(a,"afterSetExtremes",s(a.eventArgs,a.getExtremes()));delete a.eventArgs});(i||g)&&a.redraw()})}i&&this.drawChartBox();n(c,function(a){a.isDirty&&a.visible&&(!a.isCartesian||a.xAxis)&&a.redraw()});d&&d.reset&&d.reset(!0);l.draw();A(this,"redraw");m&&this.cloneRenderTo(!0);n(p,function(a){a.call()})},showLoading:function(a){var b=this.options,c=this.loadingDiv,d=b.loading;
if(!c)this.loadingDiv=c=U(Ea,{className:"highcharts-loading"},s(d.style,{zIndex:10,display:S}),this.container),this.loadingSpan=U("span",null,d.labelStyle,c);this.loadingSpan.innerHTML=a||b.lang.loading;if(!this.loadingShown)I(c,{opacity:0,display:"",left:this.plotLeft+"px",top:this.plotTop+"px",width:this.plotWidth+"px",height:this.plotHeight+"px"}),Bb(c,{opacity:d.style.opacity},{duration:d.showDuration||0}),this.loadingShown=!0},hideLoading:function(){var a=this.options,b=this.loadingDiv;b&&Bb(b,
{opacity:0},{duration:a.loading.hideDuration||100,complete:function(){I(b,{display:S})}});this.loadingShown=!1},get:function(a){var b=this.axes,c=this.series,d,e;for(d=0;d<b.length;d++)if(b[d].options.id===a)return b[d];for(d=0;d<c.length;d++)if(c[d].options.id===a)return c[d];for(d=0;d<c.length;d++){e=c[d].points||[];for(b=0;b<e.length;b++)if(e[b].id===a)return e[b]}return null},getAxes:function(){var a=this,b=this.options,c=b.xAxis=ja(b.xAxis||{}),b=b.yAxis=ja(b.yAxis||{});n(c,function(a,b){a.index=
b;a.isX=!0});n(b,function(a,b){a.index=b});c=c.concat(b);n(c,function(b){new eb(a,b)});a.adjustTickAmounts()},getSelectedPoints:function(){var a=[];n(this.series,function(b){a=a.concat(ub(b.points||[],function(a){return a.selected}))});return a},getSelectedSeries:function(){return ub(this.series,function(a){return a.selected})},getStacks:function(){var a=this;n(a.yAxis,function(a){if(a.stacks&&a.hasVisibleSeries)a.oldStacks=a.stacks});n(a.series,function(b){if(b.options.stacking&&(b.visible===!0||
a.options.chart.ignoreHiddenSeries===!1))b.stackKey=b.type+o(b.options.stack,"")})},showResetZoom:function(){var a=this,b=L.lang,c=a.options.chart.resetZoomButton,d=c.theme,e=d.states,f=c.relativeTo==="chart"?null:"plotBox";this.resetZoomButton=a.renderer.button(b.resetZoom,null,null,function(){a.zoomOut()},d,e&&e.hover).attr({align:c.position.align,title:b.resetZoomTitle}).add().align(c.position,!1,f)},zoomOut:function(){var a=this;A(a,"selection",{resetSelection:!0},function(){a.zoom()})},zoom:function(a){var b,
c=this.pointer,d=!1,e;!a||a.resetSelection?n(this.axes,function(a){b=a.zoom()}):n(a.xAxis.concat(a.yAxis),function(a){var e=a.axis,h=e.isXAxis;if(c[h?"zoomX":"zoomY"]||c[h?"pinchX":"pinchY"])b=e.zoom(a.min,a.max),e.displayBtn&&(d=!0)});e=this.resetZoomButton;if(d&&!e)this.showResetZoom();else if(!d&&T(e))this.resetZoomButton=e.destroy();b&&this.redraw(o(this.options.chart.animation,a&&a.animation,this.pointCount<100))},pan:function(a,b){var c=this,d=c.hoverPoints,e;d&&n(d,function(a){a.setState()});
n(b==="xy"?[1,0]:[1],function(b){var d=a[b?"chartX":"chartY"],h=c[b?"xAxis":"yAxis"][0],i=c[b?"mouseDownX":"mouseDownY"],j=(h.pointRange||0)/2,k=h.getExtremes(),l=h.toValue(i-d,!0)+j,i=h.toValue(i+c[b?"plotWidth":"plotHeight"]-d,!0)-j;h.series.length&&l>J(k.dataMin,k.min)&&i<r(k.dataMax,k.max)&&(h.setExtremes(l,i,!1,!1,{trigger:"pan"}),e=!0);c[b?"mouseDownX":"mouseDownY"]=d});e&&c.redraw(!1);I(c.container,{cursor:"move"})},setTitle:function(a,b){var f;var c=this,d=c.options,e;e=d.title=x(d.title,
a);f=d.subtitle=x(d.subtitle,b),d=f;n([["title",a,e],["subtitle",b,d]],function(a){var b=a[0],d=c[b],e=a[1],a=a[2];d&&e&&(c[b]=d=d.destroy());a&&a.text&&!d&&(c[b]=c.renderer.text(a.text,0,0,a.useHTML).attr({align:a.align,"class":"highcharts-"+b,zIndex:a.zIndex||4}).css(a.style).add())});c.layOutTitles()},layOutTitles:function(){var a=0,b=this.title,c=this.subtitle,d=this.options,e=d.title,d=d.subtitle,f=this.spacingBox.width-44;if(b&&(b.css({width:(e.width||f)+"px"}).align(s({y:15},e),!1,"spacingBox"),
!e.floating&&!e.verticalAlign))a=b.getBBox().height,a>=18&&a<=25&&(a=15);c&&(c.css({width:(d.width||f)+"px"}).align(s({y:a+e.margin},d),!1,"spacingBox"),!d.floating&&!d.verticalAlign&&(a=wa(a+c.getBBox().height)));this.titleOffset=a},getChartSize:function(){var a=this.options.chart,b=this.renderToClone||this.renderTo;this.containerWidth=kb(b,"width");this.containerHeight=kb(b,"height");this.chartWidth=r(0,a.width||this.containerWidth||600);this.chartHeight=r(0,o(a.height,this.containerHeight>19?this.containerHeight:
400))},cloneRenderTo:function(a){var b=this.renderToClone,c=this.container;a?b&&(this.renderTo.appendChild(c),Ta(b),delete this.renderToClone):(c&&c.parentNode===this.renderTo&&this.renderTo.removeChild(c),this.renderToClone=b=this.renderTo.cloneNode(0),I(b,{position:"absolute",top:"-9999px",display:"block"}),z.body.appendChild(b),c&&b.appendChild(c))},getContainer:function(){var a,b=this.options.chart,c,d,e;this.renderTo=a=b.renderTo;e="highcharts-"+zb++;if(ea(a))this.renderTo=a=z.getElementById(a);
a||ka(13,!0);c=y(w(a,"data-highcharts-chart"));!isNaN(c)&&Ga[c]&&Ga[c].destroy();w(a,"data-highcharts-chart",this.index);a.innerHTML="";a.offsetWidth||this.cloneRenderTo();this.getChartSize();c=this.chartWidth;d=this.chartHeight;this.container=a=U(Ea,{className:"highcharts-container"+(b.className?" "+b.className:""),id:e},s({position:"relative",overflow:"hidden",width:c+"px",height:d+"px",textAlign:"left",lineHeight:"normal",zIndex:0,"-webkit-tap-highlight-color":"rgba(0,0,0,0)"},b.style),this.renderToClone||
a);this._cursor=a.style.cursor;this.renderer=b.forExport?new za(a,c,d,!0):new Va(a,c,d);ca&&this.renderer.create(this,a,c,d)},getMargins:function(){var a=this.spacing,b,c=this.legend,d=this.margin,e=this.options.legend,f=o(e.margin,10),g=e.x,h=e.y,i=e.align,j=e.verticalAlign,k=this.titleOffset;this.resetMargins();b=this.axisOffset;if(k&&!u(d[0]))this.plotTop=r(this.plotTop,k+this.options.title.margin+a[0]);if(c.display&&!e.floating)if(i==="right"){if(!u(d[1]))this.marginRight=r(this.marginRight,c.legendWidth-
g+f+a[1])}else if(i==="left"){if(!u(d[3]))this.plotLeft=r(this.plotLeft,c.legendWidth+g+f+a[3])}else if(j==="top"){if(!u(d[0]))this.plotTop=r(this.plotTop,c.legendHeight+h+f+a[0])}else if(j==="bottom"&&!u(d[2]))this.marginBottom=r(this.marginBottom,c.legendHeight-h+f+a[2]);this.extraBottomMargin&&(this.marginBottom+=this.extraBottomMargin);this.extraTopMargin&&(this.plotTop+=this.extraTopMargin);this.hasCartesianSeries&&n(this.axes,function(a){a.getOffset()});u(d[3])||(this.plotLeft+=b[3]);u(d[0])||
(this.plotTop+=b[0]);u(d[2])||(this.marginBottom+=b[2]);u(d[1])||(this.marginRight+=b[1]);this.setChartSize()},initReflow:function(){function a(a){var g=c.width||kb(d,"width"),h=c.height||kb(d,"height"),a=a?a.target:N;if(!b.hasUserSize&&g&&h&&(a===N||a===z)){if(g!==b.containerWidth||h!==b.containerHeight)clearTimeout(e),b.reflowTimeout=e=setTimeout(function(){if(b.container)b.setSize(g,h,!1),b.hasUserSize=null},100);b.containerWidth=g;b.containerHeight=h}}var b=this,c=b.options.chart,d=b.renderTo,
e;b.reflow=a;K(N,"resize",a);K(b,"destroy",function(){$(N,"resize",a)})},setSize:function(a,b,c){var d=this,e,f,g;d.isResizing+=1;g=function(){d&&A(d,"endResize",null,function(){d.isResizing-=1})};La(c,d);d.oldChartHeight=d.chartHeight;d.oldChartWidth=d.chartWidth;if(u(a))d.chartWidth=e=r(0,t(a)),d.hasUserSize=!!e;if(u(b))d.chartHeight=f=r(0,t(b));I(d.container,{width:e+"px",height:f+"px"});d.setChartSize(!0);d.renderer.setSize(e,f,c);d.maxTicks=null;n(d.axes,function(a){a.isDirty=!0;a.setScale()});
n(d.series,function(a){a.isDirty=!0});d.isDirtyLegend=!0;d.isDirtyBox=!0;d.getMargins();d.redraw(c);d.oldChartHeight=null;A(d,"resize");Fa===!1?g():setTimeout(g,Fa&&Fa.duration||500)},setChartSize:function(a){var b=this.inverted,c=this.renderer,d=this.chartWidth,e=this.chartHeight,f=this.options.chart,g=this.spacing,h=this.clipOffset,i,j,k,l;this.plotLeft=i=t(this.plotLeft);this.plotTop=j=t(this.plotTop);this.plotWidth=k=r(0,t(d-i-this.marginRight));this.plotHeight=l=r(0,t(e-j-this.marginBottom));
this.plotSizeX=b?l:k;this.plotSizeY=b?k:l;this.plotBorderWidth=f.plotBorderWidth||0;this.spacingBox=c.spacingBox={x:g[3],y:g[0],width:d-g[3]-g[1],height:e-g[0]-g[2]};this.plotBox=c.plotBox={x:i,y:j,width:k,height:l};d=2*P(this.plotBorderWidth/2);b=wa(r(d,h[3])/2);c=wa(r(d,h[0])/2);this.clipBox={x:b,y:c,width:P(this.plotSizeX-r(d,h[1])/2-b),height:P(this.plotSizeY-r(d,h[2])/2-c)};a||n(this.axes,function(a){a.setAxisSize();a.setAxisTranslation()})},resetMargins:function(){var a=this.spacing,b=this.margin;
this.plotTop=o(b[0],a[0]);this.marginRight=o(b[1],a[1]);this.marginBottom=o(b[2],a[2]);this.plotLeft=o(b[3],a[3]);this.axisOffset=[0,0,0,0];this.clipOffset=[0,0,0,0]},drawChartBox:function(){var a=this.options.chart,b=this.renderer,c=this.chartWidth,d=this.chartHeight,e=this.chartBackground,f=this.plotBackground,g=this.plotBorder,h=this.plotBGImage,i=a.borderWidth||0,j=a.backgroundColor,k=a.plotBackgroundColor,l=a.plotBackgroundImage,m=a.plotBorderWidth||0,p,q=this.plotLeft,o=this.plotTop,n=this.plotWidth,
r=this.plotHeight,t=this.plotBox,u=this.clipRect,s=this.clipBox;p=i+(a.shadow?8:0);if(i||j)if(e)e.animate(e.crisp(null,null,null,c-p,d-p));else{e={fill:j||S};if(i)e.stroke=a.borderColor,e["stroke-width"]=i;this.chartBackground=b.rect(p/2,p/2,c-p,d-p,a.borderRadius,i).attr(e).add().shadow(a.shadow)}if(k)f?f.animate(t):this.plotBackground=b.rect(q,o,n,r,0).attr({fill:k}).add().shadow(a.plotShadow);if(l)h?h.animate(t):this.plotBGImage=b.image(l,q,o,n,r).add();u?u.animate({width:s.width,height:s.height}):
this.clipRect=b.clipRect(s);if(m)g?g.animate(g.crisp(null,q,o,n,r)):this.plotBorder=b.rect(q,o,n,r,0,-m).attr({stroke:a.plotBorderColor,"stroke-width":m,zIndex:1}).add();this.isDirtyBox=!1},propFromSeries:function(){var a=this,b=a.options.chart,c,d=a.options.series,e,f;n(["inverted","angular","polar"],function(g){c=X[b.type||b.defaultSeriesType];f=a[g]||b[g]||c&&c.prototype[g];for(e=d&&d.length;!f&&e--;)(c=X[d[e].type])&&c.prototype[g]&&(f=!0);a[g]=f})},linkSeries:function(){var a=this,b=a.series;
n(b,function(a){a.linkedSeries.length=0});n(b,function(b){var d=b.options.linkedTo;if(ea(d)&&(d=d===":previous"?a.series[b.index-1]:a.get(d)))d.linkedSeries.push(b),b.linkedParent=d})},render:function(){var a=this,b=a.axes,c=a.renderer,d=a.options,e=d.labels,f=d.credits,g;a.setTitle();a.legend=new fb(a,d.legend);a.getStacks();n(b,function(a){a.setScale()});a.getMargins();a.maxTicks=null;n(b,function(a){a.setTickPositions(!0);a.setMaxTicks()});a.adjustTickAmounts();a.getMargins();a.drawChartBox();
a.hasCartesianSeries&&n(b,function(a){a.render()});if(!a.seriesGroup)a.seriesGroup=c.g("series-group").attr({zIndex:3}).add();n(a.series,function(a){a.translate();a.setTooltipPoints();a.render()});e.items&&n(e.items,function(b){var d=s(e.style,b.style),f=y(d.left)+a.plotLeft,g=y(d.top)+a.plotTop+12;delete d.left;delete d.top;c.text(b.html,f,g).attr({zIndex:2}).css(d).add()});if(f.enabled&&!a.credits)g=f.href,a.credits=c.text(f.text,0,0).on("click",function(){if(g)location.href=g}).attr({align:f.position.align,
zIndex:8}).css(f.style).add().align(f.position);a.hasRendered=!0},destroy:function(){var a=this,b=a.axes,c=a.series,d=a.container,e,f=d&&d.parentNode;A(a,"destroy");Ga[a.index]=v;a.renderTo.removeAttribute("data-highcharts-chart");$(a);for(e=b.length;e--;)b[e]=b[e].destroy();for(e=c.length;e--;)c[e]=c[e].destroy();n("title,subtitle,chartBackground,plotBackground,plotBGImage,plotBorder,seriesGroup,clipRect,credits,pointer,scroller,rangeSelector,legend,resetZoomButton,tooltip,renderer".split(","),function(b){var c=
a[b];c&&c.destroy&&(a[b]=c.destroy())});if(d)d.innerHTML="",$(d),f&&Ta(d);for(e in a)delete a[e]},isReadyToRender:function(){var a=this;return!W&&N==N.top&&z.readyState!=="complete"||ca&&!N.canvg?(ca?Tb.push(function(){a.firstRender()},a.options.global.canvasToolsURL):z.attachEvent("onreadystatechange",function(){z.detachEvent("onreadystatechange",a.firstRender);z.readyState==="complete"&&a.firstRender()}),!1):!0},firstRender:function(){var a=this,b=a.options,c=a.callback;if(a.isReadyToRender())a.getContainer(),
A(a,"init"),a.resetMargins(),a.setChartSize(),a.propFromSeries(),a.getAxes(),n(b.series||[],function(b){a.initSeries(b)}),a.linkSeries(),A(a,"beforeRender"),a.pointer=new xb(a,b),a.render(),a.renderer.draw(),c&&c.apply(a,[a]),n(a.callbacks,function(b){b.apply(a,[a])}),a.cloneRenderTo(!0),A(a,"load")},splashArray:function(a,b){var c=b[a],c=T(c)?c:[c,c,c,c];return[o(b[a+"Top"],c[0]),o(b[a+"Right"],c[1]),o(b[a+"Bottom"],c[2]),o(b[a+"Left"],c[3])]}};yb.prototype.callbacks=[];var Pa=function(){};Pa.prototype=
{init:function(a,b,c){this.series=a;this.applyOptions(b,c);this.pointAttr={};if(a.options.colorByPoint&&(b=a.options.colors||a.chart.options.colors,this.color=this.color||b[a.colorCounter++],a.colorCounter===b.length))a.colorCounter=0;a.chart.pointCount++;return this},applyOptions:function(a,b){var c=this.series,d=c.pointValKey,a=Pa.prototype.optionsToObject.call(this,a);s(this,a);this.options=this.options?s(this.options,a):a;if(d)this.y=this[d];if(this.x===v&&c)this.x=b===v?c.autoIncrement():b;return this},
optionsToObject:function(a){var b={},c=this.series,d=c.pointArrayMap||["y"],e=d.length,f=0,g=0;if(typeof a==="number"||a===null)b[d[0]]=a;else if(Ia(a)){if(a.length>e){c=typeof a[0];if(c==="string")b.name=a[0];else if(c==="number")b.x=a[0];f++}for(;g<e;)b[d[g++]]=a[f++]}else if(typeof a==="object"){b=a;if(a.dataLabels)c._hasPointLabels=!0;if(a.marker)c._hasPointMarkers=!0}return b},destroy:function(){var a=this.series.chart,b=a.hoverPoints,c;a.pointCount--;if(b&&(this.setState(),ga(b,this),!b.length))a.hoverPoints=
null;if(this===a.hoverPoint)this.onMouseOut();if(this.graphic||this.dataLabel)$(this),this.destroyElements();this.legendItem&&a.legend.destroyItem(this);for(c in this)this[c]=null},destroyElements:function(){for(var a="graphic,dataLabel,dataLabelUpper,group,connector,shadowGroup".split(","),b,c=6;c--;)b=a[c],this[b]&&(this[b]=this[b].destroy())},getLabelConfig:function(){return{x:this.category,y:this.y,key:this.name||this.category,series:this.series,point:this,percentage:this.percentage,total:this.total||
this.stackTotal}},select:function(a,b){var c=this,d=c.series,e=d.chart,a=o(a,!c.selected);c.firePointEvent(a?"select":"unselect",{accumulate:b},function(){c.selected=c.options.selected=a;d.options.data[pa(c,d.data)]=c.options;c.setState(a&&"select");b||n(e.getSelectedPoints(),function(a){if(a.selected&&a!==c)a.selected=a.options.selected=!1,d.options.data[pa(a,d.data)]=a.options,a.setState(""),a.firePointEvent("unselect")})})},onMouseOver:function(a){var b=this.series,c=b.chart,d=c.tooltip,e=c.hoverPoint;
if(e&&e!==this)e.onMouseOut();this.firePointEvent("mouseOver");d&&(!d.shared||b.noSharedTooltip)&&d.refresh(this,a);this.setState("hover");c.hoverPoint=this},onMouseOut:function(){var a=this.series.chart,b=a.hoverPoints;if(!b||pa(this,b)===-1)this.firePointEvent("mouseOut"),this.setState(),a.hoverPoint=null},tooltipFormatter:function(a){var b=this.series,c=b.tooltipOptions,d=o(c.valueDecimals,""),e=c.valuePrefix||"",f=c.valueSuffix||"";n(b.pointArrayMap||["y"],function(b){b="{point."+b;if(e||f)a=
a.replace(b+"}",e+b+"}"+f);a=a.replace(b+"}",b+":,."+d+"f}")});return Ca(a,{point:this,series:this.series})},update:function(a,b,c){var d=this,e=d.series,f=d.graphic,g,h=e.data,i=e.chart,j=e.options,b=o(b,!0);d.firePointEvent("update",{options:a},function(){d.applyOptions(a);if(T(a)&&(e.getAttribs(),f))a&&a.marker&&a.marker.symbol?d.graphic=f.destroy():f.attr(d.pointAttr[d.state||""]);g=pa(d,h);e.xData[g]=d.x;e.yData[g]=e.toYData?e.toYData(d):d.y;e.zData[g]=d.z;j.data[g]=d.options;e.isDirty=e.isDirtyData=
!0;if(!e.fixedBox&&e.hasCartesianSeries)i.isDirtyBox=!0;j.legendType==="point"&&i.legend.destroyItem(d);b&&i.redraw(c)})},remove:function(a,b){var c=this,d=c.series,e=d.points,f=d.chart,g,h=d.data;La(b,f);a=o(a,!0);c.firePointEvent("remove",null,function(){g=pa(c,h);h.length===e.length&&e.splice(g,1);h.splice(g,1);d.options.data.splice(g,1);d.xData.splice(g,1);d.yData.splice(g,1);d.zData.splice(g,1);c.destroy();d.isDirty=!0;d.isDirtyData=!0;a&&f.redraw()})},firePointEvent:function(a,b,c){var d=this,
e=this.series.options;(e.point.events[a]||d.options&&d.options.events&&d.options.events[a])&&this.importEvents();a==="click"&&e.allowPointSelect&&(c=function(a){d.select(null,a.ctrlKey||a.metaKey||a.shiftKey)});A(this,a,b,c)},importEvents:function(){if(!this.hasImportedEvents){var a=x(this.series.options.point,this.options).events,b;this.events=a;for(b in a)K(this,b,a[b]);this.hasImportedEvents=!0}},setState:function(a){var b=this.plotX,c=this.plotY,d=this.series,e=d.options.states,f=Z[d.type].marker&&
d.options.marker,g=f&&!f.enabled,h=f&&f.states[a],i=h&&h.enabled===!1,j=d.stateMarkerGraphic,k=this.marker||{},l=d.chart,m=this.pointAttr,a=a||"";if(!(a===this.state||this.selected&&a!=="select"||e[a]&&e[a].enabled===!1||a&&(i||g&&!h.enabled)||a&&k.states&&k.states[a]&&k.states[a].enabled===!1)){if(this.graphic)e=f&&this.graphic.symbolName&&m[a].r,this.graphic.attr(x(m[a],e?{x:b-e,y:c-e,width:2*e,height:2*e}:{}));else{if(a&&h)e=h.radius,k=k.symbol||d.symbol,j&&j.currentSymbol!==k&&(j=j.destroy()),
j?j.attr({x:b-e,y:c-e}):(d.stateMarkerGraphic=j=l.renderer.symbol(k,b-e,c-e,2*e,2*e).attr(m[a]).add(d.markerGroup),j.currentSymbol=k);if(j)j[a&&l.isInsidePlot(b,c)?"show":"hide"]()}this.state=a}}};var Q=function(){};Q.prototype={isCartesian:!0,type:"line",pointClass:Pa,sorted:!0,requireSorting:!0,pointAttrToOptions:{stroke:"lineColor","stroke-width":"lineWidth",fill:"fillColor",r:"radius"},colorCounter:0,init:function(a,b){var c,d,e=a.series;this.chart=a;this.options=b=this.setOptions(b);this.linkedSeries=
[];this.bindAxes();s(this,{name:b.name,state:"",pointAttr:{},visible:b.visible!==!1,selected:b.selected===!0});if(ca)b.animation=!1;d=b.events;for(c in d)K(this,c,d[c]);if(d&&d.click||b.point&&b.point.events&&b.point.events.click||b.allowPointSelect)a.runTrackerClick=!0;this.getColor();this.getSymbol();this.setData(b.data,!1);if(this.isCartesian)a.hasCartesianSeries=!0;e.push(this);this._i=e.length-1;Kb(e,function(a,b){return o(a.options.index,a._i)-o(b.options.index,a._i)});n(e,function(a,b){a.index=
b;a.name=a.name||"Series "+(b+1)})},bindAxes:function(){var a=this,b=a.options,c=a.chart,d;a.isCartesian&&n(["xAxis","yAxis"],function(e){n(c[e],function(c){d=c.options;if(b[e]===d.index||b[e]!==v&&b[e]===d.id||b[e]===v&&d.index===0)c.series.push(a),a[e]=c,c.isDirty=!0});a[e]||ka(18,!0)})},autoIncrement:function(){var a=this.options,b=this.xIncrement,b=o(b,a.pointStart,0);this.pointInterval=o(this.pointInterval,a.pointInterval,1);this.xIncrement=b+this.pointInterval;return b},getSegments:function(){var a=
-1,b=[],c,d=this.points,e=d.length;if(e)if(this.options.connectNulls){for(c=e;c--;)d[c].y===null&&d.splice(c,1);d.length&&(b=[d])}else n(d,function(c,g){c.y===null?(g>a+1&&b.push(d.slice(a+1,g)),a=g):g===e-1&&b.push(d.slice(a+1,g+1))});this.segments=b},setOptions:function(a){var b=this.chart.options,c=b.plotOptions,d=c[this.type];this.userOptions=a;a=x(d,c.series,a);this.tooltipOptions=x(b.tooltip,a.tooltip);d.marker===null&&delete a.marker;return a},getColor:function(){var a=this.options,b=this.userOptions,
c=this.chart.options.colors,d=this.chart.counters,e;e=a.color||Z[this.type].color;if(!e&&!a.colorByPoint)u(b._colorIndex)?a=b._colorIndex:(b._colorIndex=d.color,a=d.color++),e=c[a];this.color=e;d.wrapColor(c.length)},getSymbol:function(){var a=this.userOptions,b=this.options.marker,c=this.chart,d=c.options.symbols,c=c.counters;this.symbol=b.symbol;if(!this.symbol)u(a._symbolIndex)?a=a._symbolIndex:(a._symbolIndex=c.symbol,a=c.symbol++),this.symbol=d[a];if(/^url/.test(this.symbol))b.radius=0;c.wrapSymbol(d.length)},
drawLegendSymbol:function(a){var b=this.options,c=b.marker,d=a.options,e;e=d.symbolWidth;var f=this.chart.renderer,g=this.legendGroup,a=a.baseline-t(f.fontMetrics(d.itemStyle.fontSize).b*0.3);if(b.lineWidth){d={"stroke-width":b.lineWidth};if(b.dashStyle)d.dashstyle=b.dashStyle;this.legendLine=f.path(["M",0,a,"L",e,a]).attr(d).add(g)}if(c&&c.enabled)b=c.radius,this.legendSymbol=e=f.symbol(this.symbol,e/2-b,a-b,2*b,2*b).add(g),e.isMarker=!0},addPoint:function(a,b,c,d){var e=this.options,f=this.data,
g=this.graph,h=this.area,i=this.chart,j=this.xData,k=this.yData,l=this.zData,m=this.xAxis&&this.xAxis.names,p=g&&g.shift||0,q=e.data,r;La(d,i);c&&n([g,h,this.graphNeg,this.areaNeg],function(a){if(a)a.shift=p+1});if(h)h.isArea=!0;b=o(b,!0);d={series:this};this.pointClass.prototype.applyOptions.apply(d,[a]);g=d.x;h=j.length;if(this.requireSorting&&g<j[h-1])for(r=!0;h&&j[h-1]>g;)h--;j.splice(h,0,g);k.splice(h,0,this.toYData?this.toYData(d):d.y);l.splice(h,0,d.z);if(m)m[g]=d.name;q.splice(h,0,a);r&&(this.data.splice(h,
0,null),this.processData());e.legendType==="point"&&this.generatePoints();c&&(f[0]&&f[0].remove?f[0].remove(!1):(f.shift(),j.shift(),k.shift(),l.shift(),q.shift()));this.isDirtyData=this.isDirty=!0;b&&(this.getAttribs(),i.redraw())},setData:function(a,b){var c=this.points,d=this.options,e=this.chart,f=null,g=this.xAxis,h=g&&g.names,i;this.xIncrement=null;this.pointRange=g&&g.categories?1:d.pointRange;this.colorCounter=0;var j=[],k=[],l=[],m=a?a.length:[];i=o(d.turboThreshold,1E3);var p=this.pointArrayMap,
p=p&&p.length,q=!!this.toYData;if(i&&m>i){for(i=0;f===null&&i<m;)f=a[i],i++;if(ra(f)){h=o(d.pointStart,0);d=o(d.pointInterval,1);for(i=0;i<m;i++)j[i]=h,k[i]=a[i],h+=d;this.xIncrement=h}else if(Ia(f))if(p)for(i=0;i<m;i++)d=a[i],j[i]=d[0],k[i]=d.slice(1,p+1);else for(i=0;i<m;i++)d=a[i],j[i]=d[0],k[i]=d[1];else ka(12)}else for(i=0;i<m;i++)if(a[i]!==v&&(d={series:this},this.pointClass.prototype.applyOptions.apply(d,[a[i]]),j[i]=d.x,k[i]=q?this.toYData(d):d.y,l[i]=d.z,h&&d.name))h[d.x]=d.name;ea(k[0])&&
ka(14,!0);this.data=[];this.options.data=a;this.xData=j;this.yData=k;this.zData=l;for(i=c&&c.length||0;i--;)c[i]&&c[i].destroy&&c[i].destroy();if(g)g.minRange=g.userMinRange;this.isDirty=this.isDirtyData=e.isDirtyBox=!0;o(b,!0)&&e.redraw(!1)},remove:function(a,b){var c=this,d=c.chart,a=o(a,!0);if(!c.isRemoving)c.isRemoving=!0,A(c,"remove",null,function(){c.destroy();d.isDirtyLegend=d.isDirtyBox=!0;d.linkSeries();a&&d.redraw(b)});c.isRemoving=!1},processData:function(a){var b=this.xData,c=this.yData,
d=b.length,e;e=0;var f,g,h=this.xAxis,i=this.options,j=i.cropThreshold,k=this.isCartesian;if(k&&!this.isDirty&&!h.isDirty&&!this.yAxis.isDirty&&!a)return!1;if(k&&this.sorted&&(!j||d>j||this.forceCrop))if(a=h.min,h=h.max,b[d-1]<a||b[0]>h)b=[],c=[];else if(b[0]<a||b[d-1]>h)e=this.cropData(this.xData,this.yData,a,h),b=e.xData,c=e.yData,e=e.start,f=!0;for(h=b.length-1;h>=0;h--)d=b[h]-b[h-1],d>0&&(g===v||d<g)?g=d:d<0&&this.requireSorting&&ka(15);this.cropped=f;this.cropStart=e;this.processedXData=b;this.processedYData=
c;if(i.pointRange===null)this.pointRange=g||1;this.closestPointRange=g},cropData:function(a,b,c,d){var e=a.length,f=0,g=e,h=o(this.cropShoulder,1),i;for(i=0;i<e;i++)if(a[i]>=c){f=r(0,i-h);break}for(;i<e;i++)if(a[i]>d){g=i+h;break}return{xData:a.slice(f,g),yData:b.slice(f,g),start:f,end:g}},generatePoints:function(){var a=this.options.data,b=this.data,c,d=this.processedXData,e=this.processedYData,f=this.pointClass,g=d.length,h=this.cropStart||0,i,j=this.hasGroupedData,k,l=[],m;if(!b&&!j)b=[],b.length=
a.length,b=this.data=b;for(m=0;m<g;m++)i=h+m,j?l[m]=(new f).init(this,[d[m]].concat(ja(e[m]))):(b[i]?k=b[i]:a[i]!==v&&(b[i]=k=(new f).init(this,a[i],d[m])),l[m]=k);if(b&&(g!==(c=b.length)||j))for(m=0;m<c;m++)if(m===h&&!j&&(m+=g),b[m])b[m].destroyElements(),b[m].plotX=v;this.data=b;this.points=l},setStackedPoints:function(){if(this.options.stacking&&!(this.visible!==!0&&this.chart.options.chart.ignoreHiddenSeries!==!1)){var a=this.processedXData,b=this.processedYData,c=[],d=b.length,e=this.options,
f=e.threshold,g=e.stack,e=e.stacking,h=this.stackKey,i="-"+h,j=this.negStacks,k=this.yAxis,l=k.stacks,m=k.oldStacks,p,q,o,n,t;for(o=0;o<d;o++){n=a[o];t=b[o];q=(p=j&&t<f)?i:h;l[q]||(l[q]={});if(!l[q][n])m[q]&&m[q][n]?(l[q][n]=m[q][n],l[q][n].total=null):l[q][n]=new Mb(k,k.options.stackLabels,p,n,g,e);q=l[q][n];q.points[this.index]=[q.cum||0];e==="percent"?(p=p?h:i,j&&l[p]&&l[p][n]?(p=l[p][n],q.total=p.total=r(p.total,q.total)+M(t)||0):q.total+=M(t)||0):q.total+=t||0;q.cum=(q.cum||0)+(t||0);q.points[this.index].push(q.cum);
c[o]=q.cum}if(e==="percent")k.usePercentage=!0;this.stackedYData=c;k.oldStacks={}}},setPercentStacks:function(){var a=this,b=a.stackKey,c=a.yAxis.stacks;n([b,"-"+b],function(b){var d;for(var e=a.xData.length,f,g;e--;)if(f=a.xData[e],d=(g=c[b]&&c[b][f])&&g.points[a.index],f=d)g=g.total?100/g.total:0,f[0]=ia(f[0]*g),f[1]=ia(f[1]*g),a.stackedYData[e]=f[1]})},getExtremes:function(){var a=this.yAxis,b=this.processedXData,c=this.stackedYData||this.processedYData,d=c.length,e=[],f=0,g=this.xAxis.getExtremes(),
h=g.min,g=g.max,i,j,k,l;for(l=0;l<d;l++)if(j=b[l],k=c[l],i=k!==null&&k!==v&&(!a.isLog||k.length||k>0),j=this.getExtremesFromAll||this.cropped||(b[l+1]||j)>=h&&(b[l-1]||j)<=g,i&&j)if(i=k.length)for(;i--;)k[i]!==null&&(e[f++]=k[i]);else e[f++]=k;this.dataMin=o(void 0,Ja(e));this.dataMax=o(void 0,ua(e))},translate:function(){this.processedXData||this.processData();this.generatePoints();for(var a=this.options,b=a.stacking,c=this.xAxis,d=c.categories,e=this.yAxis,f=this.points,g=f.length,h=!!this.modifyValue,
i=a.pointPlacement,j=i==="between"||ra(i),k=a.threshold,a=0;a<g;a++){var l=f[a],m=l.x,p=l.y,q=l.low,n=e.stacks[(this.negStacks&&p<k?"-":"")+this.stackKey];if(e.isLog&&p<=0)l.y=p=null;l.plotX=c.translate(m,0,0,0,1,i,this.type==="flags");if(b&&this.visible&&n&&n[m])n=n[m],p=n.points[this.index],q=p[0],p=p[1],q===0&&(q=o(k,e.min)),e.isLog&&q<=0&&(q=null),l.total=l.stackTotal=n.total,l.percentage=b==="percent"&&l.y/n.total*100,l.stackY=p,n.setOffset(this.pointXOffset||0,this.barW||0);l.yBottom=u(q)?e.translate(q,
0,1,0,1):null;h&&(p=this.modifyValue(p,l));l.plotY=typeof p==="number"&&p!==Infinity?e.translate(p,0,1,0,1):v;l.clientX=j?c.translate(m,0,0,0,1):l.plotX;l.negative=l.y<(k||0);l.category=d&&d[l.x]!==v?d[l.x]:l.x}this.getSegments()},setTooltipPoints:function(a){var b=[],c,d,e=this.xAxis,f=e&&e.getExtremes(),g=e?e.tooltipLen||e.len:this.chart.plotSizeX,h,i,j=[];if(this.options.enableMouseTracking!==!1){if(a)this.tooltipPoints=null;n(this.segments||this.points,function(a){b=b.concat(a)});e&&e.reversed&&
(b=b.reverse());this.orderTooltipPoints&&this.orderTooltipPoints(b);a=b.length;for(i=0;i<a;i++)if(e=b[i],c=e.x,c>=f.min&&c<=f.max){h=b[i+1];c=d===v?0:d+1;for(d=b[i+1]?J(r(0,P((e.clientX+(h?h.wrappedClientX||h.clientX:g))/2)),g):g;c>=0&&c<=d;)j[c++]=e}this.tooltipPoints=j}},tooltipHeaderFormatter:function(a){var b=this.tooltipOptions,c=b.xDateFormat,d=b.dateTimeLabelFormats,e=this.xAxis,f=e&&e.options.type==="datetime",b=b.headerFormat,e=e&&e.closestPointRange,g;if(f&&!c)if(e)for(g in D){if(D[g]>=
e){c=d[g];break}}else c=d.day;f&&c&&ra(a.key)&&(b=b.replace("{point.key}","{point.key:"+c+"}"));return Ca(b,{point:a,series:this})},onMouseOver:function(){var a=this.chart,b=a.hoverSeries;if(b&&b!==this)b.onMouseOut();this.options.events.mouseOver&&A(this,"mouseOver");this.setState("hover");a.hoverSeries=this},onMouseOut:function(){var a=this.options,b=this.chart,c=b.tooltip,d=b.hoverPoint;if(d)d.onMouseOut();this&&a.events.mouseOut&&A(this,"mouseOut");c&&!a.stickyTracking&&(!c.shared||this.noSharedTooltip)&&
c.hide();this.setState();b.hoverSeries=null},animate:function(a){var b=this,c=b.chart,d=c.renderer,e;e=b.options.animation;var f=c.clipBox,g=c.inverted,h;if(e&&!T(e))e=Z[b.type].animation;h="_sharedClip"+e.duration+e.easing;if(a)a=c[h],e=c[h+"m"],a||(c[h]=a=d.clipRect(s(f,{width:0})),c[h+"m"]=e=d.clipRect(-99,g?-c.plotLeft:-c.plotTop,99,g?c.chartWidth:c.chartHeight)),b.group.clip(a),b.markerGroup.clip(e),b.sharedClipKey=h;else{if(a=c[h])a.animate({width:c.plotSizeX},e),c[h+"m"].animate({width:c.plotSizeX+
99},e);b.animate=null;b.animationTimeout=setTimeout(function(){b.afterAnimate()},e.duration)}},afterAnimate:function(){var a=this.chart,b=this.sharedClipKey,c=this.group;c&&this.options.clip!==!1&&(c.clip(a.clipRect),this.markerGroup.clip());setTimeout(function(){b&&a[b]&&(a[b]=a[b].destroy(),a[b+"m"]=a[b+"m"].destroy())},100)},drawPoints:function(){var a,b=this.points,c=this.chart,d,e,f,g,h,i,j,k,l=this.options.marker,m,n=this.markerGroup;if(l.enabled||this._hasPointMarkers)for(f=b.length;f--;)if(g=
b[f],d=P(g.plotX),e=g.plotY,k=g.graphic,i=g.marker||{},a=l.enabled&&i.enabled===v||i.enabled,m=c.isInsidePlot(t(d),e,c.inverted),a&&e!==v&&!isNaN(e)&&g.y!==null)if(a=g.pointAttr[g.selected?"select":""],h=a.r,i=o(i.symbol,this.symbol),j=i.indexOf("url")===0,k)k.attr({visibility:m?W?"inherit":"visible":"hidden"}).animate(s({x:d-h,y:e-h},k.symbolName?{width:2*h,height:2*h}:{}));else{if(m&&(h>0||j))g.graphic=c.renderer.symbol(i,d-h,e-h,2*h,2*h).attr(a).add(n)}else if(k)g.graphic=k.destroy()},convertAttribs:function(a,
b,c,d){var e=this.pointAttrToOptions,f,g,h={},a=a||{},b=b||{},c=c||{},d=d||{};for(f in e)g=e[f],h[f]=o(a[g],b[f],c[f],d[f]);return h},getAttribs:function(){var a=this,b=a.options,c=Z[a.type].marker?b.marker:b,d=c.states,e=d.hover,f,g=a.color,h={stroke:g,fill:g},i=a.points||[],j=[],k,l=a.pointAttrToOptions,m=b.negativeColor,p=c.lineColor,q;b.marker?(e.radius=e.radius||c.radius+2,e.lineWidth=e.lineWidth||c.lineWidth+1):e.color=e.color||qa(e.color||g).brighten(e.brightness).get();j[""]=a.convertAttribs(c,
h);n(["hover","select"],function(b){j[b]=a.convertAttribs(d[b],j[""])});a.pointAttr=j;for(g=i.length;g--;){h=i[g];if((c=h.options&&h.options.marker||h.options)&&c.enabled===!1)c.radius=0;if(h.negative&&m)h.color=h.fillColor=m;f=b.colorByPoint||h.color;if(h.options)for(q in l)u(c[l[q]])&&(f=!0);if(f){c=c||{};k=[];d=c.states||{};f=d.hover=d.hover||{};if(!b.marker)f.color=qa(f.color||h.color).brighten(f.brightness||e.brightness).get();k[""]=a.convertAttribs(s({color:h.color,fillColor:h.color,lineColor:p===
null?h.color:v},c),j[""]);k.hover=a.convertAttribs(d.hover,j.hover,k[""]);k.select=a.convertAttribs(d.select,j.select,k[""])}else k=j;h.pointAttr=k}},update:function(a,b){var c=this.chart,d=this.type,e=X[d].prototype,f,a=x(this.userOptions,{animation:!1,index:this.index,pointStart:this.xData[0]},{data:this.options.data},a);this.remove(!1);for(f in e)e.hasOwnProperty(f)&&(this[f]=v);s(this,X[a.type||d].prototype);this.init(c,a);o(b,!0)&&c.redraw(!1)},destroy:function(){var a=this,b=a.chart,c=/AppleWebKit\/533/.test(na),
d,e,f=a.data||[],g,h,i;A(a,"destroy");$(a);n(["xAxis","yAxis"],function(b){if(i=a[b])ga(i.series,a),i.isDirty=i.forceRedraw=!0,i.stacks={}});a.legendItem&&a.chart.legend.destroyItem(a);for(e=f.length;e--;)(g=f[e])&&g.destroy&&g.destroy();a.points=null;clearTimeout(a.animationTimeout);n("area,graph,dataLabelsGroup,group,markerGroup,tracker,graphNeg,areaNeg,posClip,negClip".split(","),function(b){a[b]&&(d=c&&b==="group"?"hide":"destroy",a[b][d]())});if(b.hoverSeries===a)b.hoverSeries=null;ga(b.series,
a);for(h in a)delete a[h]},drawDataLabels:function(){var a=this,b=a.options,c=b.cursor,d=b.dataLabels,b=a.points,e,f,g,h;if(d.enabled||a._hasPointLabels)a.dlProcessOptions&&a.dlProcessOptions(d),h=a.plotGroup("dataLabelsGroup","data-labels",a.visible?"visible":"hidden",d.zIndex||6),f=d,n(b,function(b){var j,k=b.dataLabel,l,m,n=b.connector,q=!0;e=b.options&&b.options.dataLabels;j=o(e&&e.enabled,f.enabled);if(k&&!j)b.dataLabel=k.destroy();else if(j){d=x(f,e);j=d.rotation;l=b.getLabelConfig();g=d.format?
Ca(d.format,l):d.formatter.call(l,d);d.style.color=o(d.color,d.style.color,a.color,"black");if(k)if(u(g))k.attr({text:g}),q=!1;else{if(b.dataLabel=k=k.destroy(),n)b.connector=n.destroy()}else if(u(g)){k={fill:d.backgroundColor,stroke:d.borderColor,"stroke-width":d.borderWidth,r:d.borderRadius||0,rotation:j,padding:d.padding,zIndex:1};for(m in k)k[m]===v&&delete k[m];k=b.dataLabel=a.chart.renderer[j?"text":"label"](g,0,-999,null,null,null,d.useHTML).attr(k).css(s(d.style,c&&{cursor:c})).add(h).shadow(d.shadow)}k&&
a.alignDataLabel(b,k,d,null,q)}})},alignDataLabel:function(a,b,c,d,e){var f=this.chart,g=f.inverted,h=o(a.plotX,-999),i=o(a.plotY,-999),j=b.getBBox();if(a=this.visible&&f.isInsidePlot(a.plotX,a.plotY,g))d=s({x:g?f.plotWidth-i:h,y:t(g?f.plotHeight-h:i),width:0,height:0},d),s(c,{width:j.width,height:j.height}),c.rotation?(g={align:c.align,x:d.x+c.x+d.width/2,y:d.y+c.y+d.height/2},b[e?"attr":"animate"](g)):(b.align(c,null,d),g=b.alignAttr,o(c.overflow,"justify")==="justify"?this.justifyDataLabel(b,c,
g,j,d,e):o(c.crop,!0)&&(a=f.isInsidePlot(g.x,g.y)&&f.isInsidePlot(g.x+j.width,g.y+j.height)));a||b.attr({y:-999})},justifyDataLabel:function(a,b,c,d,e,f){var g=this.chart,h=b.align,i=b.verticalAlign,j,k;j=c.x;if(j<0)h==="right"?b.align="left":b.x=-j,k=!0;j=c.x+d.width;if(j>g.plotWidth)h==="left"?b.align="right":b.x=g.plotWidth-j,k=!0;j=c.y;if(j<0)i==="bottom"?b.verticalAlign="top":b.y=-j,k=!0;j=c.y+d.height;if(j>g.plotHeight)i==="top"?b.verticalAlign="bottom":b.y=g.plotHeight-j,k=!0;if(k)a.placed=
!f,a.align(b,null,e)},getSegmentPath:function(a){var b=this,c=[],d=b.options.step;n(a,function(e,f){var g=e.plotX,h=e.plotY,i;b.getPointSpline?c.push.apply(c,b.getPointSpline(a,e,f)):(c.push(f?"L":"M"),d&&f&&(i=a[f-1],d==="right"?c.push(i.plotX,h):d==="center"?c.push((i.plotX+g)/2,i.plotY,(i.plotX+g)/2,h):c.push(g,i.plotY)),c.push(e.plotX,e.plotY))});return c},getGraphPath:function(){var a=this,b=[],c,d=[];n(a.segments,function(e){c=a.getSegmentPath(e);e.length>1?b=b.concat(c):d.push(e[0])});a.singlePoints=
d;return a.graphPath=b},drawGraph:function(){var a=this,b=this.options,c=[["graph",b.lineColor||this.color]],d=b.lineWidth,e=b.dashStyle,f=b.linecap!=="square",g=this.getGraphPath(),h=b.negativeColor;h&&c.push(["graphNeg",h]);n(c,function(c,h){var k=c[0],l=a[k];if(l)Wa(l),l.animate({d:g});else if(d&&g.length)l={stroke:c[1],"stroke-width":d,zIndex:1},e?l.dashstyle=e:f&&(l["stroke-linecap"]=l["stroke-linejoin"]="round"),a[k]=a.chart.renderer.path(g).attr(l).add(a.group).shadow(!h&&b.shadow)})},clipNeg:function(){var a=
this.options,b=this.chart,c=b.renderer,d=a.negativeColor||a.negativeFillColor,e,f=this.graph,g=this.area,h=this.posClip,i=this.negClip;e=b.chartWidth;var j=b.chartHeight,k=r(e,j),l=this.yAxis;if(d&&(f||g)){d=t(l.toPixels(a.threshold||0,!0));a={x:0,y:0,width:k,height:d};k={x:0,y:d,width:k,height:k};if(b.inverted)a.height=k.y=b.plotWidth-d,c.isVML&&(a={x:b.plotWidth-d-b.plotLeft,y:0,width:e,height:j},k={x:d+b.plotLeft-e,y:0,width:b.plotLeft+d,height:e});l.reversed?(b=k,e=a):(b=a,e=k);h?(h.animate(b),
i.animate(e)):(this.posClip=h=c.clipRect(b),this.negClip=i=c.clipRect(e),f&&this.graphNeg&&(f.clip(h),this.graphNeg.clip(i)),g&&(g.clip(h),this.areaNeg.clip(i)))}},invertGroups:function(){function a(){var a={width:b.yAxis.len,height:b.xAxis.len};n(["group","markerGroup"],function(c){b[c]&&b[c].attr(a).invert()})}var b=this,c=b.chart;if(b.xAxis)K(c,"resize",a),K(b,"destroy",function(){$(c,"resize",a)}),a(),b.invertGroups=a},plotGroup:function(a,b,c,d,e){var f=this[a],g=!f;g&&(this[a]=f=this.chart.renderer.g(b).attr({visibility:c,
zIndex:d||0.1}).add(e));f[g?"attr":"animate"](this.getPlotBox());return f},getPlotBox:function(){return{translateX:this.xAxis?this.xAxis.left:this.chart.plotLeft,translateY:this.yAxis?this.yAxis.top:this.chart.plotTop,scaleX:1,scaleY:1}},render:function(){var a=this.chart,b,c=this.options,d=c.animation&&!!this.animate&&a.renderer.isSVG,e=this.visible?"visible":"hidden",f=c.zIndex,g=this.hasRendered,h=a.seriesGroup;b=this.plotGroup("group","series",e,f,h);this.markerGroup=this.plotGroup("markerGroup",
"markers",e,f,h);d&&this.animate(!0);this.getAttribs();b.inverted=this.isCartesian?a.inverted:!1;this.drawGraph&&(this.drawGraph(),this.clipNeg());this.drawDataLabels();this.drawPoints();this.options.enableMouseTracking!==!1&&this.drawTracker();a.inverted&&this.invertGroups();c.clip!==!1&&!this.sharedClipKey&&!g&&b.clip(a.clipRect);d?this.animate():g||this.afterAnimate();this.isDirty=this.isDirtyData=!1;this.hasRendered=!0},redraw:function(){var a=this.chart,b=this.isDirtyData,c=this.group,d=this.xAxis,
e=this.yAxis;c&&(a.inverted&&c.attr({width:a.plotWidth,height:a.plotHeight}),c.animate({translateX:o(d&&d.left,a.plotLeft),translateY:o(e&&e.top,a.plotTop)}));this.translate();this.setTooltipPoints(!0);this.render();b&&A(this,"updatedData")},setState:function(a){var b=this.options,c=this.graph,d=this.graphNeg,e=b.states,b=b.lineWidth,a=a||"";if(this.state!==a)this.state=a,e[a]&&e[a].enabled===!1||(a&&(b=e[a].lineWidth||b+1),c&&!c.dashstyle&&(a={"stroke-width":b},c.attr(a),d&&d.attr(a)))},setVisible:function(a,
b){var c=this,d=c.chart,e=c.legendItem,f,g=d.options.chart.ignoreHiddenSeries,h=c.visible;f=(c.visible=a=c.userOptions.visible=a===v?!h:a)?"show":"hide";n(["group","dataLabelsGroup","markerGroup","tracker"],function(a){if(c[a])c[a][f]()});if(d.hoverSeries===c)c.onMouseOut();e&&d.legend.colorizeItem(c,a);c.isDirty=!0;c.options.stacking&&n(d.series,function(a){if(a.options.stacking&&a.visible)a.isDirty=!0});n(c.linkedSeries,function(b){b.setVisible(a,!1)});if(g)d.isDirtyBox=!0;b!==!1&&d.redraw();A(c,
f)},show:function(){this.setVisible(!0)},hide:function(){this.setVisible(!1)},select:function(a){this.selected=a=a===v?!this.selected:a;if(this.checkbox)this.checkbox.checked=a;A(this,a?"select":"unselect")},drawTracker:function(){var a=this,b=a.options,c=b.trackByArea,d=[].concat(c?a.areaPath:a.graphPath),e=d.length,f=a.chart,g=f.pointer,h=f.renderer,i=f.options.tooltip.snap,j=a.tracker,k=b.cursor,l=k&&{cursor:k},k=a.singlePoints,m,p=function(){if(f.hoverSeries!==a)a.onMouseOver()};if(e&&!c)for(m=
e+1;m--;)d[m]==="M"&&d.splice(m+1,0,d[m+1]-i,d[m+2],"L"),(m&&d[m]==="M"||m===e)&&d.splice(m,0,"L",d[m-2]+i,d[m-1]);for(m=0;m<k.length;m++)e=k[m],d.push("M",e.plotX-i,e.plotY,"L",e.plotX+i,e.plotY);j?j.attr({d:d}):(a.tracker=h.path(d).attr({"stroke-linejoin":"round",visibility:a.visible?"visible":"hidden",stroke:Qb,fill:c?Qb:S,"stroke-width":b.lineWidth+(c?0:2*i),zIndex:2}).add(a.group),n([a.tracker,a.markerGroup],function(a){a.addClass("highcharts-tracker").on("mouseover",p).on("mouseout",function(a){g.onTrackerMouseOut(a)}).css(l);
if(jb)a.on("touchstart",p)}))}};G=ha(Q);X.line=G;Z.area=x(Y,{threshold:0});G=ha(Q,{type:"area",getSegments:function(){var a=[],b=[],c=[],d=this.xAxis,e=this.yAxis,f=e.stacks[this.stackKey],g={},h,i,j=this.points,k=this.options.connectNulls,l,m,p;if(this.options.stacking&&!this.cropped){for(m=0;m<j.length;m++)g[j[m].x]=j[m];for(p in f)f[p].total!==null&&c.push(+p);c.sort(function(a,b){return a-b});n(c,function(a){if(!k||g[a]&&g[a].y!==null)g[a]?b.push(g[a]):(h=d.translate(a),l=f[a].percent?f[a].total?
f[a].cum*100/f[a].total:0:f[a].cum,i=e.toPixels(l,!0),b.push({y:null,plotX:h,clientX:h,plotY:i,yBottom:i,onMouseOver:oa}))});b.length&&a.push(b)}else Q.prototype.getSegments.call(this),a=this.segments;this.segments=a},getSegmentPath:function(a){var b=Q.prototype.getSegmentPath.call(this,a),c=[].concat(b),d,e=this.options;d=b.length;var f=this.yAxis.getThreshold(e.threshold),g;d===3&&c.push("L",b[1],b[2]);if(e.stacking&&!this.closedStacks)for(d=a.length-1;d>=0;d--)g=o(a[d].yBottom,f),d<a.length-1&&
e.step&&c.push(a[d+1].plotX,g),c.push(a[d].plotX,g);else this.closeSegment(c,a,f);this.areaPath=this.areaPath.concat(c);return b},closeSegment:function(a,b,c){a.push("L",b[b.length-1].plotX,c,"L",b[0].plotX,c)},drawGraph:function(){this.areaPath=[];Q.prototype.drawGraph.apply(this);var a=this,b=this.areaPath,c=this.options,d=c.negativeColor,e=c.negativeFillColor,f=[["area",this.color,c.fillColor]];(d||e)&&f.push(["areaNeg",d,e]);n(f,function(d){var e=d[0],f=a[e];f?f.animate({d:b}):a[e]=a.chart.renderer.path(b).attr({fill:o(d[2],
qa(d[1]).setOpacity(o(c.fillOpacity,0.75)).get()),zIndex:0}).add(a.group)})},drawLegendSymbol:function(a,b){b.legendSymbol=this.chart.renderer.rect(0,a.baseline-11,a.options.symbolWidth,12,2).attr({zIndex:3}).add(b.legendGroup)}});X.area=G;Z.spline=x(Y);F=ha(Q,{type:"spline",getPointSpline:function(a,b,c){var d=b.plotX,e=b.plotY,f=a[c-1],g=a[c+1],h,i,j,k;if(f&&g){a=f.plotY;j=g.plotX;var g=g.plotY,l;h=(1.5*d+f.plotX)/2.5;i=(1.5*e+a)/2.5;j=(1.5*d+j)/2.5;k=(1.5*e+g)/2.5;l=(k-i)*(j-d)/(j-h)+e-k;i+=l;
k+=l;i>a&&i>e?(i=r(a,e),k=2*e-i):i<a&&i<e&&(i=J(a,e),k=2*e-i);k>g&&k>e?(k=r(g,e),i=2*e-k):k<g&&k<e&&(k=J(g,e),i=2*e-k);b.rightContX=j;b.rightContY=k}c?(b=["C",f.rightContX||f.plotX,f.rightContY||f.plotY,h||d,i||e,d,e],f.rightContX=f.rightContY=null):b=["M",d,e];return b}});X.spline=F;Z.areaspline=x(Z.area);la=G.prototype;F=ha(F,{type:"areaspline",closedStacks:!0,getSegmentPath:la.getSegmentPath,closeSegment:la.closeSegment,drawGraph:la.drawGraph,drawLegendSymbol:la.drawLegendSymbol});X.areaspline=
F;Z.column=x(Y,{borderColor:"#FFFFFF",borderWidth:1,borderRadius:0,groupPadding:0.2,marker:null,pointPadding:0.1,minPointLength:0,cropThreshold:50,pointRange:null,states:{hover:{brightness:0.1,shadow:!1},select:{color:"#C0C0C0",borderColor:"#000000",shadow:!1}},dataLabels:{align:null,verticalAlign:null,y:null},stickyTracking:!1,threshold:0});F=ha(Q,{type:"column",pointAttrToOptions:{stroke:"borderColor","stroke-width":"borderWidth",fill:"color",r:"borderRadius"},cropShoulder:0,trackerGroups:["group",
"dataLabelsGroup"],negStacks:!0,init:function(){Q.prototype.init.apply(this,arguments);var a=this,b=a.chart;b.hasRendered&&n(b.series,function(b){if(b.type===a.type)b.isDirty=!0})},getColumnMetrics:function(){var a=this,b=a.options,c=a.xAxis,d=a.yAxis,e=c.reversed,f,g={},h,i=0;b.grouping===!1?i=1:n(a.chart.series,function(b){var c=b.options,e=b.yAxis;if(b.type===a.type&&b.visible&&d.len===e.len&&d.pos===e.pos)c.stacking?(f=b.stackKey,g[f]===v&&(g[f]=i++),h=g[f]):c.grouping!==!1&&(h=i++),b.columnIndex=
h});var c=J(M(c.transA)*(c.ordinalSlope||b.pointRange||c.closestPointRange||1),c.len),j=c*b.groupPadding,k=(c-2*j)/i,l=b.pointWidth,b=u(l)?(k-l)/2:k*b.pointPadding,l=o(l,k-2*b);return a.columnMetrics={width:l,offset:b+(j+((e?i-(a.columnIndex||0):a.columnIndex)||0)*k-c/2)*(e?-1:1)}},translate:function(){var a=this.chart,b=this.options,c=b.borderWidth,d=this.yAxis,e=this.translatedThreshold=d.getThreshold(b.threshold),f=o(b.minPointLength,5),b=this.getColumnMetrics(),g=b.width,h=this.barW=wa(r(g,1+
2*c)),i=this.pointXOffset=b.offset,j=-(c%2?0.5:0),k=c%2?0.5:1;a.renderer.isVML&&a.inverted&&(k+=1);Q.prototype.translate.apply(this);n(this.points,function(a){var b=o(a.yBottom,e),c=J(r(-999-b,a.plotY),d.len+999+b),n=a.plotX+i,u=h,s=J(c,b),v,c=r(c,b)-s;M(c)<f&&f&&(c=f,s=t(M(s-e)>f?b-f:e-(d.translate(a.y,0,1,0,1)<=e?f:0)));a.barX=n;a.pointWidth=g;b=M(n)<0.5;u=t(n+u)+j;n=t(n)+j;u-=n;v=M(s)<0.5;c=t(s+c)+k;s=t(s)+k;c-=s;b&&(n+=1,u-=1);v&&(s-=1,c+=1);a.shapeType="rect";a.shapeArgs={x:n,y:s,width:u,height:c}})},
getSymbol:oa,drawLegendSymbol:G.prototype.drawLegendSymbol,drawGraph:oa,drawPoints:function(){var a=this,b=a.options,c=a.chart.renderer,d;n(a.points,function(e){var f=e.plotY,g=e.graphic;if(f!==v&&!isNaN(f)&&e.y!==null)d=e.shapeArgs,g?(Wa(g),g.animate(x(d))):e.graphic=c[e.shapeType](d).attr(e.pointAttr[e.selected?"select":""]).add(a.group).shadow(b.shadow,null,b.stacking&&!b.borderRadius);else if(g)e.graphic=g.destroy()})},drawTracker:function(){var a=this,b=a.chart,c=b.pointer,d=a.options.cursor,
e=d&&{cursor:d},f=function(c){var d=c.target,e;if(b.hoverSeries!==a)a.onMouseOver();for(;d&&!e;)e=d.point,d=d.parentNode;if(e!==v&&e!==b.hoverPoint)e.onMouseOver(c)};n(a.points,function(a){if(a.graphic)a.graphic.element.point=a;if(a.dataLabel)a.dataLabel.element.point=a});if(!a._hasTracking)n(a.trackerGroups,function(b){if(a[b]&&(a[b].addClass("highcharts-tracker").on("mouseover",f).on("mouseout",function(a){c.onTrackerMouseOut(a)}).css(e),jb))a[b].on("touchstart",f)}),a._hasTracking=!0},alignDataLabel:function(a,
b,c,d,e){var f=this.chart,g=f.inverted,h=a.dlBox||a.shapeArgs,i=a.below||a.plotY>o(this.translatedThreshold,f.plotSizeY),j=o(c.inside,!!this.options.stacking);if(h&&(d=x(h),g&&(d={x:f.plotWidth-d.y-d.height,y:f.plotHeight-d.x-d.width,width:d.height,height:d.width}),!j))g?(d.x+=i?0:d.width,d.width=0):(d.y+=i?d.height:0,d.height=0);c.align=o(c.align,!g||j?"center":i?"right":"left");c.verticalAlign=o(c.verticalAlign,g||j?"middle":i?"top":"bottom");Q.prototype.alignDataLabel.call(this,a,b,c,d,e)},animate:function(a){var b=
this.yAxis,c=this.options,d=this.chart.inverted,e={};if(W)a?(e.scaleY=0.001,a=J(b.pos+b.len,r(b.pos,b.toPixels(c.threshold))),d?e.translateX=a-b.len:e.translateY=a,this.group.attr(e)):(e.scaleY=1,e[d?"translateX":"translateY"]=b.pos,this.group.animate(e,this.options.animation),this.animate=null)},remove:function(){var a=this,b=a.chart;b.hasRendered&&n(b.series,function(b){if(b.type===a.type)b.isDirty=!0});Q.prototype.remove.apply(a,arguments)}});X.column=F;Z.bar=x(Z.column);la=ha(F,{type:"bar",inverted:!0});
X.bar=la;Z.scatter=x(Y,{lineWidth:0,tooltip:{headerFormat:'<span style="font-size: 10px; color:{series.color}">{series.name}</span><br/>',pointFormat:"x: <b>{point.x}</b><br/>y: <b>{point.y}</b><br/>",followPointer:!0},stickyTracking:!1});la=ha(Q,{type:"scatter",sorted:!1,requireSorting:!1,noSharedTooltip:!0,trackerGroups:["markerGroup"],takeOrdinalPosition:!1,drawTracker:F.prototype.drawTracker,setTooltipPoints:oa});X.scatter=la;Z.pie=x(Y,{borderColor:"#FFFFFF",borderWidth:1,center:[null,null],clip:!1,
colorByPoint:!0,dataLabels:{distance:30,enabled:!0,formatter:function(){return this.point.name}},ignoreHiddenPoint:!0,legendType:"point",marker:null,size:null,showInLegend:!1,slicedOffset:10,states:{hover:{brightness:0.1,shadow:!1}},stickyTracking:!1,tooltip:{followPointer:!0}});Y={type:"pie",isCartesian:!1,pointClass:ha(Pa,{init:function(){Pa.prototype.init.apply(this,arguments);var a=this,b;if(a.y<0)a.y=null;s(a,{visible:a.visible!==!1,name:o(a.name,"Slice")});b=function(b){a.slice(b.type==="select")};
K(a,"select",b);K(a,"unselect",b);return a},setVisible:function(a){var b=this,c=b.series,d=c.chart,e;b.visible=b.options.visible=a=a===v?!b.visible:a;c.options.data[pa(b,c.data)]=b.options;e=a?"show":"hide";n(["graphic","dataLabel","connector","shadowGroup"],function(a){if(b[a])b[a][e]()});b.legendItem&&d.legend.colorizeItem(b,a);if(!c.isDirty&&c.options.ignoreHiddenPoint)c.isDirty=!0,d.redraw()},slice:function(a,b,c){var d=this.series;La(c,d.chart);o(b,!0);this.sliced=this.options.sliced=a=u(a)?
a:!this.sliced;d.options.data[pa(this,d.data)]=this.options;a=a?this.slicedTranslation:{translateX:0,translateY:0};this.graphic.animate(a);this.shadowGroup&&this.shadowGroup.animate(a)}}),requireSorting:!1,noSharedTooltip:!0,trackerGroups:["group","dataLabelsGroup"],pointAttrToOptions:{stroke:"borderColor","stroke-width":"borderWidth",fill:"color"},getColor:oa,animate:function(a){var b=this,c=b.points,d=b.startAngleRad;if(!a)n(c,function(a){var c=a.graphic,a=a.shapeArgs;c&&(c.attr({r:b.center[3]/
2,start:d,end:d}),c.animate({r:a.r,start:a.start,end:a.end},b.options.animation))}),b.animate=null},setData:function(a,b){Q.prototype.setData.call(this,a,!1);this.processData();this.generatePoints();o(b,!0)&&this.chart.redraw()},generatePoints:function(){var a,b=0,c,d,e,f=this.options.ignoreHiddenPoint;Q.prototype.generatePoints.call(this);c=this.points;d=c.length;for(a=0;a<d;a++)e=c[a],b+=f&&!e.visible?0:e.y;this.total=b;for(a=0;a<d;a++)e=c[a],e.percentage=b>0?e.y/b*100:0,e.total=b},getCenter:function(){var a=
this.options,b=this.chart,c=2*(a.slicedOffset||0),d,e=b.plotWidth-2*c,f=b.plotHeight-2*c,b=a.center,a=[o(b[0],"50%"),o(b[1],"50%"),a.size||"100%",a.innerSize||0],g=J(e,f),h;return Na(a,function(a,b){h=/%$/.test(a);d=b<2||b===2&&h;return(h?[e,f,g,g][b]*y(a)/100:a)+(d?c:0)})},translate:function(a){this.generatePoints();var b=0,c=this.options,d=c.slicedOffset,e=d+c.borderWidth,f,g,h,i=c.startAngle||0,j=this.startAngleRad=xa/180*(i-90),i=(this.endAngleRad=xa/180*((c.endAngle||i+360)-90))-j,k=this.points,
l=c.dataLabels.distance,c=c.ignoreHiddenPoint,m,n=k.length,o;if(!a)this.center=a=this.getCenter();this.getX=function(b,c){h=R.asin((b-a[1])/(a[2]/2+l));return a[0]+(c?-1:1)*V(h)*(a[2]/2+l)};for(m=0;m<n;m++){o=k[m];f=j+b*i;if(!c||o.visible)b+=o.percentage/100;g=j+b*i;o.shapeType="arc";o.shapeArgs={x:a[0],y:a[1],r:a[2]/2,innerR:a[3]/2,start:t(f*1E3)/1E3,end:t(g*1E3)/1E3};h=(g+f)/2;h>0.75*i&&(h-=2*xa);o.slicedTranslation={translateX:t(V(h)*d),translateY:t(ba(h)*d)};f=V(h)*a[2]/2;g=ba(h)*a[2]/2;o.tooltipPos=
[a[0]+f*0.7,a[1]+g*0.7];o.half=h<-xa/2||h>xa/2?1:0;o.angle=h;e=J(e,l/2);o.labelPos=[a[0]+f+V(h)*l,a[1]+g+ba(h)*l,a[0]+f+V(h)*e,a[1]+g+ba(h)*e,a[0]+f,a[1]+g,l<0?"center":o.half?"right":"left",h]}},setTooltipPoints:oa,drawGraph:null,drawPoints:function(){var a=this,b=a.chart.renderer,c,d,e=a.options.shadow,f,g;if(e&&!a.shadowGroup)a.shadowGroup=b.g("shadow").add(a.group);n(a.points,function(h){d=h.graphic;g=h.shapeArgs;f=h.shadowGroup;if(e&&!f)f=h.shadowGroup=b.g("shadow").add(a.shadowGroup);c=h.sliced?
h.slicedTranslation:{translateX:0,translateY:0};f&&f.attr(c);d?d.animate(s(g,c)):h.graphic=d=b.arc(g).setRadialReference(a.center).attr(h.pointAttr[h.selected?"select":""]).attr({"stroke-linejoin":"round"}).attr(c).add(a.group).shadow(e,f);h.visible===!1&&h.setVisible(!1)})},sortByAngle:function(a,b){a.sort(function(a,d){return a.angle!==void 0&&(d.angle-a.angle)*b})},drawDataLabels:function(){var a=this,b=a.data,c,d=a.chart,e=a.options.dataLabels,f=o(e.connectorPadding,10),g=o(e.connectorWidth,1),
h=d.plotWidth,d=d.plotHeight,i,j,k=o(e.softConnector,!0),l=e.distance,m=a.center,p=m[2]/2,q=m[1],u=l>0,s,v,w,x,y=[[],[]],z,A,E,H,C,D=[0,0,0,0],J=function(a,b){return b.y-a.y};if(a.visible&&(e.enabled||a._hasPointLabels)){Q.prototype.drawDataLabels.apply(a);n(b,function(a){a.dataLabel&&y[a.half].push(a)});for(H=0;!x&&b[H];)x=b[H]&&b[H].dataLabel&&(b[H].dataLabel.getBBox().height||21),H++;for(H=2;H--;){var b=[],I=[],G=y[H],K=G.length,F;a.sortByAngle(G,H-0.5);if(l>0){for(C=q-p-l;C<=q+p+l;C+=x)b.push(C);
v=b.length;if(K>v){c=[].concat(G);c.sort(J);for(C=K;C--;)c[C].rank=C;for(C=K;C--;)G[C].rank>=v&&G.splice(C,1);K=G.length}for(C=0;C<K;C++){c=G[C];w=c.labelPos;c=9999;var N,L;for(L=0;L<v;L++)N=M(b[L]-w[1]),N<c&&(c=N,F=L);if(F<C&&b[C]!==null)F=C;else for(v<K-C+F&&b[C]!==null&&(F=v-K+C);b[F]===null;)F++;I.push({i:F,y:b[F]});b[F]=null}I.sort(J)}for(C=0;C<K;C++){c=G[C];w=c.labelPos;s=c.dataLabel;E=c.visible===!1?"hidden":"visible";c=w[1];if(l>0){if(v=I.pop(),F=v.i,A=v.y,c>A&&b[F+1]!==null||c<A&&b[F-1]!==
null)A=c}else A=c;z=e.justify?m[0]+(H?-1:1)*(p+l):a.getX(F===0||F===b.length-1?c:A,H);s._attr={visibility:E,align:w[6]};s._pos={x:z+e.x+({left:f,right:-f}[w[6]]||0),y:A+e.y-10};s.connX=z;s.connY=A;if(this.options.size===null)v=s.width,z-v<f?D[3]=r(t(v-z+f),D[3]):z+v>h-f&&(D[1]=r(t(z+v-h+f),D[1])),A-x/2<0?D[0]=r(t(-A+x/2),D[0]):A+x/2>d&&(D[2]=r(t(A+x/2-d),D[2]))}}if(ua(D)===0||this.verifyDataLabelOverflow(D))this.placeDataLabels(),u&&g&&n(this.points,function(b){i=b.connector;w=b.labelPos;if((s=b.dataLabel)&&
s._pos)E=s._attr.visibility,z=s.connX,A=s.connY,j=k?["M",z+(w[6]==="left"?5:-5),A,"C",z,A,2*w[2]-w[4],2*w[3]-w[5],w[2],w[3],"L",w[4],w[5]]:["M",z+(w[6]==="left"?5:-5),A,"L",w[2],w[3],"L",w[4],w[5]],i?(i.animate({d:j}),i.attr("visibility",E)):b.connector=i=a.chart.renderer.path(j).attr({"stroke-width":g,stroke:e.connectorColor||b.color||"#606060",visibility:E}).add(a.group);else if(i)b.connector=i.destroy()})}},verifyDataLabelOverflow:function(a){var b=this.center,c=this.options,d=c.center,e=c=c.minSize||
80,f;d[0]!==null?e=r(b[2]-r(a[1],a[3]),c):(e=r(b[2]-a[1]-a[3],c),b[0]+=(a[3]-a[1])/2);d[1]!==null?e=r(J(e,b[2]-r(a[0],a[2])),c):(e=r(J(e,b[2]-a[0]-a[2]),c),b[1]+=(a[0]-a[2])/2);e<b[2]?(b[2]=e,this.translate(b),n(this.points,function(a){if(a.dataLabel)a.dataLabel._pos=null}),this.drawDataLabels()):f=!0;return f},placeDataLabels:function(){n(this.points,function(a){var a=a.dataLabel,b;if(a)(b=a._pos)?(a.attr(a._attr),a[a.moved?"animate":"attr"](b),a.moved=!0):a&&a.attr({y:-999})})},alignDataLabel:oa,
drawTracker:F.prototype.drawTracker,drawLegendSymbol:G.prototype.drawLegendSymbol,getSymbol:oa};Y=ha(Q,Y);X.pie=Y;s(Highcharts,{Axis:eb,Chart:yb,Color:qa,Legend:fb,Pointer:xb,Point:Pa,Tick:Ma,Tooltip:wb,Renderer:Va,Series:Q,SVGElement:va,SVGRenderer:za,arrayMin:Ja,arrayMax:ua,charts:Ga,dateFormat:Ya,format:Ca,pathAnim:Ab,getOptions:function(){return L},hasBidiBug:Ub,isTouchDevice:Ob,numberFormat:Aa,seriesTypes:X,setOptions:function(a){L=x(L,a);Lb();return L},addEvent:K,removeEvent:$,createElement:U,
discardElement:Ta,css:I,each:n,extend:s,map:Na,merge:x,pick:o,splat:ja,extendClass:ha,pInt:y,wrap:mb,svg:W,canvas:ca,vml:!W&&!ca,product:"Highcharts",version:"3.0.7"})})();

//! moment.js
//! version : 2.4.0
//! authors : Tim Wood, Iskren Chernev, Moment.js contributors
//! license : MIT
//! momentjs.com

(function (undefined) {

    /************************************
     Constants
     ************************************/

    var moment,
        VERSION = "2.4.0",
        round = Math.round,
        i,

        YEAR = 0,
        MONTH = 1,
        DATE = 2,
        HOUR = 3,
        MINUTE = 4,
        SECOND = 5,
        MILLISECOND = 6,

    // internal storage for language config files
        languages = {},

    // check for nodeJS
        hasModule = (typeof module !== 'undefined' && module.exports),

    // ASP.NET json date format regex
        aspNetJsonRegex = /^\/?Date\((\-?\d+)/i,
        aspNetTimeSpanJsonRegex = /(\-)?(?:(\d*)\.)?(\d+)\:(\d+)(?:\:(\d+)\.?(\d{3})?)?/,

    // from http://docs.closure-library.googlecode.com/git/closure_goog_date_date.js.source.html
    // somewhat more in line with 4.4.3.2 2004 spec, but allows decimal anywhere
        isoDurationRegex = /^(-)?P(?:(?:([0-9,.]*)Y)?(?:([0-9,.]*)M)?(?:([0-9,.]*)D)?(?:T(?:([0-9,.]*)H)?(?:([0-9,.]*)M)?(?:([0-9,.]*)S)?)?|([0-9,.]*)W)$/,

    // format tokens
        formattingTokens = /(\[[^\[]*\])|(\\)?(Mo|MM?M?M?|Do|DDDo|DD?D?D?|ddd?d?|do?|w[o|w]?|W[o|W]?|YYYYY|YYYY|YY|gg(ggg?)?|GG(GGG?)?|e|E|a|A|hh?|HH?|mm?|ss?|S{1,4}|X|zz?|ZZ?|.)/g,
        localFormattingTokens = /(\[[^\[]*\])|(\\)?(LT|LL?L?L?|l{1,4})/g,

    // parsing token regexes
        parseTokenOneOrTwoDigits = /\d\d?/, // 0 - 99
        parseTokenOneToThreeDigits = /\d{1,3}/, // 0 - 999
        parseTokenThreeDigits = /\d{3}/, // 000 - 999
        parseTokenFourDigits = /\d{1,4}/, // 0 - 9999
        parseTokenSixDigits = /[+\-]?\d{1,6}/, // -999,999 - 999,999
        parseTokenDigits = /\d+/, // nonzero number of digits
        parseTokenWord = /[0-9]*['a-z\u00A0-\u05FF\u0700-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+|[\u0600-\u06FF\/]+(\s*?[\u0600-\u06FF]+){1,2}/i, // any word (or two) characters or numbers including two/three word month in arabic.
        parseTokenTimezone = /Z|[\+\-]\d\d:?\d\d/i, // +00:00 -00:00 +0000 -0000 or Z
        parseTokenT = /T/i, // T (ISO seperator)
        parseTokenTimestampMs = /[\+\-]?\d+(\.\d{1,3})?/, // 123456789 123456789.123

    // preliminary iso regex
    // 0000-00-00 0000-W00 or 0000-W00-0 + T + 00 or 00:00 or 00:00:00 or 00:00:00.000 + +00:00 or +0000)
        isoRegex = /^\s*\d{4}-(?:(\d\d-\d\d)|(W\d\d$)|(W\d\d-\d)|(\d\d\d))((T| )(\d\d(:\d\d(:\d\d(\.\d+)?)?)?)?([\+\-]\d\d:?\d\d|Z)?)?$/,

        isoFormat = 'YYYY-MM-DDTHH:mm:ssZ',

        isoDates = [
            'YYYY-MM-DD',
            'GGGG-[W]WW',
            'GGGG-[W]WW-E',
            'YYYY-DDD'
        ],

    // iso time formats and regexes
        isoTimes = [
            ['HH:mm:ss.SSSS', /(T| )\d\d:\d\d:\d\d\.\d{1,3}/],
            ['HH:mm:ss', /(T| )\d\d:\d\d:\d\d/],
            ['HH:mm', /(T| )\d\d:\d\d/],
            ['HH', /(T| )\d\d/]
        ],

    // timezone chunker "+10:00" > ["10", "00"] or "-1530" > ["-15", "30"]
        parseTimezoneChunker = /([\+\-]|\d\d)/gi,

    // getter and setter names
        proxyGettersAndSetters = 'Date|Hours|Minutes|Seconds|Milliseconds'.split('|'),
        unitMillisecondFactors = {
            'Milliseconds' : 1,
            'Seconds' : 1e3,
            'Minutes' : 6e4,
            'Hours' : 36e5,
            'Days' : 864e5,
            'Months' : 2592e6,
            'Years' : 31536e6
        },

        unitAliases = {
            ms : 'millisecond',
            s : 'second',
            m : 'minute',
            h : 'hour',
            d : 'day',
            D : 'date',
            w : 'week',
            W : 'isoWeek',
            M : 'month',
            y : 'year',
            DDD : 'dayOfYear',
            e : 'weekday',
            E : 'isoWeekday',
            gg: 'weekYear',
            GG: 'isoWeekYear'
        },

        camelFunctions = {
            dayofyear : 'dayOfYear',
            isoweekday : 'isoWeekday',
            isoweek : 'isoWeek',
            weekyear : 'weekYear',
            isoweekyear : 'isoWeekYear'
        },

    // format function strings
        formatFunctions = {},

    // tokens to ordinalize and pad
        ordinalizeTokens = 'DDD w W M D d'.split(' '),
        paddedTokens = 'M D H h m s w W'.split(' '),

        formatTokenFunctions = {
            M    : function () {
                return this.month() + 1;
            },
            MMM  : function (format) {
                return this.lang().monthsShort(this, format);
            },
            MMMM : function (format) {
                return this.lang().months(this, format);
            },
            D    : function () {
                return this.date();
            },
            DDD  : function () {
                return this.dayOfYear();
            },
            d    : function () {
                return this.day();
            },
            dd   : function (format) {
                return this.lang().weekdaysMin(this, format);
            },
            ddd  : function (format) {
                return this.lang().weekdaysShort(this, format);
            },
            dddd : function (format) {
                return this.lang().weekdays(this, format);
            },
            w    : function () {
                return this.week();
            },
            W    : function () {
                return this.isoWeek();
            },
            YY   : function () {
                return leftZeroFill(this.year() % 100, 2);
            },
            YYYY : function () {
                return leftZeroFill(this.year(), 4);
            },
            YYYYY : function () {
                return leftZeroFill(this.year(), 5);
            },
            gg   : function () {
                return leftZeroFill(this.weekYear() % 100, 2);
            },
            gggg : function () {
                return this.weekYear();
            },
            ggggg : function () {
                return leftZeroFill(this.weekYear(), 5);
            },
            GG   : function () {
                return leftZeroFill(this.isoWeekYear() % 100, 2);
            },
            GGGG : function () {
                return this.isoWeekYear();
            },
            GGGGG : function () {
                return leftZeroFill(this.isoWeekYear(), 5);
            },
            e : function () {
                return this.weekday();
            },
            E : function () {
                return this.isoWeekday();
            },
            a    : function () {
                return this.lang().meridiem(this.hours(), this.minutes(), true);
            },
            A    : function () {
                return this.lang().meridiem(this.hours(), this.minutes(), false);
            },
            H    : function () {
                return this.hours();
            },
            h    : function () {
                return this.hours() % 12 || 12;
            },
            m    : function () {
                return this.minutes();
            },
            s    : function () {
                return this.seconds();
            },
            S    : function () {
                return toInt(this.milliseconds() / 100);
            },
            SS   : function () {
                return leftZeroFill(toInt(this.milliseconds() / 10), 2);
            },
            SSS  : function () {
                return leftZeroFill(this.milliseconds(), 3);
            },
            SSSS : function () {
                return leftZeroFill(this.milliseconds(), 3);
            },
            Z    : function () {
                var a = -this.zone(),
                    b = "+";
                if (a < 0) {
                    a = -a;
                    b = "-";
                }
                return b + leftZeroFill(toInt(a / 60), 2) + ":" + leftZeroFill(toInt(a) % 60, 2);
            },
            ZZ   : function () {
                var a = -this.zone(),
                    b = "+";
                if (a < 0) {
                    a = -a;
                    b = "-";
                }
                return b + leftZeroFill(toInt(10 * a / 6), 4);
            },
            z : function () {
                return this.zoneAbbr();
            },
            zz : function () {
                return this.zoneName();
            },
            X    : function () {
                return this.unix();
            }
        },

        lists = ['months', 'monthsShort', 'weekdays', 'weekdaysShort', 'weekdaysMin'];

    function padToken(func, count) {
        return function (a) {
            return leftZeroFill(func.call(this, a), count);
        };
    }
    function ordinalizeToken(func, period) {
        return function (a) {
            return this.lang().ordinal(func.call(this, a), period);
        };
    }

    while (ordinalizeTokens.length) {
        i = ordinalizeTokens.pop();
        formatTokenFunctions[i + 'o'] = ordinalizeToken(formatTokenFunctions[i], i);
    }
    while (paddedTokens.length) {
        i = paddedTokens.pop();
        formatTokenFunctions[i + i] = padToken(formatTokenFunctions[i], 2);
    }
    formatTokenFunctions.DDDD = padToken(formatTokenFunctions.DDD, 3);


    /************************************
     Constructors
     ************************************/

    function Language() {

    }

    // Moment prototype object
    function Moment(config) {
        checkOverflow(config);
        extend(this, config);
    }

    // Duration Constructor
    function Duration(duration) {
        var normalizedInput = normalizeObjectUnits(duration),
            years = normalizedInput.year || 0,
            months = normalizedInput.month || 0,
            weeks = normalizedInput.week || 0,
            days = normalizedInput.day || 0,
            hours = normalizedInput.hour || 0,
            minutes = normalizedInput.minute || 0,
            seconds = normalizedInput.second || 0,
            milliseconds = normalizedInput.millisecond || 0;

        // store reference to input for deterministic cloning
        this._input = duration;

        // representation for dateAddRemove
        this._milliseconds = +milliseconds +
            seconds * 1e3 + // 1000
            minutes * 6e4 + // 1000 * 60
            hours * 36e5; // 1000 * 60 * 60
        // Because of dateAddRemove treats 24 hours as different from a
        // day when working around DST, we need to store them separately
        this._days = +days +
            weeks * 7;
        // It is impossible translate months into days without knowing
        // which months you are are talking about, so we have to store
        // it separately.
        this._months = +months +
            years * 12;

        this._data = {};

        this._bubble();
    }

    /************************************
     Helpers
     ************************************/


    function extend(a, b) {
        for (var i in b) {
            if (b.hasOwnProperty(i)) {
                a[i] = b[i];
            }
        }

        if (b.hasOwnProperty("toString")) {
            a.toString = b.toString;
        }

        if (b.hasOwnProperty("valueOf")) {
            a.valueOf = b.valueOf;
        }

        return a;
    }

    function absRound(number) {
        if (number < 0) {
            return Math.ceil(number);
        } else {
            return Math.floor(number);
        }
    }

    // left zero fill a number
    // see http://jsperf.com/left-zero-filling for performance comparison
    function leftZeroFill(number, targetLength) {
        var output = number + '';
        while (output.length < targetLength) {
            output = '0' + output;
        }
        return output;
    }

    // helper function for _.addTime and _.subtractTime
    function addOrSubtractDurationFromMoment(mom, duration, isAdding, ignoreUpdateOffset) {
        var milliseconds = duration._milliseconds,
            days = duration._days,
            months = duration._months,
            minutes,
            hours;

        if (milliseconds) {
            mom._d.setTime(+mom._d + milliseconds * isAdding);
        }
        // store the minutes and hours so we can restore them
        if (days || months) {
            minutes = mom.minute();
            hours = mom.hour();
        }
        if (days) {
            mom.date(mom.date() + days * isAdding);
        }
        if (months) {
            mom.month(mom.month() + months * isAdding);
        }
        if (milliseconds && !ignoreUpdateOffset) {
            moment.updateOffset(mom);
        }
        // restore the minutes and hours after possibly changing dst
        if (days || months) {
            mom.minute(minutes);
            mom.hour(hours);
        }
    }

    // check if is an array
    function isArray(input) {
        return Object.prototype.toString.call(input) === '[object Array]';
    }

    function isDate(input) {
        return  Object.prototype.toString.call(input) === '[object Date]' ||
            input instanceof Date;
    }

    // compare two arrays, return the number of differences
    function compareArrays(array1, array2, dontConvert) {
        var len = Math.min(array1.length, array2.length),
            lengthDiff = Math.abs(array1.length - array2.length),
            diffs = 0,
            i;
        for (i = 0; i < len; i++) {
            if ((dontConvert && array1[i] !== array2[i]) ||
                (!dontConvert && toInt(array1[i]) !== toInt(array2[i]))) {
                diffs++;
            }
        }
        return diffs + lengthDiff;
    }

    function normalizeUnits(units) {
        if (units) {
            var lowered = units.toLowerCase().replace(/(.)s$/, '$1');
            units = unitAliases[units] || camelFunctions[lowered] || lowered;
        }
        return units;
    }

    function normalizeObjectUnits(inputObject) {
        var normalizedInput = {},
            normalizedProp,
            prop,
            index;

        for (prop in inputObject) {
            if (inputObject.hasOwnProperty(prop)) {
                normalizedProp = normalizeUnits(prop);
                if (normalizedProp) {
                    normalizedInput[normalizedProp] = inputObject[prop];
                }
            }
        }

        return normalizedInput;
    }

    function makeList(field) {
        var count, setter;

        if (field.indexOf('week') === 0) {
            count = 7;
            setter = 'day';
        }
        else if (field.indexOf('month') === 0) {
            count = 12;
            setter = 'month';
        }
        else {
            return;
        }

        moment[field] = function (format, index) {
            var i, getter,
                method = moment.fn._lang[field],
                results = [];

            if (typeof format === 'number') {
                index = format;
                format = undefined;
            }

            getter = function (i) {
                var m = moment().utc().set(setter, i);
                return method.call(moment.fn._lang, m, format || '');
            };

            if (index != null) {
                return getter(index);
            }
            else {
                for (i = 0; i < count; i++) {
                    results.push(getter(i));
                }
                return results;
            }
        };
    }

    function toInt(argumentForCoercion) {
        var coercedNumber = +argumentForCoercion,
            value = 0;

        if (coercedNumber !== 0 && isFinite(coercedNumber)) {
            if (coercedNumber >= 0) {
                value = Math.floor(coercedNumber);
            } else {
                value = Math.ceil(coercedNumber);
            }
        }

        return value;
    }

    function daysInMonth(year, month) {
        return new Date(Date.UTC(year, month + 1, 0)).getUTCDate();
    }

    function daysInYear(year) {
        return isLeapYear(year) ? 366 : 365;
    }

    function isLeapYear(year) {
        return (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
    }

    function checkOverflow(m) {
        var overflow;
        if (m._a && m._pf.overflow === -2) {
            overflow =
                m._a[MONTH] < 0 || m._a[MONTH] > 11 ? MONTH :
                    m._a[DATE] < 1 || m._a[DATE] > daysInMonth(m._a[YEAR], m._a[MONTH]) ? DATE :
                        m._a[HOUR] < 0 || m._a[HOUR] > 23 ? HOUR :
                            m._a[MINUTE] < 0 || m._a[MINUTE] > 59 ? MINUTE :
                                m._a[SECOND] < 0 || m._a[SECOND] > 59 ? SECOND :
                                    m._a[MILLISECOND] < 0 || m._a[MILLISECOND] > 999 ? MILLISECOND :
                                        -1;

            if (m._pf._overflowDayOfYear && (overflow < YEAR || overflow > DATE)) {
                overflow = DATE;
            }

            m._pf.overflow = overflow;
        }
    }

    function initializeParsingFlags(config) {
        config._pf = {
            empty : false,
            unusedTokens : [],
            unusedInput : [],
            overflow : -2,
            charsLeftOver : 0,
            nullInput : false,
            invalidMonth : null,
            invalidFormat : false,
            userInvalidated : false,
            iso: false
        };
    }

    function isValid(m) {
        if (m._isValid == null) {
            m._isValid = !isNaN(m._d.getTime()) &&
                m._pf.overflow < 0 &&
                !m._pf.empty &&
                !m._pf.invalidMonth &&
                !m._pf.nullInput &&
                !m._pf.invalidFormat &&
                !m._pf.userInvalidated;

            if (m._strict) {
                m._isValid = m._isValid &&
                    m._pf.charsLeftOver === 0 &&
                    m._pf.unusedTokens.length === 0;
            }
        }
        return m._isValid;
    }

    function normalizeLanguage(key) {
        return key ? key.toLowerCase().replace('_', '-') : key;
    }

    /************************************
     Languages
     ************************************/


    extend(Language.prototype, {

        set : function (config) {
            var prop, i;
            for (i in config) {
                prop = config[i];
                if (typeof prop === 'function') {
                    this[i] = prop;
                } else {
                    this['_' + i] = prop;
                }
            }
        },

        _months : "January_February_March_April_May_June_July_August_September_October_November_December".split("_"),
        months : function (m) {
            return this._months[m.month()];
        },

        _monthsShort : "Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec".split("_"),
        monthsShort : function (m) {
            return this._monthsShort[m.month()];
        },

        monthsParse : function (monthName) {
            var i, mom, regex;

            if (!this._monthsParse) {
                this._monthsParse = [];
            }

            for (i = 0; i < 12; i++) {
                // make the regex if we don't have it already
                if (!this._monthsParse[i]) {
                    mom = moment.utc([2000, i]);
                    regex = '^' + this.months(mom, '') + '|^' + this.monthsShort(mom, '');
                    this._monthsParse[i] = new RegExp(regex.replace('.', ''), 'i');
                }
                // test the regex
                if (this._monthsParse[i].test(monthName)) {
                    return i;
                }
            }
        },

        _weekdays : "Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"),
        weekdays : function (m) {
            return this._weekdays[m.day()];
        },

        _weekdaysShort : "Sun_Mon_Tue_Wed_Thu_Fri_Sat".split("_"),
        weekdaysShort : function (m) {
            return this._weekdaysShort[m.day()];
        },

        _weekdaysMin : "Su_Mo_Tu_We_Th_Fr_Sa".split("_"),
        weekdaysMin : function (m) {
            return this._weekdaysMin[m.day()];
        },

        weekdaysParse : function (weekdayName) {
            var i, mom, regex;

            if (!this._weekdaysParse) {
                this._weekdaysParse = [];
            }

            for (i = 0; i < 7; i++) {
                // make the regex if we don't have it already
                if (!this._weekdaysParse[i]) {
                    mom = moment([2000, 1]).day(i);
                    regex = '^' + this.weekdays(mom, '') + '|^' + this.weekdaysShort(mom, '') + '|^' + this.weekdaysMin(mom, '');
                    this._weekdaysParse[i] = new RegExp(regex.replace('.', ''), 'i');
                }
                // test the regex
                if (this._weekdaysParse[i].test(weekdayName)) {
                    return i;
                }
            }
        },

        _longDateFormat : {
            LT : "h:mm A",
            L : "MM/DD/YYYY",
            LL : "MMMM D YYYY",
            LLL : "MMMM D YYYY LT",
            LLLL : "dddd, MMMM D YYYY LT"
        },
        longDateFormat : function (key) {
            var output = this._longDateFormat[key];
            if (!output && this._longDateFormat[key.toUpperCase()]) {
                output = this._longDateFormat[key.toUpperCase()].replace(/MMMM|MM|DD|dddd/g, function (val) {
                    return val.slice(1);
                });
                this._longDateFormat[key] = output;
            }
            return output;
        },

        isPM : function (input) {
            // IE8 Quirks Mode & IE7 Standards Mode do not allow accessing strings like arrays
            // Using charAt should be more compatible.
            return ((input + '').toLowerCase().charAt(0) === 'p');
        },

        _meridiemParse : /[ap]\.?m?\.?/i,
        meridiem : function (hours, minutes, isLower) {
            if (hours > 11) {
                return isLower ? 'pm' : 'PM';
            } else {
                return isLower ? 'am' : 'AM';
            }
        },

        _calendar : {
            sameDay : '[Today at] LT',
            nextDay : '[Tomorrow at] LT',
            nextWeek : 'dddd [at] LT',
            lastDay : '[Yesterday at] LT',
            lastWeek : '[Last] dddd [at] LT',
            sameElse : 'L'
        },
        calendar : function (key, mom) {
            var output = this._calendar[key];
            return typeof output === 'function' ? output.apply(mom) : output;
        },

        _relativeTime : {
            future : "in %s",
            past : "%s ago",
            s : "a few seconds",
            m : "a minute",
            mm : "%d minutes",
            h : "an hour",
            hh : "%d hours",
            d : "a day",
            dd : "%d days",
            M : "a month",
            MM : "%d months",
            y : "a year",
            yy : "%d years"
        },
        relativeTime : function (number, withoutSuffix, string, isFuture) {
            var output = this._relativeTime[string];
            return (typeof output === 'function') ?
                output(number, withoutSuffix, string, isFuture) :
                output.replace(/%d/i, number);
        },
        pastFuture : function (diff, output) {
            var format = this._relativeTime[diff > 0 ? 'future' : 'past'];
            return typeof format === 'function' ? format(output) : format.replace(/%s/i, output);
        },

        ordinal : function (number) {
            return this._ordinal.replace("%d", number);
        },
        _ordinal : "%d",

        preparse : function (string) {
            return string;
        },

        postformat : function (string) {
            return string;
        },

        week : function (mom) {
            return weekOfYear(mom, this._week.dow, this._week.doy).week;
        },

        _week : {
            dow : 0, // Sunday is the first day of the week.
            doy : 6  // The week that contains Jan 1st is the first week of the year.
        },

        _invalidDate: 'Invalid date',
        invalidDate: function () {
            return this._invalidDate;
        }
    });

    // Loads a language definition into the `languages` cache.  The function
    // takes a key and optionally values.  If not in the browser and no values
    // are provided, it will load the language file module.  As a convenience,
    // this function also returns the language values.
    function loadLang(key, values) {
        values.abbr = key;
        if (!languages[key]) {
            languages[key] = new Language();
        }
        languages[key].set(values);
        return languages[key];
    }

    // Remove a language from the `languages` cache. Mostly useful in tests.
    function unloadLang(key) {
        delete languages[key];
    }

    // Determines which language definition to use and returns it.
    //
    // With no parameters, it will return the global language.  If you
    // pass in a language key, such as 'en', it will return the
    // definition for 'en', so long as 'en' has already been loaded using
    // moment.lang.
    function getLangDefinition(key) {
        var i = 0, j, lang, next, split,
            get = function (k) {
                if (!languages[k] && hasModule) {
                    try {
                        require('./lang/' + k);
                    } catch (e) { }
                }
                return languages[k];
            };

        if (!key) {
            return moment.fn._lang;
        }

        if (!isArray(key)) {
            //short-circuit everything else
            lang = get(key);
            if (lang) {
                return lang;
            }
            key = [key];
        }

        //pick the language from the array
        //try ['en-au', 'en-gb'] as 'en-au', 'en-gb', 'en', as in move through the list trying each
        //substring from most specific to least, but move to the next array item if it's a more specific variant than the current root
        while (i < key.length) {
            split = normalizeLanguage(key[i]).split('-');
            j = split.length;
            next = normalizeLanguage(key[i + 1]);
            next = next ? next.split('-') : null;
            while (j > 0) {
                lang = get(split.slice(0, j).join('-'));
                if (lang) {
                    return lang;
                }
                if (next && next.length >= j && compareArrays(split, next, true) >= j - 1) {
                    //the next array item is better than a shallower substring of this one
                    break;
                }
                j--;
            }
            i++;
        }
        return moment.fn._lang;
    }

    /************************************
     Formatting
     ************************************/


    function removeFormattingTokens(input) {
        if (input.match(/\[[\s\S]/)) {
            return input.replace(/^\[|\]$/g, "");
        }
        return input.replace(/\\/g, "");
    }

    function makeFormatFunction(format) {
        var array = format.match(formattingTokens), i, length;

        for (i = 0, length = array.length; i < length; i++) {
            if (formatTokenFunctions[array[i]]) {
                array[i] = formatTokenFunctions[array[i]];
            } else {
                array[i] = removeFormattingTokens(array[i]);
            }
        }

        return function (mom) {
            var output = "";
            for (i = 0; i < length; i++) {
                output += array[i] instanceof Function ? array[i].call(mom, format) : array[i];
            }
            return output;
        };
    }

    // format date using native date object
    function formatMoment(m, format) {

        if (!m.isValid()) {
            return m.lang().invalidDate();
        }

        format = expandFormat(format, m.lang());

        if (!formatFunctions[format]) {
            formatFunctions[format] = makeFormatFunction(format);
        }

        return formatFunctions[format](m);
    }

    function expandFormat(format, lang) {
        var i = 5;

        function replaceLongDateFormatTokens(input) {
            return lang.longDateFormat(input) || input;
        }

        localFormattingTokens.lastIndex = 0;
        while (i >= 0 && localFormattingTokens.test(format)) {
            format = format.replace(localFormattingTokens, replaceLongDateFormatTokens);
            localFormattingTokens.lastIndex = 0;
            i -= 1;
        }

        return format;
    }


    /************************************
     Parsing
     ************************************/


        // get the regex to find the next token
    function getParseRegexForToken(token, config) {
        var a;
        switch (token) {
            case 'DDDD':
                return parseTokenThreeDigits;
            case 'YYYY':
            case 'GGGG':
            case 'gggg':
                return parseTokenFourDigits;
            case 'YYYYY':
            case 'GGGGG':
            case 'ggggg':
                return parseTokenSixDigits;
            case 'S':
            case 'SS':
            case 'SSS':
            case 'DDD':
                return parseTokenOneToThreeDigits;
            case 'MMM':
            case 'MMMM':
            case 'dd':
            case 'ddd':
            case 'dddd':
                return parseTokenWord;
            case 'a':
            case 'A':
                return getLangDefinition(config._l)._meridiemParse;
            case 'X':
                return parseTokenTimestampMs;
            case 'Z':
            case 'ZZ':
                return parseTokenTimezone;
            case 'T':
                return parseTokenT;
            case 'SSSS':
                return parseTokenDigits;
            case 'MM':
            case 'DD':
            case 'YY':
            case 'GG':
            case 'gg':
            case 'HH':
            case 'hh':
            case 'mm':
            case 'ss':
            case 'M':
            case 'D':
            case 'd':
            case 'H':
            case 'h':
            case 'm':
            case 's':
            case 'w':
            case 'ww':
            case 'W':
            case 'WW':
            case 'e':
            case 'E':
                return parseTokenOneOrTwoDigits;
            default :
                a = new RegExp(regexpEscape(unescapeFormat(token.replace('\\', '')), "i"));
                return a;
        }
    }

    function timezoneMinutesFromString(string) {
        var tzchunk = (parseTokenTimezone.exec(string) || [])[0],
            parts = (tzchunk + '').match(parseTimezoneChunker) || ['-', 0, 0],
            minutes = +(parts[1] * 60) + toInt(parts[2]);

        return parts[0] === '+' ? -minutes : minutes;
    }

    // function to convert string input to date
    function addTimeToArrayFromToken(token, input, config) {
        var a, datePartArray = config._a;

        switch (token) {
            // MONTH
            case 'M' : // fall through to MM
            case 'MM' :
                if (input != null) {
                    datePartArray[MONTH] = toInt(input) - 1;
                }
                break;
            case 'MMM' : // fall through to MMMM
            case 'MMMM' :
                a = getLangDefinition(config._l).monthsParse(input);
                // if we didn't find a month name, mark the date as invalid.
                if (a != null) {
                    datePartArray[MONTH] = a;
                } else {
                    config._pf.invalidMonth = input;
                }
                break;
            // DAY OF MONTH
            case 'D' : // fall through to DD
            case 'DD' :
                if (input != null) {
                    datePartArray[DATE] = toInt(input);
                }
                break;
            // DAY OF YEAR
            case 'DDD' : // fall through to DDDD
            case 'DDDD' :
                if (input != null) {
                    config._dayOfYear = toInt(input);
                }

                break;
            // YEAR
            case 'YY' :
                datePartArray[YEAR] = toInt(input) + (toInt(input) > 68 ? 1900 : 2000);
                break;
            case 'YYYY' :
            case 'YYYYY' :
                datePartArray[YEAR] = toInt(input);
                break;
            // AM / PM
            case 'a' : // fall through to A
            case 'A' :
                config._isPm = getLangDefinition(config._l).isPM(input);
                break;
            // 24 HOUR
            case 'H' : // fall through to hh
            case 'HH' : // fall through to hh
            case 'h' : // fall through to hh
            case 'hh' :
                datePartArray[HOUR] = toInt(input);
                break;
            // MINUTE
            case 'm' : // fall through to mm
            case 'mm' :
                datePartArray[MINUTE] = toInt(input);
                break;
            // SECOND
            case 's' : // fall through to ss
            case 'ss' :
                datePartArray[SECOND] = toInt(input);
                break;
            // MILLISECOND
            case 'S' :
            case 'SS' :
            case 'SSS' :
            case 'SSSS' :
                datePartArray[MILLISECOND] = toInt(('0.' + input) * 1000);
                break;
            // UNIX TIMESTAMP WITH MS
            case 'X':
                config._d = new Date(parseFloat(input) * 1000);
                break;
            // TIMEZONE
            case 'Z' : // fall through to ZZ
            case 'ZZ' :
                config._useUTC = true;
                config._tzm = timezoneMinutesFromString(input);
                break;
            case 'w':
            case 'ww':
            case 'W':
            case 'WW':
            case 'd':
            case 'dd':
            case 'ddd':
            case 'dddd':
            case 'e':
            case 'E':
                token = token.substr(0, 1);
            /* falls through */
            case 'gg':
            case 'gggg':
            case 'GG':
            case 'GGGG':
            case 'GGGGG':
                token = token.substr(0, 2);
                if (input) {
                    config._w = config._w || {};
                    config._w[token] = input;
                }
                break;
        }
    }

    // convert an array to a date.
    // the array should mirror the parameters below
    // note: all values past the year are optional and will default to the lowest possible value.
    // [year, month, day , hour, minute, second, millisecond]
    function dateFromConfig(config) {
        var i, date, input = [], currentDate,
            yearToUse, fixYear, w, temp, lang, weekday, week;

        if (config._d) {
            return;
        }

        currentDate = currentDateArray(config);

        //compute day of the year from weeks and weekdays
        if (config._w && config._a[DATE] == null && config._a[MONTH] == null) {
            fixYear = function (val) {
                return val ?
                    (val.length < 3 ? (parseInt(val, 10) > 68 ? '19' + val : '20' + val) : val) :
                    (config._a[YEAR] == null ? moment().weekYear() : config._a[YEAR]);
            };

            w = config._w;
            if (w.GG != null || w.W != null || w.E != null) {
                temp = dayOfYearFromWeeks(fixYear(w.GG), w.W || 1, w.E, 4, 1);
            }
            else {
                lang = getLangDefinition(config._l);
                weekday = w.d != null ?  parseWeekday(w.d, lang) :
                    (w.e != null ?  parseInt(w.e, 10) + lang._week.dow : 0);

                week = parseInt(w.w, 10) || 1;

                //if we're parsing 'd', then the low day numbers may be next week
                if (w.d != null && weekday < lang._week.dow) {
                    week++;
                }

                temp = dayOfYearFromWeeks(fixYear(w.gg), week, weekday, lang._week.doy, lang._week.dow);
            }

            config._a[YEAR] = temp.year;
            config._dayOfYear = temp.dayOfYear;
        }

        //if the day of the year is set, figure out what it is
        if (config._dayOfYear) {
            yearToUse = config._a[YEAR] == null ? currentDate[YEAR] : config._a[YEAR];

            if (config._dayOfYear > daysInYear(yearToUse)) {
                config._pf._overflowDayOfYear = true;
            }

            date = makeUTCDate(yearToUse, 0, config._dayOfYear);
            config._a[MONTH] = date.getUTCMonth();
            config._a[DATE] = date.getUTCDate();
        }

        // Default to current date.
        // * if no year, month, day of month are given, default to today
        // * if day of month is given, default month and year
        // * if month is given, default only year
        // * if year is given, don't default anything
        for (i = 0; i < 3 && config._a[i] == null; ++i) {
            config._a[i] = input[i] = currentDate[i];
        }

        // Zero out whatever was not defaulted, including time
        for (; i < 7; i++) {
            config._a[i] = input[i] = (config._a[i] == null) ? (i === 2 ? 1 : 0) : config._a[i];
        }

        // add the offsets to the time to be parsed so that we can have a clean array for checking isValid
        input[HOUR] += toInt((config._tzm || 0) / 60);
        input[MINUTE] += toInt((config._tzm || 0) % 60);

        config._d = (config._useUTC ? makeUTCDate : makeDate).apply(null, input);
    }

    function dateFromObject(config) {
        var normalizedInput;

        if (config._d) {
            return;
        }

        normalizedInput = normalizeObjectUnits(config._i);
        config._a = [
            normalizedInput.year,
            normalizedInput.month,
            normalizedInput.day,
            normalizedInput.hour,
            normalizedInput.minute,
            normalizedInput.second,
            normalizedInput.millisecond
        ];

        dateFromConfig(config);
    }

    function currentDateArray(config) {
        var now = new Date();
        if (config._useUTC) {
            return [
                now.getUTCFullYear(),
                now.getUTCMonth(),
                now.getUTCDate()
            ];
        } else {
            return [now.getFullYear(), now.getMonth(), now.getDate()];
        }
    }

    // date from string and format string
    function makeDateFromStringAndFormat(config) {

        config._a = [];
        config._pf.empty = true;

        // This array is used to make a Date, either with `new Date` or `Date.UTC`
        var lang = getLangDefinition(config._l),
            string = '' + config._i,
            i, parsedInput, tokens, token, skipped,
            stringLength = string.length,
            totalParsedInputLength = 0;

        tokens = expandFormat(config._f, lang).match(formattingTokens) || [];

        for (i = 0; i < tokens.length; i++) {
            token = tokens[i];
            parsedInput = (getParseRegexForToken(token, config).exec(string) || [])[0];
            if (parsedInput) {
                skipped = string.substr(0, string.indexOf(parsedInput));
                if (skipped.length > 0) {
                    config._pf.unusedInput.push(skipped);
                }
                string = string.slice(string.indexOf(parsedInput) + parsedInput.length);
                totalParsedInputLength += parsedInput.length;
            }
            // don't parse if it's not a known token
            if (formatTokenFunctions[token]) {
                if (parsedInput) {
                    config._pf.empty = false;
                }
                else {
                    config._pf.unusedTokens.push(token);
                }
                addTimeToArrayFromToken(token, parsedInput, config);
            }
            else if (config._strict && !parsedInput) {
                config._pf.unusedTokens.push(token);
            }
        }

        // add remaining unparsed input length to the string
        config._pf.charsLeftOver = stringLength - totalParsedInputLength;
        if (string.length > 0) {
            config._pf.unusedInput.push(string);
        }

        // handle am pm
        if (config._isPm && config._a[HOUR] < 12) {
            config._a[HOUR] += 12;
        }
        // if is 12 am, change hours to 0
        if (config._isPm === false && config._a[HOUR] === 12) {
            config._a[HOUR] = 0;
        }

        dateFromConfig(config);
        checkOverflow(config);
    }

    function unescapeFormat(s) {
        return s.replace(/\\(\[)|\\(\])|\[([^\]\[]*)\]|\\(.)/g, function (matched, p1, p2, p3, p4) {
            return p1 || p2 || p3 || p4;
        });
    }

    // Code from http://stackoverflow.com/questions/3561493/is-there-a-regexp-escape-function-in-javascript
    function regexpEscape(s) {
        return s.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
    }

    // date from string and array of format strings
    function makeDateFromStringAndArray(config) {
        var tempConfig,
            bestMoment,

            scoreToBeat,
            i,
            currentScore;

        if (config._f.length === 0) {
            config._pf.invalidFormat = true;
            config._d = new Date(NaN);
            return;
        }

        for (i = 0; i < config._f.length; i++) {
            currentScore = 0;
            tempConfig = extend({}, config);
            initializeParsingFlags(tempConfig);
            tempConfig._f = config._f[i];
            makeDateFromStringAndFormat(tempConfig);

            if (!isValid(tempConfig)) {
                continue;
            }

            // if there is any input that was not parsed add a penalty for that format
            currentScore += tempConfig._pf.charsLeftOver;

            //or tokens
            currentScore += tempConfig._pf.unusedTokens.length * 10;

            tempConfig._pf.score = currentScore;

            if (scoreToBeat == null || currentScore < scoreToBeat) {
                scoreToBeat = currentScore;
                bestMoment = tempConfig;
            }
        }

        extend(config, bestMoment || tempConfig);
    }

    // date from iso format
    function makeDateFromString(config) {
        var i,
            string = config._i,
            match = isoRegex.exec(string);

        if (match) {
            config._pf.iso = true;
            for (i = 4; i > 0; i--) {
                if (match[i]) {
                    // match[5] should be "T" or undefined
                    config._f = isoDates[i - 1] + (match[6] || " ");
                    break;
                }
            }
            for (i = 0; i < 4; i++) {
                if (isoTimes[i][1].exec(string)) {
                    config._f += isoTimes[i][0];
                    break;
                }
            }
            if (parseTokenTimezone.exec(string)) {
                config._f += "Z";
            }
            makeDateFromStringAndFormat(config);
        }
        else {
            config._d = new Date(string);
        }
    }

    function makeDateFromInput(config) {
        var input = config._i,
            matched = aspNetJsonRegex.exec(input);

        if (input === undefined) {
            config._d = new Date();
        } else if (matched) {
            config._d = new Date(+matched[1]);
        } else if (typeof input === 'string') {
            makeDateFromString(config);
        } else if (isArray(input)) {
            config._a = input.slice(0);
            dateFromConfig(config);
        } else if (isDate(input)) {
            config._d = new Date(+input);
        } else if (typeof(input) === 'object') {
            dateFromObject(config);
        } else {
            config._d = new Date(input);
        }
    }

    function makeDate(y, m, d, h, M, s, ms) {
        //can't just apply() to create a date:
        //http://stackoverflow.com/questions/181348/instantiating-a-javascript-object-by-calling-prototype-constructor-apply
        var date = new Date(y, m, d, h, M, s, ms);

        //the date constructor doesn't accept years < 1970
        if (y < 1970) {
            date.setFullYear(y);
        }
        return date;
    }

    function makeUTCDate(y) {
        var date = new Date(Date.UTC.apply(null, arguments));
        if (y < 1970) {
            date.setUTCFullYear(y);
        }
        return date;
    }

    function parseWeekday(input, language) {
        if (typeof input === 'string') {
            if (!isNaN(input)) {
                input = parseInt(input, 10);
            }
            else {
                input = language.weekdaysParse(input);
                if (typeof input !== 'number') {
                    return null;
                }
            }
        }
        return input;
    }

    /************************************
     Relative Time
     ************************************/


        // helper function for moment.fn.from, moment.fn.fromNow, and moment.duration.fn.humanize
    function substituteTimeAgo(string, number, withoutSuffix, isFuture, lang) {
        return lang.relativeTime(number || 1, !!withoutSuffix, string, isFuture);
    }

    function relativeTime(milliseconds, withoutSuffix, lang) {
        var seconds = round(Math.abs(milliseconds) / 1000),
            minutes = round(seconds / 60),
            hours = round(minutes / 60),
            days = round(hours / 24),
            years = round(days / 365),
            args = seconds < 45 && ['s', seconds] ||
                minutes === 1 && ['m'] ||
                minutes < 45 && ['mm', minutes] ||
                hours === 1 && ['h'] ||
                hours < 22 && ['hh', hours] ||
                days === 1 && ['d'] ||
                days <= 25 && ['dd', days] ||
                days <= 45 && ['M'] ||
                days < 345 && ['MM', round(days / 30)] ||
                years === 1 && ['y'] || ['yy', years];
        args[2] = withoutSuffix;
        args[3] = milliseconds > 0;
        args[4] = lang;
        return substituteTimeAgo.apply({}, args);
    }


    /************************************
     Week of Year
     ************************************/


        // firstDayOfWeek       0 = sun, 6 = sat
        //                      the day of the week that starts the week
        //                      (usually sunday or monday)
        // firstDayOfWeekOfYear 0 = sun, 6 = sat
        //                      the first week is the week that contains the first
        //                      of this day of the week
        //                      (eg. ISO weeks use thursday (4))
    function weekOfYear(mom, firstDayOfWeek, firstDayOfWeekOfYear) {
        var end = firstDayOfWeekOfYear - firstDayOfWeek,
            daysToDayOfWeek = firstDayOfWeekOfYear - mom.day(),
            adjustedMoment;


        if (daysToDayOfWeek > end) {
            daysToDayOfWeek -= 7;
        }

        if (daysToDayOfWeek < end - 7) {
            daysToDayOfWeek += 7;
        }

        adjustedMoment = moment(mom).add('d', daysToDayOfWeek);
        return {
            week: Math.ceil(adjustedMoment.dayOfYear() / 7),
            year: adjustedMoment.year()
        };
    }

    //http://en.wikipedia.org/wiki/ISO_week_date#Calculating_a_date_given_the_year.2C_week_number_and_weekday
    function dayOfYearFromWeeks(year, week, weekday, firstDayOfWeekOfYear, firstDayOfWeek) {
        var d = new Date(Date.UTC(year, 0)).getUTCDay(),
            daysToAdd, dayOfYear;

        weekday = weekday != null ? weekday : firstDayOfWeek;
        daysToAdd = firstDayOfWeek - d + (d > firstDayOfWeekOfYear ? 7 : 0);
        dayOfYear = 7 * (week - 1) + (weekday - firstDayOfWeek) + daysToAdd + 1;

        return {
            year: dayOfYear > 0 ? year : year - 1,
            dayOfYear: dayOfYear > 0 ?  dayOfYear : daysInYear(year - 1) + dayOfYear
        };
    }

    /************************************
     Top Level Functions
     ************************************/

    function makeMoment(config) {
        var input = config._i,
            format = config._f;

        if (typeof config._pf === 'undefined') {
            initializeParsingFlags(config);
        }

        if (input === null) {
            return moment.invalid({nullInput: true});
        }

        if (typeof input === 'string') {
            config._i = input = getLangDefinition().preparse(input);
        }

        if (moment.isMoment(input)) {
            config = extend({}, input);

            config._d = new Date(+input._d);
        } else if (format) {
            if (isArray(format)) {
                makeDateFromStringAndArray(config);
            } else {
                makeDateFromStringAndFormat(config);
            }
        } else {
            makeDateFromInput(config);
        }

        return new Moment(config);
    }

    moment = function (input, format, lang, strict) {
        if (typeof(lang) === "boolean") {
            strict = lang;
            lang = undefined;
        }
        return makeMoment({
            _i : input,
            _f : format,
            _l : lang,
            _strict : strict,
            _isUTC : false
        });
    };

    // creating with utc
    moment.utc = function (input, format, lang, strict) {
        var m;

        if (typeof(lang) === "boolean") {
            strict = lang;
            lang = undefined;
        }
        m = makeMoment({
            _useUTC : true,
            _isUTC : true,
            _l : lang,
            _i : input,
            _f : format,
            _strict : strict
        }).utc();

        return m;
    };

    // creating with unix timestamp (in seconds)
    moment.unix = function (input) {
        return moment(input * 1000);
    };

    // duration
    moment.duration = function (input, key) {
        var isDuration = moment.isDuration(input),
            isNumber = (typeof input === 'number'),
            duration = (isDuration ? input._input : (isNumber ? {} : input)),
        // matching against regexp is expensive, do it on demand
            match = null,
            sign,
            ret,
            parseIso,
            timeEmpty,
            dateTimeEmpty;

        if (isNumber) {
            if (key) {
                duration[key] = input;
            } else {
                duration.milliseconds = input;
            }
        } else if (!!(match = aspNetTimeSpanJsonRegex.exec(input))) {
            sign = (match[1] === "-") ? -1 : 1;
            duration = {
                y: 0,
                d: toInt(match[DATE]) * sign,
                h: toInt(match[HOUR]) * sign,
                m: toInt(match[MINUTE]) * sign,
                s: toInt(match[SECOND]) * sign,
                ms: toInt(match[MILLISECOND]) * sign
            };
        } else if (!!(match = isoDurationRegex.exec(input))) {
            sign = (match[1] === "-") ? -1 : 1;
            parseIso = function (inp) {
                // We'd normally use ~~inp for this, but unfortunately it also
                // converts floats to ints.
                // inp may be undefined, so careful calling replace on it.
                var res = inp && parseFloat(inp.replace(',', '.'));
                // apply sign while we're at it
                return (isNaN(res) ? 0 : res) * sign;
            };
            duration = {
                y: parseIso(match[2]),
                M: parseIso(match[3]),
                d: parseIso(match[4]),
                h: parseIso(match[5]),
                m: parseIso(match[6]),
                s: parseIso(match[7]),
                w: parseIso(match[8])
            };
        }

        ret = new Duration(duration);

        if (isDuration && input.hasOwnProperty('_lang')) {
            ret._lang = input._lang;
        }

        return ret;
    };

    // version number
    moment.version = VERSION;

    // default format
    moment.defaultFormat = isoFormat;

    // This function will be called whenever a moment is mutated.
    // It is intended to keep the offset in sync with the timezone.
    moment.updateOffset = function () {};

    // This function will load languages and then set the global language.  If
    // no arguments are passed in, it will simply return the current global
    // language key.
    moment.lang = function (key, values) {
        var r;
        if (!key) {
            return moment.fn._lang._abbr;
        }
        if (values) {
            loadLang(normalizeLanguage(key), values);
        } else if (values === null) {
            unloadLang(key);
            key = 'en';
        } else if (!languages[key]) {
            getLangDefinition(key);
        }
        r = moment.duration.fn._lang = moment.fn._lang = getLangDefinition(key);
        return r._abbr;
    };

    // returns language data
    moment.langData = function (key) {
        if (key && key._lang && key._lang._abbr) {
            key = key._lang._abbr;
        }
        return getLangDefinition(key);
    };

    // compare moment object
    moment.isMoment = function (obj) {
        return obj instanceof Moment;
    };

    // for typechecking Duration objects
    moment.isDuration = function (obj) {
        return obj instanceof Duration;
    };

    for (i = lists.length - 1; i >= 0; --i) {
        makeList(lists[i]);
    }

    moment.normalizeUnits = function (units) {
        return normalizeUnits(units);
    };

    moment.invalid = function (flags) {
        var m = moment.utc(NaN);
        if (flags != null) {
            extend(m._pf, flags);
        }
        else {
            m._pf.userInvalidated = true;
        }

        return m;
    };

    moment.parseZone = function (input) {
        return moment(input).parseZone();
    };

    /************************************
     Moment Prototype
     ************************************/


    extend(moment.fn = Moment.prototype, {

        clone : function () {
            return moment(this);
        },

        valueOf : function () {
            return +this._d + ((this._offset || 0) * 60000);
        },

        unix : function () {
            return Math.floor(+this / 1000);
        },

        toString : function () {
            return this.clone().lang('en').format("ddd MMM DD YYYY HH:mm:ss [GMT]ZZ");
        },

        toDate : function () {
            return this._offset ? new Date(+this) : this._d;
        },

        toISOString : function () {
            return formatMoment(moment(this).utc(), 'YYYY-MM-DD[T]HH:mm:ss.SSS[Z]');
        },

        toArray : function () {
            var m = this;
            return [
                m.year(),
                m.month(),
                m.date(),
                m.hours(),
                m.minutes(),
                m.seconds(),
                m.milliseconds()
            ];
        },

        isValid : function () {
            return isValid(this);
        },

        isDSTShifted : function () {

            if (this._a) {
                return this.isValid() && compareArrays(this._a, (this._isUTC ? moment.utc(this._a) : moment(this._a)).toArray()) > 0;
            }

            return false;
        },

        parsingFlags : function () {
            return extend({}, this._pf);
        },

        invalidAt: function () {
            return this._pf.overflow;
        },

        utc : function () {
            return this.zone(0);
        },

        local : function () {
            this.zone(0);
            this._isUTC = false;
            return this;
        },

        format : function (inputString) {
            var output = formatMoment(this, inputString || moment.defaultFormat);
            return this.lang().postformat(output);
        },

        add : function (input, val) {
            var dur;
            // switch args to support add('s', 1) and add(1, 's')
            if (typeof input === 'string') {
                dur = moment.duration(+val, input);
            } else {
                dur = moment.duration(input, val);
            }
            addOrSubtractDurationFromMoment(this, dur, 1);
            return this;
        },

        subtract : function (input, val) {
            var dur;
            // switch args to support subtract('s', 1) and subtract(1, 's')
            if (typeof input === 'string') {
                dur = moment.duration(+val, input);
            } else {
                dur = moment.duration(input, val);
            }
            addOrSubtractDurationFromMoment(this, dur, -1);
            return this;
        },

        diff : function (input, units, asFloat) {
            var that = this._isUTC ? moment(input).zone(this._offset || 0) : moment(input).local(),
                zoneDiff = (this.zone() - that.zone()) * 6e4,
                diff, output;

            units = normalizeUnits(units);

            if (units === 'year' || units === 'month') {
                // average number of days in the months in the given dates
                diff = (this.daysInMonth() + that.daysInMonth()) * 432e5; // 24 * 60 * 60 * 1000 / 2
                // difference in months
                output = ((this.year() - that.year()) * 12) + (this.month() - that.month());
                // adjust by taking difference in days, average number of days
                // and dst in the given months.
                output += ((this - moment(this).startOf('month')) -
                    (that - moment(that).startOf('month'))) / diff;
                // same as above but with zones, to negate all dst
                output -= ((this.zone() - moment(this).startOf('month').zone()) -
                    (that.zone() - moment(that).startOf('month').zone())) * 6e4 / diff;
                if (units === 'year') {
                    output = output / 12;
                }
            } else {
                diff = (this - that);
                output = units === 'second' ? diff / 1e3 : // 1000
                    units === 'minute' ? diff / 6e4 : // 1000 * 60
                        units === 'hour' ? diff / 36e5 : // 1000 * 60 * 60
                            units === 'day' ? (diff - zoneDiff) / 864e5 : // 1000 * 60 * 60 * 24, negate dst
                                units === 'week' ? (diff - zoneDiff) / 6048e5 : // 1000 * 60 * 60 * 24 * 7, negate dst
                                    diff;
            }
            return asFloat ? output : absRound(output);
        },

        from : function (time, withoutSuffix) {
            return moment.duration(this.diff(time)).lang(this.lang()._abbr).humanize(!withoutSuffix);
        },

        fromNow : function (withoutSuffix) {
            return this.from(moment(), withoutSuffix);
        },

        calendar : function () {
            var diff = this.diff(moment().zone(this.zone()).startOf('day'), 'days', true),
                format = diff < -6 ? 'sameElse' :
                    diff < -1 ? 'lastWeek' :
                        diff < 0 ? 'lastDay' :
                            diff < 1 ? 'sameDay' :
                                diff < 2 ? 'nextDay' :
                                    diff < 7 ? 'nextWeek' : 'sameElse';
            return this.format(this.lang().calendar(format, this));
        },

        isLeapYear : function () {
            return isLeapYear(this.year());
        },

        isDST : function () {
            return (this.zone() < this.clone().month(0).zone() ||
                this.zone() < this.clone().month(5).zone());
        },

        day : function (input) {
            var day = this._isUTC ? this._d.getUTCDay() : this._d.getDay();
            if (input != null) {
                input = parseWeekday(input, this.lang());
                return this.add({ d : input - day });
            } else {
                return day;
            }
        },

        month : function (input) {
            var utc = this._isUTC ? 'UTC' : '',
                dayOfMonth;

            if (input != null) {
                if (typeof input === 'string') {
                    input = this.lang().monthsParse(input);
                    if (typeof input !== 'number') {
                        return this;
                    }
                }

                dayOfMonth = this.date();
                this.date(1);
                this._d['set' + utc + 'Month'](input);
                this.date(Math.min(dayOfMonth, this.daysInMonth()));

                moment.updateOffset(this);
                return this;
            } else {
                return this._d['get' + utc + 'Month']();
            }
        },

        startOf: function (units) {
            units = normalizeUnits(units);
            // the following switch intentionally omits break keywords
            // to utilize falling through the cases.
            switch (units) {
                case 'year':
                    this.month(0);
                /* falls through */
                case 'month':
                    this.date(1);
                /* falls through */
                case 'week':
                case 'isoWeek':
                case 'day':
                    this.hours(0);
                /* falls through */
                case 'hour':
                    this.minutes(0);
                /* falls through */
                case 'minute':
                    this.seconds(0);
                /* falls through */
                case 'second':
                    this.milliseconds(0);
                /* falls through */
            }

            // weeks are a special case
            if (units === 'week') {
                this.weekday(0);
            } else if (units === 'isoWeek') {
                this.isoWeekday(1);
            }

            return this;
        },

        endOf: function (units) {
            units = normalizeUnits(units);
            return this.startOf(units).add((units === 'isoWeek' ? 'week' : units), 1).subtract('ms', 1);
        },

        isAfter: function (input, units) {
            units = typeof units !== 'undefined' ? units : 'millisecond';
            return +this.clone().startOf(units) > +moment(input).startOf(units);
        },

        isBefore: function (input, units) {
            units = typeof units !== 'undefined' ? units : 'millisecond';
            return +this.clone().startOf(units) < +moment(input).startOf(units);
        },

        isSame: function (input, units) {
            units = typeof units !== 'undefined' ? units : 'millisecond';
            return +this.clone().startOf(units) === +moment(input).startOf(units);
        },

        min: function (other) {
            other = moment.apply(null, arguments);
            return other < this ? this : other;
        },

        max: function (other) {
            other = moment.apply(null, arguments);
            return other > this ? this : other;
        },

        zone : function (input) {
            var offset = this._offset || 0;
            if (input != null) {
                if (typeof input === "string") {
                    input = timezoneMinutesFromString(input);
                }
                if (Math.abs(input) < 16) {
                    input = input * 60;
                }
                this._offset = input;
                this._isUTC = true;
                if (offset !== input) {
                    addOrSubtractDurationFromMoment(this, moment.duration(offset - input, 'm'), 1, true);
                }
            } else {
                return this._isUTC ? offset : this._d.getTimezoneOffset();
            }
            return this;
        },

        zoneAbbr : function () {
            return this._isUTC ? "UTC" : "";
        },

        zoneName : function () {
            return this._isUTC ? "Coordinated Universal Time" : "";
        },

        parseZone : function () {
            if (typeof this._i === 'string') {
                this.zone(this._i);
            }
            return this;
        },

        hasAlignedHourOffset : function (input) {
            if (!input) {
                input = 0;
            }
            else {
                input = moment(input).zone();
            }

            return (this.zone() - input) % 60 === 0;
        },

        daysInMonth : function () {
            return daysInMonth(this.year(), this.month());
        },

        dayOfYear : function (input) {
            var dayOfYear = round((moment(this).startOf('day') - moment(this).startOf('year')) / 864e5) + 1;
            return input == null ? dayOfYear : this.add("d", (input - dayOfYear));
        },

        weekYear : function (input) {
            var year = weekOfYear(this, this.lang()._week.dow, this.lang()._week.doy).year;
            return input == null ? year : this.add("y", (input - year));
        },

        isoWeekYear : function (input) {
            var year = weekOfYear(this, 1, 4).year;
            return input == null ? year : this.add("y", (input - year));
        },

        week : function (input) {
            var week = this.lang().week(this);
            return input == null ? week : this.add("d", (input - week) * 7);
        },

        isoWeek : function (input) {
            var week = weekOfYear(this, 1, 4).week;
            return input == null ? week : this.add("d", (input - week) * 7);
        },

        weekday : function (input) {
            var weekday = (this.day() + 7 - this.lang()._week.dow) % 7;
            return input == null ? weekday : this.add("d", input - weekday);
        },

        isoWeekday : function (input) {
            // behaves the same as moment#day except
            // as a getter, returns 7 instead of 0 (1-7 range instead of 0-6)
            // as a setter, sunday should belong to the previous week.
            return input == null ? this.day() || 7 : this.day(this.day() % 7 ? input : input - 7);
        },

        get : function (units) {
            units = normalizeUnits(units);
            return this[units]();
        },

        set : function (units, value) {
            units = normalizeUnits(units);
            if (typeof this[units] === 'function') {
                this[units](value);
            }
            return this;
        },

        // If passed a language key, it will set the language for this
        // instance.  Otherwise, it will return the language configuration
        // variables for this instance.
        lang : function (key) {
            if (key === undefined) {
                return this._lang;
            } else {
                this._lang = getLangDefinition(key);
                return this;
            }
        }
    });

    // helper for adding shortcuts
    function makeGetterAndSetter(name, key) {
        moment.fn[name] = moment.fn[name + 's'] = function (input) {
            var utc = this._isUTC ? 'UTC' : '';
            if (input != null) {
                this._d['set' + utc + key](input);
                moment.updateOffset(this);
                return this;
            } else {
                return this._d['get' + utc + key]();
            }
        };
    }

    // loop through and add shortcuts (Month, Date, Hours, Minutes, Seconds, Milliseconds)
    for (i = 0; i < proxyGettersAndSetters.length; i ++) {
        makeGetterAndSetter(proxyGettersAndSetters[i].toLowerCase().replace(/s$/, ''), proxyGettersAndSetters[i]);
    }

    // add shortcut for year (uses different syntax than the getter/setter 'year' == 'FullYear')
    makeGetterAndSetter('year', 'FullYear');

    // add plural methods
    moment.fn.days = moment.fn.day;
    moment.fn.months = moment.fn.month;
    moment.fn.weeks = moment.fn.week;
    moment.fn.isoWeeks = moment.fn.isoWeek;

    // add aliased format methods
    moment.fn.toJSON = moment.fn.toISOString;

    /************************************
     Duration Prototype
     ************************************/


    extend(moment.duration.fn = Duration.prototype, {

        _bubble : function () {
            var milliseconds = this._milliseconds,
                days = this._days,
                months = this._months,
                data = this._data,
                seconds, minutes, hours, years;

            // The following code bubbles up values, see the tests for
            // examples of what that means.
            data.milliseconds = milliseconds % 1000;

            seconds = absRound(milliseconds / 1000);
            data.seconds = seconds % 60;

            minutes = absRound(seconds / 60);
            data.minutes = minutes % 60;

            hours = absRound(minutes / 60);
            data.hours = hours % 24;

            days += absRound(hours / 24);
            data.days = days % 30;

            months += absRound(days / 30);
            data.months = months % 12;

            years = absRound(months / 12);
            data.years = years;
        },

        weeks : function () {
            return absRound(this.days() / 7);
        },

        valueOf : function () {
            return this._milliseconds +
                this._days * 864e5 +
                (this._months % 12) * 2592e6 +
                toInt(this._months / 12) * 31536e6;
        },

        humanize : function (withSuffix) {
            var difference = +this,
                output = relativeTime(difference, !withSuffix, this.lang());

            if (withSuffix) {
                output = this.lang().pastFuture(difference, output);
            }

            return this.lang().postformat(output);
        },

        add : function (input, val) {
            // supports only 2.0-style add(1, 's') or add(moment)
            var dur = moment.duration(input, val);

            this._milliseconds += dur._milliseconds;
            this._days += dur._days;
            this._months += dur._months;

            this._bubble();

            return this;
        },

        subtract : function (input, val) {
            var dur = moment.duration(input, val);

            this._milliseconds -= dur._milliseconds;
            this._days -= dur._days;
            this._months -= dur._months;

            this._bubble();

            return this;
        },

        get : function (units) {
            units = normalizeUnits(units);
            return this[units.toLowerCase() + 's']();
        },

        as : function (units) {
            units = normalizeUnits(units);
            return this['as' + units.charAt(0).toUpperCase() + units.slice(1) + 's']();
        },

        lang : moment.fn.lang,

        toIsoString : function () {
            // inspired by https://github.com/dordille/moment-isoduration/blob/master/moment.isoduration.js
            var years = Math.abs(this.years()),
                months = Math.abs(this.months()),
                days = Math.abs(this.days()),
                hours = Math.abs(this.hours()),
                minutes = Math.abs(this.minutes()),
                seconds = Math.abs(this.seconds() + this.milliseconds() / 1000);

            if (!this.asSeconds()) {
                // this is the same as C#'s (Noda) and python (isodate)...
                // but not other JS (goog.date)
                return 'P0D';
            }

            return (this.asSeconds() < 0 ? '-' : '') +
                'P' +
                (years ? years + 'Y' : '') +
                (months ? months + 'M' : '') +
                (days ? days + 'D' : '') +
                ((hours || minutes || seconds) ? 'T' : '') +
                (hours ? hours + 'H' : '') +
                (minutes ? minutes + 'M' : '') +
                (seconds ? seconds + 'S' : '');
        }
    });

    function makeDurationGetter(name) {
        moment.duration.fn[name] = function () {
            return this._data[name];
        };
    }

    function makeDurationAsGetter(name, factor) {
        moment.duration.fn['as' + name] = function () {
            return +this / factor;
        };
    }

    for (i in unitMillisecondFactors) {
        if (unitMillisecondFactors.hasOwnProperty(i)) {
            makeDurationAsGetter(i, unitMillisecondFactors[i]);
            makeDurationGetter(i.toLowerCase());
        }
    }

    makeDurationAsGetter('Weeks', 6048e5);
    moment.duration.fn.asMonths = function () {
        return (+this - this.years() * 31536e6) / 2592e6 + this.years() * 12;
    };


    /************************************
     Default Lang
     ************************************/


        // Set default language, other languages will inherit from English.
    moment.lang('en', {
        ordinal : function (number) {
            var b = number % 10,
                output = (toInt(number % 100 / 10) === 1) ? 'th' :
                    (b === 1) ? 'st' :
                        (b === 2) ? 'nd' :
                            (b === 3) ? 'rd' : 'th';
            return number + output;
        }
    });

    // moment.js language configuration
// language : Moroccan Arabic (ar-ma)
// author : ElFadili Yassine : https://github.com/ElFadiliY
// author : Abdel Said : https://github.com/abdelsaid

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('ar-ma', {
            months : "ÙŠÙ†Ø§ÙŠØ±_ÙØ¨Ø±Ø§ÙŠØ±_Ù…Ø§Ø±Ø³_Ø£Ø¨Ø±ÙŠÙ„_Ù…Ø§ÙŠ_ÙŠÙˆÙ†ÙŠÙˆ_ÙŠÙˆÙ„ÙŠÙˆØ²_ØºØ´Øª_Ø´ØªÙ†Ø¨Ø±_Ø£ÙƒØªÙˆØ¨Ø±_Ù†ÙˆÙ†Ø¨Ø±_Ø¯Ø¬Ù†Ø¨Ø±".split("_"),
            monthsShort : "ÙŠÙ†Ø§ÙŠØ±_ÙØ¨Ø±Ø§ÙŠØ±_Ù…Ø§Ø±Ø³_Ø£Ø¨Ø±ÙŠÙ„_Ù…Ø§ÙŠ_ÙŠÙˆÙ†ÙŠÙˆ_ÙŠÙˆÙ„ÙŠÙˆØ²_ØºØ´Øª_Ø´ØªÙ†Ø¨Ø±_Ø£ÙƒØªÙˆØ¨Ø±_Ù†ÙˆÙ†Ø¨Ø±_Ø¯Ø¬Ù†Ø¨Ø±".split("_"),
            weekdays : "Ø§Ù„Ø£Ø­Ø¯_Ø§Ù„Ø¥ØªÙ†ÙŠÙ†_Ø§Ù„Ø«Ù„Ø§Ø«Ø§Ø¡_Ø§Ù„Ø£Ø±Ø¨Ø¹Ø§Ø¡_Ø§Ù„Ø®Ù…ÙŠØ³_Ø§Ù„Ø¬Ù…Ø¹Ø©_Ø§Ù„Ø³Ø¨Øª".split("_"),
            weekdaysShort : "Ø§Ø­Ø¯_Ø§ØªÙ†ÙŠÙ†_Ø«Ù„Ø§Ø«Ø§Ø¡_Ø§Ø±Ø¨Ø¹Ø§Ø¡_Ø®Ù…ÙŠØ³_Ø¬Ù…Ø¹Ø©_Ø³Ø¨Øª".split("_"),
            weekdaysMin : "Ø­_Ù†_Ø«_Ø±_Ø®_Ø¬_Ø³".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[Ø§Ù„ÙŠÙˆÙ… Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT",
                nextDay: '[ØºØ¯Ø§ Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT',
                nextWeek: 'dddd [Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT',
                lastDay: '[Ø£Ù…Ø³ Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT',
                lastWeek: 'dddd [Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "ÙÙŠ %s",
                past : "Ù…Ù†Ø° %s",
                s : "Ø«ÙˆØ§Ù†",
                m : "Ø¯Ù‚ÙŠÙ‚Ø©",
                mm : "%d Ø¯Ù‚Ø§Ø¦Ù‚",
                h : "Ø³Ø§Ø¹Ø©",
                hh : "%d Ø³Ø§Ø¹Ø§Øª",
                d : "ÙŠÙˆÙ…",
                dd : "%d Ø£ÙŠØ§Ù…",
                M : "Ø´Ù‡Ø±",
                MM : "%d Ø£Ø´Ù‡Ø±",
                y : "Ø³Ù†Ø©",
                yy : "%d Ø³Ù†ÙˆØ§Øª"
            },
            week : {
                dow : 6, // Saturday is the first day of the week.
                doy : 12  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : Arabic (ar)
// author : Abdel Said : https://github.com/abdelsaid
// changes in months, weekdays : Ahmed Elkhatib

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('ar', {
            months : "ÙŠÙ†Ø§ÙŠØ±/ ÙƒØ§Ù†ÙˆÙ† Ø§Ù„Ø«Ø§Ù†ÙŠ_ÙØ¨Ø±Ø§ÙŠØ±/ Ø´Ø¨Ø§Ø·_Ù…Ø§Ø±Ø³/ Ø¢Ø°Ø§Ø±_Ø£Ø¨Ø±ÙŠÙ„/ Ù†ÙŠØ³Ø§Ù†_Ù…Ø§ÙŠÙˆ/ Ø£ÙŠØ§Ø±_ÙŠÙˆÙ†ÙŠÙˆ/ Ø­Ø²ÙŠØ±Ø§Ù†_ÙŠÙˆÙ„ÙŠÙˆ/ ØªÙ…ÙˆØ²_Ø£ØºØ³Ø·Ø³/ Ø¢Ø¨_Ø³Ø¨ØªÙ…Ø¨Ø±/ Ø£ÙŠÙ„ÙˆÙ„_Ø£ÙƒØªÙˆØ¨Ø±/ ØªØ´Ø±ÙŠÙ† Ø§Ù„Ø£ÙˆÙ„_Ù†ÙˆÙÙ…Ø¨Ø±/ ØªØ´Ø±ÙŠÙ† Ø§Ù„Ø«Ø§Ù†ÙŠ_Ø¯ÙŠØ³Ù…Ø¨Ø±/ ÙƒØ§Ù†ÙˆÙ† Ø§Ù„Ø£ÙˆÙ„".split("_"),
            monthsShort : "ÙŠÙ†Ø§ÙŠØ±/ ÙƒØ§Ù†ÙˆÙ† Ø§Ù„Ø«Ø§Ù†ÙŠ_ÙØ¨Ø±Ø§ÙŠØ±/ Ø´Ø¨Ø§Ø·_Ù…Ø§Ø±Ø³/ Ø¢Ø°Ø§Ø±_Ø£Ø¨Ø±ÙŠÙ„/ Ù†ÙŠØ³Ø§Ù†_Ù…Ø§ÙŠÙˆ/ Ø£ÙŠØ§Ø±_ÙŠÙˆÙ†ÙŠÙˆ/ Ø­Ø²ÙŠØ±Ø§Ù†_ÙŠÙˆÙ„ÙŠÙˆ/ ØªÙ…ÙˆØ²_Ø£ØºØ³Ø·Ø³/ Ø¢Ø¨_Ø³Ø¨ØªÙ…Ø¨Ø±/ Ø£ÙŠÙ„ÙˆÙ„_Ø£ÙƒØªÙˆØ¨Ø±/ ØªØ´Ø±ÙŠÙ† Ø§Ù„Ø£ÙˆÙ„_Ù†ÙˆÙÙ…Ø¨Ø±/ ØªØ´Ø±ÙŠÙ† Ø§Ù„Ø«Ø§Ù†ÙŠ_Ø¯ÙŠØ³Ù…Ø¨Ø±/ ÙƒØ§Ù†ÙˆÙ† Ø§Ù„Ø£ÙˆÙ„".split("_"),
            weekdays : "Ø§Ù„Ø£Ø­Ø¯_Ø§Ù„Ø¥Ø«Ù†ÙŠÙ†_Ø§Ù„Ø«Ù„Ø§Ø«Ø§Ø¡_Ø§Ù„Ø£Ø±Ø¨Ø¹Ø§Ø¡_Ø§Ù„Ø®Ù…ÙŠØ³_Ø§Ù„Ø¬Ù…Ø¹Ø©_Ø§Ù„Ø³Ø¨Øª".split("_"),
            weekdaysShort : "Ø§Ù„Ø£Ø­Ø¯_Ø§Ù„Ø¥Ø«Ù†ÙŠÙ†_Ø§Ù„Ø«Ù„Ø§Ø«Ø§Ø¡_Ø§Ù„Ø£Ø±Ø¨Ø¹Ø§Ø¡_Ø§Ù„Ø®Ù…ÙŠØ³_Ø§Ù„Ø¬Ù…Ø¹Ø©_Ø§Ù„Ø³Ø¨Øª".split("_"),
            weekdaysMin : "Ø­_Ù†_Ø«_Ø±_Ø®_Ø¬_Ø³".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[Ø§Ù„ÙŠÙˆÙ… Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT",
                nextDay: '[ØºØ¯Ø§ Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT',
                nextWeek: 'dddd [Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT',
                lastDay: '[Ø£Ù…Ø³ Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT',
                lastWeek: 'dddd [Ø¹Ù„Ù‰ Ø§Ù„Ø³Ø§Ø¹Ø©] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "ÙÙŠ %s",
                past : "Ù…Ù†Ø° %s",
                s : "Ø«ÙˆØ§Ù†",
                m : "Ø¯Ù‚ÙŠÙ‚Ø©",
                mm : "%d Ø¯Ù‚Ø§Ø¦Ù‚",
                h : "Ø³Ø§Ø¹Ø©",
                hh : "%d Ø³Ø§Ø¹Ø§Øª",
                d : "ÙŠÙˆÙ…",
                dd : "%d Ø£ÙŠØ§Ù…",
                M : "Ø´Ù‡Ø±",
                MM : "%d Ø£Ø´Ù‡Ø±",
                y : "Ø³Ù†Ø©",
                yy : "%d Ø³Ù†ÙˆØ§Øª"
            },
            week : {
                dow : 6, // Saturday is the first day of the week.
                doy : 12  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : bulgarian (bg)
// author : Krasen Borisov : https://github.com/kraz

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('bg', {
            months : "ÑÐ½ÑƒÐ°Ñ€Ð¸_Ñ„ÐµÐ²Ñ€ÑƒÐ°Ñ€Ð¸_Ð¼Ð°Ñ€Ñ‚_Ð°Ð¿Ñ€Ð¸Ð»_Ð¼Ð°Ð¹_ÑŽÐ½Ð¸_ÑŽÐ»Ð¸_Ð°Ð²Ð³ÑƒÑÑ‚_ÑÐµÐ¿Ñ‚ÐµÐ¼Ð²Ñ€Ð¸_Ð¾ÐºÑ‚Ð¾Ð¼Ð²Ñ€Ð¸_Ð½Ð¾ÐµÐ¼Ð²Ñ€Ð¸_Ð´ÐµÐºÐµÐ¼Ð²Ñ€Ð¸".split("_"),
            monthsShort : "ÑÐ½Ñ€_Ñ„ÐµÐ²_Ð¼Ð°Ñ€_Ð°Ð¿Ñ€_Ð¼Ð°Ð¹_ÑŽÐ½Ð¸_ÑŽÐ»Ð¸_Ð°Ð²Ð³_ÑÐµÐ¿_Ð¾ÐºÑ‚_Ð½Ð¾Ðµ_Ð´ÐµÐº".split("_"),
            weekdays : "Ð½ÐµÐ´ÐµÐ»Ñ_Ð¿Ð¾Ð½ÐµÐ´ÐµÐ»Ð½Ð¸Ðº_Ð²Ñ‚Ð¾Ñ€Ð½Ð¸Ðº_ÑÑ€ÑÐ´Ð°_Ñ‡ÐµÑ‚Ð²ÑŠÑ€Ñ‚ÑŠÐº_Ð¿ÐµÑ‚ÑŠÐº_ÑÑŠÐ±Ð¾Ñ‚Ð°".split("_"),
            weekdaysShort : "Ð½ÐµÐ´_Ð¿Ð¾Ð½_Ð²Ñ‚Ð¾_ÑÑ€Ñ_Ñ‡ÐµÑ‚_Ð¿ÐµÑ‚_ÑÑŠÐ±".split("_"),
            weekdaysMin : "Ð½Ð´_Ð¿Ð½_Ð²Ñ‚_ÑÑ€_Ñ‡Ñ‚_Ð¿Ñ‚_ÑÐ±".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "D.MM.YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd, D MMMM YYYY LT"
            },
            calendar : {
                sameDay : '[Ð”Ð½ÐµÑ Ð²] LT',
                nextDay : '[Ð£Ñ‚Ñ€Ðµ Ð²] LT',
                nextWeek : 'dddd [Ð²] LT',
                lastDay : '[Ð’Ñ‡ÐµÑ€Ð° Ð²] LT',
                lastWeek : function () {
                    switch (this.day()) {
                        case 0:
                        case 3:
                        case 6:
                            return '[Ð’ Ð¸Ð·Ð¼Ð¸Ð½Ð°Ð»Ð°Ñ‚Ð°] dddd [Ð²] LT';
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                            return '[Ð’ Ð¸Ð·Ð¼Ð¸Ð½Ð°Ð»Ð¸Ñ] dddd [Ð²] LT';
                    }
                },
                sameElse : 'L'
            },
            relativeTime : {
                future : "ÑÐ»ÐµÐ´ %s",
                past : "Ð¿Ñ€ÐµÐ´Ð¸ %s",
                s : "Ð½ÑÐºÐ¾Ð»ÐºÐ¾ ÑÐµÐºÑƒÐ½Ð´Ð¸",
                m : "Ð¼Ð¸Ð½ÑƒÑ‚Ð°",
                mm : "%d Ð¼Ð¸Ð½ÑƒÑ‚Ð¸",
                h : "Ñ‡Ð°Ñ",
                hh : "%d Ñ‡Ð°ÑÐ°",
                d : "Ð´ÐµÐ½",
                dd : "%d Ð´Ð½Ð¸",
                M : "Ð¼ÐµÑÐµÑ†",
                MM : "%d Ð¼ÐµÑÐµÑ†Ð°",
                y : "Ð³Ð¾Ð´Ð¸Ð½Ð°",
                yy : "%d Ð³Ð¾Ð´Ð¸Ð½Ð¸"
            },
            ordinal : function (number) {
                var lastDigit = number % 10,
                    last2Digits = number % 100;
                if (number === 0) {
                    return number + '-ÐµÐ²';
                } else if (last2Digits === 0) {
                    return number + '-ÐµÐ½';
                } else if (last2Digits > 10 && last2Digits < 20) {
                    return number + '-Ñ‚Ð¸';
                } else if (lastDigit === 1) {
                    return number + '-Ð²Ð¸';
                } else if (lastDigit === 2) {
                    return number + '-Ñ€Ð¸';
                } else if (lastDigit === 7 || lastDigit === 8) {
                    return number + '-Ð¼Ð¸';
                } else {
                    return number + '-Ñ‚Ð¸';
                }
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : breton (br)
// author : Jean-Baptiste Le Duigou : https://github.com/jbleduigou

    (function (factory) {
        factory(moment);
    }(function (moment) {
        function relativeTimeWithMutation(number, withoutSuffix, key) {
            var format = {
                'mm': "munutenn",
                'MM': "miz",
                'dd': "devezh"
            };
            return number + ' ' + mutation(format[key], number);
        }

        function specialMutationForYears(number) {
            switch (lastNumber(number)) {
                case 1:
                case 3:
                case 4:
                case 5:
                case 9:
                    return number + ' bloaz';
                default:
                    return number + ' vloaz';
            }
        }

        function lastNumber(number) {
            if (number > 9) {
                return lastNumber(number % 10);
            }
            return number;
        }

        function mutation(text, number) {
            if (number === 2) {
                return softMutation(text);
            }
            return text;
        }

        function softMutation(text) {
            var mutationTable = {
                'm': 'v',
                'b': 'v',
                'd': 'z'
            };
            if (mutationTable[text.charAt(0)] === undefined) {
                return text;
            }
            return mutationTable[text.charAt(0)] + text.substring(1);
        }

        return moment.lang('br', {
            months : "Genver_C'hwevrer_Meurzh_Ebrel_Mae_Mezheven_Gouere_Eost_Gwengolo_Here_Du_Kerzu".split("_"),
            monthsShort : "Gen_C'hwe_Meu_Ebr_Mae_Eve_Gou_Eos_Gwe_Her_Du_Ker".split("_"),
            weekdays : "Sul_Lun_Meurzh_Merc'her_Yaou_Gwener_Sadorn".split("_"),
            weekdaysShort : "Sul_Lun_Meu_Mer_Yao_Gwe_Sad".split("_"),
            weekdaysMin : "Su_Lu_Me_Mer_Ya_Gw_Sa".split("_"),
            longDateFormat : {
                LT : "h[e]mm A",
                L : "DD/MM/YYYY",
                LL : "D [a viz] MMMM YYYY",
                LLL : "D [a viz] MMMM YYYY LT",
                LLLL : "dddd, D [a viz] MMMM YYYY LT"
            },
            calendar : {
                sameDay : '[Hiziv da] LT',
                nextDay : '[Warc\'hoazh da] LT',
                nextWeek : 'dddd [da] LT',
                lastDay : '[Dec\'h da] LT',
                lastWeek : 'dddd [paset da] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "a-benn %s",
                past : "%s 'zo",
                s : "un nebeud segondennoÃ¹",
                m : "ur vunutenn",
                mm : relativeTimeWithMutation,
                h : "un eur",
                hh : "%d eur",
                d : "un devezh",
                dd : relativeTimeWithMutation,
                M : "ur miz",
                MM : relativeTimeWithMutation,
                y : "ur bloaz",
                yy : specialMutationForYears
            },
            ordinal : function (number) {
                var output = (number === 1) ? 'aÃ±' : 'vet';
                return number + output;
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : bosnian (bs)
// author : Nedim Cholich : https://github.com/frontyard
// based on (hr) translation by Bojan MarkoviÄ‡

    (function (factory) {
        factory(moment);
    }(function (moment) {

        function translate(number, withoutSuffix, key) {
            var result = number + " ";
            switch (key) {
                case 'm':
                    return withoutSuffix ? 'jedna minuta' : 'jedne minute';
                case 'mm':
                    if (number === 1) {
                        result += 'minuta';
                    } else if (number === 2 || number === 3 || number === 4) {
                        result += 'minute';
                    } else {
                        result += 'minuta';
                    }
                    return result;
                case 'h':
                    return withoutSuffix ? 'jedan sat' : 'jednog sata';
                case 'hh':
                    if (number === 1) {
                        result += 'sat';
                    } else if (number === 2 || number === 3 || number === 4) {
                        result += 'sata';
                    } else {
                        result += 'sati';
                    }
                    return result;
                case 'dd':
                    if (number === 1) {
                        result += 'dan';
                    } else {
                        result += 'dana';
                    }
                    return result;
                case 'MM':
                    if (number === 1) {
                        result += 'mjesec';
                    } else if (number === 2 || number === 3 || number === 4) {
                        result += 'mjeseca';
                    } else {
                        result += 'mjeseci';
                    }
                    return result;
                case 'yy':
                    if (number === 1) {
                        result += 'godina';
                    } else if (number === 2 || number === 3 || number === 4) {
                        result += 'godine';
                    } else {
                        result += 'godina';
                    }
                    return result;
            }
        }

        return moment.lang('bs', {
            months : "januar_februar_mart_april_maj_juni_juli_avgust_septembar_oktobar_novembar_decembar".split("_"),
            monthsShort : "jan._feb._mar._apr._maj._jun._jul._avg._sep._okt._nov._dec.".split("_"),
            weekdays : "nedjelja_ponedjeljak_utorak_srijeda_Äetvrtak_petak_subota".split("_"),
            weekdaysShort : "ned._pon._uto._sri._Äet._pet._sub.".split("_"),
            weekdaysMin : "ne_po_ut_sr_Äe_pe_su".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "DD. MM. YYYY",
                LL : "D. MMMM YYYY",
                LLL : "D. MMMM YYYY LT",
                LLLL : "dddd, D. MMMM YYYY LT"
            },
            calendar : {
                sameDay  : '[danas u] LT',
                nextDay  : '[sutra u] LT',

                nextWeek : function () {
                    switch (this.day()) {
                        case 0:
                            return '[u] [nedjelju] [u] LT';
                        case 3:
                            return '[u] [srijedu] [u] LT';
                        case 6:
                            return '[u] [subotu] [u] LT';
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                            return '[u] dddd [u] LT';
                    }
                },
                lastDay  : '[juÄer u] LT',
                lastWeek : function () {
                    switch (this.day()) {
                        case 0:
                        case 3:
                            return '[proÅ¡lu] dddd [u] LT';
                        case 6:
                            return '[proÅ¡le] [subote] [u] LT';
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                            return '[proÅ¡li] dddd [u] LT';
                    }
                },
                sameElse : 'L'
            },
            relativeTime : {
                future : "za %s",
                past   : "prije %s",
                s      : "par sekundi",
                m      : translate,
                mm     : translate,
                h      : translate,
                hh     : translate,
                d      : "dan",
                dd     : translate,
                M      : "mjesec",
                MM     : translate,
                y      : "godinu",
                yy     : translate
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : catalan (ca)
// author : Juan G. Hurtado : https://github.com/juanghurtado

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('ca', {
            months : "Gener_Febrer_MarÃ§_Abril_Maig_Juny_Juliol_Agost_Setembre_Octubre_Novembre_Desembre".split("_"),
            monthsShort : "Gen._Febr._Mar._Abr._Mai._Jun._Jul._Ag._Set._Oct._Nov._Des.".split("_"),
            weekdays : "Diumenge_Dilluns_Dimarts_Dimecres_Dijous_Divendres_Dissabte".split("_"),
            weekdaysShort : "Dg._Dl._Dt._Dc._Dj._Dv._Ds.".split("_"),
            weekdaysMin : "Dg_Dl_Dt_Dc_Dj_Dv_Ds".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay : function () {
                    return '[avui a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
                },
                nextDay : function () {
                    return '[demÃ  a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
                },
                nextWeek : function () {
                    return 'dddd [a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
                },
                lastDay : function () {
                    return '[ahir a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
                },
                lastWeek : function () {
                    return '[el] dddd [passat a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
                },
                sameElse : 'L'
            },
            relativeTime : {
                future : "en %s",
                past : "fa %s",
                s : "uns segons",
                m : "un minut",
                mm : "%d minuts",
                h : "una hora",
                hh : "%d hores",
                d : "un dia",
                dd : "%d dies",
                M : "un mes",
                MM : "%d mesos",
                y : "un any",
                yy : "%d anys"
            },
            ordinal : '%dÂº',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : czech (cs)
// author : petrbela : https://github.com/petrbela

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var months = "leden_Ãºnor_bÅ™ezen_duben_kvÄ›ten_Äerven_Äervenec_srpen_zÃ¡Å™Ã­_Å™Ã­jen_listopad_prosinec".split("_"),
            monthsShort = "led_Ãºno_bÅ™e_dub_kvÄ›_Ävn_Ävc_srp_zÃ¡Å™_Å™Ã­j_lis_pro".split("_");

        function plural(n) {
            return (n > 1) && (n < 5) && (~~(n / 10) !== 1);
        }

        function translate(number, withoutSuffix, key, isFuture) {
            var result = number + " ";
            switch (key) {
                case 's':  // a few seconds / in a few seconds / a few seconds ago
                    return (withoutSuffix || isFuture) ? 'pÃ¡r vteÅ™in' : 'pÃ¡r vteÅ™inami';
                case 'm':  // a minute / in a minute / a minute ago
                    return withoutSuffix ? 'minuta' : (isFuture ? 'minutu' : 'minutou');
                case 'mm': // 9 minutes / in 9 minutes / 9 minutes ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'minuty' : 'minut');
                    } else {
                        return result + 'minutami';
                    }
                    break;
                case 'h':  // an hour / in an hour / an hour ago
                    return withoutSuffix ? 'hodina' : (isFuture ? 'hodinu' : 'hodinou');
                case 'hh': // 9 hours / in 9 hours / 9 hours ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'hodiny' : 'hodin');
                    } else {
                        return result + 'hodinami';
                    }
                    break;
                case 'd':  // a day / in a day / a day ago
                    return (withoutSuffix || isFuture) ? 'den' : 'dnem';
                case 'dd': // 9 days / in 9 days / 9 days ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'dny' : 'dnÃ­');
                    } else {
                        return result + 'dny';
                    }
                    break;
                case 'M':  // a month / in a month / a month ago
                    return (withoutSuffix || isFuture) ? 'mÄ›sÃ­c' : 'mÄ›sÃ­cem';
                case 'MM': // 9 months / in 9 months / 9 months ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'mÄ›sÃ­ce' : 'mÄ›sÃ­cÅ¯');
                    } else {
                        return result + 'mÄ›sÃ­ci';
                    }
                    break;
                case 'y':  // a year / in a year / a year ago
                    return (withoutSuffix || isFuture) ? 'rok' : 'rokem';
                case 'yy': // 9 years / in 9 years / 9 years ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'roky' : 'let');
                    } else {
                        return result + 'lety';
                    }
                    break;
            }
        }

        return moment.lang('cs', {
            months : months,
            monthsShort : monthsShort,
            monthsParse : (function (months, monthsShort) {
                var i, _monthsParse = [];
                for (i = 0; i < 12; i++) {
                    // use custom parser to solve problem with July (Äervenec)
                    _monthsParse[i] = new RegExp('^' + months[i] + '$|^' + monthsShort[i] + '$', 'i');
                }
                return _monthsParse;
            }(months, monthsShort)),
            weekdays : "nedÄ›le_pondÄ›lÃ­_ÃºterÃ½_stÅ™eda_Ätvrtek_pÃ¡tek_sobota".split("_"),
            weekdaysShort : "ne_po_Ãºt_st_Ät_pÃ¡_so".split("_"),
            weekdaysMin : "ne_po_Ãºt_st_Ät_pÃ¡_so".split("_"),
            longDateFormat : {
                LT: "H:mm",
                L : "DD.MM.YYYY",
                LL : "D. MMMM YYYY",
                LLL : "D. MMMM YYYY LT",
                LLLL : "dddd D. MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[dnes v] LT",
                nextDay: '[zÃ­tra v] LT',
                nextWeek: function () {
                    switch (this.day()) {
                        case 0:
                            return '[v nedÄ›li v] LT';
                        case 1:
                        case 2:
                            return '[v] dddd [v] LT';
                        case 3:
                            return '[ve stÅ™edu v] LT';
                        case 4:
                            return '[ve Ätvrtek v] LT';
                        case 5:
                            return '[v pÃ¡tek v] LT';
                        case 6:
                            return '[v sobotu v] LT';
                    }
                },
                lastDay: '[vÄera v] LT',
                lastWeek: function () {
                    switch (this.day()) {
                        case 0:
                            return '[minulou nedÄ›li v] LT';
                        case 1:
                        case 2:
                            return '[minulÃ©] dddd [v] LT';
                        case 3:
                            return '[minulou stÅ™edu v] LT';
                        case 4:
                        case 5:
                            return '[minulÃ½] dddd [v] LT';
                        case 6:
                            return '[minulou sobotu v] LT';
                    }
                },
                sameElse: "L"
            },
            relativeTime : {
                future : "za %s",
                past : "pÅ™ed %s",
                s : translate,
                m : translate,
                mm : translate,
                h : translate,
                hh : translate,
                d : translate,
                dd : translate,
                M : translate,
                MM : translate,
                y : translate,
                yy : translate
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : chuvash (cv)
// author : Anatoly Mironov : https://github.com/mirontoli

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('cv', {
            months : "ÐºÄƒÑ€Ð»Ð°Ñ‡_Ð½Ð°Ñ€ÄƒÑ_Ð¿ÑƒÑˆ_Ð°ÐºÐ°_Ð¼Ð°Ð¹_Ã§Ä•Ñ€Ñ‚Ð¼Ðµ_ÑƒÑ‚Äƒ_Ã§ÑƒÑ€Ð»Ð°_Ð°Ð²ÄƒÐ½_ÑŽÐ¿Ð°_Ñ‡Ó³Ðº_Ñ€Ð°ÑˆÑ‚Ð°Ð²".split("_"),
            monthsShort : "ÐºÄƒÑ€_Ð½Ð°Ñ€_Ð¿ÑƒÑˆ_Ð°ÐºÐ°_Ð¼Ð°Ð¹_Ã§Ä•Ñ€_ÑƒÑ‚Äƒ_Ã§ÑƒÑ€_Ð°Ð²_ÑŽÐ¿Ð°_Ñ‡Ó³Ðº_Ñ€Ð°Ñˆ".split("_"),
            weekdays : "Ð²Ñ‹Ñ€ÑÐ°Ñ€Ð½Ð¸ÐºÑƒÐ½_Ñ‚ÑƒÐ½Ñ‚Ð¸ÐºÑƒÐ½_Ñ‹Ñ‚Ð»Ð°Ñ€Ð¸ÐºÑƒÐ½_ÑŽÐ½ÐºÑƒÐ½_ÐºÄ•Ã§Ð½ÐµÑ€Ð½Ð¸ÐºÑƒÐ½_ÑÑ€Ð½ÐµÐºÑƒÐ½_ÑˆÄƒÐ¼Ð°Ñ‚ÐºÑƒÐ½".split("_"),
            weekdaysShort : "Ð²Ñ‹Ñ€_Ñ‚ÑƒÐ½_Ñ‹Ñ‚Ð»_ÑŽÐ½_ÐºÄ•Ã§_ÑÑ€Ð½_ÑˆÄƒÐ¼".split("_"),
            weekdaysMin : "Ð²Ñ€_Ñ‚Ð½_Ñ‹Ñ‚_ÑŽÐ½_ÐºÃ§_ÑÑ€_ÑˆÐ¼".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD-MM-YYYY",
                LL : "YYYY [Ã§ÑƒÐ»Ñ…Ð¸] MMMM [ÑƒÐ¹ÄƒÑ…Ä•Ð½] D[-Ð¼Ä•ÑˆÄ•]",
                LLL : "YYYY [Ã§ÑƒÐ»Ñ…Ð¸] MMMM [ÑƒÐ¹ÄƒÑ…Ä•Ð½] D[-Ð¼Ä•ÑˆÄ•], LT",
                LLLL : "dddd, YYYY [Ã§ÑƒÐ»Ñ…Ð¸] MMMM [ÑƒÐ¹ÄƒÑ…Ä•Ð½] D[-Ð¼Ä•ÑˆÄ•], LT"
            },
            calendar : {
                sameDay: '[ÐŸÐ°ÑÐ½] LT [ÑÐµÑ…ÐµÑ‚Ñ€Ðµ]',
                nextDay: '[Ð«Ñ€Ð°Ð½] LT [ÑÐµÑ…ÐµÑ‚Ñ€Ðµ]',
                lastDay: '[Ä”Ð½ÐµÑ€] LT [ÑÐµÑ…ÐµÑ‚Ñ€Ðµ]',
                nextWeek: '[Ã‡Ð¸Ñ‚ÐµÑ] dddd LT [ÑÐµÑ…ÐµÑ‚Ñ€Ðµ]',
                lastWeek: '[Ð˜Ñ€Ñ‚Ð½Ä•] dddd LT [ÑÐµÑ…ÐµÑ‚Ñ€Ðµ]',
                sameElse: 'L'
            },
            relativeTime : {
                future : function (output) {
                    var affix = /ÑÐµÑ…ÐµÑ‚$/i.exec(output) ? "Ñ€ÐµÐ½" : /Ã§ÑƒÐ»$/i.exec(output) ? "Ñ‚Ð°Ð½" : "Ñ€Ð°Ð½";
                    return output + affix;
                },
                past : "%s ÐºÐ°ÑÐ»Ð»Ð°",
                s : "Ð¿Ä•Ñ€-Ð¸Ðº Ã§ÐµÐºÐºÑƒÐ½Ñ‚",
                m : "Ð¿Ä•Ñ€ Ð¼Ð¸Ð½ÑƒÑ‚",
                mm : "%d Ð¼Ð¸Ð½ÑƒÑ‚",
                h : "Ð¿Ä•Ñ€ ÑÐµÑ…ÐµÑ‚",
                hh : "%d ÑÐµÑ…ÐµÑ‚",
                d : "Ð¿Ä•Ñ€ ÐºÑƒÐ½",
                dd : "%d ÐºÑƒÐ½",
                M : "Ð¿Ä•Ñ€ ÑƒÐ¹ÄƒÑ…",
                MM : "%d ÑƒÐ¹ÄƒÑ…",
                y : "Ð¿Ä•Ñ€ Ã§ÑƒÐ»",
                yy : "%d Ã§ÑƒÐ»"
            },
            ordinal : '%d-Ð¼Ä•Ñˆ',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : Welsh (cy)
// author : Robert Allen

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang("cy", {
            months: "Ionawr_Chwefror_Mawrth_Ebrill_Mai_Mehefin_Gorffennaf_Awst_Medi_Hydref_Tachwedd_Rhagfyr".split("_"),
            monthsShort: "Ion_Chwe_Maw_Ebr_Mai_Meh_Gor_Aws_Med_Hyd_Tach_Rhag".split("_"),
            weekdays: "Dydd Sul_Dydd Llun_Dydd Mawrth_Dydd Mercher_Dydd Iau_Dydd Gwener_Dydd Sadwrn".split("_"),
            weekdaysShort: "Sul_Llun_Maw_Mer_Iau_Gwe_Sad".split("_"),
            weekdaysMin: "Su_Ll_Ma_Me_Ia_Gw_Sa".split("_"),
            // time formats are the same as en-gb
            longDateFormat: {
                LT: "HH:mm",
                L: "DD/MM/YYYY",
                LL: "D MMMM YYYY",
                LLL: "D MMMM YYYY LT",
                LLLL: "dddd, D MMMM YYYY LT"
            },
            calendar: {
                sameDay: '[Heddiw am] LT',
                nextDay: '[Yfory am] LT',
                nextWeek: 'dddd [am] LT',
                lastDay: '[Ddoe am] LT',
                lastWeek: 'dddd [diwethaf am] LT',
                sameElse: 'L'
            },
            relativeTime: {
                future: "mewn %s",
                past: "%s yn &#244;l",
                s: "ychydig eiliadau",
                m: "munud",
                mm: "%d munud",
                h: "awr",
                hh: "%d awr",
                d: "diwrnod",
                dd: "%d diwrnod",
                M: "mis",
                MM: "%d mis",
                y: "blwyddyn",
                yy: "%d flynedd"
            },
            // traditional ordinal numbers above 31 are not commonly used in colloquial Welsh
            ordinal: function (number) {
                var b = number,
                    output = '',
                    lookup = [
                        '', 'af', 'il', 'ydd', 'ydd', 'ed', 'ed', 'ed', 'fed', 'fed', 'fed', // 1af to 10fed
                        'eg', 'fed', 'eg', 'eg', 'fed', 'eg', 'eg', 'fed', 'eg', 'fed' // 11eg to 20fed
                    ];

                if (b > 20) {
                    if (b === 40 || b === 50 || b === 60 || b === 80 || b === 100) {
                        output = 'fed'; // not 30ain, 70ain or 90ain
                    } else {
                        output = 'ain';
                    }
                } else if (b > 0) {
                    output = lookup[b];
                }

                return number + output;
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : danish (da)
// author : Ulrik Nielsen : https://github.com/mrbase

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('da', {
            months : "januar_februar_marts_april_maj_juni_juli_august_september_oktober_november_december".split("_"),
            monthsShort : "jan_feb_mar_apr_maj_jun_jul_aug_sep_okt_nov_dec".split("_"),
            weekdays : "sÃ¸ndag_mandag_tirsdag_onsdag_torsdag_fredag_lÃ¸rdag".split("_"),
            weekdaysShort : "sÃ¸n_man_tir_ons_tor_fre_lÃ¸r".split("_"),
            weekdaysMin : "sÃ¸_ma_ti_on_to_fr_lÃ¸".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D. MMMM, YYYY LT"
            },
            calendar : {
                sameDay : '[I dag kl.] LT',
                nextDay : '[I morgen kl.] LT',
                nextWeek : 'dddd [kl.] LT',
                lastDay : '[I gÃ¥r kl.] LT',
                lastWeek : '[sidste] dddd [kl] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "om %s",
                past : "%s siden",
                s : "fÃ¥ sekunder",
                m : "et minut",
                mm : "%d minutter",
                h : "en time",
                hh : "%d timer",
                d : "en dag",
                dd : "%d dage",
                M : "en mÃ¥ned",
                MM : "%d mÃ¥neder",
                y : "et Ã¥r",
                yy : "%d Ã¥r"
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : german (de)
// author : lluchs : https://github.com/lluchs
// author: Menelion ElensÃºle: https://github.com/Oire

    (function (factory) {
        factory(moment);
    }(function (moment) {
        function processRelativeTime(number, withoutSuffix, key, isFuture) {
            var format = {
                'm': ['eine Minute', 'einer Minute'],
                'h': ['eine Stunde', 'einer Stunde'],
                'd': ['ein Tag', 'einem Tag'],
                'dd': [number + ' Tage', number + ' Tagen'],
                'M': ['ein Monat', 'einem Monat'],
                'MM': [number + ' Monate', number + ' Monaten'],
                'y': ['ein Jahr', 'einem Jahr'],
                'yy': [number + ' Jahre', number + ' Jahren']
            };
            return withoutSuffix ? format[key][0] : format[key][1];
        }

        return moment.lang('de', {
            months : "Januar_Februar_MÃ¤rz_April_Mai_Juni_Juli_August_September_Oktober_November_Dezember".split("_"),
            monthsShort : "Jan._Febr._Mrz._Apr._Mai_Jun._Jul._Aug._Sept._Okt._Nov._Dez.".split("_"),
            weekdays : "Sonntag_Montag_Dienstag_Mittwoch_Donnerstag_Freitag_Samstag".split("_"),
            weekdaysShort : "So._Mo._Di._Mi._Do._Fr._Sa.".split("_"),
            weekdaysMin : "So_Mo_Di_Mi_Do_Fr_Sa".split("_"),
            longDateFormat : {
                LT: "H:mm [Uhr]",
                L : "DD.MM.YYYY",
                LL : "D. MMMM YYYY",
                LLL : "D. MMMM YYYY LT",
                LLLL : "dddd, D. MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[Heute um] LT",
                sameElse: "L",
                nextDay: '[Morgen um] LT',
                nextWeek: 'dddd [um] LT',
                lastDay: '[Gestern um] LT',
                lastWeek: '[letzten] dddd [um] LT'
            },
            relativeTime : {
                future : "in %s",
                past : "vor %s",
                s : "ein paar Sekunden",
                m : processRelativeTime,
                mm : "%d Minuten",
                h : processRelativeTime,
                hh : "%d Stunden",
                d : processRelativeTime,
                dd : processRelativeTime,
                M : processRelativeTime,
                MM : processRelativeTime,
                y : processRelativeTime,
                yy : processRelativeTime
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : modern greek (el)
// author : Aggelos Karalias : https://github.com/mehiel

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('el', {
            monthsNominativeEl : "Î™Î±Î½Î¿Ï…Î¬ÏÎ¹Î¿Ï‚_Î¦ÎµÎ²ÏÎ¿Ï…Î¬ÏÎ¹Î¿Ï‚_ÎœÎ¬ÏÏ„Î¹Î¿Ï‚_Î‘Ï€ÏÎ¯Î»Î¹Î¿Ï‚_ÎœÎ¬Î¹Î¿Ï‚_Î™Î¿ÏÎ½Î¹Î¿Ï‚_Î™Î¿ÏÎ»Î¹Î¿Ï‚_Î‘ÏÎ³Î¿Ï…ÏƒÏ„Î¿Ï‚_Î£ÎµÏ€Ï„Î­Î¼Î²ÏÎ¹Î¿Ï‚_ÎŸÎºÏ„ÏŽÎ²ÏÎ¹Î¿Ï‚_ÎÎ¿Î­Î¼Î²ÏÎ¹Î¿Ï‚_Î”ÎµÎºÎ­Î¼Î²ÏÎ¹Î¿Ï‚".split("_"),
            monthsGenitiveEl : "Î™Î±Î½Î¿Ï…Î±ÏÎ¯Î¿Ï…_Î¦ÎµÎ²ÏÎ¿Ï…Î±ÏÎ¯Î¿Ï…_ÎœÎ±ÏÏ„Î¯Î¿Ï…_Î‘Ï€ÏÎ¹Î»Î¯Î¿Ï…_ÎœÎ±ÎÎ¿Ï…_Î™Î¿Ï…Î½Î¯Î¿Ï…_Î™Î¿Ï…Î»Î¯Î¿Ï…_Î‘Ï…Î³Î¿ÏÏƒÏ„Î¿Ï…_Î£ÎµÏ€Ï„ÎµÎ¼Î²ÏÎ¯Î¿Ï…_ÎŸÎºÏ„Ï‰Î²ÏÎ¯Î¿Ï…_ÎÎ¿ÎµÎ¼Î²ÏÎ¯Î¿Ï…_Î”ÎµÎºÎµÎ¼Î²ÏÎ¯Î¿Ï…".split("_"),
            months : function (momentToFormat, format) {
                if (/D/.test(format.substring(0, format.indexOf("MMMM")))) { // if there is a day number before 'MMMM'
                    return this._monthsGenitiveEl[momentToFormat.month()];
                } else {
                    return this._monthsNominativeEl[momentToFormat.month()];
                }
            },
            monthsShort : "Î™Î±Î½_Î¦ÎµÎ²_ÎœÎ±Ï_Î‘Ï€Ï_ÎœÎ±ÏŠ_Î™Î¿Ï…Î½_Î™Î¿Ï…Î»_Î‘Ï…Î³_Î£ÎµÏ€_ÎŸÎºÏ„_ÎÎ¿Îµ_Î”ÎµÎº".split("_"),
            weekdays : "ÎšÏ…ÏÎ¹Î±ÎºÎ®_Î”ÎµÏ…Ï„Î­ÏÎ±_Î¤ÏÎ¯Ï„Î·_Î¤ÎµÏ„Î¬ÏÏ„Î·_Î Î­Î¼Ï€Ï„Î·_Î Î±ÏÎ±ÏƒÎºÎµÏ…Î®_Î£Î¬Î²Î²Î±Ï„Î¿".split("_"),
            weekdaysShort : "ÎšÏ…Ï_Î”ÎµÏ…_Î¤ÏÎ¹_Î¤ÎµÏ„_Î ÎµÎ¼_Î Î±Ï_Î£Î±Î²".split("_"),
            weekdaysMin : "ÎšÏ…_Î”Îµ_Î¤Ï_Î¤Îµ_Î Îµ_Î Î±_Î£Î±".split("_"),
            meridiem : function (hours, minutes, isLower) {
                if (hours > 11) {
                    return isLower ? 'Î¼Î¼' : 'ÎœÎœ';
                } else {
                    return isLower ? 'Ï€Î¼' : 'Î Îœ';
                }
            },
            longDateFormat : {
                LT : "h:mm A",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd, D MMMM YYYY LT"
            },
            calendarEl : {
                sameDay : '[Î£Î®Î¼ÎµÏÎ± {}] LT',
                nextDay : '[Î‘ÏÏÎ¹Î¿ {}] LT',
                nextWeek : 'dddd [{}] LT',
                lastDay : '[Î§Î¸ÎµÏ‚ {}] LT',
                lastWeek : '[Ï„Î·Î½ Ï€ÏÎ¿Î·Î³Î¿ÏÎ¼ÎµÎ½Î·] dddd [{}] LT',
                sameElse : 'L'
            },
            calendar : function (key, mom) {
                var output = this._calendarEl[key],
                    hours = mom && mom.hours();

                return output.replace("{}", (hours % 12 === 1 ? "ÏƒÏ„Î·" : "ÏƒÏ„Î¹Ï‚"));
            },
            relativeTime : {
                future : "ÏƒÎµ %s",
                past : "%s Ï€ÏÎ¹Î½",
                s : "Î´ÎµÏ…Ï„ÎµÏÏŒÎ»ÎµÏ€Ï„Î±",
                m : "Î­Î½Î± Î»ÎµÏ€Ï„ÏŒ",
                mm : "%d Î»ÎµÏ€Ï„Î¬",
                h : "Î¼Î¯Î± ÏŽÏÎ±",
                hh : "%d ÏŽÏÎµÏ‚",
                d : "Î¼Î¯Î± Î¼Î­ÏÎ±",
                dd : "%d Î¼Î­ÏÎµÏ‚",
                M : "Î­Î½Î±Ï‚ Î¼Î®Î½Î±Ï‚",
                MM : "%d Î¼Î®Î½ÎµÏ‚",
                y : "Î­Î½Î±Ï‚ Ï‡ÏÏŒÎ½Î¿Ï‚",
                yy : "%d Ï‡ÏÏŒÎ½Î¹Î±"
            },
            ordinal : function (number) {
                return number + 'Î·';
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : australian english (en-au)

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('en-au', {
            months : "January_February_March_April_May_June_July_August_September_October_November_December".split("_"),
            monthsShort : "Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec".split("_"),
            weekdays : "Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"),
            weekdaysShort : "Sun_Mon_Tue_Wed_Thu_Fri_Sat".split("_"),
            weekdaysMin : "Su_Mo_Tu_We_Th_Fr_Sa".split("_"),
            longDateFormat : {
                LT : "h:mm A",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd, D MMMM YYYY LT"
            },
            calendar : {
                sameDay : '[Today at] LT',
                nextDay : '[Tomorrow at] LT',
                nextWeek : 'dddd [at] LT',
                lastDay : '[Yesterday at] LT',
                lastWeek : '[Last] dddd [at] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "in %s",
                past : "%s ago",
                s : "a few seconds",
                m : "a minute",
                mm : "%d minutes",
                h : "an hour",
                hh : "%d hours",
                d : "a day",
                dd : "%d days",
                M : "a month",
                MM : "%d months",
                y : "a year",
                yy : "%d years"
            },
            ordinal : function (number) {
                var b = number % 10,
                    output = (~~ (number % 100 / 10) === 1) ? 'th' :
                        (b === 1) ? 'st' :
                            (b === 2) ? 'nd' :
                                (b === 3) ? 'rd' : 'th';
                return number + output;
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : canadian english (en-ca)
// author : Jonathan Abourbih : https://github.com/jonbca

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('en-ca', {
            months : "January_February_March_April_May_June_July_August_September_October_November_December".split("_"),
            monthsShort : "Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec".split("_"),
            weekdays : "Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"),
            weekdaysShort : "Sun_Mon_Tue_Wed_Thu_Fri_Sat".split("_"),
            weekdaysMin : "Su_Mo_Tu_We_Th_Fr_Sa".split("_"),
            longDateFormat : {
                LT : "h:mm A",
                L : "YYYY-MM-DD",
                LL : "D MMMM, YYYY",
                LLL : "D MMMM, YYYY LT",
                LLLL : "dddd, D MMMM, YYYY LT"
            },
            calendar : {
                sameDay : '[Today at] LT',
                nextDay : '[Tomorrow at] LT',
                nextWeek : 'dddd [at] LT',
                lastDay : '[Yesterday at] LT',
                lastWeek : '[Last] dddd [at] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "in %s",
                past : "%s ago",
                s : "a few seconds",
                m : "a minute",
                mm : "%d minutes",
                h : "an hour",
                hh : "%d hours",
                d : "a day",
                dd : "%d days",
                M : "a month",
                MM : "%d months",
                y : "a year",
                yy : "%d years"
            },
            ordinal : function (number) {
                var b = number % 10,
                    output = (~~ (number % 100 / 10) === 1) ? 'th' :
                        (b === 1) ? 'st' :
                            (b === 2) ? 'nd' :
                                (b === 3) ? 'rd' : 'th';
                return number + output;
            }
        });
    }));
// moment.js language configuration
// language : great britain english (en-gb)
// author : Chris Gedrim : https://github.com/chrisgedrim

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('en-gb', {
            months : "January_February_March_April_May_June_July_August_September_October_November_December".split("_"),
            monthsShort : "Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec".split("_"),
            weekdays : "Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"),
            weekdaysShort : "Sun_Mon_Tue_Wed_Thu_Fri_Sat".split("_"),
            weekdaysMin : "Su_Mo_Tu_We_Th_Fr_Sa".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd, D MMMM YYYY LT"
            },
            calendar : {
                sameDay : '[Today at] LT',
                nextDay : '[Tomorrow at] LT',
                nextWeek : 'dddd [at] LT',
                lastDay : '[Yesterday at] LT',
                lastWeek : '[Last] dddd [at] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "in %s",
                past : "%s ago",
                s : "a few seconds",
                m : "a minute",
                mm : "%d minutes",
                h : "an hour",
                hh : "%d hours",
                d : "a day",
                dd : "%d days",
                M : "a month",
                MM : "%d months",
                y : "a year",
                yy : "%d years"
            },
            ordinal : function (number) {
                var b = number % 10,
                    output = (~~ (number % 100 / 10) === 1) ? 'th' :
                        (b === 1) ? 'st' :
                            (b === 2) ? 'nd' :
                                (b === 3) ? 'rd' : 'th';
                return number + output;
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : esperanto (eo)
// author : Colin Dean : https://github.com/colindean
// komento: Mi estas malcerta se mi korekte traktis akuzativojn en tiu traduko.
//          Se ne, bonvolu korekti kaj avizi min por ke mi povas lerni!

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('eo', {
            months : "januaro_februaro_marto_aprilo_majo_junio_julio_aÅ­gusto_septembro_oktobro_novembro_decembro".split("_"),
            monthsShort : "jan_feb_mar_apr_maj_jun_jul_aÅ­g_sep_okt_nov_dec".split("_"),
            weekdays : "DimanÄ‰o_Lundo_Mardo_Merkredo_Ä´aÅ­do_Vendredo_Sabato".split("_"),
            weekdaysShort : "Dim_Lun_Mard_Merk_Ä´aÅ­_Ven_Sab".split("_"),
            weekdaysMin : "Di_Lu_Ma_Me_Ä´a_Ve_Sa".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "YYYY-MM-DD",
                LL : "D[-an de] MMMM, YYYY",
                LLL : "D[-an de] MMMM, YYYY LT",
                LLLL : "dddd, [la] D[-an de] MMMM, YYYY LT"
            },
            meridiem : function (hours, minutes, isLower) {
                if (hours > 11) {
                    return isLower ? 'p.t.m.' : 'P.T.M.';
                } else {
                    return isLower ? 'a.t.m.' : 'A.T.M.';
                }
            },
            calendar : {
                sameDay : '[HodiaÅ­ je] LT',
                nextDay : '[MorgaÅ­ je] LT',
                nextWeek : 'dddd [je] LT',
                lastDay : '[HieraÅ­ je] LT',
                lastWeek : '[pasinta] dddd [je] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "je %s",
                past : "antaÅ­ %s",
                s : "sekundoj",
                m : "minuto",
                mm : "%d minutoj",
                h : "horo",
                hh : "%d horoj",
                d : "tago",//ne 'diurno', Ä‰ar estas uzita por proksimumo
                dd : "%d tagoj",
                M : "monato",
                MM : "%d monatoj",
                y : "jaro",
                yy : "%d jaroj"
            },
            ordinal : "%da",
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : spanish (es)
// author : Julio NapurÃ­ : https://github.com/julionc

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('es', {
            months : "enero_febrero_marzo_abril_mayo_junio_julio_agosto_septiembre_octubre_noviembre_diciembre".split("_"),
            monthsShort : "ene._feb._mar._abr._may._jun._jul._ago._sep._oct._nov._dic.".split("_"),
            weekdays : "domingo_lunes_martes_miÃ©rcoles_jueves_viernes_sÃ¡bado".split("_"),
            weekdaysShort : "dom._lun._mar._miÃ©._jue._vie._sÃ¡b.".split("_"),
            weekdaysMin : "Do_Lu_Ma_Mi_Ju_Vi_SÃ¡".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "DD/MM/YYYY",
                LL : "D [de] MMMM [de] YYYY",
                LLL : "D [de] MMMM [de] YYYY LT",
                LLLL : "dddd, D [de] MMMM [de] YYYY LT"
            },
            calendar : {
                sameDay : function () {
                    return '[hoy a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
                },
                nextDay : function () {
                    return '[maÃ±ana a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
                },
                nextWeek : function () {
                    return 'dddd [a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
                },
                lastDay : function () {
                    return '[ayer a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
                },
                lastWeek : function () {
                    return '[el] dddd [pasado a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
                },
                sameElse : 'L'
            },
            relativeTime : {
                future : "en %s",
                past : "hace %s",
                s : "unos segundos",
                m : "un minuto",
                mm : "%d minutos",
                h : "una hora",
                hh : "%d horas",
                d : "un dÃ­a",
                dd : "%d dÃ­as",
                M : "un mes",
                MM : "%d meses",
                y : "un aÃ±o",
                yy : "%d aÃ±os"
            },
            ordinal : '%dÂº',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : estonian (et)
// author : Henry Kehlmann : https://github.com/madhenry

    (function (factory) {
        factory(moment);
    }(function (moment) {
        function translateSeconds(number, withoutSuffix, key, isFuture) {
            return (isFuture || withoutSuffix) ? 'paari sekundi' : 'paar sekundit';
        }

        return moment.lang('et', {
            months        : "jaanuar_veebruar_mÃ¤rts_aprill_mai_juuni_juuli_august_september_oktoober_november_detsember".split("_"),
            monthsShort   : "jaan_veebr_mÃ¤rts_apr_mai_juuni_juuli_aug_sept_okt_nov_dets".split("_"),
            weekdays      : "pÃ¼hapÃ¤ev_esmaspÃ¤ev_teisipÃ¤ev_kolmapÃ¤ev_neljapÃ¤ev_reede_laupÃ¤ev".split("_"),
            weekdaysShort : "P_E_T_K_N_R_L".split("_"),
            weekdaysMin   : "P_E_T_K_N_R_L".split("_"),
            longDateFormat : {
                LT   : "H:mm",
                L    : "DD.MM.YYYY",
                LL   : "D. MMMM YYYY",
                LLL  : "D. MMMM YYYY LT",
                LLLL : "dddd, D. MMMM YYYY LT"
            },
            calendar : {
                sameDay  : '[TÃ¤na,] LT',
                nextDay  : '[Homme,] LT',
                nextWeek : '[JÃ¤rgmine] dddd LT',
                lastDay  : '[Eile,] LT',
                lastWeek : '[Eelmine] dddd LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s pÃ¤rast",
                past   : "%s tagasi",
                s      : translateSeconds,
                m      : "minut",
                mm     : "%d minutit",
                h      : "tund",
                hh     : "%d tundi",
                d      : "pÃ¤ev",
                dd     : "%d pÃ¤eva",
                M      : "kuu",
                MM     : "%d kuud",
                y      : "aasta",
                yy     : "%d aastat"
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : euskara (eu)
// author : Eneko Illarramendi : https://github.com/eillarra

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('eu', {
            months : "urtarrila_otsaila_martxoa_apirila_maiatza_ekaina_uztaila_abuztua_iraila_urria_azaroa_abendua".split("_"),
            monthsShort : "urt._ots._mar._api._mai._eka._uzt._abu._ira._urr._aza._abe.".split("_"),
            weekdays : "igandea_astelehena_asteartea_asteazkena_osteguna_ostirala_larunbata".split("_"),
            weekdaysShort : "ig._al._ar._az._og._ol._lr.".split("_"),
            weekdaysMin : "ig_al_ar_az_og_ol_lr".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "YYYY-MM-DD",
                LL : "YYYY[ko] MMMM[ren] D[a]",
                LLL : "YYYY[ko] MMMM[ren] D[a] LT",
                LLLL : "dddd, YYYY[ko] MMMM[ren] D[a] LT",
                l : "YYYY-M-D",
                ll : "YYYY[ko] MMM D[a]",
                lll : "YYYY[ko] MMM D[a] LT",
                llll : "ddd, YYYY[ko] MMM D[a] LT"
            },
            calendar : {
                sameDay : '[gaur] LT[etan]',
                nextDay : '[bihar] LT[etan]',
                nextWeek : 'dddd LT[etan]',
                lastDay : '[atzo] LT[etan]',
                lastWeek : '[aurreko] dddd LT[etan]',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s barru",
                past : "duela %s",
                s : "segundo batzuk",
                m : "minutu bat",
                mm : "%d minutu",
                h : "ordu bat",
                hh : "%d ordu",
                d : "egun bat",
                dd : "%d egun",
                M : "hilabete bat",
                MM : "%d hilabete",
                y : "urte bat",
                yy : "%d urte"
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : Persian Language
// author : Ebrahim Byagowi : https://github.com/ebraminio

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var symbolMap = {
            '1': 'Û±',
            '2': 'Û²',
            '3': 'Û³',
            '4': 'Û´',
            '5': 'Ûµ',
            '6': 'Û¶',
            '7': 'Û·',
            '8': 'Û¸',
            '9': 'Û¹',
            '0': 'Û°'
        }, numberMap = {
            'Û±': '1',
            'Û²': '2',
            'Û³': '3',
            'Û´': '4',
            'Ûµ': '5',
            'Û¶': '6',
            'Û·': '7',
            'Û¸': '8',
            'Û¹': '9',
            'Û°': '0'
        };

        return moment.lang('fa', {
            months : 'Ú˜Ø§Ù†ÙˆÛŒÙ‡_ÙÙˆØ±ÛŒÙ‡_Ù…Ø§Ø±Ø³_Ø¢ÙˆØ±ÛŒÙ„_Ù…Ù‡_Ú˜ÙˆØ¦Ù†_Ú˜ÙˆØ¦ÛŒÙ‡_Ø§ÙˆØª_Ø³Ù¾ØªØ§Ù…Ø¨Ø±_Ø§Ú©ØªØ¨Ø±_Ù†ÙˆØ§Ù…Ø¨Ø±_Ø¯Ø³Ø§Ù…Ø¨Ø±'.split('_'),
            monthsShort : 'Ú˜Ø§Ù†ÙˆÛŒÙ‡_ÙÙˆØ±ÛŒÙ‡_Ù…Ø§Ø±Ø³_Ø¢ÙˆØ±ÛŒÙ„_Ù…Ù‡_Ú˜ÙˆØ¦Ù†_Ú˜ÙˆØ¦ÛŒÙ‡_Ø§ÙˆØª_Ø³Ù¾ØªØ§Ù…Ø¨Ø±_Ø§Ú©ØªØ¨Ø±_Ù†ÙˆØ§Ù…Ø¨Ø±_Ø¯Ø³Ø§Ù…Ø¨Ø±'.split('_'),
            weekdays : 'ÛŒÚ©\u200cØ´Ù†Ø¨Ù‡_Ø¯ÙˆØ´Ù†Ø¨Ù‡_Ø³Ù‡\u200cØ´Ù†Ø¨Ù‡_Ú†Ù‡Ø§Ø±Ø´Ù†Ø¨Ù‡_Ù¾Ù†Ø¬\u200cØ´Ù†Ø¨Ù‡_Ø¬Ù…Ø¹Ù‡_Ø´Ù†Ø¨Ù‡'.split('_'),
            weekdaysShort : 'ÛŒÚ©\u200cØ´Ù†Ø¨Ù‡_Ø¯ÙˆØ´Ù†Ø¨Ù‡_Ø³Ù‡\u200cØ´Ù†Ø¨Ù‡_Ú†Ù‡Ø§Ø±Ø´Ù†Ø¨Ù‡_Ù¾Ù†Ø¬\u200cØ´Ù†Ø¨Ù‡_Ø¬Ù…Ø¹Ù‡_Ø´Ù†Ø¨Ù‡'.split('_'),
            weekdaysMin : 'ÛŒ_Ø¯_Ø³_Ú†_Ù¾_Ø¬_Ø´'.split('_'),
            longDateFormat : {
                LT : 'HH:mm',
                L : 'DD/MM/YYYY',
                LL : 'D MMMM YYYY',
                LLL : 'D MMMM YYYY LT',
                LLLL : 'dddd, D MMMM YYYY LT'
            },
            meridiem : function (hour, minute, isLower) {
                if (hour < 12) {
                    return "Ù‚Ø¨Ù„ Ø§Ø² Ø¸Ù‡Ø±";
                } else {
                    return "Ø¨Ø¹Ø¯ Ø§Ø² Ø¸Ù‡Ø±";
                }
            },
            calendar : {
                sameDay : '[Ø§Ù…Ø±ÙˆØ² Ø³Ø§Ø¹Øª] LT',
                nextDay : '[ÙØ±Ø¯Ø§ Ø³Ø§Ø¹Øª] LT',
                nextWeek : 'dddd [Ø³Ø§Ø¹Øª] LT',
                lastDay : '[Ø¯ÛŒØ±ÙˆØ² Ø³Ø§Ø¹Øª] LT',
                lastWeek : 'dddd [Ù¾ÛŒØ´] [Ø³Ø§Ø¹Øª] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : 'Ø¯Ø± %s',
                past : '%s Ù¾ÛŒØ´',
                s : 'Ú†Ù†Ø¯ÛŒÙ† Ø«Ø§Ù†ÛŒÙ‡',
                m : 'ÛŒÚ© Ø¯Ù‚ÛŒÙ‚Ù‡',
                mm : '%d Ø¯Ù‚ÛŒÙ‚Ù‡',
                h : 'ÛŒÚ© Ø³Ø§Ø¹Øª',
                hh : '%d Ø³Ø§Ø¹Øª',
                d : 'ÛŒÚ© Ø±ÙˆØ²',
                dd : '%d Ø±ÙˆØ²',
                M : 'ÛŒÚ© Ù…Ø§Ù‡',
                MM : '%d Ù…Ø§Ù‡',
                y : 'ÛŒÚ© Ø³Ø§Ù„',
                yy : '%d Ø³Ø§Ù„'
            },
            preparse: function (string) {
                return string.replace(/[Û°-Û¹]/g, function (match) {
                    return numberMap[match];
                }).replace(/ØŒ/g, ',');
            },
            postformat: function (string) {
                return string.replace(/\d/g, function (match) {
                    return symbolMap[match];
                }).replace(/,/g, 'ØŒ');
            },
            ordinal : '%dÙ…',
            week : {
                dow : 6, // Saturday is the first day of the week.
                doy : 12 // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : finnish (fi)
// author : Tarmo Aidantausta : https://github.com/bleadof

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var numbers_past = 'nolla yksi kaksi kolme neljÃ¤ viisi kuusi seitsemÃ¤n kahdeksan yhdeksÃ¤n'.split(' '),
            numbers_future = ['nolla', 'yhden', 'kahden', 'kolmen', 'neljÃ¤n', 'viiden', 'kuuden',
                numbers_past[7], numbers_past[8], numbers_past[9]];

        function translate(number, withoutSuffix, key, isFuture) {
            var result = "";
            switch (key) {
                case 's':
                    return isFuture ? 'muutaman sekunnin' : 'muutama sekunti';
                case 'm':
                    return isFuture ? 'minuutin' : 'minuutti';
                case 'mm':
                    result = isFuture ? 'minuutin' : 'minuuttia';
                    break;
                case 'h':
                    return isFuture ? 'tunnin' : 'tunti';
                case 'hh':
                    result = isFuture ? 'tunnin' : 'tuntia';
                    break;
                case 'd':
                    return isFuture ? 'pÃ¤ivÃ¤n' : 'pÃ¤ivÃ¤';
                case 'dd':
                    result = isFuture ? 'pÃ¤ivÃ¤n' : 'pÃ¤ivÃ¤Ã¤';
                    break;
                case 'M':
                    return isFuture ? 'kuukauden' : 'kuukausi';
                case 'MM':
                    result = isFuture ? 'kuukauden' : 'kuukautta';
                    break;
                case 'y':
                    return isFuture ? 'vuoden' : 'vuosi';
                case 'yy':
                    result = isFuture ? 'vuoden' : 'vuotta';
                    break;
            }
            result = verbal_number(number, isFuture) + " " + result;
            return result;
        }

        function verbal_number(number, isFuture) {
            return number < 10 ? (isFuture ? numbers_future[number] : numbers_past[number]) : number;
        }

        return moment.lang('fi', {
            months : "tammikuu_helmikuu_maaliskuu_huhtikuu_toukokuu_kesÃ¤kuu_heinÃ¤kuu_elokuu_syyskuu_lokakuu_marraskuu_joulukuu".split("_"),
            monthsShort : "tammi_helmi_maalis_huhti_touko_kesÃ¤_heinÃ¤_elo_syys_loka_marras_joulu".split("_"),
            weekdays : "sunnuntai_maanantai_tiistai_keskiviikko_torstai_perjantai_lauantai".split("_"),
            weekdaysShort : "su_ma_ti_ke_to_pe_la".split("_"),
            weekdaysMin : "su_ma_ti_ke_to_pe_la".split("_"),
            longDateFormat : {
                LT : "HH.mm",
                L : "DD.MM.YYYY",
                LL : "Do MMMM[ta] YYYY",
                LLL : "Do MMMM[ta] YYYY, [klo] LT",
                LLLL : "dddd, Do MMMM[ta] YYYY, [klo] LT",
                l : "D.M.YYYY",
                ll : "Do MMM YYYY",
                lll : "Do MMM YYYY, [klo] LT",
                llll : "ddd, Do MMM YYYY, [klo] LT"
            },
            calendar : {
                sameDay : '[tÃ¤nÃ¤Ã¤n] [klo] LT',
                nextDay : '[huomenna] [klo] LT',
                nextWeek : 'dddd [klo] LT',
                lastDay : '[eilen] [klo] LT',
                lastWeek : '[viime] dddd[na] [klo] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s pÃ¤Ã¤stÃ¤",
                past : "%s sitten",
                s : translate,
                m : translate,
                mm : translate,
                h : translate,
                hh : translate,
                d : translate,
                dd : translate,
                M : translate,
                MM : translate,
                y : translate,
                yy : translate
            },
            ordinal : "%d.",
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : faroese (fo)
// author : Ragnar Johannesen : https://github.com/ragnar123

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('fo', {
            months : "januar_februar_mars_aprÃ­l_mai_juni_juli_august_september_oktober_november_desember".split("_"),
            monthsShort : "jan_feb_mar_apr_mai_jun_jul_aug_sep_okt_nov_des".split("_"),
            weekdays : "sunnudagur_mÃ¡nadagur_tÃ½sdagur_mikudagur_hÃ³sdagur_frÃ­ggjadagur_leygardagur".split("_"),
            weekdaysShort : "sun_mÃ¡n_tÃ½s_mik_hÃ³s_frÃ­_ley".split("_"),
            weekdaysMin : "su_mÃ¡_tÃ½_mi_hÃ³_fr_le".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D. MMMM, YYYY LT"
            },
            calendar : {
                sameDay : '[Ã dag kl.] LT',
                nextDay : '[Ã morgin kl.] LT',
                nextWeek : 'dddd [kl.] LT',
                lastDay : '[Ã gjÃ¡r kl.] LT',
                lastWeek : '[sÃ­Ã°stu] dddd [kl] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "um %s",
                past : "%s sÃ­Ã°ani",
                s : "fÃ¡ sekund",
                m : "ein minutt",
                mm : "%d minuttir",
                h : "ein tÃ­mi",
                hh : "%d tÃ­mar",
                d : "ein dagur",
                dd : "%d dagar",
                M : "ein mÃ¡naÃ°i",
                MM : "%d mÃ¡naÃ°ir",
                y : "eitt Ã¡r",
                yy : "%d Ã¡r"
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : canadian french (fr-ca)
// author : Jonathan Abourbih : https://github.com/jonbca

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('fr-ca', {
            months : "janvier_fÃ©vrier_mars_avril_mai_juin_juillet_aoÃ»t_septembre_octobre_novembre_dÃ©cembre".split("_"),
            monthsShort : "janv._fÃ©vr._mars_avr._mai_juin_juil._aoÃ»t_sept._oct._nov._dÃ©c.".split("_"),
            weekdays : "dimanche_lundi_mardi_mercredi_jeudi_vendredi_samedi".split("_"),
            weekdaysShort : "dim._lun._mar._mer._jeu._ven._sam.".split("_"),
            weekdaysMin : "Di_Lu_Ma_Me_Je_Ve_Sa".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "YYYY-MM-DD",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[Aujourd'hui Ã ] LT",
                nextDay: '[Demain Ã ] LT',
                nextWeek: 'dddd [Ã ] LT',
                lastDay: '[Hier Ã ] LT',
                lastWeek: 'dddd [dernier Ã ] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "dans %s",
                past : "il y a %s",
                s : "quelques secondes",
                m : "une minute",
                mm : "%d minutes",
                h : "une heure",
                hh : "%d heures",
                d : "un jour",
                dd : "%d jours",
                M : "un mois",
                MM : "%d mois",
                y : "un an",
                yy : "%d ans"
            },
            ordinal : function (number) {
                return number + (number === 1 ? 'er' : '');
            }
        });
    }));
// moment.js language configuration
// language : french (fr)
// author : John Fischer : https://github.com/jfroffice

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('fr', {
            months : "janvier_fÃ©vrier_mars_avril_mai_juin_juillet_aoÃ»t_septembre_octobre_novembre_dÃ©cembre".split("_"),
            monthsShort : "janv._fÃ©vr._mars_avr._mai_juin_juil._aoÃ»t_sept._oct._nov._dÃ©c.".split("_"),
            weekdays : "dimanche_lundi_mardi_mercredi_jeudi_vendredi_samedi".split("_"),
            weekdaysShort : "dim._lun._mar._mer._jeu._ven._sam.".split("_"),
            weekdaysMin : "Di_Lu_Ma_Me_Je_Ve_Sa".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[Aujourd'hui Ã ] LT",
                nextDay: '[Demain Ã ] LT',
                nextWeek: 'dddd [Ã ] LT',
                lastDay: '[Hier Ã ] LT',
                lastWeek: 'dddd [dernier Ã ] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "dans %s",
                past : "il y a %s",
                s : "quelques secondes",
                m : "une minute",
                mm : "%d minutes",
                h : "une heure",
                hh : "%d heures",
                d : "un jour",
                dd : "%d jours",
                M : "un mois",
                MM : "%d mois",
                y : "un an",
                yy : "%d ans"
            },
            ordinal : function (number) {
                return number + (number === 1 ? 'er' : '');
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : galician (gl)
// author : Juan G. Hurtado : https://github.com/juanghurtado

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('gl', {
            months : "Xaneiro_Febreiro_Marzo_Abril_Maio_XuÃ±o_Xullo_Agosto_Setembro_Outubro_Novembro_Decembro".split("_"),
            monthsShort : "Xan._Feb._Mar._Abr._Mai._XuÃ±._Xul._Ago._Set._Out._Nov._Dec.".split("_"),
            weekdays : "Domingo_Luns_Martes_MÃ©rcores_Xoves_Venres_SÃ¡bado".split("_"),
            weekdaysShort : "Dom._Lun._Mar._MÃ©r._Xov._Ven._SÃ¡b.".split("_"),
            weekdaysMin : "Do_Lu_Ma_MÃ©_Xo_Ve_SÃ¡".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay : function () {
                    return '[hoxe ' + ((this.hours() !== 1) ? 'Ã¡s' : 'Ã¡') + '] LT';
                },
                nextDay : function () {
                    return '[maÃ±Ã¡ ' + ((this.hours() !== 1) ? 'Ã¡s' : 'Ã¡') + '] LT';
                },
                nextWeek : function () {
                    return 'dddd [' + ((this.hours() !== 1) ? 'Ã¡s' : 'a') + '] LT';
                },
                lastDay : function () {
                    return '[onte ' + ((this.hours() !== 1) ? 'Ã¡' : 'a') + '] LT';
                },
                lastWeek : function () {
                    return '[o] dddd [pasado ' + ((this.hours() !== 1) ? 'Ã¡s' : 'a') + '] LT';
                },
                sameElse : 'L'
            },
            relativeTime : {
                future : function (str) {
                    if (str === "uns segundos") {
                        return "nuns segundos";
                    }
                    return "en " + str;
                },
                past : "hai %s",
                s : "uns segundos",
                m : "un minuto",
                mm : "%d minutos",
                h : "unha hora",
                hh : "%d horas",
                d : "un dÃ­a",
                dd : "%d dÃ­as",
                M : "un mes",
                MM : "%d meses",
                y : "un ano",
                yy : "%d anos"
            },
            ordinal : '%dÂº',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : Hebrew (he)
// author : Tomer Cohen : https://github.com/tomer
// author : Moshe Simantov : https://github.com/DevelopmentIL
// author : Tal Ater : https://github.com/TalAter

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('he', {
            months : "×™× ×•××¨_×¤×‘×¨×•××¨_×ž×¨×¥_××¤×¨×™×œ_×ž××™_×™×•× ×™_×™×•×œ×™_××•×’×•×¡×˜_×¡×¤×˜×ž×‘×¨_××•×§×˜×•×‘×¨_× ×•×‘×ž×‘×¨_×“×¦×ž×‘×¨".split("_"),
            monthsShort : "×™× ×•×³_×¤×‘×¨×³_×ž×¨×¥_××¤×¨×³_×ž××™_×™×•× ×™_×™×•×œ×™_××•×’×³_×¡×¤×˜×³_××•×§×³_× ×•×‘×³_×“×¦×ž×³".split("_"),
            weekdays : "×¨××©×•×Ÿ_×©× ×™_×©×œ×™×©×™_×¨×‘×™×¢×™_×—×ž×™×©×™_×©×™×©×™_×©×‘×ª".split("_"),
            weekdaysShort : "××³_×‘×³_×’×³_×“×³_×”×³_×•×³_×©×³".split("_"),
            weekdaysMin : "×_×‘_×’_×“_×”_×•_×©".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D [×‘]MMMM YYYY",
                LLL : "D [×‘]MMMM YYYY LT",
                LLLL : "dddd, D [×‘]MMMM YYYY LT",
                l : "D/M/YYYY",
                ll : "D MMM YYYY",
                lll : "D MMM YYYY LT",
                llll : "ddd, D MMM YYYY LT"
            },
            calendar : {
                sameDay : '[×”×™×•× ×‘Ö¾]LT',
                nextDay : '[×ž×—×¨ ×‘Ö¾]LT',
                nextWeek : 'dddd [×‘×©×¢×”] LT',
                lastDay : '[××ª×ž×•×œ ×‘Ö¾]LT',
                lastWeek : '[×‘×™×•×] dddd [×”××—×¨×•×Ÿ ×‘×©×¢×”] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "×‘×¢×•×“ %s",
                past : "×œ×¤× ×™ %s",
                s : "×ž×¡×¤×¨ ×©× ×™×•×ª",
                m : "×“×§×”",
                mm : "%d ×“×§×•×ª",
                h : "×©×¢×”",
                hh : function (number) {
                    if (number === 2) {
                        return "×©×¢×ª×™×™×";
                    }
                    return number + " ×©×¢×•×ª";
                },
                d : "×™×•×",
                dd : function (number) {
                    if (number === 2) {
                        return "×™×•×ž×™×™×";
                    }
                    return number + " ×™×ž×™×";
                },
                M : "×—×•×“×©",
                MM : function (number) {
                    if (number === 2) {
                        return "×—×•×“×©×™×™×";
                    }
                    return number + " ×—×•×“×©×™×";
                },
                y : "×©× ×”",
                yy : function (number) {
                    if (number === 2) {
                        return "×©× ×ª×™×™×";
                    }
                    return number + " ×©× ×™×";
                }
            }
        });
    }));
// moment.js language configuration
// language : hindi (hi)
// author : Mayank Singhal : https://github.com/mayanksinghal

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var symbolMap = {
                '1': 'à¥§',
                '2': 'à¥¨',
                '3': 'à¥©',
                '4': 'à¥ª',
                '5': 'à¥«',
                '6': 'à¥¬',
                '7': 'à¥­',
                '8': 'à¥®',
                '9': 'à¥¯',
                '0': 'à¥¦'
            },
            numberMap = {
                'à¥§': '1',
                'à¥¨': '2',
                'à¥©': '3',
                'à¥ª': '4',
                'à¥«': '5',
                'à¥¬': '6',
                'à¥­': '7',
                'à¥®': '8',
                'à¥¯': '9',
                'à¥¦': '0'
            };

        return moment.lang('hi', {
            months : 'à¤œà¤¨à¤µà¤°à¥€_à¤«à¤¼à¤°à¤µà¤°à¥€_à¤®à¤¾à¤°à¥à¤š_à¤…à¤ªà¥à¤°à¥ˆà¤²_à¤®à¤ˆ_à¤œà¥‚à¤¨_à¤œà¥à¤²à¤¾à¤ˆ_à¤…à¤—à¤¸à¥à¤¤_à¤¸à¤¿à¤¤à¤®à¥à¤¬à¤°_à¤…à¤•à¥à¤Ÿà¥‚à¤¬à¤°_à¤¨à¤µà¤®à¥à¤¬à¤°_à¤¦à¤¿à¤¸à¤®à¥à¤¬à¤°'.split("_"),
            monthsShort : 'à¤œà¤¨._à¤«à¤¼à¤°._à¤®à¤¾à¤°à¥à¤š_à¤…à¤ªà¥à¤°à¥ˆ._à¤®à¤ˆ_à¤œà¥‚à¤¨_à¤œà¥à¤²._à¤…à¤—._à¤¸à¤¿à¤¤._à¤…à¤•à¥à¤Ÿà¥‚._à¤¨à¤µ._à¤¦à¤¿à¤¸.'.split("_"),
            weekdays : 'à¤°à¤µà¤¿à¤µà¤¾à¤°_à¤¸à¥‹à¤®à¤µà¤¾à¤°_à¤®à¤‚à¤—à¤²à¤µà¤¾à¤°_à¤¬à¥à¤§à¤µà¤¾à¤°_à¤—à¥à¤°à¥‚à¤µà¤¾à¤°_à¤¶à¥à¤•à¥à¤°à¤µà¤¾à¤°_à¤¶à¤¨à¤¿à¤µà¤¾à¤°'.split("_"),
            weekdaysShort : 'à¤°à¤µà¤¿_à¤¸à¥‹à¤®_à¤®à¤‚à¤—à¤²_à¤¬à¥à¤§_à¤—à¥à¤°à¥‚_à¤¶à¥à¤•à¥à¤°_à¤¶à¤¨à¤¿'.split("_"),
            weekdaysMin : 'à¤°_à¤¸à¥‹_à¤®à¤‚_à¤¬à¥_à¤—à¥_à¤¶à¥_à¤¶'.split("_"),
            longDateFormat : {
                LT : "A h:mm à¤¬à¤œà¥‡",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY, LT",
                LLLL : "dddd, D MMMM YYYY, LT"
            },
            calendar : {
                sameDay : '[à¤†à¤œ] LT',
                nextDay : '[à¤•à¤²] LT',
                nextWeek : 'dddd, LT',
                lastDay : '[à¤•à¤²] LT',
                lastWeek : '[à¤ªà¤¿à¤›à¤²à¥‡] dddd, LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s à¤®à¥‡à¤‚",
                past : "%s à¤ªà¤¹à¤²à¥‡",
                s : "à¤•à¥à¤› à¤¹à¥€ à¤•à¥à¤·à¤£",
                m : "à¤à¤• à¤®à¤¿à¤¨à¤Ÿ",
                mm : "%d à¤®à¤¿à¤¨à¤Ÿ",
                h : "à¤à¤• à¤˜à¤‚à¤Ÿà¤¾",
                hh : "%d à¤˜à¤‚à¤Ÿà¥‡",
                d : "à¤à¤• à¤¦à¤¿à¤¨",
                dd : "%d à¤¦à¤¿à¤¨",
                M : "à¤à¤• à¤®à¤¹à¥€à¤¨à¥‡",
                MM : "%d à¤®à¤¹à¥€à¤¨à¥‡",
                y : "à¤à¤• à¤µà¤°à¥à¤·",
                yy : "%d à¤µà¤°à¥à¤·"
            },
            preparse: function (string) {
                return string.replace(/[à¥§à¥¨à¥©à¥ªà¥«à¥¬à¥­à¥®à¥¯à¥¦]/g, function (match) {
                    return numberMap[match];
                });
            },
            postformat: function (string) {
                return string.replace(/\d/g, function (match) {
                    return symbolMap[match];
                });
            },
            // Hindi notation for meridiems are quite fuzzy in practice. While there exists
            // a rigid notion of a 'Pahar' it is not used as rigidly in modern Hindi.
            meridiem : function (hour, minute, isLower) {
                if (hour < 4) {
                    return "à¤°à¤¾à¤¤";
                } else if (hour < 10) {
                    return "à¤¸à¥à¤¬à¤¹";
                } else if (hour < 17) {
                    return "à¤¦à¥‹à¤ªà¤¹à¤°";
                } else if (hour < 20) {
                    return "à¤¶à¤¾à¤®";
                } else {
                    return "à¤°à¤¾à¤¤";
                }
            },
            week : {
                dow : 0, // Sunday is the first day of the week.
                doy : 6  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : hrvatski (hr)
// author : Bojan MarkoviÄ‡ : https://github.com/bmarkovic

// based on (sl) translation by Robert SedovÅ¡ek

    (function (factory) {
        factory(moment);
    }(function (moment) {

        function translate(number, withoutSuffix, key) {
            var result = number + " ";
            switch (key) {
                case 'm':
                    return withoutSuffix ? 'jedna minuta' : 'jedne minute';
                case 'mm':
                    if (number === 1) {
                        result += 'minuta';
                    } else if (number === 2 || number === 3 || number === 4) {
                        result += 'minute';
                    } else {
                        result += 'minuta';
                    }
                    return result;
                case 'h':
                    return withoutSuffix ? 'jedan sat' : 'jednog sata';
                case 'hh':
                    if (number === 1) {
                        result += 'sat';
                    } else if (number === 2 || number === 3 || number === 4) {
                        result += 'sata';
                    } else {
                        result += 'sati';
                    }
                    return result;
                case 'dd':
                    if (number === 1) {
                        result += 'dan';
                    } else {
                        result += 'dana';
                    }
                    return result;
                case 'MM':
                    if (number === 1) {
                        result += 'mjesec';
                    } else if (number === 2 || number === 3 || number === 4) {
                        result += 'mjeseca';
                    } else {
                        result += 'mjeseci';
                    }
                    return result;
                case 'yy':
                    if (number === 1) {
                        result += 'godina';
                    } else if (number === 2 || number === 3 || number === 4) {
                        result += 'godine';
                    } else {
                        result += 'godina';
                    }
                    return result;
            }
        }

        return moment.lang('hr', {
            months : "sjeÄanj_veljaÄa_oÅ¾ujak_travanj_svibanj_lipanj_srpanj_kolovoz_rujan_listopad_studeni_prosinac".split("_"),
            monthsShort : "sje._vel._oÅ¾u._tra._svi._lip._srp._kol._ruj._lis._stu._pro.".split("_"),
            weekdays : "nedjelja_ponedjeljak_utorak_srijeda_Äetvrtak_petak_subota".split("_"),
            weekdaysShort : "ned._pon._uto._sri._Äet._pet._sub.".split("_"),
            weekdaysMin : "ne_po_ut_sr_Äe_pe_su".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "DD. MM. YYYY",
                LL : "D. MMMM YYYY",
                LLL : "D. MMMM YYYY LT",
                LLLL : "dddd, D. MMMM YYYY LT"
            },
            calendar : {
                sameDay  : '[danas u] LT',
                nextDay  : '[sutra u] LT',

                nextWeek : function () {
                    switch (this.day()) {
                        case 0:
                            return '[u] [nedjelju] [u] LT';
                        case 3:
                            return '[u] [srijedu] [u] LT';
                        case 6:
                            return '[u] [subotu] [u] LT';
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                            return '[u] dddd [u] LT';
                    }
                },
                lastDay  : '[juÄer u] LT',
                lastWeek : function () {
                    switch (this.day()) {
                        case 0:
                        case 3:
                            return '[proÅ¡lu] dddd [u] LT';
                        case 6:
                            return '[proÅ¡le] [subote] [u] LT';
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                            return '[proÅ¡li] dddd [u] LT';
                    }
                },
                sameElse : 'L'
            },
            relativeTime : {
                future : "za %s",
                past   : "prije %s",
                s      : "par sekundi",
                m      : translate,
                mm     : translate,
                h      : translate,
                hh     : translate,
                d      : "dan",
                dd     : translate,
                M      : "mjesec",
                MM     : translate,
                y      : "godinu",
                yy     : translate
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : hungarian (hu)
// author : Adam Brunner : https://github.com/adambrunner

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var weekEndings = 'vasÃ¡rnap hÃ©tfÅ‘n kedden szerdÃ¡n csÃ¼tÃ¶rtÃ¶kÃ¶n pÃ©nteken szombaton'.split(' ');

        function translate(number, withoutSuffix, key, isFuture) {
            var num = number,
                suffix;

            switch (key) {
                case 's':
                    return (isFuture || withoutSuffix) ? 'nÃ©hÃ¡ny mÃ¡sodperc' : 'nÃ©hÃ¡ny mÃ¡sodperce';
                case 'm':
                    return 'egy' + (isFuture || withoutSuffix ? ' perc' : ' perce');
                case 'mm':
                    return num + (isFuture || withoutSuffix ? ' perc' : ' perce');
                case 'h':
                    return 'egy' + (isFuture || withoutSuffix ? ' Ã³ra' : ' Ã³rÃ¡ja');
                case 'hh':
                    return num + (isFuture || withoutSuffix ? ' Ã³ra' : ' Ã³rÃ¡ja');
                case 'd':
                    return 'egy' + (isFuture || withoutSuffix ? ' nap' : ' napja');
                case 'dd':
                    return num + (isFuture || withoutSuffix ? ' nap' : ' napja');
                case 'M':
                    return 'egy' + (isFuture || withoutSuffix ? ' hÃ³nap' : ' hÃ³napja');
                case 'MM':
                    return num + (isFuture || withoutSuffix ? ' hÃ³nap' : ' hÃ³napja');
                case 'y':
                    return 'egy' + (isFuture || withoutSuffix ? ' Ã©v' : ' Ã©ve');
                case 'yy':
                    return num + (isFuture || withoutSuffix ? ' Ã©v' : ' Ã©ve');
            }

            return '';
        }

        function week(isFuture) {
            return (isFuture ? '' : '[mÃºlt] ') + '[' + weekEndings[this.day()] + '] LT[-kor]';
        }

        return moment.lang('hu', {
            months : "januÃ¡r_februÃ¡r_mÃ¡rcius_Ã¡prilis_mÃ¡jus_jÃºnius_jÃºlius_augusztus_szeptember_oktÃ³ber_november_december".split("_"),
            monthsShort : "jan_feb_mÃ¡rc_Ã¡pr_mÃ¡j_jÃºn_jÃºl_aug_szept_okt_nov_dec".split("_"),
            weekdays : "vasÃ¡rnap_hÃ©tfÅ‘_kedd_szerda_csÃ¼tÃ¶rtÃ¶k_pÃ©ntek_szombat".split("_"),
            weekdaysShort : "vas_hÃ©t_kedd_sze_csÃ¼t_pÃ©n_szo".split("_"),
            weekdaysMin : "v_h_k_sze_cs_p_szo".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "YYYY.MM.DD.",
                LL : "YYYY. MMMM D.",
                LLL : "YYYY. MMMM D., LT",
                LLLL : "YYYY. MMMM D., dddd LT"
            },
            calendar : {
                sameDay : '[ma] LT[-kor]',
                nextDay : '[holnap] LT[-kor]',
                nextWeek : function () {
                    return week.call(this, true);
                },
                lastDay : '[tegnap] LT[-kor]',
                lastWeek : function () {
                    return week.call(this, false);
                },
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s mÃºlva",
                past : "%s",
                s : translate,
                m : translate,
                mm : translate,
                h : translate,
                hh : translate,
                d : translate,
                dd : translate,
                M : translate,
                MM : translate,
                y : translate,
                yy : translate
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : Bahasa Indonesia (id)
// author : Mohammad Satrio Utomo : https://github.com/tyok
// reference: http://id.wikisource.org/wiki/Pedoman_Umum_Ejaan_Bahasa_Indonesia_yang_Disempurnakan

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('id', {
            months : "Januari_Februari_Maret_April_Mei_Juni_Juli_Agustus_September_Oktober_November_Desember".split("_"),
            monthsShort : "Jan_Feb_Mar_Apr_Mei_Jun_Jul_Ags_Sep_Okt_Nov_Des".split("_"),
            weekdays : "Minggu_Senin_Selasa_Rabu_Kamis_Jumat_Sabtu".split("_"),
            weekdaysShort : "Min_Sen_Sel_Rab_Kam_Jum_Sab".split("_"),
            weekdaysMin : "Mg_Sn_Sl_Rb_Km_Jm_Sb".split("_"),
            longDateFormat : {
                LT : "HH.mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY [pukul] LT",
                LLLL : "dddd, D MMMM YYYY [pukul] LT"
            },
            meridiem : function (hours, minutes, isLower) {
                if (hours < 11) {
                    return 'pagi';
                } else if (hours < 15) {
                    return 'siang';
                } else if (hours < 19) {
                    return 'sore';
                } else {
                    return 'malam';
                }
            },
            calendar : {
                sameDay : '[Hari ini pukul] LT',
                nextDay : '[Besok pukul] LT',
                nextWeek : 'dddd [pukul] LT',
                lastDay : '[Kemarin pukul] LT',
                lastWeek : 'dddd [lalu pukul] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "dalam %s",
                past : "%s yang lalu",
                s : "beberapa detik",
                m : "semenit",
                mm : "%d menit",
                h : "sejam",
                hh : "%d jam",
                d : "sehari",
                dd : "%d hari",
                M : "sebulan",
                MM : "%d bulan",
                y : "setahun",
                yy : "%d tahun"
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : icelandic (is)
// author : Hinrik Ã–rn SigurÃ°sson : https://github.com/hinrik

    (function (factory) {
        factory(moment);
    }(function (moment) {
        function plural(n) {
            if (n % 100 === 11) {
                return true;
            } else if (n % 10 === 1) {
                return false;
            }
            return true;
        }

        function translate(number, withoutSuffix, key, isFuture) {
            var result = number + " ";
            switch (key) {
                case 's':
                    return withoutSuffix || isFuture ? 'nokkrar sekÃºndur' : 'nokkrum sekÃºndum';
                case 'm':
                    return withoutSuffix ? 'mÃ­nÃºta' : 'mÃ­nÃºtu';
                case 'mm':
                    if (plural(number)) {
                        return result + (withoutSuffix || isFuture ? 'mÃ­nÃºtur' : 'mÃ­nÃºtum');
                    } else if (withoutSuffix) {
                        return result + 'mÃ­nÃºta';
                    }
                    return result + 'mÃ­nÃºtu';
                case 'hh':
                    if (plural(number)) {
                        return result + (withoutSuffix || isFuture ? 'klukkustundir' : 'klukkustundum');
                    }
                    return result + 'klukkustund';
                case 'd':
                    if (withoutSuffix) {
                        return 'dagur';
                    }
                    return isFuture ? 'dag' : 'degi';
                case 'dd':
                    if (plural(number)) {
                        if (withoutSuffix) {
                            return result + 'dagar';
                        }
                        return result + (isFuture ? 'daga' : 'dÃ¶gum');
                    } else if (withoutSuffix) {
                        return result + 'dagur';
                    }
                    return result + (isFuture ? 'dag' : 'degi');
                case 'M':
                    if (withoutSuffix) {
                        return 'mÃ¡nuÃ°ur';
                    }
                    return isFuture ? 'mÃ¡nuÃ°' : 'mÃ¡nuÃ°i';
                case 'MM':
                    if (plural(number)) {
                        if (withoutSuffix) {
                            return result + 'mÃ¡nuÃ°ir';
                        }
                        return result + (isFuture ? 'mÃ¡nuÃ°i' : 'mÃ¡nuÃ°um');
                    } else if (withoutSuffix) {
                        return result + 'mÃ¡nuÃ°ur';
                    }
                    return result + (isFuture ? 'mÃ¡nuÃ°' : 'mÃ¡nuÃ°i');
                case 'y':
                    return withoutSuffix || isFuture ? 'Ã¡r' : 'Ã¡ri';
                case 'yy':
                    if (plural(number)) {
                        return result + (withoutSuffix || isFuture ? 'Ã¡r' : 'Ã¡rum');
                    }
                    return result + (withoutSuffix || isFuture ? 'Ã¡r' : 'Ã¡ri');
            }
        }

        return moment.lang('is', {
            months : "janÃºar_febrÃºar_mars_aprÃ­l_maÃ­_jÃºnÃ­_jÃºlÃ­_Ã¡gÃºst_september_oktÃ³ber_nÃ³vember_desember".split("_"),
            monthsShort : "jan_feb_mar_apr_maÃ­_jÃºn_jÃºl_Ã¡gÃº_sep_okt_nÃ³v_des".split("_"),
            weekdays : "sunnudagur_mÃ¡nudagur_Ã¾riÃ°judagur_miÃ°vikudagur_fimmtudagur_fÃ¶studagur_laugardagur".split("_"),
            weekdaysShort : "sun_mÃ¡n_Ã¾ri_miÃ°_fim_fÃ¶s_lau".split("_"),
            weekdaysMin : "Su_MÃ¡_Ãžr_Mi_Fi_FÃ¶_La".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "DD/MM/YYYY",
                LL : "D. MMMM YYYY",
                LLL : "D. MMMM YYYY [kl.] LT",
                LLLL : "dddd, D. MMMM YYYY [kl.] LT"
            },
            calendar : {
                sameDay : '[Ã­ dag kl.] LT',
                nextDay : '[Ã¡ morgun kl.] LT',
                nextWeek : 'dddd [kl.] LT',
                lastDay : '[Ã­ gÃ¦r kl.] LT',
                lastWeek : '[sÃ­Ã°asta] dddd [kl.] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "eftir %s",
                past : "fyrir %s sÃ­Ã°an",
                s : translate,
                m : translate,
                mm : translate,
                h : "klukkustund",
                hh : translate,
                d : translate,
                dd : translate,
                M : translate,
                MM : translate,
                y : translate,
                yy : translate
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : italian (it)
// author : Lorenzo : https://github.com/aliem
// author: Mattia Larentis: https://github.com/nostalgiaz

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('it', {
            months : "Gennaio_Febbraio_Marzo_Aprile_Maggio_Giugno_Luglio_Agosto_Settembre_Ottobre_Novembre_Dicembre".split("_"),
            monthsShort : "Gen_Feb_Mar_Apr_Mag_Giu_Lug_Ago_Set_Ott_Nov_Dic".split("_"),
            weekdays : "Domenica_LunedÃ¬_MartedÃ¬_MercoledÃ¬_GiovedÃ¬_VenerdÃ¬_Sabato".split("_"),
            weekdaysShort : "Dom_Lun_Mar_Mer_Gio_Ven_Sab".split("_"),
            weekdaysMin : "D_L_Ma_Me_G_V_S".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd, D MMMM YYYY LT"
            },
            calendar : {
                sameDay: '[Oggi alle] LT',
                nextDay: '[Domani alle] LT',
                nextWeek: 'dddd [alle] LT',
                lastDay: '[Ieri alle] LT',
                lastWeek: '[lo scorso] dddd [alle] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : function (s) {
                    return ((/^[0-9].+$/).test(s) ? "tra" : "in") + " " + s;
                },
                past : "%s fa",
                s : "secondi",
                m : "un minuto",
                mm : "%d minuti",
                h : "un'ora",
                hh : "%d ore",
                d : "un giorno",
                dd : "%d giorni",
                M : "un mese",
                MM : "%d mesi",
                y : "un anno",
                yy : "%d anni"
            },
            ordinal: '%dÂº',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : japanese (ja)
// author : LI Long : https://github.com/baryon

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('ja', {
            months : "1æœˆ_2æœˆ_3æœˆ_4æœˆ_5æœˆ_6æœˆ_7æœˆ_8æœˆ_9æœˆ_10æœˆ_11æœˆ_12æœˆ".split("_"),
            monthsShort : "1æœˆ_2æœˆ_3æœˆ_4æœˆ_5æœˆ_6æœˆ_7æœˆ_8æœˆ_9æœˆ_10æœˆ_11æœˆ_12æœˆ".split("_"),
            weekdays : "æ—¥æ›œæ—¥_æœˆæ›œæ—¥_ç«æ›œæ—¥_æ°´æ›œæ—¥_æœ¨æ›œæ—¥_é‡‘æ›œæ—¥_åœŸæ›œæ—¥".split("_"),
            weekdaysShort : "æ—¥_æœˆ_ç«_æ°´_æœ¨_é‡‘_åœŸ".split("_"),
            weekdaysMin : "æ—¥_æœˆ_ç«_æ°´_æœ¨_é‡‘_åœŸ".split("_"),
            longDateFormat : {
                LT : "Ahæ™‚måˆ†",
                L : "YYYY/MM/DD",
                LL : "YYYYå¹´MæœˆDæ—¥",
                LLL : "YYYYå¹´MæœˆDæ—¥LT",
                LLLL : "YYYYå¹´MæœˆDæ—¥LT dddd"
            },
            meridiem : function (hour, minute, isLower) {
                if (hour < 12) {
                    return "åˆå‰";
                } else {
                    return "åˆå¾Œ";
                }
            },
            calendar : {
                sameDay : '[ä»Šæ—¥] LT',
                nextDay : '[æ˜Žæ—¥] LT',
                nextWeek : '[æ¥é€±]dddd LT',
                lastDay : '[æ˜¨æ—¥] LT',
                lastWeek : '[å‰é€±]dddd LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%så¾Œ",
                past : "%så‰",
                s : "æ•°ç§’",
                m : "1åˆ†",
                mm : "%dåˆ†",
                h : "1æ™‚é–“",
                hh : "%dæ™‚é–“",
                d : "1æ—¥",
                dd : "%dæ—¥",
                M : "1ãƒ¶æœˆ",
                MM : "%dãƒ¶æœˆ",
                y : "1å¹´",
                yy : "%då¹´"
            }
        });
    }));
// moment.js language configuration
// language : Georgian (ka)
// author : Irakli Janiashvili : https://github.com/irakli-janiashvili

    (function (factory) {
        factory(moment);
    }(function (moment) {

        function monthsCaseReplace(m, format) {
            var months = {
                    'nominative': 'áƒ˜áƒáƒœáƒ•áƒáƒ áƒ˜_áƒ—áƒ”áƒ‘áƒ”áƒ áƒ•áƒáƒšáƒ˜_áƒ›áƒáƒ áƒ¢áƒ˜_áƒáƒžáƒ áƒ˜áƒšáƒ˜_áƒ›áƒáƒ˜áƒ¡áƒ˜_áƒ˜áƒ•áƒœáƒ˜áƒ¡áƒ˜_áƒ˜áƒ•áƒšáƒ˜áƒ¡áƒ˜_áƒáƒ’áƒ•áƒ˜áƒ¡áƒ¢áƒ_áƒ¡áƒ”áƒ¥áƒ¢áƒ”áƒ›áƒ‘áƒ”áƒ áƒ˜_áƒáƒ¥áƒ¢áƒáƒ›áƒ‘áƒ”áƒ áƒ˜_áƒœáƒáƒ”áƒ›áƒ‘áƒ”áƒ áƒ˜_áƒ“áƒ”áƒ™áƒ”áƒ›áƒ‘áƒ”áƒ áƒ˜'.split('_'),
                    'accusative': 'áƒ˜áƒáƒœáƒ•áƒáƒ áƒ¡_áƒ—áƒ”áƒ‘áƒ”áƒ áƒ•áƒáƒšáƒ¡_áƒ›áƒáƒ áƒ¢áƒ¡_áƒáƒžáƒ áƒ˜áƒšáƒ˜áƒ¡_áƒ›áƒáƒ˜áƒ¡áƒ¡_áƒ˜áƒ•áƒœáƒ˜áƒ¡áƒ¡_áƒ˜áƒ•áƒšáƒ˜áƒ¡áƒ¡_áƒáƒ’áƒ•áƒ˜áƒ¡áƒ¢áƒ¡_áƒ¡áƒ”áƒ¥áƒ¢áƒ”áƒ›áƒ‘áƒ”áƒ áƒ¡_áƒáƒ¥áƒ¢áƒáƒ›áƒ‘áƒ”áƒ áƒ¡_áƒœáƒáƒ”áƒ›áƒ‘áƒ”áƒ áƒ¡_áƒ“áƒ”áƒ™áƒ”áƒ›áƒ‘áƒ”áƒ áƒ¡'.split('_')
                },

                nounCase = (/D[oD] *MMMM?/).test(format) ?
                    'accusative' :
                    'nominative';

            return months[nounCase][m.month()];
        }

        function weekdaysCaseReplace(m, format) {
            var weekdays = {
                    'nominative': 'áƒ™áƒ•áƒ˜áƒ áƒ_áƒáƒ áƒ¨áƒáƒ‘áƒáƒ—áƒ˜_áƒ¡áƒáƒ›áƒ¨áƒáƒ‘áƒáƒ—áƒ˜_áƒáƒ—áƒ®áƒ¨áƒáƒ‘áƒáƒ—áƒ˜_áƒ®áƒ£áƒ—áƒ¨áƒáƒ‘áƒáƒ—áƒ˜_áƒžáƒáƒ áƒáƒ¡áƒ™áƒ”áƒ•áƒ˜_áƒ¨áƒáƒ‘áƒáƒ—áƒ˜'.split('_'),
                    'accusative': 'áƒ™áƒ•áƒ˜áƒ áƒáƒ¡_áƒáƒ áƒ¨áƒáƒ‘áƒáƒ—áƒ¡_áƒ¡áƒáƒ›áƒ¨áƒáƒ‘áƒáƒ—áƒ¡_áƒáƒ—áƒ®áƒ¨áƒáƒ‘áƒáƒ—áƒ¡_áƒ®áƒ£áƒ—áƒ¨áƒáƒ‘áƒáƒ—áƒ¡_áƒžáƒáƒ áƒáƒ¡áƒ™áƒ”áƒ•áƒ¡_áƒ¨áƒáƒ‘áƒáƒ—áƒ¡'.split('_')
                },

                nounCase = (/(áƒ¬áƒ˜áƒœáƒ|áƒ¨áƒ”áƒ›áƒ“áƒ”áƒ’)/).test(format) ?
                    'accusative' :
                    'nominative';

            return weekdays[nounCase][m.day()];
        }

        return moment.lang('ka', {
            months : monthsCaseReplace,
            monthsShort : "áƒ˜áƒáƒœ_áƒ—áƒ”áƒ‘_áƒ›áƒáƒ _áƒáƒžáƒ _áƒ›áƒáƒ˜_áƒ˜áƒ•áƒœ_áƒ˜áƒ•áƒš_áƒáƒ’áƒ•_áƒ¡áƒ”áƒ¥_áƒáƒ¥áƒ¢_áƒœáƒáƒ”_áƒ“áƒ”áƒ™".split("_"),
            weekdays : weekdaysCaseReplace,
            weekdaysShort : "áƒ™áƒ•áƒ˜_áƒáƒ áƒ¨_áƒ¡áƒáƒ›_áƒáƒ—áƒ®_áƒ®áƒ£áƒ—_áƒžáƒáƒ _áƒ¨áƒáƒ‘".split("_"),
            weekdaysMin : "áƒ™áƒ•_áƒáƒ _áƒ¡áƒ_áƒáƒ—_áƒ®áƒ£_áƒžáƒ_áƒ¨áƒ".split("_"),
            longDateFormat : {
                LT : "h:mm A",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd, D MMMM YYYY LT"
            },
            calendar : {
                sameDay : '[áƒ“áƒ¦áƒ”áƒ¡] LT[-áƒ–áƒ”]',
                nextDay : '[áƒ®áƒ•áƒáƒš] LT[-áƒ–áƒ”]',
                lastDay : '[áƒ’áƒ£áƒ¨áƒ˜áƒœ] LT[-áƒ–áƒ”]',
                nextWeek : '[áƒ¨áƒ”áƒ›áƒ“áƒ”áƒ’] dddd LT[-áƒ–áƒ”]',
                lastWeek : '[áƒ¬áƒ˜áƒœáƒ] dddd LT-áƒ–áƒ”',
                sameElse : 'L'
            },
            relativeTime : {
                future : function (s) {
                    return (/(áƒ¬áƒáƒ›áƒ˜|áƒ¬áƒ£áƒ—áƒ˜|áƒ¡áƒáƒáƒ—áƒ˜|áƒ¬áƒ”áƒšáƒ˜)/).test(s) ?
                        s.replace(/áƒ˜$/, "áƒ¨áƒ˜") :
                        s + "áƒ¨áƒ˜";
                },
                past : function (s) {
                    if ((/(áƒ¬áƒáƒ›áƒ˜|áƒ¬áƒ£áƒ—áƒ˜|áƒ¡áƒáƒáƒ—áƒ˜|áƒ“áƒ¦áƒ”|áƒ—áƒ•áƒ”)/).test(s)) {
                        return s.replace(/(áƒ˜|áƒ”)$/, "áƒ˜áƒ¡ áƒ¬áƒ˜áƒœ");
                    }
                    if ((/áƒ¬áƒ”áƒšáƒ˜/).test(s)) {
                        return s.replace(/áƒ¬áƒ”áƒšáƒ˜$/, "áƒ¬áƒšáƒ˜áƒ¡ áƒ¬áƒ˜áƒœ");
                    }
                },
                s : "áƒ áƒáƒ›áƒ“áƒ”áƒœáƒ˜áƒ›áƒ” áƒ¬áƒáƒ›áƒ˜",
                m : "áƒ¬áƒ£áƒ—áƒ˜",
                mm : "%d áƒ¬áƒ£áƒ—áƒ˜",
                h : "áƒ¡áƒáƒáƒ—áƒ˜",
                hh : "%d áƒ¡áƒáƒáƒ—áƒ˜",
                d : "áƒ“áƒ¦áƒ”",
                dd : "%d áƒ“áƒ¦áƒ”",
                M : "áƒ—áƒ•áƒ”",
                MM : "%d áƒ—áƒ•áƒ”",
                y : "áƒ¬áƒ”áƒšáƒ˜",
                yy : "%d áƒ¬áƒ”áƒšáƒ˜"
            },
            ordinal : function (number) {
                if (number === 0) {
                    return number;
                }

                if (number === 1) {
                    return number + "-áƒšáƒ˜";
                }

                if ((number < 20) || (number <= 100 && (number % 20 === 0)) || (number % 100 === 0)) {
                    return "áƒ›áƒ”-" + number;
                }

                return number + "-áƒ”";
            },
            week : {
                dow : 1,
                doy : 7
            }
        });
    }));
// moment.js language configuration
// language : korean (ko)
// author : Kyungwook, Park : https://github.com/kyungw00k

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('ko', {
            months : "1ì›”_2ì›”_3ì›”_4ì›”_5ì›”_6ì›”_7ì›”_8ì›”_9ì›”_10ì›”_11ì›”_12ì›”".split("_"),
            monthsShort : "1ì›”_2ì›”_3ì›”_4ì›”_5ì›”_6ì›”_7ì›”_8ì›”_9ì›”_10ì›”_11ì›”_12ì›”".split("_"),
            weekdays : "ì¼ìš”ì¼_ì›”ìš”ì¼_í™”ìš”ì¼_ìˆ˜ìš”ì¼_ëª©ìš”ì¼_ê¸ˆìš”ì¼_í† ìš”ì¼".split("_"),
            weekdaysShort : "ì¼_ì›”_í™”_ìˆ˜_ëª©_ê¸ˆ_í† ".split("_"),
            weekdaysMin : "ì¼_ì›”_í™”_ìˆ˜_ëª©_ê¸ˆ_í† ".split("_"),
            longDateFormat : {
                LT : "A hì‹œ mmë¶„",
                L : "YYYY.MM.DD",
                LL : "YYYYë…„ MMMM Dì¼",
                LLL : "YYYYë…„ MMMM Dì¼ LT",
                LLLL : "YYYYë…„ MMMM Dì¼ dddd LT"
            },
            meridiem : function (hour, minute, isUpper) {
                return hour < 12 ? 'ì˜¤ì „' : 'ì˜¤í›„';
            },
            calendar : {
                sameDay : 'ì˜¤ëŠ˜ LT',
                nextDay : 'ë‚´ì¼ LT',
                nextWeek : 'dddd LT',
                lastDay : 'ì–´ì œ LT',
                lastWeek : 'ì§€ë‚œì£¼ dddd LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s í›„",
                past : "%s ì „",
                s : "ëª‡ì´ˆ",
                ss : "%dì´ˆ",
                m : "ì¼ë¶„",
                mm : "%dë¶„",
                h : "í•œì‹œê°„",
                hh : "%dì‹œê°„",
                d : "í•˜ë£¨",
                dd : "%dì¼",
                M : "í•œë‹¬",
                MM : "%dë‹¬",
                y : "ì¼ë…„",
                yy : "%dë…„"
            },
            ordinal : '%dì¼'
        });
    }));
// moment.js language configuration
// language : Lithuanian (lt)
// author : Mindaugas MozÅ«ras : https://github.com/mmozuras

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var units = {
                "m" : "minutÄ—_minutÄ—s_minutÄ™",
                "mm": "minutÄ—s_minuÄiÅ³_minutes",
                "h" : "valanda_valandos_valandÄ…",
                "hh": "valandos_valandÅ³_valandas",
                "d" : "diena_dienos_dienÄ…",
                "dd": "dienos_dienÅ³_dienas",
                "M" : "mÄ—nuo_mÄ—nesio_mÄ—nesÄ¯",
                "MM": "mÄ—nesiai_mÄ—nesiÅ³_mÄ—nesius",
                "y" : "metai_metÅ³_metus",
                "yy": "metai_metÅ³_metus"
            },
            weekDays = "pirmadienis_antradienis_treÄiadienis_ketvirtadienis_penktadienis_Å¡eÅ¡tadienis_sekmadienis".split("_");

        function translateSeconds(number, withoutSuffix, key, isFuture) {
            if (withoutSuffix) {
                return "kelios sekundÄ—s";
            } else {
                return isFuture ? "keliÅ³ sekundÅ¾iÅ³" : "kelias sekundes";
            }
        }

        function translateSingular(number, withoutSuffix, key, isFuture) {
            return withoutSuffix ? forms(key)[0] : (isFuture ? forms(key)[1] : forms(key)[2]);
        }

        function special(number) {
            return number % 10 === 0 || (number > 10 && number < 20);
        }

        function forms(key) {
            return units[key].split("_");
        }

        function translate(number, withoutSuffix, key, isFuture) {
            var result = number + " ";
            if (number === 1) {
                return result + translateSingular(number, withoutSuffix, key[0], isFuture);
            } else if (withoutSuffix) {
                return result + (special(number) ? forms(key)[1] : forms(key)[0]);
            } else {
                if (isFuture) {
                    return result + forms(key)[1];
                } else {
                    return result + (special(number) ? forms(key)[1] : forms(key)[2]);
                }
            }
        }

        function relativeWeekDay(moment, format) {
            var nominative = format.indexOf('dddd LT') === -1,
                weekDay = weekDays[moment.weekday()];

            return nominative ? weekDay : weekDay.substring(0, weekDay.length - 2) + "Ä¯";
        }

        return moment.lang("lt", {
            months : "sausio_vasario_kovo_balandÅ¾io_geguÅ¾Ä—s_birÅ¾Ä—lio_liepos_rugpjÅ«Äio_rugsÄ—jo_spalio_lapkriÄio_gruodÅ¾io".split("_"),
            monthsShort : "sau_vas_kov_bal_geg_bir_lie_rgp_rgs_spa_lap_grd".split("_"),
            weekdays : relativeWeekDay,
            weekdaysShort : "Sek_Pir_Ant_Tre_Ket_Pen_Å eÅ¡".split("_"),
            weekdaysMin : "S_P_A_T_K_Pn_Å ".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "YYYY-MM-DD",
                LL : "YYYY [m.] MMMM D [d.]",
                LLL : "YYYY [m.] MMMM D [d.], LT [val.]",
                LLLL : "YYYY [m.] MMMM D [d.], dddd, LT [val.]",
                l : "YYYY-MM-DD",
                ll : "YYYY [m.] MMMM D [d.]",
                lll : "YYYY [m.] MMMM D [d.], LT [val.]",
                llll : "YYYY [m.] MMMM D [d.], ddd, LT [val.]"
            },
            calendar : {
                sameDay : "[Å iandien] LT",
                nextDay : "[Rytoj] LT",
                nextWeek : "dddd LT",
                lastDay : "[Vakar] LT",
                lastWeek : "[PraÄ—jusÄ¯] dddd LT",
                sameElse : "L"
            },
            relativeTime : {
                future : "po %s",
                past : "prieÅ¡ %s",
                s : translateSeconds,
                m : translateSingular,
                mm : translate,
                h : translateSingular,
                hh : translate,
                d : translateSingular,
                dd : translate,
                M : translateSingular,
                MM : translate,
                y : translateSingular,
                yy : translate
            },
            ordinal : function (number) {
                return number + '-oji';
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : latvian (lv)
// author : Kristaps Karlsons : https://github.com/skakri

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var units = {
            'mm': 'minÅ«ti_minÅ«tes_minÅ«te_minÅ«tes',
            'hh': 'stundu_stundas_stunda_stundas',
            'dd': 'dienu_dienas_diena_dienas',
            'MM': 'mÄ“nesi_mÄ“neÅ¡us_mÄ“nesis_mÄ“neÅ¡i',
            'yy': 'gadu_gadus_gads_gadi'
        };

        function format(word, number, withoutSuffix) {
            var forms = word.split('_');
            if (withoutSuffix) {
                return number % 10 === 1 && number !== 11 ? forms[2] : forms[3];
            } else {
                return number % 10 === 1 && number !== 11 ? forms[0] : forms[1];
            }
        }

        function relativeTimeWithPlural(number, withoutSuffix, key) {
            return number + ' ' + format(units[key], number, withoutSuffix);
        }

        return moment.lang('lv', {
            months : "janvÄris_februÄris_marts_aprÄ«lis_maijs_jÅ«nijs_jÅ«lijs_augusts_septembris_oktobris_novembris_decembris".split("_"),
            monthsShort : "jan_feb_mar_apr_mai_jÅ«n_jÅ«l_aug_sep_okt_nov_dec".split("_"),
            weekdays : "svÄ“tdiena_pirmdiena_otrdiena_treÅ¡diena_ceturtdiena_piektdiena_sestdiena".split("_"),
            weekdaysShort : "Sv_P_O_T_C_Pk_S".split("_"),
            weekdaysMin : "Sv_P_O_T_C_Pk_S".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD.MM.YYYY",
                LL : "YYYY. [gada] D. MMMM",
                LLL : "YYYY. [gada] D. MMMM, LT",
                LLLL : "YYYY. [gada] D. MMMM, dddd, LT"
            },
            calendar : {
                sameDay : '[Å odien pulksten] LT',
                nextDay : '[RÄ«t pulksten] LT',
                nextWeek : 'dddd [pulksten] LT',
                lastDay : '[Vakar pulksten] LT',
                lastWeek : '[PagÄjuÅ¡Ä] dddd [pulksten] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s vÄ“lÄk",
                past : "%s agrÄk",
                s : "daÅ¾as sekundes",
                m : "minÅ«ti",
                mm : relativeTimeWithPlural,
                h : "stundu",
                hh : relativeTimeWithPlural,
                d : "dienu",
                dd : relativeTimeWithPlural,
                M : "mÄ“nesi",
                MM : relativeTimeWithPlural,
                y : "gadu",
                yy : relativeTimeWithPlural
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : malayalam (ml)
// author : Floyd Pink : https://github.com/floydpink

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('ml', {
            months : 'à´œà´¨àµà´µà´°à´¿_à´«àµ†à´¬àµà´°àµà´µà´°à´¿_à´®à´¾àµ¼à´šàµà´šàµ_à´à´ªàµà´°à´¿àµ½_à´®àµ‡à´¯àµ_à´œàµ‚àµº_à´œàµ‚à´²àµˆ_à´“à´—à´¸àµà´±àµà´±àµ_à´¸àµ†à´ªàµà´±àµà´±à´‚à´¬àµ¼_à´’à´•àµà´Ÿàµ‹à´¬àµ¼_à´¨à´µà´‚à´¬àµ¼_à´¡à´¿à´¸à´‚à´¬àµ¼'.split("_"),
            monthsShort : 'à´œà´¨àµ._à´«àµ†à´¬àµà´°àµ._à´®à´¾àµ¼._à´à´ªàµà´°à´¿._à´®àµ‡à´¯àµ_à´œàµ‚àµº_à´œàµ‚à´²àµˆ._à´“à´—._à´¸àµ†à´ªàµà´±àµà´±._à´’à´•àµà´Ÿàµ‹._à´¨à´µà´‚._à´¡à´¿à´¸à´‚.'.split("_"),
            weekdays : 'à´žà´¾à´¯à´±à´¾à´´àµà´š_à´¤à´¿à´™àµà´•à´³à´¾à´´àµà´š_à´šàµŠà´µàµà´µà´¾à´´àµà´š_à´¬àµà´§à´¨à´¾à´´àµà´š_à´µàµà´¯à´¾à´´à´¾à´´àµà´š_à´µàµ†à´³àµà´³à´¿à´¯à´¾à´´àµà´š_à´¶à´¨à´¿à´¯à´¾à´´àµà´š'.split("_"),
            weekdaysShort : 'à´žà´¾à´¯àµ¼_à´¤à´¿à´™àµà´•àµ¾_à´šàµŠà´µàµà´µ_à´¬àµà´§àµ»_à´µàµà´¯à´¾à´´à´‚_à´µàµ†à´³àµà´³à´¿_à´¶à´¨à´¿'.split("_"),
            weekdaysMin : 'à´žà´¾_à´¤à´¿_à´šàµŠ_à´¬àµ_à´µàµà´¯à´¾_à´µàµ†_à´¶'.split("_"),
            longDateFormat : {
                LT : "A h:mm -à´¨àµ",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY, LT",
                LLLL : "dddd, D MMMM YYYY, LT"
            },
            calendar : {
                sameDay : '[à´‡à´¨àµà´¨àµ] LT',
                nextDay : '[à´¨à´¾à´³àµ†] LT',
                nextWeek : 'dddd, LT',
                lastDay : '[à´‡à´¨àµà´¨à´²àµ†] LT',
                lastWeek : '[à´•à´´à´¿à´žàµà´ž] dddd, LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s à´•à´´à´¿à´žàµà´žàµ",
                past : "%s à´®àµàµ»à´ªàµ",
                s : "à´…àµ½à´ª à´¨à´¿à´®à´¿à´·à´™àµà´™àµ¾",
                m : "à´’à´°àµ à´®à´¿à´¨à´¿à´±àµà´±àµ",
                mm : "%d à´®à´¿à´¨à´¿à´±àµà´±àµ",
                h : "à´’à´°àµ à´®à´£à´¿à´•àµà´•àµ‚àµ¼",
                hh : "%d à´®à´£à´¿à´•àµà´•àµ‚àµ¼",
                d : "à´’à´°àµ à´¦à´¿à´µà´¸à´‚",
                dd : "%d à´¦à´¿à´µà´¸à´‚",
                M : "à´’à´°àµ à´®à´¾à´¸à´‚",
                MM : "%d à´®à´¾à´¸à´‚",
                y : "à´’à´°àµ à´µàµ¼à´·à´‚",
                yy : "%d à´µàµ¼à´·à´‚"
            },
            meridiem : function (hour, minute, isLower) {
                if (hour < 4) {
                    return "à´°à´¾à´¤àµà´°à´¿";
                } else if (hour < 12) {
                    return "à´°à´¾à´µà´¿à´²àµ†";
                } else if (hour < 17) {
                    return "à´‰à´šàµà´š à´•à´´à´¿à´žàµà´žàµ";
                } else if (hour < 20) {
                    return "à´µàµˆà´•àµà´¨àµà´¨àµ‡à´°à´‚";
                } else {
                    return "à´°à´¾à´¤àµà´°à´¿";
                }
            }
        });
    }));
// moment.js language configuration
// language : Marathi (mr)
// author : Harshad Kale : https://github.com/kalehv

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var symbolMap = {
                '1': 'à¥§',
                '2': 'à¥¨',
                '3': 'à¥©',
                '4': 'à¥ª',
                '5': 'à¥«',
                '6': 'à¥¬',
                '7': 'à¥­',
                '8': 'à¥®',
                '9': 'à¥¯',
                '0': 'à¥¦'
            },
            numberMap = {
                'à¥§': '1',
                'à¥¨': '2',
                'à¥©': '3',
                'à¥ª': '4',
                'à¥«': '5',
                'à¥¬': '6',
                'à¥­': '7',
                'à¥®': '8',
                'à¥¯': '9',
                'à¥¦': '0'
            };

        return moment.lang('mr', {
            months : 'à¤œà¤¾à¤¨à¥‡à¤µà¤¾à¤°à¥€_à¤«à¥‡à¤¬à¥à¤°à¥à¤µà¤¾à¤°à¥€_à¤®à¤¾à¤°à¥à¤š_à¤à¤ªà¥à¤°à¤¿à¤²_à¤®à¥‡_à¤œà¥‚à¤¨_à¤œà¥à¤²à¥ˆ_à¤‘à¤—à¤¸à¥à¤Ÿ_à¤¸à¤ªà¥à¤Ÿà¥‡à¤‚à¤¬à¤°_à¤‘à¤•à¥à¤Ÿà¥‹à¤¬à¤°_à¤¨à¥‹à¤µà¥à¤¹à¥‡à¤‚à¤¬à¤°_à¤¡à¤¿à¤¸à¥‡à¤‚à¤¬à¤°'.split("_"),
            monthsShort: 'à¤œà¤¾à¤¨à¥‡._à¤«à¥‡à¤¬à¥à¤°à¥._à¤®à¤¾à¤°à¥à¤š._à¤à¤ªà¥à¤°à¤¿._à¤®à¥‡._à¤œà¥‚à¤¨._à¤œà¥à¤²à¥ˆ._à¤‘à¤—._à¤¸à¤ªà¥à¤Ÿà¥‡à¤‚._à¤‘à¤•à¥à¤Ÿà¥‹._à¤¨à¥‹à¤µà¥à¤¹à¥‡à¤‚._à¤¡à¤¿à¤¸à¥‡à¤‚.'.split("_"),
            weekdays : 'à¤°à¤µà¤¿à¤µà¤¾à¤°_à¤¸à¥‹à¤®à¤µà¤¾à¤°_à¤®à¤‚à¤—à¤³à¤µà¤¾à¤°_à¤¬à¥à¤§à¤µà¤¾à¤°_à¤—à¥à¤°à¥‚à¤µà¤¾à¤°_à¤¶à¥à¤•à¥à¤°à¤µà¤¾à¤°_à¤¶à¤¨à¤¿à¤µà¤¾à¤°'.split("_"),
            weekdaysShort : 'à¤°à¤µà¤¿_à¤¸à¥‹à¤®_à¤®à¤‚à¤—à¤³_à¤¬à¥à¤§_à¤—à¥à¤°à¥‚_à¤¶à¥à¤•à¥à¤°_à¤¶à¤¨à¤¿'.split("_"),
            weekdaysMin : 'à¤°_à¤¸à¥‹_à¤®à¤‚_à¤¬à¥_à¤—à¥_à¤¶à¥_à¤¶'.split("_"),
            longDateFormat : {
                LT : "A h:mm à¤µà¤¾à¤œà¤¤à¤¾",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY, LT",
                LLLL : "dddd, D MMMM YYYY, LT"
            },
            calendar : {
                sameDay : '[à¤†à¤œ] LT',
                nextDay : '[à¤‰à¤¦à¥à¤¯à¤¾] LT',
                nextWeek : 'dddd, LT',
                lastDay : '[à¤•à¤¾à¤²] LT',
                lastWeek: '[à¤®à¤¾à¤—à¥€à¤²] dddd, LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s à¤¨à¤‚à¤¤à¤°",
                past : "%s à¤ªà¥‚à¤°à¥à¤µà¥€",
                s : "à¤¸à¥‡à¤•à¤‚à¤¦",
                m: "à¤à¤• à¤®à¤¿à¤¨à¤¿à¤Ÿ",
                mm: "%d à¤®à¤¿à¤¨à¤¿à¤Ÿà¥‡",
                h : "à¤à¤• à¤¤à¤¾à¤¸",
                hh : "%d à¤¤à¤¾à¤¸",
                d : "à¤à¤• à¤¦à¤¿à¤µà¤¸",
                dd : "%d à¤¦à¤¿à¤µà¤¸",
                M : "à¤à¤• à¤®à¤¹à¤¿à¤¨à¤¾",
                MM : "%d à¤®à¤¹à¤¿à¤¨à¥‡",
                y : "à¤à¤• à¤µà¤°à¥à¤·",
                yy : "%d à¤µà¤°à¥à¤·à¥‡"
            },
            preparse: function (string) {
                return string.replace(/[à¥§à¥¨à¥©à¥ªà¥«à¥¬à¥­à¥®à¥¯à¥¦]/g, function (match) {
                    return numberMap[match];
                });
            },
            postformat: function (string) {
                return string.replace(/\d/g, function (match) {
                    return symbolMap[match];
                });
            },
            meridiem: function (hour, minute, isLower)
            {
                if (hour < 4) {
                    return "à¤°à¤¾à¤¤à¥à¤°à¥€";
                } else if (hour < 10) {
                    return "à¤¸à¤•à¤¾à¤³à¥€";
                } else if (hour < 17) {
                    return "à¤¦à¥à¤ªà¤¾à¤°à¥€";
                } else if (hour < 20) {
                    return "à¤¸à¤¾à¤¯à¤‚à¤•à¤¾à¤³à¥€";
                } else {
                    return "à¤°à¤¾à¤¤à¥à¤°à¥€";
                }
            },
            week : {
                dow : 0, // Sunday is the first day of the week.
                doy : 6  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : Bahasa Malaysia (ms-MY)
// author : Weldan Jamili : https://github.com/weldan

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('ms-my', {
            months : "Januari_Februari_Mac_April_Mei_Jun_Julai_Ogos_September_Oktober_November_Disember".split("_"),
            monthsShort : "Jan_Feb_Mac_Apr_Mei_Jun_Jul_Ogs_Sep_Okt_Nov_Dis".split("_"),
            weekdays : "Ahad_Isnin_Selasa_Rabu_Khamis_Jumaat_Sabtu".split("_"),
            weekdaysShort : "Ahd_Isn_Sel_Rab_Kha_Jum_Sab".split("_"),
            weekdaysMin : "Ah_Is_Sl_Rb_Km_Jm_Sb".split("_"),
            longDateFormat : {
                LT : "HH.mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY [pukul] LT",
                LLLL : "dddd, D MMMM YYYY [pukul] LT"
            },
            meridiem : function (hours, minutes, isLower) {
                if (hours < 11) {
                    return 'pagi';
                } else if (hours < 15) {
                    return 'tengahari';
                } else if (hours < 19) {
                    return 'petang';
                } else {
                    return 'malam';
                }
            },
            calendar : {
                sameDay : '[Hari ini pukul] LT',
                nextDay : '[Esok pukul] LT',
                nextWeek : 'dddd [pukul] LT',
                lastDay : '[Kelmarin pukul] LT',
                lastWeek : 'dddd [lepas pukul] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "dalam %s",
                past : "%s yang lepas",
                s : "beberapa saat",
                m : "seminit",
                mm : "%d minit",
                h : "sejam",
                hh : "%d jam",
                d : "sehari",
                dd : "%d hari",
                M : "sebulan",
                MM : "%d bulan",
                y : "setahun",
                yy : "%d tahun"
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : norwegian bokmÃ¥l (nb)
// authors : Espen Hovlandsdal : https://github.com/rexxars
//           Sigurd Gartmann : https://github.com/sigurdga

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('nb', {
            months : "januar_februar_mars_april_mai_juni_juli_august_september_oktober_november_desember".split("_"),
            monthsShort : "jan._feb._mars_april_mai_juni_juli_aug._sep._okt._nov._des.".split("_"),
            weekdays : "sÃ¸ndag_mandag_tirsdag_onsdag_torsdag_fredag_lÃ¸rdag".split("_"),
            weekdaysShort : "sÃ¸._ma._ti._on._to._fr._lÃ¸.".split("_"),
            weekdaysMin : "sÃ¸_ma_ti_on_to_fr_lÃ¸".split("_"),
            longDateFormat : {
                LT : "H.mm",
                L : "DD.MM.YYYY",
                LL : "D. MMMM YYYY",
                LLL : "D. MMMM YYYY [kl.] LT",
                LLLL : "dddd D. MMMM YYYY [kl.] LT"
            },
            calendar : {
                sameDay: '[i dag kl.] LT',
                nextDay: '[i morgen kl.] LT',
                nextWeek: 'dddd [kl.] LT',
                lastDay: '[i gÃ¥r kl.] LT',
                lastWeek: '[forrige] dddd [kl.] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "om %s",
                past : "for %s siden",
                s : "noen sekunder",
                m : "ett minutt",
                mm : "%d minutter",
                h : "en time",
                hh : "%d timer",
                d : "en dag",
                dd : "%d dager",
                M : "en mÃ¥ned",
                MM : "%d mÃ¥neder",
                y : "ett Ã¥r",
                yy : "%d Ã¥r"
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : nepali/nepalese
// author : suvash : https://github.com/suvash

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var symbolMap = {
                '1': 'à¥§',
                '2': 'à¥¨',
                '3': 'à¥©',
                '4': 'à¥ª',
                '5': 'à¥«',
                '6': 'à¥¬',
                '7': 'à¥­',
                '8': 'à¥®',
                '9': 'à¥¯',
                '0': 'à¥¦'
            },
            numberMap = {
                'à¥§': '1',
                'à¥¨': '2',
                'à¥©': '3',
                'à¥ª': '4',
                'à¥«': '5',
                'à¥¬': '6',
                'à¥­': '7',
                'à¥®': '8',
                'à¥¯': '9',
                'à¥¦': '0'
            };

        return moment.lang('ne', {
            months : 'à¤œà¤¨à¤µà¤°à¥€_à¤«à¥‡à¤¬à¥à¤°à¥à¤µà¤°à¥€_à¤®à¤¾à¤°à¥à¤š_à¤…à¤ªà¥à¤°à¤¿à¤²_à¤®à¤ˆ_à¤œà¥à¤¨_à¤œà¥à¤²à¤¾à¤ˆ_à¤…à¤—à¤·à¥à¤Ÿ_à¤¸à¥‡à¤ªà¥à¤Ÿà¥‡à¤®à¥à¤¬à¤°_à¤…à¤•à¥à¤Ÿà¥‹à¤¬à¤°_à¤¨à¥‹à¤­à¥‡à¤®à¥à¤¬à¤°_à¤¡à¤¿à¤¸à¥‡à¤®à¥à¤¬à¤°'.split("_"),
            monthsShort : 'à¤œà¤¨._à¤«à¥‡à¤¬à¥à¤°à¥._à¤®à¤¾à¤°à¥à¤š_à¤…à¤ªà¥à¤°à¤¿._à¤®à¤ˆ_à¤œà¥à¤¨_à¤œà¥à¤²à¤¾à¤ˆ._à¤…à¤—._à¤¸à¥‡à¤ªà¥à¤Ÿ._à¤…à¤•à¥à¤Ÿà¥‹._à¤¨à¥‹à¤­à¥‡._à¤¡à¤¿à¤¸à¥‡.'.split("_"),
            weekdays : 'à¤†à¤‡à¤¤à¤¬à¤¾à¤°_à¤¸à¥‹à¤®à¤¬à¤¾à¤°_à¤®à¤™à¥à¤—à¤²à¤¬à¤¾à¤°_à¤¬à¥à¤§à¤¬à¤¾à¤°_à¤¬à¤¿à¤¹à¤¿à¤¬à¤¾à¤°_à¤¶à¥à¤•à¥à¤°à¤¬à¤¾à¤°_à¤¶à¤¨à¤¿à¤¬à¤¾à¤°'.split("_"),
            weekdaysShort : 'à¤†à¤‡à¤¤._à¤¸à¥‹à¤®._à¤®à¤™à¥à¤—à¤²._à¤¬à¥à¤§._à¤¬à¤¿à¤¹à¤¿._à¤¶à¥à¤•à¥à¤°._à¤¶à¤¨à¤¿.'.split("_"),
            weekdaysMin : 'à¤†à¤‡._à¤¸à¥‹._à¤®à¤™à¥_à¤¬à¥._à¤¬à¤¿._à¤¶à¥._à¤¶.'.split("_"),
            longDateFormat : {
                LT : "Aà¤•à¥‹ h:mm à¤¬à¤œà¥‡",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY, LT",
                LLLL : "dddd, D MMMM YYYY, LT"
            },
            preparse: function (string) {
                return string.replace(/[à¥§à¥¨à¥©à¥ªà¥«à¥¬à¥­à¥®à¥¯à¥¦]/g, function (match) {
                    return numberMap[match];
                });
            },
            postformat: function (string) {
                return string.replace(/\d/g, function (match) {
                    return symbolMap[match];
                });
            },
            meridiem : function (hour, minute, isLower) {
                if (hour < 3) {
                    return "à¤°à¤¾à¤¤à¥€";
                } else if (hour < 10) {
                    return "à¤¬à¤¿à¤¹à¤¾à¤¨";
                } else if (hour < 15) {
                    return "à¤¦à¤¿à¤‰à¤à¤¸à¥‹";
                } else if (hour < 18) {
                    return "à¤¬à¥‡à¤²à¥à¤•à¤¾";
                } else if (hour < 20) {
                    return "à¤¸à¤¾à¤à¤";
                } else {
                    return "à¤°à¤¾à¤¤à¥€";
                }
            },
            calendar : {
                sameDay : '[à¤†à¤œ] LT',
                nextDay : '[à¤­à¥‹à¤²à¥€] LT',
                nextWeek : '[à¤†à¤‰à¤à¤¦à¥‹] dddd[,] LT',
                lastDay : '[à¤¹à¤¿à¤œà¥‹] LT',
                lastWeek : '[à¤—à¤à¤•à¥‹] dddd[,] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%sà¤®à¤¾",
                past : "%s à¤…à¤—à¤¾à¤¡à¥€",
                s : "à¤•à¥‡à¤¹à¥€ à¤¸à¤®à¤¯",
                m : "à¤à¤• à¤®à¤¿à¤¨à¥‡à¤Ÿ",
                mm : "%d à¤®à¤¿à¤¨à¥‡à¤Ÿ",
                h : "à¤à¤• à¤˜à¤£à¥à¤Ÿà¤¾",
                hh : "%d à¤˜à¤£à¥à¤Ÿà¤¾",
                d : "à¤à¤• à¤¦à¤¿à¤¨",
                dd : "%d à¤¦à¤¿à¤¨",
                M : "à¤à¤• à¤®à¤¹à¤¿à¤¨à¤¾",
                MM : "%d à¤®à¤¹à¤¿à¤¨à¤¾",
                y : "à¤à¤• à¤¬à¤°à¥à¤·",
                yy : "%d à¤¬à¤°à¥à¤·"
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : dutch (nl)
// author : Joris RÃ¶ling : https://github.com/jjupiter

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var monthsShortWithDots = "jan._feb._mrt._apr._mei_jun._jul._aug._sep._okt._nov._dec.".split("_"),
            monthsShortWithoutDots = "jan_feb_mrt_apr_mei_jun_jul_aug_sep_okt_nov_dec".split("_");

        return moment.lang('nl', {
            months : "januari_februari_maart_april_mei_juni_juli_augustus_september_oktober_november_december".split("_"),
            monthsShort : function (m, format) {
                if (/-MMM-/.test(format)) {
                    return monthsShortWithoutDots[m.month()];
                } else {
                    return monthsShortWithDots[m.month()];
                }
            },
            weekdays : "zondag_maandag_dinsdag_woensdag_donderdag_vrijdag_zaterdag".split("_"),
            weekdaysShort : "zo._ma._di._wo._do._vr._za.".split("_"),
            weekdaysMin : "Zo_Ma_Di_Wo_Do_Vr_Za".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD-MM-YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: '[vandaag om] LT',
                nextDay: '[morgen om] LT',
                nextWeek: 'dddd [om] LT',
                lastDay: '[gisteren om] LT',
                lastWeek: '[afgelopen] dddd [om] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "over %s",
                past : "%s geleden",
                s : "een paar seconden",
                m : "Ã©Ã©n minuut",
                mm : "%d minuten",
                h : "Ã©Ã©n uur",
                hh : "%d uur",
                d : "Ã©Ã©n dag",
                dd : "%d dagen",
                M : "Ã©Ã©n maand",
                MM : "%d maanden",
                y : "Ã©Ã©n jaar",
                yy : "%d jaar"
            },
            ordinal : function (number) {
                return number + ((number === 1 || number === 8 || number >= 20) ? 'ste' : 'de');
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : norwegian nynorsk (nn)
// author : https://github.com/mechuwind

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('nn', {
            months : "januar_februar_mars_april_mai_juni_juli_august_september_oktober_november_desember".split("_"),
            monthsShort : "jan_feb_mar_apr_mai_jun_jul_aug_sep_okt_nov_des".split("_"),
            weekdays : "sundag_mÃ¥ndag_tysdag_onsdag_torsdag_fredag_laurdag".split("_"),
            weekdaysShort : "sun_mÃ¥n_tys_ons_tor_fre_lau".split("_"),
            weekdaysMin : "su_mÃ¥_ty_on_to_fr_lÃ¸".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD.MM.YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: '[I dag klokka] LT',
                nextDay: '[I morgon klokka] LT',
                nextWeek: 'dddd [klokka] LT',
                lastDay: '[I gÃ¥r klokka] LT',
                lastWeek: '[FÃ¸regÃ¥ende] dddd [klokka] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "om %s",
                past : "for %s siden",
                s : "noen sekund",
                m : "ett minutt",
                mm : "%d minutt",
                h : "en time",
                hh : "%d timar",
                d : "en dag",
                dd : "%d dagar",
                M : "en mÃ¥nad",
                MM : "%d mÃ¥nader",
                y : "ett Ã¥r",
                yy : "%d Ã¥r"
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : polish (pl)
// author : Rafal Hirsz : https://github.com/evoL

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var monthsNominative = "styczeÅ„_luty_marzec_kwiecieÅ„_maj_czerwiec_lipiec_sierpieÅ„_wrzesieÅ„_paÅºdziernik_listopad_grudzieÅ„".split("_"),
            monthsSubjective = "stycznia_lutego_marca_kwietnia_maja_czerwca_lipca_sierpnia_wrzeÅ›nia_paÅºdziernika_listopada_grudnia".split("_");

        function plural(n) {
            return (n % 10 < 5) && (n % 10 > 1) && (~~(n / 10) !== 1);
        }

        function translate(number, withoutSuffix, key) {
            var result = number + " ";
            switch (key) {
                case 'm':
                    return withoutSuffix ? 'minuta' : 'minutÄ™';
                case 'mm':
                    return result + (plural(number) ? 'minuty' : 'minut');
                case 'h':
                    return withoutSuffix  ? 'godzina'  : 'godzinÄ™';
                case 'hh':
                    return result + (plural(number) ? 'godziny' : 'godzin');
                case 'MM':
                    return result + (plural(number) ? 'miesiÄ…ce' : 'miesiÄ™cy');
                case 'yy':
                    return result + (plural(number) ? 'lata' : 'lat');
            }
        }

        return moment.lang('pl', {
            months : function (momentToFormat, format) {
                if (/D MMMM/.test(format)) {
                    return monthsSubjective[momentToFormat.month()];
                } else {
                    return monthsNominative[momentToFormat.month()];
                }
            },
            monthsShort : "sty_lut_mar_kwi_maj_cze_lip_sie_wrz_paÅº_lis_gru".split("_"),
            weekdays : "niedziela_poniedziaÅ‚ek_wtorek_Å›roda_czwartek_piÄ…tek_sobota".split("_"),
            weekdaysShort : "nie_pon_wt_Å›r_czw_pt_sb".split("_"),
            weekdaysMin : "N_Pn_Wt_Åšr_Cz_Pt_So".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD.MM.YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd, D MMMM YYYY LT"
            },
            calendar : {
                sameDay: '[DziÅ› o] LT',
                nextDay: '[Jutro o] LT',
                nextWeek: '[W] dddd [o] LT',
                lastDay: '[Wczoraj o] LT',
                lastWeek: function () {
                    switch (this.day()) {
                        case 0:
                            return '[W zeszÅ‚Ä… niedzielÄ™ o] LT';
                        case 3:
                            return '[W zeszÅ‚Ä… Å›rodÄ™ o] LT';
                        case 6:
                            return '[W zeszÅ‚Ä… sobotÄ™ o] LT';
                        default:
                            return '[W zeszÅ‚y] dddd [o] LT';
                    }
                },
                sameElse: 'L'
            },
            relativeTime : {
                future : "za %s",
                past : "%s temu",
                s : "kilka sekund",
                m : translate,
                mm : translate,
                h : translate,
                hh : translate,
                d : "1 dzieÅ„",
                dd : '%d dni',
                M : "miesiÄ…c",
                MM : translate,
                y : "rok",
                yy : translate
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : brazilian portuguese (pt-br)
// author : Caio Ribeiro Pereira : https://github.com/caio-ribeiro-pereira

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('pt-br', {
            months : "Janeiro_Fevereiro_MarÃ§o_Abril_Maio_Junho_Julho_Agosto_Setembro_Outubro_Novembro_Dezembro".split("_"),
            monthsShort : "Jan_Fev_Mar_Abr_Mai_Jun_Jul_Ago_Set_Out_Nov_Dez".split("_"),
            weekdays : "Domingo_Segunda-feira_TerÃ§a-feira_Quarta-feira_Quinta-feira_Sexta-feira_SÃ¡bado".split("_"),
            weekdaysShort : "Dom_Seg_Ter_Qua_Qui_Sex_SÃ¡b".split("_"),
            weekdaysMin : "Dom_2Âª_3Âª_4Âª_5Âª_6Âª_SÃ¡b".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D [de] MMMM [de] YYYY",
                LLL : "D [de] MMMM [de] YYYY LT",
                LLLL : "dddd, D [de] MMMM [de] YYYY LT"
            },
            calendar : {
                sameDay: '[Hoje Ã s] LT',
                nextDay: '[AmanhÃ£ Ã s] LT',
                nextWeek: 'dddd [Ã s] LT',
                lastDay: '[Ontem Ã s] LT',
                lastWeek: function () {
                    return (this.day() === 0 || this.day() === 6) ?
                        '[Ãšltimo] dddd [Ã s] LT' : // Saturday + Sunday
                        '[Ãšltima] dddd [Ã s] LT'; // Monday - Friday
                },
                sameElse: 'L'
            },
            relativeTime : {
                future : "em %s",
                past : "%s atrÃ¡s",
                s : "segundos",
                m : "um minuto",
                mm : "%d minutos",
                h : "uma hora",
                hh : "%d horas",
                d : "um dia",
                dd : "%d dias",
                M : "um mÃªs",
                MM : "%d meses",
                y : "um ano",
                yy : "%d anos"
            },
            ordinal : '%dÂº'
        });
    }));
// moment.js language configuration
// language : portuguese (pt)
// author : Jefferson : https://github.com/jalex79

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('pt', {
            months : "Janeiro_Fevereiro_MarÃ§o_Abril_Maio_Junho_Julho_Agosto_Setembro_Outubro_Novembro_Dezembro".split("_"),
            monthsShort : "Jan_Fev_Mar_Abr_Mai_Jun_Jul_Ago_Set_Out_Nov_Dez".split("_"),
            weekdays : "Domingo_Segunda-feira_TerÃ§a-feira_Quarta-feira_Quinta-feira_Sexta-feira_SÃ¡bado".split("_"),
            weekdaysShort : "Dom_Seg_Ter_Qua_Qui_Sex_SÃ¡b".split("_"),
            weekdaysMin : "Dom_2Âª_3Âª_4Âª_5Âª_6Âª_SÃ¡b".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D [de] MMMM [de] YYYY",
                LLL : "D [de] MMMM [de] YYYY LT",
                LLLL : "dddd, D [de] MMMM [de] YYYY LT"
            },
            calendar : {
                sameDay: '[Hoje Ã s] LT',
                nextDay: '[AmanhÃ£ Ã s] LT',
                nextWeek: 'dddd [Ã s] LT',
                lastDay: '[Ontem Ã s] LT',
                lastWeek: function () {
                    return (this.day() === 0 || this.day() === 6) ?
                        '[Ãšltimo] dddd [Ã s] LT' : // Saturday + Sunday
                        '[Ãšltima] dddd [Ã s] LT'; // Monday - Friday
                },
                sameElse: 'L'
            },
            relativeTime : {
                future : "em %s",
                past : "%s atrÃ¡s",
                s : "segundos",
                m : "um minuto",
                mm : "%d minutos",
                h : "uma hora",
                hh : "%d horas",
                d : "um dia",
                dd : "%d dias",
                M : "um mÃªs",
                MM : "%d meses",
                y : "um ano",
                yy : "%d anos"
            },
            ordinal : '%dÂº',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : romanian (ro)
// author : Vlad Gurdiga : https://github.com/gurdiga
// author : Valentin Agachi : https://github.com/avaly

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('ro', {
            months : "Ianuarie_Februarie_Martie_Aprilie_Mai_Iunie_Iulie_August_Septembrie_Octombrie_Noiembrie_Decembrie".split("_"),
            monthsShort : "Ian_Feb_Mar_Apr_Mai_Iun_Iul_Aug_Sep_Oct_Noi_Dec".split("_"),
            weekdays : "DuminicÄƒ_Luni_MarÅ£i_Miercuri_Joi_Vineri_SÃ¢mbÄƒtÄƒ".split("_"),
            weekdaysShort : "Dum_Lun_Mar_Mie_Joi_Vin_SÃ¢m".split("_"),
            weekdaysMin : "Du_Lu_Ma_Mi_Jo_Vi_SÃ¢".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY H:mm",
                LLLL : "dddd, D MMMM YYYY H:mm"
            },
            calendar : {
                sameDay: "[azi la] LT",
                nextDay: '[mÃ¢ine la] LT',
                nextWeek: 'dddd [la] LT',
                lastDay: '[ieri la] LT',
                lastWeek: '[fosta] dddd [la] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "peste %s",
                past : "%s Ã®n urmÄƒ",
                s : "cÃ¢teva secunde",
                m : "un minut",
                mm : "%d minute",
                h : "o orÄƒ",
                hh : "%d ore",
                d : "o zi",
                dd : "%d zile",
                M : "o lunÄƒ",
                MM : "%d luni",
                y : "un an",
                yy : "%d ani"
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : russian (ru)
// author : Viktorminator : https://github.com/Viktorminator
// Author : Menelion ElensÃºle : https://github.com/Oire

    (function (factory) {
        factory(moment);
    }(function (moment) {
        function plural(word, num) {
            var forms = word.split('_');
            return num % 10 === 1 && num % 100 !== 11 ? forms[0] : (num % 10 >= 2 && num % 10 <= 4 && (num % 100 < 10 || num % 100 >= 20) ? forms[1] : forms[2]);
        }

        function relativeTimeWithPlural(number, withoutSuffix, key) {
            var format = {
                'mm': 'Ð¼Ð¸Ð½ÑƒÑ‚Ð°_Ð¼Ð¸Ð½ÑƒÑ‚Ñ‹_Ð¼Ð¸Ð½ÑƒÑ‚',
                'hh': 'Ñ‡Ð°Ñ_Ñ‡Ð°ÑÐ°_Ñ‡Ð°ÑÐ¾Ð²',
                'dd': 'Ð´ÐµÐ½ÑŒ_Ð´Ð½Ñ_Ð´Ð½ÐµÐ¹',
                'MM': 'Ð¼ÐµÑÑÑ†_Ð¼ÐµÑÑÑ†Ð°_Ð¼ÐµÑÑÑ†ÐµÐ²',
                'yy': 'Ð³Ð¾Ð´_Ð³Ð¾Ð´Ð°_Ð»ÐµÑ‚'
            };
            if (key === 'm') {
                return withoutSuffix ? 'Ð¼Ð¸Ð½ÑƒÑ‚Ð°' : 'Ð¼Ð¸Ð½ÑƒÑ‚Ñƒ';
            }
            else {
                return number + ' ' + plural(format[key], +number);
            }
        }

        function monthsCaseReplace(m, format) {
            var months = {
                    'nominative': 'ÑÐ½Ð²Ð°Ñ€ÑŒ_Ñ„ÐµÐ²Ñ€Ð°Ð»ÑŒ_Ð¼Ð°Ñ€Ñ‚_Ð°Ð¿Ñ€ÐµÐ»ÑŒ_Ð¼Ð°Ð¹_Ð¸ÑŽÐ½ÑŒ_Ð¸ÑŽÐ»ÑŒ_Ð°Ð²Ð³ÑƒÑÑ‚_ÑÐµÐ½Ñ‚ÑÐ±Ñ€ÑŒ_Ð¾ÐºÑ‚ÑÐ±Ñ€ÑŒ_Ð½Ð¾ÑÐ±Ñ€ÑŒ_Ð´ÐµÐºÐ°Ð±Ñ€ÑŒ'.split('_'),
                    'accusative': 'ÑÐ½Ð²Ð°Ñ€Ñ_Ñ„ÐµÐ²Ñ€Ð°Ð»Ñ_Ð¼Ð°Ñ€Ñ‚Ð°_Ð°Ð¿Ñ€ÐµÐ»Ñ_Ð¼Ð°Ñ_Ð¸ÑŽÐ½Ñ_Ð¸ÑŽÐ»Ñ_Ð°Ð²Ð³ÑƒÑÑ‚Ð°_ÑÐµÐ½Ñ‚ÑÐ±Ñ€Ñ_Ð¾ÐºÑ‚ÑÐ±Ñ€Ñ_Ð½Ð¾ÑÐ±Ñ€Ñ_Ð´ÐµÐºÐ°Ð±Ñ€Ñ'.split('_')
                },

                nounCase = (/D[oD]?(\[[^\[\]]*\]|\s+)+MMMM?/).test(format) ?
                    'accusative' :
                    'nominative';

            return months[nounCase][m.month()];
        }

        function monthsShortCaseReplace(m, format) {
            var monthsShort = {
                    'nominative': 'ÑÐ½Ð²_Ñ„ÐµÐ²_Ð¼Ð°Ñ€_Ð°Ð¿Ñ€_Ð¼Ð°Ð¹_Ð¸ÑŽÐ½ÑŒ_Ð¸ÑŽÐ»ÑŒ_Ð°Ð²Ð³_ÑÐµÐ½_Ð¾ÐºÑ‚_Ð½Ð¾Ñ_Ð´ÐµÐº'.split('_'),
                    'accusative': 'ÑÐ½Ð²_Ñ„ÐµÐ²_Ð¼Ð°Ñ€_Ð°Ð¿Ñ€_Ð¼Ð°Ñ_Ð¸ÑŽÐ½Ñ_Ð¸ÑŽÐ»Ñ_Ð°Ð²Ð³_ÑÐµÐ½_Ð¾ÐºÑ‚_Ð½Ð¾Ñ_Ð´ÐµÐº'.split('_')
                },

                nounCase = (/D[oD]?(\[[^\[\]]*\]|\s+)+MMMM?/).test(format) ?
                    'accusative' :
                    'nominative';

            return monthsShort[nounCase][m.month()];
        }

        function weekdaysCaseReplace(m, format) {
            var weekdays = {
                    'nominative': 'Ð²Ð¾ÑÐºÑ€ÐµÑÐµÐ½ÑŒÐµ_Ð¿Ð¾Ð½ÐµÐ´ÐµÐ»ÑŒÐ½Ð¸Ðº_Ð²Ñ‚Ð¾Ñ€Ð½Ð¸Ðº_ÑÑ€ÐµÐ´Ð°_Ñ‡ÐµÑ‚Ð²ÐµÑ€Ð³_Ð¿ÑÑ‚Ð½Ð¸Ñ†Ð°_ÑÑƒÐ±Ð±Ð¾Ñ‚Ð°'.split('_'),
                    'accusative': 'Ð²Ð¾ÑÐºÑ€ÐµÑÐµÐ½ÑŒÐµ_Ð¿Ð¾Ð½ÐµÐ´ÐµÐ»ÑŒÐ½Ð¸Ðº_Ð²Ñ‚Ð¾Ñ€Ð½Ð¸Ðº_ÑÑ€ÐµÐ´Ñƒ_Ñ‡ÐµÑ‚Ð²ÐµÑ€Ð³_Ð¿ÑÑ‚Ð½Ð¸Ñ†Ñƒ_ÑÑƒÐ±Ð±Ð¾Ñ‚Ñƒ'.split('_')
                },

                nounCase = (/\[ ?[Ð’Ð²] ?(?:Ð¿Ñ€Ð¾ÑˆÐ»ÑƒÑŽ|ÑÐ»ÐµÐ´ÑƒÑŽÑ‰ÑƒÑŽ)? ?\] ?dddd/).test(format) ?
                    'accusative' :
                    'nominative';

            return weekdays[nounCase][m.day()];
        }

        return moment.lang('ru', {
            months : monthsCaseReplace,
            monthsShort : monthsShortCaseReplace,
            weekdays : weekdaysCaseReplace,
            weekdaysShort : "Ð²Ñ_Ð¿Ð½_Ð²Ñ‚_ÑÑ€_Ñ‡Ñ‚_Ð¿Ñ‚_ÑÐ±".split("_"),
            weekdaysMin : "Ð²Ñ_Ð¿Ð½_Ð²Ñ‚_ÑÑ€_Ñ‡Ñ‚_Ð¿Ñ‚_ÑÐ±".split("_"),
            monthsParse : [/^ÑÐ½Ð²/i, /^Ñ„ÐµÐ²/i, /^Ð¼Ð°Ñ€/i, /^Ð°Ð¿Ñ€/i, /^Ð¼Ð°[Ð¹|Ñ]/i, /^Ð¸ÑŽÐ½/i, /^Ð¸ÑŽÐ»/i, /^Ð°Ð²Ð³/i, /^ÑÐµÐ½/i, /^Ð¾ÐºÑ‚/i, /^Ð½Ð¾Ñ/i, /^Ð´ÐµÐº/i],
            longDateFormat : {
                LT : "HH:mm",
                L : "DD.MM.YYYY",
                LL : "D MMMM YYYY Ð³.",
                LLL : "D MMMM YYYY Ð³., LT",
                LLLL : "dddd, D MMMM YYYY Ð³., LT"
            },
            calendar : {
                sameDay: '[Ð¡ÐµÐ³Ð¾Ð´Ð½Ñ Ð²] LT',
                nextDay: '[Ð—Ð°Ð²Ñ‚Ñ€Ð° Ð²] LT',
                lastDay: '[Ð’Ñ‡ÐµÑ€Ð° Ð²] LT',
                nextWeek: function () {
                    return this.day() === 2 ? '[Ð’Ð¾] dddd [Ð²] LT' : '[Ð’] dddd [Ð²] LT';
                },
                lastWeek: function () {
                    switch (this.day()) {
                        case 0:
                            return '[Ð’ Ð¿Ñ€Ð¾ÑˆÐ»Ð¾Ðµ] dddd [Ð²] LT';
                        case 1:
                        case 2:
                        case 4:
                            return '[Ð’ Ð¿Ñ€Ð¾ÑˆÐ»Ñ‹Ð¹] dddd [Ð²] LT';
                        case 3:
                        case 5:
                        case 6:
                            return '[Ð’ Ð¿Ñ€Ð¾ÑˆÐ»ÑƒÑŽ] dddd [Ð²] LT';
                    }
                },
                sameElse: 'L'
            },
            relativeTime : {
                future : "Ñ‡ÐµÑ€ÐµÐ· %s",
                past : "%s Ð½Ð°Ð·Ð°Ð´",
                s : "Ð½ÐµÑÐºÐ¾Ð»ÑŒÐºÐ¾ ÑÐµÐºÑƒÐ½Ð´",
                m : relativeTimeWithPlural,
                mm : relativeTimeWithPlural,
                h : "Ñ‡Ð°Ñ",
                hh : relativeTimeWithPlural,
                d : "Ð´ÐµÐ½ÑŒ",
                dd : relativeTimeWithPlural,
                M : "Ð¼ÐµÑÑÑ†",
                MM : relativeTimeWithPlural,
                y : "Ð³Ð¾Ð´",
                yy : relativeTimeWithPlural
            },

            // M. E.: those two are virtually unused but a user might want to implement them for his/her website for some reason

            meridiem : function (hour, minute, isLower) {
                if (hour < 4) {
                    return "Ð½Ð¾Ñ‡Ð¸";
                } else if (hour < 12) {
                    return "ÑƒÑ‚Ñ€Ð°";
                } else if (hour < 17) {
                    return "Ð´Ð½Ñ";
                } else {
                    return "Ð²ÐµÑ‡ÐµÑ€Ð°";
                }
            },

            ordinal: function (number, period) {
                switch (period) {
                    case 'M':
                    case 'd':
                    case 'DDD':
                        return number + '-Ð¹';
                    case 'D':
                        return number + '-Ð³Ð¾';
                    case 'w':
                    case 'W':
                        return number + '-Ñ';
                    default:
                        return number;
                }
            },

            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : slovak (sk)
// author : Martin Minka : https://github.com/k2s
// based on work of petrbela : https://github.com/petrbela

    (function (factory) {
        factory(moment);
    }(function (moment) {
        var months = "januÃ¡r_februÃ¡r_marec_aprÃ­l_mÃ¡j_jÃºn_jÃºl_august_september_oktÃ³ber_november_december".split("_"),
            monthsShort = "jan_feb_mar_apr_mÃ¡j_jÃºn_jÃºl_aug_sep_okt_nov_dec".split("_");

        function plural(n) {
            return (n > 1) && (n < 5);
        }

        function translate(number, withoutSuffix, key, isFuture) {
            var result = number + " ";
            switch (key) {
                case 's':  // a few seconds / in a few seconds / a few seconds ago
                    return (withoutSuffix || isFuture) ? 'pÃ¡r sekÃºnd' : 'pÃ¡r sekundami';
                case 'm':  // a minute / in a minute / a minute ago
                    return withoutSuffix ? 'minÃºta' : (isFuture ? 'minÃºtu' : 'minÃºtou');
                case 'mm': // 9 minutes / in 9 minutes / 9 minutes ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'minÃºty' : 'minÃºt');
                    } else {
                        return result + 'minÃºtami';
                    }
                    break;
                case 'h':  // an hour / in an hour / an hour ago
                    return withoutSuffix ? 'hodina' : (isFuture ? 'hodinu' : 'hodinou');
                case 'hh': // 9 hours / in 9 hours / 9 hours ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'hodiny' : 'hodÃ­n');
                    } else {
                        return result + 'hodinami';
                    }
                    break;
                case 'd':  // a day / in a day / a day ago
                    return (withoutSuffix || isFuture) ? 'deÅˆ' : 'dÅˆom';
                case 'dd': // 9 days / in 9 days / 9 days ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'dni' : 'dnÃ­');
                    } else {
                        return result + 'dÅˆami';
                    }
                    break;
                case 'M':  // a month / in a month / a month ago
                    return (withoutSuffix || isFuture) ? 'mesiac' : 'mesiacom';
                case 'MM': // 9 months / in 9 months / 9 months ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'mesiace' : 'mesiacov');
                    } else {
                        return result + 'mesiacmi';
                    }
                    break;
                case 'y':  // a year / in a year / a year ago
                    return (withoutSuffix || isFuture) ? 'rok' : 'rokom';
                case 'yy': // 9 years / in 9 years / 9 years ago
                    if (withoutSuffix || isFuture) {
                        return result + (plural(number) ? 'roky' : 'rokov');
                    } else {
                        return result + 'rokmi';
                    }
                    break;
            }
        }

        return moment.lang('sk', {
            months : months,
            monthsShort : monthsShort,
            monthsParse : (function (months, monthsShort) {
                var i, _monthsParse = [];
                for (i = 0; i < 12; i++) {
                    // use custom parser to solve problem with July (Äervenec)
                    _monthsParse[i] = new RegExp('^' + months[i] + '$|^' + monthsShort[i] + '$', 'i');
                }
                return _monthsParse;
            }(months, monthsShort)),
            weekdays : "nedeÄ¾a_pondelok_utorok_streda_Å¡tvrtok_piatok_sobota".split("_"),
            weekdaysShort : "ne_po_ut_st_Å¡t_pi_so".split("_"),
            weekdaysMin : "ne_po_ut_st_Å¡t_pi_so".split("_"),
            longDateFormat : {
                LT: "H:mm",
                L : "DD.MM.YYYY",
                LL : "D. MMMM YYYY",
                LLL : "D. MMMM YYYY LT",
                LLLL : "dddd D. MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[dnes o] LT",
                nextDay: '[zajtra o] LT',
                nextWeek: function () {
                    switch (this.day()) {
                        case 0:
                            return '[v nedeÄ¾u o] LT';
                        case 1:
                        case 2:
                            return '[v] dddd [o] LT';
                        case 3:
                            return '[v stredu o] LT';
                        case 4:
                            return '[vo Å¡tvrtok o] LT';
                        case 5:
                            return '[v piatok o] LT';
                        case 6:
                            return '[v sobotu o] LT';
                    }
                },
                lastDay: '[vÄera o] LT',
                lastWeek: function () {
                    switch (this.day()) {
                        case 0:
                            return '[minulÃº nedeÄ¾u o] LT';
                        case 1:
                        case 2:
                            return '[minulÃ½] dddd [o] LT';
                        case 3:
                            return '[minulÃº stredu o] LT';
                        case 4:
                        case 5:
                            return '[minulÃ½] dddd [o] LT';
                        case 6:
                            return '[minulÃº sobotu o] LT';
                    }
                },
                sameElse: "L"
            },
            relativeTime : {
                future : "za %s",
                past : "pred %s",
                s : translate,
                m : translate,
                mm : translate,
                h : translate,
                hh : translate,
                d : translate,
                dd : translate,
                M : translate,
                MM : translate,
                y : translate,
                yy : translate
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : slovenian (sl)
// author : Robert SedovÅ¡ek : https://github.com/sedovsek

    (function (factory) {
        factory(moment);
    }(function (moment) {
        function translate(number, withoutSuffix, key) {
            var result = number + " ";
            switch (key) {
                case 'm':
                    return withoutSuffix ? 'ena minuta' : 'eno minuto';
                case 'mm':
                    if (number === 1) {
                        result += 'minuta';
                    } else if (number === 2) {
                        result += 'minuti';
                    } else if (number === 3 || number === 4) {
                        result += 'minute';
                    } else {
                        result += 'minut';
                    }
                    return result;
                case 'h':
                    return withoutSuffix ? 'ena ura' : 'eno uro';
                case 'hh':
                    if (number === 1) {
                        result += 'ura';
                    } else if (number === 2) {
                        result += 'uri';
                    } else if (number === 3 || number === 4) {
                        result += 'ure';
                    } else {
                        result += 'ur';
                    }
                    return result;
                case 'dd':
                    if (number === 1) {
                        result += 'dan';
                    } else {
                        result += 'dni';
                    }
                    return result;
                case 'MM':
                    if (number === 1) {
                        result += 'mesec';
                    } else if (number === 2) {
                        result += 'meseca';
                    } else if (number === 3 || number === 4) {
                        result += 'mesece';
                    } else {
                        result += 'mesecev';
                    }
                    return result;
                case 'yy':
                    if (number === 1) {
                        result += 'leto';
                    } else if (number === 2) {
                        result += 'leti';
                    } else if (number === 3 || number === 4) {
                        result += 'leta';
                    } else {
                        result += 'let';
                    }
                    return result;
            }
        }

        return moment.lang('sl', {
            months : "januar_februar_marec_april_maj_junij_julij_avgust_september_oktober_november_december".split("_"),
            monthsShort : "jan._feb._mar._apr._maj._jun._jul._avg._sep._okt._nov._dec.".split("_"),
            weekdays : "nedelja_ponedeljek_torek_sreda_Äetrtek_petek_sobota".split("_"),
            weekdaysShort : "ned._pon._tor._sre._Äet._pet._sob.".split("_"),
            weekdaysMin : "ne_po_to_sr_Äe_pe_so".split("_"),
            longDateFormat : {
                LT : "H:mm",
                L : "DD. MM. YYYY",
                LL : "D. MMMM YYYY",
                LLL : "D. MMMM YYYY LT",
                LLLL : "dddd, D. MMMM YYYY LT"
            },
            calendar : {
                sameDay  : '[danes ob] LT',
                nextDay  : '[jutri ob] LT',

                nextWeek : function () {
                    switch (this.day()) {
                        case 0:
                            return '[v] [nedeljo] [ob] LT';
                        case 3:
                            return '[v] [sredo] [ob] LT';
                        case 6:
                            return '[v] [soboto] [ob] LT';
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                            return '[v] dddd [ob] LT';
                    }
                },
                lastDay  : '[vÄeraj ob] LT',
                lastWeek : function () {
                    switch (this.day()) {
                        case 0:
                        case 3:
                        case 6:
                            return '[prejÅ¡nja] dddd [ob] LT';
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                            return '[prejÅ¡nji] dddd [ob] LT';
                    }
                },
                sameElse : 'L'
            },
            relativeTime : {
                future : "Äez %s",
                past   : "%s nazaj",
                s      : "nekaj sekund",
                m      : translate,
                mm     : translate,
                h      : translate,
                hh     : translate,
                d      : "en dan",
                dd     : translate,
                M      : "en mesec",
                MM     : translate,
                y      : "eno leto",
                yy     : translate
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : Albanian (sq)
// author : FlakÃ«rim Ismani : https://github.com/flakerimi
// author: Menelion ElensÃºle: https://github.com/Oire (tests)

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('sq', {
            months : "Janar_Shkurt_Mars_Prill_Maj_Qershor_Korrik_Gusht_Shtator_Tetor_NÃ«ntor_Dhjetor".split("_"),
            monthsShort : "Jan_Shk_Mar_Pri_Maj_Qer_Kor_Gus_Sht_Tet_NÃ«n_Dhj".split("_"),
            weekdays : "E Diel_E HÃ«nÃ«_E Marte_E MÃ«rkure_E Enjte_E Premte_E ShtunÃ«".split("_"),
            weekdaysShort : "Die_HÃ«n_Mar_MÃ«r_Enj_Pre_Sht".split("_"),
            weekdaysMin : "D_H_Ma_MÃ«_E_P_Sh".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd, D MMMM YYYY LT"
            },
            calendar : {
                sameDay : '[Sot nÃ«] LT',
                nextDay : '[Neser nÃ«] LT',
                nextWeek : 'dddd [nÃ«] LT',
                lastDay : '[Dje nÃ«] LT',
                lastWeek : 'dddd [e kaluar nÃ«] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "nÃ« %s",
                past : "%s me parÃ«",
                s : "disa seconda",
                m : "njÃ« minut",
                mm : "%d minutea",
                h : "njÃ« orÃ«",
                hh : "%d orÃ«",
                d : "njÃ« ditÃ«",
                dd : "%d ditÃ«",
                M : "njÃ« muaj",
                MM : "%d muaj",
                y : "njÃ« vit",
                yy : "%d vite"
            },
            ordinal : '%d.',
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : swedish (sv)
// author : Jens Alm : https://github.com/ulmus

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('sv', {
            months : "januari_februari_mars_april_maj_juni_juli_augusti_september_oktober_november_december".split("_"),
            monthsShort : "jan_feb_mar_apr_maj_jun_jul_aug_sep_okt_nov_dec".split("_"),
            weekdays : "sÃ¶ndag_mÃ¥ndag_tisdag_onsdag_torsdag_fredag_lÃ¶rdag".split("_"),
            weekdaysShort : "sÃ¶n_mÃ¥n_tis_ons_tor_fre_lÃ¶r".split("_"),
            weekdaysMin : "sÃ¶_mÃ¥_ti_on_to_fr_lÃ¶".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "YYYY-MM-DD",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: '[Idag] LT',
                nextDay: '[Imorgon] LT',
                lastDay: '[IgÃ¥r] LT',
                nextWeek: 'dddd LT',
                lastWeek: '[FÃ¶rra] dddd[en] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "om %s",
                past : "fÃ¶r %s sedan",
                s : "nÃ¥gra sekunder",
                m : "en minut",
                mm : "%d minuter",
                h : "en timme",
                hh : "%d timmar",
                d : "en dag",
                dd : "%d dagar",
                M : "en mÃ¥nad",
                MM : "%d mÃ¥nader",
                y : "ett Ã¥r",
                yy : "%d Ã¥r"
            },
            ordinal : function (number) {
                var b = number % 10,
                    output = (~~ (number % 100 / 10) === 1) ? 'e' :
                        (b === 1) ? 'a' :
                            (b === 2) ? 'a' :
                                (b === 3) ? 'e' : 'e';
                return number + output;
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : thai (th)
// author : Kridsada Thanabulpong : https://github.com/sirn

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('th', {
            months : "à¸¡à¸à¸£à¸²à¸„à¸¡_à¸à¸¸à¸¡à¸ à¸²à¸žà¸±à¸™à¸˜à¹Œ_à¸¡à¸µà¸™à¸²à¸„à¸¡_à¹€à¸¡à¸©à¸²à¸¢à¸™_à¸žà¸¤à¸©à¸ à¸²à¸„à¸¡_à¸¡à¸´à¸–à¸¸à¸™à¸²à¸¢à¸™_à¸à¸£à¸à¸Žà¸²à¸„à¸¡_à¸ªà¸´à¸‡à¸«à¸²à¸„à¸¡_à¸à¸±à¸™à¸¢à¸²à¸¢à¸™_à¸•à¸¸à¸¥à¸²à¸„à¸¡_à¸žà¸¤à¸¨à¸ˆà¸´à¸à¸²à¸¢à¸™_à¸˜à¸±à¸™à¸§à¸²à¸„à¸¡".split("_"),
            monthsShort : "à¸¡à¸à¸£à¸²_à¸à¸¸à¸¡à¸ à¸²_à¸¡à¸µà¸™à¸²_à¹€à¸¡à¸©à¸²_à¸žà¸¤à¸©à¸ à¸²_à¸¡à¸´à¸–à¸¸à¸™à¸²_à¸à¸£à¸à¸Žà¸²_à¸ªà¸´à¸‡à¸«à¸²_à¸à¸±à¸™à¸¢à¸²_à¸•à¸¸à¸¥à¸²_à¸žà¸¤à¸¨à¸ˆà¸´à¸à¸²_à¸˜à¸±à¸™à¸§à¸²".split("_"),
            weekdays : "à¸­à¸²à¸—à¸´à¸•à¸¢à¹Œ_à¸ˆà¸±à¸™à¸—à¸£à¹Œ_à¸­à¸±à¸‡à¸„à¸²à¸£_à¸žà¸¸à¸˜_à¸žà¸¤à¸«à¸±à¸ªà¸šà¸”à¸µ_à¸¨à¸¸à¸à¸£à¹Œ_à¹€à¸ªà¸²à¸£à¹Œ".split("_"),
            weekdaysShort : "à¸­à¸²à¸—à¸´à¸•à¸¢à¹Œ_à¸ˆà¸±à¸™à¸—à¸£à¹Œ_à¸­à¸±à¸‡à¸„à¸²à¸£_à¸žà¸¸à¸˜_à¸žà¸¤à¸«à¸±à¸ª_à¸¨à¸¸à¸à¸£à¹Œ_à¹€à¸ªà¸²à¸£à¹Œ".split("_"), // yes, three characters difference
            weekdaysMin : "à¸­à¸²._à¸ˆ._à¸­._à¸ž._à¸žà¸¤._à¸¨._à¸ª.".split("_"),
            longDateFormat : {
                LT : "H à¸™à¸²à¸¬à¸´à¸à¸² m à¸™à¸²à¸—à¸µ",
                L : "YYYY/MM/DD",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY à¹€à¸§à¸¥à¸² LT",
                LLLL : "à¸§à¸±à¸™ddddà¸—à¸µà¹ˆ D MMMM YYYY à¹€à¸§à¸¥à¸² LT"
            },
            meridiem : function (hour, minute, isLower) {
                if (hour < 12) {
                    return "à¸à¹ˆà¸­à¸™à¹€à¸—à¸µà¹ˆà¸¢à¸‡";
                } else {
                    return "à¸«à¸¥à¸±à¸‡à¹€à¸—à¸µà¹ˆà¸¢à¸‡";
                }
            },
            calendar : {
                sameDay : '[à¸§à¸±à¸™à¸™à¸µà¹‰ à¹€à¸§à¸¥à¸²] LT',
                nextDay : '[à¸žà¸£à¸¸à¹ˆà¸‡à¸™à¸µà¹‰ à¹€à¸§à¸¥à¸²] LT',
                nextWeek : 'dddd[à¸«à¸™à¹‰à¸² à¹€à¸§à¸¥à¸²] LT',
                lastDay : '[à¹€à¸¡à¸·à¹ˆà¸­à¸§à¸²à¸™à¸™à¸µà¹‰ à¹€à¸§à¸¥à¸²] LT',
                lastWeek : '[à¸§à¸±à¸™]dddd[à¸—à¸µà¹ˆà¹à¸¥à¹‰à¸§ à¹€à¸§à¸¥à¸²] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "à¸­à¸µà¸ %s",
                past : "%sà¸—à¸µà¹ˆà¹à¸¥à¹‰à¸§",
                s : "à¹„à¸¡à¹ˆà¸à¸µà¹ˆà¸§à¸´à¸™à¸²à¸—à¸µ",
                m : "1 à¸™à¸²à¸—à¸µ",
                mm : "%d à¸™à¸²à¸—à¸µ",
                h : "1 à¸Šà¸±à¹ˆà¸§à¹‚à¸¡à¸‡",
                hh : "%d à¸Šà¸±à¹ˆà¸§à¹‚à¸¡à¸‡",
                d : "1 à¸§à¸±à¸™",
                dd : "%d à¸§à¸±à¸™",
                M : "1 à¹€à¸”à¸·à¸­à¸™",
                MM : "%d à¹€à¸”à¸·à¸­à¸™",
                y : "1 à¸›à¸µ",
                yy : "%d à¸›à¸µ"
            }
        });
    }));
// moment.js language configuration
// language : Tagalog/Filipino (tl-ph)
// author : Dan Hagman

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('tl-ph', {
            months : "Enero_Pebrero_Marso_Abril_Mayo_Hunyo_Hulyo_Agosto_Setyembre_Oktubre_Nobyembre_Disyembre".split("_"),
            monthsShort : "Ene_Peb_Mar_Abr_May_Hun_Hul_Ago_Set_Okt_Nob_Dis".split("_"),
            weekdays : "Linggo_Lunes_Martes_Miyerkules_Huwebes_Biyernes_Sabado".split("_"),
            weekdaysShort : "Lin_Lun_Mar_Miy_Huw_Biy_Sab".split("_"),
            weekdaysMin : "Li_Lu_Ma_Mi_Hu_Bi_Sab".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "MM/D/YYYY",
                LL : "MMMM D, YYYY",
                LLL : "MMMM D, YYYY LT",
                LLLL : "dddd, MMMM DD, YYYY LT"
            },
            calendar : {
                sameDay: "[Ngayon sa] LT",
                nextDay: '[Bukas sa] LT',
                nextWeek: 'dddd [sa] LT',
                lastDay: '[Kahapon sa] LT',
                lastWeek: 'dddd [huling linggo] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "sa loob ng %s",
                past : "%s ang nakalipas",
                s : "ilang segundo",
                m : "isang minuto",
                mm : "%d minuto",
                h : "isang oras",
                hh : "%d oras",
                d : "isang araw",
                dd : "%d araw",
                M : "isang buwan",
                MM : "%d buwan",
                y : "isang taon",
                yy : "%d taon"
            },
            ordinal : function (number) {
                return number;
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : turkish (tr)
// authors : Erhan Gundogan : https://github.com/erhangundogan,
//           Burak YiÄŸit Kaya: https://github.com/BYK

    (function (factory) {
        factory(moment);
    }(function (moment) {

        var suffixes = {
            1: "'inci",
            5: "'inci",
            8: "'inci",
            70: "'inci",
            80: "'inci",

            2: "'nci",
            7: "'nci",
            20: "'nci",
            50: "'nci",

            3: "'Ã¼ncÃ¼",
            4: "'Ã¼ncÃ¼",
            100: "'Ã¼ncÃ¼",

            6: "'ncÄ±",

            9: "'uncu",
            10: "'uncu",
            30: "'uncu",

            60: "'Ä±ncÄ±",
            90: "'Ä±ncÄ±"
        };

        return moment.lang('tr', {
            months : "Ocak_Åžubat_Mart_Nisan_MayÄ±s_Haziran_Temmuz_AÄŸustos_EylÃ¼l_Ekim_KasÄ±m_AralÄ±k".split("_"),
            monthsShort : "Oca_Åžub_Mar_Nis_May_Haz_Tem_AÄŸu_Eyl_Eki_Kas_Ara".split("_"),
            weekdays : "Pazar_Pazartesi_SalÄ±_Ã‡arÅŸamba_PerÅŸembe_Cuma_Cumartesi".split("_"),
            weekdaysShort : "Paz_Pts_Sal_Ã‡ar_Per_Cum_Cts".split("_"),
            weekdaysMin : "Pz_Pt_Sa_Ã‡a_Pe_Cu_Ct".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD.MM.YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd, D MMMM YYYY LT"
            },
            calendar : {
                sameDay : '[bugÃ¼n saat] LT',
                nextDay : '[yarÄ±n saat] LT',
                nextWeek : '[haftaya] dddd [saat] LT',
                lastDay : '[dÃ¼n] LT',
                lastWeek : '[geÃ§en hafta] dddd [saat] LT',
                sameElse : 'L'
            },
            relativeTime : {
                future : "%s sonra",
                past : "%s Ã¶nce",
                s : "birkaÃ§ saniye",
                m : "bir dakika",
                mm : "%d dakika",
                h : "bir saat",
                hh : "%d saat",
                d : "bir gÃ¼n",
                dd : "%d gÃ¼n",
                M : "bir ay",
                MM : "%d ay",
                y : "bir yÄ±l",
                yy : "%d yÄ±l"
            },
            ordinal : function (number) {
                if (number === 0) {  // special case for zero
                    return number + "'Ä±ncÄ±";
                }
                var a = number % 10,
                    b = number % 100 - a,
                    c = number >= 100 ? 100 : null;

                return number + (suffixes[a] || suffixes[b] || suffixes[c]);
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : Morocco Central Atlas TamaziÉ£t in Latin (tzm-la)
// author : Abdel Said : https://github.com/abdelsaid

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('tzm-la', {
            months : "innayr_brË¤ayrË¤_marË¤sË¤_ibrir_mayyw_ywnyw_ywlywz_É£wÅ¡t_Å¡wtanbir_ktË¤wbrË¤_nwwanbir_dwjnbir".split("_"),
            monthsShort : "innayr_brË¤ayrË¤_marË¤sË¤_ibrir_mayyw_ywnyw_ywlywz_É£wÅ¡t_Å¡wtanbir_ktË¤wbrË¤_nwwanbir_dwjnbir".split("_"),
            weekdays : "asamas_aynas_asinas_akras_akwas_asimwas_asiá¸yas".split("_"),
            weekdaysShort : "asamas_aynas_asinas_akras_akwas_asimwas_asiá¸yas".split("_"),
            weekdaysMin : "asamas_aynas_asinas_akras_akwas_asimwas_asiá¸yas".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[asdkh g] LT",
                nextDay: '[aska g] LT',
                nextWeek: 'dddd [g] LT',
                lastDay: '[assant g] LT',
                lastWeek: 'dddd [g] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "dadkh s yan %s",
                past : "yan %s",
                s : "imik",
                m : "minuá¸",
                mm : "%d minuá¸",
                h : "saÉ›a",
                hh : "%d tassaÉ›in",
                d : "ass",
                dd : "%d ossan",
                M : "ayowr",
                MM : "%d iyyirn",
                y : "asgas",
                yy : "%d isgasn"
            },
            week : {
                dow : 6, // Saturday is the first day of the week.
                doy : 12  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : Morocco Central Atlas TamaziÉ£t (tzm)
// author : Abdel Said : https://github.com/abdelsaid

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('tzm', {
            months : "âµ‰âµâµâ´°âµ¢âµ”_â´±âµ•â´°âµ¢âµ•_âµŽâ´°âµ•âµš_âµ‰â´±âµ”âµ‰âµ”_âµŽâ´°âµ¢âµ¢âµ“_âµ¢âµ“âµâµ¢âµ“_âµ¢âµ“âµâµ¢âµ“âµ£_âµ–âµ“âµ›âµœ_âµ›âµ“âµœâ´°âµâ´±âµ‰âµ”_â´½âµŸâµ“â´±âµ•_âµâµ“âµ¡â´°âµâ´±âµ‰âµ”_â´·âµ“âµŠâµâ´±âµ‰âµ”".split("_"),
            monthsShort : "âµ‰âµâµâ´°âµ¢âµ”_â´±âµ•â´°âµ¢âµ•_âµŽâ´°âµ•âµš_âµ‰â´±âµ”âµ‰âµ”_âµŽâ´°âµ¢âµ¢âµ“_âµ¢âµ“âµâµ¢âµ“_âµ¢âµ“âµâµ¢âµ“âµ£_âµ–âµ“âµ›âµœ_âµ›âµ“âµœâ´°âµâ´±âµ‰âµ”_â´½âµŸâµ“â´±âµ•_âµâµ“âµ¡â´°âµâ´±âµ‰âµ”_â´·âµ“âµŠâµâ´±âµ‰âµ”".split("_"),
            weekdays : "â´°âµ™â´°âµŽâ´°âµ™_â´°âµ¢âµâ´°âµ™_â´°âµ™âµ‰âµâ´°âµ™_â´°â´½âµ”â´°âµ™_â´°â´½âµ¡â´°âµ™_â´°âµ™âµ‰âµŽâµ¡â´°âµ™_â´°âµ™âµ‰â´¹âµ¢â´°âµ™".split("_"),
            weekdaysShort : "â´°âµ™â´°âµŽâ´°âµ™_â´°âµ¢âµâ´°âµ™_â´°âµ™âµ‰âµâ´°âµ™_â´°â´½âµ”â´°âµ™_â´°â´½âµ¡â´°âµ™_â´°âµ™âµ‰âµŽâµ¡â´°âµ™_â´°âµ™âµ‰â´¹âµ¢â´°âµ™".split("_"),
            weekdaysMin : "â´°âµ™â´°âµŽâ´°âµ™_â´°âµ¢âµâ´°âµ™_â´°âµ™âµ‰âµâ´°âµ™_â´°â´½âµ”â´°âµ™_â´°â´½âµ¡â´°âµ™_â´°âµ™âµ‰âµŽâµ¡â´°âµ™_â´°âµ™âµ‰â´¹âµ¢â´°âµ™".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[â´°âµ™â´·âµ… â´´] LT",
                nextDay: '[â´°âµ™â´½â´° â´´] LT',
                nextWeek: 'dddd [â´´] LT',
                lastDay: '[â´°âµšâ´°âµâµœ â´´] LT',
                lastWeek: 'dddd [â´´] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "â´·â´°â´·âµ… âµ™ âµ¢â´°âµ %s",
                past : "âµ¢â´°âµ %s",
                s : "âµ‰âµŽâµ‰â´½",
                m : "âµŽâµ‰âµâµ“â´º",
                mm : "%d âµŽâµ‰âµâµ“â´º",
                h : "âµ™â´°âµ„â´°",
                hh : "%d âµœâ´°âµ™âµ™â´°âµ„âµ‰âµ",
                d : "â´°âµ™âµ™",
                dd : "%d oâµ™âµ™â´°âµ",
                M : "â´°âµ¢oâµ“âµ”",
                MM : "%d âµ‰âµ¢âµ¢âµ‰âµ”âµ",
                y : "â´°âµ™â´³â´°âµ™",
                yy : "%d âµ‰âµ™â´³â´°âµ™âµ"
            },
            week : {
                dow : 6, // Saturday is the first day of the week.
                doy : 12  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : ukrainian (uk)
// author : zemlanin : https://github.com/zemlanin
// Author : Menelion ElensÃºle : https://github.com/Oire

    (function (factory) {
        factory(moment);
    }(function (moment) {
        function plural(word, num) {
            var forms = word.split('_');
            return num % 10 === 1 && num % 100 !== 11 ? forms[0] : (num % 10 >= 2 && num % 10 <= 4 && (num % 100 < 10 || num % 100 >= 20) ? forms[1] : forms[2]);
        }

        function relativeTimeWithPlural(number, withoutSuffix, key) {
            var format = {
                'mm': 'Ñ…Ð²Ð¸Ð»Ð¸Ð½Ð°_Ñ…Ð²Ð¸Ð»Ð¸Ð½Ð¸_Ñ…Ð²Ð¸Ð»Ð¸Ð½',
                'hh': 'Ð³Ð¾Ð´Ð¸Ð½Ð°_Ð³Ð¾Ð´Ð¸Ð½Ð¸_Ð³Ð¾Ð´Ð¸Ð½',
                'dd': 'Ð´ÐµÐ½ÑŒ_Ð´Ð½Ñ–_Ð´Ð½Ñ–Ð²',
                'MM': 'Ð¼Ñ–ÑÑÑ†ÑŒ_Ð¼Ñ–ÑÑÑ†Ñ–_Ð¼Ñ–ÑÑÑ†Ñ–Ð²',
                'yy': 'Ñ€Ñ–Ðº_Ñ€Ð¾ÐºÐ¸_Ñ€Ð¾ÐºÑ–Ð²'
            };
            if (key === 'm') {
                return withoutSuffix ? 'Ñ…Ð²Ð¸Ð»Ð¸Ð½Ð°' : 'Ñ…Ð²Ð¸Ð»Ð¸Ð½Ñƒ';
            }
            else if (key === 'h') {
                return withoutSuffix ? 'Ð³Ð¾Ð´Ð¸Ð½Ð°' : 'Ð³Ð¾Ð´Ð¸Ð½Ñƒ';
            }
            else {
                return number + ' ' + plural(format[key], +number);
            }
        }

        function monthsCaseReplace(m, format) {
            var months = {
                    'nominative': 'ÑÑ–Ñ‡ÐµÐ½ÑŒ_Ð»ÑŽÑ‚Ð¸Ð¹_Ð±ÐµÑ€ÐµÐ·ÐµÐ½ÑŒ_ÐºÐ²Ñ–Ñ‚ÐµÐ½ÑŒ_Ñ‚Ñ€Ð°Ð²ÐµÐ½ÑŒ_Ñ‡ÐµÑ€Ð²ÐµÐ½ÑŒ_Ð»Ð¸Ð¿ÐµÐ½ÑŒ_ÑÐµÑ€Ð¿ÐµÐ½ÑŒ_Ð²ÐµÑ€ÐµÑÐµÐ½ÑŒ_Ð¶Ð¾Ð²Ñ‚ÐµÐ½ÑŒ_Ð»Ð¸ÑÑ‚Ð¾Ð¿Ð°Ð´_Ð³Ñ€ÑƒÐ´ÐµÐ½ÑŒ'.split('_'),
                    'accusative': 'ÑÑ–Ñ‡Ð½Ñ_Ð»ÑŽÑ‚Ð¾Ð³Ð¾_Ð±ÐµÑ€ÐµÐ·Ð½Ñ_ÐºÐ²Ñ–Ñ‚Ð½Ñ_Ñ‚Ñ€Ð°Ð²Ð½Ñ_Ñ‡ÐµÑ€Ð²Ð½Ñ_Ð»Ð¸Ð¿Ð½Ñ_ÑÐµÑ€Ð¿Ð½Ñ_Ð²ÐµÑ€ÐµÑÐ½Ñ_Ð¶Ð¾Ð²Ñ‚Ð½Ñ_Ð»Ð¸ÑÑ‚Ð¾Ð¿Ð°Ð´Ð°_Ð³Ñ€ÑƒÐ´Ð½Ñ'.split('_')
                },

                nounCase = (/D[oD]? *MMMM?/).test(format) ?
                    'accusative' :
                    'nominative';

            return months[nounCase][m.month()];
        }

        function weekdaysCaseReplace(m, format) {
            var weekdays = {
                    'nominative': 'Ð½ÐµÐ´Ñ–Ð»Ñ_Ð¿Ð¾Ð½ÐµÐ´Ñ–Ð»Ð¾Ðº_Ð²Ñ–Ð²Ñ‚Ð¾Ñ€Ð¾Ðº_ÑÐµÑ€ÐµÐ´Ð°_Ñ‡ÐµÑ‚Ð²ÐµÑ€_Ð¿â€™ÑÑ‚Ð½Ð¸Ñ†Ñ_ÑÑƒÐ±Ð¾Ñ‚Ð°'.split('_'),
                    'accusative': 'Ð½ÐµÐ´Ñ–Ð»ÑŽ_Ð¿Ð¾Ð½ÐµÐ´Ñ–Ð»Ð¾Ðº_Ð²Ñ–Ð²Ñ‚Ð¾Ñ€Ð¾Ðº_ÑÐµÑ€ÐµÐ´Ñƒ_Ñ‡ÐµÑ‚Ð²ÐµÑ€_Ð¿â€™ÑÑ‚Ð½Ð¸Ñ†ÑŽ_ÑÑƒÐ±Ð¾Ñ‚Ñƒ'.split('_'),
                    'genitive': 'Ð½ÐµÐ´Ñ–Ð»Ñ–_Ð¿Ð¾Ð½ÐµÐ´Ñ–Ð»ÐºÐ°_Ð²Ñ–Ð²Ñ‚Ð¾Ñ€ÐºÐ°_ÑÐµÑ€ÐµÐ´Ð¸_Ñ‡ÐµÑ‚Ð²ÐµÑ€Ð³Ð°_Ð¿â€™ÑÑ‚Ð½Ð¸Ñ†Ñ–_ÑÑƒÐ±Ð¾Ñ‚Ð¸'.split('_')
                },

                nounCase = (/(\[[Ð’Ð²Ð£Ñƒ]\]) ?dddd/).test(format) ?
                    'accusative' :
                    ((/\[?(?:Ð¼Ð¸Ð½ÑƒÐ»Ð¾Ñ—|Ð½Ð°ÑÑ‚ÑƒÐ¿Ð½Ð¾Ñ—)? ?\] ?dddd/).test(format) ?
                        'genitive' :
                        'nominative');

            return weekdays[nounCase][m.day()];
        }

        function processHoursFunction(str) {
            return function () {
                return str + 'Ð¾' + (this.hours() === 11 ? 'Ð±' : '') + '] LT';
            };
        }

        return moment.lang('uk', {
            months : monthsCaseReplace,
            monthsShort : "ÑÑ–Ñ‡_Ð»ÑŽÑ‚_Ð±ÐµÑ€_ÐºÐ²Ñ–Ñ‚_Ñ‚Ñ€Ð°Ð²_Ñ‡ÐµÑ€Ð²_Ð»Ð¸Ð¿_ÑÐµÑ€Ð¿_Ð²ÐµÑ€_Ð¶Ð¾Ð²Ñ‚_Ð»Ð¸ÑÑ‚_Ð³Ñ€ÑƒÐ´".split("_"),
            weekdays : weekdaysCaseReplace,
            weekdaysShort : "Ð½Ð´_Ð¿Ð½_Ð²Ñ‚_ÑÑ€_Ñ‡Ñ‚_Ð¿Ñ‚_ÑÐ±".split("_"),
            weekdaysMin : "Ð½Ð´_Ð¿Ð½_Ð²Ñ‚_ÑÑ€_Ñ‡Ñ‚_Ð¿Ñ‚_ÑÐ±".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD.MM.YYYY",
                LL : "D MMMM YYYY Ñ€.",
                LLL : "D MMMM YYYY Ñ€., LT",
                LLLL : "dddd, D MMMM YYYY Ñ€., LT"
            },
            calendar : {
                sameDay: processHoursFunction('[Ð¡ÑŒÐ¾Ð³Ð¾Ð´Ð½Ñ– '),
                nextDay: processHoursFunction('[Ð—Ð°Ð²Ñ‚Ñ€Ð° '),
                lastDay: processHoursFunction('[Ð’Ñ‡Ð¾Ñ€Ð° '),
                nextWeek: processHoursFunction('[Ð£] dddd ['),
                lastWeek: function () {
                    switch (this.day()) {
                        case 0:
                        case 3:
                        case 5:
                        case 6:
                            return processHoursFunction('[ÐœÐ¸Ð½ÑƒÐ»Ð¾Ñ—] dddd [').call(this);
                        case 1:
                        case 2:
                        case 4:
                            return processHoursFunction('[ÐœÐ¸Ð½ÑƒÐ»Ð¾Ð³Ð¾] dddd [').call(this);
                    }
                },
                sameElse: 'L'
            },
            relativeTime : {
                future : "Ð·Ð° %s",
                past : "%s Ñ‚Ð¾Ð¼Ñƒ",
                s : "Ð´ÐµÐºÑ–Ð»ÑŒÐºÐ° ÑÐµÐºÑƒÐ½Ð´",
                m : relativeTimeWithPlural,
                mm : relativeTimeWithPlural,
                h : "Ð³Ð¾Ð´Ð¸Ð½Ñƒ",
                hh : relativeTimeWithPlural,
                d : "Ð´ÐµÐ½ÑŒ",
                dd : relativeTimeWithPlural,
                M : "Ð¼Ñ–ÑÑÑ†ÑŒ",
                MM : relativeTimeWithPlural,
                y : "Ñ€Ñ–Ðº",
                yy : relativeTimeWithPlural
            },

            // M. E.: those two are virtually unused but a user might want to implement them for his/her website for some reason

            meridiem : function (hour, minute, isLower) {
                if (hour < 4) {
                    return "Ð½Ð¾Ñ‡Ñ–";
                } else if (hour < 12) {
                    return "Ñ€Ð°Ð½ÐºÑƒ";
                } else if (hour < 17) {
                    return "Ð´Ð½Ñ";
                } else {
                    return "Ð²ÐµÑ‡Ð¾Ñ€Ð°";
                }
            },

            ordinal: function (number, period) {
                switch (period) {
                    case 'M':
                    case 'd':
                    case 'DDD':
                    case 'w':
                    case 'W':
                        return number + '-Ð¹';
                    case 'D':
                        return number + '-Ð³Ð¾';
                    default:
                        return number;
                }
            },

            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 1st is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : uzbek
// author : Sardor Muminov : https://github.com/muminoff

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('uz', {
            months : "ÑÐ½Ð²Ð°Ñ€ÑŒ_Ñ„ÐµÐ²Ñ€Ð°Ð»ÑŒ_Ð¼Ð°Ñ€Ñ‚_Ð°Ð¿Ñ€ÐµÐ»ÑŒ_Ð¼Ð°Ð¹_Ð¸ÑŽÐ½ÑŒ_Ð¸ÑŽÐ»ÑŒ_Ð°Ð²Ð³ÑƒÑÑ‚_ÑÐµÐ½Ñ‚ÑÐ±Ñ€ÑŒ_Ð¾ÐºÑ‚ÑÐ±Ñ€ÑŒ_Ð½Ð¾ÑÐ±Ñ€ÑŒ_Ð´ÐµÐºÐ°Ð±Ñ€ÑŒ".split("_"),
            monthsShort : "ÑÐ½Ð²_Ñ„ÐµÐ²_Ð¼Ð°Ñ€_Ð°Ð¿Ñ€_Ð¼Ð°Ð¹_Ð¸ÑŽÐ½_Ð¸ÑŽÐ»_Ð°Ð²Ð³_ÑÐµÐ½_Ð¾ÐºÑ‚_Ð½Ð¾Ñ_Ð´ÐµÐº".split("_"),
            weekdays : "Ð¯ÐºÑˆÐ°Ð½Ð±Ð°_Ð”ÑƒÑˆÐ°Ð½Ð±Ð°_Ð¡ÐµÑˆÐ°Ð½Ð±Ð°_Ð§Ð¾Ñ€ÑˆÐ°Ð½Ð±Ð°_ÐŸÐ°Ð¹ÑˆÐ°Ð½Ð±Ð°_Ð–ÑƒÐ¼Ð°_Ð¨Ð°Ð½Ð±Ð°".split("_"),
            weekdaysShort : "Ð¯ÐºÑˆ_Ð”ÑƒÑˆ_Ð¡ÐµÑˆ_Ð§Ð¾Ñ€_ÐŸÐ°Ð¹_Ð–ÑƒÐ¼_Ð¨Ð°Ð½".split("_"),
            weekdaysMin : "Ð¯Ðº_Ð”Ñƒ_Ð¡Ðµ_Ð§Ð¾_ÐŸÐ°_Ð–Ñƒ_Ð¨Ð°".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "D MMMM YYYY, dddd LT"
            },
            calendar : {
                sameDay : '[Ð‘ÑƒÐ³ÑƒÐ½ ÑÐ¾Ð°Ñ‚] LT [Ð´Ð°]',
                nextDay : '[Ð­Ñ€Ñ‚Ð°Ð³Ð°] LT [Ð´Ð°]',
                nextWeek : 'dddd [ÐºÑƒÐ½Ð¸ ÑÐ¾Ð°Ñ‚] LT [Ð´Ð°]',
                lastDay : '[ÐšÐµÑ‡Ð° ÑÐ¾Ð°Ñ‚] LT [Ð´Ð°]',
                lastWeek : '[Ð£Ñ‚Ð³Ð°Ð½] dddd [ÐºÑƒÐ½Ð¸ ÑÐ¾Ð°Ñ‚] LT [Ð´Ð°]',
                sameElse : 'L'
            },
            relativeTime : {
                future : "Ð¯ÐºÐ¸Ð½ %s Ð¸Ñ‡Ð¸Ð´Ð°",
                past : "Ð‘Ð¸Ñ€ Ð½ÐµÑ‡Ð° %s Ð¾Ð»Ð´Ð¸Ð½",
                s : "Ñ„ÑƒÑ€ÑÐ°Ñ‚",
                m : "Ð±Ð¸Ñ€ Ð´Ð°ÐºÐ¸ÐºÐ°",
                mm : "%d Ð´Ð°ÐºÐ¸ÐºÐ°",
                h : "Ð±Ð¸Ñ€ ÑÐ¾Ð°Ñ‚",
                hh : "%d ÑÐ¾Ð°Ñ‚",
                d : "Ð±Ð¸Ñ€ ÐºÑƒÐ½",
                dd : "%d ÐºÑƒÐ½",
                M : "Ð±Ð¸Ñ€ Ð¾Ð¹",
                MM : "%d Ð¾Ð¹",
                y : "Ð±Ð¸Ñ€ Ð¹Ð¸Ð»",
                yy : "%d Ð¹Ð¸Ð»"
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 7  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : vietnamese (vn)
// author : Bang Nguyen : https://github.com/bangnk

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('vn', {
            months : "thÃ¡ng 1_thÃ¡ng 2_thÃ¡ng 3_thÃ¡ng 4_thÃ¡ng 5_thÃ¡ng 6_thÃ¡ng 7_thÃ¡ng 8_thÃ¡ng 9_thÃ¡ng 10_thÃ¡ng 11_thÃ¡ng 12".split("_"),
            monthsShort : "Th01_Th02_Th03_Th04_Th05_Th06_Th07_Th08_Th09_Th10_Th11_Th12".split("_"),
            weekdays : "chá»§ nháº­t_thá»© hai_thá»© ba_thá»© tÆ°_thá»© nÄƒm_thá»© sÃ¡u_thá»© báº£y".split("_"),
            weekdaysShort : "CN_T2_T3_T4_T5_T6_T7".split("_"),
            weekdaysMin : "CN_T2_T3_T4_T5_T6_T7".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM [nÄƒm] YYYY",
                LLL : "D MMMM [nÄƒm] YYYY LT",
                LLLL : "dddd, D MMMM [nÄƒm] YYYY LT",
                l : "DD/M/YYYY",
                ll : "D MMM YYYY",
                lll : "D MMM YYYY LT",
                llll : "ddd, D MMM YYYY LT"
            },
            calendar : {
                sameDay: "[HÃ´m nay lÃºc] LT",
                nextDay: '[NgÃ y mai lÃºc] LT',
                nextWeek: 'dddd [tuáº§n tá»›i lÃºc] LT',
                lastDay: '[HÃ´m qua lÃºc] LT',
                lastWeek: 'dddd [tuáº§n rá»“i lÃºc] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "%s tá»›i",
                past : "%s trÆ°á»›c",
                s : "vÃ i giÃ¢y",
                m : "má»™t phÃºt",
                mm : "%d phÃºt",
                h : "má»™t giá»",
                hh : "%d giá»",
                d : "má»™t ngÃ y",
                dd : "%d ngÃ y",
                M : "má»™t thÃ¡ng",
                MM : "%d thÃ¡ng",
                y : "má»™t nÄƒm",
                yy : "%d nÄƒm"
            },
            ordinal : function (number) {
                return number;
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : chinese
// author : suupic : https://github.com/suupic
// author : Zeno Zeng : https://github.com/zenozeng

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('zh-cn', {
            months : "ä¸€æœˆ_äºŒæœˆ_ä¸‰æœˆ_å››æœˆ_äº”æœˆ_å…­æœˆ_ä¸ƒæœˆ_å…«æœˆ_ä¹æœˆ_åæœˆ_åä¸€æœˆ_åäºŒæœˆ".split("_"),
            monthsShort : "1æœˆ_2æœˆ_3æœˆ_4æœˆ_5æœˆ_6æœˆ_7æœˆ_8æœˆ_9æœˆ_10æœˆ_11æœˆ_12æœˆ".split("_"),
            weekdays : "æ˜ŸæœŸæ—¥_æ˜ŸæœŸä¸€_æ˜ŸæœŸäºŒ_æ˜ŸæœŸä¸‰_æ˜ŸæœŸå››_æ˜ŸæœŸäº”_æ˜ŸæœŸå…­".split("_"),
            weekdaysShort : "å‘¨æ—¥_å‘¨ä¸€_å‘¨äºŒ_å‘¨ä¸‰_å‘¨å››_å‘¨äº”_å‘¨å…­".split("_"),
            weekdaysMin : "æ—¥_ä¸€_äºŒ_ä¸‰_å››_äº”_å…­".split("_"),
            longDateFormat : {
                LT : "Ahç‚¹mm",
                L : "YYYYå¹´MMMDæ—¥",
                LL : "YYYYå¹´MMMDæ—¥",
                LLL : "YYYYå¹´MMMDæ—¥LT",
                LLLL : "YYYYå¹´MMMDæ—¥ddddLT",
                l : "YYYYå¹´MMMDæ—¥",
                ll : "YYYYå¹´MMMDæ—¥",
                lll : "YYYYå¹´MMMDæ—¥LT",
                llll : "YYYYå¹´MMMDæ—¥ddddLT"
            },
            meridiem : function (hour, minute, isLower) {
                var hm = hour * 100 + minute;
                if (hm < 600) {
                    return "å‡Œæ™¨";
                } else if (hm < 900) {
                    return "æ—©ä¸Š";
                } else if (hm < 1130) {
                    return "ä¸Šåˆ";
                } else if (hm < 1230) {
                    return "ä¸­åˆ";
                } else if (hm < 1800) {
                    return "ä¸‹åˆ";
                } else {
                    return "æ™šä¸Š";
                }
            },
            calendar : {
                sameDay : function () {
                    return this.minutes() === 0 ? "[ä»Šå¤©]Ah[ç‚¹æ•´]" : "[ä»Šå¤©]LT";
                },
                nextDay : function () {
                    return this.minutes() === 0 ? "[æ˜Žå¤©]Ah[ç‚¹æ•´]" : "[æ˜Žå¤©]LT";
                },
                lastDay : function () {
                    return this.minutes() === 0 ? "[æ˜¨å¤©]Ah[ç‚¹æ•´]" : "[æ˜¨å¤©]LT";
                },
                nextWeek : function () {
                    var startOfWeek, prefix;
                    startOfWeek = moment().startOf('week');
                    prefix = this.unix() - startOfWeek.unix() >= 7 * 24 * 3600 ? '[ä¸‹]' : '[æœ¬]';
                    return this.minutes() === 0 ? prefix + "dddAhç‚¹æ•´" : prefix + "dddAhç‚¹mm";
                },
                lastWeek : function () {
                    var startOfWeek, prefix;
                    startOfWeek = moment().startOf('week');
                    prefix = this.unix() < startOfWeek.unix()  ? '[ä¸Š]' : '[æœ¬]';
                    return this.minutes() === 0 ? prefix + "dddAhç‚¹æ•´" : prefix + "dddAhç‚¹mm";
                },
                sameElse : 'L'
            },
            ordinal : function (number, period) {
                switch (period) {
                    case "d":
                    case "D":
                    case "DDD":
                        return number + "æ—¥";
                    case "M":
                        return number + "æœˆ";
                    case "w":
                    case "W":
                        return number + "å‘¨";
                    default:
                        return number;
                }
            },
            relativeTime : {
                future : "%så†…",
                past : "%så‰",
                s : "å‡ ç§’",
                m : "1åˆ†é’Ÿ",
                mm : "%dåˆ†é’Ÿ",
                h : "1å°æ—¶",
                hh : "%då°æ—¶",
                d : "1å¤©",
                dd : "%då¤©",
                M : "1ä¸ªæœˆ",
                MM : "%dä¸ªæœˆ",
                y : "1å¹´",
                yy : "%då¹´"
            },
            week : {
                // GB/T 7408-1994ã€Šæ•°æ®å…ƒå’Œäº¤æ¢æ ¼å¼Â·ä¿¡æ¯äº¤æ¢Â·æ—¥æœŸå’Œæ—¶é—´è¡¨ç¤ºæ³•ã€‹ä¸ŽISO 8601:1988ç­‰æ•ˆ
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
    }));
// moment.js language configuration
// language : traditional chinese (zh-tw)
// author : Ben : https://github.com/ben-lin

    (function (factory) {
        factory(moment);
    }(function (moment) {
        return moment.lang('zh-tw', {
            months : "ä¸€æœˆ_äºŒæœˆ_ä¸‰æœˆ_å››æœˆ_äº”æœˆ_å…­æœˆ_ä¸ƒæœˆ_å…«æœˆ_ä¹æœˆ_åæœˆ_åä¸€æœˆ_åäºŒæœˆ".split("_"),
            monthsShort : "1æœˆ_2æœˆ_3æœˆ_4æœˆ_5æœˆ_6æœˆ_7æœˆ_8æœˆ_9æœˆ_10æœˆ_11æœˆ_12æœˆ".split("_"),
            weekdays : "æ˜ŸæœŸæ—¥_æ˜ŸæœŸä¸€_æ˜ŸæœŸäºŒ_æ˜ŸæœŸä¸‰_æ˜ŸæœŸå››_æ˜ŸæœŸäº”_æ˜ŸæœŸå…­".split("_"),
            weekdaysShort : "é€±æ—¥_é€±ä¸€_é€±äºŒ_é€±ä¸‰_é€±å››_é€±äº”_é€±å…­".split("_"),
            weekdaysMin : "æ—¥_ä¸€_äºŒ_ä¸‰_å››_äº”_å…­".split("_"),
            longDateFormat : {
                LT : "Ahé»žmm",
                L : "YYYYå¹´MMMDæ—¥",
                LL : "YYYYå¹´MMMDæ—¥",
                LLL : "YYYYå¹´MMMDæ—¥LT",
                LLLL : "YYYYå¹´MMMDæ—¥ddddLT",
                l : "YYYYå¹´MMMDæ—¥",
                ll : "YYYYå¹´MMMDæ—¥",
                lll : "YYYYå¹´MMMDæ—¥LT",
                llll : "YYYYå¹´MMMDæ—¥ddddLT"
            },
            meridiem : function (hour, minute, isLower) {
                var hm = hour * 100 + minute;
                if (hm < 900) {
                    return "æ—©ä¸Š";
                } else if (hm < 1130) {
                    return "ä¸Šåˆ";
                } else if (hm < 1230) {
                    return "ä¸­åˆ";
                } else if (hm < 1800) {
                    return "ä¸‹åˆ";
                } else {
                    return "æ™šä¸Š";
                }
            },
            calendar : {
                sameDay : '[ä»Šå¤©]LT',
                nextDay : '[æ˜Žå¤©]LT',
                nextWeek : '[ä¸‹]ddddLT',
                lastDay : '[æ˜¨å¤©]LT',
                lastWeek : '[ä¸Š]ddddLT',
                sameElse : 'L'
            },
            ordinal : function (number, period) {
                switch (period) {
                    case "d" :
                    case "D" :
                    case "DDD" :
                        return number + "æ—¥";
                    case "M" :
                        return number + "æœˆ";
                    case "w" :
                    case "W" :
                        return number + "é€±";
                    default :
                        return number;
                }
            },
            relativeTime : {
                future : "%så…§",
                past : "%så‰",
                s : "å¹¾ç§’",
                m : "ä¸€åˆ†é˜",
                mm : "%dåˆ†é˜",
                h : "ä¸€å°æ™‚",
                hh : "%då°æ™‚",
                d : "ä¸€å¤©",
                dd : "%då¤©",
                M : "ä¸€å€‹æœˆ",
                MM : "%då€‹æœˆ",
                y : "ä¸€å¹´",
                yy : "%då¹´"
            }
        });
    }));

    moment.lang('en');


    /************************************
     Exposing Moment
     ************************************/

    function makeGlobal(deprecate) {
        var warned = false, local_moment = moment;
        /*global ender:false */
        if (typeof ender !== 'undefined') {
            return;
        }
        // here, `this` means `window` in the browser, or `global` on the server
        // add `moment` as a global object via a string identifier,
        // for Closure Compiler "advanced" mode
        if (deprecate) {
            this.moment = function () {
                if (!warned && console && console.warn) {
                    warned = true;
                    console.warn(
                        "Accessing Moment through the global scope is " +
                            "deprecated, and will be removed in an upcoming " +
                            "release.");
                }
                return local_moment.apply(null, arguments);
            };
        } else {
            this['moment'] = moment;
        }
    }

    // CommonJS module is defined
    if (hasModule) {
        module.exports = moment;
        makeGlobal(true);
    } else if (typeof define === "function" && define.amd) {
        define("moment", function (require, exports, module) {
            if (module.config().noGlobal !== true) {
                // If user provided noGlobal, he is aware of global
                makeGlobal(module.config().noGlobal === undefined);
            }

            return moment;
        });
    } else {
        makeGlobal();
    }
}).call(this);
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


  buffer += "<div class=\"istac-widget-title\">\r\n    <span class=\"istac-widget-title-text\">\r\n        <a href=\"#\" target=\"_blank\"></a>\r\n    </span>\r\n</div>\r\n<div class=\"istac-widget-body\">\r\n    <div class=\"istac-widget-content\">\r\n    </div>\r\n    <div class=\"istac-widget-body-embed\">\r\n        <textarea rows=\"10\"></textarea>\r\n        <a href=\"#\" class=\"hideEmbed\">Cerrar</a>\r\n    </div>\r\n    <div class=\"istac-widget-body-allIndicators\">\r\n        <p class=\"istac-widget-embed\"><a href=\"#\" title=\"Incrustar widget\">&lt;/&gt;</a></p>\r\n        <span class=\"istac-widget-body-allIndicators-text\">\r\n            <img src=\"http://www.gobiernodecanarias.org/opencms8/export/system/modules/es.gobcan.istac.web/resources/images/maslight.gif\"><a target=\"_blank\" href=\"#\">más indicadores</a>\r\n        </span>\r\n    </div>\r\n    <div class=\"istac-widget-footer\">\r\n        <p class=\"istac-widget-credits\"><a target=\"_blank\" href=\"";
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
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.programWithDepth(program2, data, depth0)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n        </div>\r\n    ";
  return buffer;}
function program2(depth0,data,depth1) {
  
  var buffer = "", stack1;
  buffer += "\r\n                ";
  stack1 = depth0.hasValue;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.programWithDepth(program3, data, depth1)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n            ";
  return buffer;}
function program3(depth0,data,depth2) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n                    <div class=\"istac-widget-lateral-subtitle\">";
  stack1 = depth2.temporalLabel;
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
  buffer += " <span>";
  foundHelper = helpers.value;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.value; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</span></div>\r\n                ";
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
  buffer += "\r\n            <tr class=\"";
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
  buffer += "\r\n							<span>\r\n                        		<span class=\"istac-widget-observation\">";
  foundHelper = helpers.value;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.value; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + " </span>\r\n                        		<div class=\"istac-widget-unit\"> ";
  foundHelper = helpers.unit;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.unit; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</div>\r\n                        	</span>\r\n                        </div>\r\n                    ";
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

(function($) {

    Istac.widget.helper = {};

    Istac.widget.helper.tooltip = function($el, text, tooltipId) {
        var $tooltip = $(".istact-widget-tooltip");

        $tooltip = $('<p class="istact-widget-tooltip ' + tooltipId + '"></p>');
        $("body").append($tooltip);

        var xOffset = 10;
        var yOffset = 20;

        if (text) {
            $tooltip.text(text);

            var fnHoverIn = function(e) {
                $tooltip.css("top", (e.pageY - xOffset) + "px").css("left", (e.pageX + yOffset) + "px").fadeIn("fast");
            };
            var fnHoverOut = function() {
                $tooltip.fadeOut("fast");
            };

            $el.hover(fnHoverIn, fnHoverOut);
            $el.mousemove(function(e) {
                $tooltip.css("top", (e.pageY - xOffset) + "px").css("left", (e.pageX + yOffset) + "px");
            });
        }
    };

    Istac.widget.helper.addThousandSeparator = function(nStr) {
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

    Istac.widget.helper.getHost = function(url) {
        var l = document.createElement("a");
        l.href = url;
        return l.host;
    };

    Istac.widget.helper.absoluteUrl = function(url) {
        var a = document.createElement('a');
        a.href = url;
        return a.href;
    };

    Istac.widget.helper.getKeys = function(hash) {
        var keys = [];
        for ( var i in hash) {
            if (hash.hasOwnProperty(i)) {
                keys.push(i);
            }
        }
        return keys;
    };

    Istac.widget.helper.firstKey = function(hash) {
        var key;
        for ( var i in hash) {
            if (hash.hasOwnProperty(i)) {
                key = i;
                break;
            }
        }
        return key;
    };

    Istac.widget.helper.firstValue = function(hash) {
        var key = this.firstKey(hash);
        var result;
        if (key) {
            result = hash[key];
        }
        return result;
    }

    var colorHueDistributor = function(n) {
        var result = [ 0 ];
        var total = 400;
        var divisions = 3;
        var step = total / divisions;
        var offset = 0;

        for (var i = 1; i < n; i++) {
            if ((i != 0) && ((i * step) % total === 0)) {
                offset = step / 2;
            }
            result[i] = (result[i - 1] + step + offset) % total;
            if ((i != 0) && ((i * step) % total === 0)) {
                step = step / 2;
            }
        }

        result = _.map(result, function(color) {
            return color / total;
        });

        return result;
    };

    var hsvToRgb = function(h, s, v) {
        var r, g, b;

        var i = Math.floor(h * 6);
        var f = h * 6 - i;
        var p = v * (1 - s);
        var q = v * (1 - f * s);
        var t = v * (1 - (1 - f) * s);

        switch (i % 6) {
            case 0:
                r = v, g = t, b = p;
                break;
            case 1:
                r = q, g = v, b = p;
                break;
            case 2:
                r = p, g = v, b = t;
                break;
            case 3:
                r = p, g = q, b = v;
                break;
            case 4:
                r = t, g = p, b = v;
                break;
            case 5:
                r = v, g = p, b = q;
                break;
        }

        return [ r * 255, g * 255, b * 255 ];
    };

    var componentToHex = function(c) {
        var hex = c.toString(16);
        return hex.length == 1 ? "0" + hex : hex;
    };

    var rgbToHex = function(r, g, b) {
        return "#" + componentToHex(r) + componentToHex(g) + componentToHex(b);
    };

    Istac.widget.helper.colorPaletteGenerator = function(n) {
        var hues = colorHueDistributor(n);
        return _.map(hues, function(hue) {
            var rgb = hsvToRgb(hue, 0.84, 0.84);
            rgb = _.map(rgb, function(component) {
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
            var geographicalRepresentation = this._geographicalRepresentation(options.geographicalValues);
            var measureRepresentation = this._measureRepresentation(options.measures);
            return "&representation=" + geographicalRepresentation+","+measureRepresentation;
        },
        
        _geographicalRepresentation : function(geoValues) {
        	this._validateOneOrMore(geoValues);
        	var geographicalRepresentation = geoValues.join("|");
        	return "GEOGRAPHICAL[" + geographicalRepresentation + "]";
        },
        
        _measureRepresentation : function(measureValues) {
        	this._validateOneOrMore(measureValues);
        	var measureRepresentation = measureValues.join("|");
        	return "MEASURE[" + measureRepresentation + "]";
        },

        _granularity : function (options) {
            this._validateOne(options.timeGranularities);
            var timeGranularities = options.timeGranularities.join("|");
            return "&granularity=TIME[" + timeGranularities + "]";
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
                + this._representation(options)
                + this._granularity(options);
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
            this.apiUrl = options.apiUrl || "";
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
                this.titleText = $('');
                this.titleContainer = $('');
            } else {
                this.el.html(this._containerTemplate(templateOptions));
                this.titleText = this.el.find('.istac-widget-title-text');
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

            var $link = this.titleText.find('a');
            if ($link.length > 0) {
                $link.text(title);
            } else {
                this.titleText.text(title);
            }
            this.title = title;
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
                this.titleText.html('<a href="' + url + '" target="_blank"></a>');
                this.setTitle(this.title);
                this.setHeaderColor(this.headerColor);
            } else {
                this.titleText.html('');
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
        },
        
        onAfterRender : function() {
        	if (this.options.callback) {
        		this.options.callback(this);
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

                var jaxiBaseUrl = Istac.widget.configuration['indicators.jaxi.remote.url'];

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
                
                this.onAfterRender();
            },

            addTooltips : function ($el) {
                $el.find('[data-tooltip]').each(function (i, row) {
                    var $row = $(row);
                    var tooltipId = 'row-' + i;
                    Istac.widget.helper.tooltip($row, $row.data('tooltip'), tooltipId);
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
            
            orderDatasetsByInstancesOrder : function(datasets) {
            	return _.sortBy(datasets, function(dataset) {
            		return _.indexOf(this.options.instances, dataset.request.id);
            	}, this);
            },

            renderTable : function (datasets) {
                var context = {};
                context.measures = _.map(this.measures, function (measure) {
                    return measuresLabels[measure];
                });
                
                datasets = this.orderDatasetsByInstancesOrder(datasets);
                
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

    Highcharts.setOptions({
        lang : {
            months : ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
                'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
            weekdays : ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sabado'],
            shortMonths : ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
            thousandsSep : '.',
            decimalPoint : ','
        }
    });

    Istac.widget.Temporal = function (options) {
        this.init(options);
    };

    Istac.widget.Temporal.prototype = _.extend({}, Istac.widget.Base.prototype, {

        parse : function (dataset) {
            var locale = this.locale;

            var measureValue = this.measures[0];
            var timeValues = dataset.getTimeValues();
            var timeValuesTitles = dataset.getTimeValuesTitles(locale);
            var geographicalValues = this.geographicalValues;
            var geographicalValuesTitles = dataset.getGeographicalValuesTitles(locale);

            var legend = {};
            var values = [];
            var series = [];

            for (var i = 0; i < geographicalValues.length; i++) {
                var geoValue = geographicalValues[i];
                var geoValueTitle = geographicalValuesTitles[geoValue];

                var data = [];
                var tooltip = [];
                for (var j = 0; j < timeValues.length; j++) {
                    var timeValue = timeValues[j];
                    var value = dataset.getObservation(geoValue, timeValue, measureValue);
                    var valueStr = dataset.getObservationStr(geoValue, timeValue, measureValue);
                    var unit = dataset.getUnit(measureValue);

                    var date = Istac.widget.DateParser.parse(timeValue);

                    data.push([date, value]);
                    values.push(value);

                    if (_.isString(geoValueTitle)) {
                        geoValueTitle = geoValueTitle.trim();
                    }
                    tooltip.push('<strong>' + valueStr + ' ' + unit + '</strong><br/>' + geoValueTitle + '<br/>' + timeValuesTitles[timeValue]);
                }

                series.push({
                    name : geoValueTitle,
                    data : data,
                    tooltip : tooltip
                })
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

            var isNumber = function (n) {
                return _.isNumber(n)
            };

            var maxValue = _.chain(values).filter(isNumber).max().value();
            var minValue = _.chain(values).filter(isNumber).min().value();

            var chartData = {
                labels : labels,
                values : values,
                legend : legend,
                maxValue : maxValue,
                minValue : minValue,
                series : series
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
            
            this.onAfterRender();
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
            $chartContainer.css('height', 250);
            this.contentContainer.html($chartContainer);

            var highchartsOptions = {
                chart : {
                    type : 'line'
                },
                colors : Istac.widget.helper.colorPaletteGenerator(chartData.series.length),
                title : {
                    text : ''
                },
                xAxis : {
                    type : 'datetime'
                },
                yAxis : {
                    title : {
                        text : ''
                    }
                },
                tooltip : {
                    formatter : function () {
                        var index = _.indexOf(this.series.xData, this.x);
                        return chartData.series[this.series.index].tooltip[index];
                    }
                },
                legend : {
                    enabled : false,
                    itemStyle: {
                    	   fontSize: '10px'
                    }
                },
                credits : {
                    enabled : false
                },
                exporting : {
                    enabled : false
                },
                plotOptions : {
                    line : {
                        animation : false,
                        marker : {
                            enabled : false
                        }
                    }
                },
                series : chartData.series
            };

            if (!this.showLabels) {
                _.extend(highchartsOptions.xAxis, {
                    lineWidth : 0,
                    minorGridLineWidth : 0,
                    lineColor : 'transparent',
                    labels : {
                        enabled : false
                    },
                    minorTickLength : 0,
                    tickLength : 0
                });
            }

            if (this.showLegend) {
                highchartsOptions.legend.enabled = true;
            }

            if (_.isFinite(chartData.minValue) && _.isFinite(chartData.maxValue)) {
                if (this.options.scale === "minmax") {
                    highchartsOptions.yAxis.tickPositioner = function () {
                        var step = (chartData.maxValue - chartData.minValue) / 4;
                        return _.range(chartData.minValue, chartData.maxValue + step, step);
                    }
                } else if (this.options.scale == "natural-lib") {
                    var min = chartData.minValue;
                    var max = chartData.maxValue;
                    if (min < 0 && max > 0) {
                        var limit = Math.max(Math.abs(min), Math.abs(max));
                        highchartsOptions.yAxis.min = -limit;
                        highchartsOptions.yAxis.max = +limit;
                    } else if (min < 0 && max < 0) {
                        highchartsOptions.yAxis.min = min;
                        highchartsOptions.yAxis.max = 0;
                    } else if (min > 0 && max > 0) {
                        highchartsOptions.yAxis.min = 0;
                        highchartsOptions.yAxis.max = max;
                    }
                } else if (this.options.scale == "natural") {
                    var scale = Istac.widget.NaturalScale.scale({ymin : chartData.minValue, ymax : chartData.maxValue });
                    highchartsOptions.yAxis.tickPositioner = function () {
                        var step = (scale.ytop - scale.ydown) / scale.ranges;
                        return _.range(scale.ydown, scale.ytop + step, step);
                    };
                }
            }

            $chartContainer.highcharts(highchartsOptions);
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
        }
    };

}());
(function ($) {

    var showError = function (msg) {
        $(options.el).text(msg);
    };

    Istac.widget.Factory = function (options, callback) {
        options = options || {};
        
        options.callback = callback;

        if (options.hasOwnProperty('url')) {
            var url = options.url + "/widgets/external/configuration";

            var configRequest = $.ajax({
                method : "GET",
                dataType : "jsonp",
                jsonp : '_callback',
                url : url
            });

            configRequest.success(function (configuration) {
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
                
            });
        } else {
            showError("Error, no se ha especificado la url del servicio web");
        }
    };

    //Global export
    window.IstacWidget = Istac.widget.Factory;

}(jQuery));


(function () {
    "use strict";

    var YEARLY_PATTERN = /^\d{4}$/;
    var BIYEARLY_PATTERN = /^\d{4}H\d$/;
    var QUARTERLY_PATTERN = /^\d{4}Q\d$/;
    var MONTHTLY_PATTERN = /^\d{4}M\d\d$/;
    var WEEKLY_PATTERN = /^\d{4}W\d\d$/;
    var DAILY_PATTERN = /^\d{8}$/;

    Istac.widget.DateParser = {

        /**
         * @param {String} stringDate format:
         *
         *      - Yearly: yyyy (Example: 1999)
         *      - Biyearly: yyyyHs (Example: 1999H1)
         *      - Quarterly: yyyyQt (Example: 1999Q1)
         *      - Monthly: yyyyMmm (Example: 1999M01)
         *      - Weekly: yyyyWss (Example: 1999W51).
         *      - Daily: yyyymmdd (Example: 19990101)
         *
         * @return {Number} milliseconds
         */
        parse : function (stringDate) {
            var date;

            if (stringDate.match(YEARLY_PATTERN)) {
                date = moment(stringDate, "YYYY");
            } else if (stringDate.match(BIYEARLY_PATTERN)) {
                date = moment(stringDate, "YYYY?M");
                date.month(((date.month() + 1) * 6) - 1);
            } else if (stringDate.match(QUARTERLY_PATTERN)) {
                date = moment(stringDate, "YYYY?M");
                date.month(((date.month() + 1) * 3) - 1);
            } else if (stringDate.match(MONTHTLY_PATTERN)) {
                date = moment(stringDate, "YYYY'M'MM");
            } else if (stringDate.match(WEEKLY_PATTERN)) {
                date = moment(stringDate, "YYYY'W'WW").weekday(0);
            } else if (stringDate.match(DAILY_PATTERN)) {
                date = moment(stringDate, "YYYYMMDD");
            }

            if (date) {
                return date.utc().valueOf();
            }
        }
    };


}());