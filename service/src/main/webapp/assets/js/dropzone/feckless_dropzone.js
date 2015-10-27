// Get the template HTML and remove it from the document 
var previewNode = document.querySelector("#template");
previewNode.id = "";
var previewTemplate = previewNode.parentNode.innerHTML;
previewNode.parentNode.removeChild(previewNode);

var drop = new Dropzone(document.body, {
    url: "/some-url-here",
    thumbnailWidth: 200,
    thumbnailHeight: 200,
    maxFiles: 5,
    previewTemplate: previewTemplate,
    previewContainer: "#previews",
    uploadMultiple: false,
    acceptedFiles: "image/*, application/pdf",

});

drop.on("maxfilesexceeded", function(file){
    drop.removeFile(file);
});

// Update the progress bar when uploading
drop.on("totaluploadprogress", function(){
});


drop.on("sending", function(progress){
    // Make the progress bar visible when we start uploading
    // Disable the start button 
    file.previewElement.querySelector(".start").setAttribute("disabled", "disabled");
});

// Hide the progress bar when we're done 
drop.on("queuecomplete", function(progress){
});


