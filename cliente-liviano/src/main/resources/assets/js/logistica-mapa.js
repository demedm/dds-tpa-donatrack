(function () {
    var contenedor = document.getElementById('mapa');
    if (!contenedor) return;

    var mapa = L.map(contenedor).setView([-34.6037, -58.3816], 11);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap'
    }).addTo(mapa);

    var capa = L.layerGroup().addTo(mapa);
    var marcadores = {};
    var primeraCarga = true;
    var hayEntregas = false;

    function $(id) { return document.getElementById(id); }

    function setVal(id, valor) {
        var el = $(id);
        if (el) el.value = valor == null ? '' : valor;
    }

    function esc(s) {
        return String(s == null ? '' : s).replace(/[&<>"']/g, function (c) {
            return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c];
        });
    }

    // ENTREGAS
    async function cargarEntregas() {
        try {
            var resp = await fetch(contenedor.dataset.url, {
                headers: { 'Accept': 'application/json' },
                cache: 'no-store'
            });
            if (resp.status === 401) { window.location.href = '/login'; return; }
            if (!resp.ok) throw new Error('HTTP ' + resp.status);

            var entregas = await resp.json();
            capa.clearLayers();
            marcadores = {};
            var puntos = [];

            entregas.forEach(function (e) {
                if (e.latitudCamion && e.longitudCamion) {
                    var p = [e.latitudCamion, e.longitudCamion];
                    puntos.push(p);
                    L.marker(p).addTo(capa).bindPopup(
                        '<b>Camión en camino</b><br>Entrega #' + esc(e.id) +
                        '<br>Donación #' + esc(e.donacionId) +
                        '<br>Destino: ' + esc(e.direccion)
                    );
                }
            });

            hayEntregas = puntos.length > 0;
            if (primeraCarga && hayEntregas) {
                mapa.fitBounds(puntos, { padding: [30, 30], maxZoom: 14 });
            }
            primeraCarga = false;
            await actualizarUbicaciones();
        } catch (err) {
            console.error('No se pudieron cargar las entregas en curso', err);
        }
    }

    cargarEntregas();
    setInterval(cargarEntregas, 15000);

    // DEPOSITOS
    var depId = null;
    var marcadorDeposito = null;
    var depIcon = L.divIcon({ className: 'marcador-deposito', html: '🏭', iconSize: [30, 30] });

    function ponerDeposito(lat, lng, popup) {
        if (marcadorDeposito) marcadorDeposito.setLatLng([lat, lng]);
        else marcadorDeposito = L.marker([lat, lng], { icon: depIcon, zIndexOffset: 1000 }).addTo(mapa);
        marcadorDeposito.unbindPopup();
        if (popup) marcadorDeposito.bindPopup(popup);
    }

    async function cargarDeposito() {
        try {
            var r = await fetch('/admin/logistica/deposito', {
                headers: { 'Accept': 'application/json' },
                cache: 'no-store'
            });
            if (!r.ok) return;
            var d = await r.json();
            if (!d.configurado) return;

            depId = d.id;
            setVal('depNombre', d.nombre);
            setVal('depDireccion', d.direccion);
            setVal('depLat', d.latitud);
            setVal('depLng', d.longitud);

            ponerDeposito(d.latitud, d.longitud,
                '<b>' + esc(d.nombre || 'Depósito principal') + '</b><br>' + esc(d.direccion));

            // si no hay entregas que encuadrar, centramos en el depósito
            if (!hayEntregas) mapa.setView([d.latitud, d.longitud], 13);
        } catch (e) { console.error('No se pudo cargar el depósito', e); }
    }
    // para poder volver a llamarla si el panel se reemplaza por innerHTML
    window.cargarDeposito = cargarDeposito;

    // clic en el mapa → completa coordenadas (solo si el panel está abierto)
    mapa.on('click', function (ev) {
        var box = $('depositoBox');
        if (!box || !box.open) return;
        setVal('depLat', ev.latlng.lat.toFixed(6));
        setVal('depLng', ev.latlng.lng.toFixed(6));
        ponerDeposito(ev.latlng.lat, ev.latlng.lng);
    });

    // delegación: sobrevive a que el panel se reemplace por innerHTML
    document.addEventListener('click', async function (ev) {
        if (ev.target.closest('#depBuscar')) {
            var msg = $('depMsg');
            var dir = $('depDireccion');
            if (!dir || !msg) return;
            var q = dir.value.trim();
            if (!q) return;
            try {
                var r = await fetch('https://nominatim.openstreetmap.org/search?format=json&limit=1&countrycodes=ar&q='
                    + encodeURIComponent(q));
                var res = await r.json();
                if (!res.length) { msg.textContent = 'No se encontró la dirección.'; return; }
                var lat = parseFloat(res[0].lat), lng = parseFloat(res[0].lon);
                setVal('depLat', lat.toFixed(6));
                setVal('depLng', lng.toFixed(6));
                ponerDeposito(lat, lng);
                mapa.setView([lat, lng], 15);
                msg.textContent = 'Revisá que el punto sea correcto y guardá.';
            } catch (e) {
                msg.textContent = 'No se pudo buscar la dirección.';
            }
        }

        if (ev.target.closest('#depGuardar')) {
            var msg2 = $('depMsg');
            if (!$('depNombre') || !msg2) return;
            var body = {
                id: depId,
                nombre: $('depNombre').value.trim(),
                direccion: $('depDireccion').value.trim(),
                latitud: parseFloat($('depLat').value),
                longitud: parseFloat($('depLng').value),
                principal: true
            };
            if (!body.nombre || !body.direccion) { msg2.textContent = 'Completá nombre y dirección.'; return; }
            if (isNaN(body.latitud) || isNaN(body.longitud)) { msg2.textContent = 'Faltan las coordenadas.'; return; }
            try {
                var r2 = await fetch('/admin/logistica/deposito', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
                    body: JSON.stringify(body)
                });
                if (r2.status === 401) { window.location.href = '/login'; return; }
                if (r2.status === 403) { msg2.textContent = 'No tenés permisos para guardar.'; return; }
                if (!r2.ok) throw new Error('HTTP ' + r2.status);
                var saved = await r2.json();
                depId = saved.id;           // la próxima vez actualiza en vez de crear
                msg2.textContent = 'Depósito guardado.';
                ponerDeposito(body.latitud, body.longitud,
                    '<b>' + esc(body.nombre) + '</b><br>' + esc(body.direccion));
            } catch (e) {
                msg2.textContent = 'No se pudo guardar.';
            }
        }
    });

    cargarDeposito();

    async function actualizarUbicaciones() {
        try {
            var r = await fetch('/admin/logistica/ubicaciones', {
                headers: { 'Accept': 'application/json' },
                cache: 'no-store'
            });
            if (r.status === 401) { window.location.href = '/login'; return; }
            if (!r.ok) return;
            (await r.json()).forEach(function (u) {
                (marcadores[u.patente] || []).forEach(function (m) {
                    m.setLatLng([u.latitud, u.longitud]);
                });
            });
        } catch (e) {
            console.error('No se pudieron actualizar las ubicaciones', e);
        }
    }
    setInterval(actualizarUbicaciones, 5000);
})();