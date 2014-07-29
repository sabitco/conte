var infovalmer = infovalmer || {};
infovalmer.login = infovalmer.login || {};

infovalmer.login.init = function (){
  jQuery('#username').focus();
}

jQuery(function() {
  infovalmer.login.init();
});
