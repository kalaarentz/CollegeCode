function risk(height, weight, sex) {
    var riskCalc = (weight * 0.45359237) / Math.pow((height * 0.0254), 2);
    if (sex === 'M' && (riskCalc < 20.4 || riskCalc > 31.9)) {
		return true;
	} else if (sex === 'F' && (riskCalc < 19.4 || riskCalc > 27.9)) {
        return true;
	} else { return false; }
}
function roman(number) {
	number = String(number);
	if (number.toUpperCase() === 'I') {
		return 1;
	} else if (number.toUpperCase() === 'II') {
		return 2;
	} else if (number.toUpperCase() === 'III') {
		return 3;
	} else if (number.toUpperCase() === 'IV') {
		return 4;
	} else if (number.toUpperCase() === 'V') {
		return 5;
	} else if (number.toUpperCase() === 'VI') {
		return 6;
	} else if (number.toUpperCase() === 'VII') {
		return 7;
	} else if (number.toUpperCase() === 'VIII') {
		return 8;
	} else if (number.toUpperCase() === 'IX') {
		return 9;
	} else if (number.toUpperCase() === 'X') {
		return 10;
	} else if (Number(number) === 1) {
		return 'I';
	} else if (Number(number) === 2) {
		return 'II';
	} else if (Number(number) === 3) {
		return 'III';
	} else if (Number(number) === 4) {
		return 'IV';
	} else if (Number(number) === 5) {
		return 'V';
	} else if (Number(number) === 6) {
		return 'VI';
	} else if (Number(number) === 7) {
		return 'VII';
	} else if (Number(number) === 8) {
		return 'VIII';
	} else if (Number(number) === 9) {
		return 'IX';
	} else if (Number(number) === 10) {
		return 'X';
	}
}
function lettersThatFollow(text, ch) {
    //look for the letter behind the character
	var str = '';
	for (var i=0; i < text.length ;i++) {
		if ( text.charAt(i) == ch) {
			if ((i+1) != text.length) {
				str += text.charAt(i+1);
			} else {
				str += text.charAt(i);
			}
		}
	}
    // delete the duplicates
	str = str.split('');
	var result = [];
	for (var i =0; i < str.length ; i++) {
    	if (result.indexOf(str[i]) == -1) {
    		result.push(str[i]);
    	}
	}
	result=result.join('');
	return result;
}
function props(list, propertyName) {
	var result = new Array(0);
	for ( var i in list) {
		var name = list[i][propertyName];
		result.push(name);
	}
	return result;
}
function toHTML(list) {
	var result = '<ol>';
	for (var i in list) {
        // if it is a list in an list, then it will enter the if 
        // statement to be iterated through
		if (list[i].length !== undefined) {
			result += ('<li>' + list[i][0] + '<ol>');
			for (var j = 1;j<list[i].length;j++) {
				result+= ('<li>' + list[i][j] + '</li>');
			}
			result += '</ol></li>';
		} else {
			result += ('<li>' + list[i] + '</li>');
		}
	}
	result +='</ol>';
	return result;
}
function sequence(start, step) {
    var num = start;
    return function() {
    	var result = num;
    	num = num + step;
    	return result;
    }
}
function repeat(text, n) {
	var result = '';
	if (n < 0) {
		return '';
	} else {
		for (var i=0; i<n; i++ ) {
			result+=text;
		}
	}
	return result;
}
function repeatf( f, n) {
	var result = [];
	for (var i = 0;i < n; i++) {
			result.push(f.call(this,arguments));
		}
	return result;
}
function matchmaker( obj ) {
	return function f (input) {
		for (var prop in obj) {
			if (input[prop] != obj[prop]) {
				return false;
			}
		}
		return true;
	}
}
function breakup(list, partitioner) {
	var result = {};
	for (var i = 0; i < list.length; i++) {
		var ret = partitioner(list[i]);
		// if the ret is not a property in Object
		if (!(ret in result)) {
			result[ret] = [list[i]];
		} else {
			var answ = result[ret];
			answ.push(list[i])
		}
	}
	return result;
}
function eachOne(list) {
	for (var i = 0; i < list.length; i++) {
		if (list[i] === false) {
			return list[i];
		} else if (list[i] === null) {
			return list[i];
		} else if (list[i] === undefined) {
			return list[i];
		} else if (list[i] === '') {
			return list[i];
		}
	}
	return true;
}
function noSql(list, query) {
    result = [];
    for (var i in list) {
    	var works = true;
    	for (var prop in query) {
    		if (list[i][prop] !== query[prop]) {
    			works = false;
    		}
    	}
    	if (works) {
    		result.push( list[i]);
    	}
    }
    return result;
}
function justOnce( f ) {
	var executed = false;
	return function() {
		if (!executed) {
			result = f.apply(this, arguments);
		}
		executed = true;
		return result;
	}
}