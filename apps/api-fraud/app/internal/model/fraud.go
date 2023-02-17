package model

type Fraud struct {
	AccountId string `json:"account_id"`
	Allowed   bool   `json:"allowed"`
}
