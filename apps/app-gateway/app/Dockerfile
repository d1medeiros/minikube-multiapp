FROM ubuntu



RUN apt-get update \
    && apt-get install -y curl wget gpg unzip

RUN mkdir myapp
WORKDIR /myapp
COPY .. .

RUN chmod +x ./appbin
RUN chmod +x ./run.sh
EXPOSE 3000

CMD ["./run.sh"]


