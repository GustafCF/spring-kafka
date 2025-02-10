$(document).ready(function() {
    $("#productForm").submit(function(event) {
        event.preventDefault();

        var productData = {
            name: $("#name").val(),
            description: $("#description").val(),
            price: $("#price").val()
        };

        $.ajax({
            type: "POST",
            url: "/product/insert",
            contentType: "application/json",
            data: JSON.stringify(productData),
            success: function(response) {
                $("#message").html('<div class="alert alert-success">Produto cadastrado com sucesso!</div>');
                $("#productForm")[0].reset();
            },
            error: function(xhr) {
                $("#message").html('<div class="alert alert-danger">Erro ao cadastrar o produto.</div>');
            }
        });
    });
});
