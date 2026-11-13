function getVariavelCss(nome) {
    return getComputedStyle(document.documentElement).getPropertyValue(nome).trim();
}

const seletorStatusCores = {
    ABERTO: getVariavelCss('--status-serious'),
    EM_ANDAMENTO: getVariavelCss('--status-warning'),
    RESOLVIDO: getVariavelCss('--status-good'),
    CANCELADO: getVariavelCss('--status-critical')
};

const rotulosStatus = {
    ABERTO: 'Aberto',
    EM_ANDAMENTO: 'Em andamento',
    RESOLVIDO: 'Resolvido',
    CANCELADO: 'Cancelado'
};

const rotulosTipo = {
    HARDWARE: 'Hardware',
    SOFTWARE: 'Software',
    REDE: 'Rede',
    ACESSO: 'Acesso',
    OUTROS: 'Outros'
};

const coresCategoricas = [
    getVariavelCss('--series-1'),
    getVariavelCss('--series-2'),
    getVariavelCss('--series-3'),
    getVariavelCss('--series-4'),
    getVariavelCss('--series-5')
];

let graficoPeriodo;
let graficoSituacao;
let graficoTipos;

function montarQueryString() {
    const parametros = new URLSearchParams();
    const dataInicio = document.getElementById('filtro-data-inicio').value;
    const dataFim = document.getElementById('filtro-data-fim').value;
    const status = document.getElementById('filtro-status').value;
    const tipo = document.getElementById('filtro-tipo').value;
    const prioridade = document.getElementById('filtro-prioridade').value;

    if (dataInicio) parametros.set('dataInicio', dataInicio);
    if (dataFim) parametros.set('dataFim', dataFim);
    if (status) parametros.set('status', status);
    if (tipo) parametros.set('tipo', tipo);
    if (prioridade) parametros.set('prioridade', prioridade);

    return parametros.toString();
}

function formatarHoras(horas) {
    if (horas === null || horas === undefined) {
        return '–';
    }
    return `${horas.toFixed(1)} h`;
}

async function carregarIndicadores(query) {
    const resposta = await fetch(`/api/chamados/indicadores?${query}`);
    const dados = await resposta.json();

    document.getElementById('valor-total').textContent = dados.totalChamados;
    document.getElementById('valor-abertos').textContent = dados.totalAbertos;
    document.getElementById('valor-em-andamento').textContent = dados.totalEmAndamento;
    document.getElementById('valor-resolvidos').textContent = dados.totalResolvidos;
    document.getElementById('valor-tempo-atendimento').textContent = formatarHoras(dados.tempoMedioAtendimentoHoras);
    document.getElementById('valor-tempo-resolucao').textContent = formatarHoras(dados.tempoMedioResolucaoHoras);
}

async function carregarGraficoPeriodo(query) {
    const resposta = await fetch(`/api/chamados/por-periodo?${query}`);
    const dados = await resposta.json();

    const rotulos = dados.map(item => item.periodo);
    const abertos = dados.map(item => item.abertos);
    const resolvidos = dados.map(item => item.resolvidos);

    const contexto = document.getElementById('grafico-periodo').getContext('2d');

    if (graficoPeriodo) {
        graficoPeriodo.destroy();
    }

    graficoPeriodo = new Chart(contexto, {
        type: 'line',
        data: {
            labels: rotulos,
            datasets: [
                {
                    label: 'Abertos',
                    data: abertos,
                    borderColor: coresCategoricas[0],
                    backgroundColor: coresCategoricas[0],
                    borderWidth: 2,
                    tension: 0.3,
                    pointRadius: 3
                },
                {
                    label: 'Resolvidos',
                    data: resolvidos,
                    borderColor: coresCategoricas[1],
                    backgroundColor: coresCategoricas[1],
                    borderWidth: 2,
                    tension: 0.3,
                    pointRadius: 3
                }
            ]
        },
        options: {
            responsive: true,
            interaction: {
                mode: 'index',
                intersect: false
            },
            scales: {
                x: {
                    grid: { color: getVariavelCss('--gridline') },
                    ticks: { color: getVariavelCss('--text-muted') }
                },
                y: {
                    beginAtZero: true,
                    grid: { color: getVariavelCss('--gridline') },
                    ticks: { color: getVariavelCss('--text-muted') }
                }
            },
            plugins: {
                legend: {
                    labels: { color: getVariavelCss('--text-secondary') }
                }
            }
        }
    });
}

async function carregarGraficoSituacao(query) {
    const resposta = await fetch(`/api/chamados/situacao?${query}`);
    const dados = await resposta.json();

    const rotulos = dados.map(item => rotulosStatus[item.status]);
    const valores = dados.map(item => item.quantidade);
    const cores = dados.map(item => seletorStatusCores[item.status]);

    const contexto = document.getElementById('grafico-situacao').getContext('2d');

    if (graficoSituacao) {
        graficoSituacao.destroy();
    }

    graficoSituacao = new Chart(contexto, {
        type: 'doughnut',
        data: {
            labels: rotulos,
            datasets: [{
                data: valores,
                backgroundColor: cores,
                borderColor: getVariavelCss('--surface-1'),
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { color: getVariavelCss('--text-secondary') }
                }
            }
        }
    });
}

async function carregarGraficoTipos(query) {
    const resposta = await fetch(`/api/chamados/tipos-mais-atendidos?${query}`);
    const dados = await resposta.json();

    const rotulos = dados.map(item => rotulosTipo[item.tipo]);
    const valores = dados.map(item => item.quantidade);
    const cores = dados.map((_, indice) => coresCategoricas[indice % coresCategoricas.length]);

    const contexto = document.getElementById('grafico-tipos').getContext('2d');

    if (graficoTipos) {
        graficoTipos.destroy();
    }

    graficoTipos = new Chart(contexto, {
        type: 'bar',
        data: {
            labels: rotulos,
            datasets: [{
                label: 'Chamados atendidos',
                data: valores,
                backgroundColor: cores,
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            indexAxis: 'y',
            scales: {
                x: {
                    beginAtZero: true,
                    grid: { color: getVariavelCss('--gridline') },
                    ticks: { color: getVariavelCss('--text-muted') }
                },
                y: {
                    grid: { display: false },
                    ticks: { color: getVariavelCss('--text-primary') }
                }
            },
            plugins: {
                legend: { display: false }
            }
        }
    });
}

async function carregarDashboard() {
    const query = montarQueryString();
    await Promise.all([
        carregarIndicadores(query),
        carregarGraficoPeriodo(query),
        carregarGraficoSituacao(query),
        carregarGraficoTipos(query)
    ]);
}

document.getElementById('botao-filtrar').addEventListener('click', carregarDashboard);
document.addEventListener('DOMContentLoaded', carregarDashboard);
