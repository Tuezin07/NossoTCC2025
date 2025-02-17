// Variável para armazenar o valor total da compra
let totalCompra = 0;

// Elementos da interface
const telas = {
    inicial: document.getElementById('tela-inicial'),
    leitura: document.getElementById('tela-leitura'),
    pagamento: document.getElementById('tela-pagamento')
};

const elementos = {
    codigoBarras: document.getElementById('codigo-barras'),
    produtosLidos: document.getElementById('produtos-lidos'),
    valorTotal: document.getElementById('valor-total'),
    qrCode: document.getElementById('qr-code')
};

// Configuração inicial das telas
telas.inicial.style.display = 'block';
telas.leitura.style.display = 'none';
telas.pagamento.style.display = 'none';

// Evento de clique no botão "Iniciar"
document.getElementById('iniciar').addEventListener('click', () => {
    telas.inicial.style.display = 'none';
    telas.leitura.style.display = 'block';
    elementos.codigoBarras.focus();
});

// Variável para controle do timeout
let leituraTimeout;

// Evento de input no campo de código de barras (com timeout)
elementos.codigoBarras.addEventListener('input', (event) => {
    clearTimeout(leituraTimeout);
    
    // Aguarda 300ms após a última digitação para considerar o código completo
    leituraTimeout = setTimeout(() => {
        const codigoBruto = elementos.codigoBarras.value;
        elementos.codigoBarras.value = ''; // Limpa o campo APÓS processar
        
        // Sanitiza o código (remove caracteres não numéricos)
        const codigoBarras = codigoBruto.replace(/\D/g, '');

        if (!codigoBarras) {
            alert("Código inválido!");
            return;
        }

        console.log(`Código Completo Lido: ${codigoBarras}`);

        // Busca o produto na API
        fetch(`/produtos/${codigoBarras}`)
            .then(response => {
                if (!response.ok) throw new Error(`Produto ${codigoBarras} não encontrado`);
                return response.json();
            })
            .then(produto => adicionarProduto(produto))
            .catch(error => {
                console.error(error.message);
                alert(error.message);
            });
    }, 300); // Tempo ajustável conforme necessidade do leitor
});

// Função para adicionar produto à lista
function adicionarProduto(produto) {
    const item = document.createElement('div');
    item.className = 'produto-item';
    item.innerHTML = `
        <span>${produto.nome}</span>
        <span>R$ ${produto.preco.toFixed(2)}</span>
    `;
    
    elementos.produtosLidos.appendChild(item);
    atualizarTotal(produto.preco);
}

function atualizarTotal(valor) {
    totalCompra += valor;
    elementos.valorTotal.textContent = totalCompra.toFixed(2);
}

// Evento de clique no botão "Finalizar Compra"
document.getElementById('finalizar-compra').addEventListener('click', function() {
    fetch('/sales', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            valorTotal: totalCompra,
            metodoPagamento: "PIX"
        })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                throw new Error(err.error || 'Erro ao processar pagamento');
            });
        }
        return response.json();
    })
    .then(data => {
        // Redireciona para a página de pagamento do Mercado Pago
        window.location.href = data.urlPagamento;
    })
    .catch(error => {
        console.error('Erro:', error);
        alert(error.message);
    });
});

// Evento de clique no botão "Voltar"
document.getElementById('voltar').addEventListener('click', () => {
    telas.pagamento.style.display = 'none';
    telas.leitura.style.display = 'block';
});