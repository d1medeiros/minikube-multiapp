FROM ubuntu



RUN apt-get update \
    && apt-get install -y curl wget gpg unzip

RUN curl -O -L "https://github.com/grafana/agent/releases/download/v0.31.3/grafana-agentctl-linux-amd64.zip" && unzip "grafana-agentctl-linux-amd64.zip" && chmod a+x "grafana-agentctl-linux-amd64" && ./grafana-agentctl-linux-amd64


RUN mkdir myapp
WORKDIR /myapp
COPY .. .

RUN chmod +x ./appbin
RUN chmod +x ./run.sh
EXPOSE 3000

CMD ["./run.sh"]


