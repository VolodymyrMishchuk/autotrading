FROM python:3.11-slim
ENV PYTHONDONTWRITEBYTECODE=1 PYTHONUNBUFFERED=1
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends tini && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY app ./app

RUN mkdir -p /data/sessions /data/logs
VOLUME ["/data/sessions", "/data/logs"]

ENV SESSIONS_DIR=/data/sessions \
    KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
    KAFKA_TOPIC=signals.raw

EXPOSE 8000
ENTRYPOINT ["/usr/bin/tini","--"]
CMD ["uvicorn","app.main:app","--host","0.0.0.0","--port","8000"]