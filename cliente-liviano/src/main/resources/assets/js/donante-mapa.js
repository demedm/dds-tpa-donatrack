

(function () {
    var contenedor = document.getElementById('mapa');
    if (!contenedor) return;

    var mapa = L.map(contenedor).setView([-34.6037, -58.3816], 11);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap'
    }).addTo(mapa);

    var capaDepositos = L.layerGroup().addTo(mapa);

    function esc(texto) {
        return String(texto == null ? '' : texto).replace(/[&<>"']/g, function (c) {
            return {
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                '"': '&quot;',
                "'": '&#39;'
            }[c];
        });
    }

    async function cargarDepositos() {
        try {
            var respuesta = await fetch(
                contenedor.dataset.url || '/donante/depositos',
                {
                    headers: { 'Accept': 'application/json' },
                    cache: 'no-store'
                }
            );

            if (respuesta.status === 401) {
                window.location.href = '/login';
                return;
            }

            if (!respuesta.ok) {
                throw new Error('HTTP ' + respuesta.status);
            }

            var depositos = await respuesta.json();

            capaDepositos.clearLayers();

            var puntos = [];

            depositos.forEach(function (deposito) {
                var lat = Number(deposito.latitud);
                var lng = Number(deposito.longitud);

                // No crear marcadores si las coordenadas no son válidas.
                if (!Number.isFinite(lat) || !Number.isFinite(lng)) return;
                if (lat < -90 || lat > 90 || lng < -180 || lng > 180) return;

                var punto = [lat, lng];
                puntos.push(punto);

                var icono = L.divIcon({
                    className: 'marcador-deposito-donante',
                    html: deposito.principal ? '🏭' : '📦',
                    iconSize: [32, 32],
                    iconAnchor: [16, 16]
                });

                var popup =
                    '<b>' + esc(deposito.nombre || 'Depósito') + '</b>' +
                    '<br>' + esc(deposito.direccion || 'Sin dirección') +
                    (deposito.principal
                        ? '<br><strong>Depósito principal</strong>'
                        : '');

                L.marker(punto, { icon: icono })
                    .addTo(capaDepositos)
                    .bindPopup(popup);
            });

            if (puntos.length > 0) {
                if (puntos.length === 1) {
                    mapa.setView(puntos[0], 14);
                } else {
                    mapa.fitBounds(puntos, {
                        padding: [35, 35],
                        maxZoom: 14
                    });
                }
            }

            if (depositos.length === 0) {
                console.info('No hay depósitos registrados.');
            }

        } catch (error) {
            console.error('No se pudieron cargar los depósitos:', error);
        }
    }

    cargarDepositos();
})();
