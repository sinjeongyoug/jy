// 범용성 있는 자바스크립트
String.prototype.replaceAll = function(org, dest) {
	return this.split(org).join(dest);
}

function getUriParams(uri) {
	uri = uri.trim();
	uri = uri.replaceAll('&amp;', '&');
	if (uri.indexOf('#') !== -1) {
		var pos = uri.indexOf('#');
		uri = uri.substr(0, pos);
	}

	var params = {};

	uri.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) {
		params[key] = value;
	});
	return params;
}

function jq_attr($el, attrName, elseValue) {
	var value = $el.attr(attrName);

	if (value === undefined || value === "") {
		return elseValue;
	}

	return value;
}

function isCellphoneNo(str) {
	if ( str.substr(0, 1) != '0' ) {
		return false;
	}
	
	return isNumber(str);
}

function isNumber(n) {
	return /^-?[\d.]+(?:e-?\d+)?$/.test(n);
}

$(document).on('ready', function() {
	  
	  $('.field').on('focus', function() {
	    $('body').addClass('is-focus');
	  });
	  
	  $('.field').on('blur', function() {
	    $('body').removeClass('is-focus is-type');
	  });
	  
	  $('.field').on('keydown', function(event) {
	    $('body').addClass('is-type');
	    if((event.which === 8) && $(this).val() === '') {
	      $('body').removeClass('is-type');
	    }
	  });
	  
	});