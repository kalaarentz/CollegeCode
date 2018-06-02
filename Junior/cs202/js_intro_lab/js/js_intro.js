function parseEmail(string) {
    var result = {};
    var user = string.substr(0,string.search('@'));
    var d = string.substr(string.search('@')+1);
    if( user === '' || d === '') {
    	return undefined;
    }
    result.username = user;
    result.domain = d;
    return result;  
}
function winners(list) {
	var result =[];
	for(var i = 0; i<list.length;i++) {
		if(list[i].home.score > list[i].away.score) {
			result.push(list[i].home.name);
		} else if (list[i].home.score < list[i].away.score) {
			result.push(list[i].away.name);
		} else {
			return 'tie';
		}
	}
	return result;
}
function rank(list) {
	var result = [];
	var people = {};
	for(var i = 0; i<list.length;i++) {
		candidate = list[i].toUpperCase();
		if( candidate in people){
			people[candidate] = people[candidate]+1;
		} else {
			people[candidate] = 1;
		}
	}
	// sort 
	var sorted = [];
	for(var prop in people) {
		sorted.push([prop, people[prop]]);
	}
    
	sorted.sort( function (a, b) {
		return b[1]-a[1]; })
    
	for (var i in sorted) {
		result.push(sorted[i][0]);
	}
	return result;
}