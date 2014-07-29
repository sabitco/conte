var infovalmer = infovalmer || {};
infovalmer.util = infovalmer.util || {};

infovalmer.util.SessionTimer = function (options){
  this.opts = options;
  this.opts.refreshInterval = 1000;
  this.opts.maxCounter = this.opts.timeOut / this.opts.refreshInterval;
}

infovalmer.util.SessionTimer.prototype.start = function() {

  var that = this;

  window.clearInterval(that.id);
  that.reset();

  jQuery(document).on('click mousemove', '.v-tabsheet-tabitemcell, .v-tree-node-caption span' +
    ', #validacionparadas .v-button, #breadcrumb .v-button-wrap', function() {
    that.reset();
  });

  that.id = window.setInterval(function(){
    that.counter++;
    if (that.counter > that.opts.maxCounter) {
      window.clearInterval(that.id);
      window.location.href = window.location.pathname + that.opts.destinationUrl;
    }
  }, that.opts.refreshInterval);
};

infovalmer.util.SessionTimer.prototype.reset = function() {
  this.counter = 0;
};