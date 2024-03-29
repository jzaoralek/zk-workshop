/**
 * 
 */
CKEDITOR.editorConfig = function(config) {
	config.resize_enabled = false;
    config.removePlugins = 'elementspath';
    config.toolbar = 'Complex';
    config.toolbar_Simple = [ [ 'Bold', 'Italic', '-', 'NumberedList',
            'BulletedList', '-', 'Link', 'Unlink', '-', 'Maximize' ] ];
    config.toolbar_Complex = [
            [ 'Undo', 'Redo' ],
            [ 'Bold', 'Italic', 'Underline', '-',
                    '-', '-', 'BGColor', '-', '-', '-',
                    '-', '-', 'NumberedList',
                    'BulletedList', '-', 'Link', 'Unlink'],
            [ 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock' ],
            [ 'Format', 'Font', 'FontSize', 'Maximize' ] ];
};