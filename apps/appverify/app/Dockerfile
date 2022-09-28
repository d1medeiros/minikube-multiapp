FROM ubuntu

RUN mkdir myapp
WORKDIR /myapp
COPY .. .

RUN chmod +x ./appbin
RUN chmod +x ./run.sh
EXPOSE 3001

CMD ["./run.sh"]


