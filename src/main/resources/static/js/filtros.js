function filtrarEventos(categoriaDesejada, botaoClicado) {
    const cards = document.querySelectorAll('.event-card');
    const botoes = document.querySelectorAll('.btn-filtro');

    botoes.forEach(botao => botao.classList.remove('ativo'));

    botaoClicado.classList.add('ativo');

    cards.forEach(card => {
        const categoriaDoCard = card.querySelector('.event-categoria').innerText.trim().toLowerCase();
        const filtro = categoriaDesejada.toLowerCase();

        if (filtro === 'todos' || categoriaDoCard.includes(filtro)) {
            card.style.display = 'flex'; 
        } else {
            card.style.display = 'none';
        }
    });
}