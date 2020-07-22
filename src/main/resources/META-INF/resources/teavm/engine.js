"use strict";
var main;
(function() {
var $rt_seed = 2463534242;
function $rt_nextId() {
    var x = $rt_seed;
    x ^= x << 13;
    x ^= x >> 17;
    x ^= x << 5;
    $rt_seed = x;
    return x;
}
function $rt_compare(a, b) {
    return a > b ? 1 : a < b ?  -1 : a === b ? 0 : 1;
}
function $rt_isInstance(obj, cls) {
    return obj !== null && !!obj.constructor.$meta && $rt_isAssignable(obj.constructor, cls);
}
function $rt_isAssignable(from, to) {
    if (from === to) {
        return true;
    }
    if (to.$meta.item !== null) {
        return from.$meta.item !== null && $rt_isAssignable(from.$meta.item, to.$meta.item);
    }
    var supertypes = from.$meta.supertypes;
    for (var i = 0;i < supertypes.length;i = i + 1 | 0) {
        if ($rt_isAssignable(supertypes[i], to)) {
            return true;
        }
    }
    return false;
}
Array.prototype.fill = Array.prototype.fill || function(value, start, end) {
    var len = this.length;
    if (!len) return this;
    start = start | 0;
    var i = start < 0 ? Math.max(len + start, 0) : Math.min(start, len);
    end = end === undefined ? len : end | 0;
    end = end < 0 ? Math.max(len + end, 0) : Math.min(end, len);
    for (;i < end;i++) {
        this[i] = value;
    }
    return this;
};
function $rt_createArray(cls, sz) {
    var data = new Array(sz);
    data.fill(null);
    return new $rt_array(cls, data);
}
function $rt_createArrayFromData(cls, init) {
    return $rt_wrapArray(cls, init);
}
function $rt_wrapArray(cls, data) {
    return new $rt_array(cls, data);
}
function $rt_createUnfilledArray(cls, sz) {
    return new $rt_array(cls, new Array(sz));
}
function $rt_createLongArray(sz) {
    var data = new Array(sz);
    var arr = new $rt_array($rt_longcls(), data);
    data.fill(Long_ZERO);
    return arr;
}
function $rt_createLongArrayFromData(init) {
    return new $rt_array($rt_longcls(), init);
}
function $rt_createNumericArray(cls, nativeArray) {
    return new $rt_array(cls, nativeArray);
}
function $rt_createCharArray(sz) {
    return $rt_createNumericArray($rt_charcls(), new Uint16Array(sz));
}
function $rt_createCharArrayFromData(data) {
    var buffer = new Uint16Array(data.length);
    buffer.set(data);
    return $rt_createNumericArray($rt_charcls(), buffer);
}
function $rt_createByteArray(sz) {
    return $rt_createNumericArray($rt_bytecls(), new Int8Array(sz));
}
function $rt_createByteArrayFromData(data) {
    var buffer = new Int8Array(data.length);
    buffer.set(data);
    return $rt_createNumericArray($rt_bytecls(), buffer);
}
function $rt_createShortArray(sz) {
    return $rt_createNumericArray($rt_shortcls(), new Int16Array(sz));
}
function $rt_createShortArrayFromData(data) {
    var buffer = new Int16Array(data.length);
    buffer.set(data);
    return $rt_createNumericArray($rt_shortcls(), buffer);
}
function $rt_createIntArray(sz) {
    return $rt_createNumericArray($rt_intcls(), new Int32Array(sz));
}
function $rt_createIntArrayFromData(data) {
    var buffer = new Int32Array(data.length);
    buffer.set(data);
    return $rt_createNumericArray($rt_intcls(), buffer);
}
function $rt_createBooleanArray(sz) {
    return $rt_createNumericArray($rt_booleancls(), new Int8Array(sz));
}
function $rt_createBooleanArrayFromData(data) {
    var buffer = new Int8Array(data.length);
    buffer.set(data);
    return $rt_createNumericArray($rt_booleancls(), buffer);
}
function $rt_createFloatArray(sz) {
    return $rt_createNumericArray($rt_floatcls(), new Float32Array(sz));
}
function $rt_createFloatArrayFromData(data) {
    var buffer = new Float32Array(data.length);
    buffer.set(data);
    return $rt_createNumericArray($rt_floatcls(), buffer);
}
function $rt_createDoubleArray(sz) {
    return $rt_createNumericArray($rt_doublecls(), new Float64Array(sz));
}
function $rt_createDoubleArrayFromData(data) {
    var buffer = new Float64Array(data.length);
    buffer.set(data);
    return $rt_createNumericArray($rt_doublecls(), buffer);
}
function $rt_arraycls(cls) {
    var result = cls.$array;
    if (result === null) {
        var arraycls = {  };
        var name = "[" + cls.$meta.binaryName;
        arraycls.$meta = { item : cls, supertypes : [$rt_objcls()], primitive : false, superclass : $rt_objcls(), name : name, binaryName : name, enum : false, simpleName : null, declaringClass : null, enclosingClass : null };
        arraycls.classObject = null;
        arraycls.$array = null;
        result = arraycls;
        cls.$array = arraycls;
    }
    return result;
}
function $rt_createcls() {
    return { $array : null, classObject : null, $meta : { supertypes : [], superclass : null } };
}
function $rt_createPrimitiveCls(name, binaryName) {
    var cls = $rt_createcls();
    cls.$meta.primitive = true;
    cls.$meta.name = name;
    cls.$meta.binaryName = binaryName;
    cls.$meta.enum = false;
    cls.$meta.item = null;
    cls.$meta.simpleName = null;
    cls.$meta.declaringClass = null;
    cls.$meta.enclosingClass = null;
    return cls;
}
var $rt_booleanclsCache = null;
function $rt_booleancls() {
    if ($rt_booleanclsCache === null) {
        $rt_booleanclsCache = $rt_createPrimitiveCls("boolean", "Z");
    }
    return $rt_booleanclsCache;
}
var $rt_charclsCache = null;
function $rt_charcls() {
    if ($rt_charclsCache === null) {
        $rt_charclsCache = $rt_createPrimitiveCls("char", "C");
    }
    return $rt_charclsCache;
}
var $rt_byteclsCache = null;
function $rt_bytecls() {
    if ($rt_byteclsCache === null) {
        $rt_byteclsCache = $rt_createPrimitiveCls("byte", "B");
    }
    return $rt_byteclsCache;
}
var $rt_shortclsCache = null;
function $rt_shortcls() {
    if ($rt_shortclsCache === null) {
        $rt_shortclsCache = $rt_createPrimitiveCls("short", "S");
    }
    return $rt_shortclsCache;
}
var $rt_intclsCache = null;
function $rt_intcls() {
    if ($rt_intclsCache === null) {
        $rt_intclsCache = $rt_createPrimitiveCls("int", "I");
    }
    return $rt_intclsCache;
}
var $rt_longclsCache = null;
function $rt_longcls() {
    if ($rt_longclsCache === null) {
        $rt_longclsCache = $rt_createPrimitiveCls("long", "J");
    }
    return $rt_longclsCache;
}
var $rt_floatclsCache = null;
function $rt_floatcls() {
    if ($rt_floatclsCache === null) {
        $rt_floatclsCache = $rt_createPrimitiveCls("float", "F");
    }
    return $rt_floatclsCache;
}
var $rt_doubleclsCache = null;
function $rt_doublecls() {
    if ($rt_doubleclsCache === null) {
        $rt_doubleclsCache = $rt_createPrimitiveCls("double", "D");
    }
    return $rt_doubleclsCache;
}
var $rt_voidclsCache = null;
function $rt_voidcls() {
    if ($rt_voidclsCache === null) {
        $rt_voidclsCache = $rt_createPrimitiveCls("void", "V");
    }
    return $rt_voidclsCache;
}
function $rt_throw(ex) {
    throw $rt_exception(ex);
}
var $rt_exceptionTag = Symbol("teavmException");
function $rt_exception(ex) {
    var err = ex.$jsException;
    if (!err) {
        err = new Error("Java exception thrown");
        if (typeof Error.captureStackTrace === "function") {
            Error.captureStackTrace(err);
        }
        err.$javaException = ex;
        err[$rt_exceptionTag] = true;
        ex.$jsException = err;
        $rt_fillStack(err, ex);
    }
    return err;
}
function $rt_fillStack(err, ex) {
    if (typeof $rt_decodeStack === "function" && err.stack) {
        var stack = $rt_decodeStack(err.stack);
        var javaStack = $rt_createArray($rt_objcls(), stack.length);
        var elem;
        var noStack = false;
        for (var i = 0;i < stack.length;++i) {
            var element = stack[i];
            elem = $rt_createStackElement($rt_str(element.className), $rt_str(element.methodName), $rt_str(element.fileName), element.lineNumber);
            if (elem == null) {
                noStack = true;
                break;
            }
            javaStack.data[i] = elem;
        }
        if (!noStack) {
            $rt_setStack(ex, javaStack);
        }
    }
}
function $rt_createMultiArray(cls, dimensions) {
    var first = 0;
    for (var i = dimensions.length - 1;i >= 0;i = i - 1 | 0) {
        if (dimensions[i] === 0) {
            first = i;
            break;
        }
    }
    if (first > 0) {
        for (i = 0;i < first;i = i + 1 | 0) {
            cls = $rt_arraycls(cls);
        }
        if (first === dimensions.length - 1) {
            return $rt_createArray(cls, dimensions[first]);
        }
    }
    var arrays = new Array($rt_primitiveArrayCount(dimensions, first));
    var firstDim = dimensions[first] | 0;
    for (i = 0;i < arrays.length;i = i + 1 | 0) {
        arrays[i] = $rt_createArray(cls, firstDim);
    }
    return $rt_createMultiArrayImpl(cls, arrays, dimensions, first);
}
function $rt_createByteMultiArray(dimensions) {
    var arrays = new Array($rt_primitiveArrayCount(dimensions, 0));
    if (arrays.length === 0) {
        return $rt_createMultiArray($rt_bytecls(), dimensions);
    }
    var firstDim = dimensions[0] | 0;
    for (var i = 0;i < arrays.length;i = i + 1 | 0) {
        arrays[i] = $rt_createByteArray(firstDim);
    }
    return $rt_createMultiArrayImpl($rt_bytecls(), arrays, dimensions);
}
function $rt_createCharMultiArray(dimensions) {
    var arrays = new Array($rt_primitiveArrayCount(dimensions, 0));
    if (arrays.length === 0) {
        return $rt_createMultiArray($rt_charcls(), dimensions);
    }
    var firstDim = dimensions[0] | 0;
    for (var i = 0;i < arrays.length;i = i + 1 | 0) {
        arrays[i] = $rt_createCharArray(firstDim);
    }
    return $rt_createMultiArrayImpl($rt_charcls(), arrays, dimensions, 0);
}
function $rt_createBooleanMultiArray(dimensions) {
    var arrays = new Array($rt_primitiveArrayCount(dimensions, 0));
    if (arrays.length === 0) {
        return $rt_createMultiArray($rt_booleancls(), dimensions);
    }
    var firstDim = dimensions[0] | 0;
    for (var i = 0;i < arrays.length;i = i + 1 | 0) {
        arrays[i] = $rt_createBooleanArray(firstDim);
    }
    return $rt_createMultiArrayImpl($rt_booleancls(), arrays, dimensions, 0);
}
function $rt_createShortMultiArray(dimensions) {
    var arrays = new Array($rt_primitiveArrayCount(dimensions, 0));
    if (arrays.length === 0) {
        return $rt_createMultiArray($rt_shortcls(), dimensions);
    }
    var firstDim = dimensions[0] | 0;
    for (var i = 0;i < arrays.length;i = i + 1 | 0) {
        arrays[i] = $rt_createShortArray(firstDim);
    }
    return $rt_createMultiArrayImpl($rt_shortcls(), arrays, dimensions, 0);
}
function $rt_createIntMultiArray(dimensions) {
    var arrays = new Array($rt_primitiveArrayCount(dimensions, 0));
    if (arrays.length === 0) {
        return $rt_createMultiArray($rt_intcls(), dimensions);
    }
    var firstDim = dimensions[0] | 0;
    for (var i = 0;i < arrays.length;i = i + 1 | 0) {
        arrays[i] = $rt_createIntArray(firstDim);
    }
    return $rt_createMultiArrayImpl($rt_intcls(), arrays, dimensions, 0);
}
function $rt_createLongMultiArray(dimensions) {
    var arrays = new Array($rt_primitiveArrayCount(dimensions, 0));
    if (arrays.length === 0) {
        return $rt_createMultiArray($rt_longcls(), dimensions);
    }
    var firstDim = dimensions[0] | 0;
    for (var i = 0;i < arrays.length;i = i + 1 | 0) {
        arrays[i] = $rt_createLongArray(firstDim);
    }
    return $rt_createMultiArrayImpl($rt_longcls(), arrays, dimensions, 0);
}
function $rt_createFloatMultiArray(dimensions) {
    var arrays = new Array($rt_primitiveArrayCount(dimensions, 0));
    if (arrays.length === 0) {
        return $rt_createMultiArray($rt_floatcls(), dimensions);
    }
    var firstDim = dimensions[0] | 0;
    for (var i = 0;i < arrays.length;i = i + 1 | 0) {
        arrays[i] = $rt_createFloatArray(firstDim);
    }
    return $rt_createMultiArrayImpl($rt_floatcls(), arrays, dimensions, 0);
}
function $rt_createDoubleMultiArray(dimensions) {
    var arrays = new Array($rt_primitiveArrayCount(dimensions, 0));
    if (arrays.length === 0) {
        return $rt_createMultiArray($rt_doublecls(), dimensions);
    }
    var firstDim = dimensions[0] | 0;
    for (var i = 0;i < arrays.length;i = i + 1 | 0) {
        arrays[i] = $rt_createDoubleArray(firstDim);
    }
    return $rt_createMultiArrayImpl($rt_doublecls(), arrays, dimensions, 0);
}
function $rt_primitiveArrayCount(dimensions, start) {
    var val = dimensions[start + 1] | 0;
    for (var i = start + 2;i < dimensions.length;i = i + 1 | 0) {
        val = val * (dimensions[i] | 0) | 0;
        if (val === 0) {
            break;
        }
    }
    return val;
}
function $rt_createMultiArrayImpl(cls, arrays, dimensions, start) {
    var limit = arrays.length;
    for (var i = start + 1 | 0;i < dimensions.length;i = i + 1 | 0) {
        cls = $rt_arraycls(cls);
        var dim = dimensions[i];
        var index = 0;
        var packedIndex = 0;
        while (index < limit) {
            var arr = $rt_createUnfilledArray(cls, dim);
            for (var j = 0;j < dim;j = j + 1 | 0) {
                arr.data[j] = arrays[index];
                index = index + 1 | 0;
            }
            arrays[packedIndex] = arr;
            packedIndex = packedIndex + 1 | 0;
        }
        limit = packedIndex;
    }
    return arrays[0];
}
function $rt_assertNotNaN(value) {
    if (typeof value === 'number' && isNaN(value)) {
        throw "NaN";
    }
    return value;
}
function $rt_createOutputFunction(printFunction) {
    var buffer = "";
    var utf8Buffer = 0;
    var utf8Remaining = 0;
    function putCodePoint(ch) {
        if (ch === 0xA) {
            printFunction(buffer);
            buffer = "";
        } else if (ch < 0x10000) {
            buffer += String.fromCharCode(ch);
        } else {
            ch = ch - 0x10000 | 0;
            var hi = (ch >> 10) + 0xD800;
            var lo = (ch & 0x3FF) + 0xDC00;
            buffer += String.fromCharCode(hi, lo);
        }
    }
    return function(ch) {
        if ((ch & 0x80) === 0) {
            putCodePoint(ch);
        } else if ((ch & 0xC0) === 0x80) {
            if (utf8Buffer > 0) {
                utf8Remaining <<= 6;
                utf8Remaining |= ch & 0x3F;
                if ( --utf8Buffer === 0) {
                    putCodePoint(utf8Remaining);
                }
            }
        } else if ((ch & 0xE0) === 0xC0) {
            utf8Remaining = ch & 0x1F;
            utf8Buffer = 1;
        } else if ((ch & 0xF0) === 0xE0) {
            utf8Remaining = ch & 0x0F;
            utf8Buffer = 2;
        } else if ((ch & 0xF8) === 0xF0) {
            utf8Remaining = ch & 0x07;
            utf8Buffer = 3;
        }
    };
}
var $rt_putStdout = typeof $rt_putStdoutCustom === "function" ? $rt_putStdoutCustom : typeof console === "object" ? $rt_createOutputFunction(function(msg) {
    console.info(msg);
}) : function() {
};
var $rt_putStderr = typeof $rt_putStderrCustom === "function" ? $rt_putStderrCustom : typeof console === "object" ? $rt_createOutputFunction(function(msg) {
    console.error(msg);
}) : function() {
};
var $rt_packageData = null;
function $rt_packages(data) {
    var i = 0;
    var packages = new Array(data.length);
    for (var j = 0;j < data.length;++j) {
        var prefixIndex = data[i++];
        var prefix = prefixIndex >= 0 ? packages[prefixIndex] : "";
        packages[j] = prefix + data[i++] + ".";
    }
    $rt_packageData = packages;
}
function $rt_metadata(data) {
    var packages = $rt_packageData;
    var i = 0;
    while (i < data.length) {
        var cls = data[i++];
        cls.$meta = {  };
        var m = cls.$meta;
        var className = data[i++];
        m.name = className !== 0 ? className : null;
        if (m.name !== null) {
            var packageIndex = data[i++];
            if (packageIndex >= 0) {
                m.name = packages[packageIndex] + m.name;
            }
        }
        m.binaryName = "L" + m.name + ";";
        var superclass = data[i++];
        m.superclass = superclass !== 0 ? superclass : null;
        m.supertypes = data[i++];
        if (m.superclass) {
            m.supertypes.push(m.superclass);
            cls.prototype = Object.create(m.superclass.prototype);
        } else {
            cls.prototype = {  };
        }
        var flags = data[i++];
        m.enum = (flags & 8) !== 0;
        m.flags = flags;
        m.primitive = false;
        m.item = null;
        cls.prototype.constructor = cls;
        cls.classObject = null;
        m.accessLevel = data[i++];
        var innerClassInfo = data[i++];
        if (innerClassInfo === 0) {
            m.simpleName = null;
            m.declaringClass = null;
            m.enclosingClass = null;
        } else {
            var enclosingClass = innerClassInfo[0];
            m.enclosingClass = enclosingClass !== 0 ? enclosingClass : null;
            var declaringClass = innerClassInfo[1];
            m.declaringClass = declaringClass !== 0 ? declaringClass : null;
            var simpleName = innerClassInfo[2];
            m.simpleName = simpleName !== 0 ? simpleName : null;
        }
        var clinit = data[i++];
        cls.$clinit = clinit !== 0 ? clinit : function() {
        };
        var virtualMethods = data[i++];
        if (virtualMethods !== 0) {
            for (var j = 0;j < virtualMethods.length;j += 2) {
                var name = virtualMethods[j];
                var func = virtualMethods[j + 1];
                if (typeof name === 'string') {
                    name = [name];
                }
                for (var k = 0;k < name.length;++k) {
                    cls.prototype[name[k]] = func;
                }
            }
        }
        cls.$array = null;
    }
}
function $rt_wrapFunction0(f) {
    return function() {
        return f(this);
    };
}
function $rt_wrapFunction1(f) {
    return function(p1) {
        return f(this, p1);
    };
}
function $rt_wrapFunction2(f) {
    return function(p1, p2) {
        return f(this, p1, p2);
    };
}
function $rt_wrapFunction3(f) {
    return function(p1, p2, p3) {
        return f(this, p1, p2, p3, p3);
    };
}
function $rt_wrapFunction4(f) {
    return function(p1, p2, p3, p4) {
        return f(this, p1, p2, p3, p4);
    };
}
function $rt_threadStarter(f) {
    return function() {
        var args = Array.prototype.slice.apply(arguments);
        $rt_startThread(function() {
            f.apply(this, args);
        });
    };
}
function $rt_mainStarter(f) {
    return function(args, callback) {
        if (!args) {
            args = [];
        }
        var javaArgs = $rt_createArray($rt_objcls(), args.length);
        for (var i = 0;i < args.length;++i) {
            javaArgs.data[i] = $rt_str(args[i]);
        }
        $rt_startThread(function() {
            f.call(null, javaArgs);
        }, callback);
    };
}
var $rt_stringPool_instance;
function $rt_stringPool(strings) {
    $rt_stringPool_instance = new Array(strings.length);
    for (var i = 0;i < strings.length;++i) {
        $rt_stringPool_instance[i] = $rt_intern($rt_str(strings[i]));
    }
}
function $rt_s(index) {
    return $rt_stringPool_instance[index];
}
function $rt_eraseClinit(target) {
    return target.$clinit = function() {
    };
}
var $rt_numberConversionView = new DataView(new ArrayBuffer(8));
function $rt_doubleToLongBits(n) {
    $rt_numberConversionView.setFloat64(0, n, true);
    return new Long($rt_numberConversionView.getInt32(0, true), $rt_numberConversionView.getInt32(4, true));
}
function $rt_longBitsToDouble(n) {
    $rt_numberConversionView.setInt32(0, n.lo, true);
    $rt_numberConversionView.setInt32(4, n.hi, true);
    return $rt_numberConversionView.getFloat64(0, true);
}
function $rt_floatToIntBits(n) {
    $rt_numberConversionView.setFloat32(0, n);
    return $rt_numberConversionView.getInt32(0);
}
function $rt_intBitsToFloat(n) {
    $rt_numberConversionView.setInt32(0, n);
    return $rt_numberConversionView.getFloat32(0);
}
function $rt_javaException(e) {
    return e instanceof Error && $rt_exceptionTag in e && typeof e.$javaException === 'object' ? e.$javaException : null;
}
function $rt_jsException(e) {
    return typeof e.$jsException === 'object' ? e.$jsException : null;
}
function $rt_wrapException(err) {
    var ex = err.$javaException;
    if (!ex || !($rt_exceptionTag in err)) {
        ex = $rt_createException($rt_str("(JavaScript) " + err.toString()));
        err.$javaException = ex;
        err[$rt_exceptionTag] = true;
        ex.$jsException = err;
        $rt_fillStack(err, ex);
    }
    return ex;
}
function $dbg_class(obj) {
    var cls = obj.constructor;
    var arrayDegree = 0;
    while (cls.$meta && cls.$meta.item) {
        ++arrayDegree;
        cls = cls.$meta.item;
    }
    var clsName = "";
    if (cls === $rt_booleancls()) {
        clsName = "boolean";
    } else if (cls === $rt_bytecls()) {
        clsName = "byte";
    } else if (cls === $rt_shortcls()) {
        clsName = "short";
    } else if (cls === $rt_charcls()) {
        clsName = "char";
    } else if (cls === $rt_intcls()) {
        clsName = "int";
    } else if (cls === $rt_longcls()) {
        clsName = "long";
    } else if (cls === $rt_floatcls()) {
        clsName = "float";
    } else if (cls === $rt_doublecls()) {
        clsName = "double";
    } else {
        clsName = cls.$meta ? cls.$meta.name || "a/" + cls.name : "@" + cls.name;
    }
    while (arrayDegree-- > 0) {
        clsName += "[]";
    }
    return clsName;
}
function Long(lo, hi) {
    this.lo = lo | 0;
    this.hi = hi | 0;
}
Long.prototype.__teavm_class__ = function() {
    return "long";
};
Long.prototype.toString = function() {
    var result = [];
    var n = this;
    var positive = Long_isPositive(n);
    if (!positive) {
        n = Long_neg(n);
    }
    var radix = new Long(10, 0);
    do  {
        var divRem = Long_divRem(n, radix);
        result.push(String.fromCharCode(48 + divRem[1].lo));
        n = divRem[0];
    }while (n.lo !== 0 || n.hi !== 0);
    result = (result.reverse()).join('');
    return positive ? result : "-" + result;
};
Long.prototype.valueOf = function() {
    return Long_toNumber(this);
};
var Long_ZERO = new Long(0, 0);
var Long_MAX_NORMAL = 1 << 18;
function Long_fromInt(val) {
    return new Long(val,  -(val < 0) | 0);
}
function Long_fromNumber(val) {
    if (val >= 0) {
        return new Long(val | 0, val / 0x100000000 | 0);
    } else {
        return Long_neg(new Long( -val | 0,  -val / 0x100000000 | 0));
    }
}
function Long_toNumber(val) {
    return 0x100000000 * val.hi + (val.lo >>> 0);
}
var $rt_imul = Math.imul || function(a, b) {
    var ah = a >>> 16 & 0xFFFF;
    var al = a & 0xFFFF;
    var bh = b >>> 16 & 0xFFFF;
    var bl = b & 0xFFFF;
    return al * bl + (ah * bl + al * bh << 16 >>> 0) | 0;
};
var $rt_udiv = function(a, b) {
    return (a >>> 0) / (b >>> 0) >>> 0;
};
var $rt_umod = function(a, b) {
    return (a >>> 0) % (b >>> 0) >>> 0;
};
function $rt_checkBounds(index, array) {
    if (index < 0 || index >= array.length) {
        $rt_throwAIOOBE();
    }
    return index;
}
function $rt_checkUpperBound(index, array) {
    if (index >= array.length) {
        $rt_throwAIOOBE();
    }
    return index;
}
function $rt_checkLowerBound(index) {
    if (index < 0) {
        $rt_throwAIOOBE();
    }
    return index;
}
function $rt_classWithoutFields(superclass) {
    if (superclass === 0) {
        return function() {
        };
    }
    if (superclass === void 0) {
        superclass = $rt_objcls();
    }
    return function() {
        superclass.call(this);
    };
}
function $rt_setCloneMethod(target, f) {
    target.$clone = f;
}
function $rt_cls(cls) {
    return jl_Class_getClass(cls);
}
function $rt_str(str) {
    if (str === null) {
        return null;
    }
    var characters = $rt_createCharArray(str.length);
    var charsBuffer = characters.data;
    for (var i = 0; i < str.length; i = (i + 1) | 0) {
        charsBuffer[i] = str.charCodeAt(i) & 0xFFFF;
    }
    return jl_String__init_(characters);
}
function $rt_ustr(str) {
    if (str === null) {
        return null;
    }
    var data = str.$characters.data;
    var result = "";
    for (var i = 0; i < data.length; i = (i + 1) | 0) {
        result += String.fromCharCode(data[i]);
    }
    return result;
}
function $rt_objcls() { return jl_Object; }
function $rt_nullCheck(val) {
    if (val === null) {
        $rt_throw(jl_NullPointerException__init_());
    }
    return val;
}
function $rt_intern(str) {
    return str;
}
function $rt_getThread() {
    return null;
}
function $rt_setThread(t) {
}
function $rt_createException(message) {
    return jl_RuntimeException__init_(message);
}
function $rt_createStackElement(className, methodName, fileName, lineNumber) {
    return null;
}
function $rt_setStack(e, stack) {
}
function $rt_throwAIOOBE() {
    $rt_throw(jl_ArrayIndexOutOfBoundsException__init_());
}
var $java = Object.create(null);
function jl_Object() {
    this.$id$ = 0;
}
function jl_Object__init_() {
    var var_0 = new jl_Object();
    jl_Object__init_0(var_0);
    return var_0;
}
function jl_Object__init_0($this) {}
function jl_Object_getClass($this) {
    return jl_Class_getClass($this.constructor);
}
function jl_Object_toString($this) {
    return $rt_nullCheck($rt_nullCheck($rt_nullCheck((jl_StringBuilder__init_()).$append($rt_nullCheck(jl_Object_getClass($this)).$getName())).$append($rt_s(0))).$append(jl_Integer_toHexString(jl_Object_identity($this)))).$toString();
}
function jl_Object_identity($this) {
    var $platformThis, var$2;
    $platformThis = $this;
    if (!$platformThis.$id$) {
        var$2 = $rt_nextId();
        $platformThis.$id$ = var$2;
    }
    return $this.$id$;
}
function jl_Object_clone($this) {
    var var$1, $result, var$3;
    if (!$rt_isInstance($this, jl_Cloneable)) {
        var$1 = $this;
        if (var$1.constructor.$meta.item === null)
            $rt_throw(jl_CloneNotSupportedException__init_());
    }
    $result = otp_Platform_clone($this);
    var$1 = $result;
    var$3 = $rt_nextId();
    var$1.$id$ = var$3;
    return $result;
}
function jl_Throwable() {
    var a = this; jl_Object.call(a);
    a.$message = null;
    a.$suppressionEnabled = 0;
    a.$writableStackTrace = 0;
}
function jl_Throwable__init_() {
    var var_0 = new jl_Throwable();
    jl_Throwable__init_0(var_0);
    return var_0;
}
function jl_Throwable__init_1(var_0) {
    var var_1 = new jl_Throwable();
    jl_Throwable__init_2(var_1, var_0);
    return var_1;
}
function jl_Throwable__init_0($this) {
    $this.$suppressionEnabled = 1;
    $this.$writableStackTrace = 1;
    $this.$fillInStackTrace();
}
function jl_Throwable__init_2($this, $message) {
    $this.$suppressionEnabled = 1;
    $this.$writableStackTrace = 1;
    $this.$fillInStackTrace();
    $this.$message = $message;
}
function jl_Throwable_fillInStackTrace($this) {
    return $this;
}
var jl_Exception = $rt_classWithoutFields(jl_Throwable);
function jl_Exception__init_() {
    var var_0 = new jl_Exception();
    jl_Exception__init_0(var_0);
    return var_0;
}
function jl_Exception__init_1(var_0) {
    var var_1 = new jl_Exception();
    jl_Exception__init_2(var_1, var_0);
    return var_1;
}
function jl_Exception__init_0($this) {
    jl_Throwable__init_0($this);
}
function jl_Exception__init_2($this, $message) {
    jl_Throwable__init_2($this, $message);
}
var jl_RuntimeException = $rt_classWithoutFields(jl_Exception);
function jl_RuntimeException__init_0() {
    var var_0 = new jl_RuntimeException();
    jl_RuntimeException__init_1(var_0);
    return var_0;
}
function jl_RuntimeException__init_(var_0) {
    var var_1 = new jl_RuntimeException();
    jl_RuntimeException__init_2(var_1, var_0);
    return var_1;
}
function jl_RuntimeException__init_1($this) {
    jl_Exception__init_0($this);
}
function jl_RuntimeException__init_2($this, $message) {
    jl_Exception__init_2($this, $message);
}
var jl_IndexOutOfBoundsException = $rt_classWithoutFields(jl_RuntimeException);
function jl_IndexOutOfBoundsException__init_() {
    var var_0 = new jl_IndexOutOfBoundsException();
    jl_IndexOutOfBoundsException__init_0(var_0);
    return var_0;
}
function jl_IndexOutOfBoundsException__init_0($this) {
    jl_RuntimeException__init_1($this);
}
var ju_Arrays = $rt_classWithoutFields();
function ju_Arrays_copyOf($array, $length) {
    var $result, var$4, $sz, $i, var$7, var$8;
    $result = $rt_createCharArray($length);
    $array = $rt_nullCheck($array);
    var$4 = $array.data;
    $sz = jl_Math_min($length, var$4.length);
    $i = 0;
    while ($i < $sz) {
        var$7 = $result.data;
        $i = $rt_checkBounds($i, var$4);
        var$8 = var$4[$i];
        $i = $rt_checkUpperBound($i, var$7);
        var$7[$i] = var$8;
        $i = $i + 1 | 0;
    }
    return $result;
}
var otj_JSObject = $rt_classWithoutFields(0);
var otjb_TimerHandler = $rt_classWithoutFields(0);
function ovegcc_WebSocket$sendMessage$static$lambda$_7_0() {
    var a = this; jl_Object.call(a);
    a.$_0 = null;
    a.$_1 = null;
}
function ovegcc_WebSocket$sendMessage$static$lambda$_7_0__init_(var_0, var_1) {
    var var_2 = new ovegcc_WebSocket$sendMessage$static$lambda$_7_0();
    ovegcc_WebSocket$sendMessage$static$lambda$_7_0__init_0(var_2, var_0, var_1);
    return var_2;
}
function ovegcc_WebSocket$sendMessage$static$lambda$_7_0__init_0(var$0, var$1, var$2) {
    jl_Object__init_0(var$0);
    var$0.$_0 = var$1;
    var$0.$_1 = var$2;
}
function ovegcc_WebSocket$sendMessage$static$lambda$_7_0_onTimer(var$0) {
    ovegcc_WebSocket_lambda$sendMessage$0$static(var$0.$_0, var$0.$_1);
}
function ovegcc_WebSocket$sendMessage$static$lambda$_7_0_onTimer$exported$0(var$0) {
    var$0.$onTimer();
}
var ji_Serializable = $rt_classWithoutFields(0);
var jl_Number = $rt_classWithoutFields();
var jl_Comparable = $rt_classWithoutFields(0);
var jl_Integer = $rt_classWithoutFields(jl_Number);
var jl_Integer_TYPE = null;
function jl_Integer_$callClinit() {
    jl_Integer_$callClinit = $rt_eraseClinit(jl_Integer);
    jl_Integer__clinit_();
}
function jl_Integer_toHexString($i) {
    jl_Integer_$callClinit();
    return otci_IntegerUtil_toUnsignedLogRadixString($i, 4);
}
function jl_Integer_numberOfLeadingZeros($i) {
    var $n, var$3, var$4;
    jl_Integer_$callClinit();
    if (!$i)
        return 32;
    $n = 0;
    var$3 = $i >>> 16;
    if (var$3)
        $n = 16;
    else
        var$3 = $i;
    var$4 = var$3 >>> 8;
    if (!var$4)
        var$4 = var$3;
    else
        $n = $n | 8;
    var$3 = var$4 >>> 4;
    if (!var$3)
        var$3 = var$4;
    else
        $n = $n | 4;
    var$4 = var$3 >>> 2;
    if (!var$4)
        var$4 = var$3;
    else
        $n = $n | 2;
    if (var$4 >>> 1)
        $n = $n | 1;
    return (32 - $n | 0) - 1 | 0;
}
function jl_Integer__clinit_() {
    jl_Integer_TYPE = $rt_cls($rt_intcls());
}
var jl_NullPointerException = $rt_classWithoutFields(jl_RuntimeException);
function jl_NullPointerException__init_() {
    var var_0 = new jl_NullPointerException();
    jl_NullPointerException__init_0(var_0);
    return var_0;
}
function jl_NullPointerException__init_0($this) {
    jl_RuntimeException__init_1($this);
}
var jl_CloneNotSupportedException = $rt_classWithoutFields(jl_Exception);
function jl_CloneNotSupportedException__init_() {
    var var_0 = new jl_CloneNotSupportedException();
    jl_CloneNotSupportedException__init_0(var_0);
    return var_0;
}
function jl_CloneNotSupportedException__init_0($this) {
    jl_Exception__init_0($this);
}
var jl_Error = $rt_classWithoutFields(jl_Throwable);
function jl_Error__init_(var_0) {
    var var_1 = new jl_Error();
    jl_Error__init_0(var_1, var_0);
    return var_1;
}
function jl_Error__init_0($this, $message) {
    jl_Throwable__init_2($this, $message);
}
var jl_LinkageError = $rt_classWithoutFields(jl_Error);
function jl_LinkageError__init_(var_0) {
    var var_1 = new jl_LinkageError();
    jl_LinkageError__init_0(var_1, var_0);
    return var_1;
}
function jl_LinkageError__init_0($this, $message) {
    jl_Error__init_0($this, $message);
}
var jl_IncompatibleClassChangeError = $rt_classWithoutFields(jl_LinkageError);
function jl_IncompatibleClassChangeError__init_(var_0) {
    var var_1 = new jl_IncompatibleClassChangeError();
    jl_IncompatibleClassChangeError__init_0(var_1, var_0);
    return var_1;
}
function jl_IncompatibleClassChangeError__init_0($this, $message) {
    jl_LinkageError__init_0($this, $message);
}
var jl_NoSuchFieldError = $rt_classWithoutFields(jl_IncompatibleClassChangeError);
function jl_NoSuchFieldError__init_(var_0) {
    var var_1 = new jl_NoSuchFieldError();
    jl_NoSuchFieldError__init_0(var_1, var_0);
    return var_1;
}
function jl_NoSuchFieldError__init_0($this, $message) {
    jl_IncompatibleClassChangeError__init_0($this, $message);
}
var ovegcc_OnMessageHandler = $rt_classWithoutFields(0);
var jl_Character = $rt_classWithoutFields();
var jl_Character_TYPE = null;
var jl_Character_characterCache = null;
function jl_Character_$callClinit() {
    jl_Character_$callClinit = $rt_eraseClinit(jl_Character);
    jl_Character__clinit_();
}
function jl_Character_forDigit($digit, $radix) {
    jl_Character_$callClinit();
    if ($radix >= 2 && $radix <= 36 && $digit < $radix)
        return $digit < 10 ? (48 + $digit | 0) & 65535 : ((97 + $digit | 0) - 10 | 0) & 65535;
    return 0;
}
function jl_Character__clinit_() {
    jl_Character_TYPE = $rt_cls($rt_charcls());
    jl_Character_characterCache = $rt_createArray(jl_Character, 128);
}
function jl_Enum() {
    var a = this; jl_Object.call(a);
    a.$name = null;
    a.$ordinal = 0;
}
function jl_Enum__init_($this, $name, $ordinal) {
    jl_Object__init_0($this);
    $this.$name = $name;
    $this.$ordinal = $ordinal;
}
function jl_Enum_name($this) {
    return $this.$name;
}
function jl_Enum_ordinal($this) {
    return $this.$ordinal;
}
function jl_Enum_valueOf($enumType, $name) {
    var $constants, var$4, var$5, var$6, var$7, $constant;
    $enumType = $rt_nullCheck($enumType);
    $constants = $enumType.$getEnumConstants();
    if ($constants === null)
        $rt_throw(jl_IllegalArgumentException__init_($rt_s(1)));
    var$4 = $constants.data;
    var$5 = var$4.length;
    var$6 = 0;
    while (true) {
        if (var$6 >= var$5)
            $rt_throw(jl_IllegalArgumentException__init_($rt_nullCheck($rt_nullCheck($rt_nullCheck($rt_nullCheck($rt_nullCheck((jl_StringBuilder__init_()).$append($rt_s(2))).$append0($enumType)).$append($rt_s(3))).$append0($name)).$append($rt_s(4))).$toString()));
        var$7 = $rt_checkLowerBound(var$6);
        $constant = var$4[var$7];
        $constant = $rt_nullCheck($constant);
        if ($rt_nullCheck(jl_Enum_name($constant)).$equals($name))
            break;
        var$6 = var$7 + 1 | 0;
    }
    return $constant;
}
var otci_IntegerUtil = $rt_classWithoutFields();
function otci_IntegerUtil_toUnsignedLogRadixString($value, $radixLog2) {
    var $radix, $mask, $sz, $chars, $pos, $target, var$9, $target_0, var$11;
    if (!$value)
        return $rt_s(5);
    $radix = 1 << $radixLog2;
    $mask = $radix - 1 | 0;
    $sz = (((32 - jl_Integer_numberOfLeadingZeros($value) | 0) + $radixLog2 | 0) - 1 | 0) / $radixLog2 | 0;
    $chars = $rt_createCharArray($sz);
    $pos = $rt_imul($sz - 1 | 0, $radixLog2);
    $target = 0;
    while ($pos >= 0) {
        var$9 = $chars.data;
        $target_0 = $target + 1 | 0;
        var$11 = jl_Character_forDigit($value >>> $pos & $mask, $radix);
        $target = $rt_checkBounds($target, var$9);
        var$9[$target] = var$11;
        $pos = $pos - $radixLog2 | 0;
        $target = $target_0;
    }
    return jl_String__init_($chars);
}
var jl_Math = $rt_classWithoutFields();
function jl_Math_min($a, $b) {
    if ($a < $b)
        $b = $a;
    return $b;
}
function jl_Math_max($a, $b) {
    if ($a > $b)
        $b = $a;
    return $b;
}
var otjc_JSNumber = $rt_classWithoutFields();
function otjc_JSNumber_intValue$static($this) {
    return $this;
}
function ovegcc_Communicator() {
    var a = this; jl_Object.call(a);
    a.$webSocket = null;
    a.$order = 0;
}
function ovegcc_Communicator__init_(var_0) {
    var var_1 = new ovegcc_Communicator();
    ovegcc_Communicator__init_0(var_1, var_0);
    return var_1;
}
function ovegcc_Communicator__init_0($this, $onMessageHandler) {
    var var$2;
    jl_Object__init_0($this);
    $this.$order = 0;
    var$2 = jl_StringBuilder__init_();
    var$2 = $rt_nullCheck($rt_nullCheck(var$2.$append($rt_str(window.location.href))).$append($rt_s(6))).$toString();
    $this.$webSocket = ovegcc_WebSocket_connect(var$2, $onMessageHandler);
}
function ovegcc_Communicator_sendPlayerCommand($this, $uuid, $direction) {
    var $playerCommand, var$4, var$5, var$6;
    $playerCommand = otjc_JSObjects_create$js_body$_2();
    $direction = $rt_nullCheck($direction);
    var$4 = jl_Enum_ordinal($direction);
    $playerCommand.direction = var$4;
    var$5 = $this.$order;
    $this.$order = var$5 + 1 | 0;
    var$6 = var$5;
    $playerCommand.order = var$6;
    var$4 = $rt_ustr($uuid);
    $playerCommand.uuid = var$4;
    ovegcc_WebSocket_sendMessage$static($this.$webSocket, $playerCommand);
}
var otjc_JSObjects = $rt_classWithoutFields();
function otjc_JSObjects_create$js_body$_2() {
    return {  };
}
var otjde_EventTarget = $rt_classWithoutFields(0);
var otjde_GamepadEventTarget = $rt_classWithoutFields(0);
var jl_Cloneable = $rt_classWithoutFields(0);
var otji_JS = $rt_classWithoutFields();
function otji_JS_function(var$1, var$2) {
    var name = 'jso$functor$' + var$2;
    if (!var$1[name]) {
        var fn = function() {
            return var$1[var$2].apply(var$1, arguments);
        };
        var$1[name] = function() {
            return fn;
        };
    }
    return var$1[name]();
}
function otji_JS_functionAsObject(var$1, var$2) {
    if (typeof var$1 !== "function") return var$1;
    var result = {};
    result[var$2] = var$1;
    return result;
}
var jl_CharSequence = $rt_classWithoutFields(0);
var otjde_LoadEventTarget = $rt_classWithoutFields(0);
function ovegc_GameCanvas() {
    var a = this; jl_Object.call(a);
    a.$document = null;
    a.$canvas = null;
    a.$context = null;
    a.$width = 0;
    a.$height = 0;
}
function ovegc_GameCanvas__init_() {
    var var_0 = new ovegc_GameCanvas();
    ovegc_GameCanvas__init_0(var_0);
    return var_0;
}
function ovegc_GameCanvas__init_0($this) {
    jl_Object__init_0($this);
    $this.$document = window.document;
    $this.$canvas = $this.$document.getElementById("canvas");
    $this.$context = $this.$canvas.getContext("2d");
    $this.$width = $this.$canvas.width;
    $this.$height = $this.$canvas.height;
}
function ovegc_GameCanvas_clear($this) {
    var var$1, var$2, var$3;
    var$1 = $this.$context;
    var$2 = $this.$width;
    var$3 = $this.$height;
    var$1.clearRect(0.0, 0.0, var$2, var$3);
}
function ovegc_GameCanvas_drawPlayer($this, $x, $y) {
    var var$3, var$4, var$5, var$6;
    var$3 = $this.$context;
    var$4 = "black";
    var$3.fillStyle = var$4;
    var$4 = $this.$context;
    var$5 = $x;
    var$6 = $y;
    var$4.fillRect(var$5, var$6, 32.0, 32.0);
}
function ovegc_GameCanvas_drawTileMap($this, $tileMap) {
    var var$2, var$3, var$4, var$5, $rowTiles, var$7, var$8, var$9, $tile, var$11, $color;
    $tileMap = $rt_nullCheck($tileMap);
    var$2 = $rt_nullCheck($tileMap.$getTiles()).data;
    var$3 = var$2.length;
    var$4 = 0;
    while (var$4 < var$3) {
        var$5 = $rt_checkLowerBound(var$4);
        $rowTiles = var$2[var$5];
        $rowTiles = $rt_nullCheck($rowTiles);
        var$7 = $rowTiles.data;
        var$8 = var$7.length;
        var$4 = 0;
        while (var$4 < var$8) {
            var$9 = $rt_checkLowerBound(var$4);
            $tile = var$7[var$9];
            $tile = $rt_nullCheck($tile);
            var$11 = $tile.$getTileType();
            ovegs_TileType_$callClinit();
            $color = var$11 !== ovegs_TileType_GROUND ? $rt_s(7) : $rt_s(8);
            $this.$drawTile($tile.$getX(), $tile.$getY(), $tile.$getWidth(), $tile.$getHeight(), $color);
            var$4 = var$9 + 1 | 0;
        }
        var$4 = var$5 + 1 | 0;
    }
}
function ovegc_GameCanvas_drawCollision($this, $tileJson, $direction) {
    var $point, $x, $y, $w, $h, var$8, var$9;
    a: {
        $point = $tileJson.topLeftCorner;
        $x = $point.x;
        $y = $point.y;
        $w = 32.0;
        $h = 32.0;
        $this.$drawTile($x, $y, $w, $h, $rt_s(9));
        ovegc_GameCanvas$1_$callClinit();
        var$8 = ovegc_GameCanvas$1_$SwitchMap$org$vaadin$erik$game$shared$Direction;
        $direction = $rt_nullCheck($direction);
        var$9 = jl_Enum_ordinal($direction);
        var$8 = $rt_nullCheck(var$8).data;
        switch (var$8[$rt_checkBounds(var$9, var$8)]) {
            case 1:
                break;
            case 2:
                $this.$drawTile($x, $y + $h - 5.0, $w, 5.0, $rt_s(10));
                break a;
            case 3:
                $this.$drawTile($x, $y, 5.0, $h, $rt_s(10));
                break a;
            case 4:
                $this.$drawTile($x + $w - 5.0, $y, 5.0, $h, $rt_s(10));
                break a;
            default:
                break a;
        }
        $this.$drawTile($x, $y, $w, 5.0, $rt_s(10));
    }
}
function ovegc_GameCanvas_drawTile($this, $x, $y, $w, $h, $color) {
    var var$6, var$7;
    var$6 = $this.$context;
    var$7 = $rt_ustr($color);
    var$6.fillStyle = var$7;
    $this.$context.fillRect($x, $y, $w, $h);
}
var jl_StringIndexOutOfBoundsException = $rt_classWithoutFields(jl_IndexOutOfBoundsException);
function jl_StringIndexOutOfBoundsException__init_() {
    var var_0 = new jl_StringIndexOutOfBoundsException();
    jl_StringIndexOutOfBoundsException__init_0(var_0);
    return var_0;
}
function jl_StringIndexOutOfBoundsException__init_0($this) {
    jl_IndexOutOfBoundsException__init_0($this);
}
var ovegc_GameCanvas$1 = $rt_classWithoutFields();
var ovegc_GameCanvas$1_$SwitchMap$org$vaadin$erik$game$shared$Direction = null;
function ovegc_GameCanvas$1_$callClinit() {
    ovegc_GameCanvas$1_$callClinit = $rt_eraseClinit(ovegc_GameCanvas$1);
    ovegc_GameCanvas$1__clinit_();
}
function ovegc_GameCanvas$1__clinit_() {
    var var$1, var$2, var$3;
    ovegc_GameCanvas$1_$SwitchMap$org$vaadin$erik$game$shared$Direction = $rt_createIntArray($rt_nullCheck(ovegs_Direction_values()).data.length);
    var$1 = ovegc_GameCanvas$1_$SwitchMap$org$vaadin$erik$game$shared$Direction;
    var$2 = jl_Enum_ordinal($rt_nullCheck(ovegs_Direction_UP));
    var$1 = $rt_nullCheck(var$1).data;
    var$1[$rt_checkBounds(var$2, var$1)] = 1;
    var$1 = ovegc_GameCanvas$1_$SwitchMap$org$vaadin$erik$game$shared$Direction;
    var$3 = jl_Enum_ordinal($rt_nullCheck(ovegs_Direction_DOWN));
    var$1 = $rt_nullCheck(var$1).data;
    var$1[$rt_checkBounds(var$3, var$1)] = 2;
    var$1 = ovegc_GameCanvas$1_$SwitchMap$org$vaadin$erik$game$shared$Direction;
    var$3 = jl_Enum_ordinal($rt_nullCheck(ovegs_Direction_LEFT));
    var$1 = $rt_nullCheck(var$1).data;
    var$1[$rt_checkBounds(var$3, var$1)] = 3;
    var$1 = ovegc_GameCanvas$1_$SwitchMap$org$vaadin$erik$game$shared$Direction;
    var$3 = jl_Enum_ordinal($rt_nullCheck(ovegs_Direction_RIGHT));
    var$1 = $rt_nullCheck(var$1).data;
    var$1[$rt_checkBounds(var$3, var$1)] = 4;
}
var jlr_Type = $rt_classWithoutFields(0);
var ovegcc_WebSocket = $rt_classWithoutFields();
function ovegcc_WebSocket_connect($url, $onMessageHandler) {
    var $request, var$4;
    $request = otjc_JSObjects_create$js_body$_2();
    var$4 = "websocket";
    $request.transport = var$4;
    var$4 = $rt_ustr($url);
    $request.url = var$4;
    var$4 = otji_JS_function($onMessageHandler, "onMessage");
    $request.onMessage = var$4;
    return window.vaadinPush.atmosphere.subscribe($request);
}
function ovegcc_WebSocket_sendMessage$static($this, $message) {
    var $messageString;
    $messageString = $rt_str(JSON.stringify($message));
    setTimeout(otji_JS_function(ovegcc_WebSocket$sendMessage$static$lambda$_7_0__init_($this, $messageString), "onTimer"), 100);
}
function ovegcc_WebSocket_lambda$sendMessage$0$static($this, $messageString) {
    $this.push($rt_ustr($messageString));
    console.warn("Message sent from TeaVM...");
}
var ovegs_Direction = $rt_classWithoutFields(jl_Enum);
var ovegs_Direction_UP = null;
var ovegs_Direction_DOWN = null;
var ovegs_Direction_LEFT = null;
var ovegs_Direction_RIGHT = null;
var ovegs_Direction_$VALUES = null;
function ovegs_Direction_$callClinit() {
    ovegs_Direction_$callClinit = $rt_eraseClinit(ovegs_Direction);
    ovegs_Direction__clinit_();
}
function ovegs_Direction__init_(var_0, var_1) {
    var var_2 = new ovegs_Direction();
    ovegs_Direction__init_0(var_2, var_0, var_1);
    return var_2;
}
function ovegs_Direction_values() {
    ovegs_Direction_$callClinit();
    return $rt_nullCheck(ovegs_Direction_$VALUES).$clone();
}
function ovegs_Direction_valueOf($name) {
    ovegs_Direction_$callClinit();
    return jl_Enum_valueOf($rt_cls(ovegs_Direction), $name);
}
function ovegs_Direction__init_0($this, var$1, var$2) {
    ovegs_Direction_$callClinit();
    jl_Enum__init_($this, var$1, var$2);
}
function ovegs_Direction__clinit_() {
    ovegs_Direction_UP = ovegs_Direction__init_($rt_s(11), 0);
    ovegs_Direction_DOWN = ovegs_Direction__init_($rt_s(12), 1);
    ovegs_Direction_LEFT = ovegs_Direction__init_($rt_s(13), 2);
    ovegs_Direction_RIGHT = ovegs_Direction__init_($rt_s(14), 3);
    ovegs_Direction_$VALUES = $rt_createArrayFromData(ovegs_Direction, [ovegs_Direction_UP, ovegs_Direction_DOWN, ovegs_Direction_LEFT, ovegs_Direction_RIGHT]);
}
function jl_AbstractStringBuilder() {
    var a = this; jl_Object.call(a);
    a.$buffer = null;
    a.$length = 0;
}
function jl_AbstractStringBuilder__init_() {
    var var_0 = new jl_AbstractStringBuilder();
    jl_AbstractStringBuilder__init_0(var_0);
    return var_0;
}
function jl_AbstractStringBuilder__init_1(var_0) {
    var var_1 = new jl_AbstractStringBuilder();
    jl_AbstractStringBuilder__init_2(var_1, var_0);
    return var_1;
}
function jl_AbstractStringBuilder__init_0($this) {
    jl_AbstractStringBuilder__init_2($this, 16);
}
function jl_AbstractStringBuilder__init_2($this, $capacity) {
    jl_Object__init_0($this);
    $this.$buffer = $rt_createCharArray($capacity);
}
function jl_AbstractStringBuilder_append($this, $string) {
    return $this.$insert($this.$length, $string);
}
function jl_AbstractStringBuilder_insert($this, $index, $string) {
    var $i, var$4, var$5, var$6, var$7;
    if ($index >= 0 && $index <= $this.$length) {
        if ($string === null)
            $string = $rt_s(15);
        else if ($string.$isEmpty())
            return $this;
        $this.$ensureCapacity($this.$length + $string.$length0() | 0);
        $i = $this.$length - 1 | 0;
        while ($i >= $index) {
            var$4 = $this.$buffer;
            var$5 = $i + $string.$length0() | 0;
            var$6 = $rt_nullCheck($this.$buffer).data;
            $i = $rt_checkBounds($i, var$6);
            var$7 = var$6[$i];
            var$4 = $rt_nullCheck(var$4).data;
            var$4[$rt_checkBounds(var$5, var$4)] = var$7;
            $i = $i + (-1) | 0;
        }
        $this.$length = $this.$length + $string.$length0() | 0;
        $i = 0;
        while ($i < $string.$length0()) {
            var$4 = $this.$buffer;
            var$5 = $index + 1 | 0;
            var$7 = $string.$charAt($i);
            var$4 = $rt_nullCheck(var$4).data;
            var$4[$rt_checkBounds($index, var$4)] = var$7;
            $i = $i + 1 | 0;
            $index = var$5;
        }
        return $this;
    }
    $rt_throw(jl_StringIndexOutOfBoundsException__init_());
}
function jl_AbstractStringBuilder_append0($this, $obj) {
    return $this.$insert0($this.$length, $obj);
}
function jl_AbstractStringBuilder_insert0($this, $index, $obj) {
    return $this.$insert($index, $obj === null ? $rt_s(15) : $obj.$toString());
}
function jl_AbstractStringBuilder_ensureCapacity($this, $capacity) {
    var $newLength;
    if ($rt_nullCheck($this.$buffer).data.length >= $capacity)
        return;
    $newLength = $rt_nullCheck($this.$buffer).data.length >= 1073741823 ? 2147483647 : jl_Math_max($capacity, jl_Math_max($rt_nullCheck($this.$buffer).data.length * 2 | 0, 5));
    $this.$buffer = ju_Arrays_copyOf($this.$buffer, $newLength);
}
function jl_AbstractStringBuilder_toString($this) {
    return jl_String__init_0($this.$buffer, 0, $this.$length);
}
var jl_Appendable = $rt_classWithoutFields(0);
var jl_StringBuilder = $rt_classWithoutFields(jl_AbstractStringBuilder);
function jl_StringBuilder__init_() {
    var var_0 = new jl_StringBuilder();
    jl_StringBuilder__init_0(var_0);
    return var_0;
}
function jl_StringBuilder__init_0($this) {
    jl_AbstractStringBuilder__init_0($this);
}
function jl_StringBuilder_append($this, $string) {
    jl_AbstractStringBuilder_append($this, $string);
    return $this;
}
function jl_StringBuilder_append0($this, $obj) {
    jl_AbstractStringBuilder_append0($this, $obj);
    return $this;
}
function jl_StringBuilder_insert($this, $index, $obj) {
    jl_AbstractStringBuilder_insert0($this, $index, $obj);
    return $this;
}
function jl_StringBuilder_insert0($this, $index, $string) {
    jl_AbstractStringBuilder_insert($this, $index, $string);
    return $this;
}
function jl_StringBuilder_toString($this) {
    return jl_AbstractStringBuilder_toString($this);
}
function jl_StringBuilder_ensureCapacity($this, var$1) {
    jl_AbstractStringBuilder_ensureCapacity($this, var$1);
}
function jl_StringBuilder_insert1($this, var$1, var$2) {
    return $this.$insert1(var$1, var$2);
}
function jl_StringBuilder_insert2($this, var$1, var$2) {
    return $this.$insert2(var$1, var$2);
}
var jlr_AnnotatedElement = $rt_classWithoutFields(0);
function ovegc_GameClient() {
    var a = this; jl_Object.call(a);
    a.$document0 = null;
    a.$communicator = null;
    a.$gameCanvas = null;
    a.$tileMap = null;
    a.$playerUuid = null;
}
function ovegc_GameClient__init_() {
    var var_0 = new ovegc_GameClient();
    ovegc_GameClient__init_0(var_0);
    return var_0;
}
function ovegc_GameClient_main($args) {
    ovegc_GameClient_start(ovegc_GameClient__init_());
}
function ovegc_GameClient__init_0($this) {
    jl_Object__init_0($this);
    $this.$document0 = window.document;
    $this.$communicator = ovegcc_Communicator__init_(ovegc_GameClient$_init_$lambda$_1_0__init_($this));
    $this.$gameCanvas = ovegc_GameCanvas__init_();
    $this.$tileMap = ovegct_TileMap__init_();
}
function ovegc_GameClient_start($this) {
    otjde_KeyboardEventTarget_listenKeyDown$static($this.$document0.body, ovegc_GameClient$start$lambda$_2_0__init_($this));
}
function ovegc_GameClient_onMessageReceived($this, $event) {
    var $object, $snapshot, var$4, var$5, $i, $player, var$8, var$9, $j, $collision, var$12;
    console.warn("Message received in the TeaVM GameClient");
    $object = JSON.parse($rt_ustr($rt_str($event.responseBody)));
    if ("uuid" in $object ? 1 : 0) {
        $this.$playerUuid = $rt_str($object.uuid);
        return;
    }
    $snapshot = JSON.parse($rt_ustr($rt_str($event.responseBody)));
    $rt_nullCheck($this.$gameCanvas).$clear();
    var$4 = $this.$gameCanvas;
    var$5 = $this.$tileMap;
    $rt_nullCheck(var$4).$drawTileMap(var$5);
    $i = 0;
    while ($i < $snapshot.players.length) {
        $player = $snapshot.players[$i];
        var$5 = $this.$gameCanvas;
        var$8 = otjc_JSNumber_intValue$static($player.x);
        var$9 = otjc_JSNumber_intValue$static($player.y);
        $rt_nullCheck(var$5).$drawPlayer(var$8, var$9);
        $j = 0;
        while ($j < $player.tileCollisions.length) {
            $collision = $player.tileCollisions[$j];
            var$5 = $this.$gameCanvas;
            var$4 = $collision.tile;
            var$12 = ovegccj_TileCollisionJson_getFromDirection$static($collision);
            $rt_nullCheck(var$5).$drawCollision(var$4, var$12);
            $j = $j + 1 | 0;
        }
        $i = $i + 1 | 0;
    }
}
function ovegc_GameClient_lambda$start$0($this, $event) {
    var $direction, var$3, var$4, var$5;
    a: {
        console.warn("Keydown!");
        $direction = null;
        var$3 = $rt_str($event.code);
        var$4 = (-1);
        var$5 = $rt_nullCheck(var$3);
        switch (var$5.$hashCode()) {
            case 251549619:
                if (!var$5.$equals($rt_s(16)))
                    break a;
                var$4 = 2;
                break a;
            case 930625636:
                if (!var$5.$equals($rt_s(17)))
                    break a;
                var$4 = 0;
                break a;
            case 977763216:
                if (!var$5.$equals($rt_s(18)))
                    break a;
                var$4 = 1;
                break a;
            default:
        }
    }
    b: {
        switch (var$4) {
            case 0:
                ovegs_Direction_$callClinit();
                $direction = ovegs_Direction_UP;
                break b;
            case 1:
                ovegs_Direction_$callClinit();
                $direction = ovegs_Direction_LEFT;
                break b;
            case 2:
                ovegs_Direction_$callClinit();
                $direction = ovegs_Direction_RIGHT;
                break b;
            default:
        }
    }
    if ($direction !== null)
        $rt_nullCheck($this.$communicator).$sendPlayerCommand($this.$playerUuid, $direction);
}
var otjde_EventListener = $rt_classWithoutFields(0);
var ovegccj_TileCollisionJson = $rt_classWithoutFields();
function ovegccj_TileCollisionJson_getFromDirection$static($this) {
    return ovegs_Direction_valueOf($rt_str($this.fromDirection));
}
var otjde_FocusEventTarget = $rt_classWithoutFields(0);
var otjde_MouseEventTarget = $rt_classWithoutFields(0);
var otjde_KeyboardEventTarget = $rt_classWithoutFields(0);
function otjde_KeyboardEventTarget_listenKeyDown$static($this, $listener) {
    $this.addEventListener("keydown", otji_JS_function($listener, "handleEvent"));
}
var otjb_WindowEventTarget = $rt_classWithoutFields(0);
var ovegct_TileMapService = $rt_classWithoutFields();
var ovegs_HasPosition = $rt_classWithoutFields(0);
function ovegs_Tile() {
    var a = this; jl_Object.call(a);
    a.$topLeftCorner = null;
    a.$tileType = null;
}
function ovegs_Tile__init_(var_0, var_1) {
    var var_2 = new ovegs_Tile();
    ovegs_Tile__init_0(var_2, var_0, var_1);
    return var_2;
}
function ovegs_Tile__init_0($this, $topLeftCorner, $tileType) {
    jl_Object__init_0($this);
    $this.$topLeftCorner = $topLeftCorner;
    $this.$tileType = $tileType;
}
function ovegs_Tile_getTileType($this) {
    return $this.$tileType;
}
function ovegs_Tile_getX($this) {
    return $rt_nullCheck($this.$topLeftCorner).$getX();
}
function ovegs_Tile_getY($this) {
    return $rt_nullCheck($this.$topLeftCorner).$getY();
}
function ovegs_Tile_getWidth($this) {
    return 32.0;
}
function ovegs_Tile_getHeight($this) {
    return 32.0;
}
function ovegc_GameClient$start$lambda$_2_0() {
    jl_Object.call(this);
    this.$_00 = null;
}
function ovegc_GameClient$start$lambda$_2_0__init_(var_0) {
    var var_1 = new ovegc_GameClient$start$lambda$_2_0();
    ovegc_GameClient$start$lambda$_2_0__init_0(var_1, var_0);
    return var_1;
}
function ovegc_GameClient$start$lambda$_2_0__init_0(var$0, var$1) {
    jl_Object__init_0(var$0);
    var$0.$_00 = var$1;
}
function ovegc_GameClient$start$lambda$_2_0_handleEvent(var$0, var$1) {
    ovegc_GameClient$start$lambda$_2_0_handleEvent0(var$0, var$1);
}
function ovegc_GameClient$start$lambda$_2_0_handleEvent0(var$0, var$1) {
    ovegc_GameClient_lambda$start$0($rt_nullCheck(var$0.$_00), var$1);
}
function ovegc_GameClient$start$lambda$_2_0_handleEvent$exported$0(var$0, var$1) {
    var$0.$handleEvent0(var$1);
}
var otjb_StorageProvider = $rt_classWithoutFields(0);
var otjc_JSArrayReader = $rt_classWithoutFields(0);
var otjb_Window = $rt_classWithoutFields();
function otjb_Window_addEventListener$exported$0(var$0, var$1, var$2) {
    var$0.$addEventListener($rt_str(var$1), otji_JS_functionAsObject(var$2, "handleEvent"));
}
function otjb_Window_removeEventListener$exported$1(var$0, var$1, var$2) {
    var$0.$removeEventListener($rt_str(var$1), otji_JS_functionAsObject(var$2, "handleEvent"));
}
function otjb_Window_get$exported$2(var$0, var$1) {
    return var$0.$get(var$1);
}
function otjb_Window_removeEventListener$exported$3(var$0, var$1, var$2, var$3) {
    var$0.$removeEventListener0($rt_str(var$1), otji_JS_functionAsObject(var$2, "handleEvent"), var$3 ? 1 : 0);
}
function otjb_Window_dispatchEvent$exported$4(var$0, var$1) {
    return !!var$0.$dispatchEvent(var$1);
}
function otjb_Window_getLength$exported$5(var$0) {
    return var$0.$getLength();
}
function otjb_Window_addEventListener$exported$6(var$0, var$1, var$2, var$3) {
    var$0.$addEventListener0($rt_str(var$1), otji_JS_functionAsObject(var$2, "handleEvent"), var$3 ? 1 : 0);
}
var otp_Platform = $rt_classWithoutFields();
function otp_Platform_clone(var$1) {
    var copy = new var$1.constructor();
    for (var field in var$1) {
        if (!var$1.hasOwnProperty(field)) {
            continue;
        }
        copy[field] = var$1[field];
    }
    return copy;
}
function otp_Platform_getEnumConstants(var$1) {
    var c = '$$enumConstants$$';
    ovegs_Direction[c] = ovegs_Direction_values;
    ovegs_TileType[c] = ovegs_TileType_values;
    otp_Platform_getEnumConstants = function(cls) {
        if (!cls.hasOwnProperty(c)) {
            return null;
        }
        if (typeof cls[c] === "function") {
            cls[c] = cls[c]();
        }
        return cls[c];
    };
    return otp_Platform_getEnumConstants(var$1);
}
function otp_Platform_isPrimitive($cls) {
    return $cls.$meta.primitive ? 1 : 0;
}
function otp_Platform_isEnum($cls) {
    return $cls.$meta.enum ? 1 : 0;
}
function otp_Platform_getName($cls) {
    return $rt_str($cls.$meta.name);
}
function ovegc_GameClient$_init_$lambda$_1_0() {
    jl_Object.call(this);
    this.$_01 = null;
}
function ovegc_GameClient$_init_$lambda$_1_0__init_(var_0) {
    var var_1 = new ovegc_GameClient$_init_$lambda$_1_0();
    ovegc_GameClient$_init_$lambda$_1_0__init_0(var_1, var_0);
    return var_1;
}
function ovegc_GameClient$_init_$lambda$_1_0__init_0(var$0, var$1) {
    jl_Object__init_0(var$0);
    var$0.$_01 = var$1;
}
function ovegc_GameClient$_init_$lambda$_1_0_onMessage(var$0, var$1) {
    ovegc_GameClient_onMessageReceived($rt_nullCheck(var$0.$_01), var$1);
}
function ovegc_GameClient$_init_$lambda$_1_0_onMessage$exported$0(var$0, var$1) {
    var$0.$onMessage(var$1);
}
var ovegc_Logger = $rt_classWithoutFields();
function jl_String() {
    var a = this; jl_Object.call(a);
    a.$characters = null;
    a.$hashCode0 = 0;
}
var jl_String_CASE_INSENSITIVE_ORDER = null;
function jl_String_$callClinit() {
    jl_String_$callClinit = $rt_eraseClinit(jl_String);
    jl_String__clinit_();
}
function jl_String__init_(var_0) {
    var var_1 = new jl_String();
    jl_String__init_1(var_1, var_0);
    return var_1;
}
function jl_String__init_0(var_0, var_1, var_2) {
    var var_3 = new jl_String();
    jl_String__init_2(var_3, var_0, var_1, var_2);
    return var_3;
}
function jl_String__init_1($this, $characters) {
    var var$2, var$3, $i, var$5, var$6;
    jl_String_$callClinit();
    jl_Object__init_0($this);
    $characters = $rt_nullCheck($characters);
    var$2 = $characters.data;
    var$3 = var$2.length;
    $this.$characters = $rt_createCharArray(var$3);
    $i = 0;
    while ($i < var$3) {
        var$5 = $this.$characters;
        $i = $rt_checkBounds($i, var$2);
        var$6 = var$2[$i];
        var$5 = $rt_nullCheck(var$5).data;
        $i = $rt_checkUpperBound($i, var$5);
        var$5[$i] = var$6;
        $i = $i + 1 | 0;
    }
}
function jl_String__init_2($this, $value, $offset, $count) {
    var $i, var$5, var$6, var$7;
    jl_String_$callClinit();
    jl_Object__init_0($this);
    $this.$characters = $rt_createCharArray($count);
    $i = 0;
    while ($i < $count) {
        var$5 = $this.$characters;
        var$6 = $i + $offset | 0;
        $value = $rt_nullCheck($value);
        var$7 = $value.data;
        var$6 = var$7[$rt_checkBounds(var$6, var$7)];
        var$7 = $rt_nullCheck(var$5).data;
        $i = $rt_checkBounds($i, var$7);
        var$7[$i] = var$6;
        $i = $i + 1 | 0;
    }
}
function jl_String_charAt($this, $index) {
    var var$2;
    if ($index >= 0 && $index < $rt_nullCheck($this.$characters).data.length) {
        var$2 = $rt_nullCheck($this.$characters).data;
        $index = $rt_checkBounds($index, var$2);
        return var$2[$index];
    }
    $rt_throw(jl_StringIndexOutOfBoundsException__init_());
}
function jl_String_length($this) {
    return $rt_nullCheck($this.$characters).data.length;
}
function jl_String_isEmpty($this) {
    return $rt_nullCheck($this.$characters).data.length ? 0 : 1;
}
function jl_String_toString($this) {
    return $this;
}
function jl_String_equals($this, $other) {
    var $str, $i;
    if ($this === $other)
        return 1;
    if (!($other instanceof jl_String))
        return 0;
    $str = $other;
    $str = $rt_nullCheck($str);
    if ($str.$length0() != $this.$length0())
        return 0;
    $i = 0;
    while ($i < $str.$length0()) {
        if ($this.$charAt($i) != $str.$charAt($i))
            return 0;
        $i = $i + 1 | 0;
    }
    return 1;
}
function jl_String_hashCode($this) {
    var var$1, var$2, var$3, var$4, $c;
    a: {
        if (!$this.$hashCode0) {
            var$1 = $rt_nullCheck($this.$characters).data;
            var$2 = var$1.length;
            var$3 = 0;
            while (true) {
                if (var$3 >= var$2)
                    break a;
                var$4 = $rt_checkLowerBound(var$3);
                $c = var$1[var$4];
                $this.$hashCode0 = (31 * $this.$hashCode0 | 0) + $c | 0;
                var$3 = var$4 + 1 | 0;
            }
        }
    }
    return $this.$hashCode0;
}
function jl_String__clinit_() {
    jl_String_CASE_INSENSITIVE_ORDER = jl_String$_clinit_$lambda$_82_0__init_();
}
var jl_NoClassDefFoundError = $rt_classWithoutFields(jl_LinkageError);
var jl_NoSuchMethodError = $rt_classWithoutFields(jl_IncompatibleClassChangeError);
function jl_NoSuchMethodError__init_(var_0) {
    var var_1 = new jl_NoSuchMethodError();
    jl_NoSuchMethodError__init_0(var_1, var_0);
    return var_1;
}
function jl_NoSuchMethodError__init_0($this, $message) {
    jl_IncompatibleClassChangeError__init_0($this, $message);
}
var jl_ArrayIndexOutOfBoundsException = $rt_classWithoutFields(jl_IndexOutOfBoundsException);
function jl_ArrayIndexOutOfBoundsException__init_() {
    var var_0 = new jl_ArrayIndexOutOfBoundsException();
    jl_ArrayIndexOutOfBoundsException__init_0(var_0);
    return var_0;
}
function jl_ArrayIndexOutOfBoundsException__init_0($this) {
    jl_IndexOutOfBoundsException__init_0($this);
}
var jl_IllegalArgumentException = $rt_classWithoutFields(jl_RuntimeException);
function jl_IllegalArgumentException__init_(var_0) {
    var var_1 = new jl_IllegalArgumentException();
    jl_IllegalArgumentException__init_0(var_1, var_0);
    return var_1;
}
function jl_IllegalArgumentException__init_0($this, $message) {
    jl_RuntimeException__init_2($this, $message);
}
function ovegs_Point() {
    var a = this; jl_Object.call(a);
    a.$x = 0.0;
    a.$y = 0.0;
}
function ovegs_Point__init_(var_0, var_1) {
    var var_2 = new ovegs_Point();
    ovegs_Point__init_0(var_2, var_0, var_1);
    return var_2;
}
function ovegs_Point__init_0($this, $x, $y) {
    jl_Object__init_0($this);
    $this.$x = $x;
    $this.$y = $y;
}
function ovegs_Point_getX($this) {
    return $this.$x;
}
function ovegs_Point_getY($this) {
    return $this.$y;
}
function ovegct_TileMap() {
    jl_Object.call(this);
    this.$tiles = null;
}
function ovegct_TileMap__init_() {
    var var_0 = new ovegct_TileMap();
    ovegct_TileMap__init_0(var_0);
    return var_0;
}
function ovegct_TileMap__init_0($this) {
    var $tileData, $rowIndex, $columns, $columnIndex, var$5, var$6, $tileType, $x, $y, var$10;
    jl_Object__init_0($this);
    $tileData = window.tileMapData.tiles;
    $this.$tiles = $rt_createMultiArray($rt_arraycls($rt_arraycls(ovegs_Tile)), [$tileData[0].length, $tileData.length]);
    $rowIndex = 0;
    while ($rowIndex < $tileData.length) {
        $columns = $tileData[$rowIndex];
        $columnIndex = 0;
        while ($columnIndex < $columns.length) {
            var$5 = ovegs_TileType_values();
            var$6 = otjc_JSNumber_intValue$static($columns[$columnIndex]);
            var$5 = $rt_nullCheck(var$5).data;
            $tileType = var$5[$rt_checkBounds(var$6, var$5)];
            $x = $columnIndex * 32 | 0;
            $y = $rowIndex * 32 | 0;
            var$5 = $rt_nullCheck($this.$tiles).data;
            $rowIndex = $rt_checkBounds($rowIndex, var$5);
            var$5 = var$5[$rowIndex];
            var$10 = ovegs_Tile__init_(ovegs_Point__init_($x, $y), $tileType);
            var$5 = $rt_nullCheck(var$5).data;
            $columnIndex = $rt_checkBounds($columnIndex, var$5);
            var$5[$columnIndex] = var$10;
            $columnIndex = $columnIndex + 1 | 0;
        }
        $rowIndex = $rowIndex + 1 | 0;
    }
    console.warn("Successfully created an array!");
}
function ovegct_TileMap_getTiles($this) {
    return $this.$tiles;
}
var ju_Comparator = $rt_classWithoutFields(0);
var jl_String$_clinit_$lambda$_82_0 = $rt_classWithoutFields();
function jl_String$_clinit_$lambda$_82_0__init_() {
    var var_0 = new jl_String$_clinit_$lambda$_82_0();
    jl_String$_clinit_$lambda$_82_0__init_0(var_0);
    return var_0;
}
function jl_String$_clinit_$lambda$_82_0__init_0(var$0) {
    jl_Object__init_0(var$0);
}
function jl_Class() {
    var a = this; jl_Object.call(a);
    a.$name1 = null;
    a.$platformClass = null;
}
function jl_Class__init_(var_0) {
    var var_1 = new jl_Class();
    jl_Class__init_0(var_1, var_0);
    return var_1;
}
function jl_Class__init_0($this, $platformClass) {
    var var$2;
    jl_Object__init_0($this);
    $this.$platformClass = $platformClass;
    var$2 = $this;
    $platformClass.classObject = var$2;
}
function jl_Class_getClass($cls) {
    var $result;
    if ($cls === null)
        return null;
    $result = $cls.classObject;
    if ($result === null)
        $result = jl_Class__init_($cls);
    return $result;
}
function jl_Class_toString($this) {
    return $rt_nullCheck($rt_nullCheck((jl_StringBuilder__init_()).$append($this.$isInterface() ? $rt_s(19) : !$this.$isPrimitive() ? $rt_s(20) : $rt_s(21))).$append($this.$getName())).$toString();
}
function jl_Class_getName($this) {
    if ($this.$name1 === null)
        $this.$name1 = otp_Platform_getName($this.$platformClass);
    return $this.$name1;
}
function jl_Class_isPrimitive($this) {
    return otp_Platform_isPrimitive($this.$platformClass);
}
function jl_Class_isEnum($this) {
    return otp_Platform_isEnum($this.$platformClass);
}
function jl_Class_isInterface($this) {
    return !($this.$platformClass.$meta.flags & 2) ? 0 : 1;
}
function jl_Class_getEnumConstants($this) {
    if (!$this.$isEnum0())
        return null;
    $this.$platformClass.$clinit();
    return $rt_nullCheck(otp_Platform_getEnumConstants($this.$platformClass)).$clone();
}
var ovegs_TileType = $rt_classWithoutFields(jl_Enum);
var ovegs_TileType_AIR = null;
var ovegs_TileType_GROUND = null;
var ovegs_TileType_$VALUES = null;
function ovegs_TileType_$callClinit() {
    ovegs_TileType_$callClinit = $rt_eraseClinit(ovegs_TileType);
    ovegs_TileType__clinit_();
}
function ovegs_TileType__init_(var_0, var_1) {
    var var_2 = new ovegs_TileType();
    ovegs_TileType__init_0(var_2, var_0, var_1);
    return var_2;
}
function ovegs_TileType_values() {
    ovegs_TileType_$callClinit();
    return $rt_nullCheck(ovegs_TileType_$VALUES).$clone();
}
function ovegs_TileType__init_0($this, var$1, var$2) {
    ovegs_TileType_$callClinit();
    jl_Enum__init_($this, var$1, var$2);
}
function ovegs_TileType__clinit_() {
    ovegs_TileType_AIR = ovegs_TileType__init_($rt_s(22), 0);
    ovegs_TileType_GROUND = ovegs_TileType__init_($rt_s(23), 1);
    ovegs_TileType_$VALUES = $rt_createArrayFromData(ovegs_TileType, [ovegs_TileType_AIR, ovegs_TileType_GROUND]);
}
var otjj_JSON = $rt_classWithoutFields();
$rt_packages([-1, "java", 0, "lang", -1, "org", 2, "vaadin", 3, "erik", 4, "game", 5, "shared"
]);
$rt_metadata([jl_Object, "Object", 1, 0, [], 0, 3, 0, 0, ["$getClass0", $rt_wrapFunction0(jl_Object_getClass), "$toString", $rt_wrapFunction0(jl_Object_toString), "$identity", $rt_wrapFunction0(jl_Object_identity), "$clone", $rt_wrapFunction0(jl_Object_clone)],
jl_Throwable, 0, jl_Object, [], 0, 3, 0, 0, ["$fillInStackTrace", $rt_wrapFunction0(jl_Throwable_fillInStackTrace)],
jl_Exception, 0, jl_Throwable, [], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(jl_Exception__init_0), "$_init_0", $rt_wrapFunction1(jl_Exception__init_2)],
jl_RuntimeException, 0, jl_Exception, [], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(jl_RuntimeException__init_1), "$_init_0", $rt_wrapFunction1(jl_RuntimeException__init_2)],
jl_IndexOutOfBoundsException, 0, jl_RuntimeException, [], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(jl_IndexOutOfBoundsException__init_0)],
ju_Arrays, 0, jl_Object, [], 0, 3, 0, 0, 0,
otj_JSObject, 0, jl_Object, [], 3, 3, 0, 0, 0,
otjb_TimerHandler, 0, jl_Object, [otj_JSObject], 3, 3, 0, 0, 0,
ovegcc_WebSocket$sendMessage$static$lambda$_7_0, 0, jl_Object, [otjb_TimerHandler], 0, 3, 0, 0, ["$_init_2", $rt_wrapFunction2(ovegcc_WebSocket$sendMessage$static$lambda$_7_0__init_0), "$onTimer", $rt_wrapFunction0(ovegcc_WebSocket$sendMessage$static$lambda$_7_0_onTimer), "$onTimer$exported$0", $rt_wrapFunction0(ovegcc_WebSocket$sendMessage$static$lambda$_7_0_onTimer$exported$0)],
ji_Serializable, 0, jl_Object, [], 3, 3, 0, 0, 0,
jl_Number, 0, jl_Object, [ji_Serializable], 1, 3, 0, 0, 0,
jl_Comparable, 0, jl_Object, [], 3, 3, 0, 0, 0,
jl_Integer, 0, jl_Number, [jl_Comparable], 0, 3, 0, jl_Integer_$callClinit, 0,
jl_NullPointerException, 0, jl_RuntimeException, [], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(jl_NullPointerException__init_0)],
jl_CloneNotSupportedException, 0, jl_Exception, [], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(jl_CloneNotSupportedException__init_0)],
jl_Error, 0, jl_Throwable, [], 0, 3, 0, 0, ["$_init_0", $rt_wrapFunction1(jl_Error__init_0)],
jl_LinkageError, 0, jl_Error, [], 0, 3, 0, 0, ["$_init_0", $rt_wrapFunction1(jl_LinkageError__init_0)],
jl_IncompatibleClassChangeError, 0, jl_LinkageError, [], 0, 3, 0, 0, ["$_init_0", $rt_wrapFunction1(jl_IncompatibleClassChangeError__init_0)],
jl_NoSuchFieldError, 0, jl_IncompatibleClassChangeError, [], 0, 3, 0, 0, ["$_init_0", $rt_wrapFunction1(jl_NoSuchFieldError__init_0)],
ovegcc_OnMessageHandler, 0, jl_Object, [otj_JSObject], 3, 3, 0, 0, 0,
jl_Character, 0, jl_Object, [jl_Comparable], 0, 3, 0, jl_Character_$callClinit, 0,
jl_Enum, 0, jl_Object, [jl_Comparable, ji_Serializable], 1, 3, 0, 0, ["$_init_3", $rt_wrapFunction2(jl_Enum__init_), "$name0", $rt_wrapFunction0(jl_Enum_name), "$ordinal0", $rt_wrapFunction0(jl_Enum_ordinal)],
otci_IntegerUtil, 0, jl_Object, [], 4, 3, 0, 0, 0,
jl_Math, 0, jl_Object, [], 4, 3, 0, 0, 0,
otjc_JSNumber, 0, jl_Object, [otj_JSObject], 1, 3, 0, 0, 0,
ovegcc_Communicator, 0, jl_Object, [], 0, 3, 0, 0, ["$_init_6", $rt_wrapFunction1(ovegcc_Communicator__init_0), "$sendPlayerCommand", $rt_wrapFunction2(ovegcc_Communicator_sendPlayerCommand)],
otjc_JSObjects, 0, jl_Object, [], 4, 3, 0, 0, 0,
otjde_EventTarget, 0, jl_Object, [otj_JSObject], 3, 3, 0, 0, 0,
otjde_GamepadEventTarget, 0, jl_Object, [otjde_EventTarget], 3, 3, 0, 0, 0,
jl_Cloneable, 0, jl_Object, [], 3, 3, 0, 0, 0,
otji_JS, 0, jl_Object, [], 4, 0, 0, 0, 0,
jl_CharSequence, 0, jl_Object, [], 3, 3, 0, 0, 0,
otjde_LoadEventTarget, 0, jl_Object, [otjde_EventTarget], 3, 3, 0, 0, 0,
ovegc_GameCanvas, 0, jl_Object, [], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(ovegc_GameCanvas__init_0), "$clear", $rt_wrapFunction0(ovegc_GameCanvas_clear), "$drawPlayer", $rt_wrapFunction2(ovegc_GameCanvas_drawPlayer), "$drawTileMap", $rt_wrapFunction1(ovegc_GameCanvas_drawTileMap), "$drawCollision", $rt_wrapFunction2(ovegc_GameCanvas_drawCollision), "$drawTile", function(var_1, var_2, var_3, var_4, var_5) { ovegc_GameCanvas_drawTile(this, var_1, var_2, var_3, var_4, var_5); }],
jl_StringIndexOutOfBoundsException, 0, jl_IndexOutOfBoundsException, [], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(jl_StringIndexOutOfBoundsException__init_0)],
ovegc_GameCanvas$1, 0, jl_Object, [], 32, 0, 0, ovegc_GameCanvas$1_$callClinit, 0,
jlr_Type, 0, jl_Object, [], 3, 3, 0, 0, 0,
ovegcc_WebSocket, 0, jl_Object, [otj_JSObject], 1, 3, 0, 0, 0,
ovegs_Direction, "Direction", 6, jl_Enum, [], 12, 3, 0, ovegs_Direction_$callClinit, 0,
jl_AbstractStringBuilder, 0, jl_Object, [ji_Serializable, jl_CharSequence], 0, 0, 0, 0, ["$_init_", $rt_wrapFunction0(jl_AbstractStringBuilder__init_0), "$_init_4", $rt_wrapFunction1(jl_AbstractStringBuilder__init_2), "$append1", $rt_wrapFunction1(jl_AbstractStringBuilder_append), "$insert", $rt_wrapFunction2(jl_AbstractStringBuilder_insert), "$append2", $rt_wrapFunction1(jl_AbstractStringBuilder_append0), "$insert0", $rt_wrapFunction2(jl_AbstractStringBuilder_insert0), "$ensureCapacity", $rt_wrapFunction1(jl_AbstractStringBuilder_ensureCapacity),
"$toString", $rt_wrapFunction0(jl_AbstractStringBuilder_toString)],
jl_Appendable, 0, jl_Object, [], 3, 3, 0, 0, 0,
jl_StringBuilder, 0, jl_AbstractStringBuilder, [jl_Appendable], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(jl_StringBuilder__init_0), "$append", $rt_wrapFunction1(jl_StringBuilder_append), "$append0", $rt_wrapFunction1(jl_StringBuilder_append0), "$insert1", $rt_wrapFunction2(jl_StringBuilder_insert), "$insert2", $rt_wrapFunction2(jl_StringBuilder_insert0), "$toString", $rt_wrapFunction0(jl_StringBuilder_toString), "$ensureCapacity", $rt_wrapFunction1(jl_StringBuilder_ensureCapacity), "$insert0", $rt_wrapFunction2(jl_StringBuilder_insert1),
"$insert", $rt_wrapFunction2(jl_StringBuilder_insert2)],
jlr_AnnotatedElement, 0, jl_Object, [], 3, 3, 0, 0, 0,
ovegc_GameClient, 0, jl_Object, [], 0, 3, 0, 0, 0,
otjde_EventListener, 0, jl_Object, [otj_JSObject], 3, 3, 0, 0, 0,
ovegccj_TileCollisionJson, 0, jl_Object, [otj_JSObject], 1, 3, 0, 0, 0,
otjde_FocusEventTarget, 0, jl_Object, [otjde_EventTarget], 3, 3, 0, 0, 0,
otjde_MouseEventTarget, 0, jl_Object, [otjde_EventTarget], 3, 3, 0, 0, 0,
otjde_KeyboardEventTarget, 0, jl_Object, [otjde_EventTarget], 3, 3, 0, 0, 0,
otjb_WindowEventTarget, 0, jl_Object, [otjde_EventTarget, otjde_FocusEventTarget, otjde_MouseEventTarget, otjde_KeyboardEventTarget, otjde_LoadEventTarget, otjde_GamepadEventTarget], 3, 3, 0, 0, 0]);
$rt_metadata([ovegct_TileMapService, 0, jl_Object, [], 1, 3, 0, 0, 0,
ovegs_HasPosition, 0, jl_Object, [], 3, 3, 0, 0, 0,
ovegs_Tile, 0, jl_Object, [ovegs_HasPosition], 0, 3, 0, 0, ["$_init_8", $rt_wrapFunction2(ovegs_Tile__init_0), "$getTileType", $rt_wrapFunction0(ovegs_Tile_getTileType), "$getX", $rt_wrapFunction0(ovegs_Tile_getX), "$getY", $rt_wrapFunction0(ovegs_Tile_getY), "$getWidth", $rt_wrapFunction0(ovegs_Tile_getWidth), "$getHeight", $rt_wrapFunction0(ovegs_Tile_getHeight)],
ovegc_GameClient$start$lambda$_2_0, 0, jl_Object, [otjde_EventListener], 0, 3, 0, 0, ["$_init_7", $rt_wrapFunction1(ovegc_GameClient$start$lambda$_2_0__init_0), "$handleEvent0", $rt_wrapFunction1(ovegc_GameClient$start$lambda$_2_0_handleEvent), "$handleEvent", $rt_wrapFunction1(ovegc_GameClient$start$lambda$_2_0_handleEvent0), "$handleEvent$exported$0", $rt_wrapFunction1(ovegc_GameClient$start$lambda$_2_0_handleEvent$exported$0)],
otjb_StorageProvider, 0, jl_Object, [], 3, 3, 0, 0, 0,
otjc_JSArrayReader, 0, jl_Object, [otj_JSObject], 3, 3, 0, 0, 0,
otjb_Window, 0, jl_Object, [otj_JSObject, otjb_WindowEventTarget, otjb_StorageProvider, otjc_JSArrayReader], 1, 3, 0, 0, ["$addEventListener$exported$0", $rt_wrapFunction2(otjb_Window_addEventListener$exported$0), "$removeEventListener$exported$1", $rt_wrapFunction2(otjb_Window_removeEventListener$exported$1), "$get$exported$2", $rt_wrapFunction1(otjb_Window_get$exported$2), "$removeEventListener$exported$3", $rt_wrapFunction3(otjb_Window_removeEventListener$exported$3), "$dispatchEvent$exported$4", $rt_wrapFunction1(otjb_Window_dispatchEvent$exported$4),
"$getLength$exported$5", $rt_wrapFunction0(otjb_Window_getLength$exported$5), "$addEventListener$exported$6", $rt_wrapFunction3(otjb_Window_addEventListener$exported$6)],
otp_Platform, 0, jl_Object, [], 4, 3, 0, 0, 0,
ovegc_GameClient$_init_$lambda$_1_0, 0, jl_Object, [ovegcc_OnMessageHandler], 0, 3, 0, 0, ["$_init_7", $rt_wrapFunction1(ovegc_GameClient$_init_$lambda$_1_0__init_0), "$onMessage", $rt_wrapFunction1(ovegc_GameClient$_init_$lambda$_1_0_onMessage), "$onMessage$exported$0", $rt_wrapFunction1(ovegc_GameClient$_init_$lambda$_1_0_onMessage$exported$0)],
ovegc_Logger, 0, jl_Object, [], 0, 3, 0, 0, 0,
jl_String, 0, jl_Object, [ji_Serializable, jl_Comparable, jl_CharSequence], 0, 3, 0, jl_String_$callClinit, ["$_init_1", $rt_wrapFunction1(jl_String__init_1), "$_init_5", $rt_wrapFunction3(jl_String__init_2), "$charAt", $rt_wrapFunction1(jl_String_charAt), "$length0", $rt_wrapFunction0(jl_String_length), "$isEmpty", $rt_wrapFunction0(jl_String_isEmpty), "$toString", $rt_wrapFunction0(jl_String_toString), "$equals", $rt_wrapFunction1(jl_String_equals), "$hashCode", $rt_wrapFunction0(jl_String_hashCode)],
jl_NoClassDefFoundError, 0, jl_LinkageError, [], 0, 3, 0, 0, 0,
jl_NoSuchMethodError, 0, jl_IncompatibleClassChangeError, [], 0, 3, 0, 0, ["$_init_0", $rt_wrapFunction1(jl_NoSuchMethodError__init_0)],
jl_ArrayIndexOutOfBoundsException, 0, jl_IndexOutOfBoundsException, [], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(jl_ArrayIndexOutOfBoundsException__init_0)],
jl_IllegalArgumentException, 0, jl_RuntimeException, [], 0, 3, 0, 0, ["$_init_0", $rt_wrapFunction1(jl_IllegalArgumentException__init_0)],
ovegs_Point, 0, jl_Object, [], 0, 3, 0, 0, ["$_init_9", $rt_wrapFunction2(ovegs_Point__init_0), "$getX", $rt_wrapFunction0(ovegs_Point_getX), "$getY", $rt_wrapFunction0(ovegs_Point_getY)],
ovegct_TileMap, 0, jl_Object, [], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(ovegct_TileMap__init_0), "$getTiles", $rt_wrapFunction0(ovegct_TileMap_getTiles)],
ju_Comparator, 0, jl_Object, [], 3, 3, 0, 0, 0,
jl_String$_clinit_$lambda$_82_0, 0, jl_Object, [ju_Comparator], 0, 3, 0, 0, ["$_init_", $rt_wrapFunction0(jl_String$_clinit_$lambda$_82_0__init_0)],
jl_Class, 0, jl_Object, [jlr_AnnotatedElement, jlr_Type], 0, 3, 0, 0, ["$toString", $rt_wrapFunction0(jl_Class_toString), "$getName", $rt_wrapFunction0(jl_Class_getName), "$isPrimitive", $rt_wrapFunction0(jl_Class_isPrimitive), "$isEnum0", $rt_wrapFunction0(jl_Class_isEnum), "$isInterface", $rt_wrapFunction0(jl_Class_isInterface), "$getEnumConstants", $rt_wrapFunction0(jl_Class_getEnumConstants)],
ovegs_TileType, 0, jl_Enum, [], 12, 3, 0, ovegs_TileType_$callClinit, 0,
otjj_JSON, 0, jl_Object, [], 4, 3, 0, 0, 0]);
function $rt_array(cls, data) {
    this.$monitor = null;
    this.$id$ = 0;
    this.type = cls;
    this.data = data;
    this.constructor = $rt_arraycls(cls);
}
$rt_array.prototype = Object.create(($rt_objcls()).prototype);
$rt_array.prototype.toString = function() {
    var str = "[";
    for (var i = 0;i < this.data.length;++i) {
        if (i > 0) {
            str += ", ";
        }
        str += this.data[i].toString();
    }
    str += "]";
    return str;
};
$rt_setCloneMethod($rt_array.prototype, function() {
    var dataCopy;
    if ('slice' in this.data) {
        dataCopy = this.data.slice();
    } else {
        dataCopy = new this.data.constructor(this.data.length);
        for (var i = 0;i < dataCopy.length;++i) {
            dataCopy[i] = this.data[i];
        }
    }
    return new $rt_array(this.type, dataCopy);
});
$rt_stringPool(["@", "Class does not represent enum", "Enum ", " does not have the ", "constant", "0", "game/command", "blue", "green", "red", "yellow", "UP", "DOWN", "LEFT", "RIGHT", "null", "ArrowRight", "ArrowUp", "ArrowLeft", "interface ", "class ", "", "AIR", "GROUND"]);
jl_String.prototype.toString = function() {
    return $rt_ustr(this);
};
jl_String.prototype.valueOf = jl_String.prototype.toString;
jl_Object.prototype.toString = function() {
    return $rt_ustr(jl_Object_toString(this));
};
jl_Object.prototype.__teavm_class__ = function() {
    return $dbg_class(this);
};
function $rt_startThread(runner, callback) {
    var result;
    try {
        result = runner();
    } catch (e){
        result = e;
    }
    if (typeof callback !== 'undefined') {
        callback(result);
    } else if (result instanceof Error) {
        throw result;
    }
}
function $rt_suspending() {
    return false;
}
function $rt_resuming() {
    return false;
}
function $rt_nativeThread() {
    return null;
}
function $rt_invalidPointer() {
}
main = $rt_mainStarter(ovegc_GameClient_main);
(function() {
    var c;
    c = ovegcc_WebSocket$sendMessage$static$lambda$_7_0.prototype;
    c.onTimer = c.$onTimer$exported$0;
    c = ovegc_GameClient$start$lambda$_2_0.prototype;
    c.handleEvent = c.$handleEvent$exported$0;
    c = otjb_Window.prototype;
    c.dispatchEvent = c.$dispatchEvent$exported$4;
    c.addEventListener = c.$addEventListener$exported$0;
    c.removeEventListener = c.$removeEventListener$exported$1;
    c.getLength = c.$getLength$exported$5;
    c.get = c.$get$exported$2;
    c.addEventListener = c.$addEventListener$exported$6;
    c.removeEventListener = c.$removeEventListener$exported$3;
    c = ovegc_GameClient$_init_$lambda$_1_0.prototype;
    c.onMessage = c.$onMessage$exported$0;
})();
})();

//# sourceMappingURL=engine.js.map