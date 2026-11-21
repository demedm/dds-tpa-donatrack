#!/bin/sh
# uso: tarea <servicio> <clase con main> [argumentos...]
. /etc/donatrack.env

SERVICIO="$1"
CLASE="$2"
shift 2

cd "/app/$SERVICIO" || exit 1
echo "$(date '+%F %T') inicia $CLASE $*"
java -cp "classes:lib/*" "$CLASE" "$@"
CODIGO=$?
echo "$(date '+%F %T') termino $CLASE (codigo $CODIGO)"
exit $CODIGO

