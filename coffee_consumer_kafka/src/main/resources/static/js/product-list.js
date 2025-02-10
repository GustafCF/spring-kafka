$(document).ready(function() {
    loadProducts();

    function loadProducts() {
        $.ajax({
            type: "GET",
            url: "/product/list",
            success: function(products) {
                var tableBody = $("#productTableBody");
                tableBody.empty();

                products.forEach(function(product) {
                    var row = `
                        <tr>
                            <td>${product.id}</td>
                            <td>${product.name}</td>
                            <td>${product.description}</td>
                            <td>R$ ${product.price.toFixed(2)}</td>
                            <td>
                                <button class="btn btn-danger btn-sm delete-btn" data-id="${product.id}">Excluir</button>
                            </td>
                        </tr>
                    `;
                    tableBody.append(row);
                });

                $(".delete-btn").click(function() {
                    var productId = $(this).data("id");
                    confirmDelete(productId);
                });
            },
            error: function(xhr) {
                console.error("Erro ao carregar produtos:", xhr);
            }
        });
    }

    function confirmDelete(productId) {
        Swal.fire({
            title: "Tem certeza?",
            text: "Essa ação não pode ser desfeita!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sim, excluir!"
        }).then((result) => {
            if (result.isConfirmed) {
                deleteProduct(productId);
            }
        });
    }

    function deleteProduct(productId) {
        $.ajax({
            type: "DELETE",
            url: `/product/delete/${productId}`,
            success: function() {
                Swal.fire({
                    icon: "success",
                    title: "Excluído!",
                    text: "O produto foi removido com sucesso.",
                    confirmButtonColor: "#3085d6"
                });
                loadProducts();
            },
            error: function(xhr) {
                Swal.fire({
                    icon: "error",
                    title: "Erro!",
                    text: "Não foi possível excluir o produto.",
                    confirmButtonColor: "#d33"
                });
            }
        });
    }
});
