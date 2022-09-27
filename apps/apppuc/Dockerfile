FROM ubuntu

RUN mkdir myapp
WORKDIR /myapp
COPY . .

RUN chmod +x ./app
RUN chmod +x ./run.sh
EXPOSE 3000

CMD ["./run.sh"]


