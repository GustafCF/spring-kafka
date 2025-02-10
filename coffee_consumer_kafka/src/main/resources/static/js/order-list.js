$(document).ready(function() {
    loadOrders();

    function loadOrders() {
        $.ajax({
            type: "GET",
            url: "/order/listAll",
            success: function(orders) {
                var tableBody = $("#orderTableBody");
                tableBody.empty();

                orders.forEach(function(order) {
                    var row = `
                        <tr>
                            <td>${order.id}</td>
                            <td>${order.client ? order.client.name : "Desconhecido"}</td>
                            <td>${order.orderTime ? new Date(order.orderTime).toLocaleString() : "Não informado"}</td>
                            <td>
                                <button class="btn btn-danger btn-sm delete-btn" data-id="${order.id}">Excluir</button>
                            </td>
                        </tr>
                    `;
                    tableBody.append(row);
                });

                $(".delete-btn").click(function() {
                    var orderId = $(this).data("id");
                    confirmDelete(orderId);
                });
            },
            error: function(xhr) {
                console.error("Erro ao carregar pedidos:", xhr);
            }
        });
    }

    function confirmDelete(orderId) {
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
                deleteOrder(orderId);
            }
        });
    }

    function deleteOrder(orderId) {
        $.ajax({
            type: "DELETE",
            url: `/order/delete/${orderId}`,
            success: function() {
                Swal.fire({
                    icon: "success",
                    title: "Excluído!",
                    text: "O pedido foi removido com sucesso.",
                    confirmButtonColor: "#3085d6"
                });
                loadOrders();
            },
            error: function(xhr) {
                Swal.fire({
                    icon: "error",
                    title: "Erro!",
                    text: "Não foi possível excluir o pedido.",
                    confirmButtonColor: "#d33"
                });
            }
        });
    }
});
