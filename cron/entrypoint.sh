#!/bin/sh
# cron no les pasa las variables del contenedor a las tareas:
# se guardan en un archivo que tarea.sh lee
env | grep -E '^(DB_USER|DB_PASSWORD|PLANNER_URL|MAIL_|TWILIO_)' \
    | sed "s/^\([^=]*\)=\(.*\)$/export \1='\2'/" > /etc/donatrack.env
chmod 600 /etc/donatrack.env

echo "cron listo, tareas programadas:"
grep -v '^#' /etc/cron.d/donatrack | grep -v '^\s*$'

exec cron -f