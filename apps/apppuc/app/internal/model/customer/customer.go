package customer

type Customer struct {
	Id       string`json:"id"`
	Eligible bool`json:"eligible"`
	Limit    float64`json:"limit"`
}
