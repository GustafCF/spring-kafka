document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('orderForm');
    const nameProduct = document.getElementById('nameProduct');
    const quantity = document.getElementById('quantity');
    const client = document.getElementById('client');
    const responseMessage = document.getElementById('responseMessage');

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        if (!nameProduct.value.trim()) {
            responseMessage.innerHTML = '<p style="color: red;">Nome do produto é obrigatório!</p>';
            return;
        }

        if (!quantity.value.trim() || quantity.value <= 0) {
            responseMessage.innerHTML = '<p style="color: red;">Quantidade deve ser um número maior que 0!</p>';
            return;
        }

        if (!client.value.trim()) {
            responseMessage.innerHTML = '<p style="color: red;">Nome do cliente é obrigatório!</p>';
            return;
        }

        const orderData = {
            nameProduct: nameProduct.value,
            quantity: quantity.value,
            client: client.value
        };

        fetch('/order/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        })
            .then(response => response.text())
            .then(data => {
                responseMessage.innerHTML = `<p style="color: green;">${data}</p>`;
            })
            .catch(error => {
                responseMessage.innerHTML = '<p style="color: red;">Erro ao enviar o pedido.</p>';
            });
    });
});
